package webApplication.grafica;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public abstract class CustomFileFilter extends FileFilter {

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String extension = getExtension(f);
		if (extension != null) {
			return validExtension(extension);
		}
		return false;
	}

	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if ((i > 0) && (i < s.length() - 1)) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	protected abstract boolean validExtension(String extension);

	public abstract String getDescription();
}
