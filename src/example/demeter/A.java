package example.demeter;

public class A {
	private B b;
	private B b2;
	
	public A() {
		this.b = new B();
		this.b2 = new B();
	}
	
	public B getB() {
		return this.b;
	}
	
	public void doAThing() {
		System.out.println("A doing something...");
	}
	
	public void doThingWithB() {
		this.b.doBThing();
		this.b2.doBThing();
	}
	
	public void doThingWithCRight() {
		this.b.doThingWithC();
	}
	
	public void doThingWithCWrong() {
		this.b.getC().doCThing();
	}
	
	public void doThingWithCWrongVar() {
		C myC = this.b.getC();
		myC.doCThing();
	}
	
	public void doThingWithCParam(C c) {
		c.doCThing();
	}
	
	public void doThingWithCParamMultipleParams(int x, D d, String y, C c) {
		c.doCThing();
		d.doDThing();
	}
	
	public void doThingWithBParamRight(C c) {
		c.doThingWithBRight();
	}
	
	public void doThingWithBParamAlsoRight(C c) {
		c.getA().doThingWithB();
	}
	
	public void doThingWithBParamWrong(C c) {
		c.getA().getB().doBThing();
	}
	
	public void doThingWithNewC() {
		C c = new C();
		c.doCThing();
	}
	
	public void doThingWithNewCWrong() {
		C c = new C();
		this.b.getC().doCThing();
	}
}
