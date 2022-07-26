package com.ssmtariq.srlab.jtanalyzer;

import com.ssmtariq.srlab.jtanalyzer.model.Node;

import java.util.*;
import static com.ssmtariq.srlab.jtanalyzer.Constants.SUCCESS_REQUESTS_TO_RETRIEVE;

public class MergedTree {
    private static final int[] requestCounter = {0};
    private static Map<String, Double> averageLatencyMap = new HashMap<>();
    private static Map<String, Long> totalRequestToRPCMap = new HashMap<>();

    public static void generate(Map<String, Node> rawTrees, List<String> roots){
        /* Display the tree info */
        for (String root: roots) {
            GenericTree.collectSuccessRequestRoots(rawTrees.get(root));
        }
        List<String> completedRequestRoots = GenericTree.getSuccessRequestRoots();
        requestCounter[0] = completedRequestRoots.size();
        int reqNum = 0;
        for(String root: completedRequestRoots){
            constructMergedTree(rawTrees, rawTrees.get(root));
            reqNum++;
            if(SUCCESS_REQUESTS_TO_RETRIEVE>0 && reqNum==SUCCESS_REQUESTS_TO_RETRIEVE){
                requestCounter[0] = SUCCESS_REQUESTS_TO_RETRIEVE;
                break;
            }
        };
    }

    public static void constructMergedTree(Map<String, Node> rawTrees, Node root) {
        if (root == null) return;
        Queue<Node> queue = new LinkedList<>(); // Create a queue
        queue.add(root); // Enqueue root
        while (!queue.isEmpty()) {
            int size = queue.size();
            // If this node has children
            while (size > 0) {
                Node node = queue.peek();
                queue.remove();
                String path = getNodePathFromRoot(root, node.getSpanId());

                Double latency = Double.valueOf(node.getDuration());
                if(averageLatencyMap.containsKey(path)){
                    latency += averageLatencyMap.get(path);
                }
                averageLatencyMap.put(path, latency);

                Long requestCount = 1L;
                if(totalRequestToRPCMap.containsKey(path)){
                    requestCount += totalRequestToRPCMap.get(path);
                }
                totalRequestToRPCMap.put(path, requestCount);
                // Enqueue all children of then dequeued item
                if (node.getChildren().size() > 0) queue.addAll(node.getChildren());
                size--;
            }
        }
    }

    public static List<String> nodeToRootPath(Node node, String spanId){
        if (node.getSpanId().equals(spanId)){
            List<String> nodeList = new ArrayList<>();
            nodeList.add(Utility.getServiceNameShort(node.getServiceName())+"["+node.getOperationName()+"]");
            return nodeList;
        }
        for (Node child: node.getChildren()){
            List<String> pathTillChild = nodeToRootPath(child, spanId);
            if (pathTillChild.size()>0){
                pathTillChild.add(Utility.getServiceNameShort(node.getServiceName())+"["+node.getOperationName()+"]");
                return pathTillChild;
            }
        }
        return new ArrayList<>();
    }

    public static String getNodePathFromRoot(Node root, String spanId){
        StringBuilder path = new StringBuilder();
        List<String> pathNodes = nodeToRootPath(root, spanId);
        //Get Root to Node path from Node to Root
        Collections.reverse(pathNodes);
        for (String edge: pathNodes){
            path.append(edge);
            path.append("->");
        }
        return path.substring(0, (path.length()-2));
    }

    public static void displayAggregatedResult() {
        averageLatencyMap = Utility.sortByValue(averageLatencyMap);
        System.out.println("================= Latency Calculation Results =================");
        System.out.println("TOTAL NUMBER OF REQUESTS FOUND: "+requestCounter[0]);
        if (averageLatencyMap.keySet().size()==totalRequestToRPCMap.keySet().size()){
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-22s  %-11s %-26s  \n", "AVERAGE LATENCY(seconds)", "TOTAL SPAN", "REMOTE PROCEDURE");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
            averageLatencyMap.forEach((k, v) -> {
                if (v > 0) {
                    System.out.printf("|%-24s |%-10s |%-25s \n",((v/1000000) / requestCounter[0]), totalRequestToRPCMap.get(k), k);
                }
            });
            System.out.println();
        }
    }
}
