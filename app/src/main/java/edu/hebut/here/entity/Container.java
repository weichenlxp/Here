package edu.hebut.here.entity;

public class Container {
    int containerID;
    String containerName;
    int userID;

    public Container(int containerID, String containerName, int userID) {
        this.containerID = containerID;
        this.containerName = containerName;
        this.userID = userID;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Container{" +
                "containerID=" + containerID +
                ", containerName='" + containerName + '\'' +
                ", userID=" + userID +
                '}';
    }
}
