package webApplication.grafica;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.ComponenteSemplice;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

import webApplication.grafica.TreePanel;


import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane;
	private static JTextField textField_Name;
	private static JTextField textField_Type;
	private static JTextField textField_Category;
	private static JComboBox comboBox_Importance;
	private static JComboBox comboBox_Emphasize;
	private static JTextArea editorPane_text;
	private static JTextField textField_imagepath;
	private static JPanel content_panel;
	private static JList list_composite;
	private static JPanel panel_composite;
	private static JButton button_deleteFromComp;
	private static JButton button_addExistComp;
	private static JButton button_addNewComp;
	private static JPanel errorePath;
	private static JPanel erroreTestoLink;
	private static JPanel erroreUrl;
	
	public static final int LOADSAVE = 0;
	public static final int IMAGE = 1;
	public static final int TEXT = 2;
	
	private Wizard myWizard;
	
	private String currentProject;
	
	public MainWindow thisWindow;
	
	private static Options frameOptions;
	
	//oggetti che per fare prove con l'interfaccia
	//TODO rimuovere questi oggetti dopo aver verificato che tutto funziona
	private JScrollPane scrollPane_composite;

	// oggetti che per fare prove con l'interfaccia
	// TODO rimuovere questi oggetti dopo aver verificato che tutto funziona
	private Immagine img;
	private Link lnk;
	private Testo txt;
	private ComponenteAlternative alt;
	private ComponenteComposto cmp;

	private static Componente focused;

	private static Testo focusedTxt;
	private static Immagine focusedImg;
	private static Link focusedLnk;
	private static ComponenteComposto focusedCmp;
	private static ComponenteAlternative focusedAlt;

	private static final String URL_REGEX =
            "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
	
	
	private static final String[] categorie = { "Necessary", "Indifferent", "Expendable"}; //FIXME Andrebbero rese globali per tutte le classi??
	private static final String[] importanze = { "Greatly", "Normally", "Not at all"}; //FIXME Andrebbero rese globali per tutte le classi?? E ne mancano 2 che non ricordo

	private static JTextField textField_linktext;
	private static JTextField textField_url;
	//TODO le due stringhe andrebbero esportate da qualche altra parte
	
	private JRootPane root;
	private static TreePanel albero;

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
		setBounds(100, 100, 728, 502);
		setResizable(false);
		thisWindow = this;

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String newPath = fileChooser(LOADSAVE);
				
				
				if (newPath.length()>0)
					//TODO aprire un JDialog per chiedere di salvare se il vecchio proj e' stato modificato
					currentProject = newPath;
					//TODO creare nuovo JTree
				}
		});
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmNew);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String newPath = fileChooser(LOADSAVE);

				
				if (newPath.length()>0)
					//TODO aprire un JDialog per chiedere di salvare se il vecchio proj e' stato modificato
					//TODO controllare che il nuovo file esista, e sia corretto
					currentProject = newPath;
				}
		});
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//TODO controllare che non ci sia qualcosa da salvare prima di chiudere
				dispose();
				 System.exit(0);
			}
		});
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
		
		mntmOptions.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {

				try {
					setEnabled(false);
					//TIP qua probabilmente c'e' la sol http://castever.wordpress.com/2008/07/31/how-to-create-your-own-events-in-java/
					if (frameOptions== null)
						frameOptions = new Options();
					frameOptions.addWindowListener(new WindowAdapter(){
						@Override
						public void windowClosing(WindowEvent e) {
							frameOptions.dispose();
							setEnabled(true);
						}
						
						
					});

					frameOptions.setVisible(true);
					frameOptions.addEventListener(new MyEventClassListener(){

						@Override
						public void handleMyEventClassEvent(
								EventObject e) {
							setEnabled(true);

						}

						@Override
						public void handleMyEventClassEvent(MyEventClass e) {
							// TODO Auto-generated method stub
							
						}});
				} catch (Exception e) {
					e.printStackTrace();
				}
				//options.menu();
			}
		});
		mnOptions.add(mntmOptions);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(5, 0, 710, 37);
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		contentPane.add(panel);
		panel.setLayout(null);

		JButton button = new JButton("");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				setFocus(txt);
			}
		});
		button.setBounds(12, 4, 30, 30);
		panel.add(button);
		button.setToolTipText("Open");
		button.setIcon(new ImageIcon(
				MainWindow.class
						.getResource("/com/sun/java/swing/plaf/motif/icons/TreeOpen.gif")));

		JButton button_3 = new JButton("");
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setFocus(img);
			}
		});
		button_3.setToolTipText("Open");
		button_3.setBounds(45, 4, 30, 30);
		panel.add(button_3);

		JButton button_4 = new JButton("");
		button_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setFocus(lnk);
			}
		});
		button_4.setToolTipText("Open");
		button_4.setBounds(78, 4, 30, 30);
		panel.add(button_4);

		JButton button_1 = new JButton("");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				setFocus(alt);
			}
		});
		button_1.setToolTipText("Open");
		button_1.setBounds(120, 4, 30, 30);
		panel.add(button_1);

		JButton button_2 = new JButton("");
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setFocus(cmp);
			}
		});
		button_2.setToolTipText("Open");
		button_2.setBounds(153, 4, 30, 30);
		panel.add(button_2);

		JButton button_5 = new JButton("");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setEnabled(false);
				//TIP qua probabilmente c'e' la sol http://castever.wordpress.com/2008/07/31/how-to-create-your-own-events-in-java/
				if (myWizard== null)
					myWizard = new Wizard();
				myWizard.setVisible(true);
				myWizard.addEventListener(new MyEventClassListener(){

					@Override
					public void handleMyEventClassEvent(
							EventObject e) {
								setEnabled(true);
								myWizard=null;
						// TODO Auto-generated method stub
						
					}

					@Override
					public void handleMyEventClassEvent(MyEventClass e) {
						// TODO Auto-generated method stub
						
					}});
			}
		});
		button_5.setIcon(new ImageIcon("/home/enrico/Documenti/PSI/icons/list-add-md.png"));
		button_5.setToolTipText("Open");
		button_5.setBounds(195, 4, 30, 30);
		panel.add(button_5);

		JButton button_6 = new JButton("");
		button_6.setToolTipText("Open");
		button_6.setBounds(228, 4, 30, 30);
		panel.add(button_6);

		JButton addButton = new JButton("");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addNewWizard();
			}
		});
		addButton.setIcon(new ImageIcon(MainWindow.class.getResource("/webApplication/grafica/add_icon.gif")));
		addButton.setToolTipText("Open");
		addButton.setBounds(277, 4, 30, 30);
		panel.add(addButton);

		JButton button_8 = new JButton("");
		button_8.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/gtk/resources/gtk-cancel-4.png")));
		button_8.setToolTipText("Open");
		button_8.setBounds(310, 4, 30, 30);
		panel.add(button_8);

		JButton btnGenerateWebsite = new JButton("GENERATE WEBSITE");
		
		btnGenerateWebsite.setToolTipText("Open");
		btnGenerateWebsite.setBounds(365, 4, 187, 30);
		boldify(btnGenerateWebsite);
		panel.add(btnGenerateWebsite);

		JPanel properties = new JPanel();
		properties.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), " Properties ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));

		properties.setLayout(null);
		properties.setBounds(249, 49, 466, 392);
		contentPane.add(properties);

		JPanel id_panel = new JPanel();
		id_panel.setBounds(12, 18, 193, 125);
		properties.add(id_panel);
		id_panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207,
				229)), " ID ", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(51, 51, 51)));
		id_panel.setLayout(null);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 40, 51, 15);
		id_panel.add(lblName);

		textField_Name = new JTextField();
		textField_Name.setToolTipText("name");
		textField_Name.addFocusListener(new FocusAdapter() {
			// TODO mettere check che il nome non esista gia', serve anche
			// questo nel caso venga incollato del testo...
			@Override
			public void focusLost(FocusEvent arg0) {
				setFocusedName();
			}
		});
		textField_Name.addKeyListener(new KeyAdapter() {
			// TODO mettere check che il nome non sia gia' esistente
			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
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
		presentation_panel.setBorder(new TitledBorder(new LineBorder(new Color(
				184, 207, 229)), " Presentation ", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));
		presentation_panel.setBounds(217, 18, 237, 125);
		properties.add(presentation_panel);
		presentation_panel.setLayout(null);

		JLabel lblCategory = new JLabel("Category:");
		lblCategory.setBounds(12, 24, 81, 15);
		presentation_panel.add(lblCategory);

		JLabel lblEmphasize = new JLabel("Emphasize:");
		lblEmphasize.setBounds(12, 54, 81, 14);
		presentation_panel.add(lblEmphasize);

		comboBox_Importance = new JComboBox(importanze);
		comboBox_Importance.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setFocusedImpo();
			}
		});
		comboBox_Importance.setBounds(111, 49, 112, 24);
		presentation_panel.add(comboBox_Importance);

		textField_Category = new JTextField();
		textField_Category.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				setFocusedCategory();
			}
		});
		textField_Category.setColumns(10);
		textField_Category.setBounds(109, 22, 114, 19);
		presentation_panel.add(textField_Category);

		JLabel lblImportance = new JLabel("Importance:");
		lblImportance.setBounds(12, 86, 97, 15);
		presentation_panel.add(lblImportance);

		comboBox_Emphasize = new JComboBox(categorie);
		comboBox_Emphasize.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setFocusedEmph();
			}
		});
		comboBox_Emphasize.setBounds(111, 85, 112, 24);
		presentation_panel.add(comboBox_Emphasize);

		// TODO Gestire i diversi contenuti per i vari tipi di oggetto
		// (soprattutto i composite e alternative)
		// TODO mettere immagine priorità per alternative, up e down
		content_panel = new JPanel();
		content_panel.setBorder(new TitledBorder(new LineBorder(new Color(184,
				207, 229)), " Content ", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));
		content_panel.setBounds(12, 155, 442, 225);
		properties.add(content_panel);
		content_panel.setLayout(new CardLayout(0, 0));

		JPanel panel_image = new JPanel();
		content_panel.add(panel_image, "panel_image");
		panel_image.setLayout(null);

		JLabel label_2 = new JLabel("File path:");
		label_2.setBounds(22, 12, 91, 14);
		panel_image.add(label_2);
				
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
		textField_imagepath.setBounds(22, 42, 292, 22);
		panel_image.add(textField_imagepath);
		
		JButton button_browseImg = new JButton("Browse\r\n");
		button_browseImg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				fileChooser(IMAGE, textField_imagepath);
				
			}
		});
		button_browseImg.setBounds(331, 39, 89, 29);
		panel_image.add(button_browseImg);
		
		errorePath = new JPanel();
		errorePath.setToolTipText("The file doesn't exist or is not readable");
		errorePath.setBorder(new LineBorder(Color.RED));
		errorePath.setBounds(19, 38, 300, 30);
		panel_image.add(errorePath);
		
		panel_composite = new JPanel();
