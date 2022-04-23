package example.strategy.strategytype.typeb;

public class CompliantStrategyB2 implements CompliantStrategyTypeB {

	@Override
	public void otherExampleStrategyMethod() {
		System.out.println("still compliant again");
	}

	@Override
	public void exampleStrategyMethodWithParam(int param) {
		System.out.println("I got " + (param + 1));
	}
}
