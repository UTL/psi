package webApplication.grafica;

import javax.swing.tree.DefaultMutableTreeNode;

public class DisabledNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4393520003312604873L;

	private boolean enabled;
	protected boolean isCorrect;

	public DisabledNode(Object obj) {
		super(obj);
		enabled = true;
		isCorrect = true;
	}

	protected void setEnabled(boolean state) {
		enabled = state;
	}

	protected boolean getEnabled() {
		return enabled;
	}

}
