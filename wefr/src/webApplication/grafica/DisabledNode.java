package webApplication.grafica;

//import java.util.Vector;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class DisabledNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4393520003312604873L;

	private boolean enabled;
	private List<Problem> errors;
	//TODO vettore degli errori presenti nel nodo->usi futuri
//	private Vector errors;

	public DisabledNode(Object obj) {
		super(obj);
		enabled = true;
	}

	protected void setEnabled(boolean state) {
		enabled = state;
	}

	protected boolean getEnabled() {
		return enabled;
	}
	
	//TODO da completare l'integrazione degli errori

}
