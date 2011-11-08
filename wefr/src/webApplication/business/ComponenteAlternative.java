/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webApplication.business;

import java.io.Serializable;
import java.util.Vector;

/**
 * 
 * @author Landi Jessica & Miglioranzi Marco
 */
public class ComponenteAlternative extends ComponenteMolteplice implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5660719737295297574L;
	public static String ALTERNATIVETYPE = "Alternative";

	/**
	 * Costruttore di un componenteAlternative
	 */
	public ComponenteAlternative(String n, String c, int v, int e) {
		super(n, c, v, e, ALTERNATIVETYPE);
		opzioni = new Vector<ComponenteSemplice>();
	}

	public void aggiornaOrdine() {
		// TODO pensare a come cambiare l'ordine degli elementi nel vector
	}

}
