/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webApplication.business;

import java.io.Serializable;

/**
 * 
 * @author Landi Jessica & Miglioranzi Marco
 */
public class Testo extends ComponenteSemplice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4128305818045516427L;
	private String testo;
	public static String TEXTTYPE = "Text";

	/**
	 * Costruttore del componente semplice di tipo Testo
	 */
	public Testo(String n, String c, int v, int e, String t) {

		super(n, c, v, e, TEXTTYPE);
		testo = t;
	}

	/**
	 * Metodo che permette la modifica del contenuto del testo
	 * 
	 * @param t
	 */
	public void setTesto(String t) {

		testo = t;

	}

	/**
	 * 
	 * @return il contenuto del testo
	 */
	public String getTesto() {

		return testo;

	}

}
