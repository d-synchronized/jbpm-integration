package uk.co.techblue.workitems;

import java.util.HashMap;
import java.util.Map;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

/**
 * The Class PatientCheckInWorkItem.
 */
public class PatientCheckInWorkItem implements WorkItemHandler {

    /** The work item manager. */
    private WorkItemManager workItemManager;

    /** The input parameters. */
    private Map<String, Object> inputParameters;

    /** The work item id. */
    private Long workItemId;

    /*
     * (non-Javadoc)
     * 
     * @see org.drools.runtime.process.WorkItemHandler#abortWorkItem(org.drools.runtime.process.WorkItem,
     * org.drools.runtime.process.WorkItemManager)
     */
    @Override
    public void abortWorkItem(final WorkItem workItem, final WorkItemManager workItemManager) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.drools.runtime.process.WorkItemHandler#executeWorkItem(org.drools.runtime.process.WorkItem,
     * org.drools.runtime.process.WorkItemManager)
     */
    @Override
    public void executeWorkItem(final WorkItem workItem, final WorkItemManager workItemManager) {
        this.workItemId = workItem.getId();
        this.workItemManager = workItemManager;

        inputParameters = new HashMap<String, Object>();
        final Map<String, Object> workItemInputParameters = workItem.getParameters();
        if (workItemInputParameters != null && !workItemInputParameters.isEmpty()) {
            inputParameters.putAll(workItemInputParameters);
        }

    }

    /**
     * Complete work item handler.
     * 
     * @param workItemInputParameters the work item input parameters
     */
    public void completeWorkItemHandler(final Map<String, Object> workItemInputParameters) {
        workItemManager.completeWorkItem(workItemId, workItemInputParameters);
    }

    /**
     * Gets the input parameters.
     * 
     * @return the input parameters
     */
    public Map<String, Object> getInputParameters() {
        return inputParameters;
    }

    /**
     * Sets the input parameters.
     * 
     * @param inputParameters the input parameters
     */
    public void setInputParameters(Map<String, Object> inputParameters) {
        this.inputParameters = inputParameters;
    }

}
