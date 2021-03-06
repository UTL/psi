/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webApplication.business;

import java.io.*;

/**
 * 
 * @author Landi Jessica & Miglioranzi Marco
 */

public abstract class Componente implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7375591465729638889L;
	/*
	 * visibilita' 0 = necessary1 = Indifferent2 = expendable
	 * 
	 * enfasi 0 = Greatly"); 1 = Normally"); 2 = Not at all
	 */
	private String nome;
	private String categoria;
	private int visibilita;
	private int enfasi;
	private String type;

	/**
	 * Costruzione di un componente
	 */
	public Componente(String n, String c, int v, int e, String tp) {

		nome = n;
		categoria = c;
		visibilita = v;
		enfasi = e;
		type = tp;

	}

	/**
	 * Metodo che permette la modifica del nome del componente
	 * 
	 * @param n
	 */
	public void setNome(String n) {

		nome = n;

	}

	/**
	 * 
	 * @return nome del componente
	 */
	public String getNome() {

		return nome;

	}

	/**
	 * Metodo per il settaggio della categoria del componente
	 * 
	 * @param c
	 */
	public void setCategoria(String c) {

		categoria = c;

	}

	/**
	 * 
	 * @return categoria del componente
	 */
	public String getCategoria() {

		return categoria;

	}

	/**
	 * Metodo che modifica la visibilità del componente
	 * 
	 * @param v
	 */
	public void setVisibilita(int v) {

		visibilita = v;

	}

	/**
	 * 
	 * @return la visbilità del componente
	 */
	public int getVisibilita() {

		return visibilita;

	}

	/**
	 * Metodo che modifica l'anfasi del componente
	 * 
	 * @param e
	 */
	public void setEnfasi(int e) {

		enfasi = e;

	}

	/**
	 * 
	 * @return l'enfasi del componente
	 */
	public int getEnfasi() {

		return enfasi;

	}

	public String toString() {
		return nome;
	}

	public String getType() {
		return type;
	}

	public void setType(String t) {
		type = t;
	}

	public Componente clone() {
		try {
			return (Componente) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;

	}

	public boolean isSimple() {
		if ((this.getType().equals(ComponenteAlternative.ALTERNATIVETYPE)) || (this.getType().equals(ComponenteComposto.COMPOSTOTYPE)))
			return false;
		return true;
	}

}
