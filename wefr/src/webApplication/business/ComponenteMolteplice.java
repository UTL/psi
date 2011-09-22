package webApplication.business;

import java.io.Serializable;
import java.util.Collections;
import java.util.Vector;

public class ComponenteMolteplice extends Componente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8925750927733776204L;
	protected Vector<ComponenteSemplice> opzioni;

	public ComponenteMolteplice(String n, String c, int v, int e, String tp) {
		super(n, c, v, e, tp);
	}
	
	
	public Vector<ComponenteSemplice> getOpzioni() {
		return opzioni;
	}
	
	/**
	 * Metodo per l'aggiunta di una nuova alternativa al vettore di alternative
	 * 
	 * @param c
	 */
	public void aggiungiOpzione(ComponenteSemplice c) {
		opzioni.add(c);
	}

	/**
	 * Metodo per l'eliminazione di un'alternativa dal vettore si alternative
	 * 
	 * @param i
	 */
	public void cancellaOpzione(int i) {
		opzioni.remove(i);
	}
	
	/**
	 * Metodo per spostare una opzione
	 * @param c L'opzione da spostare
	 * @param p Il salto da fare
	 */
	public void spostaOpzione(ComponenteSemplice c, int p)	{
		int indice = cercaOpzione(c.getNome());
		Vector<ComponenteSemplice> opzioni = getOpzioni();
		Collections.swap(opzioni, indice, indice+p);
	}

	/**
	 * 
	 * @param posizione
	 * @return l'alternativa che si trova alla posizione chiesta all'interno del
	 *         vettore
	 */
	public ComponenteSemplice getOpzione(int posizione) {
		return opzioni.get(posizione);
	}

	/**
	 * Metodo che permette la modifica del vettore di alternative
	 * 
	 * @param cs
	 */
	public void setOpzioni(Vector<ComponenteSemplice> cs) {
		opzioni = cs;
	}

	/**
	 * 
	 * @param n
	 * @return la posizione dell'alternativa cercata tramite il suo nome
	 */
	public int cercaOpzione(String n) {
		int c = -1;
		for (int i = 0; i < opzioni.size(); i++) {
			if ((opzioni.get(i).getNome()).equals(n)) {
				c = i;
			}
		}
		return c;
	}
}
