package webApplication.grafica;

import java.awt.Color;
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
	private JLabel errorPath;
	private static final String BASEURLPATH = "http://";
	protected JTextField urlText;
	private JLabel errorText;

	private static final String ERRORPATHLABEL = "Invalid URL";
	private static final String ERRORTEXTLABEL = "Empty value";
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
		
		errorPath = new JLabel(ERRORPATHLABEL);
		errorPath.setBounds(urlPath.getX(), urlPath.getY()+urlPath.getHeight()-3, 150, 20);
		errorPath.setForeground(Color.RED);
		errorPath.setVisible(false);
		add(errorPath);

		JLabel lblLinkText = new JLabel("Link text:");
		lblLinkText.setBounds(lblLinkTarget.getX() - 26, lblLinkTarget.getY() + 50, 66, 14);
		add(lblLinkText);

		urlText = new JTextField();
		urlText.setToolTipText(TEXTTOOLTIP);
		urlText.setBounds(urlPath.getX(), lblLinkText.getY() - 3, 174, 20);
		add(urlText);
		
		errorText = new JLabel(ERRORTEXTLABEL);
		errorText.setBounds(urlText.getX(), urlText.getY()+urlText.getHeight()-3, 150, 20);
		errorText.setForeground(Color.RED);
		errorText.setVisible(false);
		add(errorText);
		
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
		boolean correct = !Utils.redify(urlPath, !urlMatcher.matches());
		errorPath.setVisible(Utils.redify(urlPath, !urlMatcher.matches()));
		if (correct) {
			urlPath.setToolTipText(TEXTTOOLTIP);
		} else {
			urlPath.setToolTipText(ERRORTEXTTOOLTIP);
		}
		
//		return !Utils.redify(urlPath, !urlMatcher.matches());
		return correct;
	}
	
	protected boolean isTextCorrect() {
		boolean correct = !Utils.redify(urlText, urlText.getText().isEmpty());
		errorText.setVisible(Utils.redify(urlText, urlText.getText().isEmpty()));
		if (correct) {
			urlText.setToolTipText(TEXTTOOLTIP);
		} else {
			urlText.setToolTipText(ERRORTEXTTOOLTIP);
		}
		return correct;
	}

}
