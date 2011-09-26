package webApplication.grafica;

import java.io.File;

import javax.swing.filechooser.FileFilter;


public abstract class CustomFileFilter extends FileFilter {
	
	public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            return extensions(extension);
        }

        return false;
    }

	protected abstract boolean extensions(String extension);
	/*{
		if (extension.equals(Utils.tiff) ||
		    extension.equals(Utils.tif) ||
		    extension.equals(Utils.gif) ||
		    extension.equals(Utils.jpeg) ||
		    extension.equals(Utils.jpg) ||
		    extension.equals(Utils.png)) {
		        return true;
		} else {
		    return false;
		}
	}*/

    //The description of this filter
    public abstract String getDescription(); 
}
