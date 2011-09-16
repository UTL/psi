package webApplication.grafica;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;

public class AListenerRemoveFromAlt implements ActionListener {
	private JList l_alter = null;
	public AListenerRemoveFromAlt(JList l_alt) {
		setList(l_alt);
	}

	public void setList(JList l_alt){
		l_alter=l_alt;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		// TODO fare un controllo sul nome dell'oggetto
		
		if(l_alter!= null)
			MainWindow.removeElementFromComposto(l_alter.getSelectedIndices());	
	}

}
