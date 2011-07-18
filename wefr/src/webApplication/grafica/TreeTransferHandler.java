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

import webApplication.business.Componente;

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
        	String mimeType1 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.Componente[].class.getName()+"\"";
        	componenteFlavor = new DataFlavor(mimeType1);
        	flavors[0] = componenteFlavor;
        	String mimeType2 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.ComponenteAlternative[].class.getName()+"\"";
        	nodesFlavor = new DataFlavor(mimeType2);
        	flavors[1] = alternativeFlavor;
        	String mimeType3 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.ComponenteComposto[].class.getName()+"\"";
        	nodesFlavor = new DataFlavor(mimeType3);
        	flavors[2] = compositeFlavor;
        	String mimeType4 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.Immagine[].class.getName()+"\"";
        	nodesFlavor = new DataFlavor(mimeType4);
        	flavors[3] = imageFlavor;
        	String mimeType5 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.Link[].class.getName()+"\"";
        	nodesFlavor = new DataFlavor(mimeType5);
        	flavors[4] = linkFlavor;
        	String mimeType6 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.Testo[].class.getName()+"\"";
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
    		System.out.println("Non puoi droppare qui");
    		return false;
    	}
    	//indica visivamente dove sta avvenendo l'operazione di drop
    	support.setShowDropLocation(true);
    	//3. se il data flavor non è supportato allora ritorna false
        if (!support.isDataFlavorSupported(nodesFlavor)) {
        	System.out.println("Tipo non supportato");
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
    		Componente comp = (Componente) node.getUserObject();
    		List<Componente> compCopies = new ArrayList<Componente>();
    		List<Componente> compToRemove = new ArrayList<Componente>();
    		Componente compCopy = copy(comp);
    		compCopies.add(compCopy);
    		compToRemove.add(comp);
    		List<DefaultMutableTreeNode> toRemove = new ArrayList<DefaultMutableTreeNode>(); //lista dei nodi da rimuovere->NOTA: memorizzo i nodi tanto mi serve sapere se erano Componente!
    		toRemove.add(node); //aggiungo il nodo originale agli elementi da eliminare*/
    		// se il nodo corrente può avere dei figli devo copiare anche quelli
    		if (node.getAllowsChildren())	{
    			for (int i=0; i<node.getChildCount(); i++)	{
    				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
    				Componente childComp = (Componente) childNode.getUserObject();
    				Componente childCompCopy = copy(childComp);
    				toRemove.add(childNode);
    				compCopies.add(childCompCopy);
    				compToRemove.add(childComp);
    			}
    		}
    		Componente[] compsToCopy = compCopies.toArray(new Componente[compCopies.size()]);
    		nodesToRemove = toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
    		return new ComponenteTransferable(compsToCopy);
    	}
    	//non ho nulla selezionato
        return null;
    }
    
    private Componente copy(Componente comp) {
    	Componente compCopy = comp.clone();
        return compCopy;
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
        System.out.println("Inizio ad importare i dati");
        if (!canImport(support)) {
            return false;
        }
        // Estraggo i dati
        Componente[] comps = null;
        try {
            Transferable t = support.getTransferable();
            //nodes = (DefaultMutableTreeNode[]) t.getTransferData(nodesFlavor);
            //System.out.println("Ho creato il nodo[]...");
            comps = (Componente[]) t.getTransferData(componenteFlavor); //non funziona
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
        for (int i = 0; i < comps.length; i++) {
        //    if (JOptionPane.showConfirmDialog(null, "Confirm drop of " + nodes[i].toString(), "Confirm Drop", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
        	model.insertNodeInto(new DefaultMutableTreeNode(comps[i]), parent, index++);
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
    	//Componente[] comps;
    	
    	NodeTransferable(DefaultMutableTreeNode[] n)	{
    		nodes=n;
    		//comps=(Componente[])n;
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
			if (nodesFlavor.equals(flavor))	{
				return true;
			}
			return false;
		}
    	
    }
    
    class ComponenteTransferable implements Transferable	{
    	Componente[] comps;
    	
    	ComponenteTransferable(Componente[] n)	{
    		comps=n;
    	}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException	{
			if (!isDataFlavorSupported(flavor))	{
				throw new UnsupportedFlavorException(flavor);
			}
			return comps;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (componenteFlavor.equals(flavor))	{
				return true;
			}
			return false;
		}
    	
    }

}
