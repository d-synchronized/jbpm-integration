package uk.co.techblue.bpmn;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;




import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.workflow.instance.WorkflowProcessInstance;

import uk.co.techblue.workitems.PatientCheckInWorkItem;


public class KnowledgeSessionInitializer extends BaseKnowledgeSessionInitializer {

    private PatientCheckInWorkItem patientCheckInWorkItem;
    
    @Override
    protected Map<Resource, ResourceType> getResources() {
        Map<Resource, ResourceType> resources = new HashMap<Resource, ResourceType>();
        resources.put(ResourceFactory.newClassPathResource("hospital-management.bpmn"), ResourceType.BPMN2);
        return resources;
    }

    public void setupWorkItemHandlers() {
        patientCheckInWorkItem = new PatientCheckInWorkItem();
        // register the same handler for all the Work Items present in the process.
        statefulKnowledgeSession.getWorkItemManager().registerWorkItemHandler("Human Task", patientCheckInWorkItem);
        statefulKnowledgeSession.getWorkItemManager().registerWorkItemHandler("Notify Gate to Ambulance", patientCheckInWorkItem);
        statefulKnowledgeSession.getWorkItemManager().registerWorkItemHandler("Notify Rejection to Ambulance", patientCheckInWorkItem);
        statefulKnowledgeSession.getWorkItemManager().registerWorkItemHandler("Assign Bed", patientCheckInWorkItem);
    }
    
    public static void main(final String args[]){
        KnowledgeSessionInitializer knowledgeSessionInitializer=new KnowledgeSessionInitializer();
        knowledgeSessionInitializer.initializeSession();
        knowledgeSessionInitializer.setupWorkItemHandlers();
        
        final String patientName="Dishant Anand";
        final String patientAge="24";
        final String patientGender="Male";
        final String patientStatus="Critical";
        
        final Map<String, Object> processInputVariables = new HashMap<String, Object>();
        processInputVariables.put("patientName", patientName);
        processInputVariables.put("patientAge", patientAge);
        processInputVariables.put("patientGender", patientGender);
        processInputVariables.put("patientStatus", patientStatus);
        
        final WorkflowProcessInstance processInstance = (WorkflowProcessInstance) knowledgeSessionInitializer.statefulKnowledgeSession.startProcess("patient-check-in", processInputVariables);
        String currentnodeName=processInstance.getNodeInstances().iterator().next().getNodeName();
        if(currentnodeName!=null && currentnodeName.equals("Assign Bed")){
            final Map<String,Object> taskResults = new HashMap<String, Object>();
            taskResults.put("bedAssignedToPatient", "true");
            taskResults.put("patientBedNumber", "53CFR1C3");
            knowledgeSessionInitializer.patientCheckInWorkItem.completeWorkItemHandler(taskResults);
        }
        
        currentnodeName=processInstance.getNodeInstances().iterator().next().getNodeName();
        if(currentnodeName!=null && currentnodeName.equals("CoordinateStaff")){
            final Map<String,Object> taskResults = new HashMap<String, Object>();
            taskResults.put("checkInResultGate", "Gate1");
            knowledgeSessionInitializer.patientCheckInWorkItem.completeWorkItemHandler(taskResults);
        }
        currentnodeName=processInstance.getNodeInstances().iterator().next().getNodeName();
        if(currentnodeName!=null && currentnodeName.equals("Notify Gate To Ambulance")){
            final Map<String,Object> taskResults = new HashMap<String, Object>();
            taskResults.put("ambulanceNotified", "true");
            knowledgeSessionInitializer.patientCheckInWorkItem.completeWorkItemHandler(taskResults);
        }
        currentnodeName=processInstance.getNodeInstances().iterator().next().getNodeName();
        if(currentnodeName!=null && currentnodeName.equals("CheckIn Patient")){
            final Map<String,Object> taskResults = new HashMap<String, Object>();
            taskResults.put("patientCheckedIn", "true");
            taskResults.put("patientCheckInTime", new Date());
            knowledgeSessionInitializer.patientCheckInWorkItem.completeWorkItemHandler(taskResults);
        }
        
        if(processInstance.getState()==ProcessInstance.STATE_COMPLETED){
            System.out.println("Process completed successfull, patient check in time is "+processInstance.getVariable("checkInTime"));
        }
    }

}
