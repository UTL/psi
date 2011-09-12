/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webApplication.business;

/**
 *
 * @author Landi Jessica & Miglioranzi Marco
 */
import java.io.Serializable;
import java.util.Vector;

public class ComponenteComposto extends ComponenteMolteplice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6858370768463511794L;
	public static String COMPOSTOTYPE = "Composite";

	/**
	 * Creazione di un componente composto
	 */
	public ComponenteComposto(String n, String c, int v, int e) {

		super(n, c, v, e, COMPOSTOTYPE);
		opzioni = new Vector<ComponenteSemplice>();

	}

}
