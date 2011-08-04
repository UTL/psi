package webApplication.grafica;

import java.awt.BorderLayout;
import java.awt.EventQueue;
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
import java.awt.event.InputEvent;

public class ProvaAlbero extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6651724218295819734L;
	public TreePanel treePanel;
    private JPanel workspace;
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
		setBounds(100, 100, 450, 472);
		
		ccpListener = new TransferActionListener();
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmCut = new JMenuItem("Cut");
		mntmCut.setActionCommand((String)TransferHandler.getCutAction().getValue(Action.NAME));
		mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mntmCut.setMnemonic(KeyEvent.VK_X);
		mntmCut.addActionListener(ccpListener);
		mnEdit.add(mntmCut);
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
		mntmCopy.setActionCommand((String)TransferHandler.getCopyAction().getValue(Action.NAME));
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mntmCopy.setMnemonic(KeyEvent.VK_C);
		mntmCopy.addActionListener(ccpListener);
		mnEdit.add(mntmCopy);
		
		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.setActionCommand((String)TransferHandler.getPasteAction().getValue(Action.NAME));
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mntmPaste.setMnemonic(KeyEvent.VK_V);
		mntmPaste.addActionListener(ccpListener);
		mnEdit.add(mntmPaste);
		
		
		workspace = new JPanel(new BorderLayout());
		setContentPane(workspace);
		treePanel = new TreePanel();
		treePanel.setBounds(100, 100, this.getWidth(),this.getHeight());
		workspace.add(treePanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		workspace.add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		JButton btnAdd = new JButton("Add");
		buttonPanel.add(btnAdd, BorderLayout.WEST);
		btnAdd.setActionCommand(TreePanel.ADD_COMMAND);
		
		JButton btnRemove = new JButton("Remove");
		buttonPanel.add(btnRemove, BorderLayout.EAST);
		btnRemove.setActionCommand(TreePanel.REMOVE_COMMAND);
		
		JButton btnClear = new JButton("Clear");
		buttonPanel.add(btnClear, BorderLayout.NORTH);
		btnClear.setActionCommand(TreePanel.CLEAR_COMMAND);
		
		
		btnClear.addActionListener(treePanel);
		btnRemove.addActionListener(treePanel);
		btnAdd.addActionListener(treePanel);
		
		treePanel.setLayout(new BoxLayout(treePanel, BoxLayout.X_AXIS));

	}
}