package webApplication.grafica;

import java.awt.Component;

public class CustomFCSave extends CustomFCLoadSave {

	public CustomFCSave(Options frameOptions, Component p) {
		super(frameOptions, p);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int show() {
		return fc.showSaveDialog(parent);

	}

	@Override 
	public String getFilePath(){
		if(super.getFilePath().endsWith("."+CustomFFEudSave.EXTENSION))
			return super.getFilePath();
		return super.getFilePath()+"."+CustomFFEudSave.EXTENSION;
		
	}
}
