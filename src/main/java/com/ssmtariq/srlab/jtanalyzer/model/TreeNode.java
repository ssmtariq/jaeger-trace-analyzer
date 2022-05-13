package com.ssmtariq.srlab.jtanalyzer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeNode {
    private String spanId;
    private String serviceName;
    private String parentId;
    private long duration;
    private List<TreeNode> children = new ArrayList<>();

    public TreeNode(String spanId, long duration){
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addChildren(TreeNode childNode) {
        this.children.add(childNode);
    }

    public boolean isRoot(){
        return Objects.isNull(parentId);
    }
}
