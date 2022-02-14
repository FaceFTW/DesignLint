package example.demeter;

public class B {
	private C c;
	
	public B() {
		this.c = new C();
	}
	
	public void doBThing() {
		System.out.println("B doing something...");
	}
	
	public void doThingWithC() {
		this.c.doCThing();
	}
	
	public C getC() {
		return this.c;
	}
	
}
