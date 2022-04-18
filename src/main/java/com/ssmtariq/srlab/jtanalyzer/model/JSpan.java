package com.ssmtariq.srlab.jtanalyzer.model;

import java.util.ArrayList;
import java.util.Map;

public class JSpan {
    private String spanID;
    private String traceID;
    private String flags;
    private String operationName;
    private ArrayList<Map<String, Object>> references;
    private Double startTime;
    private Double startTimeMillis;
    private Double duration;
    private ArrayList<Map<String, Object>> tags;
    private Map<String, Object> tag;
    private ArrayList<String> logs;
    private Process process;

    public String getSpanID() {
        return spanID;
    }

    public void setSpanID(String spanID) {
        this.spanID = spanID;
    }

    public String getTraceID() {
        return traceID;
    }

    public void setTraceID(String traceID) {
        this.traceID = traceID;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public ArrayList<Map<String, Object>> getReferences() {
        return references;
    }

    public void setReferences(ArrayList<Map<String, Object>> references) {
        this.references = references;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(Double startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public ArrayList<Map<String, Object>> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Map<String, Object>> tags) {
        this.tags = tags;
    }

    public Map<String, Object> getTag() {
        return tag;
    }

    public void setTag(Map<String, Object> tag) {
        this.tag = tag;
    }

    public ArrayList<String> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<String> logs) {
        this.logs = logs;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }
}
