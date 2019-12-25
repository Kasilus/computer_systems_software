package edu.stanislav.computer_systems_software.lab3;

import java.util.*;

public class DataFlowParallelModel implements ParallelModel {

    public Set<DataFlowNode> finishedTasks = new HashSet<>();
    public Set<DataFlowNode> readyTasks = new HashSet<>();
    public Set<DataFlowNode> otherTasks = new HashSet<>();

    private int processors;
    private Map<String, Integer> operationDurability;

    private StringBuilder modelOutput;
    private int lengthOfOutputForEachProcessor = 20;
    // we can run only 1 RunningTask on Processor at every period of time
    private Map<Integer, RunningTask> currentTasks = new HashMap<>();

    public DataFlowParallelModel(int processors, Map<String, Integer> operationDurability) {
        this.processors = processors;
        this.operationDurability = operationDurability;
        initModelOutput();
    }

    @Override
    public void run(GraphNode graphNode) {
        if (!(graphNode instanceof DataFlowNode)) {
            return;
        }
        DataFlowNode dataFlowNode = (DataFlowNode) graphNode;
        // init queues
        System.out.println("Init queues");
        initTasksToQueues(dataFlowNode);
        printQueues();
        System.out.println("\n" + modelOutput);

        while ((!readyTasks.isEmpty() || !otherTasks.isEmpty())) {
            // at first try sequential algorithm
            int emptyProcessor;
            while ((emptyProcessor = getEmptyProcessor()) != 0 && !readyTasks.isEmpty()) {
                DataFlowNode readyTask = readyTasks.iterator().next();
                currentTasks.put(emptyProcessor, new RunningTask(readyTask, operationDurability.get(readyTask.getValue())));
                readyTasks.remove(readyTask);
            }

            // do a tact
            modelOutput.append("\n");
            modelOutput.append("|");
            for (int i = 1; i <= processors; i++) {
                RunningTask currentTask = currentTasks.get(i);
                if (currentTask != null) {
                    currentTask.completeTact();
                    modelOutput.append(getOutputRowPartForProcessor(currentTask.toString()));
                    if (currentTask.getTactsLast() == 0) {
                        finishedTasks.add(currentTask.getTask());
                        currentTasks.put(i, null);
                    }
                } else {
                    modelOutput.append(getOutputRowPartForProcessor(""));
                }
                modelOutput.append("|");
            }

            // move other tasks to ready queues
            checkReadyTasks(dataFlowNode);

            System.out.println(modelOutput);
            printQueues();
        }
    }

    private int getEmptyProcessor() {
        for (int i = 1; i <= processors; i++) {
            if (currentTasks.get((i)) == null) {
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
        modelOutput = new StringBuilder();
        modelOutput.append("|");
        for (int i = 0; i < processors; i++) {
            modelOutput.append(getOutputRowPartForProcessor("P" + (i + 1)));
            modelOutput.append("|");
        }
        modelOutput.append("\n");
        for (int i = 0; i < processors * lengthOfOutputForEachProcessor + (processors + 1); i++) {
            modelOutput.append("=");
        }
        modelOutput.append("\n");
    }

    private StringBuilder getOutputRowPartForProcessor(String input) {
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
        return output;
    }

    private class RunningTask {
        private DataFlowNode task;
        private int tactsLast;

        public RunningTask(DataFlowNode task, int tactsLast) {
            this.task = task;
            this.tactsLast = tactsLast;
        }

        public DataFlowNode getTask() {
            return task;
        }

        public int getTactsLast() {
            return tactsLast;
        }

        public void completeTact() {
            tactsLast--;
        }

        @Override
        public String toString() {
            return this.task.toString();
        }
    }
}
