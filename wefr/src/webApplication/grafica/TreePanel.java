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
	
	public TreePanel() {
		init();
	}

	/**
	 * Initialize the Tree component
	 */
	private void init() {
		th = new TreeTransferHandler(this);
		rootNode = new DefaultMutableTreeNode(ROOT);
		model = new DefaultTreeModel(rootNode);
		tree =  new JTree(model);
		tree.setShowsRootHandles(true); // rende visibile il nodo root
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); //solo un nodo alla volta � selezionabile
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
		//model.addTreeModelListener(this);
		undoManager = new UndoManager();
		undoManager.setLimit(LIMITUNDO);
		undoSupport = new UndoableEditSupport();
		undoSupport.addUndoableEditListener(new UndoAdapter());
	}

	/**
	 * Reset the Tree
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
	 * Remove a node from the tree
	 * @param node The node to remove
	 */
	protected void removeNode(DefaultMutableTreeNode node) {
        /*TreePath currentSelection = tree.getSelectionPath();
		if (currentSelection != null) {
			if (currentSelection.getParentPath() != null) {
				DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
				String name = currentNode.toString();//((Componente) currentNode.getUserObject()).toString();
				String message = DELETEMESSAGE + name + CONFIRMMESSAGE;
				// Chiede conferma per la cancellazione dell'elemento selezionato
				int choice = JOptionPane.showConfirmDialog(
						this.getTopLevelAncestor(), message, "Warning!",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (choice == 0) {
					if (currentSelection != null) {
						if (currentNode.getChildCount() != 0) {
							message = name + NOTEMPTYMESSAGE;
							// Chiede conferma per eliminare un componente contenente altri elementi
							choice = JOptionPane.showConfirmDialog(this.getTopLevelAncestor(), message,"Warning!", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
						}
						if (choice == 1) {
							return;
						}
						DefaultMutableTreeNode parent = (DefaultMutableTreeNode) currentNode.getParent();
						int parentIndex;
						if (parent.isRoot())	{
							parentIndex = -1;
						}
						else	{
							DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
							parentIndex = root.getIndex(parent);
						}
						
						UndoableEdit edit = new UndoableRemoveNode(tree,(Componente)currentNode.getUserObject(),parentIndex,parent.getIndex(currentNode));
						undoSupport.postEdit(edit);*/
						
						model.removeNodeFromParent(node);/*
					}
				}
			}
        	else	{
        		//� stata selezionata la radice perci� avviso l'utente che non pu� essere cancellata->risolto: la root non pu� essere selezionata
        		JOptionPane.showMessageDialog(this.getTopLevelAncestor(),ROOTDELETE,"Error!",JOptionPane.ERROR_MESSAGE);
        	}
		}
		else	{
			//Segnala che non c'� nulla selezionato quindi non pu� rimuovere nulla->da rivedere in disabilita Remove se non c'� nulla selezionato
			JOptionPane.showMessageDialog(this.getTopLevelAncestor(),EMPTYSELECTION,"Error!",JOptionPane.ERROR_MESSAGE);
		}*/
	}


	/**
	 * Add a new node to the Tree
	 * @param parent	The parent of the node
	 * @param node		The node
	 */
	protected void addNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode node) {
		//DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(node);
		if (parent == null) {
			parent = rootNode;
		}	else	{
			Componente compElem = (Componente)parent.getUserObject();
			if (!compElem.isSimple())	{
				((ComponenteMolteplice)compElem).aggiungiOpzione((ComponenteSemplice)node.getUserObject());
			}
		}
		//Verifico che il nodo genitore possa avere nodi figli anche se con il drag&drop � gi� esclusa come destinazione possibile (vedi canImport)
		if (parent.getAllowsChildren()==false)	{
			JOptionPane.showMessageDialog(this.getTopLevelAncestor(),parent.toString()+ADDCHILDRENNOTALLOWED,"Error!",JOptionPane.ERROR_MESSAGE);
			return;
		}
		model.insertNodeInto(node, parent, parent.getChildCount());
		if (((Componente)node.getUserObject()).isSimple())	{
			node.setAllowsChildren(false);
		}
		
		//UndoableEdit edit = new UndoableAddNode(tree,(Componente) node.getUserObject(),parentIndex,parent.getIndex(node));
		//undoSupport.postEdit(edit);
		
	}
	
	/**
	 * Aggiunge le azioni di copia/taglia/incolla
	 * @param tree	L'albero
	 */
	private void setMappings(JTree tree)	{
		ActionMap map = tree.getActionMap();
	    map.put(TransferHandler.getCutAction().getValue(Action.NAME),TransferHandler.getCutAction());
	    map.put(TransferHandler.getCopyAction().getValue(Action.NAME),TransferHandler.getCopyAction());
	    map.put(TransferHandler.getPasteAction().getValue(Action.NAME),TransferHandler.getPasteAction());
		
		//disabilito le azioni di default di ctrl+x ctrl+c ctrl+v per avere un controllo migliore 
		tree.getInputMap().put(KeyStroke.getKeyStroke("control C"), "none");
		tree.getInputMap().put(KeyStroke.getKeyStroke("control X"), "none");
		tree.getInputMap().put(KeyStroke.getKeyStroke("control V"), "none");
	}

	/**
	 * TODO questi non mi serviranno pi�!!!
	 */
	/*public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (ADD_COMMAND.equals(command)) {
        	ComponenteComposto composto1 = new ComponenteComposto("Composite1","Comp",0,0);
            addNode(null,new DefaultMutableTreeNode(composto1));
            /*Testo testo1 = new Testo("Testo1","Txt",0,0,"Sono un testo");
            addNode(null,testo1);
            Immagine img1 = new Immagine("Immagine1","Img",0,0,"Boh");
            addNode(null,img1);
            Link link1 = new Link("Link1", "lnk", 0, 0, "boh", "boh");
            addNode(null, link1);
            ComponenteAlternative alternative1 = new ComponenteAlternative("Alternative1","Alte",0,0);
            addNode(null,alternative1);
        } else if (REMOVE_COMMAND.equals(command)) {
            removeSelectedNode();
        } else if (CLEAR_COMMAND.equals(command)) {
            clear();
        }
	}*/


	/**
	 * @param e
	 */
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (node == null || node.isRoot())	{
			tree.clearSelection();
			//non � stato selezionato nulla o � stata selezionata la radice;
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
	
	public JTree getTree()	{
		return tree;
	}
	
	public boolean isEmpty()	{
		if ((((DefaultMutableTreeNode) model.getRoot()).getChildCount())!=0)	{
			return false;
		}
		return true;
	}
	
	public class AddAction extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -4513876659563150305L;
		private static final String COMPONENTE = "Componente";
		private static final String PARENTINDEX = "ParentIndex";
		
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (getValue(COMPONENTE)!=null)	{
				System.out.println("Sto aggiungendo");
				DefaultMutableTreeNode parent = null;
				DefaultMutableTreeNode compNode = new DefaultMutableTreeNode((Componente)getValue(COMPONENTE));
				if ((Integer)this.getValue(PARENTINDEX)==-1)	{
					
					addNode(null,compNode);
				}	else	{
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
	
	public class RemoveAction extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3159160087603892781L;

		@Override
		public void actionPerformed(ActionEvent e) {
	        TreePath currentSelection = tree.getSelectionPath();
			if (currentSelection != null) {
				if (currentSelection.getParentPath() != null) {
					DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
					String name = currentNode.toString();//((Componente) currentNode.getUserObject()).toString();
					String message = DELETEMESSAGE + name + CONFIRMMESSAGE;
					// Chiede conferma per la cancellazione dell'elemento selezionato
					int choice = JOptionPane.showConfirmDialog(getTopLevelAncestor(), message, "Warning!",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (choice == 0) {
						if (currentSelection != null) {
							if (currentNode.getChildCount() != 0) {
								message = name + NOTEMPTYMESSAGE;
								// Chiede conferma per eliminare un componente contenente altri elementi
								choice = JOptionPane.showConfirmDialog(getTopLevelAncestor(), message,"Warning!", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
							}
							if (choice == 1) {
								return;
							}
							DefaultMutableTreeNode parent = (DefaultMutableTreeNode) currentNode.getParent();
							int parentIndex;
							if (parent.isRoot())	{
								parentIndex = -1;
							}
							else	{
								DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
								parentIndex = root.getIndex(parent);
							}
							//Creo l'oggetto per l'undo action
							UndoableEdit edit = new UndoableRemoveNode(tree,(Componente)currentNode.getUserObject(),parentIndex,parent.getIndex(currentNode));
							undoSupport.postEdit(edit);
							//rimuovo effettivamente il nodo
							removeNode(currentNode);
						}
					}
				}
	        	else	{
	        		//� stata selezionata la radice perci� avviso l'utente che non pu� essere cancellata->risolto: la root non pu� essere selezionata
	        		JOptionPane.showMessageDialog(getTopLevelAncestor(),ROOTDELETE,"Error!",JOptionPane.ERROR_MESSAGE);
	        	}
			}
			else	{
				//Segnala che non c'� nulla selezionato quindi non pu� rimuovere nulla->da rivedere in disabilita Remove se non c'� nulla selezionato
				JOptionPane.showMessageDialog(getTopLevelAncestor(),EMPTYSELECTION,"Error!",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	//NOTA: una CopyAction � fondamentalmente una AddAction ma l'elemento inserito � una copia di quello esistente
	//		infatti le tengo separate per "comprensione" ma in pratica uso sempre la AddAction
	public class CopyAction extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -4612100142468392225L;
		//private static final String COMPONENTE = "Componente";
		private static final String PARENTINDEX = "ParentIndex";
		private static final String INDEX = "Index";

		@Override
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
	
	public class MoveAction extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3099984963394542917L;
		private static final String OLDPARENTINDEX = "OldParentIndex";
		private static final String NEWPARENTINDEX = "NewParentIndex";
		private static final String NEWINDEX = "NewIndex";
		private static final String OLDINDEX = "OldIndex";
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			UndoableEdit edit = new UndoableMoveNode(tree,(Integer)getValue(OLDPARENTINDEX),(Integer)getValue(NEWPARENTINDEX),(Integer)getValue(OLDINDEX),(Integer)getValue(NEWINDEX));
			undoSupport.postEdit(edit);
		}
		
	}
	
	
	public class UndoAction extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3952201139907413522L;

		@Override
		public void actionPerformed(ActionEvent e) {
			undoManager.undo();
		}
		
	}
	
	public class RedoAction extends AbstractAction	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2568562509253946673L;

		@Override
		public void actionPerformed(ActionEvent e) {
			undoManager.redo();
		}
		
	}
	
	private class UndoAdapter implements UndoableEditListener {
		
		public void undoableEditHappened (UndoableEditEvent evt) {
			UndoableEdit edit = evt.getEdit();
		    undoManager.addEdit(edit);
		}
	}
		
}