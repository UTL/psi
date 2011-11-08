package webApplication.grafica;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import webApplication.business.Componente;
import webApplication.business.Immagine;
import webApplication.business.Testo;
import webApplication.business.Link;

/**
 * L'oggetto per l'undo di una azione di modifica di un campo di un nodo
 * 
 * @author Andrea
 * 
 */
public class UndoableChangeField extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7409537247558248907L;

	private static final String PRESENTATIONNAME = "Field changed";

	private JTree tree;
	private DisabledNode root;
	private int parentIndex;
	private int componenteIndex;
	private String fieldName;
	private Object oldValue;
	private Object newValue;

	/**
	 * Il costruttore di base
	 * 
	 * @param t		L'albero
	 * @param pi	L'indice del genitore
	 * @param ci	L'indice del componente
	 * @param f		Il nome campo
	 * @param ov	Il vecchio valore
	 * @param nv	Il nuovo valore
	 */
	public UndoableChangeField(JTree t, int pi, int ci, String f, Object ov, Object nv) {
		tree = t;
		root = (DisabledNode) t.getModel().getRoot();
		parentIndex = pi;
		componenteIndex = ci;
		fieldName = f;
		oldValue = ov;
		newValue = nv;
	}

	/**
	 * {@inheritDoc}
	 */
	public void undo() throws CannotUndoException {
		//rimuovo i listener per evitare loop sulla modifica del campo
		MainWindow.properties.removeListeners();
		
		DisabledNode parent;
		if (parentIndex == -1) {
			parent = root;
		} else {
			parent = (DisabledNode) root.getChildAt(parentIndex);
		}
		DisabledNode node = (DisabledNode) parent.getChildAt(componenteIndex);
		Componente comp = (Componente) (node).getUserObject();
		if (fieldName.equals(TreePanel.ChangeFieldAction.NAME)) {
			comp.setNome((String) oldValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.CATEGORY)) {
			comp.setCategoria((String) oldValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.EMPHASIS)) {
			comp.setEnfasi((Integer) oldValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.VISIBILITY)) {
			comp.setVisibilita((Integer) oldValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.TEXT)) {
			((Testo)comp).setTesto((String) oldValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.IMAGE)) {
			((Immagine)comp).setPath((String) oldValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.URLPATH)) {
			((Link)comp).setUri((String) oldValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.URLTEXT)) {
			((Link)comp).setTesto((String) oldValue);
		}
		
		//riseleziono l'elemento per refreshare la vista e riaggiungere i listener
		tree.clearSelection();
		tree.setSelectionPath(new TreePath(node.getPath()));
	}

	/**
	 * {@inheritDoc}
	 */
	public void redo() throws CannotRedoException {
		//rimuovo i listener per evitare loop sulla modifica del campo
		MainWindow.properties.removeListeners();
		
		DisabledNode parent;
		if (parentIndex == -1) {
			parent = root;
		} else {
			parent = (DisabledNode) root.getChildAt(parentIndex);
		}
		Componente comp = (Componente) ((DisabledNode) parent.getChildAt(componenteIndex)).getUserObject();
		if (fieldName.equals(TreePanel.ChangeFieldAction.NAME)) {
			comp.setNome((String) newValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.CATEGORY)) {
			comp.setCategoria((String) newValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.EMPHASIS)) {
			comp.setEnfasi((Integer) newValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.VISIBILITY)) {
			comp.setVisibilita((Integer) newValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.TEXT)) {
			((Testo)comp).setTesto((String) newValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.IMAGE)) {
			((Immagine)comp).setPath((String) newValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.URLPATH)) {
			((Link)comp).setUri((String) newValue);
		} else if (fieldName.equals(TreePanel.ChangeFieldAction.URLTEXT)) {
			((Link)comp).setTesto((String) newValue);
		}
		
		//riseleziono l'elemento per refreshare la vista e riaggiungere i listener
		tree.clearSelection();
		tree.setSelectionPath(new TreePath(((DisabledNode) parent.getChildAt(componenteIndex)).getPath()));
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
