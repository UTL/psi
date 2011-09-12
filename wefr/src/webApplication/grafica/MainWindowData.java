package webApplication.grafica;

import javax.swing.JRootPane;
import javax.swing.JScrollPane;

import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

public class MainWindowData {
	private Wizard myWizard;
	private String currentProject;
	private MainWindow thisWindow;
	private JScrollPane scrollPane_composite;
	private Immagine img;
	private Link lnk;
	private Testo txt;
	private ComponenteAlternative alt;
	private ComponenteComposto cmp;
	private JRootPane root;

	public MainWindowData() {
		
		
	}

	public Wizard getMyWizard() {
		return myWizard;
	}

	public void setMyWizard(Wizard myWizard) {
		this.myWizard = myWizard;
	}

	public String getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(String currentProject) {
		this.currentProject = currentProject;
	}

	public MainWindow getThisWindow() {
		return thisWindow;
	}

	public void setThisWindow(MainWindow thisWindow) {
		this.thisWindow = thisWindow;
	}

	public JScrollPane getScrollPane_composite() {
		return scrollPane_composite;
	}

	public void setScrollPane_composite(JScrollPane scrollPane_composite) {
		this.scrollPane_composite = scrollPane_composite;
	}

	public Immagine getImg() {
		return img;
	}

	public void setImg(Immagine img) {
		this.img = img;
	}

	public Link getLnk() {
		return lnk;
	}

	public void setLnk(Link lnk) {
		this.lnk = lnk;
	}

	public Testo getTxt() {
		return txt;
	}

	public void setTxt(Testo txt) {
		this.txt = txt;
	}

	public ComponenteAlternative getAlt() {
		return alt;
	}

	public void setAlt(ComponenteAlternative alt) {
		this.alt = alt;
	}

	public ComponenteComposto getCmp() {
		return cmp;
	}

	public void setCmp(ComponenteComposto cmp) {
		this.cmp = cmp;
	}

	public JRootPane getRoot() {
		return root;
	}

	public void setRoot(JRootPane root) {
		this.root = root;
	}
}