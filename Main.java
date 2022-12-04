import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame f = new JFrame("Programma Esame Oggetti");
        File fil= new File("dati.txt");                     //file da cui leggiami i dati
        //BufferedReader leggi=new BufferedReader(new FileReader(fil));
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
            //System.out.println("Valor di j"+j);
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
        //leggi.close();
    }
}