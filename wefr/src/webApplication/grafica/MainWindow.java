package webApplication.grafica;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextComponent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import java.awt.CardLayout;
import javax.swing.JList;
import java.awt.event.KeyAdapter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTree;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.ComponenteSemplice;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.regex.Pattern;
import java.awt.event.TextListener;
import java.awt.event.TextEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


public class MainWindow extends JFrame {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_Name;
	private JTextField textField_Type;
	private JTextField textField_Category;
	private JComboBox comboBox_Importance;
	private JComboBox comboBox_Emphasize;
	private JEditorPane editorPane_text;
	private JTextField textField_imagepath;
	private JPanel content_panel;
	private JList list_composite;
	private JScrollPane scrollPane_composite;
	private JPanel panel_composite;
	private JButton button_deleteFromComp;
	private JButton button_addExistComp;
	private JButton button_addNewComp;
	private JPanel errorePath;
	private JPanel erroreTestoLink;
	private JPanel erroreUrl;


	//oggetti che per fare prove con l'interfaccia
	//TODO rimuovere questi oggetti dopo aver verificato che tutto funziona
	private Immagine img;
	private Link lnk;
	private Testo txt;
	private ComponenteAlternative alt;
	private ComponenteComposto cmp;
	
	private Componente focused;
	
	private Testo focusedTxt;
	private Immagine focusedImg;
	private Link focusedLnk;
	private ComponenteComposto focusedCmp;
	private ComponenteAlternative focusedAlt;

	
	
	private static final String[] categorie = { "Necessary", "Indifferent", "Expendable"}; //FIXME Andrebbero rese globali per tutte le classi??
	private static final String[] importanze = { "Greatly", "Normally", "Not at all"}; //FIXME Andrebbero rese globali per tutte le classi?? E ne mancano 2 che non ricordo
	private JTextField textField_linktext;
	private JTextField textField_url;
	//TODO le due stringhe andrebbero esportate da qualche altra parte
	
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
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
		button.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/motif/icons/TreeOpen.gif")));
		
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
		btnGenerateWebsite.setBounds(365, 4, 187, 30);
		boldify(btnGenerateWebsite);
		panel.add(btnGenerateWebsite);
		
