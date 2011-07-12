/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webApplication.business;

/**
 *
 * @author Landi Jessica & Miglioranzi Marco
 */
import java.util.Vector;
import java.io.*;
import javax.swing.JOptionPane;

public class Pagina implements Serializable  {

    private String titolo;
    private Vector<Componente> moduli;

    /**
     * Costruttore della pagina
     */
    public Pagina (){

        titolo="Home";
        moduli=new Vector<Componente>();

    }

    /**
     * Metodo per la modifica del titolo della pagina
     * @param t
     */
    public void setTitolo (String t){
        
        titolo=t;

    }

    /**
     *
     * @return il titolo della pagina
     */
    public String getTitolo (){
        
        return titolo;

    }

    /**
     *
     * @return l'insieme di tutti i moduli contenuti nella pagina
     */
    public Vector<Componente> getModuli(){
        return moduli;
    }

    /**
     * Metodo per l'aggiunta di un nuovo modulo all'interno della pagina
     * @param c
     */
    public void aggiungiModulo(Componente c){
        
        moduli.add(c);

    }

    /**
     * Metodo che permette l'aliminazione di un modulo all'interno della pagina
     * @param i
     */
    public void cancellaModulo(int i){

        moduli.remove(i);

    }

    /**
     * Metodo che permette di verificare se il nome del componente che si vuole inserire è univoco
     * @param c
     * @return se il nome è già presente oppure no
     */
    public boolean controlloNome(Componente c){
        boolean unico=true;
        for(int i=0;i<moduli.size();i++){ //scorre tutti i moduli presenti nella pagina
            if((moduli.get(i).getNome()).equals(c.getNome())){ //controlla il nome dei moduli esterni
                unico=false;
            }
            if((moduli.get(i).getClass().getName()).equals("webApplication.business.ComponenteAlternative")){
                for(int j=0;j<((ComponenteAlternative)moduli.get(i)).getAlternative().size();j++){ //scorre tutte le sottoalternative dei moduli di tipo ComponenteAlternativo
                    if((((ComponenteAlternative)moduli.get(i)).getAlternative().get(j).getNome()).equals(c.getNome())){ //controlla l'univocità del nome nelle sottoAlternative
                       unico=false;
                    }//if
                }//for
            }//if alternative
            if((moduli.get(i).getClass().getName()).equals("webApplication.business.ComponenteComposto")){
                Vector<ComponenteSemplice> cs=((ComponenteComposto)moduli.get(i)).getComponenti();
                for(int j=0;j<cs.size();j++){ //scorre tutti i sottocomponenti dei moduli di tipo ComponenteComposto
                    if((cs.get(j).getNome()).equals(c.getNome())){ //controlla l'univocità del nome nei sottocomponenti
                       unico=false;
                    }//if
                }//for
            }
        }
        return unico;
    }

    /**
     * Metodo che permette di verificare se il nome del componente che si vuole inserire è univoco
     * @param nome
     * @return se il nome è già presente oppure no
     */
     public boolean controlloNome(String nome){
        boolean unico=true;
        for(int i=0;i<moduli.size();i++){ //scorre tutti i moduli presenti nella pagina
            if((moduli.get(i).getNome()).equals(nome)){ //controlla il nome dei moduli esterni
                unico=false;
            }
            if((moduli.get(i).getClass().getName()).equals("webApplication.business.ComponenteAlternative")){
                for(int j=0;j<((ComponenteAlternative)moduli.get(i)).getAlternative().size();j++){ //scorre tutte le sottoalternative dei moduli di tipo ComponenteAlternativo
                    if((((ComponenteAlternative)moduli.get(i)).getAlternative().get(j).getNome()).equals(nome)){ //controlla l'univocità del nome nelle sottoAlternative
                       unico=false;
                    }//if
                }//for
            }//if alternative
            if((moduli.get(i).getClass().getName()).equals("webApplication.business.ComponenteComposto")){
                Vector<ComponenteSemplice> cs=((ComponenteComposto)moduli.get(i)).getComponenti();
                for(int j=0;j<cs.size();j++){  //scorre tutti i sottocomponenti dei moduli di tipo ComponenteComposto
                    if((cs.get(j).getNome()).equals(nome)){  //controlla l'univocità del nome nei sottocomponenti
                       unico=false;
                    }//if
                }//for
            }
        }
        return unico;
    }

     /**
      *
      * @param nome
      * @return il componenteAlternative padre del componente il cui nome corrisponde a quello cercato
      */
    public ComponenteAlternative sottoAlternative(String nome){

        ComponenteAlternative padre=null;
        for(int i=0;i<moduli.size();i++){  //scorre tutti i moduli presenti nella pagina
            if((moduli.get(i).getClass().getName()).equals("webApplication.business.ComponenteAlternative")){
                Vector<ComponenteSemplice> cs=((ComponenteAlternative)moduli.get(i)).getAlternative();
                for(int j=0;j<cs.size();j++){ //scorre tutte le sottoalternative dei moduli di tipo ComponenteAlternativo
                    if((cs.get(j).getNome()).equals(nome)){ //cerca la sottoAlternativa con nome uguale a quello cercato
                       padre=(ComponenteAlternative)moduli.get(i); //ne recupera il padre
                    }//if
                }//for
            }//if
        }//for
        return padre;
    }

    /**
     *
     * @param posizione
     * @return il componente che all'interno della pagina occupa la posizione cercata
     */
    public Componente getModulo(int posizione){
        return moduli.get(posizione);
    }

