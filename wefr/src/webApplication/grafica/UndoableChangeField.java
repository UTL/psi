package webApplication.grafica;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import webApplication.business.Componente;

/**
 * L'oggetto per l'undo di una azione di modifica di un campo di un nodo
 * @author Andrea
 *
 */
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
	
	/**
	 * Il costruttore di base
	 * @param t		L'albero
	 * @param pi	L'indice del genitore
	 * @param ci	L'indice del componente
	 * @param f		Il nome campo
	 * @param ov	Il vecchio valore
	 * @param nv	Il nuovo valore
	 */
	UndoableChangeField(JTree t, int pi, int ci, String f, Object ov, Object nv)	{
		root=(DefaultMutableTreeNode) t.getModel().getRoot();
		parentIndex=pi;
		componenteIndex=ci;
		fieldName=f;
		oldValue=ov;
		newValue=nv;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
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
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
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
		return "Field Changed";
	}

}
