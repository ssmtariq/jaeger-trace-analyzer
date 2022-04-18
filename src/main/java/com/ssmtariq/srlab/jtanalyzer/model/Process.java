package com.ssmtariq.srlab.jtanalyzer.model;

import java.util.ArrayList;
import java.util.Map;

public class Process {
    private String serviceName;
    private ArrayList<Map<String, Object>> tags;
    private Map<String, Object> tag;

    public String getServiceName() {
        return serviceName;
    }

    public ArrayList<Map<String, Object>> getTags() {
        return tags;
    }

    public Map<String, Object> getTag() {
        return tag;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setTags(ArrayList<Map<String, Object>> tags) {
        this.tags = tags;
    }

    public void setTag(Map<String, Object> tag) {
        this.tag = tag;
    }
}
