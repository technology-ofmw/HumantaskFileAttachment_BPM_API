package com.file.api;


import com.file.util.ListQueryColumnConstant;
import com.file.util.ProcessUtils;
import com.file.util.SearchCriteria;

import java.io.File;
import java.io.InputStream;

import java.util.List;

import oracle.bpel.services.bpm.common.IBPMContext;
import oracle.bpel.services.workflow.StaleObjectException;
import oracle.bpel.services.workflow.WorkflowException;
import oracle.bpel.services.workflow.client.util.WorkflowAttachmentUtil;
import oracle.bpel.services.workflow.query.ITaskQueryService;
import oracle.bpel.services.workflow.repos.Ordering;
import oracle.bpel.services.workflow.repos.Predicate;
import oracle.bpel.services.workflow.repos.TableConstants;
import oracle.bpel.services.workflow.task.model.AttachmentType;
import oracle.bpel.services.workflow.task.model.Task;
import oracle.bpel.services.workflow.verification.IWorkflowContext;

import oracle.bpm.services.common.exception.BPMException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


public class HumantaskWebformDocumentam {
    private static String PROJECT_NAME = "AWebform_Documentam";
    private static String PROCESS_NAME = "AWebform_DocumentamProcess";
    private static String VERSION = "13.0";

    private static ProcessUtils processUtils;

    public HumantaskWebformDocumentam() {
        super();
    }

    public static void main(String[] args) throws WorkflowException, BPMException, Exception {
        HumantaskWebformDocumentam humanFormDoc = new HumantaskWebformDocumentam();
        processUtils = new ProcessUtils();
        // Get Workflow, BPM context
        IWorkflowContext wrkflwCtx = processUtils.getWrkflowCtx("weblogic", "welcome1");
        IBPMContext bpmCtx = (IBPMContext)wrkflwCtx;

        SearchCriteria searchCriteria = processUtils.buildSearchCriteria(PROCESS_NAME, "AWebform_DocumentamHumantask", null, "ASSIGNED");
        List<Task> taskList = listTask(wrkflwCtx, searchCriteria);

        String taskId = null;
        for (Task task : taskList) {
            System.out.println("<<< AWebform_DocumentamHumantask HT - Process Id [" + task.getProcessInfo().getInstanceId() +
                               "] Task [Number:" + task.getSystemAttributes().getTaskNumber() + "][ Name:" +
                               task.getSystemAttributes().getTaskDefinitionName() + "] [ Task Id : " +
                               task.getSystemAttributes().getTaskId() + " ]>>> ");
            taskId = task.getSystemAttributes().getTaskId();
        }

        Task documentamTask = humanFormDoc.updateTask(wrkflwCtx, taskId);
        System.out.println("wrkflwCtx >> " + wrkflwCtx);
        humanFormDoc.submitTask(wrkflwCtx, taskId, "APPROVE");

        System.out.println("documentamTask+ " + documentamTask);
        if (documentamTask != null) {
            // Update outcome of the Task (SUBMIT, ACCEPT, REJECT etc...)
            documentamTask = humanFormDoc.submitTask(wrkflwCtx, taskId, "SUBMIT");
            if (documentamTask != null) {
                System.out.println("<<< HT Updated and Submitted successfully. >>>");
            } else {
                System.out.println("<<< HT Submittion failed. >>>");
            }
        } else {
            System.out.println("<<< HT Updation Failed. >>>");
        }

    }

    /**
     * @Description list of 'Task' based on search criteria.
     * @param wrkflowCtx
     * @param searchCriteria 'ProcessName' and 'TaskName' is mandatory
     * @return List of 'Task' based on search criteria.
     * @throws WorkflowException
     *
     */
    public static List<Task> listTask(IWorkflowContext wrkflowCtx, SearchCriteria searchCriteria) throws WorkflowException {
        List<Task> tasks = null;

        if (searchCriteria != null) {
            String processName = searchCriteria.getProcessName();
            String taskName = searchCriteria.getTaskName();
            String state = searchCriteria.getTaskState();
            String processId = searchCriteria.getProcessInstanceId();

            Predicate predicate = processUtils.buildPredicate(processName, taskName, processId, state);
            ITaskQueryService.AssignmentFilter assignmentFilter = ITaskQueryService.AssignmentFilter.MY_AND_GROUP;
            List<String> columns = ListQueryColumnConstant.getBaseTaskColumnList();
            Ordering ordering = new Ordering(TableConstants.WFTASK_TITLE_COLUMN, true, true);
            tasks = processUtils.listTask(wrkflowCtx, columns, assignmentFilter, predicate, ordering);
        }

        return tasks;
    }

