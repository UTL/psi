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

public class UndoableRemoveNode extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8110828293232783974L;
	
	private JTree tree;
	private Componente nodeComp;
	private int parentIndex;
	private int index;
	
	UndoableRemoveNode(JTree t, Componente n, int p, int i)	{
		tree=t;
		nodeComp=n;
		parentIndex=p;
		index=i;
	}
	
	public void undo() throws CannotUndoException	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeComp);
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode parent = findParent();
		if (!(parent.isRoot()) && (!((Componente)parent.getUserObject()).isSimple()))	{
			ComponenteMolteplice parentComp = (ComponenteMolteplice)parent.getUserObject();			
			parentComp.aggiungiOpzione((ComponenteSemplice) node.getUserObject());
		}
		model.insertNodeInto(node, parent, index);
	}
	
	public void redo() throws CannotRedoException	{
		DefaultMutableTreeNode parent = findParent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt(index);
		model.removeNodeFromParent(node);
	}
	
	private DefaultMutableTreeNode findParent()	{
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		if ((!nodeComp.isSimple()) || (parentIndex==-1))	{
			return (DefaultMutableTreeNode) model.getRoot();
		}
		else	{
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			return (DefaultMutableTreeNode) root.getChildAt(parentIndex);
		}
	}
	
	public boolean canUndo()	{
		return true;
	}
	
	public boolean canRedo()	{
		return true;
	}
	
	public String getPresentationName()	{
		return "RemoveNode";
	}

}
