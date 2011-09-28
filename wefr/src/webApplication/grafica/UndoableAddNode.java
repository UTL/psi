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

/**
 * L'oggetto per l'undo di una azione di aggiunta di un nodo all'albero
 * @author Andrea
 *
 */
public class UndoableAddNode extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5324954463054904036L;
	
	private JTree tree;
	private Componente nodeComp;
	private int parentIndex;
	private int index;
	
	/**
	 * Il costruttore di base
	 * @param t	L'albero
	 * @param n	Il componente
	 * @param p	L'indice del genitore
	 * @param i	L'indice del nodo
	 */
	UndoableAddNode(JTree t, Componente n, int p, int i)	{
		tree=t;
		nodeComp=n;
		parentIndex=p;
		index=i;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException	{
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode parent = findParent();
		DefaultMutableTreeNode node;
		if (parentIndex == -1)	{
			parent = (DefaultMutableTreeNode) model.getRoot();
		}	else	{
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			parent=(DefaultMutableTreeNode) root.getChildAt(parentIndex);
		}
		node = (DefaultMutableTreeNode) parent.getChildAt(index);
		model.removeNodeFromParent(node);
		if (!(parent.isRoot()) && (!((Componente)parent.getUserObject()).isSimple()))	{
			ComponenteMolteplice parentComp = (ComponenteMolteplice)parent.getUserObject();
			parentComp.cancellaOpzione(index);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotRedoException	{
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode parent = findParent();
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeComp);
		if (parentIndex == -1)	{
			parent = (DefaultMutableTreeNode) model.getRoot();
		}	else	{
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			parent=(DefaultMutableTreeNode) root.getChildAt(parentIndex);
		}
		model.insertNodeInto(node, parent, index);
		if (!(parent.isRoot()) && (!((Componente)parent.getUserObject()).isSimple()))	{
			ComponenteMolteplice parentComp = (ComponenteMolteplice)parent.getUserObject();			
			parentComp.aggiungiOpzione((ComponenteSemplice) node.getUserObject());
		}
	}
	
	/**
	 * Cerca il genitore del nodo salvato nel campo nodeComp
	 * @return	Il genitore
	 */
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
		return "Add Node";
	}

}
