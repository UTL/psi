package webApplication.grafica;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Il pannello per la visualizzazione del contenuto di un elemento di tipo Link
 * 
 * @author Andrea
 */
public class PannelloLink extends PannelloGeneric {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4413191946425965666L;
	protected JTextField urlPath;
	private static final String BASEURLPATH = "http://";
	protected JTextField urlText;

	private static final String PATHTOOLTIP = "Enter the link of the site you want to be connected to";
	private static final String TEXTTOOLTIP = "Enter the text you want to be displayed for this link";

	/**
	 * Crea il pannello ed i relativi campi
	 */
	protected PannelloLink() {
		super();
		
		JLabel lblLinkTarget = new JLabel("URL:");
		lblLinkTarget.setBounds(110, 60, 26, 14);
		// lblLinkTarget.setBounds(135, 109, 26, 14);
		add(lblLinkTarget);

		urlPath = new JTextField(BASEURLPATH);
		urlPath.setToolTipText(PATHTOOLTIP);
		// TODO aggiungere il document listener
		urlPath.setBounds(lblLinkTarget.getX() + 46, lblLinkTarget.getY() - 3, 174, 22);
		add(urlPath);

		JLabel lblLinkText = new JLabel("Link text:");
		lblLinkText.setBounds(lblLinkTarget.getX() - 26, lblLinkTarget.getY() + 50, 66, 14);
		add(lblLinkText);

		urlText = new JTextField();
		urlText.setToolTipText(TEXTTOOLTIP);
		urlText.setBounds(urlPath.getX(), lblLinkText.getY() - 3, 174, 20);
		add(urlText);
	}

	protected void setPath(String t) {
		urlPath.setText(t);
	}

	protected String getPath() {
		return urlPath.getText();
	}

	protected void setText(String t) {
		urlText.setText(t);
	}

	protected String getText() {
		return urlText.getText();
	}

}
