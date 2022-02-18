package example.strategy.strategytype.typea;

public class CompliantStrategyA2 implements CompliantStrategyTypeA{

	@Override
	public void exampleStrategyMethod1() {
		System.out.println("a little trolling");
	}

	@Override
	public int exampleStrategyMethodWithReturn() {
		return 1;
	}
	
}
