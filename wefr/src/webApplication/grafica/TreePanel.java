package webApplication.grafica;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import webApplication.business.*;

public class TreePanel extends JPanel implements ActionListener, TreeSelectionListener	{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8834919285623008305L;
	
	private static String ROOT = "Home";
	private static String DELETEMESSAGE = "You are going to delete ";
	private static String CONFIRMMESSAGE ="\n Do you want to continue?";
	private static String NOTEMPTYMESSAGE = " is not empty.\n Deleting this element will delete all the elements inside.\n Do you want to continue?";
	private static String CLEARALL = "This will delete the current work.\n Do you wish to continue?";
	private static String ROOTDELETE = "You cannot delete this element because it is the tree root";
	private static String EMPTYSELECTION = "No element selected";
	private static String ADDCHILDRENNOTALLOWED = " can't have children";
	protected static String ADD_COMMAND = "add";
    protected static String REMOVE_COMMAND = "remove";
    protected static String CLEAR_COMMAND = "clear";
	
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel model;
	private JTree tree;
	
	public TreePanel() {
		init();
	}

	/**
	 * Initialize the Tree component
	 */
	private void init() {
		rootNode = new DefaultMutableTreeNode(ROOT);
		model = new DefaultTreeModel(rootNode);
		tree =  new JTree(model);
		tree.setShowsRootHandles(true); // rende visibile il nodo root
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); //solo un nodo alla volta è selezionabile
		tree.setCellRenderer(new CustomCellRenderer());
		tree.setDragEnabled(true);
		setMappings(tree);
		tree.setDropMode(DropMode.ON_OR_INSERT);
		tree.setTransferHandler(new TreeDnDTransferHandler());
		tree.addTreeSelectionListener(this); //il listener per l'evento di selezione di un elemento
		//editor delle celle
		tree.setEditable(false); // fa in modo che l'albero non sia editabile
		tree.setAutoscrolls(true);
		//pannello contenente il tree
		JScrollPane scrollPane = new JScrollPane(tree,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane);
	}

	
	private void addActionListener(TreeActionListener treeActionListener) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Reset the Tree
	 */
	protected void clear()	{
		if (rootNode.getChildCount()!=0)	{
			//chiedo conferma prima di resettare tutto
			int choice = JOptionPane.showConfirmDialog(this.getTopLevelAncestor(), CLEARALL,"Warning!",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			if (choice == 1)	{
				return;
			}
		}
		rootNode.removeAllChildren();
		model.reload();
	}
	

	/**
	 * Remove the current selected node
	 */
	protected void removeSelectedNode() {
        TreePath currentSelection = tree.getSelectionPath();
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
						model.removeNodeFromParent(currentNode);
					}
				}
			}
        	else	{
        		//è stata selezionata la radice perciò avviso l'utente che non può essere cancellata
        		JOptionPane.showMessageDialog(this.getTopLevelAncestor(),ROOTDELETE,"Error!",JOptionPane.ERROR_MESSAGE);
        	}
		}
		else	{
			//Segnala che non c'è nulla selezionato quindi non può rimuovere nulla->da rivedere in disabilita Remove se non c'è nulla selezionato
			JOptionPane.showMessageDialog(this.getTopLevelAncestor(),EMPTYSELECTION,"Error!",JOptionPane.ERROR_MESSAGE);
		}
	}


	/**
	 * Add a new node to the Tree
	 * @param parent	The parent of the node
	 * @param node		The node
	 */
	private void addNode(DefaultMutableTreeNode parent, Componente node) {
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(node);
		if (parent == null) {
			parent = rootNode;
		}	else	{
			Componente compElem = (Componente)parent.getUserObject();
			String type = compElem.getType();
			if (type==ComponenteComposto.COMPOSTOTYPE)	{
				((ComponenteComposto)compElem).aggiungiComponenteS((ComponenteSemplice) node);
			}
			else if (type==ComponenteAlternative.ALTERNATIVETYPE)	{
				((ComponenteAlternative)compElem).aggiungiAlternativa((ComponenteSemplice)node);
			}
		}
		//Verifico che il nodo genitore possa avere nodi figli anche se con il drag&drop è già esclusa come destinazione possibile (vedi canImport)
		if (parent.getAllowsChildren()==false)	{
			JOptionPane.showMessageDialog(this.getTopLevelAncestor(),parent.toString()+ADDCHILDRENNOTALLOWED,"Error!",JOptionPane.ERROR_MESSAGE);
			return;
		}
		parent.add(childNode);
		//NOTA: Java SE7 può fare switch su String ma Java SE6 no!
		if (node.getType()==Testo.TEXTTYPE || node.getType()==Immagine.IMAGETYPE || node.getType()==Link.LINKTYPE)	{
			childNode.setAllowsChildren(false);
		}
		model.reload(); //ricarico il modello dopo le modifiche
		//Espande l'albero fino al nuovo componente e centra la vista sul nuovo componente
		tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		//Seleziona l'elemtno appena creato
		tree.setSelectionPath(new TreePath(childNode.getPath()));
	}
	
	/**
	 * Aggiunge le azioni di copia/taglia/incolla
	 * @param tree
	 */
	private void setMappings(JTree tree)	{
		ActionMap map = tree.getActionMap();
	    map.put(TreeDnDTransferHandler.getCutAction().getValue(Action.NAME),TreeDnDTransferHandler.getCutAction());
	    map.put(TreeDnDTransferHandler.getCopyAction().getValue(Action.NAME),TreeDnDTransferHandler.getCopyAction());
	    map.put(TreeDnDTransferHandler.getPasteAction().getValue(Action.NAME),TreeDnDTransferHandler.getPasteAction());

	}

	/**
	 * TODO con i giusti command e listener!
	 */
	public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (ADD_COMMAND.equals(command)) {
        	ComponenteComposto composto1 = new ComponenteComposto("Composite1","Comp",0,0);
            addNode(null,composto1);
            Testo testo1 = new Testo("Testo1","Txt",0,0,"Sono un testo");
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
	}


	/**
	 * @param e
	 */
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (node == null || node.isRoot())	{
			tree.clearSelection();
			//non è stato selezionato nulla o è stata selezionata la radice;
			return;
		}
		//qui si implementeranno le operazioni da fare sul nodo per ora visualizzo solo i dati
		Componente comp = (Componente)node.getUserObject();
		System.out.println(comp.getNome());
		System.out.println(comp.getCategoria());
		System.out.println(comp.getVisibilita());
		System.out.println(comp.getEnfasi());
		System.out.println(comp.getType());
		if (comp.getType() == Testo.TEXTTYPE)	{
			String testo = ((Testo)comp).getTesto();
			System.out.println(testo);
		}
		if (comp.getType() == ComponenteComposto.COMPOSTOTYPE)	{
			System.out.println(((ComponenteComposto)comp).getComponenti());
		}
		if (comp.getType() == ComponenteAlternative.ALTERNATIVETYPE)	{
			System.out.println(((ComponenteAlternative)comp).getAlternative());
		}
	}
}