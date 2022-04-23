package example.strategy.strategytype.typeb;

public class CompliantStrategyB1 implements CompliantStrategyTypeB {

	@Override
	public void otherExampleStrategyMethod() {
		System.out.println("still compliant");
	}

	@Override
	public void exampleStrategyMethodWithParam(int param) {
		System.out.println("I got " + param);
	}

}
