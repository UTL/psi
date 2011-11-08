package webApplication.grafica;

import java.util.EventListener;

public abstract class PannelloSimple extends PannelloGeneric {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5172975635376788718L;
	
	protected PannelloSimple() {
		super();
	}

	protected abstract void addFieldsListener(EventListener list);

}
