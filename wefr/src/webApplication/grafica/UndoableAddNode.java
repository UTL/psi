package webApplication.grafica;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import webApplication.business.Componente;
import webApplication.business.ComponenteMolteplice;
import webApplication.business.ComponenteSemplice;

public class UndoableAddNode extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5324954463054904036L;
	
	private DefaultTreeModel model;
	private DefaultMutableTreeNode node;
	private DefaultMutableTreeNode parent;
	private int index;
	
	UndoableAddNode(JTree t, DefaultMutableTreeNode n, DefaultMutableTreeNode p, int i)	{
		model=(DefaultTreeModel) t.getModel();
		node=n;
		parent=p;
		index=i;
	}
	
	public void undo() throws CannotUndoException	{
		model.removeNodeFromParent(node);
		if (!((Componente)parent.getUserObject()).isSimple())	{
			ComponenteMolteplice parentComp = (ComponenteMolteplice)parent.getUserObject();
			parentComp.cancellaOpzione(index);
		}
	}
	
	public void redo() throws CannotRedoException	{
		model.insertNodeInto(node, parent, index);
		if (!((Componente)parent.getUserObject()).isSimple())	{
			ComponenteMolteplice parentComp = (ComponenteMolteplice)parent.getUserObject();			
			parentComp.aggiungiOpzione((ComponenteSemplice) node.getUserObject());
		}
	}
	
	public boolean canUndo()	{
		return true;
	}
	
	public boolean canRedo()	{
		return true;
	}
	
	public String getPresentationName()	{
		return "AddNode";
	}

}
