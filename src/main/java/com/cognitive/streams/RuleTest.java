package com.cognitive.streams;

import java.net.URL;

import org.kie.api.runtime.StatelessKieSession;

import com.cognitive.model.Patient;

public class RuleTest {

	public static void main(String[] args) {
		String root = System.getProperty("user.dir");
		StatelessKieSession kieSession  = KieSessionFactory.getKieSession(root + "/bin/rules/sepsis.xls");
		Patient p = new Patient("1", 1464818  , "CA");
		p.setTemperature(80);
		p.setHeartRate(95);
		p.setRespiratoryRate(25);
		p.setWbc(3000);
		p.setSbp(85);
		p.setInfectionFlag("Y");
		p.setHypotension(1);
		p.setOrganFailCount(3);
		
		RuleProcessor processor = new RuleProcessor();
		processor.init(null);
		processor.process("2", p);
		kieSession.execute(p);
	}

}
