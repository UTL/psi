package webApplication.grafica;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import webApplication.business.Componente;

public class UndoableChangeField extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7409537247558248907L;
	private DefaultMutableTreeNode root;
	private int parentIndex;
	private int componenteIndex;
	private String fieldName;
	private Object oldValue;
	private Object newValue;
	
	UndoableChangeField(JTree t, int pi, int ci, String f, Object ov, Object nv)	{
		root=(DefaultMutableTreeNode) t.getModel().getRoot();
		parentIndex=pi;
		componenteIndex=ci;
		fieldName=f;
		oldValue=ov;
		newValue=nv;
	}
	
	public void undo() throws CannotUndoException	{
		DefaultMutableTreeNode parent;
		if (parentIndex!=-1)	{
			parent = root;
		}
		else 	{
			parent = (DefaultMutableTreeNode) root.getChildAt(parentIndex);
		}
		Componente comp=(Componente) ((DefaultMutableTreeNode) parent.getChildAt(componenteIndex)).getUserObject();
		if (fieldName==TreePanel.ChangeField.NOME)	{
			comp.setNome((String)oldValue);
		}
		else if (fieldName==TreePanel.ChangeField.CATEGORY)	{
			comp.setCategoria((String)oldValue);
		}
		else if (fieldName==TreePanel.ChangeField.EMPHASIS)	{
			comp.setEnfasi((Integer)oldValue);
		}
		else if (fieldName==TreePanel.ChangeField.VISIBILITY)	{
			comp.setVisibilita((Integer)oldValue);
		}
	}
	
	public void redo() throws CannotRedoException	{
		DefaultMutableTreeNode parent;
		if (parentIndex!=-1)	{
			parent = root;
		}
		else 	{
			parent = (DefaultMutableTreeNode) root.getChildAt(parentIndex);
		}
		Componente comp=(Componente) ((DefaultMutableTreeNode) parent.getChildAt(componenteIndex)).getUserObject();
		if (fieldName==TreePanel.ChangeField.NOME)	{
			comp.setNome((String)newValue);
		}
		else if (fieldName==TreePanel.ChangeField.CATEGORY)	{
			comp.setCategoria((String)newValue);
		}
		else if (fieldName==TreePanel.ChangeField.EMPHASIS)	{
			comp.setEnfasi((Integer)newValue);
		}
		else if (fieldName==TreePanel.ChangeField.VISIBILITY)	{
			comp.setVisibilita((Integer)newValue);
		}
	}
	
	public boolean canUndo()	{
		return true;
	}
	
	public boolean canRedo()	{
		return true;
	}
	
	public String getPresentationName()	{
		return "FieldChanged";
	}

}
