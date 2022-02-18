package example.strategy;

import example.strategy.strategytype.typea.CompliantStrategyTypeA;

public final class CompliantFinalClassUsingStrategyWithMethods {

	public static void doSomething(CompliantStrategyTypeA a) {
		a.exampleStrategyMethod1();
	}

	public static int returnStaticInt(CompliantStrategyTypeA a) {
		return a.exampleStrategyMethodWithReturn();
	}
}
