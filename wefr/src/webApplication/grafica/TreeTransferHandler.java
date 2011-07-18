package webApplication.grafica;

import java.util.List;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class TreeTransferHandler extends TransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1326376733326817767L;
	private DataFlavor componenteFlavor;
	private DataFlavor alternativeFlavor;
	private DataFlavor compositeFlavor;
	private DataFlavor textFlavor;
	private DataFlavor imageFlavor;
	private DataFlavor linkFlavor;
    private DataFlavor nodesFlavor;
    private DataFlavor[] flavors = new DataFlavor[7];
    private DefaultMutableTreeNode[] nodesToRemove;
    
    /**
     * 
     */
    public TreeTransferHandler() {
        try {
        	String mimeType1 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.Componente.class.getName()+"\"";
        	System.out.println(mimeType1);
        	componenteFlavor = new DataFlavor(mimeType1);
        	flavors[0] = componenteFlavor;
        	String mimeType2 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.ComponenteAlternative.class.getName()+"\"";
        	nodesFlavor = new DataFlavor(mimeType2);
        	flavors[1] = alternativeFlavor;
        	String mimeType3 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.ComponenteComposto.class.getName()+"\"";
        	nodesFlavor = new DataFlavor(mimeType3);
        	flavors[2] = compositeFlavor;
        	String mimeType4 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.Immagine.class.getName()+"\"";
        	nodesFlavor = new DataFlavor(mimeType4);
        	flavors[3] = imageFlavor;
        	String mimeType5 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.Link.class.getName()+"\"";
        	nodesFlavor = new DataFlavor(mimeType5);
        	flavors[4] = linkFlavor;
        	String mimeType6 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.Testo.class.getName()+"\"";
        	nodesFlavor = new DataFlavor(mimeType6);
        	flavors[5] = textFlavor;
        	String mimeType8 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + javax.swing.tree.DefaultMutableTreeNode[].class.getName() + "\"";
        	nodesFlavor = new DataFlavor(mimeType8);
        	flavors[6] = nodesFlavor;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }
    
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#canImport(javax.swing.TransferHandler.TransferSupport)
     */
    @Override
    public boolean canImport(TransferSupport support)	{
    	//controlli per verificare se il drop è valido
    	//1. se l'operazione non è di drop allora ritorna false
    	if (!support.isDrop())	{
    		return false;
    	}
    	//indica visivamente dove sta avvenendo l'operazione di drop
    	support.setShowDropLocation(true);
    	//3. se il data flavor non è supportato allora ritorna false
        if (!support.isDataFlavorSupported(nodesFlavor)) {
            return false;
        }
        //4. se la DropLocation è la stessa della DragLocation ritorna false
        JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
        JTree tree = (JTree) support.getComponent();
        TreePath path = dl.getPath();
        int dropRow = tree.getRowForPath(path);
        int[] selRows = tree.getSelectionRows();
        for (int i = 0; i < selRows.length; i++) {
            if (selRows[i] == dropRow) {
                return false;
            }
        }
        //5. se la DropLocation è un componente che non permette figli
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        if (!node.getAllowsChildren())	{
        	return false;
        }
        //6. altre???
    	return true;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
     */
    @Override
    protected Transferable createTransferable(JComponent c)	{
    	JTree tree = (JTree) c;
    	/**
    	 * TODO Modificare per creare la copia di un singolo elemento se è un componente semplice, o la copia di tutti i suoi figli se è complesso
    	 */
    	TreePath path = tree.getSelectionPath();
    	if (path!=null)	{
    		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
    		List<DefaultMutableTreeNode> copies = new ArrayList<DefaultMutableTreeNode>(); //lista della copia degli elementi
    		List<DefaultMutableTreeNode> toRemove = new ArrayList<DefaultMutableTreeNode>(); //lista degli elementi da rimuovere
    		DefaultMutableTreeNode nodeCopy = copy(node); //faccio una copia dell'elemento selezionato
    		copies.add(nodeCopy); //aggiungo la copia agli elementi da copiare
    		toRemove.add(node); //aggiungo il nodo originale agli elementi da eliminare
    		// se il nodo corrente può avere dei figli devo copiare anche quelli
    		if (node.getAllowsChildren())	{
    			for (int i=0; i<node.getChildCount(); i++)	{
    				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
    				DefaultMutableTreeNode childNodeCopy = copy(childNode);
    				copies.add(childNodeCopy);
    				toRemove.add(childNode);
    			}
    		}
    		DefaultMutableTreeNode[] nodesToCopy = copies.toArray(new DefaultMutableTreeNode[copies.size()]);
    		nodesToRemove = toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
    		return new NodeTransferable(nodesToCopy);
    		
    	}
    	//non ho nulla selezionato
        return null;
    }
    
    /**
     * Esegue una copia dell'elemento dell'albero
     * @param node
     * @return
     */
    private DefaultMutableTreeNode copy(DefaultMutableTreeNode node) {
        return new DefaultMutableTreeNode(node);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#exportDone(javax.swing.JComponent, java.awt.datatransfer.Transferable, int)
     */
    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        System.out.println("Inizio ad esportare");
        if ((action & MOVE) == MOVE) {
            JTree tree = (JTree) source;
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            // Rimuovo gli elementi indicati per la rimozione
            for (int i = 0; i < nodesToRemove.length; i++) {
                model.removeNodeFromParent(nodesToRemove[i]);
            }
        }
        /**
         * TODO aggiungere gestione del copy
         */
        System.out.println("Ho finito di esportare");
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
     */
    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#importData(javax.swing.TransferHandler.TransferSupport)
     */
    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
    	/**
    	 * TODO genera errore nel cast del cellrenderer...forse devo rendere transferable ogni singolo elemento???
    	 */
        System.out.println("Inizio ad importare i dati");
        if (!canImport(support)) {
            return false;
        }
        // Estraggo i dati
        DefaultMutableTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (DefaultMutableTreeNode[]) t.getTransferData(nodesFlavor);
        } catch (UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch (java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
        // Recupero la location per il drop
        JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dest.getLastPathComponent();
        JTree tree = (JTree) support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        // Configure for drop mode.
        int index = childIndex; // DropMode.INSERT
        if (childIndex == -1) { // DropMode.ON
            index = parent.getChildCount();
        }
        // Aggiungo i dati al modello
        for (int i = 0; i < nodes.length; i++) {
        //    if (JOptionPane.showConfirmDialog(null, "Confirm drop of " + nodes[i].toString(), "Confirm Drop", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
        	model.insertNodeInto(nodes[i], parent, index++);
        //    }
        }
        /**
         * TODO e se l'azione è un copy???
         */
        System.out.println("Import terminato");
        return true;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getName();
    }

    
    class NodeTransferable implements Transferable	{
    	DefaultMutableTreeNode[] nodes;
    	
    	NodeTransferable(DefaultMutableTreeNode[] n)	{
    		nodes=n;
    	}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException	{
			if (!isDataFlavorSupported(flavor))	{
				throw new UnsupportedFlavorException(flavor);
			}
			return nodes;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			System.out.println("Classe rappresentante: "+flavor.getRepresentationClass());
			if (nodesFlavor.equals(flavor) || componenteFlavor.equals(flavor) || alternativeFlavor.equals(flavor) || textFlavor.equals(flavor) || imageFlavor.equals(flavor) || linkFlavor.equals(flavor) || compositeFlavor.equals(flavor))	{
				return true;
			}
			return false;
		}
    	
    }

}
