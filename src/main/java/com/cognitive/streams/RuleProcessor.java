package com.cognitive.streams;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.kie.api.runtime.StatelessKieSession;

import com.cognitive.model.Patient;

public class RuleProcessor implements Processor<String, Patient> {

   // private final Patient patient;
    private ProcessorContext context;
    private String root = System.getProperty("user.dir");
    private String ruleFile = "/bin/rules/sepsis.xls";
    StatelessKieSession kieSession;

    public RuleProcessor() {
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        kieSession = KieSessionFactory.getKieSession(root + ruleFile);
    }

    @Override
	public void process(String id, Patient patient) {
    	kieSession.execute(patient);
    }

    @Override
	public void punctuate(long timestamp) {
        // Stays empty.  In this use case there would be no need for a periodical
        // action of this processor.
    }

    @Override
	public void close() {
        // The code for clean up goes here.
        // This processor instance will not be used again after this call.
    }
}
