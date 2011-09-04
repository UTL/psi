package webApplication.grafica;

import webApplication.business.Componente;

public class MyEventClass extends java.util.EventObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Componente componente;

	//here's the constructor
    public MyEventClass(Object source) {
        super(source);
    }
    
    public MyEventClass(Object source, Componente comp) {
        super(source);
        this.componente = comp;
    }
    
    public Componente getComponente(){
    	//TODO sarebbe bello usare il metodo clone di andrea
    	return componente;
    }
}