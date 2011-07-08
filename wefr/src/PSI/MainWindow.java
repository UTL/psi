package PSI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JInternalFrame;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.Box;
import java.awt.FlowLayout;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class MainWindow extends JFrame {

	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
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
	public MainWindow() {
		setTitle("EUD-MAMBA");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 564, 386);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setEnabled(false);
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.setEnabled(false);
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmRedo = new JMenuItem("Redo");
		mnEdit.add(mntmRedo);
		mntmRedo.setEnabled(false);

		
		JSeparator separator_1 = new JSeparator();
		mnEdit.add(separator_1);
		
		JMenuItem mntmCut = new JMenuItem("Cut");
		mnEdit.add(mntmCut);
		mntmCut.setEnabled(false);

		
		JMenuItem mntmCopy = new JMenuItem("Copy");
		mnEdit.add(mntmCopy);
		mntmCopy.setEnabled(false);

		
		JMenuItem mntmPaste = new JMenuItem("Paste");
		mnEdit.add(mntmPaste);
		mntmPaste.setEnabled(false);

		
		JMenu mnOptions = new JMenu("TODO Options");
		menuBar.add(mnOptions);
		
		JMenuItem mntmOptions = new JMenuItem("Image directory");
		mnOptions.add(mntmOptions);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTree tree = new JTree();
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("JTree") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("colors");
						node_1.add(new DefaultMutableTreeNode("blue"));
						node_1.add(new DefaultMutableTreeNode("violet"));
						node_1.add(new DefaultMutableTreeNode("red"));
						node_1.add(new DefaultMutableTreeNode("yellow"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("sports");
						node_1.add(new DefaultMutableTreeNode("basketball"));
						node_1.add(new DefaultMutableTreeNode("soccer"));
						node_1.add(new DefaultMutableTreeNode("football"));
						node_1.add(new DefaultMutableTreeNode("hockey"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("food");
						node_1.add(new DefaultMutableTreeNode("hot dogs"));
						node_1.add(new DefaultMutableTreeNode("pizza"));
						node_1.add(new DefaultMutableTreeNode("ravioli"));
						node_1.add(new DefaultMutableTreeNode("bananas"));
					add(node_1);
				}
			}
		));
		tree.setBounds(5, 49, 216, 281);
		contentPane.add(tree);
		
		JInternalFrame internalFrame = new JInternalFrame("Properties");
		internalFrame.setBounds(230, 49, 320, 274);
		contentPane.add(internalFrame);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(5, 0, 545, 37);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton button = new JButton("");
		button.setBounds(12, 4, 30, 30);
		panel.add(button);
		button.setToolTipText("Open");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		button.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/motif/icons/TreeOpen.gif")));
		
		JButton button_3 = new JButton("");
		button_3.setToolTipText("Open");
		button_3.setBounds(45, 4, 30, 30);
		panel.add(button_3);
		
		JButton button_4 = new JButton("");
		button_4.setToolTipText("Open");
		button_4.setBounds(78, 4, 30, 30);
		panel.add(button_4);
		
		JButton button_1 = new JButton("");
		button_1.setToolTipText("Open");
		button_1.setBounds(120, 4, 30, 30);
		panel.add(button_1);
		
		JButton button_2 = new JButton("");
		button_2.setToolTipText("Open");
		button_2.setBounds(153, 4, 30, 30);
		panel.add(button_2);
		
		JButton button_5 = new JButton("");
		button_5.setToolTipText("Open");
		button_5.setBounds(195, 4, 30, 30);
		panel.add(button_5);
		
		JButton button_6 = new JButton("");
		button_6.setToolTipText("Open");
		button_6.setBounds(228, 4, 30, 30);
		panel.add(button_6);
		
		JButton button_7 = new JButton("");
		button_7.setToolTipText("Open");
		button_7.setBounds(277, 4, 30, 30);
		panel.add(button_7);
		
		JButton button_8 = new JButton("");
		button_8.setToolTipText("Open");
		button_8.setBounds(310, 4, 30, 30);
		panel.add(button_8);
		
		JButton btnGenerateWebsite = new JButton("GENERATE WEBSITE");
		btnGenerateWebsite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnGenerateWebsite.setToolTipText("Open");
		btnGenerateWebsite.setBounds(365, 4, 168, 30);
		panel.add(btnGenerateWebsite);
		internalFrame.setVisible(true);
	}
	
	private void boldify(JButton button){
		Font newButtonFont=new Font(button.getFont().getName(),Font.ITALIC+Font.BOLD,button.getFont().getSize());
		button.setFont(newButtonFont);
	}
	
}
