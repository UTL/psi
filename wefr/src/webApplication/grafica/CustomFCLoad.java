package webApplication.grafica;

import java.awt.Component;

public class CustomFCLoad extends CustomFCLoadSave {

	public CustomFCLoad(Options frameOptions, Component p) {
		super(frameOptions, p);
	}

	@Override
	protected int show() {
		return fc.showOpenDialog(parent);
		
	}

}
