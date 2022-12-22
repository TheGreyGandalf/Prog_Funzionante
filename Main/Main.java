package Main;

import Classe_Conto.*;
import Scrittura_File.ScritturaFile;
import Struttura.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame f = new JFrame("Programma Esame Oggetti");
        File fil= new File("Struttura/dati.txt");                     //file da cui leggiamo i dati
        //BufferedReader leggi=new BufferedReader(new FileReader(fil));
        //JOptionPane.showMessageDialog(null, "This is an alert box.");
        Scanner scan =new Scanner((fil));

        //variabili per inserire i valori nell'array
        String Dat, Desc;
        int ammo;

        ArrayList<Conto> arr =new ArrayList();

        while(scan.hasNextLine())
        {
            Dat= scan.nextLine();
            Desc= scan.nextLine();
            ammo= Integer.parseInt(scan.nextLine());
            Conto ogg= new Conto(Dat, Desc, ammo);
            arr.add(ogg);
        }

        MyPanel panel = new MyPanel(arr);
        f.add(panel);
        f.pack();
        //f.add(new JScrollPane());
        //f.setBounds(300,300, 300,300);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);

        f.setVisible(true);
        //chiusura file di testo
        scan.close();
        
    }
}