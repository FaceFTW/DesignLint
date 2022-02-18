package example.strategy.strategytype.typeb;

import example.strategy.strategytype.typea.CompliantStrategyTypeA;

public class NonCompliantStrategyB4ImplementsOtherStrategyInterface
		implements CompliantStrategyTypeB, CompliantStrategyTypeA {

	@Override
	public void exampleStrategyMethod1() {
		System.out.println("HOW IS THIS HAPPENING???");
	}

	@Override
	public int exampleStrategyMethodWithReturn() {
		return -1;
	}

	@Override
	public void otherExampleStrategyMethod() {
		System.out.println("ALL I HEAR ARE SCREAMS OF THE DAMNED");
	}

	@Override
	public void exampleStrategyMethodWithParam(int param) {
		System.out.println("Still got " + param);

	}

}
