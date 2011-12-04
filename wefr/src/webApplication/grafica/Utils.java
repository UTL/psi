package webApplication.grafica;

import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

public class Utils {

	public static boolean isBlank(JTextComponent toCheck) {
		if (toCheck.getText().trim().length() > 0)
			return false;
		return true;
	}

	public static boolean redify(JTextComponent toRed, boolean b) {
		if (b) {
			toRed.setBorder(new LineBorder(new Color(255, 0, 0), 1, true));// bordo rosso
		} else {
			toRed.setBorder(new LineBorder(new Color(184, 207, 229), 1, true));// bordo normale
		}
		return b;
	}
	
}
