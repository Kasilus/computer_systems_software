package edu.stanislav.computer_systems_software.lab3;

import java.util.*;

public class DataFlowParallelModel implements ParallelModel {

    public Set<DataFlowNode> finishedTasks = new HashSet<>();
    public Set<DataFlowNode> readyTasks = new HashSet<>();
    public Set<DataFlowNode> otherTasks = new HashSet<>();

    private int processors;
    private Map<String, Integer> operationDurability;

    private StringBuilder modelOutput = new StringBuilder();
    // we can run only 1 RunningTask on Processor at every period of time
    private Map<Integer, RunningTask> currentTasks = new HashMap<>();

    public DataFlowParallelModel(int processors, Map<String, Integer> operationDurability) {
        this.processors = processors;
        this.operationDurability = operationDurability;
    }

    @Override
    public void run(GraphNode graphNode) {
        if (!(graphNode instanceof DataFlowNode)) {
            return;
        }
        DataFlowNode dataFlowNode = (DataFlowNode) graphNode;
        // init queues
        System.out.println("Init queues");
        moveTasksToQueues(dataFlowNode);
        printQueues();

        StringBuilder processing = new StringBuilder();
        System.out.println("|    P1    |    P2    |");
        System.out.println("=======================");
        System.out.println("|    a(8)  |    a(8)  |");
        System.out.println("|    a(8)  |    a(8)  |");
        System.out.println("|    a(8)  |    a(8)  |");
        System.out.println("|    a(8)  |    a(8)  |");

        while (!readyTasks.isEmpty() && !otherTasks.isEmpty()) {
            return;
        }
    }

    private void moveTasksToQueues(DataFlowNode dataFlowNode) {
        if (dataFlowNode.getLeftChild() != null) {
            moveTasksToQueues(dataFlowNode.getLeftChild());
        }
        if (dataFlowNode.getRightChild() != null) {
            moveTasksToQueues(dataFlowNode.getRightChild());
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

    private void printQueues() {
        System.out.println("Finished tasks");
        System.out.println(Arrays.toString(finishedTasks.toArray()));
        System.out.println("Ready tasks");
        System.out.println(Arrays.toString(readyTasks.toArray()));
        System.out.println("Other tasks");
        System.out.println(Arrays.toString(otherTasks.toArray()));
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
    }
}
