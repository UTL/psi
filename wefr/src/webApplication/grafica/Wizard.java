package webApplication.grafica;

import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.TextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Font;
import javax.swing.JList;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.TextListener;
import java.awt.event.TextEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.SwingConstants;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.ComponenteSemplice;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

import java.awt.SystemColor;
import java.awt.event.ActionListener;

public class Wizard extends JFrame  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8706297359676605479L;
	private JPanel contentPane;
	private TextField name;
	private JComboBox choice_type;
	private JTabbedPane tabbedPane;
	private JButton btnDone_text;
	private JTextArea text;
	private JButton button;
	private JButton button_2;
	private JButton button_doneComp;
	private JButton btnDone_link;
	private JTextField textField_imagepath;
	private JButton btnDone_Image;
	private TextField category;
	private JComboBox impo;
	private JComboBox emph;
	private TextField textField_url;
	private TextField textField_linktext;
	private AddNew myAddNew;
	
	PannelloAlt panel_alt;
	PannelloComp panel_comp;
	
	private JPanel panel_composite_s3;
	
	private Immagine focusedImg;
	
	private Immagine img;
	private Link lnk;
	private Testo txt;
	private ComponenteAlternative alt;
	private ComponenteComposto cmp;
	
	private Vector<ComponenteSemplice> componentiComposite;
	//private DefaultListModel componentiAlternative;
	
	//TODO come nel MainWindow, le tre stringhe tipi categorie e importanze andrebbero estratte
	private final static String TESTO = "Text";
	private final static String IMAGE ="Image";
	private final static String LINK ="Link";
	private final static String ALT = "Alternative";
	private final static String COMP = "Composite";
	private final static String[] tipi= {TESTO,IMAGE,LINK,ALT,COMP};

	private static final String[] categorie = { "Necessary", "Indifferent", "Expendable"}; //FIXME Andrebbero rese globali per tutte le classi??
	private static final String[] importanze = { "Greatly", "Normally", "Not at all"}; //FIXME Andrebbero rese globali per tutte le classi?? 
	private static final boolean CREATENEWCOMP = false;
	private Options frameOptions;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Wizard frame = new Wizard(new Options());
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
	public Wizard(Options o) {
		setResizable(false);
		setTitle("Add element");
		
		frameOptions= o;
		
		//TODO mettere input da history (magari con un metodo)
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 477, 354);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setEnabled(false);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(0, 0, 471, 326);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		tabbedPane.addTab("1", null, panel, null);
		panel.setLayout(null);
		
		choice_type = new JComboBox(tipi);
		choice_type.setBounds(181, 165, 174, 20);
		panel.add(choice_type);
		
		name = new TextField();
		name.addTextListener(new TextListener() {
			public void textValueChanged(TextEvent e) {
				if (name.getText().equalsIgnoreCase(""))
					button.setEnabled(false);
				else if (name.getText().matches(" * "))
					     button.setEnabled(false);
				else button.setEnabled(true);
			}
		});
		name.setBounds(181, 106, 174, 22);
		panel.add(name);
		name.setText("Element0"); //TODO mettere default incrementale
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setLayout(null);
		panel_1.setBounds(10, 261, 446, 49);
		panel.add(panel_1);
		
		button = new JButton("Next");
		button.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(1);
				
			}
		});
		button.setBounds(375, 11, 66, 27);
		panel_1.add(button);
		
		JButton button_1 = new JButton("Exit");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireEvent();
				dispose();
			}
		});
		button_1.setBounds(10, 11, 66, 27);
		panel_1.add(button_1);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBounds(10, 54, 446, 2);
		panel.add(separator);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(129, 109, 46, 14);
		panel.add(lblName);
		
		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(130, 168, 46, 14);
		panel.add(lblType);
		
		JLabel lblStep = new JLabel("Step:");
		lblStep.setBounds(26, 21, 46, 14);
		panel.add(lblStep);
		
		JPanel panel_14 = new JPanel();
		panel_14.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.WHITE, null));
		panel_14.setBackground(Color.WHITE);
		panel_14.setBounds(61, 7, 46, 43);
		panel.add(panel_14);
		panel_14.setLayout(null);
		
		JLabel label_6 = new JLabel("1");
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setBounds(10, 0, 26, 43);
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_14.add(label_6);
		
		JPanel panel_15 = new JPanel();
		panel_15.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_15.setLayout(null);
		panel_15.setBackground(SystemColor.control);
		panel_15.setBounds(117, 7, 46, 43);
		panel.add(panel_15);
		
		JLabel label_7 = new JLabel("2");
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_7.setBounds(10, 0, 26, 43);
		panel_15.add(label_7);
		
		JPanel panel_16 = new JPanel();
		panel_16.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_16.setLayout(null);
		panel_16.setBackground(SystemColor.control);
		panel_16.setBounds(173, 7, 46, 43);
		panel.add(panel_16);
		
		JLabel label_8 = new JLabel("3");
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_8.setBounds(10, 0, 26, 43);
		panel_16.add(label_8);
		name.getText();
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("2", null, panel_2, null);
		panel_2.setLayout(null);
		
		category = new TextField();
		category.addTextListener(new TextListener() {
			public void textValueChanged(TextEvent e) {
				if (category.getText().equalsIgnoreCase(""))
					button_2.setEnabled(false);
				else if (category.getText().matches(" * "))
					     button_2.setEnabled(false);
				else button_2.setEnabled(true);
			}
		});
		category.setBounds(180, 92, 174, 22);
		category.setText("Category0");
		panel_2.add(category);
		
		impo = new JComboBox(categorie);
		impo.setBounds(180, 192, 174, 20);
		panel_2.add(impo);
		impo.setSelectedIndex(1);
		
		emph = new JComboBox(importanze);
		emph.setBounds(180, 143, 174, 20);
		panel_2.add(emph);
		emph.setSelectedIndex(1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setLayout(null);
		panel_3.setBounds(10, 261, 446, 49);
		panel_2.add(panel_3);
		
		button_2 = new JButton("Next");
		button_2.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (choice_type.getSelectedItem().equals(tipi[0]))
					tabbedPane.setSelectedIndex(2);
				else if (choice_type.getSelectedItem().equals(tipi[2]))
					tabbedPane.setSelectedIndex(3);
				else if (choice_type.getSelectedItem().equals(tipi[1]))
				    tabbedPane.setSelectedIndex(4);
				else if (choice_type.getSelectedItem().equals(tipi[4])){
					
					panel_comp.setComponent(buildComposite());
					//TODO questo oggetto non deve essere statico, ma creato durante l'esecuzione
					/*popolaOggetti();
					Vector<ComponenteSemplice> componenti = cmp.getComponenti();
					componentiComposite= ((Vector<ComponenteSemplice>)componenti.clone());
					popolaProperties(componentiComposite);*/
					tabbedPane.setSelectedIndex(5);
				}
				else if (choice_type.getSelectedItem().equals(tipi[3])){ //alternative
					panel_alt.setComponent(buildAlternative());
					tabbedPane.setSelectedIndex(6);
					}
			}
		});
		button_2.setBounds(375, 11, 66, 27);
		panel_3.add(button_2);
		
		JButton button_3 = new JButton("Exit");
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireEvent();
				dispose();
			}
		});
		button_3.setBounds(10, 11, 66, 27);
		panel_3.add(button_3);
		
		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(0);
			}
		});
		btnBack.setBounds(299, 11, 66, 27);
		panel_3.add(btnBack);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(10, 54, 446, 2);
		panel_2.add(separator_1);
		
		JLabel lblCategoryIdentifier = new JLabel("Category identifier:");
		lblCategoryIdentifier.setBounds(56, 95, 108, 14);
		panel_2.add(lblCategoryIdentifier);
		
		JLabel lblEmphasize = new JLabel("Emphasize:");
		lblEmphasize.setBounds(97, 146, 108, 14);
		panel_2.add(lblEmphasize);
		
		JLabel lblImportance = new JLabel("Importance:");
		lblImportance.setBounds(93, 195, 108, 14);
		panel_2.add(lblImportance);
		
		JLabel label_1 = new JLabel("Step:");
		label_1.setBounds(25, 21, 46, 14);
		panel_2.add(label_1);
		
		JPanel panel_17 = new JPanel();
		panel_17.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_17.setBackground(SystemColor.control);
		panel_17.setBounds(60, 6, 46, 43);
		panel_2.add(panel_17);
		panel_17.setLayout(null);
		
		JLabel label_9 = new JLabel("1");
		label_9.setBounds(10, 0, 26, 43);
		label_9.setHorizontalAlignment(SwingConstants.CENTER);
		label_9.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_17.add(label_9);
		
		JPanel panel_18 = new JPanel();
		panel_18.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_18.setBackground(SystemColor.menu);
		panel_18.setBounds(173, 6, 46, 43);
		panel_2.add(panel_18);
		panel_18.setLayout(null);
		
		JLabel label_10 = new JLabel("3");
		label_10.setBounds(10, 0, 26, 43);
		label_10.setHorizontalAlignment(SwingConstants.CENTER);
		label_10.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_18.add(label_10);
		
		JPanel panel_19 = new JPanel();
		panel_19.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_19.setBackground(SystemColor.text);
		panel_19.setBounds(116, 6, 46, 43);
		panel_2.add(panel_19);
		panel_19.setLayout(null);
		
		JLabel label_11 = new JLabel("2");
		label_11.setBounds(10, 0, 26, 43);
		label_11.setHorizontalAlignment(SwingConstants.CENTER);
		label_11.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_19.add(label_11);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("3t", null, panel_4, null);
		panel_4.setLayout(null);
		
		JLabel lblEnterText = new JLabel("Enter text:");
		lblEnterText.setBounds(25, 69, 94, 14);
		panel_4.add(lblEnterText);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.BLACK);
		separator_2.setBounds(10, 54, 446, 2);
		panel_4.add(separator_2);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_5.setLayout(null);
		panel_5.setBounds(10, 261, 446, 49);
		panel_4.add(panel_5);
		
		btnDone_text = new JButton("Done");
		
		btnDone_text.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				/*System.out.println("Name:"+textField.getText());
				System.out.println("Type:"+choice.getSelectedItem());
				System.out.println("Category:"+textField_1.getText());
				System.out.println("Emphasize:"+choice_2.getSelectedItem());
				System.out.println("Importance:"+choice_1.getSelectedItem());
				System.out.println("Text:"+textArea.getText());*/
				
				fireEvent(CREATENEWCOMP);
				dispose();
			}

			
		});
		
		btnDone_text.setEnabled(false);
		btnDone_text.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDone_text.setBounds(375, 11, 66, 27);
		panel_5.add(btnDone_text);
		
		JButton button_5 = new JButton("Exit");
		button_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireEvent();
				dispose();
			}
		});
		button_5.setBounds(10, 11, 66, 27);
		panel_5.add(button_5);
		
		JButton button_6 = new JButton("Back");
		button_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		button_6.setBounds(299, 11, 66, 27);
		panel_5.add(button_6);
		
		JButton btnImportFromFile = new JButton("Import from file");
		btnImportFromFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				try {
					chooseFile(fileChooser.showOpenDialog(contentPane), fileChooser, text);
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				btnDone_text.setEnabled(true);
			}
		});
		btnImportFromFile.setBounds(25, 223, 171, 27);
		panel_4.add(btnImportFromFile);
		
		text = new JTextArea();
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (text.getText().equalsIgnoreCase(""))
					btnDone_text.setEnabled(false);
				else if (text.getText().matches("[ \n]*"))
				     btnDone_text.setEnabled(false);
				else btnDone_text.setEnabled(true);
			}
		});
		JScrollPane scrollingArea = new JScrollPane(text);
		scrollingArea.setBounds(25, 89, 423, 124);
		panel_4.add(scrollingArea);
		
		JLabel label_2 = new JLabel("Step:");
		label_2.setBounds(25, 21, 46, 14);
		panel_4.add(label_2);
		
		JPanel panel_20 = new JPanel();
		panel_20.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_20.setLayout(null);
		panel_20.setBackground(SystemColor.control);
		panel_20.setBounds(60, 6, 46, 43);
		panel_4.add(panel_20);
		
		JLabel label_12 = new JLabel("1");
		label_12.setHorizontalAlignment(SwingConstants.CENTER);
		label_12.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_12.setBounds(10, 0, 26, 43);
		panel_20.add(label_12);
		
		JPanel panel_21 = new JPanel();
		panel_21.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_21.setLayout(null);
		panel_21.setBackground(SystemColor.menu);
		panel_21.setBounds(116, 6, 46, 43);
		panel_4.add(panel_21);
		
		JLabel label_13 = new JLabel("2");
		label_13.setHorizontalAlignment(SwingConstants.CENTER);
		label_13.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_13.setBounds(10, 0, 26, 43);
		panel_21.add(label_13);
		
		JPanel panel_22 = new JPanel();
		panel_22.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_22.setLayout(null);
		panel_22.setBackground(SystemColor.text);
		panel_22.setBounds(173, 6, 46, 43);
		panel_4.add(panel_22);
		
		JLabel label_14 = new JLabel("3");
		label_14.setHorizontalAlignment(SwingConstants.CENTER);
		label_14.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_14.setBounds(10, 0, 26, 43);
		panel_22.add(label_14);
		
		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("3l", null, panel_6, null);
		panel_6.setLayout(null);
		
		
		JLabel lblLinkTarget = new JLabel("URL:");
		lblLinkTarget.setBounds(132, 121, 66, 14);
		panel_6.add(lblLinkTarget);
		
		textField_url = new TextField();
		textField_url.setBounds(204, 113, 174, 22);
		panel_6.add(textField_url);
		
		textField_linktext = new TextField();
		textField_linktext.addTextListener(new TextListener() {
			public void textValueChanged(TextEvent arg0) {
				if (textField_linktext.getText().equalsIgnoreCase(""))
					btnDone_link.setEnabled(false);
				else if (textField_linktext.getText().matches(" * "))
					     btnDone_link.setEnabled(false);
				else btnDone_link.setEnabled(true);
			}
		});
		textField_linktext.setBounds(204, 158, 174, 22);
		panel_6.add(textField_linktext);
		
		JLabel lblLinkText = new JLabel("Link text:");
		lblLinkText.setBounds(132, 166, 66, 14);
		panel_6.add(lblLinkText);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.BLACK);
		separator_3.setBounds(10, 54, 446, 2);
		panel_6.add(separator_3);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_7.setLayout(null);
		panel_7.setBounds(10, 259, 446, 49);
		panel_6.add(panel_7);
		
		btnDone_link = new JButton("Done");
		btnDone_link.setEnabled(false);
		btnDone_link.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*System.out.println("Name:"+textField.getText());
				System.out.println("Type:"+choice.getSelectedItem());
				System.out.println("Category:"+textField_1.getText());
				System.out.println("Emphasize:"+choice_2.getSelectedItem());
				System.out.println("Importance:"+choice_1.getSelectedItem());
				System.out.println("Value: link target:"+textField_3.getText()+"  link text:"+textField_4.getText());*/
				lnk= new Link(name.getText(), category.getText(), impo.getSelectedIndex(), emph.getSelectedIndex(),textField_url.getText(), textField_linktext.getText());
				fireEvent(CREATENEWCOMP);
				dispose();
			}
		});
		btnDone_link.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDone_link.setBounds(375, 11, 66, 27);
		panel_7.add(btnDone_link);
		
		JButton button_8 = new JButton("Exit");
		button_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireEvent();
				dispose();
			}
		});
		button_8.setBounds(10, 11, 66, 27);
		panel_7.add(button_8);
		
		JButton button_9 = new JButton("Back");
		button_9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		button_9.setBounds(299, 11, 66, 27);
		panel_7.add(button_9);
		
		JLabel label_3 = new JLabel("Step:");
		label_3.setBounds(27, 21, 46, 14);
		panel_6.add(label_3);
		
		JPanel panel_23 = new JPanel();
		panel_23.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_23.setLayout(null);
		panel_23.setBackground(SystemColor.control);
		panel_23.setBounds(61, 6, 46, 43);
		panel_6.add(panel_23);
		
		JLabel label_15 = new JLabel("1");
		label_15.setHorizontalAlignment(SwingConstants.CENTER);
		label_15.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_15.setBounds(10, 0, 26, 43);
		panel_23.add(label_15);
		
		JPanel panel_24 = new JPanel();
		panel_24.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_24.setLayout(null);
		panel_24.setBackground(SystemColor.control);
		panel_24.setBounds(117, 6, 46, 43);
		panel_6.add(panel_24);
		
		JLabel label_16 = new JLabel("2");
		label_16.setHorizontalAlignment(SwingConstants.CENTER);
		label_16.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_16.setBounds(10, 0, 26, 43);
		panel_24.add(label_16);
		
		JPanel panel_25 = new JPanel();
		panel_25.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_25.setLayout(null);
		panel_25.setBackground(SystemColor.text);
		panel_25.setBounds(173, 6, 46, 43);
		panel_6.add(panel_25);
		
		JLabel label_17 = new JLabel("3");
		label_17.setHorizontalAlignment(SwingConstants.CENTER);
		label_17.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_17.setBounds(10, 0, 26, 43);
		panel_25.add(label_17);
		
		JPanel panel_8 = new JPanel();
		tabbedPane.addTab("3i", null, panel_8, null);
		panel_8.setLayout(null);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setForeground(Color.BLACK);
		separator_4.setBounds(10, 54, 446, 2);
		panel_8.add(separator_4);
		
		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_9.setLayout(null);
		panel_9.setBounds(10, 259, 446, 49);
		panel_8.add(panel_9);
		
		btnDone_Image = new JButton("Done");
		btnDone_Image.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*System.out.println("Name:"+textField.getText());
				System.out.println("Type:"+choice.getSelectedItem());
				System.out.println("Category:"+textField_1.getText());
				System.out.println("Emphasize:"+choice_2.getSelectedItem());
				System.out.println("Importance:"+choice_1.getSelectedItem());
				System.out.println("File path:"+textField_5.getText());*/
				img = new Immagine(name.getText(), category.getText(), impo.getSelectedIndex(),emph.getSelectedIndex(), textField_imagepath.getText());
				fireEvent(CREATENEWCOMP);
				dispose();
			}
		});
		btnDone_Image.setEnabled(false);
		btnDone_Image.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDone_Image.setBounds(375, 11, 66, 27);
		panel_9.add(btnDone_Image);
		
		JButton button_11 = new JButton("Exit");
		button_11.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireEvent();
				dispose();
			}
		});
		button_11.setBounds(10, 11, 66, 27);
		panel_9.add(button_11);
		
		JButton button_12 = new JButton("Back");
		button_12.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		button_12.setBounds(299, 11, 66, 27);
		panel_9.add(button_12);
		
		JLabel lblFilePath = new JLabel("File path:");
		lblFilePath.setBounds(87, 146, 59, 14);
		panel_8.add(lblFilePath);
		
	    textField_imagepath = new JTextField();
	    
	    textField_imagepath.setToolTipText("Path of the image file");
		textField_imagepath.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				
				updateImagePath();
			}
			public void removeUpdate(DocumentEvent e) {
				updateImagePath();
			// text was deleted
			}
			public void insertUpdate(DocumentEvent e) {
				updateImagePath();

			// text was inserted
			}
			});
		
		textField_imagepath.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				updateImagePath();
			}

			
		});
	    
		textField_imagepath.setBounds(150, 143, 174, 22);
		panel_8.add(textField_imagepath);
		
		JButton btnBrowse = new JButton("Browse\r\n");
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				chooseFile(fileChooser.showOpenDialog(contentPane), fileChooser, textField_imagepath);
				btnDone_Image.setEnabled(true);
			}
		});
		btnBrowse.setBounds(346, 139, 99, 29);
		panel_8.add(btnBrowse);
		
		JLabel label_4 = new JLabel("Step:");
		label_4.setBounds(23, 22, 46, 14);
		panel_8.add(label_4);
		
		JPanel panel_26 = new JPanel();
		panel_26.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_26.setLayout(null);
		panel_26.setBackground(SystemColor.control);
		panel_26.setBounds(61, 6, 46, 43);
		panel_8.add(panel_26);
		
		JLabel label_18 = new JLabel("1");
		label_18.setHorizontalAlignment(SwingConstants.CENTER);
		label_18.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_18.setBounds(10, 0, 26, 43);
		panel_26.add(label_18);
		
		JPanel panel_27 = new JPanel();
		panel_27.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_27.setLayout(null);
		panel_27.setBackground(SystemColor.control);
		panel_27.setBounds(117, 6, 46, 43);
		panel_8.add(panel_27);
		
		JLabel label_19 = new JLabel("2");
		label_19.setHorizontalAlignment(SwingConstants.CENTER);
		label_19.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_19.setBounds(10, 0, 26, 43);
		panel_27.add(label_19);
		
		JPanel panel_28 = new JPanel();
		panel_28.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_28.setLayout(null);
		panel_28.setBackground(SystemColor.text);
		panel_28.setBounds(173, 6, 46, 43);
		panel_8.add(panel_28);
		
		JLabel label_20 = new JLabel("3");
		label_20.setHorizontalAlignment(SwingConstants.CENTER);
		label_20.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_20.setBounds(10, 0, 26, 43);
		panel_28.add(label_20);
		
		panel_composite_s3 = new JPanel();
		tabbedPane.addTab("3c", null, panel_composite_s3, null);
		panel_composite_s3.setLayout(null);
		
		JPanel panel_12 = new JPanel();
		panel_comp = new PannelloComp(this, frameOptions);
		panel_comp.setLocation(6, 56);
		panel_comp.setSize(436, 201);
		panel_composite_s3.add(panel_comp);
		panel_12.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_12.setLayout(null);
		panel_12.setBounds(10, 259, 446, 49);
		panel_composite_s3.add(panel_12);
		
		button_doneComp = new JButton("Done");
		button_doneComp.setEnabled(false);
		button_doneComp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				/*int i;
				cmp= new ComponenteComposto(textField.getText(), textField_1.getText(), choice_1.getSelectedIndex(),choice_2.getSelectedIndex());
				for(i=list_composite.getComponentCount()-1; i>=0; i-- ){
	    		cmp.aggiungiComponenteS((ComponenteSemplice) list_composite.);	
				}*/
				fireEvent(CREATENEWCOMP);
				dispose();
			}
		});
		button_doneComp.setFont(new Font("Tahoma", Font.BOLD, 12));
		button_doneComp.setBounds(375, 11, 66, 27);
		panel_12.add(button_doneComp);
		
		JButton button_7 = new JButton("Exit");
		button_7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireEvent();
				dispose();
			}
		});
		button_7.setBounds(10, 11, 66, 27);
		panel_12.add(button_7);
		
		JButton button_10 = new JButton("Back");
		button_10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		button_10.setBounds(299, 11, 66, 27);
		panel_12.add(button_10);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(Color.BLACK);
		separator_5.setBounds(10, 54, 446, 2);
		panel_composite_s3.add(separator_5);
		
		JLabel lblStep_1 = new JLabel("Step:");
		lblStep_1.setBounds(28, 21, 46, 14);
		panel_composite_s3.add(lblStep_1);
		
		JPanel panel_29 = new JPanel();
		panel_29.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_29.setLayout(null);
		panel_29.setBackground(SystemColor.control);
		panel_29.setBounds(61, 6, 46, 43);
		panel_composite_s3.add(panel_29);
		
		JLabel label_21 = new JLabel("1");
		label_21.setHorizontalAlignment(SwingConstants.CENTER);
		label_21.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_21.setBounds(10, 0, 26, 43);
		panel_29.add(label_21);
		
		JPanel panel_30 = new JPanel();
		panel_30.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_30.setLayout(null);
		panel_30.setBackground(SystemColor.menu);
		panel_30.setBounds(117, 6, 46, 43);
		panel_composite_s3.add(panel_30);
		
		JLabel label_22 = new JLabel("2");
		label_22.setHorizontalAlignment(SwingConstants.CENTER);
		label_22.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_22.setBounds(10, 0, 26, 43);
		panel_30.add(label_22);
		
		JPanel panel_31 = new JPanel();
		panel_31.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_31.setLayout(null);
		panel_31.setBackground(SystemColor.text);
		panel_31.setBounds(173, 6, 46, 43);
		panel_composite_s3.add(panel_31);
		
		JLabel label_23 = new JLabel("3");
		label_23.setHorizontalAlignment(SwingConstants.CENTER);
		label_23.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_23.setBounds(10, 0, 26, 43);
		panel_31.add(label_23);
		
		JPanel panel_11 = new JPanel();
		tabbedPane.addTab("3a", null, panel_11, null);
		panel_11.setLayout(null);
		
		panel_alt = new PannelloAlt(this, frameOptions);
		panel_alt.setSize(421, 193);
		panel_alt.setLocation(25, 61);
		panel_11.add(panel_alt);
		
		JPanel panel_13 = new JPanel();
		panel_13.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_13.setLayout(null);
		panel_13.setBounds(10, 259, 446, 49);
		panel_11.add(panel_13);
		
		JButton button_doneAlt = new JButton("Done");
		button_doneAlt.setEnabled(true);
		button_doneAlt.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*int i;
				alt= new ComponenteAlternative(textField.getText(), textField_1.getText(), choice_1.getSelectedIndex(),choice_2.getSelectedIndex());
				for(i=list_alternative.getComponentCount()-1; i>=0; i-- ){
					alt.aggiungiAlternativa((ComponenteSemplice)list_alternative.getSelectedValue());	
				}*/
				fireEvent(CREATENEWCOMP);
				dispose();
			}
		});
		button_doneAlt.setFont(new Font("Tahoma", Font.BOLD, 12));
		button_doneAlt.setBounds(375, 11, 66, 27);
		panel_13.add(button_doneAlt);
		
		JButton button_14 = new JButton("Exit");
		button_14.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireEvent();
				dispose();
			}
		});
		button_14.setBounds(10, 11, 66, 27);
		panel_13.add(button_14);
		
		JButton button_15 = new JButton("Back");
		button_15.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		
		button_15.setBounds(299, 11, 66, 27);
		panel_13.add(button_15);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setForeground(Color.BLACK);
		separator_6.setBounds(10, 54, 446, 2);
		panel_11.add(separator_6);
		
		JLabel label = new JLabel("Elements:");
		label.setBounds(72, 64, 66, 14);
		panel_11.add(label);
		
		JLabel label_5 = new JLabel("Step:");
		label_5.setBounds(20, 21, 46, 14);
		panel_11.add(label_5);
		
		JPanel panel_32 = new JPanel();
		panel_32.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_32.setLayout(null);
		panel_32.setBackground(SystemColor.control);
		panel_32.setBounds(61, 6, 46, 43);
		panel_11.add(panel_32);
		
		JLabel label_24 = new JLabel("1");
		label_24.setHorizontalAlignment(SwingConstants.CENTER);
		label_24.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_24.setBounds(10, 0, 26, 43);
		panel_32.add(label_24);
		
		JPanel panel_33 = new JPanel();
		panel_33.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_33.setLayout(null);
		panel_33.setBackground(SystemColor.menu);
		panel_33.setBounds(117, 6, 46, 43);
		panel_11.add(panel_33);
		
		JLabel label_25 = new JLabel("2");
		label_25.setHorizontalAlignment(SwingConstants.CENTER);
		label_25.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_25.setBounds(10, 0, 26, 43);
		panel_33.add(label_25);
		
		JPanel panel_34 = new JPanel();
		panel_34.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_34.setLayout(null);
		panel_34.setBackground(SystemColor.text);
		panel_34.setBounds(173, 6, 46, 43);
		panel_11.add(panel_34);
		
		JLabel label_26 = new JLabel("3");
		label_26.setHorizontalAlignment(SwingConstants.CENTER);
		label_26.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_26.setBounds(10, 0, 26, 43);
		panel_34.add(label_26);
		
		
	}
	

	protected ComponenteComposto buildComposite() {
		cmp = new ComponenteComposto(name.getText(), category.getText(), impo.getSelectedIndex(), emph.getSelectedIndex());
		return cmp;
	}

	protected ComponenteAlternative buildAlternative() {
		alt = new ComponenteAlternative(name.getText(), category.getText(), impo.getSelectedIndex(), emph.getSelectedIndex());
		return alt;
	}

	private void chooseFile(int chooserValue, JFileChooser fc, JTextField target){
		//TODO settare le cartelle di default
		if (chooserValue == JFileChooser.APPROVE_OPTION) {
            target.setText(fc.getSelectedFile().getAbsolutePath());
        } 

	}
	
	private void chooseFile(int chooserValue, JFileChooser fc, JTextArea target) throws IOException{
		//TODO settare le cartelle di default
		if (chooserValue == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String letta = readFile(file);
            target.setText(letta);
            
        } 

	}
	
	public static String readFile (File file) throws IOException{
		//FIXME se si prova a leggere il file /dev/zero va in loop infinito (la console continua a leggere e scrollare...)
		 String letto = "";
	     FileReader reader = new FileReader(file);
	     while(true)
	        { int x = reader.read(); // read restituisce un intero che vale -1 se il file è finito
	          if (x == -1) break;     
	          char c = (char) x;      
	          letto = letto+c;
	          System.out.println( letto+c);
	        }
	     return letto;
	}
	
	 private ArrayList<MyEventClassListener> _listeners = new ArrayList<MyEventClassListener>();
		
	 public synchronized void addEventListener(MyEventClassListener listener)  {
		 _listeners.add(listener);
	 }
	 public synchronized void removeEventListener(MyEventClassListener listener)   {
		 _listeners.remove(listener);
	 }
	 
	 private synchronized void fireEvent() {	
			MyEventClass event = new MyEventClass(this);
			Iterator<MyEventClassListener> i = _listeners.iterator();
			while(i.hasNext())  {
				((MyEventClassListener) i.next()).handleMyEventClassEvent(event);
			}
		}
	 
	 private void popolaProperties(Vector<ComponenteSemplice> componenti){
			//FIXME questo metodo fa schifo
			//TODO verificare se il list_composite ha le scrollbar (non credo)
			//TODO aggiungere le iconcine in parte ai nomi
			//TODO disabilitare l'add existing quando non esistono elementi da aggiungere
			
			/*if(list_composite != null && panel_composite_s3!=null)
				panel_composite_s3.remove(list_composite);
			
			String[] nomiComponenti= new String[componenti.size()];
			
			//TODO mettere icone per il tipo degli oggetti
			int i;
			for(i=0; i < componenti.size();i++){
				nomiComponenti[i]=componenti.get(i).getNome();
			}
			
			list_composite = new JList(nomiComponenti);
			list_composite.setBounds(28, 89, 408, 132);
			list_composite.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent arg0) {
					if(list_composite.getSelectedIndex()>-1) //esiste almeno un elemento
						button_deleteComp.setEnabled(true);
				}
			});
			panel_composite_s3.add(list_composite);
		
			button_deleteComp.setEnabled(false);

			list_composite.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				
					if(list_composite.getSelectedIndex()>-1) //esiste almeno un elemento
						button_deleteComp.setEnabled(true);
				}
			});
			
			panel_composite_s3.repaint();
			
			*/
			
		}
	 
	 
	 
	 private void updateImagePath() {
			// TODO Evitare di salvare se il path e' errato? Se si sostituire il metodo con il commento qua sotto
			/* if(focusedImg!= null && checkImagePath())
			 *	 focusedImg.setPath(textField_imagepath.getText());
			 */
			if(focusedImg!= null)
				focusedImg.setPath(textField_imagepath.getText());
			checkImagePath();
		}
	 
	 private boolean checkImagePath() {
			if(isPathCorrect(textField_imagepath.getText())){
				textField_imagepath.setToolTipText("Path of the image file");
				btnDone_Image.setEnabled(true);
				return true;
			}
			else {
				textField_imagepath.setToolTipText("The file doesn't exist or is not readable");
				btnDone_Image.setEnabled(false);
			}
			return false;
		}
	 
		private boolean isPathCorrect(String path){
			//TODO fare prove con files e dir verificare che funzioni
			File daControllare = new File(path);
			if(daControllare.isFile() && daControllare.canRead())
				return true;
			return false;
		}
		
		private synchronized void fireEvent(boolean onlyDispose) {	
			MyEventClass event = null;
			if (onlyDispose == CREATENEWCOMP)
				event = new MyEventClass(this, buildNewComp());
			Iterator<MyEventClassListener> i = _listeners.iterator();
			while(i.hasNext())  {
				((MyEventClassListener) i.next()).handleMyEventClassEvent(event);
			}
		}

		private Componente buildNewComp() {
			System.out.println("builder");
			if(choice_type.getSelectedItem()==TESTO)
				return new Testo(name.getText(), category.getText(), impo.getSelectedIndex(), emph.getSelectedIndex(), text.getText());
			else if(choice_type.getSelectedItem()==IMAGE)
				return new Immagine(name.getText(), category.getText(), impo.getSelectedIndex(), emph.getSelectedIndex(), textField_imagepath.getText());			
			else if(choice_type.getSelectedItem()==LINK)
				return new Link(name.getText(), category.getText(), impo.getSelectedIndex(), emph.getSelectedIndex(), textField_url.getText(), textField_url.getText());
			else if(choice_type.getSelectedItem()==ALT){
				
				//TODO settare la lista delle alternative
				//newAlt.setAlternative()
				return alt;
			}
			else {
				//ComponenteComposto newComp = new ComponenteComposto(name.getText(), category.getText(), impo.getSelectedIndex(), emph.getSelectedIndex());
				//TODO settare la lista delle alternative
				//newComp.setComponenti(cs);
				return cmp;
				}
		}
		
}













