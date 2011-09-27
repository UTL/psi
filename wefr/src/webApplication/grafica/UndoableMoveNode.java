package webApplication.grafica;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;

public class UndoableMoveNode extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2678510820175996811L;
	private JTree tree;
	private int oldParentIndex;
	private int newParentIndex;
	private int oldIndex;
	private int newIndex;
	
	UndoableMoveNode(JTree t, int op, int np, int oi, int ni)	{
		tree=t;
		oldParentIndex=op;
		newParentIndex=np;
		oldIndex=oi;
		newIndex=ni;
	}
	
	public void undo() throws CannotUndoException	{
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		DefaultMutableTreeNode parent = root;
		if (newParentIndex!=-1)	{
			parent = (DefaultMutableTreeNode) root.getChildAt(newParentIndex);
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt(newIndex);
		model.removeNodeFromParent(node);
		if (oldParentIndex!=-1)	{
			parent = (DefaultMutableTreeNode) root.getChildAt(oldParentIndex);
		}

		model.insertNodeInto(node, parent, oldIndex);
	}
	
	public void redo() throws CannotUndoException	{
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		DefaultMutableTreeNode parent = root;
		if (oldParentIndex!=-1)	{
			parent = (DefaultMutableTreeNode) root.getChildAt(oldParentIndex);
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt(oldIndex);
		model.removeNodeFromParent(node);
		if (newParentIndex!=-1)	{
			parent = (DefaultMutableTreeNode) root.getChildAt(newParentIndex);
		}
		
		model.insertNodeInto(node, parent, newIndex);
	}
	
	public boolean canUndo()	{
		return true;
	}
	
	public boolean canRedo()	{
		return true;
	}
	
	public String getPresentationName()	{
		return "MoveNode";
	}

}
