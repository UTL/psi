package webApplication.grafica;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

public abstract class CustomFileChooser{
	
	
	protected JFileChooser fc;
	protected Options optionFrame;
	
	protected Component parent;
	protected int option;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3668680215000685482L;
	
	public CustomFileChooser(Options oFrame, Component p) {
		this.optionFrame= oFrame;
		this.parent=p;
	}
	
	protected abstract int buildFileChooser ();
	
	public void showDialog(){
		option=buildFileChooser();
	}
	
	public String getFilePath() {
		if(option == fc.APPROVE_OPTION)
			return fc.getSelectedFile().getAbsolutePath();
		else return "";
		}

	
	public File getFile() {
		if(option == fc.APPROVE_OPTION)
			return fc.getSelectedFile();
		else return null;
	}
	
	
	/*{
		JFileChooser fileChooser=null;
		if(frameOptions != null){
			if(i== LOADSAVE && frameOptions.getDefDirLoadSave()!= null && frameOptions.getDefDirLoadSave().length()>0)
				fileChooser = new JFileChooser(frameOptions.getDefDirLoadSave()); 
			else if (i == TEXT && frameOptions.getDefDirText()!= null && frameOptions.getDefDirText().length()>0)
				fileChooser = new JFileChooser(frameOptions.getDefDirText()); 
			else if (i == IMAGE){
				if(frameOptions.getDefDirImage()!= null && frameOptions.getDefDirImage().length()>0)
					fileChooser = new JFileChooser(frameOptions.getDefDirImage()); 
				else
					fileChooser = new JFileChooser();
				
				fileChooser.addChoosableFileFilter(new CustomFileFilter());
				fileChooser.setAcceptAllFileFilterUsed(false);
				System.out.println("asfds");
			}
			else {
				return null;
			}
		}
		else
			fileChooser = new JFileChooser();
		
		chooseFile(fileChooser.showOpenDialog(contentPane), fileChooser); 
		
		return fileChooser;

	}*/
	
	

	
	protected void chooseFile(int chooserValue, JFileChooser fc, JTextField target){
		//TODO settare le cartelle di default
		if (chooserValue == JFileChooser.APPROVE_OPTION) {
            target.setText(fc.getSelectedFile().getAbsolutePath());
        } 
	}
	
	String chooseFile(int chooserValue, JFileChooser fc){
		//TODO settare le cartelle di default
		String output = "";
		if (chooserValue == JFileChooser.APPROVE_OPTION) {
			output = fc.getSelectedFile().getAbsolutePath();
        } 
		return output;
	}
	
}
