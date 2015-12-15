/*
 * Description: List of Task and Process query coulums. 
 */
package com.file.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oracle.bpel.services.workflow.IWorkflowConstants;
import oracle.bpel.services.workflow.repos.Column;
import oracle.bpel.services.workflow.repos.TableConstants;

import oracle.bpm.services.instancequery.IColumnConstants;


public class ListQueryColumnConstant {

    private final static List<String> baseTaskColumnList_;
    private final static List<Column> baseProcessColumnList_;


    /**
     * Common list of task columns
     * @return
     */
    public static List<String> getBaseTaskColumnList() {
        return baseTaskColumnList_;
    }

    /**
     * Common list of process columns
     * @return
     */
    public static List<Column> getBaseProcessColumnList() {
        return baseProcessColumnList_;
    }


    static {
        List<Column> columns = new ArrayList<Column>();
        columns.add(IColumnConstants.PROCESS_ACTIVITYID_COLUMN);
        columns.add(IColumnConstants.PROCESS_ACTIVITYNAME_COLUMN);
        columns.add(IColumnConstants.PROCESS_APPLICATIONCONTEXT_COLUMN);
        columns.add(IColumnConstants.PROCESS_APPLICATIONNAME_COLUMN);
        columns.add(IColumnConstants.PROCESS_COMPONENTNAME_COLUMN);
        columns.add(IColumnConstants.PROCESS_COMPONENTTYPE_COLUMN);
        columns.add(IColumnConstants.PROCESS_COMPOSITEDN_COLUMN);
        columns.add(IColumnConstants.PROCESS_COMPOSITEINSTANCEID_COLUMN);
        columns.add(IColumnConstants.PROCESS_COMPOSITENAME_COLUMN);
        columns.add(IColumnConstants.PROCESS_COMPOSITEVERSION_COLUMN);
        columns.add(IColumnConstants.PROCESS_CREATEDDATE_COLUMN);
        columns.add(IColumnConstants.PROCESS_CREATOR_COLUMN);
        columns.add(IColumnConstants.PROCESS_DUEDATE_COLUMN);
        columns.add(IColumnConstants.PROCESS_ENDDATE_COLUMN);
        columns.add(IColumnConstants.PROCESS_EXPIRATIONDATE_COLUMN);
        columns.add(IColumnConstants.PROCESS_EXPIRATIONDURATION_COLUMN);
        columns.add(IColumnConstants.PROCESS_ID_COLUMN);
        columns.add(IColumnConstants.PROCESS_IDENTITYCONTEXT_COLUMN);
        columns.add(IColumnConstants.PROCESS_INSTANCEID_COLUMN);
        columns.add(IColumnConstants.PROCESS_PROCESSNAME_COLUMN);
        columns.add(IColumnConstants.PROCESS_NUMBER_COLUMN);
        columns.add(IColumnConstants.PROCESS_NUMBEROFTIMESMODIFIED_COLUMN);
        columns.add(IColumnConstants.PROCESS_OWNERGROUP_COLUMN);
        columns.add(IColumnConstants.PROCESS_OWNERROLE_COLUMN);
        columns.add(IColumnConstants.PROCESS_OWNERUSER_COLUMN);
        columns.add(IColumnConstants.PROCESS_PARENTTHREAD_COLUMN);
        columns.add(IColumnConstants.PROCESS_PROCESSDEFINITIONID_COLUMN);
        columns.add(IColumnConstants.PROCESS_PROCESSDEFINITIONNAME_COLUMN);
        columns.add(IColumnConstants.PROCESS_STATE_COLUMN);
        columns.add(IColumnConstants.PROCESS_TITLE_COLUMN);        
        baseProcessColumnList_ = columns;
    }

    static {
        List<String> baseColumnList = new ArrayList<String>();
        baseColumnList.add(TableConstants.WFTASK_STATE_COLUMN.getName());
        baseColumnList.add(TableConstants.WFTASK_TITLE_COLUMN.getName());
        baseColumnList.add(TableConstants.WFTASK_INSTANCEID_COLUMN.getName());
        baseColumnList.add(TableConstants.WFTASK_PROCESSNAME_COLUMN.getName());
        baseColumnList.add(TableConstants.WFTASK_PROCESSVERSION_COLUMN.getName());
        baseColumnList.add(TableConstants.WFTASK_COMPONENTNAME_COLUMN.getName());
        baseColumnList.add(TableConstants.WFTASK_COMPOSITEINSTANCEID_COLUMN.getName());
        baseColumnList.add(TableConstants.WFTASK_COMPOSITENAME_COLUMN.getName());
        baseColumnList.add(TableConstants.WFTASK_COMPOSITEVERSION_COLUMN.getName());
        baseColumnList.add(TableConstants.WFTASK_TASKDEFINITIONID_COLUMN.getName());
        baseColumnList.add(TableConstants.WFTASK_TASKDEFINITIONNAME_COLUMN.getName());
        baseColumnList.add(TableConstants.WFTASK_TASKNAMESPACE_COLUMN.getName());        
        baseTaskColumnList_ = Collections.unmodifiableList(baseColumnList);
    }
}

