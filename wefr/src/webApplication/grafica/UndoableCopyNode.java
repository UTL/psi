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

public class UndoableCopyNode extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7438827416021865326L;

	
	private JTree tree;
	private Componente nodeComp;
	private int newParentIndex;
	private int newIndex;
	private int oldParentIndex;
	private int oldIndex;
	
	UndoableCopyNode(JTree t, Componente n, int op, int oi, int np, int ni)	{
		tree=t;
		nodeComp=n;
		oldParentIndex=op;
		oldIndex=oi;
		newParentIndex=np;
		newIndex=ni;
	}
	
	public void undo() throws CannotUndoException	{
		//TODO
	}
	
	public void redo() throws CannotRedoException	{
		//TODO
	}
	
	/*private DefaultMutableTreeNode findParent()	{
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		if ((!nodeComp.isSimple()) || (parentIndex==-1))	{
			return (DefaultMutableTreeNode) model.getRoot();
		}
		else	{
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			return (DefaultMutableTreeNode) root.getChildAt(parentIndex);
		}
	}*/
	
	public boolean canUndo()	{
		return true;
	}
	
	public boolean canRedo()	{
		return true;
	}
	
	public String getPresentationName()	{
		return "CopyNode";
	}
}
