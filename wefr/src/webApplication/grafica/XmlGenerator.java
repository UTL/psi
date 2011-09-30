package webApplication.grafica;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import webApplication.business.Componente;
import webApplication.business.ComponenteSemplice;
import webApplication.business.ComponenteComposto;
import webApplication.business.Link;
import webApplication.business.Immagine;
import webApplication.business.Testo;
import webApplication.business.ComponenteAlternative;

public class XmlGenerator {
	
	public static void generateXML(Vector<Componente> moduli) {//GEN-FIRST:event_btWebsiteActionPerformed

		try {
			//FileOutputStream file = new FileOutputStream("fileCA.xml");
			PrintStream Output = new PrintStream(new File ("fileCA.xml"), "UTF-16");

			Output.println("<?xml version=\"1.0\" encoding=\"UTF-16\" ?>"); // prima riga dell'Xml di intestazione
			Output.println("<CARoot xmlns=\"http://mamba.org/catree\">"); //associazione all'Xsd di riferimento
			Output.println("<ID>001</ID>");                        //
			Output.println("<Type>AND</Type>");                   //  caratteristiche della Home
			Output.println("<Necessity>1.0</Necessity>");        //
			Output.println("<Relevance>1.0</Relevance>");       //

			for(int i=0;i<moduli.size();i++){

				if(moduli.get(i) instanceof ComponenteComposto)
					genComposto(Output, moduli, i);
				else if(moduli.get(i) instanceof ComponenteAlternative)
					genAlternative(Output, moduli, i);
				else
					genSimple(Output, moduli, i);

			}
			Output.println("</CARoot>");
			JOptionPane.showMessageDialog(null, "XML generated successfully!");


		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "XML not generated");
			System.out.println("Error: " + e);
			System.exit(1);
		}

	}//GEN-LAST:event_btWebsiteActionPerformed

	protected static void genSimple(PrintStream Output,
			Vector<Componente> moduli, int i) throws IOException {
		{
			Output.println("<ECI>");
			Output.println("<ID>"+moduli.get(i).getNome()+"</ID>");
			if(moduli.get(i) instanceof Immagine){
				printType(Output, "Image");
			}else  if(moduli.get(i) instanceof Link){
				printType(Output, "Link");
			}else  if(moduli.get(i) instanceof Testo){
				printType(Output, "Text");
			}
			printGeneric(Output, moduli, i);
			if(moduli.get(i) instanceof Immagine){
				printImg(Output, moduli, i);

			}else  if(moduli.get(i) instanceof Link){
				printLink(Output, moduli, i);
				
			}else  if(moduli.get(i) instanceof Testo){
				printText(Output, moduli, i);
			}
		    Output.println("</ECI>");
            }
	}

	protected static void printType(PrintStream Output, String s) {
		Output.println("<Type>"+s+"</Type>");
	}

	protected static void printText(PrintStream Output,	Vector<Componente> moduli, int i) {
		int count = ((Testo)moduli.get(i)).getTesto().length();
		Output.println("<Char_count>"+count+"</Char_count>");
		//Output.println(" <Value><![CDATA[test]]></Value>");
		Output.println(" <Value><![CDATA["+((Testo)moduli.get(i)).getTesto()+"]]></Value>");
		//Output.println(" <Value>"+((Testo)moduli.get(i)).getTesto()+"</Value> ");
	}

	protected static void printLink(PrintStream Output,	Vector<Componente> moduli, int i) {
		int count = ((Link)moduli.get(i)).getUri().length();
		Output.println("<Char_count>"+count+"</Char_count>");
		//Output.println(" <Value><![CDATA[test]]></Value>");
		Output.println(" <Value><![CDATA["+((Link)moduli.get(i)).getUri()+"]]></Value>");
		//   Output.println("<Value>"+((Link)moduli.get(i)).getUri()+"</Value>");  //chiedere testo
	}

	protected static void printImg(PrintStream Output,Vector<Componente> moduli, int i) throws IOException {
		String path=((Immagine)moduli.get(i)).getPath();
		Image immagine = ImageIO.read(new File(path));
		Output.println("<Width>"+immagine.getWidth(null)+"</Width>");
		Output.println("<Height>"+immagine.getHeight(null)+"</Height>");
		String str=((Immagine)moduli.get(i)).getPath();
		int pos=str.lastIndexOf("\\");
		str=str.substring(pos+1);
		Output.println(" <Value>./image/"+str+"</Value>");
	}

	protected static void printGeneric(PrintStream Output,Vector<Componente> moduli, int i) {
		int visibilita;
		int enfasi;
		visibilita=(int)((1-(moduli.get(i).getVisibilita()/3+0.2))*100);
		Output.println("<Necessity>0."+visibilita+"</Necessity>");
		enfasi=(int)((1-(moduli.get(i).getEnfasi()/3+0.2))*100);
		Output.println("<Relevance>0."+enfasi+"</Relevance>");
		Output.println("<Category>"+moduli.get(i).getCategoria()+"</Category>");
	}

	protected static void genAlternative(PrintStream Output,Vector<Componente> moduli, int i) throws IOException {
		{
		    Output.println("<CINode>");
		    Output.println("<ID>"+moduli.get(i).getNome()+"</ID>");
		    Output.println("<Type>XOR</Type>");
		    printGeneric(Output, moduli, i);
		    Vector<ComponenteSemplice>ca =((ComponenteAlternative)moduli.get(i)).getOpzioni();
		    double passo=0.8/ca.size();
		    for(int j=0;j<ca.size();j++){
		        Output.println("<ECI>");
		        Output.println("<ID>"+ca.get(j).getNome()+"</ID>");
		        if((ca.get(j).getClass().getName()).equals("webApplication.business.Immagine")){
		            Output.println("<Type>Image</Type>");
		        }else  if((ca.get(j).getClass().getName()).equals("webApplication.business.Link")){
		            Output.println("<Type>Link</Type>");
		        }else  if((ca.get(j).getClass().getName()).equals("webApplication.business.Testo")){
		            Output.println("<Type>Text</Type>");
		        }
		        int coverage=(int)((0.9-(passo*j))*100);
		        //String str = String.format ("%.2f", coverage);
		        Output.println("<Coverage>0."+coverage+"</Coverage>");
		        Output.println("<Category>"+ca.get(j).getCategoria()+"</Category>");
		        if((ca.get(j).getClass().getName()).equals("webApplication.business.Immagine")){
		            String path=((Immagine)ca.get(j)).getPath();
		            Image immagine = ImageIO.read(new File(path));
		            Output.println("<Width>"+immagine.getWidth(null)+"</Width>");
		            Output.println("<Height>"+immagine.getHeight(null)+"</Height>");
		            String str=((Immagine)ca.get(i)).getPath();
		            int pos=str.lastIndexOf("\\");
		            str=str.substring(pos+1);
		            Output.println(" <Value><![CDATA[./image/"+str+"]]></Value>");
		        }else  if((ca.get(j).getClass().getName()).equals("webApplication.business.Link")){
		            int count = ((Link)ca.get(j)).getUri().length();
		            Output.println("<Char_count>"+count+"</Char_count>");
		           // Output.println("<Value>"+((Link)ca.get(j)).getUri()+"</Value>");  //chiedere testo
		            Output.println(" <Value><![CDATA["+((Link)ca.get(j)).getUri()+"]]></Value>");
		        }else  if((ca.get(j).getClass().getName()).equals("webApplication.business.Testo")){
		            int count = ((Testo)ca.get(j)).getTesto().length();
		            Output.println("<Char_count>"+count+"</Char_count>");
		            //Output.println(" <Value><![CDATA[test]]></Value>");
		            Output.println(" <Value><![CDATA["+((Testo)ca.get(j)).getTesto()+"]]></Value>");
		            //Output.println(" <Value>"+((Testo)ca.get(j)).getTesto()+"</Value> ");
		        }
		        Output.println("</ECI>");

		    }//for
		    Output.println("</CINode>");
		}
	}

	protected static void genComposto(PrintStream Output,
			Vector<Componente> moduli, int i) throws IOException {
		int visibilita;
		int enfasi;
		{
		    Vector<ComponenteSemplice> cs=((ComponenteComposto)moduli.get(i)).getOpzioni();
		    Output.println("<CINode>");
		    Output.println("<ID>"+moduli.get(i).getNome()+"</ID>");
		    Output.println("<Type>AND</Type>");
		    printGeneric(Output, moduli, i);
  
		    for(int j=0;j<cs.size();j++){
		        Output.println("<ECI>");
		        Output.println("<ID>"+cs.get(j).getNome()+"</ID>");
		        if((cs.get(j).getClass().getName()).equals("webApplication.business.Immagine")){
		            Output.println("<Type>Image</Type>");
		        }else  if((cs.get(j).getClass().getName()).equals("webApplication.business.Link")){
		            Output.println("<Type>Link</Type>");
		        } else  if((cs.get(j).getClass().getName()).equals("webApplication.business.Testo")){
		            Output.println("<Type>Text</Type>");
		        }
		        visibilita=(int)((1-(cs.get(j).getVisibilita()/3+0.2))*100);
		        Output.println("<Necessity>0."+visibilita+"</Necessity>");
		        enfasi=(int)((1-(cs.get(j).getEnfasi()/3+0.2))*100);
		        Output.println("<Relevance>0."+enfasi+"</Relevance>");
		        Output.println("<Category>"+cs.get(j).getCategoria()+"</Category>");
		        if((cs.get(j).getClass().getName()).equals("webApplication.business.Immagine")){
		            String path=((Immagine)cs.get(j)).getPath();
		            Image immagine = ImageIO.read(new File(path));
		            Output.println("<Width>"+immagine.getWidth(null)+"</Width>");
		            Output.println("<Height>"+immagine.getHeight(null)+"</Height>");
		            //Output.println(" <Value><![CDATA[test]]></Value>");
		            int pos=path.lastIndexOf("\\");
		            path=path.substring(pos+1);
		            Output.println(" <Value><![CDATA[./image/"+path+"]]></Value>");
		        }else  if((cs.get(j).getClass().getName()).equals("webApplication.business.Link")){
		            int count = ((Link)cs.get(j)).getUri().length();
		            Output.println("<Char_count>"+count+"</Char_count>");
		            //Output.println("<Value>"+((Link)cs.get(j)).getUri()+"</Value>");  //chiedere testo
		            Output.println(" <Value><![CDATA["+((Link)cs.get(j)).getUri()+"]]></Value>");
		            //Output.println(" <Value><![CDATA[test]]></Value>");
		        } else  if((cs.get(j).getClass().getName()).equals("webApplication.business.Testo")){
		            int count = ((Testo)cs.get(j)).getTesto().length();
		            Output.println("<Char_count>"+count+"</Char_count>");
		           // Output.println(" <Value>"+((Testo)cs.get(j)).getTesto()+"</Value> ");
		            //Output.println(" <Value><![CDATA[test]]></Value>");
		            Output.println(" <Value><![CDATA["+((Testo)cs.get(j)).getTesto()+"]]></Value>");
		        }
		        Output.println("</ECI>");
		    }//for
		    Output.println("</CINode>");
		}
	}
}
