package webApplication.grafica;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BoxLayout;

public class ProvaAlbero extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6651724218295819734L;
	public TreePanel treePanel;
    private JPanel workspace;
	
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
		setBounds(100, 100, 450, 300);
		workspace = new JPanel(new BorderLayout());
		setContentPane(workspace);
		treePanel = new TreePanel();
		treePanel.setBounds(100, 100, this.getWidth(),this.getHeight());
		workspace.add(treePanel, BorderLayout.CENTER);
		
		JButton btnAdd = new JButton("Add");
		workspace.add(btnAdd,BorderLayout.WEST);
		btnAdd.setActionCommand(TreePanel.ADD_COMMAND);
		btnAdd.addActionListener(treePanel);
		
		JButton btnRemove = new JButton("Remove");
		workspace.add(btnRemove,BorderLayout.EAST);
		btnRemove.setActionCommand(TreePanel.REMOVE_COMMAND);
		btnRemove.addActionListener(treePanel);
		
		JButton btnClear = new JButton("Clear");
		workspace.add(btnClear, BorderLayout.SOUTH);
		btnClear.setActionCommand(TreePanel.CLEAR_COMMAND);
		btnClear.addActionListener(treePanel);
		treePanel.setLayout(new BoxLayout(treePanel, BoxLayout.X_AXIS));

	}
}