		JPanel properties = new JPanel();
		properties.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), " Properties ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		properties.setLayout(null);
		properties.setBounds(249, 49, 466, 392);
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
		textField_Name.setToolTipText("name");
		textField_Name.addFocusListener(new FocusAdapter() {
			//TODO mettere check che il nome non esista gia', serve anche questo nel caso venga incollato del testo...
			@Override
			public void focusLost(FocusEvent arg0) {
				setFocusedName();
			}
		});
		textField_Name.addKeyListener(new KeyAdapter() {
			//TODO mettere check che il nome non sia gia' esistente
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
		
		//TODO Gestire i diversi contenuti per i vari tipi di oggetto (soprattutto i composite e alternative)
		//TODO mettere immagine priorità per alternative, up e down
		content_panel = new JPanel();
		content_panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), " Content ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
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
		/*addTextListener(new TextListener() {
			public void textValueChanged(TextEvent arg0) {
				checkImagePath();
			}
		});*/
		textField_imagepath.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				updateImagePath();
			}

			
		});
		textField_imagepath.setBounds(22, 42, 292, 22);
		panel_image.add(textField_imagepath);
		
		JButton button_17 = new JButton("Browse\r\n");
		button_17.setBounds(331, 39, 89, 29);
		panel_image.add(button_17);
		
		errorePath = new JPanel();
		errorePath.setToolTipText("The file doesn't exist or is not readable");
		errorePath.setBorder(new LineBorder(Color.RED));
		errorePath.setBounds(19, 38, 300, 30);
		panel_image.add(errorePath);
		
		panel_composite = new JPanel();
		content_panel.add(panel_composite, "panel_composite");
		panel_composite.setLayout(null);
		
		JLabel label_1 = new JLabel("Elements:");
		label_1.setBounds(12, 0, 91, 13);
		panel_composite.add(label_1);
		
		list_composite = new JList();
		
		list_composite.setBounds(12, 25, 408, 132);
		panel_composite.add(list_composite);
		
		//Aggiunta la scroll bar
		//scrollPane_composite = new JScrollPane(list_composite);
		
		button_deleteFromComp = new JButton("Delete");
		button_deleteFromComp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				removeElementFromComposite(list_composite.getSelectedIndices());
			}
		});
		button_deleteFromComp.setBounds(12, 162, 91, 27);
		panel_composite.add(button_deleteFromComp);
		
		button_addExistComp = new JButton("Add existing");
		button_addExistComp.setBounds(195, 162, 121, 27);
		panel_composite.add(button_addExistComp);
		
		button_addNewComp = new JButton("Add new");
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
		button_10.setToolTipText("Click here to decrease the priority of selected element");
		button_10.setBounds(12, 96, 46, 53);
		panel_alternative.add(button_10);
		
		JButton button_11 = new JButton("Delete");
		button_11.setBounds(65, 161, 90, 27);
		panel_alternative.add(button_11);
		
		JButton button_12 = new JButton("Add existing");
		button_12.setBounds(198, 161, 121, 27);
		panel_alternative.add(button_12);
		
		JButton button_13 = new JButton("Add new");
		button_13.setBounds(322, 161, 98, 27);
		panel_alternative.add(button_13);
		
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
		
		editorPane_text = new JEditorPane();
		editorPane_text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateTextContent();
			}
		});
		editorPane_text.setBounds(12, 32, 408, 156);
		panel_text.add(editorPane_text);
		
		JTree tree = new JTree();
		tree.setBounds(15, 63, 222, 378);
		contentPane.add(tree);
		
		//TODO rimuovere invocazione a testing concluso
		popolaOggetti();
		
	}
	
	
	private void setGenerici(Componente selected, String type){
		textField_Name.setText(selected.getNome());
		
		textField_Category.setText(selected.getCategoria());
		
		textField_Type.setText(type);

		
		//TODO verificare che l'ordine sia giusto (che il numero restituito dal getenfasi corrisp a quello del menu a tendina)
		comboBox_Emphasize.setSelectedIndex(selected.getEnfasi());
		comboBox_Importance.setSelectedIndex(selected.getVisibilita());
		
	}
	
	private void popolaProperties(Testo selected){
		setGenerici(selected, "Text");
		editorPane_text.setText(selected.getTesto());
		
		setContentLayout("panel_text");
        }
	
	private void popolaProperties(Immagine selected){
		setGenerici(selected,"Image");
		textField_imagepath.setText(selected.getPath());
		
		setContentLayout("panel_image");

	}
	
	private void popolaProperties(Link selected){
		setGenerici(selected,"Link");
		textField_url.setText(selected.getUri());
		textField_linktext.setText(selected.getTesto());
		
		setContentLayout("panel_link");
	}
	
	private void popolaProperties(ComponenteAlternative selected){
		setGenerici(selected, "Alternative");
		
		setContentLayout("panel_alternative");
	}
	
	private void popolaProperties(ComponenteComposto selected){
		//FIXME questo metodo fa schifo
		//TODO verificare se il list_composite ha le scrollbar (non credo)
		//TODO aggiungere le iconcine in parte ai nomi
		//TODO disabilitare l'add existing quando non esistono elementi da aggiungere
		
		if(list_composite != null && panel_composite!=null)
			panel_composite.remove(list_composite);

		setGenerici(selected,"Composite");
		
		setContentLayout("panel_composite");
		
		Vector<ComponenteSemplice> componenti = selected.getComponenti();
		
		String[] nomiComponenti= new String[componenti.size()];
		int i;
		for(i=0; i < componenti.size();i++){
			nomiComponenti[i]=componenti.get(i).getNome();
		}
		
		list_composite = new JList(nomiComponenti);
		list_composite.setBounds(12, 25, 408, 132);
		panel_composite.add(list_composite);
	
		button_deleteFromComp.setEnabled(false);

		list_composite.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(list_composite.getSelectedIndex()>-1) //esiste almeno un elemento
					button_deleteFromComp.setEnabled(true);
			}
		});
		
		panel_composite.repaint();
		
		
		
	}
	
	private void setContentLayout(String panel){
		CardLayout cl = (CardLayout)(content_panel.getLayout());
        cl.show(content_panel, panel);
	}
	
	private void removeElementFromComposite(int[] daRimuovere){
		int i;
		for(i=0;i< daRimuovere.length; i++)
			focusedCmp.cancellaComponenteS(daRimuovere[i]);
		popolaProperties(focusedCmp);
		//FIXME sarebbe meglio fare anche un controllo sul nome e non solo sul numero di indice
		//TODO tenere traccia della rimorzione
	}
	
	//metodo per popolare oggetti per farci prove
	private void popolaOggetti(){
		img= new Immagine("immagineeee", "immagini!", 1,0,"/questo/e/un/path");
		img.setNome("immagineeee");
		img.setCategoria("immagini!");
		img.setPath("/questo/e/un/path");
		img.setVisibilita(2);
		img.setEnfasi(0);
		
		lnk= new Link("", "!", 0,0,"", "");
		lnk.setNome("linkozzo");
		lnk.setCategoria("altra");
		lnk.setVisibilita(1);
		lnk.setUri("www.url.it");
		lnk.setTesto("clicca qui");
		lnk.setEnfasi(2);
		
		txt= new Testo("", "", 0, 0, "");
		txt.setNome("testo");
		txt.setCategoria("testi!");
		txt.setVisibilita(0);
		txt.setEnfasi(1);
		txt.setTesto("scriviamoci tanta roba");
		
		alt= new ComponenteAlternative("alternativa", "alterniamoci", 2, 0);
		cmp= new ComponenteComposto("Compostato", "compi", 1, 1);
		cmp.aggiungiComponenteS(img);
		cmp.aggiungiComponenteS(lnk);
		cmp.aggiungiComponenteS(txt);
		

	}
	
	//TODO agganciare i metodi setfocus al click nelle foglie sull'albero...

	//metodo per togliere il focus all'oggetto precedente
	private void unFocus(){
		focused = null; 
		focusedTxt = null;
		focusedImg = null;
		focusedLnk = null;
		focusedCmp = null;
		focusedAlt = null;
	}
	
	private void setFocus(Immagine selected){
		unFocus();
		focusedImg = selected;
		setFocusGeneric(selected);
		popolaProperties(selected);

	}
	
	private void setFocus(Testo selected){
		unFocus();
		focusedTxt=selected;
		setFocusGeneric(selected);
		popolaProperties(selected);

	}
	private void setFocus(Link selected){
		unFocus();
		focusedLnk=selected;
		setFocusGeneric(selected);
		popolaProperties(selected);

	}
	private void setFocus(ComponenteComposto selected){
		unFocus();
		focusedCmp=selected;
		setFocusGeneric(selected);
		popolaProperties(selected);
		//TODO implementare parte specifica

	}
	private void setFocus(ComponenteAlternative selected){
		unFocus();
		focusedAlt=selected;
		setFocusGeneric(selected);
		popolaProperties(selected);
		//TODO implementare parte specifica

	}
	
	
	private void setFocusGeneric(Componente comp){
		focused = comp;
	}
	
	//TODO verificare se va
	private void setFocusedName(){
		if(focused!= null)
			focused.setNome(textField_Name.getText());
	}
	
	private void setFocusedCategory(){
		if(focused!= null)
			focused.setCategoria(textField_Category.getText());
	}
	
	private void setFocusedEmph(){
		if(focused!= null)
			focused.setEnfasi(comboBox_Emphasize.getSelectedIndex());//
	}
	
	private void setFocusedImpo(){
		if(focused!= null)
			focused.setVisibilita(comboBox_Importance.getSelectedIndex());//focused.setVisibilita
	}
	
	private void updateTextContent(){
		if(focusedTxt!= null)
			focusedTxt.setTesto(editorPane_text.getText());
		//TODO finire updatecontent per i vari tipi di oggetto
			
	}
	
	private boolean checkLinkText(){
		//TODO bisognerebbe controllare i caratteri da escapare
		if(textField_linktext.getText().length() != 0){
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
	
	private boolean isPathCorrect(String path){
		//TODO fare prove con files e dir verificare che funzioni
		File daControllare = new File(path);
		if(daControllare.isFile() && daControllare.canRead())
			return true;
		return false;
	}
	
	private void boldify(JButton button){
		Font newButtonFont=new Font(button.getFont().getName(),Font.ITALIC+Font.BOLD,button.getFont().getSize()+1);
		button.setFont(newButtonFont);
	}
}
