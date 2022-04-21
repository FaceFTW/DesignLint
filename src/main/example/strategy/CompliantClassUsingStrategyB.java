package example.strategy;

import example.strategy.strategytype.typeb.CompliantStrategyTypeB;

public class CompliantClassUsingStrategyB {
	private CompliantStrategyTypeB strat;

	public CompliantClassUsingStrategyB(CompliantStrategyTypeB strat) {
		this.strat = strat;
	}

	public void doSomethingAlt() {
		this.strat.otherExampleStrategyMethod();
	}

	public void doOtherThings(int param) {
		this.strat.exampleStrategyMethodWithParam(param);
	}
}
