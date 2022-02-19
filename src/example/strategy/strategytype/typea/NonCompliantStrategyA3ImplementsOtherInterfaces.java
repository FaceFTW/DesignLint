package example.strategy.strategytype.typea;

public class NonCompliantStrategyA3ImplementsOtherInterfaces implements CompliantStrategyTypeA, Cloneable {

	@Override
	public void exampleStrategyMethod1() {
		System.out.println("oh no this is not compliant");

	}

	@Override
	public int exampleStrategyMethodWithReturn() {
		return 2;
	}

}
