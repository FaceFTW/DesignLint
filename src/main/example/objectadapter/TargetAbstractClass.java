package example.objectadapter;

public abstract class TargetAbstractClass {
	
	public void doSomethingConcrete() {
		System.out.println("Doing something concrete...");
	}
	
	public abstract void doInterestingThing();
	
	public abstract String doReallyBoringThing(char [] numberCharacters);
	
	public abstract double doHardThing(int number);
}
