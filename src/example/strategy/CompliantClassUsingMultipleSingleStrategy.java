package example.strategy;

import example.strategy.strategytype.typea.CompliantStrategyTypeA;

public class CompliantClassUsingMultipleSingleStrategy {
	private CompliantStrategyTypeA[] strat;

	public CompliantClassUsingMultipleSingleStrategy(CompliantStrategyTypeA[] strat) {
		this.strat = strat;
	}

	public void doSomething() {
		for (CompliantStrategyTypeA strategy : this.strat) {
			strategy.exampleStrategyMethod1();
		}
	}

	public int doOtherThings() {
		int i = 0;
		for (CompliantStrategyTypeA strategy : this.strat) {
			i += strategy.exampleStrategyMethodWithReturn();
		}

		return i;
	}
}