/*=======

		JButton button_17 = new JButton("Browse\r\n");
		button_17.setBounds(298, 25, 89, 29);
		panel_image.add(button_17);

		JPanel panel_composite = new JPanel();
>>>>>>> refs/remotes/org.eclipse.jgit.transport.RemoteConfig@1aa9a7bb/testing*/
		content_panel.add(panel_composite, "panel_composite");
		panel_composite.setLayout(null);

		JLabel label_1 = new JLabel("Elements:");
		label_1.setBounds(12, 0, 91, 13);
		panel_composite.add(label_1);

		list_composite = new JList();
		//TODO aggiungere un bottone o un menu contestuale per vedere i dettagli degli elementi
		
		list_composite.setBounds(12, 25, 408, 132);
		panel_composite.add(list_composite);
		
		//Aggiunta la scroll bar
		//scrollPane_composite = new JScrollPane(list_composite);
		
		button_deleteFromComp = new JButton("Delete");
		button_deleteFromComp.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO fare un controllo sul nome dell'oggetto
				removeElementFromComposite(list_composite.getSelectedIndices());
				
			}
		});
		button_deleteFromComp.setBounds(12, 162, 91, 27);
		panel_composite.add(button_deleteFromComp);
		
		button_addExistComp = new JButton("Add existing");
		button_addExistComp.setBounds(195, 162, 121, 27);
		panel_composite.add(button_addExistComp);
		
		button_addNewComp = new JButton("Add new");
		button_addNewComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setEnabled(false);
				AddNew nuovo = new AddNew();
				nuovo.addWindowListener(new WindowAdapter(){
					@Override
					public void windowClosing(WindowEvent e) {
						setEnabled(true);
					}
				});
				nuovo.addEventListener(new MyEventClassListener(){

					@Override
					public void handleMyEventClassEvent(
							MyEventClass e) {
								setEnabled(true);
								if(e != null){
									System.out.println("nome del componente "+e.getComponente().getNome());
									addElementToComposite(e.getComponente());
								}
								System.out.println("premuto add NUOVO");						
					}

					@Override
					public void handleMyEventClassEvent(EventObject e) {
						// TODO Auto-generated method stub
						
					}

					

					});

				nuovo.setVisible(true);
			}
		});
		button_addNewComp.setBounds(320, 162, 100, 27);
		panel_composite.add(button_addNewComp);
		

		JPanel panel_alternative = new JPanel();
		content_panel.add(panel_alternative, "panel_alternative");
		panel_alternative.setLayout(null);
		
		//TODO cambiare le icone terribili dei bottoni up e down

		JButton button_9 = new JButton("^");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		button_9.setToolTipText("Click here to increase the priority of selected element");
		button_9.setBounds(12, 0, 46, 53);
		panel_alternative.add(button_9);

		JList list_alternative = new JList();
		list_alternative.setBounds(65, 0, 355, 149);
		panel_alternative.add(list_alternative);

		JButton button_10 = new JButton("v");
		button_10
				.setToolTipText("Click here to decrease the priority of selected element");
		button_10.setBounds(12, 96, 46, 53);
		panel_alternative.add(button_10);

		JButton button_11 = new JButton("Delete");
		button_11.setBounds(65, 161, 90, 27);
		panel_alternative.add(button_11);

		JButton button_12 = new JButton("Add existing");
		button_12.setBounds(198, 161, 121, 27);
		panel_alternative.add(button_12);

		JButton button_addNewAlter = new JButton("Add new");
		button_addNewAlter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setEnabled(false);
				AddNew nuovo = new AddNew();
				
				nuovo.addWindowListener(new WindowAdapter(){
					@Override
					public void windowClosing(WindowEvent e) {
						setEnabled(true);
					}
				});
				
				nuovo.addEventListener(new MyEventClassListener(){

					
					@Override
					public void handleMyEventClassEvent(
							MyEventClass e) {
								setEnabled(true);
								if(e != null)
									System.out.println("nome del componente "+e.getComponente().getNome());
								System.out.println("premuto add NUOVO");						
					}

					@Override
					public void handleMyEventClassEvent(EventObject e) {
						// TODO Auto-generated method stub
						
					}});

				nuovo.setVisible(true);
			}
		});
		button_addNewAlter.setBounds(322, 161, 98, 27);
		panel_alternative.add(button_addNewAlter);

		JPanel panel_link = new JPanel();
		content_panel.add(panel_link, "panel_link");
		panel_link.setLayout(null);

		JLabel lbl_linktxt = new JLabel("Link text:");
		lbl_linktxt.setBounds(13, 14, 78, 15);
		panel_link.add(lbl_linktxt);

		textField_linktext = new JTextField();
		textField_linktext.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {

				updateLinkText();
			}
			public void removeUpdate(DocumentEvent e) {
				updateLinkText();
				// text was deleted
			}
			public void insertUpdate(DocumentEvent e) {
				updateLinkText();

				// text was inserted
			}
		});
		textField_linktext.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				updateLinkText();
			}
		});
		textField_linktext.setColumns(10);
		textField_linktext.setBounds(93, 12, 313, 19);
		panel_link.add(textField_linktext);

		JLabel lbl_url = new JLabel("URL:");
		lbl_url.setBounds(12, 47, 78, 15);
		panel_link.add(lbl_url);
		
		textField_url = new JTextField();
		textField_url.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				updateLinkUrl();
			}
		});
		textField_url.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {

				updateLinkUrl();
			}
			public void removeUpdate(DocumentEvent e) {
				updateLinkUrl();
				// text was deleted
			}
			public void insertUpdate(DocumentEvent e) {
				updateLinkUrl();

				// text was inserted
			}
		});
		textField_url.setColumns(10);
		textField_url.setBounds(93, 45, 313, 19);
		panel_link.add(textField_url);
		
		erroreTestoLink = new JPanel();
		erroreTestoLink.setBorder(new LineBorder(Color.RED));
		erroreTestoLink.setToolTipText("");
		erroreTestoLink.setBounds(90, 9, 319, 24);
		panel_link.add(erroreTestoLink);
		
		erroreUrl = new JPanel();
		erroreUrl.setBorder(new LineBorder(Color.RED));
		erroreUrl.setToolTipText("");
		erroreUrl.setBounds(90, 42, 319, 24);
		panel_link.add(erroreUrl);
		

		JPanel panel_text = new JPanel();
		content_panel.add(panel_text, "panel_text");
		panel_text.setLayout(null);

		JLabel label_namecontent = new JLabel("Name:");
		label_namecontent.setBounds(12, 5, 45, 15);
		panel_text.add(label_namecontent);
		
		editorPane_text = new JTextArea();
		JScrollPane scrollingArea = new JScrollPane(editorPane_text);
		scrollingArea.setBounds(12, 32, 408, 156);
		editorPane_text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateTextContent();
			}
		});
		//editorPane_text.setBounds(12, 32, 408, 156);
		panel_text.add(scrollingArea);

		albero = new TreePanel();
		albero.setBounds(15, 63, 222, 378);
		contentPane.add(albero);
		albero.setLayout(new BoxLayout(albero, BoxLayout.X_AXIS));
		
		

		// TODO rimuovere invocazione a testing concluso
		popolaOggetti();

