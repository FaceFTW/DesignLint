package example.strategy.strategytype.typeb;

public class CompliantStrategyB3 implements CompliantStrategyTypeB {

	private int privateConcreteStrategyField;

	public CompliantStrategyB3(int fieldVal) {
		this.privateConcreteStrategyField = fieldVal;
	}

	@Override
	public void otherExampleStrategyMethod() {
		System.out.println(privateConcreteStrategyField);

	}

	@Override
	public void exampleStrategyMethodWithParam(int param) {
		System.out.println("I've got " + param);

	}

}
