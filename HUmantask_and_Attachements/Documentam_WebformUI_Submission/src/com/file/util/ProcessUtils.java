/*
 * Description: This Class contains the utility methods of BPM process implemented with BPM & HWF APIs.
 * Used to handle Oracle BPM process programatically.
 */

package com.file.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.bpel.services.bpm.common.IBPMContext;
import oracle.bpel.services.workflow.WorkflowException;
import oracle.bpel.services.workflow.client.IWorkflowServiceClient;
import oracle.bpel.services.workflow.client.IWorkflowServiceClientConstants;
import oracle.bpel.services.workflow.client.config.ClientConfigurationUtil;
import oracle.bpel.services.workflow.client.config.WorkflowServicesClientConfigurationType;
import oracle.bpel.services.workflow.query.ITaskQueryService;
import oracle.bpel.services.workflow.repos.Ordering;
import oracle.bpel.services.workflow.repos.Predicate;
import oracle.bpel.services.workflow.repos.TableConstants;
import oracle.bpel.services.workflow.task.model.Task;
import oracle.bpel.services.workflow.verification.IWorkflowContext;

import oracle.bpm.client.BPMServiceClientFactory;
import oracle.bpm.services.client.IBPMServiceClient;
import oracle.bpm.services.processmetadata.IProcessMetadataService;
import oracle.bpm.services.processmetadata.ProcessMetadataSummary;


public class ProcessUtils {
    private static IBPMServiceClient bpmSvcClient;
    private static IWorkflowServiceClient wfSvcClient;
    private static String CONFIG = "com/file/config/wf_client_config.xml";


    /**
     * @description Set BPM Service Client and Human Workflow Service
     * @param clientFactory
     */
    public ProcessUtils() {
        // Get BPMServiceClientFactory by properties (Client type, Server details)
        if (bpmSvcClient == null || wfSvcClient == null) {
            BPMServiceClientFactory bpmServiceClientFactory = ProcessUtils.getServiceClientFactoryByConfigFile(CONFIG);
            bpmSvcClient = bpmServiceClientFactory.getBPMServiceClient();
            wfSvcClient = bpmServiceClientFactory.getWorkflowServiceClient();
        }
    }

    /**
     * @description Get BPM Service Client from BPMServiceClientFactory
     * @return IBPMServiceClient
     */
    public static IBPMServiceClient getBPMServiceClient() {
        return bpmSvcClient;
    }

    /**
     * @description Get Human Workflow Service client from BPMServiceClientFactory
     * @return IWorkflowServiceClient
     */
    public static IWorkflowServiceClient getHWFServiceClient() {
        return wfSvcClient;
    }

    /**
   * @description Get IWorkflowContext for specified Username and Password
   * @param userName, password
   * @return IWorkflowContext
   * @throws WorkflowException
   */
  public IWorkflowContext getWrkflowCtx(String userName, String password)
    throws WorkflowException
  {
    IWorkflowContext wrkflowCtx;
    wrkflowCtx = getHWFServiceClient().getTaskQueryService().authenticate(userName, password.toCharArray(), null);
    System.out.println("return " + wrkflowCtx);
    return wrkflowCtx;
  }

    /**
     * @description Get the IBPMContext from IWorkflowContext
     * @param wrkflowCtx
     * @return IBPMContext
     */
    public IBPMContext getBpmCtx(IWorkflowContext wrkflowCtx) {
        IBPMContext bpmCtx;
        return bpmCtx = (IBPMContext)wrkflowCtx;
    }

    /**
     * @description Get the Initable process list ProcessDN
     * @param context ,compositeName, version, processName
     * @return Process Distinguished Name (DN)
     * @throws Exception
     */
    public String getInitableProcessDN(IBPMContext context, String compositeName, String version, String processName) throws Exception {
        List<ProcessMetadataSummary> metadataSummaryList = listInitiatableProcess(context);
        for (ProcessMetadataSummary metadataSummary : metadataSummaryList) {
            if (metadataSummary.getCompositeName().equals(compositeName) && metadataSummary.getProcessName().equals(processName) &&
                metadataSummary.getRevision().equals(version)) {
                return metadataSummary.getCompositeDN() + "/" + metadataSummary.getProcessName();
            }
        }
        return null;
    }

    /**
     * @description Get List of initiable Process (Initiable Patten - Exposed as a Service)
     * @param context
     * @return Metadata summary (processId, projectName, processName, domainName, compositeName, revision)
     * @throws Exception
     */
    private List<ProcessMetadataSummary> listInitiatableProcess(IBPMContext context) throws Exception {
        IProcessMetadataService service = bpmSvcClient.getProcessMetadataService();
        return service.getInitiatableProcesses(context);
    }


