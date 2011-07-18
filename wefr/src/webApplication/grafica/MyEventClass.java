package webApplication.grafica;

public class MyEventClass extends java.util.EventObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//here's the constructor
    public MyEventClass(Object source) {
        super(source);
    }
}