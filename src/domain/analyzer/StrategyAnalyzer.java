package domain.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.ASMParser;
import domain.DomainAnalyzer;
import domain.ErrType;
import domain.LinterError;
import domain.ReturnType;

public class StrategyAnalyzer extends DomainAnalyzer {

	public static final String IMPLEMENTS_TOO_MANY_INTERFACE_ERR = "Strategy %s implements too many interfaces!";
	public static final String NO_IMPLEMENTING_STRATEGIES = "Strategy Type %s has no strategies implementing it!";
	public static final String IS_STRATEGY_TYPE = "%s is a strategy type";
	public static final String IS_STRATEGY = "%s is a strategy for the strategy type %s";
	public static final String USES_STRATEGY = "%s properly uses the strategy pattern for strategies of type %s";
	public static final String CONCRETE_FIELD = "%s declares a concrete strategy as a field!";

	private ASMParser parser;

	private List<String> strategyTypeList;
	private List<String> strategyList;
	private List<String> nonStratclassList;
	private List<LinterError> errorList;

	private Map<String, List<String>> strategyTypeStrategiesMap;
	private Map<String, List<String>> classUsedStrategies;

	public StrategyAnalyzer(ASMParser parser) {
		super();

		this.parser = parser;
		this.strategyList = new ArrayList<>();
		this.strategyTypeList = new ArrayList<>();
		this.strategyTypeStrategiesMap = new HashMap<>();
		this.errorList = new ArrayList<>();
		this.classUsedStrategies = new HashMap<>();
		this.nonStratclassList = new ArrayList<>();
	}

	@Override
	public void getRelevantData(String[] classList) {
		this.sweepInterfaces(classList);
		this.lintInterfaceList();
		this.getUsingClasses();
	}

	@Override
	public void analyzeData() {
		for (String stratType : this.strategyTypeList) {
			errorList.add(new LinterError(stratType, String.format(IS_STRATEGY_TYPE, stratType), ErrType.PATTERN));
			for (String strat : this.strategyTypeStrategiesMap.get(stratType)) {
				errorList.add(new LinterError(strat, String.format(IS_STRATEGY, strat, stratType), ErrType.PATTERN));
			}
		}

		for (String usingClass : this.classUsedStrategies.keySet()) {
			for (String usedStratType : this.classUsedStrategies.get(usingClass)) {
				errorList.add(new LinterError(usingClass, String.format(USES_STRATEGY, usingClass, usedStratType),
						ErrType.PATTERN));
			}
		}
	}

	@Override
	public ReturnType composeReturnType() {
		return new ReturnType("Strategy Pattern Detection", errorList);
	}

	public void sweepInterfaces(String[] classList) {
		for (String className : classList) {
			if (parser.isInterface(className) && !parser.isEnum(className)) {
				this.strategyTypeList.add(className);
			}
		}

		for (String strategyType : this.strategyTypeList) {
			List<String> implementingStrategies = new ArrayList<>();
			for (String className : classList) {
				List<String> interfaces = Arrays.asList(this.parser.getInterfaces(className));
				if (interfaces.contains(strategyType)) {
					implementingStrategies.add(className);
					strategyList.add(className);
				}
			}

			this.strategyTypeStrategiesMap.put(strategyType, implementingStrategies);
		}

		for (String className : classList) {
			if (!this.strategyTypeList.contains(className) && !this.strategyList.contains(className)) {
				this.nonStratclassList.add(className);
			}
		}
	}

	public void lintInterfaceList() {
		List<String> typesToRemove = new ArrayList<>();
		for (String stratType : this.strategyTypeStrategiesMap.keySet()) {
			if (this.strategyTypeStrategiesMap.get(stratType).size() == 0) {
				typesToRemove.add(stratType);
				this.errorList.add(new LinterError(stratType, String.format(NO_IMPLEMENTING_STRATEGIES, stratType),
						ErrType.WARNING));
			}
		}

		if (typesToRemove.size() >= 1) {
			for (String removedType : typesToRemove) {
				this.strategyTypeList.remove(removedType);
				this.strategyTypeStrategiesMap.remove(removedType);
			}
		}

		List<String> toRemove = new ArrayList<>();
		for (String strat : this.strategyList) {
			String[] interfaces = this.parser.getInterfaces(strat);

			if (interfaces.length >= 2) {
				this.errorList.add(
						new LinterError(strat, String.format(IMPLEMENTS_TOO_MANY_INTERFACE_ERR, strat), ErrType.ERROR));

				toRemove.add(strat);

				for (String interfaceName : interfaces) {
					List<String> tempList = this.strategyTypeStrategiesMap.get(interfaceName);
					if (this.strategyTypeList.contains(interfaceName)) {
						tempList.remove(strat);
						this.strategyTypeStrategiesMap.put(interfaceName, tempList);
					}
				}
			}

		}

		if (toRemove.size() >= 1) {
			for (String removeMe : toRemove) {
				this.strategyTypeList.remove(removeMe);
			}
		}
	}

	public Map<String, List<String>> returnStrategyMap() {
		return this.strategyTypeStrategiesMap;
	}

	public void getUsingClasses() {
		for (String className : this.nonStratclassList) {
			List<String> usedStrats = new ArrayList<>();
			if (parser.isFinal(className) && parser.allMethodsStatic(className)) {
				String[] paramTypes = parser.getAllMethodParameterTypes(className);
				for (String param : paramTypes) {
					if (this.strategyTypeList.contains(param)) {
						usedStrats.add(param);
					}
				}
			} else {
				String[] classFields = parser.getFieldTypeNames(className);
				for (String field : classFields) {
					String fixedField = field.replace("[", "");
					if (this.strategyList.contains(fixedField)) {
						errorList.add(
								new LinterError(className, String.format(CONCRETE_FIELD, className), ErrType.ERROR));
					} else if (this.strategyTypeList.contains(fixedField)) {
						usedStrats.add(fixedField);
					}
				}
			}

			if (usedStrats.size() >= 1) {
				this.classUsedStrategies.put(className, usedStrats);
			}
		}

	}

	public Map<String, List<String>> returnCompliantClassUsings() {
		return this.classUsedStrategies;
	}

}
