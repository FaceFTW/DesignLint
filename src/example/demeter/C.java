package example.demeter;

import java.util.ArrayList;
import java.util.List;

public class C {
	
	private A a;
	
	public C() {
		this.a = new A();
	}
	
	public void doCThing() {
		System.out.println("C doing something...");
	}
	
	public A getA() {
		return this.a;
	}
	
	public void doThingWithBRight() {
		this.a.doThingWithB();
	}
	
	public void doThingWithBWrong() {
		this.a.getB().doBThing();
	}
	
	public void letADoThingWithC() {
		this.a.doThingWithCParam(this);
	}
	
	public void letADoThingWithCLoop() {
		List<A> as = new ArrayList<A>();
		as.add(this.a);
		for(A a : as) {
			a.doThingWithCParam(this);
		}
	}
	
	
	
}
