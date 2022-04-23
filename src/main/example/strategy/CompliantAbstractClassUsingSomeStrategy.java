package example.strategy;

import example.strategy.strategytype.typeb.CompliantStrategyTypeB;

public abstract class CompliantAbstractClassUsingSomeStrategy {
	CompliantStrategyTypeB strat;

	public abstract void doSomethingAlt();

	public void doOtherThings(int param) {
		this.strat.exampleStrategyMethodWithParam(param);
	}
}
