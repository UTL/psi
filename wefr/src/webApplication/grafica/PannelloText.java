package webApplication.grafica;

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
	private static final String IMPORTTEXT = "Import from file";
	private static final String BTNTOOLTIP = "Click here to select a file from which load text";
	private static final String LOAD_TEXT = "Load text";
	protected static final String TEXTTOOLTIP = "The text that will be displayed";
	protected static final String ERRORTEXTTOOLTIP = "Text cannot be empty";

	private static final int labelHeight = 14;
	private static final int labelWidth = 94;
	private static final int textAreaHeight = 130;
	private static final int textAreaWidth = 420;
	private static final int btnHeight = 27;
	private static final int btnWidth = 130;

	protected JTextArea textArea;

	/**
	 * Crea il pannello ed i relativi campi
	 */
	protected PannelloText() {
		super();

		JLabel lblEnterText = new JLabel(LABELTEXT);
		lblEnterText.setBounds(0, 6, labelWidth, labelHeight);
		add(lblEnterText);

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
		return !Utils.redify(textArea, textArea.getText().isEmpty());
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
					textArea.setText(letto);
				} catch (FileNotFoundException e1) {
					// TODO mettere l'errore
				}
			}
		}
	}

}
