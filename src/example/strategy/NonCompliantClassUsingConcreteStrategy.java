package example.strategy;

import example.strategy.strategytype.typea.CompliantStrategyA1;

public class NonCompliantClassUsingConcreteStrategy {
	CompliantStrategyA1 strategyA1;

	public NonCompliantClassUsingConcreteStrategy(CompliantStrategyA1 a1) {
		this.strategyA1 = a1;
	}

	public void doSomething() {
		this.strategyA1.exampleStrategyMethod1();
	}
}
