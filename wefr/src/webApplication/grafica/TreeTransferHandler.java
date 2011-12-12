package webApplication.grafica;

import java.util.List;
import java.util.Vector;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import webApplication.business.Componente;
import webApplication.business.ComponenteMolteplice;
import webApplication.business.ComponenteSemplice;
import webApplication.grafica.TreePanel.CopyAction;
import webApplication.grafica.TreePanel.MoveAction;

/**
 * Gestisce le azioni di copia/taglia/incolla e drag&drop sui nodi dell'albero 
 * @author Andrea
 *
 */
/**
 * @author Andrea
 * 
 */
public class TreeTransferHandler extends TransferHandler implements
		ClipboardOwner {

	// FIXME undo/redo -> sistemare i vari focus o fare refresh dei nodi!

	/**
	 * 
	 */
	private static final long serialVersionUID = 1326376733326817767L;

	private DataFlavor componenteFlavor;
	private DataFlavor nodesFlavor;
	private DataFlavor[] flavors = new DataFlavor[4];
	private DisabledNode[] nodesToRemove;
	private DisabledNode selection;
	private Componente[] compsToCopy;
	private int cont = 1;
	private Clipboard clipboard;
	private CopyAction copyAction;
	private MoveAction moveAction;
	private TreePanel panel;
	private boolean isCopy;

	/**
	 * Il costruttore di base
	 */
	public TreeTransferHandler(TreePanel p) {
		try {
			String mimeType1 = DataFlavor.javaJVMLocalObjectMimeType+ ";class=\""+ Componente[].class.getName()+ "\"";
			componenteFlavor = new DataFlavor(mimeType1);
			flavors[0] = componenteFlavor;
			String mimeType8 = DataFlavor.javaJVMLocalObjectMimeType+ ";class=\""+ DisabledNode[].class.getName()+ "\"";
			nodesFlavor = new DataFlavor(mimeType8);
			flavors[1] = nodesFlavor;
			clipboard = new Clipboard("Tree Clipboard");
			panel = p;
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound: " + e.getMessage());
		}
	}

	/**
	 * Get the clipboard containing copied/cutted node
	 * 
	 * @return The clipboard used for transfering operation
	 */
	protected Clipboard getClipboard() {
		return clipboard;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canImport(TransferSupport support) {
		// controlli per verificare se il drop � valid
		if (support.isDrop()) {// se � una drop verifico direttamente
			support.setShowDropLocation(true);// indica visivamente dove sta avvenendo l'operazione di drop
			// 1. se il data flavor non � supportato allora ritorna false
			if ((!support.isDataFlavorSupported(nodesFlavor)) && (!support.isDataFlavorSupported(componenteFlavor))) {
				return false;
			}
			// 2a. se la DropLocation � la stessa della DragLocation ritorna
			// false
			// 2b. se la DropLocation e la DragLocation sono entrambi elementi
			// composti
			// 2c. se la DragLocation � la root
			JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
			JTree tree = (JTree) support.getComponent();
			TreePath path = dl.getPath();
			int dropRow = tree.getRowForPath(path);
			DisabledNode node = (DisabledNode) path.getLastPathComponent();
			int[] selRows = tree.getSelectionRows();
			for (int i = 0; i < selRows.length; i++) {
				TreePath sourcePath = tree.getPathForRow(selRows[i]);
				DisabledNode source = (DisabledNode) sourcePath.getLastPathComponent();
				if ((selRows[i] == dropRow) || (!node.isRoot() && source.getAllowsChildren() && node.getAllowsChildren())) {
					return false;
				}
				if (source.isRoot()) {
					return false;
				}
			}
			// 3. se la DropLocation � un componente che non permette figli
			if (!node.getAllowsChildren()) {
				return false;
			}
		} else {// se � un copia/taglia dalla clipboard devo estrarre i dati
				// prima
			Transferable trans = clipboard.getContents(null);
			if ((trans == null) || (!trans.isDataFlavorSupported(nodesFlavor)) && (!trans.isDataFlavorSupported(componenteFlavor))) {
				return false;
			}
		}
		// 4. altri???
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Transferable createTransferable(JComponent c) {
		// NOTA: i dati li salvo in un campo locale per facilit� di accesso, ma
		// potrei estrarli dal transferable ogni volta che servono
		JTree tree = (JTree) c;
		TreePath path = tree.getSelectionPath();
		if (path != null) {
			DisabledNode node = (DisabledNode) path.getLastPathComponent();
			// Salvo le informazioni sulla posizione attuale del nodo per l'undo
			copyAction = panel.new CopyAction();
			moveAction = panel.new MoveAction();
			int oldIndex = node.getParent().getIndex(node);
			moveAction.putValue(TreePanel.MoveAction.OLDINDEX, oldIndex);
			if (((DisabledNode) node.getParent()).isRoot()) {
				moveAction.putValue(TreePanel.MoveAction.OLDPARENTINDEX, -1);
			} else {
				int oldParentIndex = ((DisabledNode) tree.getModel().getRoot()).getIndex(node.getParent());
				moveAction.putValue(TreePanel.MoveAction.OLDPARENTINDEX, oldParentIndex);
			}
			Componente comp = (Componente) node.getUserObject();
			List<Componente> compCopies = new ArrayList<Componente>();
			List<DisabledNode> toRemove = new ArrayList<DisabledNode>(); // lista dei nodi da rimuovere
			//NOTA: memorizzo i nodi tanto non mi serve sapere se erano Componente!
			toRemove.add(node); // aggiungo il nodo originale agli elementi da eliminare
			Componente compCopy = copy(comp);
			compCopies.add(compCopy);
			// se il nodo corrente pu� avere dei figli devo copiare anche quelli
			if (node.getAllowsChildren()) {
				for (int i = 0; i < node.getChildCount(); i++) {
					DisabledNode childNode = (DisabledNode) node.getChildAt(i);
					Componente childComp = (Componente) childNode.getUserObject();
					Componente childCompCopy = copy(childComp);
					toRemove.add(childNode);
					compCopies.add(childCompCopy);
				}
			}
			compsToCopy = compCopies.toArray(new Componente[compCopies.size()]);
			nodesToRemove = toRemove.toArray(new DisabledNode[toRemove.size()]);
			return new ComponenteSelection(compsToCopy);
		}
		// non ho nulla selezionato
		return null;
	}

	/**
	 * Crea una copia del nodo
	 * 
	 * @param comp
	 *            Il nodo da copiare
	 * @return Il nodo copiato
	 */
	private Componente copy(Componente comp) {
		Componente compCopy = comp.clone();
		return compCopy;
	}

	/**
	 * {@inheritDoc}
	 */
	public void exportDone(JComponent source, Transferable data, int action) {
		System.out.println("Inizio ad esportare");
		if (action == MOVE) {
			System.out.println("Drop - Taglia");
			JTree tree = (JTree) source;
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			// Rimuovo gli elementi indicati per la rimozione
			for (int i = 0; i < nodesToRemove.length; i++) {
				DisabledNode parent = (DisabledNode) nodesToRemove[i].getParent();
				// Se il parent era un nodo composto o alternative elimino
				// l'elemento dall'elenco
				if (!parent.isRoot()) {
					ComponenteMolteplice parentComp = (ComponenteMolteplice) parent.getUserObject();
					int indiceComponente = parentComp.cercaOpzione(((Componente) nodesToRemove[i].getUserObject()).getNome());
					parentComp.cancellaOpzione(indiceComponente);
				}
				model.removeNodeFromParent(nodesToRemove[i]);
			}
			moveAction.actionPerformed(new ActionEvent(this, MOVE, "MoveAction"));
		} else if (action == COPY) {
			System.out.println("Drop - Copia");
			copyAction.actionPerformed(new ActionEvent(this, COPY, "CopyAction"));
		}
		MainWindow.albero.getTree().setSelectionPath(new TreePath(selection.getPath()));
		MainWindow.properties.showProperties(selection);
		System.out.println("Terminata esportazione");
	}

	/**
	 * {@inheritDoc}
	 */
	public void exportToClipboard(JComponent c, Clipboard clip, int action) {
		System.out.println("Inizio ad esportare nella clipboard...");
		Transferable treeTransferable = createTransferable(c);
		if (compsToCopy == null) {
			return;
		}
		if ((action == MOVE) && (nodesToRemove != null)) {
			System.out.println("CCP - Taglia");
			isCopy = false;
			JTree tree = (JTree) c;
//			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			// Rimuovo gli elementi indicati per la rimozione
			for (int i = 0; i <nodesToRemove.length ; i++) {
				nodesToRemove[i].setEnabled(false);
			}
			tree.repaint();
			//model.reload();
		} else if ((action == COPY)) {
			System.out.println("CCP - Copia");
			isCopy = true;
		}
		clipboard.setContents(treeTransferable, this);
		System.out.println("Terminata esportazione nella clipboard...");
	}

	/**
	 * {@inheritDoc}
	 */
	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean importData(TransferHandler.TransferSupport support) {
		System.out.println("Inizio ad importare...");
		DisabledNode parent = null;
		int index = -1;
		if (!canImport(support)) {
			return false;
		}
		JTree tree = (JTree) support.getComponent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		if (support.isDrop()) {
			System.out.println("Operazione di drop");
			if (support.getDropAction() == COPY) {
				renameComponenti();
			}
			// Recupero la location per il drop
			JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
			int childIndex = dl.getChildIndex();
			TreePath dest = dl.getPath();
			parent = (DisabledNode) dest.getLastPathComponent();
			// Configure for drop mode.
			index = childIndex; // DropMode.INSERT
			if (childIndex == -1) { // DropMode.ON
				index = parent.getChildCount();
			}
		} else {
			System.out.println("Operazione di paste");
			//In questo caso i dati li recupero dalla clipboard
			Transferable trans = clipboard.getContents(null);
			try {
				compsToCopy = (Componente[]) trans.getTransferData(componenteFlavor);
			} catch (UnsupportedFlavorException ufe) {
				System.out.println("UnsupportedFlavor: " + ufe.getMessage());
			} catch (java.io.IOException ioe) {
				System.out.println("I/O error: " + ioe.getMessage());
			}
			TreePath path = tree.getSelectionPath();
			DisabledNode node = null;
			if (path != null) {
				node = (DisabledNode) path.getLastPathComponent();
			} else {
				node = (DisabledNode) (tree.getModel()).getRoot();
			}
			// Se il nodo da spostare � composto, l'unico parent concesso � la
			// root e come posizione sar� il successivo al target corrente
			if (!compsToCopy[0].isSimple()) {
				parent = (DisabledNode) tree.getModel().getRoot();
				index = parent.getChildCount();
			} else { // se non � composto allora � un nodo semplice
				if (!node.getAllowsChildren()) {
					// se il target non permette figli, il parent del target corrente va bene
					parent = (DisabledNode) node.getParent();
					index = parent.getChildCount();
				} else {
					// se il target corrente consente figli allora il target va bene e sar� messo in coda
					parent = node;
					index = node.getChildCount();
				}
			}

			if (isCopy) {
				renameComponenti();
			}

		}

		//Setto i valori della nuova posizione nelle action di copia e taglia
		if (parent.isRoot()) {
			copyAction.putValue(TreePanel.CopyAction.PARENTINDEX, -1);
			moveAction.putValue(TreePanel.MoveAction.NEWPARENTINDEX, -1);
		} else {
			int parentIndex = parent.getParent().getIndex(parent);
			copyAction.putValue(TreePanel.CopyAction.PARENTINDEX, parentIndex);
			moveAction.putValue(TreePanel.MoveAction.NEWPARENTINDEX, parentIndex);
		}
		copyAction.putValue(TreePanel.CopyAction.INDEX, index);
		moveAction.putValue(TreePanel.MoveAction.NEWINDEX, index);

		selection = null;
		// Inserisco i dati
		for (int i = 0; i < compsToCopy.length; i++) {
			DisabledNode nodeToInsert = new DisabledNode(compsToCopy[i]);
			if (i==0) {
				selection = nodeToInsert;
			}
			if (compsToCopy[i].isSimple()) {
				nodeToInsert.setAllowsChildren(false);
			}
			//FIXME DEVO AGGIORNARE ANCHE I NOMI DENTRO LA LISTA!
			model.insertNodeInto(nodeToInsert, parent, index++);
			if (i == 0) {
				// NOTA: fatta una verifica anche sul tipo dato per sicurezza
				if ((!parent.isRoot()) && (compsToCopy[i].isSimple())) {
					Componente parentComp = (Componente) parent.getUserObject();
					((ComponenteMolteplice) parentComp).aggiungiOpzione((ComponenteSemplice) compsToCopy[i], index-1);
				}
				if (!compsToCopy[i].isSimple()) {
					parent = nodeToInsert;
					index = 0;
				}
			}
		}

		// NOTA: se sono su una paste, con CCP ho gia fatto l'export quindi
		// l'azione e gia terminata e lancio l'evento
		if (!support.isDrop()) {
			if (isCopy) {
				copyAction.actionPerformed(new ActionEvent(this, COPY, "CopyAction"));
			} else {
				for (int i=0; i<nodesToRemove.length; i++)	{
					parent = (DisabledNode) nodesToRemove[i].getParent(); 
					if (!parent.isRoot()) {
						ComponenteMolteplice parentComp = (ComponenteMolteplice) parent.getUserObject();
						int indiceComponente = parentComp.cercaOpzione(((Componente) nodesToRemove[i].getUserObject()).getNome());
						parentComp.cancellaOpzione(indiceComponente);
					}
					// se era un taglia rimuovo i nodi
					model.removeNodeFromParent(nodesToRemove[i]);
				}
				moveAction.actionPerformed(new ActionEvent(this, MOVE, "MoveAction"));
			}
			MainWindow.albero.getTree().setSelectionPath(new TreePath(selection.getPath()));
			MainWindow.properties.showProperties(selection);
		}
		// Cambio il valore di isCopy perche eventuali ulteriori paste, anche se
		// l'origine era cut ora sono tutte copy
		if (!parent.isRoot()) {
			tree.expandPath(new TreePath(parent.getPath()));
		}
		isCopy = true;
		System.out.println("Terminata importazione");
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return getClass().getName();
	}

	
	private void renameComponenti() {
		for (int i = 0; i < compsToCopy.length; i++) {
			compsToCopy[i] = renameComponente(compsToCopy[i]);
		}
		if (compsToCopy.length != 1) {
			Vector<ComponenteSemplice> nuoveOpzioni = new Vector<ComponenteSemplice>();
			for (int i=1; i<compsToCopy.length;i++) {
				nuoveOpzioni.add((ComponenteSemplice) compsToCopy[i]);
			}
			((ComponenteMolteplice)compsToCopy[0]).setOpzioni(nuoveOpzioni);
		}
	}
	
	/**
	 * Modifica il campo Nome dell'oggetto Componente
	 * 
	 * @param comp	Il componente il cui nome deve essere modificato
	 * @return		Il nuovo componente con il nome modificato
	 */
	private Componente renameComponente(Componente comp) {
		System.out.println("Inizio a rinominare...");
		Componente newComp = copy(comp);
		String name = comp.getNome();
		int beginIndex = name.lastIndexOf('(');
		if (beginIndex == -1) {
			beginIndex = comp.getNome().length();
		}
		String newName = name.substring(0, beginIndex);
		newName = newName + '(' + cont + ')';
		newComp.setNome(newName);
		cont++;
		System.out.println("Terminato di rinominare...");
		return newComp;
	}

	/**
	 * Annulla una azione di taglia
	 */
	protected void cancelCut() {
		// NOTA: nel caso l'utente faccia un cut non seguito da paste, ma da
		// altra azione, trasformo il cut in una azione di remove undoable
		for (int i=0; i<nodesToRemove.length; i++)	{
			nodesToRemove[i].setEnabled(true);
		}
		clipboard.removeFlavorListener(MainWindow.eventDispatcher);
		clipboard.setContents(null, null);
		clipboard.addFlavorListener(MainWindow.eventDispatcher);
		MainWindow.pasteState(false);
		TreePath path = MainWindow.albero.getTree().getSelectionPath();
		if (path == null) {
			path = (new TreePath(nodesToRemove[0].getPath()));
		}
		MainWindow.albero.getTree().repaint();
	}

	/**
	 * Implementa il dataflavor dell'oggetto Nodo usato per il drag&drop
	 * 
	 * @author Andrea
	 * 
	 */
	class NodeSelection implements Transferable {
		DisabledNode[] nodes;

		/**
		 * Il costruttore di base
		 * 
		 * @param n
		 *            i nodi da spostare
		 */
		NodeSelection(DisabledNode[] n) {
			nodes = n;
		}

		/**
		 * {@inheritDoc}
		 */
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return nodes;
		}

		/**
		 * {@inheritDoc}
		 */
		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (nodesFlavor.equals(flavor)) {
				System.out.println("Node data flavor");
				return true;
			}
			System.out.println("Node data flavor fuori if");
			return false;
		}
	}

	/**
	 * Implementa il dataflavor dell'oggetto componente usato per il drag&drop
	 * 
	 * @author Andrea
	 * 
	 */
	class ComponenteSelection implements Transferable {
		Componente[] comps;

		/**
		 * Costruttore di base
		 * 
		 * @param n
		 *            I nodi da spostare
		 */
		ComponenteSelection(Componente[] n) {
			comps = n;
		}

		/**
		 * {@inheritDoc}
		 */
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return comps;
		}

		/**
		 * {@inheritDoc}
		 */
		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (componenteFlavor.equals(flavor)) {
				return true;
			}
			return false;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// non mi serve
		System.out.println("Ownership lost");
	}

}
