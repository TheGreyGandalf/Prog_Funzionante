package Struttura;

import Classe_Conto.Conto;
import Scrittura_File.ScritturaFile;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;  //Per capire se le date sono valide
import java.text.ParseException;    //Per errori di conversione

import static javax.swing.JOptionPane.*;


public class tab extends DefaultTableModel {

    private DefaultTableModel tm ;
    private ArrayList<Conto> arrayConto;
    private JTable tabella;
    //private Vector v;

    /**
     *
     * @param C Array che contiene la lista degli oggetti da inserire
     */
    public tab(ArrayList<Conto> C) {
        //this.v = v; // inizializzato con il vettore
        arrayConto=C;

        settaValori();
    }

    public tab() {

    }


/**
 * numero righe = dimensione del vettore
 */
    @Override
    public int getRowCount() {
        if(arrayConto == null) return 0;
        return arrayConto.size();
        }


    /**
     * @param row the row whose value is to be queried
     * @param col the column whose value is to be queried
     * @return
     */

    @Override
    public Object getValueAt(int row, int col) {
        // seleziona il libro
        switch (col)
        {
            case 0: return arrayConto.get(row).getData();
            case 1: return arrayConto.get(row).getDescrizione();
            case 2: return arrayConto.get(row).getAmmontare();
        }
        return "";
    }

    /**
     * Metodo che aggiunge UNA sola riga per l'aggiunta e in diretta aggiunge il record
     */
    public void Cambia() {
        String Dat, Desc;
        int ammo;
        int max = arrayConto.size()-1;
        DefaultTableModel tavolam= tm;
        JTable table = new JTable(tm);
        Dat=arrayConto.get(max).getData();
        Desc=arrayConto.get(max).getDescrizione();
        ammo=arrayConto.get(max).getAmmontare();
        tavolam.addRow(new Object[]{Dat, Desc, ammo});
        fireTableDataChanged();
    }

    /**
     * Metodo che Assegna valori quando viene richiesto ed inserisce nuove righe con del
     * valore da far vedere
     */
    public void settaValori() {
        String Dat, Desc;
        int ammo;
        tm= new DefaultTableModel();
        JTable table = new JTable(tm);
        tm.addColumn(getColumnName(0));
        tm.addColumn(getColumnName(1));
        tm.addColumn(getColumnName(2));

        for (int i = 0; i < arrayConto.size(); i++) {
            Dat=arrayConto.get(i).getData();
            Desc=arrayConto.get(i).getDescrizione();
            ammo=arrayConto.get(i).getAmmontare();
            tm.addRow(new Object[]{Dat, Desc, ammo});
        }
    }

    public void RiSetta(ArrayList<Conto> c)
    {
        arrayConto=c;
        settaValori();
    }

    public void importTable(String s,ArrayList<Conto> c)
    {
        c.clear();
        File f= new File("");
        String Dat, Desc;
        int ammo;
        try{
            BufferedReader br=new BufferedReader(new FileReader(f.getAbsolutePath()+f.separator+ s));
            while((Dat=br.readLine())!=null)
            {
                Desc=br.readLine();
                ammo= Integer.parseInt(br.readLine());
                Conto con=new Conto(Dat,Desc,ammo);
                c.add(con);
            }
            br.close();
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "File not found", "Errore", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
        //settaValori();
    }

    /**
     *
     * @param value          Il valore "oggetto" che è stato inserito
     * @param row             Riga in cui è inserito
     * @param col          Colonna in cui è inserito
     */
    public void setValueAt(Object value, int row, int col) {
        Conto contello = arrayConto.get(row);

        //non so come mettere if else
        boolean toggle =true;

        contello.Data= value.toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse((contello.Data).trim());
        } catch (ParseException e) {  
            showMessageDialog(null, "Data non valida", "Errore", ERROR_MESSAGE);
            toggle=false;
            return;
        }

      if(!toggle)
      {
        if (col == 0) {
        contello.Data= value.toString();

            arrayConto.set(row, contello);
        }
        if(col==1){
            contello.Descrizione= value.toString();
            //arrayConto.get(row).setDescrizione(value.toString());
            arrayConto.set(row, contello);
        }
        if(col==2){
            contello.Ammontare= Integer.parseInt(value.toString());
            //arrayConto.get(row).setAmmontare(Integer.parseInt(value.toString()));
            arrayConto.set(row, contello);
        }

        ScritturaFile s = new ScritturaFile();
        s.ScriviNormale("Struttura/dati.txt", arrayConto, false);

        fireTableDataChanged();
      }
    }

    /**
     *
     * @param col  La colonna che necessita di nome
     * @return Ritorna i vari valori della intestazione
     */
    public String getColumnName(int col) {
        switch (col)
        {
            case 0: return "Data";
            case 1: return "Descrizione";
            case 2: return "Ammontare";
        }
        return "";
    }

    /**
     *
     * Funzione che assegna il numero di colonne che sono necessare alla tabella per visualizzare i dati richiesti
     */
    @Override
    public int getColumnCount() {
        return 3;
    }


   @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

}