    /**
     *
     * @param n
     * @return la posizione all'interno della pagina del componente cercato
     */
    public int cercaModulo(String n){
        int c=-1;
        for(int i=0;i<moduli.size();i++){  //scorre tutti i moduli presenti nella pagina
            if((moduli.get(i).getNome()).equals(n)){  //cerca il modulo con nome uguale a quello cercato
                c=i; //salva la posizione del modulo
            }
        }
        return c;
    }

    /**
     * Metodo per la cancellazione dei vari componenti, sia semplici che composti che sottocomponenti
     * @return se l'elemento è stato cancellato con successo oppure no
     */
    public boolean cancellaModuloAll(String n, Boolean grafica){
        int app=-1;
        boolean cancellato=false;
        for(int i=0;i<moduli.size();i++){ //scorrimento di tutti i moduli della pagina
            if((moduli.get(i).getNome()).equals(n)){
                app=i;  //se l'elemento da cancellare è un modulo salvo la sua posizione nella pagina
            }
            if((moduli.get(i).getClass().getName()).equals("webApplication.business.ComponenteComposto")){
                Vector<ComponenteSemplice> cs=((ComponenteComposto)moduli.get(i)).getComponenti();
                for(int j=0;j<cs.size();j++){ //scorrimento dei sottocompponenti di nu ComponenteComposto
                    if((cs.get(j).getNome()).equals(n)){
                        cancellato=true;
                        if(cs.size()==1){ //controllo che il comopnenteComposto non abbia un solo sottoelemento
                            if(!grafica){
                                if(JOptionPane.showConfirmDialog(null,"If you delete this component is also deleted the composite component. Continue?")==0){ //nel caso non lavoro con il drag and drop viene chiesta la conferma
                                    ((ComponenteComposto)moduli.get(i)).getComponenti().remove(j); //se l'elemento è solo uno vieme eliminato
                                    app=i; // e salvata la posizione del padre
                                    cancellato=false;
                                }else{
                                    cancellato=false;
                                }
                            } else { //con il drag and drop elimino direttamente anche il padre
                                ((ComponenteComposto)moduli.get(i)).getComponenti().remove(j);
                                    app=i;
                                    cancellato=false;
                            }
                          
                        }else{ //se ho altri sottocomponenti tolgo solo quello desiderato
                            ((ComponenteComposto)moduli.get(i)).getComponenti().remove(j);
                            
                        }
                    }//if
                }//for
            }else{ //stessa cosa per i componenti di tipo Alternativo
                 if((moduli.get(i).getClass().getName()).equals("webApplication.business.ComponenteAlternative")){
                     for(int j=0;j<((ComponenteAlternative)moduli.get(i)).getAlternative().size();j++){
                        if((((ComponenteAlternative)moduli.get(i)).getAlternative().get(j).getNome()).equals(n)){
                            cancellato=true;
                            if(((ComponenteAlternative)moduli.get(i)).getAlternative().size()==1){
                                if(!grafica){
                                    if(JOptionPane.showConfirmDialog(null,"If you delete this component is also deleted the composite component. Continue?")==0){
                                        ((ComponenteAlternative)moduli.get(i)).getAlternative().remove(j);
                                        app=i;
                                        cancellato=false;
                                    }else{
                                        cancellato=false;
                                    }
                                }else {
                                    ((ComponenteAlternative)moduli.get(i)).getAlternative().remove(j);
                                    app=i;
                                    cancellato=false;
                                }
                            }else{
                                ((ComponenteAlternative)moduli.get(i)).getAlternative().remove(j);
                            }
                        }//if
                     }//for
                }//if alternative
            }//else
        }
        if(!cancellato&&app!=-1){ //nel caso in cui il componente che si voleva eliminare è un modulo viene effettuata la rimozione dell'intero modulo
            moduli.remove(app);
            cancellato=true;
        }
        return cancellato;
    }

    /**
     * Metodo per la ricerca di tutti i componenti anche figli di componenti composti o alternativi
     * @return il componente cercato
     */
    public Componente cercaModuloAll(String n){
        int app=-1;
        boolean cancellato=false;
        Componente trovato=null;
        for(int i=0;i<moduli.size();i++){ //ricerca tra i moduli della pagina
            if((moduli.get(i).getNome()).equals(n)){
                app=i; //salvo la posizione se viene trovato l'elemento cercato
            }
            if((moduli.get(i).getClass().getName()).equals("webApplication.business.ComponenteComposto")){
                Vector<ComponenteSemplice> cs=((ComponenteComposto)moduli.get(i)).getComponenti();
                for(int j=0;j<cs.size();j++){ //ricerca tra i vari sottocomponenti
                    if((cs.get(j).getNome()).equals(n)){
                        cancellato=true;
                        trovato=cs.get(j);
                    }//if
                }//for
            }else{
                 if((moduli.get(i).getClass().getName()).equals("webApplication.business.ComponenteAlternative")){
                     for(int j=0;j<((ComponenteAlternative)moduli.get(i)).getAlternative().size();j++){ //ricerca tra le varie sottoAlternative
                        if((((ComponenteAlternative)moduli.get(i)).getAlternative().get(j).getNome()).equals(n)){
                            cancellato=true;
                           trovato=((ComponenteAlternative)moduli.get(i)).getAlternative().get(j);
                         }//if
                     }//for
                }//if alternative
            }//else
        }
        if(!cancellato&&app!=-1){
            return trovato=moduli.get(app);
        }
        return trovato;
    }

}
