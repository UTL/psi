package webApplication.grafica;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import webApplication.business.*;

/**
 * Il pannello contenente l'albero
 * @author Andrea
 *
 */
public class TreePanel extends JPanel implements /*ActionListener,*/ TreeSelectionListener	{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8834919285623008305L;
	
	private static final String ROOT = "Home";
	private static final String DELETEMESSAGE = "You are going to delete ";
	private static final String CONFIRMMESSAGE ="\n Do you want to continue?";
	private static final String NOTEMPTYMESSAGE = " is not empty.\n Deleting this element will delete all the elements inside.\n Do you want to continue?";
	private static final String CLEARALL = "This will delete the current work.\n Do you wish to continue?";
	private static final String ROOTDELETE = "You cannot delete this element because it is the tree root";
	private static final String EMPTYSELECTION = "No element selected";
	private static final String ADDCHILDRENNOTALLOWED = " can't have children";
	private static final int LIMITUNDO=10;
	protected static final String ADD_COMMAND = "add";
    protected static final String REMOVE_COMMAND = "remove";
    protected static final String CLEAR_COMMAND = "clear";
	
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel model;
	private JTree tree;
	private TreeTransferHandler th;
	
	
	private UndoableEditSupport undoSupport;
	private UndoManager undoManager;
	
	//private CustomTreeEditor te;
	
	/**
	 * Il costruttore del pannello contenente l'albero.
	 */
	public TreePanel() {
		init();
	}

	/**
	 * Inizializza il pannello contenente l'albero
	 */
	private void init() {
		th = new TreeTransferHandler(this);
		rootNode = new DefaultMutableTreeNode(ROOT);
		model = new DefaultTreeModel(rootNode);
		tree =  new JTree(model);
		tree.setShowsRootHandles(true); // rende visibile il nodo root
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); //solo un nodo alla volta ï¿½ selezionabile
		tree.setCellRenderer(new CustomCellRenderer());
		tree.setDragEnabled(true);
		tree.setDropMode(DropMode.ON_OR_INSERT);
		tree.setTransferHandler(th);
		tree.addTreeSelectionListener(this); //il listener per l'evento di selezione di un elemento -> devo aggiungere anche il frame per abilitare/disabilitare i pulsanti?
		//editor delle celle
		tree.setEditable(false); // fa in modo che l'albero non sia editabile
		tree.setAutoscrolls(true);
		setMappings(tree);
		add(tree);
		
		//pannello contenente il tree
		JScrollPane scrollPane = new JScrollPane(tree,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane);
		