//>>>>>>> refs/remotes/org.eclipse.jgit.transport.RemoteConfig@1aa9a7bb/testing
	}

	private void addNewWizard() {
		//TODO agganciarci il wizard e non l'addnew
		//TODO andrebbe creata una classe e tolto il codice da qui

		setEnabled(false);
		AddNew nuovo = new AddNew();
		nuovo.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				setEnabled(true);
			}
		});
		nuovo.addEventListener(new MyEventClassListener(){

			@Override
			public void handleMyEventClassEvent(
					MyEventClass e) {
						setEnabled(true);
						if(e != null){
							addElementToTree(e.getComponente());
							
						}
			}

			@Override
			public void handleMyEventClassEvent(EventObject e) {
				// TODO Auto-generated method stub
				
			}});

		nuovo.setVisible(true);
	}

	protected void addElementToTree(ComponenteSemplice componente) {
		// TODO Auto-generated method stub
		if(focused == null || focused.getType()==Testo.TEXTTYPE || focused.getType() == Link.LINKTYPE || focused.getType()== Immagine.IMAGETYPE)
			albero.addNode(null, componente);
	}

	private static void setGenerici(Componente selected, String type) {
		textField_Name.setText(selected.getNome());

		textField_Category.setText(selected.getCategoria());

		textField_Type.setText(type);

		// TODO verificare che l'ordine sia giusto (che il numero restituito dal
		// getenfasi corrisp a quello del menu a tendina)
		comboBox_Emphasize.setSelectedIndex(selected.getEnfasi());
		comboBox_Importance.setSelectedIndex(selected.getVisibilita());

	}

	private static void popolaProperties(Testo selected) {
		setGenerici(selected, "Text");
		editorPane_text.setText(selected.getTesto());

		setContentLayout("panel_text");
	}

	private static void popolaProperties(Immagine selected) {
		setGenerici(selected, "Image");
		textField_imagepath.setText(selected.getPath());

		setContentLayout("panel_image");

	}
	
	private static void popolaProperties(Link selected){
		setGenerici(selected,"Link");
		textField_url.setText(selected.getUri());

		textField_linktext.setText(selected.getTesto());

		setContentLayout("panel_link");
	}

	private static void popolaProperties(ComponenteAlternative selected) {
		setGenerici(selected, "Alternative");

		setContentLayout("panel_alternative");
	}
	
	private static void popolaProperties(ComponenteComposto selected){
		//FIXME questo metodo fa schifo
		//TODO il list_composite non ha le scrollbar
		//TODO aggiungere le iconcine in parte ai nomi
		//TODO disabilitare l'add existing quando non esistono elementi da aggiungere
		
		if(list_composite != null && panel_composite!=null)
			panel_composite.remove(list_composite);

		setGenerici(selected,"Composite");
		

		setContentLayout("panel_composite");
		
		Vector<ComponenteSemplice> componenti = selected.getComponenti();
		
		String[] nomiComponenti= new String[componenti.size()];
		
		//TODO mettere icone per il tipo degli oggetti

		int i;
		for (i = 0; i < componenti.size(); i++) {
			nomiComponenti[i] = componenti.get(i).getNome();
		}
		
		list_composite = new JList(nomiComponenti);
		list_composite.setBounds(12, 25, 408, 132);
		panel_composite.add(list_composite);
	
		buttonDeleteMgmt();

		list_composite.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(list_composite.getSelectedIndex()>-1) //esiste almeno un elemento
					buttonDeleteMgmt();
			}
		});
		
		panel_composite.repaint();
		
		
		
	}
	
	private static void setContentLayout(String panel){
		CardLayout cl = (CardLayout)(content_panel.getLayout());
        cl.show(content_panel, panel);
	}
	
	private void removeElementFromComposite(int[] daRimuovere){
		int i;
		//ciclo for al contrario: rimuovere gli elementi dall'ultimo a scendere altrimenti va in crash
		for(i=daRimuovere.length-1; i>=0; i--){
			((ComponenteComposto)focused).cancellaComponenteS(daRimuovere[i]);
		}
		popolaProperties(focusedCmp);
		//FIXME sarebbe meglio fare anche un controllo sul nome e non solo sul numero di indice
		//TODO tenere traccia della rimorzione
	}
	


	 //metodo per popolare oggetti per farci prove
	 private void popolaOggetti()	{ img= new Immagine("immagineeee", "immagini!",
	 1,0,"/questo/e/un/path"); img.setNome("immagineeee");
	 img.setCategoria("immagini!"); img.setPath("/questo/e/un/path");
	 img.setVisibilita(2); img.setEnfasi(0);
	 lnk= new Link("", "!", 0,0,"", ""); lnk.setNome("linkozzo");
	 lnk.setCategoria("altra"); lnk.setVisibilita(1);
	 lnk.setUri("www.url.it"); lnk.setTesto("clicca qui"); lnk.setEnfasi(2);
	 txt= new Testo("", "", 0, 0, ""); txt.setNome("testo");
	 txt.setCategoria("testi!"); txt.setVisibilita(0); txt.setEnfasi(1);
	 txt.setTesto("scriviamoci tanta roba");
	 alt= new ComponenteAlternative("alternativa", "alterniamoci", 2, 0);
	 cmp=new ComponenteComposto("Compostato", "compi", 1, 1);
	 cmp.aggiungiComponenteS(img); cmp.aggiungiComponenteS(lnk);
	 cmp.aggiungiComponenteS(txt);
	 }
	 

	// TODO agganciare i metodi setfocus al click nelle foglie sull'albero...

	// metodo per togliere il focus all'oggetto precedente
	private static void unFocus() {
		focused = null;
		focusedTxt = null;
		focusedImg = null;
		focusedLnk = null;
		focusedCmp = null;
		focusedAlt = null;
	}

	public static void setFocus(Immagine selected) {
		unFocus();
		focusedImg = selected;
		setFocusGeneric(selected);
		popolaProperties(selected);

	}

	public static void setFocus(Testo selected) {
		unFocus();
		focusedTxt = selected;
		setFocusGeneric(selected);
		popolaProperties(selected);

	}

	public static void setFocus(Link selected) {
		unFocus();
		focusedLnk = selected;
		setFocusGeneric(selected);
		popolaProperties(selected);

	}

	public static void setFocus(ComponenteComposto selected) {
		unFocus();
		focusedCmp = selected;
		setFocusGeneric(selected);
		popolaProperties(selected);
		// TODO implementare parte specifica

	}

	public static void setFocus(ComponenteAlternative selected) {
		unFocus();
		focusedAlt = selected;
		setFocusGeneric(selected);
		popolaProperties(selected);
		// TODO implementare parte specifica

	}

	private static void setFocusGeneric(Componente comp) {
		focused = comp;
	}

	// TODO verificare se va
	private void setFocusedName() {
		if (focused != null)
			focused.setNome(textField_Name.getText());
	}

	private void setFocusedCategory() {
		if (focused != null)
			focused.setCategoria(textField_Category.getText());
	}

	private void setFocusedEmph() {
		if (focused != null)
			focused.setEnfasi(comboBox_Emphasize.getSelectedIndex());//
	}

	private void setFocusedImpo() {
		if (focused != null)
			focused.setVisibilita(comboBox_Importance.getSelectedIndex());// focused.setVisibilita
	}

	private void updateTextContent() {
		if (focusedTxt != null)
			focusedTxt.setTesto(editorPane_text.getText());
		// TODO finire updatecontent per i vari tipi di oggetto

	}
	
	private boolean checkLinkText(){
		//TODO bisognerebbe controllare i caratteri da escapare
		if(textField_linktext.getText().trim().length()!=0){
			textField_linktext.setToolTipText("Text of the link"); //TODO tooltip inutile al 100%
			erroreTestoLink.setVisible(false);
			return true;
		}
		else {
			erroreTestoLink.setVisible(true); 
			textField_linktext.setToolTipText("Link text can't be empty");
		}
		return false;
	}
	
	private void updateLinkText(){
		if(focusedLnk!= null)

			focusedLnk.setTesto(textField_linktext.getText());
		checkLinkText();
			//TODO sarebbe bello sollevare un'eccezione ogni volta che questo if non si verifica (e anche per tutti gli altri metodi di update)
		}
	
	private void updateLinkUrl(){
		if(focusedLnk!= null)
			focusedLnk.setUri(textField_url.getText());
		checkLinkUrl();

	}
	
	private boolean checkLinkUrl(){
		if(isUrlCorrect(textField_url.getText())){
			erroreUrl.setVisible(false);
			textField_url.setToolTipText("URL of the link");
			return true;
		}
		else {
			erroreUrl.setVisible(true);
			textField_url.setToolTipText("The URL is not correct");
		}
		return false;
	}
	
	private boolean isUrlCorrect(String text) {
		//TODO serve una regex per controllare le url!
		Matcher urlMatcher = URL_PATTERN.matcher(text);
        if (!urlMatcher.matches()){
            return false;}
		return true;
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
			errorePath.setVisible(false);
			textField_imagepath.setToolTipText("Path of the image file");
			return true;
		}
		else {
			errorePath.setVisible(true);
			textField_imagepath.setToolTipText("The file doesn't exist or is not readable");
		}
		return false;
	}
	
	
	static void chooseFile(int chooserValue, JFileChooser fc, JTextField target){
		//TODO settare le cartelle di default
		if (chooserValue == JFileChooser.APPROVE_OPTION) {
            target.setText(fc.getSelectedFile().getAbsolutePath());
        } 
	}
	
	static String chooseFile(int chooserValue, JFileChooser fc){
		//TODO settare le cartelle di default
		String output = "";
		if (chooserValue == JFileChooser.APPROVE_OPTION) {
			output = fc.getSelectedFile().getAbsolutePath();
        } 
		return output;
	}
	
	public static boolean isPathCorrect(String path){
		//TODO fare prove con files e dir verificare che funzioni
		File daControllare = new File(path);
		if(daControllare.isFile() && daControllare.canRead())
			return true;
		return false;
	}
	
	private void boldify(JButton button) {
		Font newButtonFont = new Font(button.getFont().getName(), Font.ITALIC
				+ Font.BOLD, button.getFont().getSize() + 1);
		button.setFont(newButtonFont);
	}
	
	public static String fileChooser(int i){
		JFileChooser filec=buildFileChooser(i);
		if(filec==null)
			return "";
		if (filec.getSelectedFile()==null){
			return "";
		}
		return filec.getSelectedFile().getAbsolutePath();
	}
	
	public static void addElementToComposite(ComponenteSemplice componente) {
		
		int[] selected = list_composite.getSelectedIndices();
		((ComponenteComposto)focusedCmp).aggiungiComponenteS(componente);
		popolaProperties(focusedCmp);
		list_composite.setSelectedIndices(selected);
		buttonDeleteMgmt();
		
		
	}
	
	private static void buttonDeleteMgmt(){
		
		if (list_composite.getSelectedIndices().length > 0)
			button_deleteFromComp.setEnabled(true);
		else
			button_deleteFromComp.setEnabled(false);
		
	}
	
	private static JFileChooser buildFileChooser (int i){
		JFileChooser fileChooser=null;
		if(frameOptions != null){
			if(i== LOADSAVE && frameOptions.getDefDirLoadSave()!= null && frameOptions.getDefDirLoadSave().length()>0)
				fileChooser = new JFileChooser(frameOptions.getDefDirLoadSave()); 
			else if (i == TEXT && frameOptions.getDefDirText()!= null && frameOptions.getDefDirText().length()>0)
				fileChooser = new JFileChooser(frameOptions.getDefDirText()); 
			else if (i == IMAGE && frameOptions.getDefDirImage()!= null && frameOptions.getDefDirImage().length()>0)
				fileChooser = new JFileChooser(frameOptions.getDefDirImage()); 
			else {
				return null;
			}
		}
		else
			fileChooser = new JFileChooser();
		chooseFile(fileChooser.showOpenDialog(contentPane), fileChooser); 
		return fileChooser;

	}
	
	public static File getFileFromChooser(int i){
		return buildFileChooser(i).getSelectedFile();
	}
	
	private void fileChooser(int i, JTextField target){
		String path;
		path=fileChooser(i);
		if (path!= null && path.length()>0)
			target.setText(path);
	}

}
