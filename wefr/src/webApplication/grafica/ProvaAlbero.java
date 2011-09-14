package webApplication.grafica;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.TransferHandler;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import webApplication.business.ComponenteComposto;
import webApplication.grafica.TreePanel.AddAction;
import webApplication.grafica.TreePanel.RedoAction;
import webApplication.grafica.TreePanel.RemoveAction;
import webApplication.grafica.TreePanel.UndoAction;

import java.awt.event.InputEvent;

/**
 * 
 * @author Andrea
 *
 */
public class ProvaAlbero extends JFrame implements TreeSelectionListener, TreeModelListener, ActionListener	{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6651724218295819734L;
	private TreePanel treePanel;
    private JPanel workspace;
    private JPanel buttonPanel;
    private JButton btnClear;
    private JButton btnRemove;
    private JButton btnAdd;
    private JButton btnUndo;
	private JButton btnRedo;
    private JMenuBar menuBar;
    private JMenuItem mntmCut;
    private JMenuItem mntmCopy;
    private JMenuItem mntmPaste;
     
	private TransferActionListener ccpListener;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProvaAlbero frame = new ProvaAlbero();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ProvaAlbero() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 970, 643);
		
		ccpListener = new TransferActionListener();
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mntmCut = new JMenuItem("Cut");
		mntmCut.setEnabled(false);
		mntmCut.setActionCommand((String)TransferHandler.getCutAction().getValue(Action.NAME));
		mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mntmCut.setMnemonic(KeyEvent.VK_X);
		mntmCut.addActionListener(ccpListener);
		mntmCut.addActionListener(this);
		mnEdit.add(mntmCut);
		
		mntmCopy = new JMenuItem("Copy");
		mntmCopy.setEnabled(false);
		mntmCopy.setActionCommand((String)TransferHandler.getCopyAction().getValue(Action.NAME));
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mntmCopy.setMnemonic(KeyEvent.VK_C);
		mntmCopy.addActionListener(ccpListener);
		mntmCopy.addActionListener(this);
		mnEdit.add(mntmCopy);
		
		mntmPaste = new JMenuItem("Paste");
		mntmPaste.setEnabled(false);
		mntmPaste.setActionCommand((String)TransferHandler.getPasteAction().getValue(Action.NAME));
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mntmPaste.setMnemonic(KeyEvent.VK_V);
		mntmPaste.addActionListener(ccpListener);
		mnEdit.add(mntmPaste);
		
		
		workspace = new JPanel(new BorderLayout());
		setContentPane(workspace);
		
		treePanel = new TreePanel();
		treePanel.setLayout(new BoxLayout(treePanel, BoxLayout.X_AXIS));
		workspace.add(treePanel, BorderLayout.CENTER);
		
		buttonPanel = new JPanel();
		workspace.add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		btnAdd = new JButton("Add");
		buttonPanel.add(btnAdd, BorderLayout.WEST);
		btnAdd.setActionCommand(TreePanel.ADD_COMMAND);
		
		btnRemove = new JButton("Remove");
		btnRemove.setEnabled(false);
		buttonPanel.add(btnRemove, BorderLayout.EAST);
		btnRemove.setActionCommand(TreePanel.REMOVE_COMMAND);
		
		btnClear = new JButton("Clear");
		btnClear.setEnabled(false);
		buttonPanel.add(btnClear, BorderLayout.NORTH);
		btnClear.setActionCommand(TreePanel.CLEAR_COMMAND);
		
		btnUndo = new JButton("Undo");
		buttonPanel.add(btnUndo,BorderLayout.CENTER);		
		
		btnRedo = new JButton("Redo");
		buttonPanel.add(btnRedo,BorderLayout.SOUTH);
		
		//btnClear.addActionListener(treePanel);
		
		RemoveAction removeAction = treePanel.new RemoveAction();
		btnRemove.addActionListener(removeAction);
		
		AddAction addAction = treePanel.new AddAction();
		ComponenteComposto composto1 = new ComponenteComposto("Composite1","Comp",0,0);
		int pi = ((DefaultMutableTreeNode) treePanel.getTree().getModel().getRoot()).getChildCount();
		addAction.setValues(composto1, -1, pi);
		btnAdd.addActionListener(addAction);
		
		UndoAction undoAction = treePanel.new UndoAction();
		btnUndo.addActionListener(undoAction);
		
		RedoAction redoAction = treePanel.new RedoAction();
		btnRedo.addActionListener(redoAction);
		
		treePanel.getTree().addTreeSelectionListener(this);
		treePanel.getTree().getModel().addTreeModelListener(this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 * Abilita o disabilita il pulsante Remove a seconda che ci sia un elemento selezionato o no (la root non è selezionabile)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (treePanel.getTree().getLastSelectedPathComponent()!=null)	{
			mntmCopy.setEnabled(true);
			mntmCut.setEnabled(true);
			btnRemove.setEnabled(true);
		}	else 	{
			mntmCopy.setEnabled(false);
			mntmCut.setEnabled(false);
			btnRemove.setEnabled(false);
		}	
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		return;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
	 * Abilita il pulsante Clear dopo che un nodo è stato aggiunto
	 */
	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		btnClear.setEnabled(true);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
	 * Abilita o disabilita il pulsante Clear dopo che il pulsante Remove è stato usato
	 */
	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		if (((DefaultMutableTreeNode)(treePanel.getTree().getModel().getRoot())).getChildCount()!=0)	{
			btnClear.setEnabled(true);
		}	else	{
			btnClear.setEnabled(false);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
	 * Disabilita o abilita il pulsante Clear dopo che il pulsante stesso è stato usato
	 */
	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		if (treePanel.isEmpty())	{
			btnClear.setEnabled(false);
		}	else	{
			btnClear.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((e.getActionCommand().equalsIgnoreCase((String)TransferHandler.getCopyAction().getValue(Action.NAME))) || (e.getActionCommand().equalsIgnoreCase((String)TransferHandler.getCutAction().getValue(Action.NAME))))	{
			mntmPaste.setEnabled(true);
		}
	}

}