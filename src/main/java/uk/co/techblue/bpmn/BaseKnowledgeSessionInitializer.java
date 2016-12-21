package uk.co.techblue.bpmn;

import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.process.DefaultProcessEventListener;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.io.Resource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * The Class KnowledgeSessionInitializer.
 */
public abstract class BaseKnowledgeSessionInitializer {

    /** The stateful knowledge session. */
    protected StatefulKnowledgeSession statefulKnowledgeSession;

    /**
     * Compiles the resources indicated by the concrete implementation of this class (using {@link #getResources()} abstract
     * method. If there is any compilation error an {@link IllegalStateException} is thrown. After the resources are compiled, a
     * kbase is created and populated with the resulting knowledge package/s. From this kbase, a new StatefulKnowledgeSession is
     * created. The session is configured with 2 listeners: 1.- An Agenda event listener that will fireAllRules() every time an
     * activation happens 2.- A Process event listener that will insert the process instance as a Fact once a process is
     * started.
     */
    protected void initializeSession() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        for (Map.Entry<Resource, ResourceType> entry : this.getResources().entrySet()) {
            kbuilder.add(entry.getKey(), entry.getValue());
        }
        if (kbuilder.hasErrors()) {
            KnowledgeBuilderErrors errors = kbuilder.getErrors();
            for (KnowledgeBuilderError error : errors) {
                System.out.println(">>> Error:" + error.getMessage());
            }
            throw new IllegalStateException(">>> Knowledge ..couldn't be parsed! ");
        }
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        statefulKnowledgeSession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(statefulKnowledgeSession);

        statefulKnowledgeSession.addEventListener(new DefaultProcessEventListener() {
            @Override
            public void beforeProcessStarted(ProcessStartedEvent event) {
                statefulKnowledgeSession.insert(event.getProcessInstance());
            }
        });
    }

    /**
     * Gets the resources.
     * 
     * @return the resources
     */
    protected abstract Map<Resource, ResourceType> getResources();
}
