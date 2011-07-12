/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webApplication.business;

import java.io.*;

/**
 *
 * @author Landi Jessica & Miglioranzi Marco
 */
public class Link extends ComponenteSemplice implements Serializable {
    
    private String uri;
    private String testo;

    /**
     *  Costruttore del componente semplice di tipo Link
     */
     public Link(String n, String c, int v, int e, String u, String t ){

            super(n,c,v,e);
            uri=u;
            testo=t;
     }

     /**
      * Metodo per la modifica dell'URI del link
      * @param u
      */
    public void setUri(String u){
        uri=u;
    }

    /**
     *
     * @return l'URI del link
     */
    public String getUri(){
        return uri;
    }

    /**
     * Metodo per la modifica del testo del link
     * @param t
     */
    public void setTesto(String t){
        testo=t;
    }

    /**
     *
     * @return il testo del link
     */
    public String getTesto(){
        return testo;
    }

    }
