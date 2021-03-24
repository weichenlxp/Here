package edu.hebut.here.data;

public class Container {
    private int containerID;
    private String containerName;

    public Container(int containerID, String containerName) {
        this.containerID = containerID;
        this.containerName = containerName;
    }

    public int getContainerID() {
        return containerID;
    }

    public void setContainerID(int containerID) {
        this.containerID = containerID;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    @Override
    public String toString() {
        return "Container{" +
                "containerID=" + containerID +
                ", containerName='" + containerName + '\'' +
                '}';
    }
}