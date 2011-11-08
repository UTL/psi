package webApplication.grafica;

import java.util.Collections;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;

import webApplication.business.ComponenteMolteplice;
import webApplication.business.ComponenteSemplice;

/**
 * L'oggetto per l'undo di una azione di modifica della priorità di un nodo
 * appartenente ad un componente Alternative
 * 
 * @author Andrea
 * 
 */
public class UndoableChangePriority extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -364657303250331099L;

	public static final String PRESENTATIONNAME = "Priority Change";

	private JTree tree;
	private DefaultTreeModel model;
	private int parentIndex;
	private int[] oldIndexes;
	private int gap;

	/**
	 * Il costruttore di base
	 * 
	 * @param t
	 *            L'albero
	 * @param pi
	 *            L'indice del genitore
	 * @param oi
	 *            Il vecchio indice
	 * @param g
	 *            L'entità dello spostamento
	 */
	public UndoableChangePriority(JTree t, int pi, int[] oi, int g) {
		tree = t;
		model = (DefaultTreeModel) t.getModel();
		parentIndex = pi;
		oldIndexes = oi;
		gap = g;
	}

	/**
	 * {@inheritDoc}
	 */
	public void undo() throws CannotUndoException {
		DisabledNode parent = (DisabledNode) ((DisabledNode) model.getRoot()).getChildAt(parentIndex);
		// NOTA: nel caso di undo le operazioni di swap vanno fatte in ordine
		// inverso rispetto a quanto fatto nel cambio iniziale
		// dato che giustamente e l'operazione inversa
		if (gap < 0) {
			for (int i = (oldIndexes.length - 1); i >= 0; i--) {
				swappaNodi(parent, i);
			}
		} else {
			for (int i = 0; i < oldIndexes.length; i++) {
				swappaNodi(parent, i);
			}
		}
		// workaround: per fare in modo che la lista si aggiorni cancello la
		// selezione e riseleziono l'elemento
		tree.clearSelection();
		tree.setSelectionPath(new TreePath(parent.getPath()));
		tree.expandPath(new TreePath(parent.getPath()));
	}

	/**
	 * {@inheritDoc}
	 */
	public void redo() throws CannotUndoException {
		DisabledNode parent = (DisabledNode) ((DisabledNode) model.getRoot()).getChildAt(parentIndex);
		// NOTA: nel redo l'ordine delle operazioni di swap torna "normale"
		if (gap < 0) {
			for (int i = 0; i < oldIndexes.length; i++) {
				swappaNodi(parent, i);
			}
		} else {
			for (int i = (oldIndexes.length - 1); i >= 0; i--) {
				swappaNodi(parent, i);
			}
		}
		// workaround: per fare in modo che la lista si aggiorni cancello la
		// selezione e riseleziono l'elemento
		tree.clearSelection();
		tree.setSelectionPath(new TreePath(parent.getPath()));
		tree.expandPath(new TreePath(parent.getPath()));
	}

	/**
	 * Sposta un nodo nell'elenco degli elementi interni del genitore
	 * 
	 * @param p	Il genitore
	 * @param i	L'indice del nodo da spostare
	 */
	private void swappaNodi(DisabledNode p, int i) {
		Vector<ComponenteSemplice> opzioni = ((ComponenteMolteplice) p.getUserObject()).getOpzioni();
		Collections.swap(opzioni, oldIndexes[i], oldIndexes[i] + gap);
		DisabledNode n = (DisabledNode) p.getChildAt(oldIndexes[i] + gap);
		model.removeNodeFromParent(n);
		model.insertNodeInto(n, p, oldIndexes[i]);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canUndo() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canRedo() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPresentationName() {
		return PRESENTATIONNAME;
	}

}
