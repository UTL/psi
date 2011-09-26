package webApplication.grafica;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

public abstract class CustomFCLoadSave extends CustomFileChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8528793808045907800L;

	public CustomFCLoadSave(Options frameOptions, Component p) {
		super(frameOptions,p);
	}

	@Override
	protected int buildFileChooser() {
		if(optionFrame.getLoadSavePath().length()>0)
			fc = new JFileChooser(optionFrame.getLoadSavePath()); 
		else 
			fc= new JFileChooser();
		fc.addChoosableFileFilter(new CustomFFEudSave());
		fc.setAcceptAllFileFilterUsed(false);
		return show();

	}

	protected abstract int show() ;

	

}
