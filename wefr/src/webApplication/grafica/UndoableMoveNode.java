package webApplication.grafica;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;

import webApplication.business.ComponenteMolteplice;
import webApplication.business.ComponenteSemplice;

/**
 * L'oggetto per l'undo di una azione di spostamento di un nodo nell'albero
 * 
 * @author Andrea
 * 
 */
public class UndoableMoveNode extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2678510820175996811L;

	public static final String PRESENTATIONNAME = "Move Node";

	private JTree tree;
	private int oldParentIndex;
	private int newParentIndex;
	private int oldIndex;
	private int newIndex;

	/**
	 * Il costruttore di base
	 * 
	 * @param t		L'albero
	 * @param op	L'indice del precedente genitore
	 * @param np	L'indice del nuovo genitore
	 * @param oi	Il vecchio indice del nodo
	 * @param ni	Il nuovo indice del nodo
	 */
	public UndoableMoveNode(JTree t, int op, int np, int oi, int ni) {
		tree = t;
		oldParentIndex = op;
		newParentIndex = np;
		oldIndex = oi;
		newIndex = ni;
	}

	/**
	 * {@inheritDoc}
	 */
	public void undo() throws CannotUndoException {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DisabledNode root = (DisabledNode) model.getRoot();
		DisabledNode newParent = root;
		DisabledNode node = null;
		if (newParentIndex != -1) {
			newParent = (DisabledNode) root.getChildAt(newParentIndex);
			ComponenteMolteplice parentComp = (ComponenteMolteplice) newParent.getUserObject();
			parentComp.cancellaOpzione(newIndex);
		}
		node = (DisabledNode) newParent.getChildAt(newIndex);
		model.removeNodeFromParent(node);

		DisabledNode oldParent = root;
		if (oldParentIndex != -1) {
			oldParent = (DisabledNode) root.getChildAt(oldParentIndex);
			((ComponenteMolteplice)oldParent.getUserObject()).aggiungiOpzione((ComponenteSemplice) node.getUserObject(), oldIndex);
		}
		model.insertNodeInto(node, oldParent, oldIndex);
		MainWindow.albero.getTree().setSelectionPath(new TreePath(node.getPath()));
		if (node.getAllowsChildren()) {
			MainWindow.albero.getTree().expandPath(new TreePath(node.getPath()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void redo() throws CannotUndoException {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DisabledNode root = (DisabledNode) model.getRoot();
		DisabledNode parent = root;
		System.out.println("Old parent index: "+oldParentIndex);
		System.out.println("OldIndex: "+oldIndex);
		if (oldParentIndex != -1) {
			parent = (DisabledNode) root.getChildAt(oldParentIndex);
			System.out.println("old parent: "+parent);
			((ComponenteMolteplice)parent.getUserObject()).cancellaOpzione(oldIndex);
		}
		DisabledNode node = (DisabledNode) parent.getChildAt(oldIndex);
		System.out.println("node: "+node);
		model.removeNodeFromParent(node);

		System.out.println("New parent index: "+newParentIndex);
		System.out.println("New index: "+newIndex);
		parent = root;
		if (newParentIndex != -1) {
			parent = (DisabledNode) root.getChildAt(newParentIndex);
			ComponenteMolteplice parentComp = (ComponenteMolteplice) parent.getUserObject();
			parentComp.aggiungiOpzione((ComponenteSemplice) node.getUserObject(), newIndex);
		} 
		model.insertNodeInto(node, parent, newIndex);
		MainWindow.albero.getTree().setSelectionPath(new TreePath(node.getPath()));
		if (node.getAllowsChildren()) {
			MainWindow.albero.getTree().expandPath(new TreePath(node.getPath()));
		}
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
