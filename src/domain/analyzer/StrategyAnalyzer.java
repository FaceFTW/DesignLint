package domain.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.ASMParser;
import domain.DomainAnalyzer;
import domain.LinterError;
import domain.ReturnType;

public class StrategyAnalyzer extends DomainAnalyzer {

	private ASMParser parser;

	private List<String> strategyTypeList;
	private List<String> strategyList;
	private List<String> usingStrategyList;
	private List<LinterError> errorList;

	private Map<String, String[]> strategyTypeStrategiesMap;
	private Map<String, String[]> classUsedStrategies;

	public StrategyAnalyzer(ASMParser parser) {
		super();

		this.strategyList = new ArrayList<>();
		this.strategyTypeList = new ArrayList<>();
		this.strategyTypeStrategiesMap = new HashMap<>();
		this.errorList = new ArrayList<>();
		this.usingStrategyList = new ArrayList<>();
		this.classUsedStrategies = new HashMap<>();
	}

	@Override
	public void getRelevantData(String[] classList) {

	}

	@Override
	public void analyzeData() {

	}

	@Override
	public ReturnType composeReturnType() {

		return null;
	}

	public void SweepInterfaces(String[] classList) {
		for (String className : classList) {
			if (parser.isInterface(className) && !parser.isEnum(className)) {
				this.strategyTypeList.add(className);
			}
		}

		for (String strategyType : this.strategyTypeList) {
			List<String> usedClasses = new ArrayList<>();
			// Do it again but this time for implementing
			for (String className : classList) {
				List<String> interfaces = Arrays.asList(this.parser.getInterfaces(className));
			}
		}

	}

	public void LintInterfaceList() {

	}

	public Map<String, String[]> returnStrategyMap() {
		return this.strategyTypeStrategiesMap;
	}

	public void getUsingClasses() {

	}

	public List<String> returnCompliantClasses() {
		return this.usingStrategyList;
	};

	public void findBadUsingClasses() {

	}

}
