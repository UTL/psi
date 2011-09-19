package webApplication.grafica;

import java.awt.Component;

import javax.swing.JFileChooser;

public class CustomFCText extends CustomFileChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -44099801738168546L;

	public CustomFCText(Options frameOptions, Component p) {
		super(frameOptions,p);
	}



	@Override
	protected int buildFileChooser() {
		if(optionFrame.getTextPath().length()>0)
			fc = new JFileChooser(optionFrame.getTextPath()); 
		else 
			fc= new JFileChooser();
		
		return fc.showOpenDialog(parent);
		
	}

}
