package com.ssmtariq.srlab.jtanalyzer;

import com.ssmtariq.srlab.jtanalyzer.model.Node;

import java.util.*;

import static com.ssmtariq.srlab.jtanalyzer.Constants.*;

public class GenericTree {
    private static Integer NUMBER_OF_SERVICE_INVOLVED = 0;
    private static final int[] requestCounter = {0};
    private static Map<String, Long> aggregatorMap = new HashMap<>();

    /**
     * Euler traversal
     *
     * @param node
     */
    public static void display(Node node) {
        String str = Utility.getServiceNameShort(node.getServiceName()) + "[" + node.getOperationName() + "]" + "(" + node.getSpanId() + ")" + "=" + node.getDuration() + "ms" + " --> ";
        if (node.getChildren().size() > 0) str += "\n\t";

        for (Node child : node.getChildren()) {
            str += Utility.getServiceNameShort(node.getServiceName()) + "(" + child.getSpanId() + ")" + ", ";
        }
        str += ".";
        System.out.println(str);

        for (Node child : node.getChildren()) {
            display(child);
        }
    }

    static void LevelOrderTraversal(Node root) {
        if (root == null) return;
        Queue<Node> queue = new LinkedList<>(); // Create a queue
        queue.add(root); // Enqueue root
        while (!queue.isEmpty()) {
            int size = queue.size();
            // If this node has children
            while (size > 0) {
                Node node = queue.peek();
                queue.remove();
//                System.out.print(parent.getServiceName() + " ");
                System.out.print(Utility.getServiceNameShort(node.getServiceName()) + "(" + node.getOperationName() + ")" + " ");
                // Enqueue all children of
                // the dequeued item
                for (Node child : node.getChildren()) {
                    queue.add(child);
                }
                size--;
            }
            // Print new line between two levels
            System.out.println("\n");
        }
    }

    public static void levelOrder(Node node) {
        final int[] counter = {0};
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(node);
        while (queue.size() > 0) {
            node = queue.remove();
            System.out.println(Utility.getServiceNameShort(node.getServiceName()) + "(" + node.getOperationName() + ")" + " ");

            for (Node child : node.getChildren()) {
                queue.add(child);
            }
            counter[0]++;
        }
        System.out.println(".");
    }

    public static void displaySpanSummary(Node node) {
        Node root = node;
        final int[] spanCounter = {0};
        Map<String, Integer> spanCollector = new HashMap<>();
        Map<String, Double> serviceDuration = new HashMap<>();
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(node);
        while (queue.size() > 0) {
            node = queue.remove();
            calculateServiceSpanCount(spanCollector, node);
            calculateServiceDuration(serviceDuration, node);
            aggregateResult(serviceDuration);
            for (Node child : node.getChildren()) {
                queue.add(child);
            }
            spanCounter[0]++;
        }
        NUMBER_OF_SERVICE_INVOLVED = spanCollector.entrySet().size();
        if (NUMBER_OF_SERVICE_INVOLVED.equals(NUMBER_OF_SERVICE_COUNT)) {
            requestCounter[0]++;
            System.out.println("####################");
            System.out.println("#Total Spans - " + spanCounter[0] + "#");
            System.out.println("####################");
            displayServiceAggregatedInfo(spanCollector, serviceDuration);
            LevelOrderTraversal(root);
            System.out.println("\n");
        }
    }

    public static void calculateServiceSpanCount(Map<String, Integer> spanCounter, Node node) {
        Integer i = 0;
        if (spanCounter.containsKey(node.getServiceName())) {
            i = spanCounter.get(node.getServiceName());
            spanCounter.put(node.getServiceName(), ++i);
        } else {
            spanCounter.put(node.getServiceName(), 1);
        }
    }

    public static void displayServiceAggregatedInfo(Map<String, Integer> spanCounter, Map<String, Double> serviceDuration) {
        System.out.printf("%-26s %-6s %-15s \n", "SERVICE", "SPAN", "DURATION(seconds)");
        System.out.println("---------------------------------------------------");
        final int[] counter = {0};
        spanCounter.forEach((k, v) -> {
            if (v > 0) {
                counter[0]++;
                System.out.printf("|%-25s |%-5s |%-15s| \n", k, v, (serviceDuration.get(k) / 1000000));
            }
        });
        System.out.println();
    }

    public static void calculateServiceDuration(Map<String, Double> serviceDuration, Node node) {
        if (!serviceDuration.containsKey(node.getServiceName())) {
            serviceDuration.put(node.getServiceName(), (double) node.getDuration());
        }
    }

    public static void aggregateResult(Map<String, Double> serviceDuration) {
        serviceDuration.forEach((k,v)->{
            if (aggregatorMap.containsKey(k)) {
                aggregatorMap.put(k, (v.intValue()+aggregatorMap.get(k)));
            } else {
                aggregatorMap.put(k, (long) v.intValue());
            }
        });
    }

    public static void displayAggregatedResult() {
        System.out.println("***********Latency Calculation Results*************");
        System.out.println("TOTAL NUMBER OF REQUESTS: "+requestCounter[0]);
        System.out.printf("%-26s %-15s \n", "SERVICE", "AVERAGE LATENCY(seconds)");
        System.out.println("---------------------------------------------------");
        final int[] counter = {0};
        aggregatorMap.forEach((k, v) -> {
            if (v > 0) {
                counter[0]++;
                System.out.printf("|%-25s |%-15s| \n", k, v, (aggregatorMap.get(k) / requestCounter[0]));
            }
        });
        System.out.println();
    }
}
