package webApplication.grafica;

import java.awt.Component;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


public class CustomTreeRenderer extends DefaultTreeCellRenderer {

	private ImageIcon rootIcon;
	private ImageIcon linkIcon;
	private ImageIcon alternativeIcon;
	private ImageIcon compositeIcon;
	private ImageIcon textIcon;
	private ImageIcon imageIcon;
	
	public CustomTreeRenderer() {
		super();
		
		File schema = new File("/home/enrico/Immagini/Flash_icon.gif");	

		if( schema.canRead())
			System.out.println("LEGGIBILE");
		else
			System.out.println("NON LEGGIBILE");
		
		//TODO cercare delle icone per l'albero JTree e metterle nel path corretto
		rootIcon = new ImageIcon(CustomTreeRenderer.class.getResource("test_icon.gif"));
		/*linkIcon = new ImageIcon(CustomTreeRenderer.class.getResource("/images/specialIcon.gif"));
		alternativeIcon = new ImageIcon(CustomTreeRenderer.class.getResource("/images/defaultIcon.gif"));
		compositeIcon = new ImageIcon(CustomTreeRenderer.class.getResource("/images/specialIcon.gif"));
		textIcon = new ImageIcon(CustomTreeRenderer.class.getResource("/images/defaultIcon.gif"));
//		imageIcon = new ImageIcon(CustomTreeRenderer.class.getResource("/images/specialIcon.gif"));*/
	}
	
	
	public Component getTreeCellRendererComponent(Albero tree,
		      Object value,boolean sel,boolean expanded,boolean leaf,
		      int row,boolean hasFocus) {

		        super.getTreeCellRendererComponent(tree, value, sel, 
		          expanded, leaf, row, hasFocus);

		        Object nodeObj = ((DefaultMutableTreeNode)value).getUserObject();
		        setIcon(rootIcon);
		        // check whatever you need to on the node user object
		        /*if (true) { //TODO verificare il tipo dell'oggetto // ((WhateverNodeObjectClass)nodeObj).someProperty
		            setIcon(rootIcon);
		        } else {
		            setIcon(linkIcon);
		        } */
		        return this;
		    }
	
	
}
