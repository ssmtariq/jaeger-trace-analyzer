package com.ssmtariq.srlab.jtanalyzer;

import com.ssmtariq.srlab.jtanalyzer.model.Node;

public class GenericTree {
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
}
