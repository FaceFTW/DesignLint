package domain;

import datasource.ASMParser;

import java.io.IOException;

public class SwitchCaseAnalyzer extends DomainAnalyzer {

	ASMParser parser;

	public SwitchCaseAnalyzer(String[] classNames) {
		try {
			this.parser = new ASMParser(classNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getRelevantData(String[] classList) {

	}

	public void analyzeData() {

	}

	@Override
	public ReturnType composeReturnType() {
		return null;
	}

}