		//inizializzo il gestore delle azioni di undo/redo
		undoManager = new UndoManager();
		undoManager.setLimit(LIMITUNDO);
		undoSupport = new UndoableEditSupport();
		undoSupport.addUndoableEditListener(new UndoAdapter());
	}

	/**
	 * Cancella tutti gli elementi inseriti nell'albero
	 */
	protected void clear()	{
		if (rootNode.getChildCount()!=0)	{
			//chiedo conferma prima di resettare tutto
			System.out.println("Ancestor: "+this.getTopLevelAncestor().getClass());
			int choice = JOptionPane.showConfirmDialog(this.getTopLevelAncestor(), CLEARALL,"Warning!",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			if (choice == 1)	{
				return;
			}
		}
		rootNode.removeAllChildren();
		model.nodeStructureChanged(rootNode);
		model.reload();
	}
	
	/**
	 * Rimuove un nodo dall'albero. Per geneare la corretta azione di undo/redo passare attraverso il listener RemoveAction
	 * @param node Il nodo da rimuovere
	 */
	private void removeNode(DefaultMutableTreeNode node) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
		if (!parent.isRoot())	{
			ComponenteMolteplice compParent = (ComponenteMolteplice) parent.getUserObject();
			compParent.cancellaOpzione(parent.getIndex(node));
		}
		model.removeNodeFromParent(node);
	}


	/**
	 * Aggiunge un nodo all'albero. Per generare la corretta azione di undo/redo passare attraverso il listener AddAction
	 * @param parent	Il genitore del nodo che si vuole inserire
	 * @param node		Il nodo da inserire
	 */
	private void addNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode node) {
		//DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(node);
		if (parent == null) {
			parent = rootNode;
		}	else	{
			Componente compElem = (Componente)parent.getUserObject();
			if (!compElem.isSimple())	{
				((ComponenteMolteplice)compElem).aggiungiOpzione((ComponenteSemplice)node.getUserObject());
			}
		}
		//Verifico che il nodo genitore possa avere nodi figli anche se con il drag&drop ï¿½ giï¿½ esclusa come destinazione possibile (vedi canImport)
		if (parent.getAllowsChildren()==false)	{
			JOptionPane.showMessageDialog(this.getTopLevelAncestor(),parent.toString()+ADDCHILDRENNOTALLOWED,"Error!",JOptionPane.ERROR_MESSAGE);
			return;
		}
		model.insertNodeInto(node, parent, parent.getChildCount());
		if (!((Componente)node.getUserObject()).isSimple())	{
			parent = node;
			System.out.println("Parent name: "+((Componente)parent.getUserObject()).getNome());
			ComponenteMolteplice comps = (ComponenteMolteplice)node.getUserObject();
			for (int i =0; i<comps.getOpzioni().size(); i++)	{
				model.insertNodeInto(new DefaultMutableTreeNode((ComponenteSemplice)comps.getOpzione(i)), parent, i);
			}
		} else	{
		//if (((Componente)node.getUserObject()).isSimple())	{
			node.setAllowsChildren(false);
		}
	}
	
	/**
	 * Aggiunge le azioni di copia/taglia/incolla all'albero
	 * @param tree	L'albero
	 */
	private void setMappings(JTree tree)	{
		ActionMap map = tree.getActionMap();
	    map.put(TransferHandler.getCutAction().getValue(Action.NAME),TransferHandler.getCutAction());
	    map.put(TransferHandler.getCopyAction().getValue(Action.NAME),TransferHandler.getCopyAction());
	    map.put(TransferHandler.getPasteAction().getValue(Action.NAME),TransferHandler.getPasteAction());
		
		//disabilito le azioni di default di ctrl+x ctrl+c ctrl+v per avere un controllo sugli eventi 
		tree.getInputMap().put(KeyStroke.getKeyStroke("control C"), "none");
		tree.getInputMap().put(KeyStroke.getKeyStroke("control X"), "none");
		tree.getInputMap().put(KeyStroke.getKeyStroke("control V"), "none");
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (node == null || node.isRoot())	{
			tree.clearSelection();
			//non ï¿½ stato selezionato nulla o ï¿½ stata selezionata la radice;
			return;
		}	else	{
			Componente comp = (Componente) node.getUserObject();
			System.out.println("Componente: "+comp.getNome());
			if (!comp.isSimple())	{
				Vector<ComponenteSemplice> elementi= ((ComponenteMolteplice)comp).getOpzioni();
				System.out.println("Elementi del componente:");
				for (int i=0; i<elementi.size(); i++)	{
					System.out.println(elementi.elementAt(i).getNome());
				}
			}
		}
	}
	
	/**
	 * Metodo per ottenere l'oggetto JTree interno al pannello
	 * @return L'albero
	 */
	public JTree getTree()	{
		return tree;
	}
	
	/**
	 * Verifica se l'albero è vuoto
	 * @return True o False a seconda se l'albero è vuoto o no
	 */
	public boolean isEmpty()	{
		if ((((DefaultMutableTreeNode) model.getRoot()).getChildCount())!=0)	{
			return false;
		}
		return true;
	}
	
	/** 
	 * Fornisce la lista dei Componenti di primo livello dell'albero
	 * @return Il vettore contenente i nodi
	 */
	public Vector<Componente> getComponenti()	{
		Vector<Componente> comps = new Vector<Componente>();
		for (int i=0; i< rootNode.getChildCount(); i++)	{
			comps.add((Componente)((DefaultMutableTreeNode) rootNode.getChildAt(i)).getUserObject());
		}
		return comps;
	}
	
	/**
	 * Permette il caricamento di una lista di Componenti nell'albero
	 * @param comps	Il vettore dei componenti da inserire
	 */
	public void setComponenti(Vector<Componente> comps)	{
		for (int i=0; i<comps.size(); i++)	{
			addNode(null,new DefaultMutableTreeNode((Componente)comps.get(i)));
		}
	}
	
	/**
	 * Ascolta gli eventi di aggiunta di un nodo all'albero
	 * @author Andrea
	 *
	 */
	public class AddAction extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -4513876659563150305L;
		private static final String COMPONENTE = "Componente";
		private static final String PARENTINDEX = "ParentIndex";
		
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent evt) {
			if (getValue(COMPONENTE)!=null)	{
				System.out.println("Sto aggiungendo");
				DefaultMutableTreeNode parent = null;
				DefaultMutableTreeNode compNode = new DefaultMutableTreeNode((Componente)getValue(COMPONENTE));
				if ((Integer)this.getValue(PARENTINDEX)==-1)	{
					addNode(null,compNode);
				}	else	{
					System.out.println("Parent index: "+(Integer) getValue(PARENTINDEX));
					parent = (DefaultMutableTreeNode) rootNode.getChildAt((Integer) getValue(PARENTINDEX));
					addNode(parent,compNode);
					
				}
				if (parent==null)	{
					parent = rootNode;
				}
				//creo l'undoable dell'add
				UndoableEdit edit = new UndoableAddNode(tree,(Componente) compNode.getUserObject(),(Integer)getValue(PARENTINDEX),parent.getIndex(compNode));
				undoSupport.postEdit(edit);
				
				//setto il focus sul nuovo oggetto
				tree.setSelectionPath(new TreePath(compNode.getPath()));
			}
		}
	}
	
	/**
	 * Ascolta gli eventi di rimozione di un elemento dall'albero
	 * @author Andrea
	 *
	 */
	public class RemoveAction extends AbstractAction	{

		public static final String INDEXES ="Indexes";
		/**
		 * 
		 */
		private static final long serialVersionUID = -3159160087603892781L;

		public RemoveAction()	{
			super();
			this.putValue(INDEXES, null); //per default il nodo da rimuovere è la selezione corrente sull'albero
		}
		
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
	        TreePath currentSelection = tree.getSelectionPath();
	        DefaultMutableTreeNode currentNode;
			if (currentSelection != null) {
				//NOTA: disabilitando il pulsante se nulla è selezionato sarà sempre vera
				if (currentSelection.getParentPath() != null) {
					if (getValue(INDEXES)==null)	{
						//indice = null indica che il nodo da rimuovere è quello selezionato sull'albero
						currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
						delete(currentSelection, currentNode);
					}	else	{
						DefaultMutableTreeNode parent = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
						int[] indexes = (int[]) getValue(INDEXES);
						for (int i=0; i<indexes.length; i++)	{
							currentNode = (DefaultMutableTreeNode) parent.getChildAt(indexes[i]);
							delete(currentSelection, currentNode);
						}
					}
					
				}
	        	else	{
	        		//una verifica che la root non può essere rimossa -> non più usato perchè la root non è selezionabile
	        		JOptionPane.showMessageDialog(getTopLevelAncestor(),ROOTDELETE,"Error!",JOptionPane.ERROR_MESSAGE);
	        	}
			}
			else	{
				//non c'è nulla selezionato quindi non può essere rimosso nulla -> disabilitato il pulsante quindi non può accadere
				JOptionPane.showMessageDialog(getTopLevelAncestor(),EMPTYSELECTION,"Error!",JOptionPane.ERROR_MESSAGE);
			}
		}

		/**
		 * Rimuove un nodo dall'albero e gestisce la creazione dell'oggetto per l'undo 
		 * @param currentSelection	Il nodo selezionato sull'albero
		 * @param nodeToRemove		Il nodo che si intende rimuovere
		 */
		private void delete(TreePath currentSelection, DefaultMutableTreeNode nodeToRemove) {
			/*NOTA: il nodo selezionato sull'albero NON è sempre il nodo da rimuovere
			 		se si considera ad esempio il pannello delle proprietà di un elemento molteplice, ad esempio,
			 		il nodo selezionato nell'albero è l'elemento molteplice ma è possibile rimuovere uno dei suoi figli selezionandolo
			 		nella lista delle proprietà. In questo caso, la currentSelection è l'elemento molteplice, ma il currentNode sarà 
			 		il nodo figlio, e sarà quest'ultimo da rimuovere*/
			String name = nodeToRemove.toString();//((Componente) currentNode.getUserObject()).toString();
			String message = DELETEMESSAGE + name + CONFIRMMESSAGE;
			// Chiede conferma per la cancellazione dell'elemento selezionato -> eliminato per mancata sincronizzazione con la JList
			//int choice;
			//int choice = JOptionPane.showConfirmDialog(getTopLevelAncestor(), message, "Warning!",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			//if (choice == 0) {
				if (currentSelection != null) {
					if (nodeToRemove.getChildCount() != 0) {
						message = name + NOTEMPTYMESSAGE;
						// Chiede conferma per eliminare un componente contenente altri elementi
						int choice = JOptionPane.showConfirmDialog(getTopLevelAncestor(), message,"Warning!", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
						if (choice == 1)	{
							return;
						}
					}
					/*if (choice == 1) {
						return;
					}*/
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodeToRemove.getParent();
					int parentIndex;
					if (parent.isRoot())	{
						parentIndex = -1;
					}
					else	{
						DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
						parentIndex = root.getIndex(parent);
					}
					//Creo l'oggetto per l'undo action
					UndoableEdit edit = new UndoableRemoveNode(tree,(Componente)nodeToRemove.getUserObject(),parentIndex,parent.getIndex(nodeToRemove));
					undoSupport.postEdit(edit);
					//rimuovo effettivamente il nodo
					removeNode(nodeToRemove);
				}
			//}
		}
	}
	
	/**
	 * Ascolta la copia di un nodo dell'albero
	 * @author Andrea
	 *
	 */
	public class CopyAction extends AbstractAction	{
		//NOTA: una CopyAction è fondamentalmente una AddAction ma l'elemento inserito è una copia di quello esistente
		//		infatti le tengo separate per "comprensione" ma in pratica uso sempre la AddAction
		/**
		 * 
		 */
		private static final long serialVersionUID = -4612100142468392225L;
		//private static final String COMPONENTE = "Componente";
		private static final String PARENTINDEX = "ParentIndex";
		private static final String INDEX = "Index";


		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			DefaultMutableTreeNode parent;
			if (((Integer)this.getValue(PARENTINDEX))==-1)	{
				parent = rootNode;
			}
			else	{
				parent = (DefaultMutableTreeNode) rootNode.getChildAt((Integer)this.getValue(PARENTINDEX));
			}
			DefaultMutableTreeNode compNode = (DefaultMutableTreeNode) parent.getChildAt((Integer)this.getValue(INDEX));
			UndoableEdit edit = new UndoableAddNode(tree,(Componente) compNode.getUserObject(),(Integer)getValue(PARENTINDEX),parent.getIndex(compNode));
			undoSupport.postEdit(edit);
		}
	}
	
	/**
	 * Ascolta lo spostamento di un nodo nell'albero
	 * @author Andrea
	 *
	 */
	public class MoveAction extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3099984963394542917L;
		private static final String OLDPARENTINDEX = "OldParentIndex";
		private static final String NEWPARENTINDEX = "NewParentIndex";
		private static final String NEWINDEX = "NewIndex";
		private static final String OLDINDEX = "OldIndex";
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			UndoableEdit edit = new UndoableMoveNode(tree,(Integer)getValue(OLDPARENTINDEX),(Integer)getValue(NEWPARENTINDEX),(Integer)getValue(OLDINDEX),(Integer)getValue(NEWINDEX));
			undoSupport.postEdit(edit);
		}
		
	}
	
	/**
	 * Ascolta la modifica della priorità degli elementi all'interno di un Alternative
	 * @author Andrea
	 *
	 */
	public class ChangePriorityAction extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5836454808095990928L;
		private static final String OLDINDEX = "OldIndex";
		private static final String PARENT = "Parent";
		private static final String GAP = "gap";
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0)	{
			// TODO Verificare che funzioni effettivamente
			//Il senso: prendo la selezione, prendo il parent e swappo spostandolo
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) getValue(PARENT);
			int oldIndex = (Integer)getValue(OLDINDEX);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt(oldIndex);
			int gap = (Integer)getValue(GAP);
			//se sono in testa alla lista non posso diminuire viceversa se sono in fondo non posso scendere->giï¿½ gestito anche nell'interfaccia disabilitando i bottoni
			if ((oldIndex!=0 && gap!=-1) || (oldIndex!=(parent.getChildCount()-1) && gap!=+1))	{
				model.removeNodeFromParent(node);
				model.insertNodeInto(node, parent, oldIndex+gap);
				//((ComponenteMolteplice)parent.getUserObject()).spostaOpzione((ComponenteSemplice)node.getUserObject(),+gap);
				UndoableChangePriority edit = new UndoableChangePriority(tree, parent, oldIndex, gap);
				undoSupport.postEdit(edit);
			}
		}
	}
	
	/**
	 * Ascolta la modifica di un campo di un Elemento
	 * @author Andrea
	 *
	 */
	public class ChangeField extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 8756265766454125789L;
		private static final String COMPONENTEINDEX = "Componente";
		private static final String PARENTINDEX = "ParentIndex";
		private static final String FIELD = "Field";
		private static final String OLDVALUE = "OldValue";
		private static final String NEWVALUE = "NewValue";
		
		public static final String NOME = "Nome";
		public static final String CATEGORY = "Category";
		public static final String EMPHASIS = "Category";
		public static final String VISIBILITY = "Category";

		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			//TODO Verificare che funzioni correttamente
			DefaultMutableTreeNode parent;
			if ((Integer)getValue(PARENTINDEX)==-1)	{
				parent = rootNode;
			}
			else	{
				parent = (DefaultMutableTreeNode) rootNode.getChildAt((Integer)getValue(PARENTINDEX));
			}
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt((Integer)getValue(COMPONENTEINDEX));
			Componente comp = (Componente)node.getUserObject();
			String field = (String)getValue(FIELD);
			if (field==NOME)	{
				comp.setNome((String)getValue(NEWVALUE));
			}
			else if (field==CATEGORY)	{
				comp.setCategoria((String)getValue(NEWVALUE));
			}
			else if (field==EMPHASIS)	{
				comp.setEnfasi((Integer)getValue(NEWVALUE));
			}
			else if (field==VISIBILITY)	{
				comp.setVisibilita((Integer)getValue(NEWVALUE));
			}
			UndoableChangeField edit = new UndoableChangeField(tree, (Integer)getValue(PARENTINDEX), (Integer)getValue(COMPONENTEINDEX), (String)getValue(FIELD), (Object)getValue(OLDVALUE), (Object)getValue(NEWVALUE));
			undoSupport.postEdit(edit);
		}
		
	}
	
	/**
	 * Ascolta una richiesta di Undo
	 * @author Andrea
	 *
	 */
	public class UndoAction extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3952201139907413522L;

		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			undoManager.undo();
		}
		
	}
	
	/**
	 * Ascolta una richiesta di Redo
	 * @author Andrea
	 *
	 */
	public class RedoAction extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2568562509253946673L;

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			undoManager.redo();
		}
		
	}
	
	/**
	 * Listener per gli eventi di Undo/Redo
	 * @author Andrea
	 *
	 */
	private class UndoAdapter implements UndoableEditListener {
		
		/* (non-Javadoc)
		 * @see javax.swing.event.UndoableEditListener#undoableEditHappened(javax.swing.event.UndoableEditEvent)
		 */
		public void undoableEditHappened (UndoableEditEvent evt) {
			UndoableEdit edit = evt.getEdit();
		    undoManager.addEdit(edit);
		}
	}
		
}
