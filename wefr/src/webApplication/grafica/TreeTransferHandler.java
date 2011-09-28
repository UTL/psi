package webApplication.grafica;

import java.util.List;
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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.ComponenteMolteplice;
import webApplication.business.ComponenteSemplice;
import webApplication.grafica.TreePanel.CopyAction;
import webApplication.grafica.TreePanel.MoveAction;

/**
 * Gestisce le azioni di copia/taglia/incolla e drag&drop sui nodi dell'albero 
 * @author Andrea
 *
 */
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
    private CopyAction copyAction;
    private MoveAction moveAction;
    private TreePanel panel;
    private boolean isCopy;
    
    /**
     * Il costruttore di base
     */
    public TreeTransferHandler(TreePanel p) {
        try {
        	String mimeType1 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + webApplication.business.Componente[].class.getName()+"\"";
        	componenteFlavor = new DataFlavor(mimeType1);
        	flavors[0] = componenteFlavor;
        	String mimeType8 = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + javax.swing.tree.DefaultMutableTreeNode[].class.getName() + "\"";
        	nodesFlavor = new DataFlavor(mimeType8);
        	flavors[1] = nodesFlavor;
        	clipboard = new Clipboard("Tree Clipboard");
        	panel = p;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }
    
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#canImport(javax.swing.TransferHandler.TransferSupport)
     */
    @Override
    public boolean canImport(TransferSupport support)	{
    	//controlli per verificare se il drop è valid
    	if (support.isDrop())	{//se è una drop verifico direttamente
    		support.setShowDropLocation(true);//indica visivamente dove sta avvenendo l'operazione di drop
    		//1. se il data flavor non è supportato allora ritorna false
            if ((!support.isDataFlavorSupported(nodesFlavor)) && (!support.isDataFlavorSupported(componenteFlavor)))	{
                return false;
            }
            //2a. se la DropLocation è la stessa della DragLocation ritorna false
            //2b. se la DropLocation e la DragLocation sono entrambi elementi composti
            //2c. se la DragLocation è la root
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
            //3. se la DropLocation è un componente che non permette figli
            if (!node.getAllowsChildren())	{
            	return false;
            }
    	}
    	else	{//se è un copia/taglia dalla clipboard devo estrarre i dati prima
    		Transferable trans = clipboard.getContents(null);
            if ((trans==null) || (!trans.isDataFlavorSupported(nodesFlavor)) && (!trans.isDataFlavorSupported(componenteFlavor)))	{
                return false;
            }
    	}
        //4. altri???
    	return true;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
     */
    protected Transferable createTransferable(JComponent c)	{
    	// NOTA: i dati li salvo in un campo locale per facilità di accesso, ma potrei estrarli dal transferable ogni volta che servono
    	JTree tree = (JTree) c;
    	TreePath path = tree.getSelectionPath();
    	if (path!=null)	{
    		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
    		
    		//Salvo le informazioni sulla posizione attuale del nodo per l'undo
        	copyAction = panel.new CopyAction();
        	moveAction = panel.new MoveAction();
    		int oldIndex = node.getParent().getIndex(node);
    		copyAction.putValue("OldIndex", oldIndex);
    		moveAction.putValue("OldIndex", oldIndex);
    		if (((DefaultMutableTreeNode) node.getParent()).isRoot())	{
    			copyAction.putValue("OldParentIndex", -1);
        		moveAction.putValue("OldParentIndex", -1);
    		}	else	{
    			int oldParentIndex = ((DefaultMutableTreeNode) tree.getModel().getRoot()).getIndex(node.getParent());
    			copyAction.putValue("OldParentIndex", oldParentIndex);
        		moveAction.putValue("OldParentIndex", oldParentIndex);
    		}
    		
    		Componente comp = (Componente) node.getUserObject();
    		List<Componente> compCopies = new ArrayList<Componente>();
    		List<DefaultMutableTreeNode> toRemove = new ArrayList<DefaultMutableTreeNode>(); //lista dei nodi da rimuovere->NOTA: memorizzo i nodi tanto mi serve sapere se erano Componente!
    		toRemove.add(node); //aggiungo il nodo originale agli elementi da eliminare*/
    		Componente compCopy = copy(comp);
    		compCopies.add(compCopy);
    		// se il nodo corrente può avere dei figli devo copiare anche quelli
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
    
    /**
     * Crea una copia del nodo
     * @param comp	Il nodo da copiare
     * @return		Il nodo copiato
     */
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
        	isCopy=false;
            JTree tree = (JTree) source;
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
            // Rimuovo gli elementi indicati per la rimozione
            for (int i = 0; i <nodesToRemove.length ; i++)	{
            	DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodesToRemove[i].getParent();
                //Se il parent era un nodo composto o alternative elimino l'elemento dall'elenco
            	if (!parent.isRoot())	{
        			ComponenteMolteplice parentComp = (ComponenteMolteplice) parent.getUserObject();
        			int indiceComponente = parentComp.cercaOpzione(((Componente)nodesToRemove[i].getUserObject()).getNome());
        			parentComp.cancellaOpzione(indiceComponente);
        			moveAction.putValue("OldParentIndex", root.getIndex(parent));
            	}	else	{
            		moveAction.putValue("OldParentIndex", -1);
            	}
            	moveAction.putValue("OldIndex", parent.getIndex(nodesToRemove[i]));
            	model.removeNodeFromParent(nodesToRemove[i]);
            }
        }	else	{
        	isCopy=true;
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
    		isCopy=false;
            JTree tree = (JTree) c;
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            // Rimuovo gli elementi indicati per la rimozione
            for (int i = 0; i <nodesToRemove.length ; i++) {
            	model.removeNodeFromParent(nodesToRemove[i]);
            }
    	}
    	else if ((action == COPY))	{//&& (nodesToRemove!=null))	{
    		isCopy=true;
    	}
    	clipboard.setContents(treeTransferable, this);
    	System.out.println("Terminata esportazione nella clipboard...");
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
     */
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#importData(javax.swing.TransferHandler.TransferSupport)
     */
    public boolean importData(TransferHandler.TransferSupport support) {
    	System.out.println("Inizio ad importare...");
    	DefaultMutableTreeNode parent = null;
    	int index = -1;
        if (!canImport(support)) {
            return false;
        }
        JTree tree = (JTree) support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        if (support.isDrop())	{
        	System.out.println("Operazione di drop");
        	if (support.getDropAction()==MOVE)	{
        		System.out.println("Sposta");
        		isCopy=false;
        	}	else	{
        		System.out.println("Copia");
        		isCopy=true;
        	}
            // Estraggo i dati -> i dati sono già salvati in compsToCopy non devo estrarli dal transferable
            /*try {
            	comps = compsToCopy;
                Transferable t = support.getTransferable();
                comps = (Componente[]) t.getTransferData(componenteFlavor);
            } catch (UnsupportedFlavorException ufe) {
                System.out.println("UnsupportedFlavor: " + ufe.getMessage());
            } catch (java.io.IOException ioe) {
                System.out.println("I/O error: " + ioe.getMessage());
            }*/
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
            	compsToCopy = (Componente[]) trans.getTransferData(componenteFlavor);
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
            //Se il nodo da spostare è composto, l'unico parent concesso è la root e come posizione sarà il successivo al target corrente
            if (!compsToCopy[0].isSimple())	{
            	parent = (DefaultMutableTreeNode) tree.getModel().getRoot();
            	index = parent.getChildCount();
            }	else	{ //se non è composto allora è un nodo semplice
            	if (!node.getAllowsChildren())	{ //se il target non permette figli, il parent del target corrente va bene
            		parent = (DefaultMutableTreeNode) node.getParent();
            		index=parent.getChildCount();
            	}	else	{ //se il target corrente consente figli allora il target va bene e sarà messo in coda
            		parent=node;
            		index=node.getChildCount();
            	}
            }
        }
        //Inserisco i dati
        for (int i = 0; i < compsToCopy.length; i++) {
        	
        	//Se il nome esiste già rinomino il componente da incollare
        	//NOTA: lo faccio prima di creare il nodo altrimenti le modifiche non sono rilevate
        	if (nameExists(tree, compsToCopy[i].getNome()) && isCopy)	{
        		compsToCopy[i]=renameComponente(compsToCopy[i]);
        	}
        	
        	DefaultMutableTreeNode nodeToInsert = new DefaultMutableTreeNode(compsToCopy[i]);
        	if (compsToCopy[i].isSimple())	{
        		nodeToInsert.setAllowsChildren(false);
        	}
        	if (!parent.isRoot() && (i==0))	{ //se entro ma i!=0 significa che è sto spostando un componente composto e non devo riaggiungere i suoi elementi semplici
        		Componente parentComp = (Componente) parent.getUserObject();
        		((ComponenteMolteplice)parentComp).aggiungiOpzione((ComponenteSemplice) compsToCopy[i]);
        		copyAction.putValue("ParentIndex",parent.getParent().getIndex(parent));
        		moveAction.putValue("NewParentIndex",parent.getParent().getIndex(parent));
        	}	else	{
        		copyAction.putValue("ParentIndex", -1);
        		moveAction.putValue("NewParentIndex", -1);
        	}
        	model.insertNodeInto(nodeToInsert, parent, index++);
        	System.out.println("è una copia: "+isCopy);
        	if (isCopy)	{
        		copyAction.putValue("Index", index-1);
        		copyAction.actionPerformed(new ActionEvent(this, -1, "CopyAction"));
        	}	else	{
        		moveAction.putValue("NewIndex", index-1);
        		moveAction.actionPerformed(new ActionEvent(this, -1, "MoveAction"));
        	}
        	if (i==0 && ((compsToCopy[i]).getType()==ComponenteComposto.COMPOSTOTYPE)||((compsToCopy[i]).getType()==ComponenteAlternative.ALTERNATIVETYPE))	{
        		parent = nodeToInsert;
        		index=0;
        	}
        }
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
    
    /**
     * Modifica il campo Nome dell'oggetto Componente
     * @param comp	Il componente il cui nome deve essere modificato
     * @return		Il nuovo componente con il nome modificato
     */
    private Componente renameComponente(Componente comp)	{
    	System.out.println("Inizio a rinominare...");
    	Componente newComp = copy(comp);
    	String name = comp.getNome();
		int beginIndex = name.lastIndexOf('(');
		if (beginIndex == -1)	{
			beginIndex = comp.getNome().length();
		}
		String newName = name.substring(0,beginIndex);
		newName = newName+'('+cont+')';
    	newComp.setNome(newName);
    	cont++;
		System.out.println("Terminato di rinominare...");
		return newComp;
    }

    
    /**
     * Implementa il dataflavor dell'oggetto Nodo usato per il drag&drop
     * @author Andrea
     *
     */
    class NodeSelection implements Transferable	{
    	DefaultMutableTreeNode[] nodes;
    	
    	/**
    	 * Il costruttore di base
    	 * @param n	i nodi da spostare
    	 */
    	NodeSelection(DefaultMutableTreeNode[] n)	{
    		nodes=n;
    	}

		/* (non-Javadoc)
		 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
		 */
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException	{
			if (!isDataFlavorSupported(flavor))	{
				throw new UnsupportedFlavorException(flavor);
			}
			return nodes;
		}

		/* (non-Javadoc)
		 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
		 */
		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		/* (non-Javadoc)
		 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
		 */
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (nodesFlavor.equals(flavor))	{
				System.out.println("Node data flavor");
				return true;
			}
			System.out.println("Node data flavor fuori if");
			return false;
		}
    }
    
    /**
     * Implementa il dataflavor dell'oggetto componente usato per il drag&drop
     * @author Andrea
     *
     */
    class ComponenteSelection implements Transferable	{
    	Componente[] comps;
    	
    	/**
    	 * Costruttore di base
    	 * @param n	I nodi da spostare
    	 */
    	ComponenteSelection(Componente[] n)	{
    		comps=n;
    	}

		/* (non-Javadoc)
		 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
		 */
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException	{
			if (!isDataFlavorSupported(flavor))	{
				throw new UnsupportedFlavorException(flavor);
			}
			return comps;
		}

		/* (non-Javadoc)
		 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
		 */
		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		/* (non-Javadoc)
		 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
		 */
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (componenteFlavor.equals(flavor))	{
				return true;
			}
			return false;
		}
    	
    }

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
	 */
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		//non mi serve
		System.out.println("Ownership lost");
	}
	
	/**
	 * Verifica se un nome è già stato usato nell'albero
	 * @param name	Il nome da verificare
	 * @return	True o False a seconda se il nome è già stato usato o meno
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
	 * Verifica se un nome è già stato usato in un nodo specifico 
	 * @param name	Il nome da verificare
	 * @param node	Il nodo in cui verificare
	 * @return		True o False a seconda se il nome è già stato usato o meno
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
