package webApplication.grafica;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JList;

import webApplication.business.ComponenteSemplice;

public class Utils {
	public static String[] extractNomiComponenti (Vector<ComponenteSemplice> componenti){
		String[] nomiComponenti= new String[componenti.size()];
		
		//TODO mettere icone per il tipo degli oggetti

		int i;
		for (i = 0; i < componenti.size(); i++) {
			nomiComponenti[i] = componenti.get(i).getNome();
		}
		return nomiComponenti;
	}
	
	public static void buttonDeleteMgmt(JList lista, JButton bottone){
		
		if (lista.getSelectedIndices().length > 0)
			bottone.setEnabled(true);
		else
			bottone.setEnabled(false);
		
	}
	
	public static void buttonUpDownMgmt(JList lista, JButton b_up, JButton b_down) {
		int num_elem = lista.getModel().getSize();
		int max_selected = lista.getMaxSelectionIndex(); // -1 se nessun elemento selezionato
		int min_selected = lista.getMinSelectionIndex();
		
		if(num_elem < 2 || max_selected == -1 || (min_selected == 0 && max_selected == num_elem-1)){
			b_down.setEnabled(false);
			b_up.setEnabled(false);
		}
		else if(min_selected == 0){
			b_down.setEnabled(true);
			b_up.setEnabled(false);
		}
		else if(max_selected == num_elem-1){
			b_down.setEnabled(false);
			b_up.setEnabled(true);
		}
		else {
			b_down.setEnabled(true);
			b_up.setEnabled(true);
		}
	}
}
