//TODO ispathcorrect manca nel main
package webApplication.grafica;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import webApplication.business.ComponenteSemplice;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

import java.awt.event.ActionEvent;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNew extends AddComponent implements DocumentListener,
KeyListener,
ActionListener {

	private static final Color COLOR_PANEL_DISABLED = new Color(204, 204, 204);

	private static final Color COLOR_PANEL_ACTIVE = new Color(184, 207, 229);

	/**
	 * 
	 */
	private static final long serialVersionUID = 3236559861349447627L;

	private static final String NAME = "Name:";
	private static final String NAME_EMPTY = "It's mandatory to fill the name field";
	private static final String NAME_EXISTING = "Another element with the same name alredy existing";

	private static final String NAMETOOLTIP = "Name of the new element";
	private static final String CATEGORY = "Category:";
	private static final String CATE_EMPTY = "It's mandatory to fill the category field";


	private static final String DEFAULTCATEGORYVALUE = "Category0";

	private static final String LINKPANELTITLE = " Link ";
	private JLabel lblUrl;
	private static final String LINK_PATH = "URL:";
	private static final String DEFAULTLINKPATH = "http://";
	private static final String LINK_PATH_TOOLTIP = "Enter the desired link";
	private static final String URL = "URL of the link";
	private static final String URL_EMPTY = "This field must be a valid url (ex http://www.myweb.com)";

	private JLabel lblLink;
	private static final String LINK_TEXT = "Link text:";
	private static final String LINK_TEXT_TOOLTIP = "Enter the text for the link";

	private static final String TEXTPANELTITLE = " Text ";
	private static final String TEXT_EMPTY = "It's mandatory to fill the text field";
	private JButton importTextButton;
	private static final String LOAD_TEXT_BTN = "Import from file";
	private static final String LOAD_TEXT_TOOLTIP = "Click to load text from an existing file";

	private static final String IMAGEPANELTITLE = " Image ";
	private static final String IMAGE_EMPTY = "It's mandatory to enter a valid path";
	private JLabel lblPath;
	private static final String IMAGE_PATH = "File path:";

	private JButton importImageButton;
	private static final String LOAD_IMAGE_BTN = "Browse";
	private static final String LOAD_IMAGE_TOOLTIP = "Click to load image from a file";

	private JPanel panel_link;
	private JPanel panel_text;
	private JPanel panel_image;

	private JTextArea textArea;
	private JScrollPane scrollingArea;

	private JRadioButton rdbtnLink;
	private static final String LINK = "Link";
	private static final String RBTN_LINK_TOOLTIP = "Select to enable fields of Link element";
	private static final String RBTN_LINK_ACTION = "Link";
	private JRadioButton rdbtnText;
	private static final String TEXT = "Text";
	private static final String RBTN_TEXT_TOOLTIP = "Select to enable fields of Text element";
	private static final String RBTN_TEXT_ACTION = "Text";
	private JRadioButton rdbtnImage;
	private static final String IMAGE = "Image";
	private static final String RBTN_IMAGE_TOOLTIP = "Select to enable fields of Image element";
	private static final String RBTN_IMAGE_ACTION = "Image";

	private JTextField textField_category;
	private JTextField textField_name;
	private JTextField textField_linkText;
	private JTextField textField_url;
	private JTextField textField_imagePath;

	private static final String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private Pattern valid_url = Pattern.compile(regex);
	private static final String allowedUrlChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVXYWZZ0123456789+&@#/%?=~_|!:,.;";

	/**
	 * Create the frame.
	 */
	public AddNew(Window owner, String title) {
		super(owner, title);

		buildGenericFields();

		buildPanelText();

		buildPanelImage();

		buildPanelLink();

		buildRadioButtons();

		addDocumentListeners();

		// enabler(panel_text);
		textField_name.setText(MainWindow.setDefaultName());

		/*
		 * //redify(textField_name,Utils.isBlank(textField_name));
		 * redify(textField_category,Utils.isBlank(textField_category));
		 * updateAddBtn();
		 * 
		 * MultiLineToolTip tip = new MultiLineToolTip();
		 * tip.setComponent(panel_link);
		 * panel_link.setToolTipText("Hello\nworld"); setVisible(true);
		 */
	}

	private void buildGenericFields() {
		JLabel name = new JLabel(NAME);
		name.setBounds(119, 13, 51, 15);
		basePane.add(name);

		textField_name = new JTextField(setDefaultName());
		textField_name.setToolTipText(NAMETOOLTIP);
		textField_name.setColumns(10);
		textField_name.setBounds(229, 11, 114, 19);
		basePane.add(textField_name);

		JLabel category = new JLabel(CATEGORY);
		category.setBounds(119, 38, 81, 15);
		basePane.add(category);

		textField_category = new JTextField(DEFAULTCATEGORYVALUE);
		textField_category.setColumns(10);
		textField_category.setBounds(229, 36, 114, 19);
		basePane.add(textField_category);
	}

	private void buildPanelText() {
		panel_text = new JPanel();
		panel_text.setBorder(new TitledBorder(new LineBorder(COLOR_PANEL_DISABLED, 1, true), TEXTPANELTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_text.setLayout(null);
		panel_text.setBounds(92, 72, 426, 183);
		basePane.add(panel_text);

		importTextButton = new JButton(LOAD_TEXT_BTN);
		importTextButton.setToolTipText(LOAD_TEXT_TOOLTIP);
		importTextButton.addActionListener(this);
		importTextButton.setActionCommand(LOAD_TEXT_BTN);
		importTextButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
		importTextButton.setBounds(12, 152, 142, 19);
		panel_text.add(importTextButton);

		textArea = new JTextArea();
		scrollingArea = new JScrollPane(textArea);
		scrollingArea.setBorder(new LineBorder(COLOR_PANEL_DISABLED, 1, true));
		scrollingArea.setBounds(12, 20, 402, 120);
		panel_text.add(scrollingArea);
		panelTextEnabled(true);
	}

	private void buildPanelImage() {
		panel_image = new JPanel();
		panel_image.setBorder(new TitledBorder(new LineBorder(COLOR_PANEL_DISABLED, 1, true), IMAGEPANELTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_image.setLayout(null);
		panel_image.setBounds(92, 267, 426, 88);
		basePane.add(panel_image);

		lblPath = new JLabel(IMAGE_PATH);
		lblPath.setBounds(12, 24, 81, 15);
		panel_image.add(lblPath);

		textField_imagePath = new JTextField();
		textField_imagePath.setColumns(10);
		textField_imagePath.setBounds(12, 51, 301, 19);
		panel_image.add(textField_imagePath);

		importImageButton = new JButton(LOAD_IMAGE_BTN);
		importImageButton.setToolTipText(LOAD_IMAGE_TOOLTIP);
		importImageButton.addActionListener(this);
		importImageButton.setActionCommand(LOAD_IMAGE_BTN);
		importImageButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
		importImageButton.setBounds(325, 51, 89, 19);
		panel_image.add(importImageButton);
		panelImageEnabled(false);
	}

	private void buildPanelLink() {
		panel_link = new JPanel();
		panel_link.setBorder(new TitledBorder(new LineBorder(COLOR_PANEL_DISABLED, 1, true), LINKPANELTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_link.setLayout(null);
		panel_link.setBounds(92, 367, 426, 84);
		basePane.add(panel_link);

		lblUrl = new JLabel(LINK_PATH);
		lblUrl.setBounds(24, 23, 51, 15);
		panel_link.add(lblUrl);

		textField_url = new JTextField(DEFAULTLINKPATH);
		textField_url.setToolTipText(LINK_PATH_TOOLTIP);
		textField_url.setColumns(10);
		textField_url.setBounds(111, 21, 303, 19);
		panel_link.add(textField_url);

		lblLink = new JLabel(LINK_TEXT);
		lblLink.setBounds(24, 48, 81, 15);
		panel_link.add(lblLink);

		textField_linkText = new JTextField();
		textField_linkText.setToolTipText(LINK_TEXT_TOOLTIP);
		textField_linkText.setColumns(10);
		textField_linkText.setBounds(111, 46, 303, 19);
		panel_link.add(textField_linkText);
		panelLinkEnabled(false);
	}

	private void buildRadioButtons() {
		rdbtnLink = new JRadioButton(LINK);
		rdbtnLink.setToolTipText(RBTN_LINK_TOOLTIP);
		rdbtnLink.setActionCommand(RBTN_LINK_ACTION);
		rdbtnLink.addActionListener(this);
		rdbtnLink.setBounds(8,
				(panel_link.getHeight() - 23) / 2 + panel_link.getY(), 68, 23);
		basePane.add(rdbtnLink);

		rdbtnText = new JRadioButton(TEXT);
		rdbtnText.setSelected(true);
		rdbtnText.setToolTipText(RBTN_TEXT_TOOLTIP);
		rdbtnText.setActionCommand(RBTN_TEXT_ACTION);
		rdbtnText.addActionListener(this);
		rdbtnText.setBounds(8,
				(panel_text.getHeight() - 23) / 2 + panel_text.getY(), 68, 23);
		basePane.add(rdbtnText);

		rdbtnImage = new JRadioButton(IMAGE);
		rdbtnImage.setToolTipText(RBTN_IMAGE_TOOLTIP);
		rdbtnImage.setActionCommand(RBTN_IMAGE_ACTION);
		rdbtnImage.addActionListener(this);
		rdbtnImage.setBounds(8, (panel_image.getHeight() - 23) / 2 + panel_image.getY(), 68, 23);
		basePane.add(rdbtnImage);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnImage);
		bg.add(rdbtnText);
		bg.add(rdbtnLink);
	}

	protected ComponenteSemplice getComponente() {
		if (rdbtnImage.isSelected()) {
			return new Immagine(textField_name.getText(), textField_category.getText(), DEFAULTEMPIMP, DEFAULTEMPIMP, textField_imagePath.getText());
		} else if (rdbtnLink.isSelected()) {
			return new Link(textField_name.getText(), textField_category.getText(), DEFAULTEMPIMP, DEFAULTEMPIMP, textField_url.getText(), textField_linkText.getText());
		} else {
			return new Testo(textField_name.getText(), textField_category.getText(), DEFAULTEMPIMP, DEFAULTEMPIMP, textArea.getText());
		}
	}

	private void panelLinkEnabled(boolean state) {
		panel_link.setEnabled(state);
		if (state) {
			panel_link.setBorder(new TitledBorder(new LineBorder(COLOR_PANEL_ACTIVE, 1, true), LINKPANELTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			redify(textField_url, Utils.isBlank(textField_url)|| errorUrl());
			redify(textField_linkText, Utils.isBlank(textField_linkText));
		} else{
			panel_link.setBorder(new TitledBorder(new LineBorder(COLOR_PANEL_DISABLED, 1, true), LINKPANELTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
			redify(textField_url, state);
			redify(textField_linkText, state);
		}
		lblUrl.setEnabled(state);
		textField_url.setEnabled(state);
		lblLink.setEnabled(state);
		textField_linkText.setEnabled(state);
		basePane.revalidate();
		basePane.repaint();
	}

	private void panelTextEnabled(boolean state) {
		panel_text.setEnabled(state);
		if (state) {
			panel_text.setBorder(new TitledBorder(new LineBorder(COLOR_PANEL_ACTIVE, 1, true), TEXTPANELTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			redify(textArea, Utils.isBlank(textArea));
		} else{
			panel_text.setBorder(new TitledBorder(new LineBorder(COLOR_PANEL_DISABLED, 1, true), TEXTPANELTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
			redify(textArea, state);
		}
		importTextButton.setEnabled(state);
		textArea.setEnabled(state);
		basePane.revalidate();
		basePane.repaint();
	}

	private void panelImageEnabled(boolean state) {
		panel_image.setEnabled(state);
		if (state) {
			panel_image.setBorder(new TitledBorder(new LineBorder(COLOR_PANEL_ACTIVE, 1, true), IMAGEPANELTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			redify(textField_imagePath,Utils.isBlank(textField_imagePath) || fileError());
		} else{
			panel_image.setBorder(new TitledBorder(new LineBorder(COLOR_PANEL_DISABLED, 1, true), IMAGEPANELTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
			redify(textField_imagePath,state);

		}
		lblPath.setEnabled(state);
		textField_imagePath.setEnabled(state);
		importImageButton.setEnabled(state);
		basePane.revalidate();
		basePane.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getActionCommand().equals(RBTN_LINK_ACTION)) {
			panelLinkEnabled(true);
			panelTextEnabled(false);
			panelImageEnabled(false);
			updateAddBtn();
		} else if (e.getActionCommand().equals(RBTN_TEXT_ACTION)) {
			panelLinkEnabled(false);
			panelTextEnabled(true);
			panelImageEnabled(false);
			updateAddBtn();
		} else if (e.getActionCommand().equals(RBTN_IMAGE_ACTION)) {
			panelLinkEnabled(false);
			panelTextEnabled(false);
			panelImageEnabled(true);
			updateAddBtn();
		} else if (e.getActionCommand().equals(LOAD_IMAGE_BTN)) {
			JFileChooser fc = new JFileChooser(MainWindow.defImageDir);
			ImagePreview imgPrev = new ImagePreview();
			fc.setAccessory(imgPrev);
			fc.addPropertyChangeListener(imgPrev);
			fc.setFileFilter(new ImageFileFilter());
			fc.setAcceptAllFileFilterUsed(false);
			int choice = fc.showOpenDialog(this);
			if (choice == JFileChooser.APPROVE_OPTION) {
				textField_imagePath.setText(fc.getSelectedFile().getPath());
			}
		} else if (e.getActionCommand().equals(LOAD_TEXT_BTN)) {
			JFileChooser fc = new JFileChooser(MainWindow.defTextDir);
			int choice = fc.showOpenDialog(this);
			if (choice == JFileChooser.APPROVE_OPTION) {
				String letto = "";
				String eol = System.getProperty("line.separator");
				try {
					FileReader reader = new FileReader(fc.getSelectedFile().getPath());
					Scanner scanReader = new Scanner(reader);
					while (scanReader.hasNextLine()) {
						letto = letto + scanReader.nextLine() + eol;
					}
					textArea.setText(letto);
				} catch (FileNotFoundException e1) {
				}
			}
		}
	}

	// TODO spostare nel MainWindow
	protected String setDefaultName() {
		return "Element0";
	}

	private void addDocumentListeners() {
		textField_category.getDocument().addDocumentListener(this);
		textField_linkText.getDocument().addDocumentListener(this);
		textField_name.getDocument().addDocumentListener(this);
		textField_url.getDocument().addDocumentListener(this);
		textField_imagePath.getDocument().addDocumentListener(this);
		textArea.getDocument().addDocumentListener(this);
		textField_url.addKeyListener(this);
	}

	private void updateComponent(Component figlio, boolean enable){
		figlio.setEnabled(enable);
		if(figlio == scrollingArea){
			updateComponent(textArea,enable);
			//le tre righe qua sotto sono un workaround per un baco delle JScrollPane
			scrollingArea.getHorizontalScrollBar().setEnabled(enable);
			scrollingArea.getVerticalScrollBar().setEnabled(enable);
			scrollingArea.getViewport().getView().setEnabled(enable);
			manageTooltips(textArea, Utils.isBlank(textArea));
		}
		if (figlio instanceof javax.swing.JTextField || figlio instanceof javax.swing.JTextArea){
			if(enable){
				if(figlio == textField_url)
					redify((JTextComponent) figlio,Utils.isBlank((JTextComponent) figlio)||errorUrl());
				else
					redify((JTextComponent) figlio,Utils.isBlank((JTextComponent) figlio));
				figlio.setBackground(new Color(255, 255, 255));
			}
			else{
				((JTextComponent)figlio).setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
				figlio.setBackground(new Color(245, 245, 245));

			}
		}
	}

	private void redify(JTextComponent toRed, boolean b){
		if(b){
			toRed.setBorder(new LineBorder(new Color(255, 0, 0), 1, true));//bordo rosso
		}
		else {
			toRed.setBorder(new LineBorder(COLOR_PANEL_ACTIVE, 1, true));//bordo normale
		}
		manageTooltips(toRed, b);

	}

	private void manageTooltips(Component component, boolean b) {
		if (component == textField_name){
			if(b)
				textField_name.setToolTipText(NAME_EMPTY);
			else if(nameExists())
				textField_name.setToolTipText(NAME_EXISTING);
			else
				textField_name.setToolTipText(NAME);
		}
		else if(component == textField_category){
			if(b)
				textField_category.setToolTipText(CATE_EMPTY);
			else
				textField_category.setToolTipText(CATEGORY);
		}
		else if(component == textField_url){
			if(b)
				textField_url.setToolTipText(URL_EMPTY);
			else
				textField_url.setToolTipText(URL);
		}
		else if(component == textField_linkText ){
			if(b)
				textField_linkText.setToolTipText(TEXT_EMPTY);
			else
				textField_linkText.setToolTipText(TEXT);
		}
		else if(component == textArea){
			if(b)
				textArea.setToolTipText(TEXT_EMPTY);
			else
				textArea.setToolTipText(TEXT);
		}
		else if(component == textField_imagePath){
			if(b)
				textField_imagePath.setToolTipText(IMAGE_EMPTY);
			else
				textField_imagePath.setToolTipText(TEXT);
		}
	}

	private void changeEvent(DocumentEvent e) {
		if(e.getDocument()== textField_url.getDocument())
			redify(fromDocToJComp(e.getDocument()),Utils.isBlank((fromDocToJComp(e.getDocument())))||errorUrl());
		else if(e.getDocument()==textField_imagePath.getDocument()){
			checkPath();
		}
		else if(e.getDocument()==textField_name.getDocument()){
			Utils.redify(textField_name,Utils.isBlank(textField_name)||nameExists());
			manageTooltips(textField_name, Utils.isBlank(textField_name));
		}
		else
			redify(fromDocToJComp(e.getDocument()),Utils.isBlank((fromDocToJComp(e.getDocument()))));
		updateAddBtn();
	}

	private JTextComponent fromDocToJComp(Document doc){
		if(doc== textField_category.getDocument())
			return textField_category;
		else if(doc== textField_imagePath.getDocument())
			return textField_imagePath;
		else if(doc== textField_linkText.getDocument())
			return textField_linkText;
		else if(doc== textField_name.getDocument())
			return textField_name;
		else if(doc== textField_url.getDocument())
			return textField_url;
		else if (doc== textArea.getDocument())
			return textArea;
		return null;
	}

	private boolean nameExists(){
		if (this.getOwner() instanceof Wizard)
			return(((Wizard) this.getOwner()).nameExistsAll(textField_name.getText()));
		else
			return MainWindow.albero.nameExists(textField_name.getText());
	}

	private boolean erroriPresenti(){
		boolean result;
		result = Utils.isBlank(textField_name) || nameExists();
		result= result || Utils.isBlank(textField_category);
		if (rdbtnImage.isSelected())
			result= result || Utils.isBlank(textField_imagePath) || fileError();
		else if (rdbtnLink.isSelected())
			result= result || Utils.isBlank(textField_linkText) || Utils.isBlank(textField_url) || errorUrl();
		else
			result= result || Utils.isBlank(textArea);
		return result;

	}

	private boolean errorUrl() {
		return !(IsMatch(textField_url.getText()));
	}

	private boolean fileError(){
		return !MainWindow.isPathCorrect(textField_imagePath.getText());
	}

	private void checkPath(){
		redify(textField_imagePath, !MainWindow.isPathCorrect(textField_imagePath.getText()));
	}


	private void updateAddBtn(){
		updateComponent(buttonAdd, !erroriPresenti());
	}

	private boolean IsMatch(String s) {
		try {
			Matcher matcher = valid_url.matcher(s);
			return matcher.matches();
		} catch (RuntimeException e) {
			return false;
		}
	}

	private boolean allowedChar(char c){
		if(allowedUrlChar.indexOf(c)==-1)
			return false;
		return true;
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		changeEvent(arg0);
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		changeEvent(arg0);

	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		changeEvent(arg0);

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(allowedChar(arg0.getKeyChar()))
			arg0.consume();

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(allowedChar(arg0.getKeyChar()))
			arg0.consume();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if(!allowedChar(arg0.getKeyChar()))
			arg0.consume();
	}


		}