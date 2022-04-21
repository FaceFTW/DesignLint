package example.strategy;

import example.strategy.strategytype.typea.CompliantStrategyTypeA;

public class CompliantClassUsingStrategyA {
	private CompliantStrategyTypeA strat;

	public CompliantClassUsingStrategyA(CompliantStrategyTypeA strat) {
		this.strat = strat;
	}

	public void  doSomething(){
		this.strat.exampleStrategyMethod1();
	}

	public int doOtherThings(){
		return this.strat.exampleStrategyMethodWithReturn();
	}
}
