package webApplication.grafica;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	protected static final String PATHTOOLTIP = "Enter the link of the site you want to be connected to";
	protected static final String ERRORPATHTOOLTIP = "Link blank or not valid";
	protected static final String TEXTTOOLTIP = "Enter the text you want to be displayed for this link";
	protected static final String ERRORTEXTTOOLTIP = "Text blank or not valid";
	
	private static final String URL_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

	/**
	 * Crea il pannello ed i relativi campi
	 */
	protected PannelloLink() {
		super();
		
		JLabel lblLinkTarget = new JLabel("URL:");
		lblLinkTarget.setBounds(110, 60, 26, 14);
		add(lblLinkTarget);

		urlPath = new JTextField(BASEURLPATH);
		urlPath.setToolTipText(PATHTOOLTIP);
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
	
	protected boolean isPathCorrect() {
		Matcher urlMatcher = URL_PATTERN.matcher(urlPath.getText());
		return !Utils.redify(urlPath, !urlMatcher.matches());
	}
	
	protected boolean isTextCorrect() {
		return !Utils.redify(urlText, urlText.getText().isEmpty());
	}

}
