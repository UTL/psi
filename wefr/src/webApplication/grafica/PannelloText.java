package webApplication.grafica;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import webApplication.business.Testo;
import webApplication.grafica.TreePanel.ChangeFieldAction;

/**
 * Il pannello per la visualizzazione del contenuto di un elemento di tipo Testo
 * 
 * @author Andrea
 */
public class PannelloText extends PannelloGeneric implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8158020877021306738L;

	private static final String LABELTEXT = "Enter text:";
	private static final String ERRORLABELTEXT = "Empty value";
	private static final String IMPORTTEXT = "Import from file";
	private static final String BTNTOOLTIP = "Click here to select a file from which load text";
	private static final String LOAD_TEXT = "Load text";
	private static final String TEXTTOOLTIP = "The text that will be displayed";
	private static final String ERRORTEXTTOOLTIP = "Text cannot be empty";

	private static final int labelHeight = 14;
	private static final int labelWidth = 94;
	private static final int textAreaHeight = 130;
	private static final int textAreaWidth = 420;
	private static final int btnHeight = 27;
	private static final int btnWidth = 145;

	private JLabel errorLabel;
	protected JTextArea textArea;

	/**
	 * Crea il pannello ed i relativi campi
	 */
	protected PannelloText() {
		super();

		JLabel lblEnterText = new JLabel(LABELTEXT);
		lblEnterText.setBounds(0, 6, labelWidth, labelHeight);
		add(lblEnterText);
		
		errorLabel = new JLabel(ERRORLABELTEXT);
		errorLabel.setBounds(350, 6, 150, 20);
		errorLabel.setForeground(Color.RED);
		errorLabel.setVisible(true);
		add(errorLabel);

		textArea = new JTextArea();
		textArea.setToolTipText(TEXTTOOLTIP);
		// TODO inserire il document listener per gli errori
		JScrollPane scrollingArea = new JScrollPane(textArea);
		scrollingArea.setBounds(lblEnterText.getX(), lblEnterText.getY() + 19, textAreaWidth, textAreaHeight);
		add(scrollingArea);

		JButton btnImportFromFile = new JButton(IMPORTTEXT);
		btnImportFromFile.setActionCommand(LOAD_TEXT);
		btnImportFromFile.setToolTipText(BTNTOOLTIP);
		btnImportFromFile.addActionListener(this);
		btnImportFromFile.setBounds(lblEnterText.getX() + (textAreaWidth - btnWidth) / 2, lblEnterText.getY() + 154, btnWidth, btnHeight);
		add(btnImportFromFile);
	}

	/**
	 * Setta il testo relativo al componente
	 * 
	 * @param t	Il testo da inserire
	 */
	protected void setText(String t) {
		textArea.setText(t);
	}

	/**
	 * Ritorna il testo relativo al componente
	 * 
	 * @return Il testo
	 */
	protected String getText() {
		return textArea.getText();
	}
	
	protected boolean isCorrect() {
		boolean correct = !Utils.redify(textArea, Utils.isBlank(textArea));
		errorLabel.setVisible(Utils.redify(textArea, Utils.isBlank(textArea)));
		if (correct) {
			textArea.setToolTipText(TEXTTOOLTIP);
		} else {
			textArea.setToolTipText(ERRORTEXTTOOLTIP);
		}
		return correct;
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(LOAD_TEXT)) {
			JFileChooser fc = new JFileChooser(MainWindow.defTextDir);
			// TODO definire il filtro dei file?
			int choice = fc.showOpenDialog(this);
			if (choice == JFileChooser.APPROVE_OPTION) {
				String letto = "";
				String eol = System.getProperty("line.separator");
				try {
					FileReader reader = new FileReader(fc.getSelectedFile()
							.getPath());
					Scanner scanReader = new Scanner(reader);
					while (scanReader.hasNextLine()) {
						letto = letto + scanReader.nextLine() + eol;
					}
					MainWindow.properties.removeListeners();
					textArea.setText(letto);
					if (!(this.getTopLevelAncestor() instanceof Wizard)) {
						ChangeFieldAction changeFieldAction = MainWindow.albero.new ChangeFieldAction();
						changeFieldAction.putValue(TreePanel.ChangeFieldAction.FIELD, TreePanel.ChangeFieldAction.TEXT);
						changeFieldAction.putValue(TreePanel.ChangeFieldAction.OLDVALUE, ((Testo)((DisabledNode)MainWindow.albero.getTree().getLastSelectedPathComponent()).getUserObject()).getTesto());
						changeFieldAction.putValue(TreePanel.ChangeFieldAction.NEWVALUE, (MainWindow.properties.pannelloText.getText()));
						changeFieldAction.actionPerformed(new ActionEvent(MainWindow.properties.pannelloText.textArea, ActionEvent.ACTION_PERFORMED,""));
					}
					MainWindow.properties.addListeners();
				} catch (FileNotFoundException e1) {
					// non serve
				}
			}
		}
	}

}
