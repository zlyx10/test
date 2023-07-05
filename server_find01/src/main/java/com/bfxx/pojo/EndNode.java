package com.bfxx.pojo;

import lombok.Data;


public class EndNode {

    private String name;
    private String ip;
    private Boolean onlineStatus;
    private double cpuStatus;
    private double memoryStatus;
    private double networkFlow;
    private long timeStamp;


    @Override
    public String toString() {
        return "EndPoint{" +
                "name='" + name + '\'' +
                ", IP='" + ip + '\'' +
                ", onlineStatus=" + onlineStatus +
                ", cpuStatus=" + cpuStatus +
                ", memoryStatus=" + memoryStatus +
                ", networkFlow=" + networkFlow +
                '}';
    }

    public EndNode(){

    }

    public EndNode(String name, String IP, double cpuStatus, double memoryStatus, double networkFlow, long timeStamp, Boolean onlineStatus) {
        this.name = name;
        this.ip = IP;
        this.cpuStatus = cpuStatus;
        this.memoryStatus = memoryStatus;
        this.networkFlow = networkFlow;
        this.timeStamp = timeStamp;
        this.onlineStatus = onlineStatus;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIP(String IP) {
        this.ip = IP;
    }

    public void setOnlineStatus(Boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public void setCpuStatus(double cpuStatus) {
        this.cpuStatus = cpuStatus;
    }

    public void setMemoryStatus(double memoryStatus) {
        this.memoryStatus = memoryStatus;
    }

    public void setNetworkFlow(double networkFlow) {
        this.networkFlow = networkFlow;
    }

    public String getName() {
        return name;
    }

    public String getIP() {
        return ip;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }

    public double getCpuStatus() {
        return cpuStatus;
    }

    public double getMemoryStatus() {
        return memoryStatus;
    }

    public double getNetworkFlow() {
        return networkFlow;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
