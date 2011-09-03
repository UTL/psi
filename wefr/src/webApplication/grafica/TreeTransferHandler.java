package webApplication.grafica;

import java.util.List;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
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
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.ComponenteSemplice;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

public class TreeTransferHandler extends TransferHandler implements ClipboardOwner	{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1326376733326817767L;
	private DataFlavor componenteFlavor;
    private DataFlavor nodesFlavor;
    private DataFlavor[] flavors = new DataFlavor[4];
    private DefaultMutableTreeNode[] nodesToRemove;
    private Componente[] compsToCopy;
    private int cont = 1;
    private Clipboard clipboard;
    
    /**
     * 
     */
    public TreeTransferHandler() {
        try {
        	String mimeType1 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.Componente[].class.getName()+"\"";
        	componenteFlavor = new DataFlavor(mimeType1);
        	flavors[0] = componenteFlavor;
        	String mimeType8 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + javax.swing.tree.DefaultMutableTreeNode[].class.getName() + "\"";
        	nodesFlavor = new DataFlavor(mimeType8);
        	flavors[1] = nodesFlavor;
        	clipboard = new Clipboard("Tree Clipboard");
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }
    
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#canImport(javax.swing.TransferHandler.TransferSupport)
     */
    @Override
    public boolean canImport(TransferSupport support)	{
    	//controlli per verificare se il drop � valid
    	if (support.isDrop())	{//se � una drop verifico direttamente
    		support.setShowDropLocation(true);//indica visivamente dove sta avvenendo l'operazione di drop
    		//1. se il data flavor non � supportato allora ritorna false
            if ((!support.isDataFlavorSupported(nodesFlavor)) && (!support.isDataFlavorSupported(componenteFlavor)))	{
                return false;
            }
            //2a. se la DropLocation � la stessa della DragLocation ritorna false
            //2b. se la DropLocation e la DragLocation sono entrambi elementi composti
            //2c. se la DragLocation � la root
            JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
            JTree tree = (JTree) support.getComponent();
            TreePath path = dl.getPath();
            int dropRow = tree.getRowForPath(path);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
            int[] selRows = tree.getSelectionRows();
            for (int i = 0; i < selRows.length; i++) {
            	TreePath sourcePath = tree.getPathForRow(selRows[i]);
            	DefaultMutableTreeNode source = (DefaultMutableTreeNode) sourcePath.getLastPathComponent();
                if ((selRows[i] == dropRow) || (!node.isRoot() && source.getAllowsChildren() && node.getAllowsChildren()))	{
                    return false;
                }
                if (source.isRoot())	{
                	return false;
                }
            }
            //3. se la DropLocation � un componente che non permette figli
            if (!node.getAllowsChildren())	{
            	return false;
            }
    	}
    	else	{//se � un copia/taglia dalla clipboard devo estrarre i dati prima
    		Transferable trans = clipboard.getContents(null);
            if ((trans==null) || (!trans.isDataFlavorSupported(nodesFlavor)) && (!trans.isDataFlavorSupported(componenteFlavor)))	{
                return false;
            }
            //verifiche da fare se � un incolla?
    	}
        //4. altri???
    	return true;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
     */
    @Override
    protected Transferable createTransferable(JComponent c)	{
    	JTree tree = (JTree) c;
    	TreePath path = tree.getSelectionPath();
    	if (path!=null)	{
    		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
    		Componente comp = (Componente) node.getUserObject();
    		List<Componente> compCopies = new ArrayList<Componente>();
    		List<DefaultMutableTreeNode> toRemove = new ArrayList<DefaultMutableTreeNode>(); //lista dei nodi da rimuovere->NOTA: memorizzo i nodi tanto mi serve sapere se erano Componente!
    		//Se il parent era un nodo composto o alternative elimino l'elemento dall'elenco
    		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
    		if (!parent.isRoot())	{
    			Componente parentComp = (Componente) parent.getUserObject();
    			if (parentComp.getType()==ComponenteComposto.COMPOSTOTYPE)	{
    				ComponenteComposto parentComposto = (ComponenteComposto)parentComp;
    				int indiceComponenteS = parentComposto.cercaComponenteS(comp.getNome());
    				parentComposto.cancellaComponenteS(indiceComponenteS);
    			}	else if (parentComp.getType()==ComponenteAlternative.ALTERNATIVETYPE)	{
    				ComponenteAlternative parentComposto = (ComponenteAlternative)parentComp;
    				int indiceAlternativa = parentComposto.cercaAlternativa(comp.getNome());
    				parentComposto.cancellaAlternativa(indiceAlternativa);
    			}
    		}
    		toRemove.add(node); //aggiungo il nodo originale agli elementi da eliminare*/
    		Componente compCopy = copy(comp);
    		compCopies.add(compCopy);
    		// se il nodo corrente pu� avere dei figli devo copiare anche quelli
    		if (node.getAllowsChildren())	{
    			for (int i=0; i<node.getChildCount(); i++)	{
    				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
    				Componente childComp = (Componente) childNode.getUserObject();
    				Componente childCompCopy = copy(childComp);
    				toRemove.add(childNode);
    				compCopies.add(childCompCopy);
    			}
    		}
    		compsToCopy = compCopies.toArray(new Componente[compCopies.size()]);
    		nodesToRemove = toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
    		return new ComponenteSelection(compsToCopy);
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
        if (action == MOVE) {
            JTree tree = (JTree) source;
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            // Rimuovo gli elementi indicati per la rimozione
            for (int i = 0; i <nodesToRemove.length ; i++) {
            	model.removeNodeFromParent(nodesToRemove[i]);
            }
        }
        System.out.println("Terminata esportazione");
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#exportToClipboard(javax.swing.JComponent, java.awt.datatransfer.Clipboard, int)
     */
    @Override
    public void exportToClipboard(JComponent c, Clipboard clip, int action)	{
    	System.out.println("Inizio ad esportare nella clipboard...");
    	Transferable treeTransferable = createTransferable(c);
    	if (compsToCopy == null)	{
    		return;
    	}
    	if ((action == MOVE) && (nodesToRemove!=null))	{
            JTree tree = (JTree) c;
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            // Rimuovo gli elementi indicati per la rimozione
            for (int i = 0; i <nodesToRemove.length ; i++) {
            	model.removeNodeFromParent(nodesToRemove[i]);
            }
    	}
    	else if ((action == COPY)&& (nodesToRemove!=null))	{
    		for (int i=0; i<compsToCopy.length;i++)	{
    			String name = compsToCopy[i].getNome();
    			int beginIndex = name.lastIndexOf('(');
    			if (beginIndex == -1)	{
    				beginIndex = compsToCopy[i].getNome().length();
    			}
    			String newName = name.substring(0,beginIndex);
    			newName = newName+'('+cont+')';
    			compsToCopy[i].setNome(newName);
    			cont++;
    		}
    	}
    	clipboard.setContents(treeTransferable, this);
    	//nodesToRemove = null;
    	System.out.println("Terminata esportazione nella clipboard...");
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
    	System.out.println("Inizio ad importare...");
    	Componente[] comps = null;
    	DefaultMutableTreeNode parent = null;
    	int index = -1;
        if (!canImport(support)) {
            return false;
        }
        JTree tree = (JTree) support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        if (support.isDrop())	{
        	System.out.println("Operazione di drop");
            // Estraggo i dati
        	if (support.getDropAction()== COPY)	{
        		renameSavedComps();
        	}
            try {
                Transferable t = support.getTransferable();
                comps = (Componente[]) t.getTransferData(componenteFlavor);
            } catch (UnsupportedFlavorException ufe) {
                System.out.println("UnsupportedFlavor: " + ufe.getMessage());
            } catch (java.io.IOException ioe) {
                System.out.println("I/O error: " + ioe.getMessage());
            }
        	// Recupero la location per il drop
        	JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
        	int childIndex = dl.getChildIndex();
        	TreePath dest = dl.getPath();
        	parent = (DefaultMutableTreeNode) dest.getLastPathComponent();
        	//Configure for drop mode.
        	index = childIndex; // DropMode.INSERT
        	if (childIndex == -1) { // DropMode.ON
        		index = parent.getChildCount();
        	}
        }	else	{
        	System.out.println("Operazione di paste");
        	Transferable trans = clipboard.getContents(null);
            try {
                comps = (Componente[]) trans.getTransferData(componenteFlavor);
            } catch (UnsupportedFlavorException ufe) {
                System.out.println("UnsupportedFlavor: " + ufe.getMessage());
            } catch (java.io.IOException ioe) {
                System.out.println("I/O error: " + ioe.getMessage());
            }
            TreePath path = tree.getSelectionPath();
            DefaultMutableTreeNode node = null;
            if (path != null)	{
            	node = (DefaultMutableTreeNode) path.getLastPathComponent();
            }
            else {
            	node = (DefaultMutableTreeNode) (tree.getModel()).getRoot();
            }
            //DefaultMutableTreeNode parent = null;
            //Se il nodo da spostare � composto, l'unico parent concesso � la root e come posizione sar� il successivo al target corrente
            if ((comps[0].getType()==ComponenteComposto.COMPOSTOTYPE) || (comps[0].getType()==ComponenteAlternative.ALTERNATIVETYPE))	{
            	parent = (DefaultMutableTreeNode) tree.getModel().getRoot();
            	//index = node.getParent().getIndex(node)+1;
            	index = parent.getChildCount();
            }	else	{ //se non � composto allora � un nodo semplice
            	if (!node.getAllowsChildren())	{ //se il target non permette figli, il parent del target corrente va bene
            		parent = (DefaultMutableTreeNode) node.getParent();
            		index=parent.getChildCount();
            	}	else	{ //se il target corrente consente figli allora il target va bene e sar� messo in coda
            		parent=node;
            		index=node.getChildCount();
            	}
            }
            renameSavedComps();
        }
        //Inserisco i dati
        for (int i = 0; i < comps.length; i++) {
        	DefaultMutableTreeNode nodeToInsert = new DefaultMutableTreeNode(comps[i]);
        	if (comps[i].getType()==Testo.TEXTTYPE || comps[i].getType()==Immagine.IMAGETYPE || comps[i].getType()==Link.LINKTYPE)	{
        		nodeToInsert.setAllowsChildren(false);
        	}
        	if (!parent.isRoot() && (i==0))	{ //se entro ma i!=0 significa che � sto spostando un componente composto e non devo riaggiungere i suoi elementi semplici
        		Componente parentComp = (Componente) parent.getUserObject();
        		if (parentComp.getType()==ComponenteComposto.COMPOSTOTYPE)	{
        			((ComponenteComposto)parentComp).aggiungiComponenteS((ComponenteSemplice) comps[i]);
        		}	else if (parentComp.getType()==ComponenteAlternative.ALTERNATIVETYPE)	{
        			((ComponenteAlternative)parentComp).aggiungiAlternativa((ComponenteSemplice) comps[i]);
        		}
        	}
        	model.insertNodeInto(nodeToInsert, parent, index++);
        	if (i==0 && ((comps[i]).getType()==ComponenteComposto.COMPOSTOTYPE)||((comps[i]).getType()==ComponenteAlternative.ALTERNATIVETYPE))	{
        		parent = nodeToInsert;
        		index=0;
        	}
        }
        //rinomino i dati in memoria per il paste
        //renameSavedComps();
        System.out.println("Terminata importazione");
        return true;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getName();
    }
    
    private void renameSavedComps()	{
    	System.out.println("Inizio a rinominare...");
    	Componente[] newCompsToCopy = compsToCopy;
		for (int i=0; i<compsToCopy.length;i++)	{
			newCompsToCopy[i] = copy(compsToCopy[i]);
			String name = compsToCopy[i].getNome();
			int beginIndex = name.lastIndexOf('(');
			if (beginIndex == -1)	{
				beginIndex = compsToCopy[i].getNome().length();
			}
			String newName = name.substring(0,beginIndex);
			newName = newName+'('+cont+')';
			newCompsToCopy[i].setNome(newName);
			cont++;
		}
		compsToCopy = newCompsToCopy;
		System.out.println("Terminato di rinominare...");
    }

    
    class NodeSelection implements Transferable	{
    	DefaultMutableTreeNode[] nodes;
    	//Componente[] comps;
    	
    	NodeSelection(DefaultMutableTreeNode[] n)	{
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
				System.out.println("Node data flavor");
				return true;
			}
			System.out.println("Node data flavor fuori if");
			return false;
		}
    }
    
    class ComponenteSelection implements Transferable	{
    	Componente[] comps;
    	
    	ComponenteSelection(Componente[] n)	{
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

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		System.out.println("Ownership lost");
	}
	
	/**
	 * Check if the given element name is already taken
	 * @param name
	 * @return
	 */
	public boolean nameExists(JTree tree, String name)	{
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		for (int i=0; i<root.getChildCount(); i++)	{
			if (nameExists(name, (DefaultMutableTreeNode) root.getChildAt(i)))	{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if the given element name is already taken for the given node
	 * @param name
	 * @param node
	 * @return
	 */
	private boolean nameExists(String name, DefaultMutableTreeNode node)	{
		Componente comp = (Componente) node.getUserObject();
		if (comp.getNome().equalsIgnoreCase(name))	{
			return true;
		}
		if (node.getAllowsChildren())	{
			for (int i=0; i<node.getChildCount(); i++)	{
				if (nameExists(name, (DefaultMutableTreeNode) node.getChildAt(i)))	{
					return true;
				}
			}
		}
		return false;
	}

}