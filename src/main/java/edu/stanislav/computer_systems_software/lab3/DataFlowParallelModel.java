package edu.stanislav.computer_systems_software.lab3;

import java.util.*;

public class DataFlowParallelModel implements ParallelModel {

    public Set<DataFlowNode> finishedTasks = new HashSet<>();
    public Set<DataFlowNode> readyTasks = new HashSet<>();
    public Set<DataFlowNode> otherTasks = new HashSet<>();

    private int processors;
    private Map<String, Integer> operationDurability;
    private int memoryBanks;
    private int totalTacts;

    private Map<Integer, List<String>> processorsHistory = new HashMap<>();
    private Map<Integer, List<String>> memoryBanksHistory = new HashMap<>();
    private int lengthOfOutputForEachProcessor = 20;
    // we can run only 1 RunningTask on Processor at every period of time
    private Map<Integer, RunningTask> currentTasks = new HashMap<>();

    public DataFlowParallelModel(int processors, Map<String, Integer> operationDurability, int memoryBanks) {
        this.processors = processors;
        for (int i = 1; i <= processors; i++) {
            processorsHistory.put(i, new ArrayList<>(Arrays.asList(new String[1000])));
        }
        this.operationDurability = operationDurability;
        this.memoryBanks = memoryBanks;
        for (int i = 1; i <= memoryBanks; i++) {
            memoryBanksHistory.put(i, new ArrayList<>(Arrays.asList(new String[1000])));
        }
    }

    @Override
    public void run(GraphNode graphNode) {
        if (!(graphNode instanceof DataFlowNode)) {
            return;
        }
        DataFlowNode dataFlowNode = (DataFlowNode) graphNode;
        initTasksToQueues(dataFlowNode);

        int currentTact = 0;
        while (!readyTasks.isEmpty() || !otherTasks.isEmpty() || isProcessingTask()) {
            int emptyProcessor;
            while ((emptyProcessor = getEmptyProcessor(currentTact)) != 0 && !readyTasks.isEmpty()) {
                DataFlowNode readyTask = getTaskWithMaxWeight(readyTasks);
                int tactToRead = currentTact;
                List<String> emptyProcessorHistory = processorsHistory.get(emptyProcessor);
                int emptyMemoryBank;
                while ((emptyMemoryBank = getEmptyMemoryBank(tactToRead)) == 0) {
                    emptyProcessorHistory.remove(tactToRead);
                    emptyProcessorHistory.add(tactToRead, getOutputRowPartForProcessor(""));
                    tactToRead++;
                }
                List<String> emptyMemoryBankHistory = memoryBanksHistory.get(emptyMemoryBank);
                emptyProcessorHistory.remove(tactToRead);
                emptyProcessorHistory.add(tactToRead, getOutputRowPartForProcessor("R [" + readyTask.toString() + "]"));
                emptyMemoryBankHistory.remove(tactToRead);
                emptyMemoryBankHistory.add(tactToRead, getOutputRowPartForProcessor("R [" + readyTask.toString() + "]"));
                for (int i = 0; i < operationDurability.get(readyTask.getValue()); i++) {
                    emptyProcessorHistory.add(tactToRead + (i + 1), getOutputRowPartForProcessor(readyTask.toString()));
                }
                int tactToWrite = tactToRead + operationDurability.get(readyTask.getValue()) + 1;
                while (getEmptyMemoryBank(tactToWrite) == 0) {
                    emptyProcessorHistory.remove(tactToWrite);
                    emptyProcessorHistory.add(tactToWrite, getOutputRowPartForProcessor(""));
                    tactToWrite++;
                }
                emptyProcessorHistory.remove(tactToWrite);
                emptyProcessorHistory.add(tactToWrite, getOutputRowPartForProcessor("W [" + readyTask.toString() + "]"));
                emptyMemoryBankHistory.remove(tactToWrite);
                emptyMemoryBankHistory.add(tactToWrite, getOutputRowPartForProcessor("W [" + readyTask.toString() + "]"));
                currentTasks.put(emptyProcessor, new RunningTask(readyTask));
                readyTasks.remove(readyTask);
            }

            // apply tasks to finished, if write is happened
            for (int i = 1; i <= processors; i++) {
                String currentTactRow = processorsHistory.get(i).get(currentTact);
                if (currentTactRow != null && currentTactRow.contains("W [")) {
                    finishedTasks.add(currentTasks.get(i).getTask());
                    currentTasks.put(i, null);
                }
            }

            // move other tasks to ready queues
            checkReadyTasks(dataFlowNode);
            currentTact++;
        }

        totalTacts = currentTact;

    }

    private boolean isProcessingTask() {
        for (RunningTask currentTask: currentTasks.values()) {
            if (currentTask != null) {
                return true;
            }
        }
        return false;
    }


    private DataFlowNode getTaskWithMaxWeight(Set<DataFlowNode> readyTasks) {
        int maxWeight = 0;
        DataFlowNode taskWithMaxWeight = null;
        for (DataFlowNode readyTask: readyTasks) {
            if (readyTask.getWeight() > maxWeight) {
                maxWeight = readyTask.getWeight();
                taskWithMaxWeight = readyTask;
            }
        }
        return taskWithMaxWeight;
    }

