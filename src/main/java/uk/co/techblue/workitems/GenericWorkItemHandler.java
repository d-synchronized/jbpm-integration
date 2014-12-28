package uk.co.techblue.workitems;

import java.util.HashMap;
import java.util.Map;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import uk.co.techblue.dto.CommandContext;
import uk.co.techblue.service.interfaces.ExecutorServiceEntryPoint;

/**
 * The Class GenericWorkItemHandler.
 */
public class GenericWorkItemHandler implements WorkItemHandler {

    /** The executor service entry point. */
    private ExecutorServiceEntryPoint executorServiceEntryPoint;

    /** The session id. */
    private int sessionId;

    /** The execution key. */
    private String executionKey;

    /**
     * Instantiates a new generic work item handler.
     * 
     * @param executorServiceEntryPoint the executor service entry point
     * @param sessionId the session id
     */
    public GenericWorkItemHandler(final ExecutorServiceEntryPoint executorServiceEntryPoint, final int sessionId) {
        this.executorServiceEntryPoint = executorServiceEntryPoint;
        this.sessionId = sessionId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.drools.runtime.process.WorkItemHandler#abortWorkItem(org.drools.runtime.process.WorkItem,
     * org.drools.runtime.process.WorkItemManager)
     */
    @Override
    public void abortWorkItem(final WorkItem workItem, final WorkItemManager workItemManager) {
        final Long requestId = (Long) workItem.getParameter("requestId");
        executorServiceEntryPoint.cancelRequest(requestId);

        final Boolean waitUntilTrue = (Boolean) workItem.getParameter("waitUntilTrue");
        if (waitUntilTrue != null && !waitUntilTrue.booleanValue()) {
            workItemManager.abortWorkItem(sessionId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.drools.runtime.process.WorkItemHandler#executeWorkItem(org.drools.runtime.process.WorkItem,
     * org.drools.runtime.process.WorkItemManager)
     */
    @Override
    public void executeWorkItem(final WorkItem workItem, final WorkItemManager workItemManager) {
        final Long workItemId = workItem.getId();
        final String callbacks = (String) workItem.getParameter("callbacks");
        final String command = (String) workItem.getParameter("command");
        final Boolean waitUntilTrue = (Boolean) workItem.getParameter("waitUntilTrue");

        executionKey = workItem.getName() + "_" + workItem.getProcessInstanceId() + "_" + workItemId + "@sessionId="
                + this.sessionId;

        final CommandContext commandContext = new CommandContext();
        if (commandContext.getCommandContextData() == null) {
            final Map<String, Object> contextDataMap = new HashMap<String, Object>();
            commandContext.setCommandContextData(contextDataMap);
        }
        commandContext.getCommandContextData().put("_workItemId", String.valueOf(workItemId));
        commandContext.getCommandContextData().put("callbacks", callbacks);
        commandContext.getCommandContextData().put("businessKey", executionKey);

        final Long requestId = executorServiceEntryPoint.scheduleRequest(command, commandContext);
        workItem.getParameters().put("requestId", requestId);
        if (!waitUntilTrue) {
            workItemManager.completeWorkItem(workItemId, workItem.getResults());
        }
    }

}
