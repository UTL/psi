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

public class ComponenteSemplice extends Componente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1310689558595301355L;

	/**
	 * Costruttore del componente semplice
	 */
	public ComponenteSemplice(String n, String c, int v, int e, String tp) {
		super(n, c, v, e, tp);

	}

}
