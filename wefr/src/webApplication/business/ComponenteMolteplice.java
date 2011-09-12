package webApplication.business;

import java.io.Serializable;
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
	public void setOpzione(Vector<ComponenteSemplice> cs) {
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
