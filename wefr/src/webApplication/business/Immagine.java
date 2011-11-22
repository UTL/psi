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

public class Immagine extends ComponenteSemplice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2184659453061615158L;
	private String path;
	public static String IMAGETYPE = "Image";

	/**
	 * Costruttore del componente semplice di tipo Immagine
	 */
	public Immagine(String n, String c, int v, int e, String p) {

		super(n, c, v, e, IMAGETYPE);
		path = p;

	}

	/**
	 * Metodo che permette la modifica del path dell'immagine
	 * 
	 * @param p
	 */
	public void setPath(String p) {
		path = p;
	}

	/**
	 * 
	 * @return il path dell'immagine
	 */
	public String getPath() {
		return path;
	}

}
