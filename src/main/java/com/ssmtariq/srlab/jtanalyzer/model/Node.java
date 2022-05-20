package com.ssmtariq.srlab.jtanalyzer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {
    private String spanId;
    private String serviceName;
    private String parentId;
    private String operationName;
    private long duration;
    private List<Node> children = new ArrayList<>();

    public Node(String spanId, long duration){
        this.spanId = spanId;
        this.duration = duration;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChildren(Node childNode) {
        this.children.add(childNode);
    }

    public boolean isRoot(){
        return Objects.isNull(parentId);
    }
}
