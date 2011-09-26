package webApplication.grafica;

import java.awt.Component;

import javax.swing.JFileChooser;

public class CustomFCOptions extends CustomFileChooser {

	public CustomFCOptions(Options oFrame, Component p) {
		super(null, p);
	}

	@Override
	protected int buildFileChooser() {
		fc= new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		return fc.showOpenDialog(parent);
	}

}
