package com.ssmtariq.srlab.jtanalyzer;

import com.ssmtariq.srlab.jtanalyzer.model.Node;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import static com.ssmtariq.srlab.jtanalyzer.Constants.*;

public class GenericTree {
    private static Integer NUMBER_OF_SERVICE_INVOLVED = 0;
    /**
     * Euler traversal
     * @param node
     */
    public static void display(Node node){
        String str = Utility.getServiceNameShort(node.getServiceName())+"["+node.getOperationName() +"]" +"("+node.getSpanId() +")" +"=" +node.getDuration()+"ms" + " --> ";
        if(node.getChildren().size()>0) str += "\n\t";

        for (Node child: node.getChildren()){
            str += Utility.getServiceNameShort(node.getServiceName())+"("+child.getSpanId() +")"+ ", ";
        }
        str += ".";
        System.out.println(str);

        for (Node child: node.getChildren()){
            display(child);
        }
    }

    public static void levelOrder(Node node){
        final int[] counter = {0};
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(node);
        while (queue.size()>0){
            node = queue.remove();
            System.out.println(Utility.getServiceNameShort(node.getServiceName()) + " ");

            for (Node child: node.getChildren()){
                queue.add(child);
            }
            counter[0]++;
        }
        System.out.println(".");
    }

    public static void displaySpanSummary(Node node){
        final int[] counter = {0};
        Map<String, Integer> spanCounter = new HashMap<>();
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(node);
        while (queue.size()>0){
            node = queue.remove();
            calculateServiceSpanCount(spanCounter, node);

            for (Node child: node.getChildren()){
                queue.add(child);
            }
            counter[0]++;
        }
        NUMBER_OF_SERVICE_INVOLVED = spanCounter.entrySet().size();
        if(NUMBER_OF_SERVICE_INVOLVED.equals(NUMBER_OF_SERVICE_COUNT)){
            System.out.println("Total Spans - "+ counter[0]);
            displayServiceSpanCount(spanCounter);
        }
    }

    public static void calculateServiceSpanCount(Map<String, Integer> spanCounter, Node node){
        Integer i = 0;
        if(spanCounter.containsKey(node.getServiceName())){
            i = spanCounter.get(node.getServiceName());
            spanCounter.put(node.getServiceName(), ++i);
        }else {
            spanCounter.put(node.getServiceName(), 1);
        }
    }

    public static void displayServiceSpanCount(Map<String, Integer> spanCounter){
        System.out.println("## Displaying service wise span counts ##");
        final int[] counter = {0};
        spanCounter.forEach((k,v)->{
            if(v>0){
                counter[0]++;
                System.out.printf(k + " (" + v +")\t");
                if(counter[0]%5==0) System.out.println();
            }
        });
        System.out.println();
    }
}
