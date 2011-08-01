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
public class ComponenteAlternative extends Componente implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5660719737295297574L;
	private Vector<ComponenteSemplice> alternative;
	public static String ALTERNATIVETYPE = "Alternative";

	/**
	 * Costruttore di un componenteAlternative
	 */
	public ComponenteAlternative(String n, String c, int v, int e) {
		super(n, c, v, e, ALTERNATIVETYPE);
		alternative = new Vector<ComponenteSemplice>();
	}

	/**
	 * 
	 * @return vettore di componenti semplici che costituiscono le varie
	 *         alternative
	 */
	public Vector<ComponenteSemplice> getAlternative() {
		return alternative;
	}

	/**
	 * Metodo per l'aggiunta di una nuova alternativa al vettore di alternative
	 * 
	 * @param c
	 */
	public void aggiungiAlternativa(ComponenteSemplice c) {
		alternative.add(c);
	}

	/**
	 * Metodo per l'eliminazione di un'alternativa dal vettore si alternative
	 * 
	 * @param i
	 */
	public void cancellaAlternativa(int i) {
		alternative.remove(i);
	}

	/**
	 * 
	 * @param posizione
	 * @return l'alternativa che si trova alla posizione chiesta all'interno del
	 *         vettore
	 */
	public ComponenteSemplice getAlternativa(int posizione) {
		return alternative.get(posizione);
	}

	/**
	 * Metodo che permette la modifica del vettore di alternative
	 * 
	 * @param cs
	 */
	public void setAlternative(Vector<ComponenteSemplice> cs) {
		alternative = cs;
	}

	/**
	 * 
	 * @param n
	 * @return la posizione dell'alternativa cercata tramite il suo nome
	 */
	public int cercaAlternativa(String n) {
		int c = -1;
		for (int i = 0; i < alternative.size(); i++) {
			if ((alternative.get(i).getNome()).equals(n)) {
				c = i;
			}
		}
		return c;
	}

}
