package domain.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.ASMParser;
import domain.AnalyzerReturn;
import domain.DomainAnalyzer;
import domain.message.InfoLinterMessage;
import domain.message.LinterMessage;

public class EqualsAndHashcodeAnalyzer extends DomainAnalyzer {

	ASMParser parser;
	public Map<String, String[]> classAndMethodNames;
	List<LinterMessage> errorList = new ArrayList<>();

	public EqualsAndHashcodeAnalyzer(String[] classNames) {
		try {
			this.parser = new ASMParser(classNames);
			this.classAndMethodNames = new HashMap<String, String[]>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public EqualsAndHashcodeAnalyzer(ASMParser parser) {
		this.parser = parser;
		this.classAndMethodNames = new HashMap<>();
	}

	@Override
	public void getRelevantData(String[] classList) {

		for (String className : classList) {
			String[] methodNames = parser.getMethods(className);
			classAndMethodNames.put(className, methodNames);
		}
	}

	public void analyzeData() {
		for (String className : classAndMethodNames.keySet()) {
			String[] methodNames = classAndMethodNames.get(className);
			boolean seenEquals = false;
			boolean seenHashcode = false;
			for (String methodName : methodNames) {
				if (methodName.equals("equals")) {
					seenEquals = true;
				}
				if (methodName.equals("hashCode")) {
					seenHashcode = true;
				}
			}
			if (seenEquals) {
				if (!seenHashcode) {
					LinterMessage err = new InfoLinterMessage(className,
							"When overriding the equals method, you should also override the hashCode method ");
					this.errorList.add(err);
				}
			}
			if (seenHashcode) {
				if (!seenEquals) {
					LinterMessage err = new InfoLinterMessage(className,
							"When overriding the hashCode method, you should also override the equals method ");
					this.errorList.add(err);
				}
			}
		}
	}

	@Override
	public AnalyzerReturn composeReturnType() {
		AnalyzerReturn type = new AnalyzerReturn("Equals And Hashcode Override Check", this.errorList);
		return type;
	}
}