    /**
     * @description Update a Task data (Payload, Attachment, comments etc...)
     * @param ctx, task
     * @return Task
     * @throws Exception
     */
    public Task updateTask(IWorkflowContext ctx, Task task) throws Exception {
        return wfSvcClient.getTaskService().updateTask(ctx, task);
    }

    /**
     * @description Update Task outcome [SUBMIT, ACCEPT, REJECT etc...]
     * @param wrkflowCtx, taskId , outcome
     * @return Task
     * @throws Exception
     */
    public Task updateTaskOutcome(IWorkflowContext wrkflowCtx, String taskId, String outcome) throws Exception {
        return wfSvcClient.getTaskService().updateTaskOutcome(wrkflowCtx, taskId, outcome);
    }

    /**
     * @description Return a 'Task' by 'TaskId'
     * @param wrkflowCtx , taskId
     * @return Task
     * @throws WorkflowException
     */
    public Task getTaskByTaskId(IWorkflowContext wrkflowCtx, String taskId) throws WorkflowException {
        ITaskQueryService querySvc = wfSvcClient.getTaskQueryService();
        return querySvc.getTaskDetailsById(wrkflowCtx, taskId);
    }

    /**
     * @description Task Query API
     * @param context, queryColumns, assignmentFilter, predicate
     * @return List of 'Task'
     * @throws WorkflowException
     */
    public List<Task> listTask(IWorkflowContext context, List<String> queryColumns, ITaskQueryService.AssignmentFilter assignmentFilter,
                               Predicate predicate, Ordering order) throws WorkflowException {

        ITaskQueryService querySvc = wfSvcClient.getTaskQueryService();
        System.out.println("querySvc"+querySvc);
        List<Task> tasks = querySvc.queryTasks(context, queryColumns, null, //Do not query additional info
                assignmentFilter, null, //No keywords
                predicate, //custom predicate
                order, // ordering
                0, //Do not page the query result
                0);
        return tasks;
    }

    /**
     * @description Build search criteria for 'listTask()'
     * @param processName, taskName, processInstanceId, taskState
     * @return SearchCriteria
     */
    public static SearchCriteria buildSearchCriteria(String processName, String taskName, String processInstanceId, String taskState) {

        SearchCriteria sc = new SearchCriteria();
        sc.setProcessName(processName);
        sc.setTaskName(taskName);
        sc.setProcessInstanceId(processInstanceId);
        sc.setTaskState(taskState);
        return sc;
    }

    /**
     * @description Build predicate (search criteria)
     * @param processName, taskName, state, processId
     * @return Predicate
     * @throws WorkflowException
     */
    public static Predicate buildPredicate(String processName, String taskName, String processId,
                                           String taskState) throws WorkflowException {
        Predicate predicate = null;

        List pArray = new ArrayList<Predicate>();
        if (processName != null)
            pArray.add(new Predicate(TableConstants.WFTASK_PROCESSNAME_COLUMN, Predicate.OP_EQ, processName));
        if (taskName != null)
            pArray.add(new Predicate(TableConstants.WFTASK_TASKDEFINITIONNAME_COLUMN, Predicate.OP_EQ, taskName));
        if (processId != null)
            pArray.add(new Predicate(TableConstants.WFTASK_INSTANCEID_COLUMN, Predicate.OP_EQ, processId));
        if (taskState != null)
            pArray.add(new Predicate(TableConstants.WFTASK_STATE_COLUMN, Predicate.OP_EQ, taskState));

        Iterator it = pArray.iterator();
        if (it.hasNext()) {
            predicate = (Predicate)it.next();
            while (it.hasNext()) {
                predicate = new Predicate(predicate, Predicate.AND, (Predicate)it.next());
            }
        }

        return predicate;
    }

    /**
     * @description Used to creates instance of IWorkflowServiceClient, IBPMServiceClient.
     * @return BPMServiceClientFactory
     */
    public static BPMServiceClientFactory getServiceClientFactoryByProperties() {
        Map properties = new HashMap();
        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.CLIENT_TYPE, BPMServiceClientFactory.REMOTE_CLIENT);
        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_PROVIDER_URL, "t3://127.0.0.1:9001");
        BPMServiceClientFactory factory = BPMServiceClientFactory.getInstance(properties, null, null);
        return factory;
    }

    public static BPMServiceClientFactory getServiceClientFactoryByConfigFile(String fileName) {
        WorkflowServicesClientConfigurationType cfg = ClientConfigurationUtil.getClientConfiguration(fileName, null);
        if (cfg == null) {
            throw new RuntimeException("workflow client configuration file not found:" + fileName);
        }
        cfg.setClientType("REMOTE");
        return BPMServiceClientFactory.getInstance(cfg, null);
    }


}
