package webApplication.grafica;

import java.awt.Component;

import javax.swing.JFileChooser;

public class CustomFCImage extends CustomFileChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7958820582237786585L;

	public CustomFCImage(Options frameOptions, Component p) {
		super(frameOptions,p);
	}

	@Override
	protected int buildFileChooser() {
		if(optionFrame.getImagePath().length()>0)
			fc = new JFileChooser(optionFrame.getImagePath()); 
		else
			fc = new JFileChooser();
		
		fc.addChoosableFileFilter(new CustomFFImage());
		fc.setAcceptAllFileFilterUsed(false);
		
		return fc.showOpenDialog(parent);
		
	}

}
