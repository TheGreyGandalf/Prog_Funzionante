import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class tab extends DefaultTableModel {
    private ArrayList<Conto> arrayConto;
    //private Vector v;

    /**
     *
     * @param C Array che contiene la lista degli oggetti da inserire
     */
    public tab(ArrayList<Conto> C) {
        //this.v = v; // inizializzato con il vettore
        arrayConto=C;
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
     *
     * @param row        the row whose value is to be queried
     * @param col     the column whose value is to be queried
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
     *
     * @param value          Il valore che è stato inserito
     * @param row             Riga in cui è inserito
     * @param col          Colonna in cui è inserito
     */
    public void setValueAt(Conto value, int row, int col) {                //NON FUNZIONANTE
        //Book b = (Book)v.elementAt(row);
        //Conto[] c = new Conto[1];
        ArrayList<Conto> arrConto = new ArrayList<>(1);
        arrayConto.set(row, value);


        /*if (col ==1)
            arrConto.set(row,c).setData(value);
        if (col == 2)
        // modifica la quantita'
            c[row].Descrizione = ((String)value);
        if (col == 3)
        // modifica il prezzo
            c[row].Ammontare = ((int)value);*/
        // notifica il cambiamento
        //fireTableDataChanged();
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
    public boolean isCellEditable(/*Object valore,*/ int row, int col) {
        // celle editabili


        /*if (col == 2)
            b.Data = ((Integer)valore).intValue();
        if (col == 3)
            b.price = ((Float)valore).floatValue();*/

        //fireTableDataChanged();
        return true;
    }

}