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
import java.awt.GridLayout;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JFormattedTextField;
import java.awt.Panel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;
import java.awt.TextArea;
import javax.swing.JEditorPane;

public class MainWindow extends JFrame {

	
	private JPanel contentPane;
	private JTextField textField_Name;
	private JTextField textField_Type;
	private JTextField textField_Category;

	private static final String[] categorie = { "Necessary", "Indifferent", "Expandable"}; //FIXME Andrebbero rese globali per tutte le classi??
	private static final String[] importanze = { "Greatly", "TODO", "TODO"}; //FIXME Andrebbero rese globali per tutte le classi?? E ne mancano 2 che non ricordo
	
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
		setBounds(100, 100, 728, 465);
		
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
		
		JPanel panel = new JPanel();
		panel.setBounds(5, 0, 710, 37);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
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
		
		JPanel properties = new JPanel();
		properties.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), " Properties ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		properties.setLayout(null);
		properties.setBounds(249, 49, 466, 353);
		contentPane.add(properties);
		
		JPanel id_panel = new JPanel();
		id_panel.setBounds(12, 18, 193, 125);
		properties.add(id_panel);
		id_panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), " ID ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		id_panel.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 40, 51, 15);
		id_panel.add(lblName);
		
		textField_Name = new JTextField();
		textField_Name.setBounds(67, 40, 114, 19);
		id_panel.add(textField_Name);
		textField_Name.setColumns(10);
		
		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(12, 70, 51, 15);
		id_panel.add(lblType);
		
		textField_Type = new JTextField();
		textField_Type.setEditable(false);
		textField_Type.setColumns(10);
		textField_Type.setBounds(67, 70, 114, 19);
		id_panel.add(textField_Type);
		
		JPanel presentation_panel = new JPanel();
		presentation_panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), " Presentation ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		presentation_panel.setBounds(217, 18, 237, 125);
		properties.add(presentation_panel);
		presentation_panel.setLayout(null);
		
		JLabel lblCategory = new JLabel("Category:");
		lblCategory.setBounds(12, 24, 81, 15);
		presentation_panel.add(lblCategory);
		
		JLabel lblEmphasize = new JLabel("Emphasize:");
		lblEmphasize.setBounds(12, 54, 81, 14);
		presentation_panel.add(lblEmphasize);
		
		
		JComboBox comboBox_Importance = new JComboBox(importanze);
		comboBox_Importance.setBounds(111, 49, 112, 24);
		presentation_panel.add(comboBox_Importance);
		
		textField_Category = new JTextField();
		textField_Category.setColumns(10);
		textField_Category.setBounds(109, 22, 114, 19);
		presentation_panel.add(textField_Category);
		
		JLabel lblImportance = new JLabel("Importance:");
		lblImportance.setBounds(12, 86, 97, 15);
		presentation_panel.add(lblImportance);
		
				
		JComboBox comboBox_Emphasize = new JComboBox(categorie);
		comboBox_Emphasize.setBounds(111, 85, 112, 24);
		presentation_panel.add(comboBox_Emphasize);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), " Content ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_1.setLayout(null);
		panel_1.setBounds(12, 155, 442, 186);
		properties.add(panel_1);
		
		JLabel label = new JLabel("Name:");
		label.setBounds(12, 23, 51, 15);
		panel_1.add(label);
		
		JEditorPane editorPane = new JEditorPane();		//TODO mancano le scrollbar all'editorpane
		editorPane.setBounds(22, 50, 408, 124);
		panel_1.add(editorPane);

	}
	
	private void boldify(JButton button){
		Font newButtonFont=new Font(button.getFont().getName(),Font.ITALIC+Font.BOLD,button.getFont().getSize());
		button.setFont(newButtonFont);
	}
}
