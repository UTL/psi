package webApplication.grafica;

import java.awt.Image;
import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteMolteplice;
import webApplication.business.ComponenteSemplice;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;
import webApplication.xml.CARoot;
import webApplication.xml.CAandxor;
import webApplication.xml.CINodeType;
import webApplication.xml.ECIType;
import webApplication.xml.ObjectFactory;
import webApplication.xml.TypeOfECI;
import webApplication.xml.XMLMarshaller;

/**
 * Convert the tree into the corresponding xml file
 * @author Andrea
 *
 */
public class XMLGenerator {
	
	//FIXME probabile: spostare la logica dentro il pacchetto di generazione dell'xml e tenere la parte grafica qui?
	
	private static final String FILENAME = "fileCA.xml";
	private static final String JOPTIONPANETITLE = "File existing";
	private static final String FILEEXISTINGMESSAGE = "XML file already exists. Continuing will overwrite the old file.\n Do you want to continue?";
	
	private Window owner;

	protected XMLGenerator(Window owner) {
		this.owner = owner;
	}
	
	/**
	 * Write the xml file according to the schema
	 * @return		The state of the execution.
	 * 				0 = execution ended normally
	 *				-1= xml file already exists
	 * 				-2= context creation exception
	 * 				-3= file related exception 
	 * 				-4= validator related exception
	 * 				-5= close outputstream related exception
	 */
	protected int generateXML() {
//		try {
			File file = new File(FILENAME);
			if (file.exists()) {
				int choice = JOptionPane.showConfirmDialog(owner, FILEEXISTINGMESSAGE, JOPTIONPANETITLE, JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.NO_OPTION) {
					return -1;
				}
			}
			XMLMarshaller marshaller = new XMLMarshaller(MainWindow.albero.getTree(), file);
			marshaller.execute();
//		} catch (JAXBException e) {
//			e.printStackTrace();
//			return -2;
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return -3;
//		} catch (SAXException e) {
//			e.printStackTrace();
//			return -4;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return -5;
//		}
		return 0;
	}

}
