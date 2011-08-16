package webApplication.grafica;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

import java.awt.event.InputEvent;

/**
 * 
 * @author Andrea
 *
 */
public class ProvaAlbero extends JFrame implements TreeSelectionListener, TreeModelListener	{
/*
 * TODO aggiungere focuslistener per abilitare/disabilitare i bottoni!
 */
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
		mnEdit.add(mntmCut);
		
		mntmCopy = new JMenuItem("Copy");
		mntmCopy.setEnabled(false);
		mntmCopy.setActionCommand((String)TransferHandler.getCopyAction().getValue(Action.NAME));
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mntmCopy.setMnemonic(KeyEvent.VK_C);
		mntmCopy.addActionListener(ccpListener);
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
		
		
		btnClear.addActionListener(treePanel);
		btnRemove.addActionListener(treePanel);
		btnAdd.addActionListener(treePanel);
		
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

}