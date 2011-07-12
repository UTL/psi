/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webApplication.business;

/**
 *
 * @author Landi Jessica & Miglioranzi Marco
 */
import java.io.Serializable;
import java.util.Vector;

public class ComponenteComposto extends Componente implements Serializable {

    private Vector<ComponenteSemplice> componenti;

    /**
     *  Creazione di un componente composto
     */
    public ComponenteComposto(String n,String c,int v,int e){

        super(n,c,v,e);
        componenti=new Vector<ComponenteSemplice>();

    }

    /**
     * Metodo che permette l'aggiunta dei sottocomponenti del componente composto
     * @param c
     */
     public void aggiungiComponenteS(ComponenteSemplice c){

         componenti.add(c);

     }

     /**
      *
      * @return il vettore dei sottocomponenti semplici del componente composto
      */
    public Vector<ComponenteSemplice> getComponenti(){

        return componenti;

    }

    /**
     * Metodo che restituisce uno specifico sotto componente del componente composto
     * @param posizione
     * @return
     */
    public ComponenteSemplice getComponente(int posizione){

        return componenti.get(posizione);

    }

    /**
     * Metodo che permette di modificare il vettore di sottocomponenti del componente composto
     * @param cs
     */
    public void setComponenti(Vector<ComponenteSemplice> cs){

        componenti=cs;

    }

    /**
     * Metodo che permette di cancellare un sottocomponente del componente composto
     * @param i
     */
    public void cancellaComponenteS(int i){

        componenti.remove(i);

    }

    
}
