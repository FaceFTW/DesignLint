package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.ASMParser;

public class HighCouplingAnalyzer extends DomainAnalyzer {

	private ASMParser parser;

	private Map<String, List<String>> classCouplingMap;

	public HighCouplingAnalyzer(ASMParser parser) {
		super();

		this.parser = parser;
		this.classCouplingMap = new HashMap<>();
	}

	@Override
	public void getRelevantData(String[] classList) {
		// This method will generally prepare the list of classes that will be parsed in
		// the map.

	}

	@Override
	public void analyzeData() {
		// TODO Auto-generated method stub

	}

	@Override
	public ReturnType composeReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> checkMethodCompliance(String className) {
		List<String> coupledClasses = new ArrayList<>();

		return coupledClasses;
	}

}
