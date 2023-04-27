package Scrittura_File;

import Classe_Conto.Conto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ScriviCsv extends ScritturaFile {
    public void ScriviNormale(String Nomef, ArrayList<Conto> lista, boolean tipo)
    {
        try {
            File file = new File(Nomef+".csv");            //Apro il file passato
            FileWriter fw = new FileWriter(file, tipo);         //passo file e se scrivo in append
            pw = new PrintWriter(fw);               //Strumento con cui vado a scrivere su file

            for (int i=0;i<lista.size();i++)            //Scrittura su file di tutto il contenuto
            {
                String Dat, Desc;
                int ammo;
                Dat=lista.get(i).getData();
                Desc=lista.get(i).getDescrizione();
                ammo=lista.get(i).getAmmontare();
                pw.println(Dat+","+Desc+","+ammo);              //Scrittura con dati separati da ,
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