    /**
     * @param wrkFlwCtx
     * @param taskId
     * @return
     * @throws WorkflowException
     * @throws StaleObjectException
     * @throws Exception
     */
    public Task updateTask(IWorkflowContext wrkFlwCtx, String taskId) throws WorkflowException, StaleObjectException, Exception {
        Task documentamTask = null;
        Task task = processUtils.getTaskByTaskId(wrkFlwCtx, taskId);
        System.out.println("task" + task);

        if (task != null) {
            if (task.getAttachment().size() > 0) {
                List attachments = task.getAttachment();
                for (Object o : attachments) {
                    AttachmentType a = (AttachmentType)o;
                    String mime = a.getMimeType();
                    String name = a.getName();
                    String updatedBy = a.getUpdatedBy();
                    String taskID = a.getTaskId();
                    int version = a.getVersion();
                    java.util.Calendar c = a.getUpdatedDate();
                    String cid = a.getCorrelationId();
                    long size = a.getSize();
                    String attScope = a.getAttachmentScope();
                    String updatedByDispName = a.getUpdatedByDisplayName();


                    System.out.println(mime + " :: " + name + " :: " + updatedBy + " :: " + taskID + " :: " + version + " :: " +
                                       c.getInstance().getTime() + " :: " + cid + " :: " + size + " :: " + attScope + " :: " +
                                       updatedByDispName);


                    System.out.println("Context Token >> " + wrkFlwCtx.getToken());
                    System.out.println("SOA URL >> " +
                                       processUtils.getHWFServiceClient().getRuntimeConfigService().getServerURLFromFabricConfig());
                    System.out.println("taskId >> " + taskId);
                    System.out.println("Version >> " + a.getVersion());
                    System.out.println("name" + name);
                    InputStream is =
                        WorkflowAttachmentUtil.getAttachment(wrkFlwCtx, processUtils.getHWFServiceClient().getRuntimeConfigService().getServerURLFromFabricConfig(),
                                                             taskID, a.getVersion(), a.getName(), null);

                    System.out.println("InputStream >>> " + is);
                    String content = IOUtils.toString(is);
                    System.out.println("String >>> " + content);

                    File file = new File("/home/dev/files/documentum_home/" + a.getName());
                    FileUtils.writeStringToFile(file, content);
                }
            }
        }
        documentamTask = task;
        return documentamTask;
    }

    /**
     * @param wrkFlwCtx_
     * @param taskId
     * @param outcome
     * @return
     * @throws StaleObjectException
     * @throws WorkflowException
     * @throws Exception
     */
    public Task submitTask(IWorkflowContext wrkFlwCtx_, String taskId, final String outcome) throws StaleObjectException,
                                                                                                    WorkflowException, Exception {
        Task riCompilationSubmitedTask = null;
        Task task = processUtils.updateTaskOutcome(wrkFlwCtx_, taskId, outcome);
        if (task != null) {
            riCompilationSubmitedTask = task;
        }

        return riCompilationSubmitedTask;
    }

    //  public static InputStream getAttachment1(IWorkflowContext context, String soaUrl, String taskId, int version, String fileName,
    //                                           Logger logger)
    //    throws Exception
    //  {
    //    System.out.println("soaUrl: " + soaUrl);
    //    HTTPConnection connection = new HTTPConnection(new URL(soaUrl));
    //    System.out.println("connection: " + connection.getLoadBalanceProvider());
    //    System.out.println("connection: " + connection.getLoadBalanceEndpoint());
    //    connection.getPort();
    //    connection.getHost();
    //    //    connection.get
    //
    //    connection.removeModule(CookieModule.class);
    //
    //    List headers = new ArrayList();
    //    NVPair[] nvHeader = new NVPair[2];
    //
    //    nvHeader[0] = new NVPair("Content-Type", "text/html; charset=utf-8");
    //    nvHeader[1] = new NVPair("Accept-Charset", "utf-8");
    //
    //    NVPair[] form_data = new NVPair[5];
    //    form_data[0] = new NVPair("QueryAttachment", "QueryAttachment");
    //
    //    form_data[1] = new NVPair("bpmWorklistTaskVersion", version + "");
    //    form_data[2] = new NVPair("bpmWorklistTaskId", taskId);
    //    form_data[3] = new NVPair("bpmWorklistContext", context.getToken());
    //    form_data[4] = new NVPair("WFTASK_ATTACHMENT_NAME", fileName);
    //    System.out.println(form_data.length);
    //    for (int i = 0; i < form_data.length; i++)
    //    {
    //      System.out.println(":::" + form_data[i]);
    //    }
    //
    //    HTTPResponse response = connection.Get("/integration/services/ADFAttachmentHelper", form_data, nvHeader);
    //    System.out.println("response: " + response);
    //    System.out.println("Input streem : " + response.getInputStream());
    //    return response.getInputStream();
    //  }
}
