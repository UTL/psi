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

public class UndoableChangePriority extends AbstractUndoableEdit {
	private DefaultTreeModel model;
	private DefaultMutableTreeNode parent;
	private int oldIndex;
	private int gap;

	/**
	 * 
	 */
	private static final long serialVersionUID = -364657303250331099L;

	UndoableChangePriority(JTree t, DefaultMutableTreeNode p, int oi, int g) {
		model = (DefaultTreeModel) t.getModel();
		parent = p;
		oldIndex=oi;
		gap=g;
	}
	
	public void undo() throws CannotUndoException	{
		Vector<ComponenteSemplice> opzioni = ((ComponenteMolteplice)parent.getUserObject()).getOpzioni();
		Collections.swap(opzioni, oldIndex, oldIndex+gap);
		model.insertNodeInto((DefaultMutableTreeNode) parent.getChildAt(oldIndex+gap), parent, oldIndex);
		model.removeNodeFromParent((DefaultMutableTreeNode) parent.getChildAt(oldIndex+gap));
	}
	
	public void redo() throws CannotUndoException	{
		Vector<ComponenteSemplice> opzioni = ((ComponenteMolteplice)parent.getUserObject()).getOpzioni();
		Collections.swap(opzioni, oldIndex, oldIndex+gap);
		model.insertNodeInto((DefaultMutableTreeNode) parent.getChildAt(oldIndex), parent, oldIndex+gap);
		model.removeNodeFromParent((DefaultMutableTreeNode) parent.getChildAt(oldIndex));
	}
	
	public boolean canUndo()	{
		return true;
	}
	
	public boolean canRedo()	{
		return true;
	}
	
	public String getPresentationName()	{
		return "PriorityChanged";
	}
	
}
