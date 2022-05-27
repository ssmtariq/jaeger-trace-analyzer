package com.ssmtariq.srlab.jtanalyzer;

import com.ssmtariq.srlab.jtanalyzer.model.Node;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import static com.ssmtariq.srlab.jtanalyzer.Constants.*;

public class GenericTree {
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
        final int[] counter = new int[NUMBER_OF_SERVICE_COUNT];
        Map<String, Integer> spanCounter = new HashMap<>();
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(node);
        while (queue.size()>0){
            node = queue.remove();
//            System.out.println(Utility.getServiceNameShort(node.getServiceName()) + " ");
            calculateServiceSpanCount(spanCounter, node);

            for (Node child: node.getChildren()){
                queue.add(child);
            }
            counter[0]++;
        }
        System.out.println(".");
        System.out.println("Total Spans - "+ counter[0]);
        displayServiceSpanCount(spanCounter);
    }

    public static void calculateServiceSpanCount(Map<String, Integer> spanCounter, Node node){
        Integer i = 0;
        switch(node.getServiceName()) {
            case SERVICE_PRESERVE_OTHER:
                i = spanCounter.get(SERVICE_PRESERVE_OTHER);
                spanCounter.put(SERVICE_PRESERVE_OTHER, i==null?1:++i);
                break;
            case SERVICE_TRAVEL2:
                i = spanCounter.get(SERVICE_TRAVEL2);
                spanCounter.put(SERVICE_TRAVEL2, i==null?1:++i);
                break;
            case SERVICE_TICKET_INFO:
                i = spanCounter.get(SERVICE_TICKET_INFO);
                spanCounter.put(SERVICE_TICKET_INFO, i==null?1:++i);
                break;
            case SERVICE_BASIC:
                i = spanCounter.get(SERVICE_BASIC);
                spanCounter.put(SERVICE_BASIC, i==null?1:++i);
                break;
            case SERVICE_PRICE:
                i = spanCounter.get(SERVICE_PRICE);
                spanCounter.put(SERVICE_PRICE, i==null?1:++i);
                break;
            case SERVICE_SEAT:
                i = spanCounter.get(SERVICE_SEAT);
                spanCounter.put(SERVICE_SEAT, i==null?1:++i);
                break;
            case SERVICE_CONFIG:
                i = spanCounter.get(SERVICE_CONFIG);
                spanCounter.put(SERVICE_CONFIG, i==null?1:++i);
                break;
            case SERVICE_ORDER_OTHER:
                i = spanCounter.get(SERVICE_ORDER_OTHER);
                spanCounter.put(SERVICE_ORDER_OTHER, i==null?1:++i);
                break;
            case SERVICE_STATION:
                i = spanCounter.get(SERVICE_STATION);
                spanCounter.put(SERVICE_STATION, i==null?1:++i);
                break;
            case SERVICE_TRAIN:
                i = spanCounter.get(SERVICE_TRAIN);
                spanCounter.put(SERVICE_TRAIN, i==null?1:++i);
                break;
            case SERVICE_ROUTE:
                i = spanCounter.get(SERVICE_ROUTE);
                spanCounter.put(SERVICE_ROUTE, i==null?1:++i);
                break;
            case SERVICE_CONTACTS:
                i = spanCounter.get(SERVICE_CONTACTS);
                spanCounter.put(SERVICE_CONTACTS, i==null?1:++i);
                break;
            case SERVICE_ORDER:
                i = spanCounter.get(SERVICE_ORDER);
                spanCounter.put(SERVICE_ORDER, i==null?1:++i);
                break;
            case SERVICE_SECURITY:
                i = spanCounter.get(SERVICE_SECURITY);
                spanCounter.put(SERVICE_SECURITY, i==null?1:++i);
                break;
            case SERVICE_USER:
                i = spanCounter.get(SERVICE_USER);
                spanCounter.put(SERVICE_USER, i==null?1:++i);
                break;
            case SERVICE_NOTIFICATION:
                i = spanCounter.get(SERVICE_NOTIFICATION);
                spanCounter.put(SERVICE_NOTIFICATION, i==null?1:++i);
                break;
            case SERVICE_FOOD:
                i = spanCounter.get(SERVICE_FOOD);
                spanCounter.put(SERVICE_FOOD, i==null?1:++i);
                break;
            default:
                System.out.println(node.getServiceName());
        }
    }

    public static void displayServiceSpanCount(Map<String, Integer> spanCounter){
        System.out.println("## Displaying Servicewise span counts ##");
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
