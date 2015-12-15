/*
 * Description: used to build the search criteria
 */
package com.file.util;

public class SearchCriteria {
    private String processName;
    private String taskName;
    private String processInstanceId;
    private String taskState;

    public SearchCriteria() {
        super();
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskState() {
        return taskState;
    }
}
