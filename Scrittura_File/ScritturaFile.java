package Scrittura_File;

import Classe_Conto.Conto;
import Struttura.tab;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.*;

/*
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import jxl.Workbook;                    //librearie per la scrittura in Excel
import jxl.write.Label;
*/


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

            for (int i=0;i<lista.size();i++)            //Scrittura su file di tutto il contenuto
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

    private void LeggiFile(File f){

    }

    /**
     *
     * @param table  La tabella che si Vuole salvare
     * @param path   Il percorso che si deve compiere per il file
     * @throws FileNotFoundException   Errore in caso il file non esista
     * @throws IOException Errore in caso si inserisce una carattere non valido
     */

    /*public void writeToExcel(JTable table, String path) throws FileNotFoundException, IOException {
        //new WorkbookFactory();
        Workbook wb = new XSSFWorkbook(); //Excel workbook
        Sheet sheet = wb.createSheet(); //WorkSheet
        Row row = sheet.createRow(2); //Row created at line 3
        TableModel model = table.getModel(); //Table model

        Row headerRow = sheet.createRow(0); //Create row at line 0
        for(int headings = 0; headings < model.getColumnCount(); headings++){ //For each column
            headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
        }

        for(int rows = 0; rows < model.getRowCount(); rows++){ //For each table row
            for(int cols = 0; cols < table.getColumnCount(); cols++){ //For each table column
                row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString()); //Write value
            }

            //Set the row to the next one in the sequence
            row = sheet.createRow((rows + 3));
        }
        wb.write(new FileOutputStream(path.toString()));//Save the file
    }*/

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
     * Metodo per l'esportazione da JFrame a .xls
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


        /*                  ClASSE PER SCRITTURA SU EXCEL
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
    }

}
         */
