package webApplication.grafica;

import webApplication.business.ComponenteSemplice;

public class MyEventClass extends java.util.EventObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ComponenteSemplice componente;

	//here's the constructor
    public MyEventClass(Object source) {
        super(source);
    }
    
    public MyEventClass(Object source, ComponenteSemplice comp) {
        super(source);
        this.componente = comp;
    }
    
    public ComponenteSemplice getComponente(){
    	//TODO sarebbe bello usare il metodo clone di andrea
    	return componente;
    }
}