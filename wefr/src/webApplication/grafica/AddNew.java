package webApplication.grafica;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import webApplication.business.Componente;
import webApplication.business.ComponenteSemplice;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

import java.awt.event.ActionEvent;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class AddNew extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3236559861349447627L;
	private JPanel contentPane;
	private JTextField textField_category;
	private JTextField textField_name;
	private JTextField textField_linkText;
	private JTextField textField_url;
	private JTextField textField_imagePath;
	
	private JPanel panel_link;
	private JPanel panel_text;
	private JPanel panel_image; 
	
	private static JTextArea textArea;
	private JScrollPane scrollingArea;
	private JButton button_back;
	private JButton buttonAdd;
	
	private JRadioButton rdbtnLink;
	private JRadioButton rdbtnText;
	private JRadioButton rdbtnImage;
	
	private static boolean ONLYDISPOSE = true;
	private static boolean CREATENEWCOMP = false;
	
	private boolean anError=true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddNew frame = new AddNew();
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
	public AddNew() {
		setAlwaysOnTop(true);

		setResizable(false);
		setTitle("Add new to Alternative");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 532, 542);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField_category = new JTextField();
		textField_category.setColumns(10);
		textField_category.setBounds(229, 36, 114, 19);
		contentPane.add(textField_category);
		
		textField_name = new JTextField();
		textField_name.setToolTipText("name");
		textField_name.setColumns(10);
		textField_name.setBounds(229, 11, 114, 19);
		contentPane.add(textField_name);
		
		JLabel name = new JLabel("Name:");
		name.setBounds(119, 13, 51, 15);
		contentPane.add(name);
		
		JLabel category = new JLabel("Category:");
		category.setBounds(119, 38, 81, 15);
		contentPane.add(category);
		
		panel_link = new JPanel();
		panel_link.setEnabled(false);
		
		panel_link.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), " Link ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
		panel_link.setBounds(92, 72, 426, 84);
		contentPane.add(panel_link);
		panel_link.setLayout(null);
		
		JLabel lblUrl = new JLabel("URL:");
		lblUrl.setBounds(24, 23, 51, 15);
		panel_link.add(lblUrl);
		
		JLabel lblLink = new JLabel("Link text:");
		lblLink.setBounds(24, 48, 81, 15);
		panel_link.add(lblLink);
		
		textField_linkText = new JTextField();
		textField_linkText.setColumns(10);
		textField_linkText.setBounds(111, 46, 303, 19);
		panel_link.add(textField_linkText);
		
		textField_url = new JTextField();
		textField_url.setToolTipText("name");
		textField_url.setColumns(10);
		textField_url.setBounds(111, 21, 303, 19);
		panel_link.add(textField_url);
		
		rdbtnLink = new JRadioButton("Link");
		
		rdbtnLink.setBounds(8, 110, 68, 23);
		contentPane.add(rdbtnLink);
		
		rdbtnText = new JRadioButton("Text");
		rdbtnText.setSelected(true);
		rdbtnText.setBounds(8, 238, 68, 23);
		contentPane.add(rdbtnText);
		
		panel_text = new JPanel();
		panel_text.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), " Text ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_text.setLayout(null);
		panel_text.setEnabled(false);
		panel_text.setBounds(92, 168, 426, 183);
		contentPane.add(panel_text);
		
		JButton button = new JButton("Import from file");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				readFile();//MainWindow.fileChooser(MainWindow.TEXT), textArea);
				updateAddBtn();

			}

			
		});
		button.setFont(new Font("Dialog", Font.PLAIN, 12));
		button.setBounds(12, 152, 142, 19);
		panel_text.add(button);
		
		textArea = new JTextArea();
		scrollingArea = new JScrollPane(textArea);
		scrollingArea.setBorder(new LineBorder(new Color(204, 204, 204), 1, true));
		scrollingArea.setBounds(12, 20, 402, 120);

		panel_text.add(scrollingArea);
		
		rdbtnImage = new JRadioButton("Image");
		rdbtnImage.setBounds(8, 378, 68, 23);
		contentPane.add(rdbtnImage);
		
		panel_image = new JPanel();
		panel_image.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), " Image ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
		panel_image.setLayout(null);
		panel_image.setEnabled(false);
		panel_image.setBounds(92, 363, 426, 88);
		contentPane.add(panel_image);
		
		JLabel lblPath = new JLabel("File path:");
		lblPath.setBounds(12, 24, 81, 15);
		panel_image.add(lblPath);
		
		textField_imagePath = new JTextField();
		textField_imagePath.setToolTipText("name");
		textField_imagePath.setColumns(10);
		textField_imagePath.setBounds(12, 51, 301, 19);
		textField_imagePath.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				
				checkPath();
				updateAddBtn();

			}
			public void removeUpdate(DocumentEvent e) {
				checkPath();
				updateAddBtn();

			// text was deleted
			}
			public void insertUpdate(DocumentEvent e) {
				checkPath();
				updateAddBtn();


			// text was inserted
			}
			});
		panel_image.add(textField_imagePath);
		
		JButton button_browse = new JButton("Browse");
		button_browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPath();
				updateAddBtn();

			}
		});
		button_browse.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_browse.setBounds(325, 51, 89, 19);
		panel_image.add(button_browse);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnImage);
		bg.add(rdbtnText);
		bg.add(rdbtnLink);
		
		button_back = new JButton("Back");
		button_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fireEvent(ONLYDISPOSE);
				dispose();
				
			}
		});
		button_back.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_back.setBounds(341, 468, 82, 27);
		contentPane.add(button_back);
		
		buttonAdd = new JButton("Add");
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fireEvent(CREATENEWCOMP);
				dispose();
			}
		});
		buttonAdd.setFont(new Font("Dialog", Font.BOLD, 12));
		buttonAdd.setBounds(433, 468, 82, 27);
		contentPane.add(buttonAdd);
		
		rdbtnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enabler(panel_image);
				updateAddBtn();
			}
		});
		
		rdbtnText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enabler(panel_text);
				updateAddBtn();

			}
		});
		
		rdbtnLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enabler(panel_link);
				updateAddBtn();


			}
		});
		
		setChangeListener(textField_category);
		setChangeListener(textField_linkText);
		setChangeListener(textField_name);
		setChangeListener(textField_url);
		setChangeListener(textArea);
		
		enabler(panel_text);
		
		redify(textField_name,isBlank(textField_name));
		redify(textField_category,isBlank(textField_category));
		updateAddBtn();
		
	}
	
	private void enabler(JPanel toEnable){
		enablerDisabler(panel_image, false);
		enablerDisabler(panel_link, false);
		enablerDisabler(panel_text, false);
		enablerDisabler(toEnable, true);
	}
	
	private void enablerDisabler(JPanel toUpdate, boolean enable){
		setBordi(toUpdate, enable);
		toUpdate.setEnabled(enable);
		Component[] figli =  toUpdate.getComponents();
		int i;
		for(i=0; i < figli.length;i++)
		{
			updateComponent(figli[i], enable);

		}
		
	}
	
	private void updateComponent(Component figlio, boolean enable){
		figlio.setEnabled(enable);
		if(figlio == scrollingArea){
			updateComponent(textArea,enable);
			
			//le tre righe qua sotto sono un workaround per un baco delle JScrollPane
			scrollingArea.getHorizontalScrollBar().setEnabled(enable);
			scrollingArea.getVerticalScrollBar().setEnabled(enable);
			scrollingArea.getViewport().getView().setEnabled(enable);
		}
		if (figlio instanceof javax.swing.JTextField || figlio instanceof javax.swing.JTextArea){
			if(enable)
				redify((JTextComponent) figlio,isBlank((JTextComponent) figlio));
			else
				redify((JTextComponent) figlio, false);
		}
	}
	
	private void setBordi(JPanel toDisable, boolean enable) {
		if(enable){
			if(toDisable== panel_image)				
				panel_image.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), " Image ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			else if (toDisable == panel_link)
				panel_link.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), " Link ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			else
				panel_text.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), " Text ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));

		}
		else{
			if(toDisable== panel_image)
				panel_image.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), " Image ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
			else if (toDisable == panel_link)
				panel_link.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), " Link ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
			else
				panel_text.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), " Text ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
		}
		
	}
	
	private void redify(JTextComponent toRed, boolean b){
		if(b)
			toRed.setBorder(new LineBorder(new Color(255, 0, 0), 1, true));//bordo rosso
		else 
			toRed.setBorder(new LineBorder(new Color(184, 207, 229), 1, true));//bordo normale
			
	}
	
	private void setChangeListener (JTextComponent toAttachListener){
		
		JTextComponent textComponent_imagepath=toAttachListener;
		textComponent_imagepath.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				redify(fromDocToJComp(e.getDocument()),isBlank((fromDocToJComp(e.getDocument()))));
				updateAddBtn();
			}
			public void removeUpdate(DocumentEvent e) {
				redify(fromDocToJComp(e.getDocument()),isBlank((fromDocToJComp(e.getDocument()))));
				updateAddBtn();
			}
			public void insertUpdate(DocumentEvent e) {
				redify(fromDocToJComp(e.getDocument()),isBlank((fromDocToJComp(e.getDocument()))));
				updateAddBtn();
			}
			});
		
		
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
	
	private boolean isBlank(JTextComponent toCheck){
		if (toCheck.getText().trim().length()>0)
			return false;
		return true;
	}
	
	
	private boolean nameExists(){
		//TODO scrivere la funzione
		return false;
	}
	
	private boolean erroriPresenti(){
		boolean result;
		result=isBlank(textField_name) || nameExists();
		result= result || isBlank(textField_category);
		if (rdbtnImage.isSelected())
			result= result || isBlank(textField_imagePath) || fileError();
		
		else if
			(rdbtnLink.isSelected())
			result= result || isBlank(textField_linkText) || isBlank(textField_url) || errorUrl();
		else 
			result= result || isBlank(textArea);
		return result;	
		
	}

	private boolean errorUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean fileError(){
		return !MainWindow.isPathCorrect(textField_imagePath.getText());
	}
	
	private ArrayList<MyEventClassListener> _listeners = new ArrayList<MyEventClassListener>();

	public void addEventListener(MyEventClassListener listener) {
		_listeners.add(listener);
	}
	
	private void checkPath(){
		redify(textField_imagePath, !MainWindow.isPathCorrect(textField_imagePath.getText()));
	}
	
	private static void readFile(){
		try {
			//TODO escapare caratteri speciali
			File f = MainWindow.getFileFromChooser(MainWindow.TEXT);
			
			if (f != null){
			String letto = Wizard.readFile(f);
			if ( letto!= null && letto.length()>0)
				textArea.setText(letto);}
		} catch (IOException e) {
		}
	}
	
	private ComponenteSemplice getNuovoComp(){
		//TODO escapare i vari campi
		//TODO quando viene invocato il metodo bisogna passargli i valori di enfasi e importanza
		ComponenteSemplice output = null;
		if (!erroriPresenti()){
			if (rdbtnImage.isSelected())
				//FIXME non vanno fissati a zero i due parametri!
				output = new Immagine(textField_name.getText(), textField_category.getText(), 0, 0, textField_imagePath.getText());

			else if (rdbtnLink.isSelected())
				output = new Link(textField_name.getText(), textField_category.getText(), 0,0, textField_url.getText(), textField_linkText.getText());
			else 
				output= new Testo(textField_name.getText(), textField_category.getText(),0,0,textField_imagePath.getText());
		}
		return output;
	}
	
	private void setPath(){
		String path = MainWindow.fileChooser(MainWindow.IMAGE);
		if (path!= null && path.length()>0)
			textField_imagePath.setText(path);
	}
	
	private void updateAddBtn(){
		updateComponent(buttonAdd, !erroriPresenti());
	}

	private synchronized void fireEvent(boolean onlyDispose) {	
		MyEventClass event = null;
		if (onlyDispose == CREATENEWCOMP)
			event = new MyEventClass(this, getNuovoComp());
		Iterator<MyEventClassListener> i = _listeners.iterator();
		while(i.hasNext())  {
			((MyEventClassListener) i.next()).handleMyEventClassEvent(event);
		}
	}
	
}





