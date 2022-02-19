package example.strategy;

import example.strategy.strategytype.typea.CompliantStrategyTypeA;
import example.strategy.strategytype.typeb.CompliantStrategyTypeB;

public class CompliantClassUsingDifferentSingleStrategy {
	CompliantStrategyTypeA stratA;
	CompliantStrategyTypeB stratB;

	public CompliantClassUsingDifferentSingleStrategy(CompliantStrategyTypeA a, CompliantStrategyTypeB b) {
		this.stratA = a;
		this.stratB = b;
	}

	public void doSomething() {
		this.stratA.exampleStrategyMethod1();
	}

	public int doOtherThings() {
		return this.stratA.exampleStrategyMethodWithReturn();
	}

	public void doSomethingAlt() {
		this.stratB.otherExampleStrategyMethod();
	}

	public void doOtherThings(int param) {
		this.stratB.exampleStrategyMethodWithParam(param);
	}
}
