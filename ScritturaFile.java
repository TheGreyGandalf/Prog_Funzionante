import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
/*
import com.aspose.cells.License;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

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
}

class Scritt extends ScritturaFile{


}