    @Override
    public void printModel() {
        StringBuilder modelOutput = new StringBuilder();
        modelOutput.append("|");
        for (int i = 0; i < processors; i++) {
            modelOutput.append(getOutputRowPartForProcessor("P" + (i + 1)));
            modelOutput.append("|");
        }
        modelOutput.append(getOutputRowPartForProcessor(""));
        modelOutput.append("|");
        for (int i = 0; i < memoryBanks; i++) {
            modelOutput.append(getOutputRowPartForProcessor("MB" + (i + 1)));
            modelOutput.append("|");
        }
        modelOutput.append("\n");
        for (int i = 0; i < processors * lengthOfOutputForEachProcessor + (processors + 1); i++) {
            modelOutput.append("=");
        }
        modelOutput.append(getOutputRowPartForProcessor(""));
        for (int i = 0; i < memoryBanks * lengthOfOutputForEachProcessor + (memoryBanks + 1); i++) {
            modelOutput.append("=");
        }
        modelOutput.append("\n");

        for (int i = 0; i < totalTacts; i++) {
            modelOutput.append("|");
            for (int j = 1; j <= processors; j++) {
                String currentProcessorHistoryRaw = processorsHistory.get(j).get(i);
                if (currentProcessorHistoryRaw == null) {
                    modelOutput.append(getOutputRowPartForProcessor(""));
                } else {
                    modelOutput.append(currentProcessorHistoryRaw);
                }
                modelOutput.append("|");
            }
            modelOutput.append(getOutputRowPartForProcessor(""));
            modelOutput.append("|");
            for (int j = 1; j <= memoryBanks; j++) {
                String currentMemoryBankHistoryRaw = memoryBanksHistory.get(j).get(i);
                if (currentMemoryBankHistoryRaw == null) {
                    modelOutput.append(getOutputRowPartForProcessor(""));
                } else {
                    modelOutput.append(currentMemoryBankHistoryRaw);
                }
                modelOutput.append("|");
            }
            modelOutput.append("\n");
        }

        for (int i = 0; i < processors * lengthOfOutputForEachProcessor + (processors + 1); i++) {
            modelOutput.append("=");
        }
        modelOutput.append(getOutputRowPartForProcessor(""));
        for (int i = 0; i < memoryBanks * lengthOfOutputForEachProcessor + (memoryBanks + 1); i++) {
            modelOutput.append("=");
        }
        System.out.println(modelOutput);
    }

    private int getEmptyProcessor(int currentTact) {
        for (int i = 1; i <= processors; i++) {
            if (processorsHistory.get(i).get(currentTact) == null) {
                return i;
            }
        }
        return 0;
    }

    private int getEmptyMemoryBank(int currentTact) {
        for (int i = 1; i <= memoryBanks; i++) {
            if (memoryBanksHistory.get(i).get(currentTact) == null) {
                return i;
            }
        }
        return 0;
    }

    private void initTasksToQueues(DataFlowNode dataFlowNode) {
        if (dataFlowNode.getLeftChild() != null) {
            initTasksToQueues(dataFlowNode.getLeftChild());
        }
        if (dataFlowNode.getRightChild() != null) {
            initTasksToQueues(dataFlowNode.getRightChild());
        }
        if (dataFlowNode.getLeftChild() == null && dataFlowNode.getRightChild() == null) {
            finishedTasks.add(dataFlowNode);
        }
        if (finishedTasks.contains(dataFlowNode.getLeftChild()) && finishedTasks.contains(dataFlowNode.getRightChild())) {
            readyTasks.add(dataFlowNode);
        }
        if (!finishedTasks.contains(dataFlowNode) && !readyTasks.contains(dataFlowNode)) {
            otherTasks.add(dataFlowNode);
        }
    }

    private void checkReadyTasks(DataFlowNode dataFlowNode) {
        if (!otherTasks.contains(dataFlowNode)) {
            return;
        }

        if ((finishedTasks.contains(dataFlowNode.getLeftChild()) && finishedTasks.contains(dataFlowNode.getRightChild()))) {
            readyTasks.add(dataFlowNode);
            otherTasks.remove(dataFlowNode);
            return;
        }

        if (dataFlowNode.getLeftChild() != null) {
            checkReadyTasks(dataFlowNode.getLeftChild());
        }
        if (dataFlowNode.getRightChild() != null) {
            checkReadyTasks(dataFlowNode.getRightChild());
        }
    }

    private void printQueues() {
        System.out.println("Finished tasks");
        System.out.println(Arrays.toString(finishedTasks.toArray()));
        System.out.println("Ready tasks");
        System.out.println(Arrays.toString(readyTasks.toArray()));
        System.out.println("Other tasks");
        System.out.println(Arrays.toString(otherTasks.toArray()));
    }

    private void initModelOutput() {

    }

    private String getOutputRowPartForProcessor(String input) {
        int inputLength = input.length();
        int startIndex = (lengthOfOutputForEachProcessor - inputLength) / 2;
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < startIndex; i++) {
            output.append(" ");
        }
        output.append(input);
        for (int i = output.length(); i < lengthOfOutputForEachProcessor; i++) {
            output.append(" ");
        }
        return output.toString();
    }

    public int getTotalTacts() {
        return totalTacts;
    }

    private class RunningTask {
        private DataFlowNode task;

        public RunningTask(DataFlowNode task) {
            this.task = task;
        }

        public DataFlowNode getTask() {
            return task;
        }

        @Override
        public String toString() {
            return this.task.toString();
        }
    }
}
