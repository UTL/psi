package webApplication.grafica;

import javax.swing.JButton;

public class ButtonRemover extends JButton{

	private static final long serialVersionUID = -7745387450078903035L;
	
	public ButtonRemover(String s) {
		super(s);
	}

	public void removeElements(){
		if(this.getParent() != null && this.getParent() instanceof PannelloAlternative)
			((PannelloAlternative)this.getParent()).removeElements();
		else if(this.getParent() != null && this.getParent() instanceof PannelloAlt)
			((PannelloAlt)this.getParent()).removeElements();
	}
	
}
