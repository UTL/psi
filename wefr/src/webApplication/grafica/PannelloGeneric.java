package webApplication.grafica;

import javax.swing.JPanel;

public abstract class PannelloGeneric extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8963033423779199816L;

	private static final int width = 421;
	private static final int height = 193;

	protected PannelloGeneric() {
		super();
		setLayout(null);
		setSize(width, height);
	}

}
