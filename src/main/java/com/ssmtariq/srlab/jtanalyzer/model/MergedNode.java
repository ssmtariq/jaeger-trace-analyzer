package com.ssmtariq.srlab.jtanalyzer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MergedNode {
    private String serviceName;
    private String path;
    private String operationName;
    private Long duration;
    private Long spanCount;
    private String parentPath;

    private List<MergedNode> children = new ArrayList<>();

    public MergedNode() {

    }

    public MergedNode(String serviceName, String operationName, Long duration) {
        this.serviceName = serviceName;
        this.operationName = operationName;
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(Long spanCount) {
        this.spanCount = spanCount;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public List<MergedNode> getChildren() {
        return children;
    }

    public void addChildren(MergedNode childNode) {
        this.children.add(childNode);
    }

    public boolean isRoot() {
        return Objects.isNull(path);
    }
}
