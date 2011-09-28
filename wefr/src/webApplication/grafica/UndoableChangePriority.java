package webApplication.grafica;

import java.util.Collections;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;

import webApplication.business.ComponenteMolteplice;
import webApplication.business.ComponenteSemplice;

/**
 * L'oggetto per l'undo di una azione di modifica della priorità di un nodo appartenente ad un componente Alternative
 * @author Andrea
 *
 */
public class UndoableChangePriority extends AbstractUndoableEdit {
	private DefaultTreeModel model;
	private DefaultMutableTreeNode parent;
	private int oldIndex;
	private int gap;

	/**
	 * 
	 */
	private static final long serialVersionUID = -364657303250331099L;

	/**
	 * Il costruttore di base
	 * @param t		L'albero
	 * @param p		Il genitore
	 * @param oi	Il vecchio indice
	 * @param g		L'entità dello spostamento
	 */
	UndoableChangePriority(JTree t, DefaultMutableTreeNode p, int oi, int g) {
		model = (DefaultTreeModel) t.getModel();
		parent = p;
		oldIndex=oi;
		gap=g;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException	{
		Vector<ComponenteSemplice> opzioni = ((ComponenteMolteplice)parent.getUserObject()).getOpzioni();
		Collections.swap(opzioni, oldIndex, oldIndex+gap);
		model.insertNodeInto((DefaultMutableTreeNode) parent.getChildAt(oldIndex+gap), parent, oldIndex);
		model.removeNodeFromParent((DefaultMutableTreeNode) parent.getChildAt(oldIndex+gap));
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotUndoException	{
		Vector<ComponenteSemplice> opzioni = ((ComponenteMolteplice)parent.getUserObject()).getOpzioni();
		Collections.swap(opzioni, oldIndex, oldIndex+gap);
		model.insertNodeInto((DefaultMutableTreeNode) parent.getChildAt(oldIndex), parent, oldIndex+gap);
		model.removeNodeFromParent((DefaultMutableTreeNode) parent.getChildAt(oldIndex));
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#canUndo()
	 */
	public boolean canUndo()	{
		return true;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#canRedo()
	 */
	public boolean canRedo()	{
		return true;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	public String getPresentationName()	{
		return "Priority Change";
	}
	
}
