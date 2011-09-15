package webApplication.grafica;

import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;

import webApplication.business.ComponenteSemplice;

public class Utils {
	public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Utils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
	
	public static String[] extractNomiComponenti (Vector<ComponenteSemplice> componenti){
		if(componenti == null)
			return null;
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
