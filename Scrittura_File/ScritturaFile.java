package Scrittura_File;

import Classe_Conto.Conto;

import java.io.*;
import java.util.ArrayList;

/*import jxl.Workbook;                    //librearie per la scrittura in Excel
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;*/

import javax.swing.*;
import javax.swing.table.TableModel;

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
     */
    public void ScriviNormale(String Nomef, ArrayList<Conto> lista, String separatore, boolean tipo) {
        try {
            File file = new File(Nomef);            //Apro il file passato
            FileWriter fw = new FileWriter(file, tipo);         //passo file e se scrivo in append
            pw = new PrintWriter(fw);               //Strumento con cui vado a scrivere su file

            for (int i=0;i<lista.size();i++)            //Scrttura su file di tutto il contenuto
            {
                String Dat, Desc;
                int ammo;
                Dat=lista.get(i).getData();
                Desc=lista.get(i).getDescrizione();
                ammo=lista.get(i).getAmmontare();
                pw.println(Dat+separatore+Desc+separatore+ammo);
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

    /**
     * Metodo per l'esportazione da JFrame a .xls
     */
    /*static class fillData extends Scrittura_File.ScritturaFile{
    JTable t;
    File f;

    fillData(JTable table, File file)
        {
            t=table;
            f=file;
        }

        public void scriv(){
            try {

                WritableWorkbook workbook1 = Workbook.createWorkbook(f);
                WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);
                TableModel model = t.getModel();

                for (int i = 0; i < model.getColumnCount(); i++) {
                    Label column = new Label(i, 0, model.getColumnName(i));
                    sheet1.addCell(column);
                }
                int j = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (j = 0; j < model.getColumnCount(); j++) {
                        Label row = new Label(j, i + 1, model.getValueAt(i, j).toString());
                        sheet1.addCell(row);
                    }
                }
                workbook1.write();
                workbook1.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }*/
}
