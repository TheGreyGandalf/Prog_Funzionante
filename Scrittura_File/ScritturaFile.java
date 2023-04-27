package Scrittura_File;

import Classe_Conto.Conto;
import Struttura.tab;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.*;


/**
 * Classe che implementa i metodi di una scrittura su file, qui verrà anche implementata
 * la scrittura in formato corretto con i ; e se possibile importabile in altre applicazioni
 */
public class ScritturaFile {
    PrintWriter pw = null;                     //file da cui leggiamo i dati

    //BufferedReader leggi=new BufferedReader(new FileReader(fil));

    /**
     *
     * @param Nomef Nome del file in cui andremo a esportare
     * @param lista Lista con aggiunta di voci a cui andremo a scrivere
     * @param tipo Se il file viene aperto e utilizzato in append
     */
    public void ScriviNormale(String Nomef, ArrayList<Conto> lista, boolean tipo) {
        try {
            File file = new File(Nomef+".txt");            //Apro il file passato
            FileWriter fw = new FileWriter(file, tipo);         //passo file e se scrivo in append
            pw = new PrintWriter(fw);               //Strumento con cui vado a scrivere su file

            for (int i=0;i<lista.size();i++)            //Scrittura su file di tutto il contenuto
            {
                String Dat, Desc;
                int ammo;
                Dat=lista.get(i).getData();
                Desc=lista.get(i).getDescrizione();
                ammo=lista.get(i).getAmmontare();
                pw.println(Dat+"\n"+Desc+"\n"+ammo);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (RuntimeException e) {

        } finally
        {
            if (pw != null) {
                pw.close();
            }
        }
    }

    public ArrayList<Conto> LeggiFile(File f, ArrayList<Conto> a){
        Scanner scan = null;
        try {
            scan = new Scanner((f));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        //variabili per inserire i valori nell array
        String Dat, Desc;
        int ammo;

        while(scan.hasNextLine())
        {
            Dat= scan.nextLine();
            Desc= scan.nextLine();
            ammo= Integer.parseInt(scan.nextLine());
            Conto ogg= new Conto(Dat, Desc, ammo);
            a.add(ogg);
        }
        scan.close();
        return a;

    }

    /**
     * @param f Varviabile file del file in cui andiamo a scrivere
     * @param Valori Arraylist con valori che andiamo a scrivere
     */

    public void OpenDoc(File f, ArrayList<Conto> Valori)
    {
        // Create the data to save.
        final Object[][] data = new Object[Valori.size()][3];
        for (int i = 0; i < Valori.size(); i++) {
            data[i]= new Object[] {Valori.get(i).getData(), Valori.get(i).getDescrizione(), Valori.get(i).getAmmontare()};
        }

        String[] columns = new String[] {"Data", "Descrizione", "Ammontare"};

        TableModel model = new DefaultTableModel(data, columns);

        // Save the data to an ODS file and open it.
        try {
            SpreadSheet.createEmpty(model).saveAs(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            OOUtils.open(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo per l'esportazione da JFrame a .ods
     */
    public static class fillData extends Scrittura_File.ScritturaFile {
        JTable t;
        File f;

        public fillData(JTable table, File file) {
            t = table;
            f = file;
        }
    }

}

