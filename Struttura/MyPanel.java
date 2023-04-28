package Struttura;

import Classe_Conto.Conto;                  //classi dei package
import Scrittura_File.ScritturaFile;
import Scrittura_File.ScriviCsv;
import Struttura.CalcolaEntrate;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.SimpleDateFormat;  //Per capire se le date sono valide
import java.text.ParseException;    //Per errori di conversione

import static javax.swing.JOptionPane.*;


import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Classe principale che governa gran parte dell'applicazione,
 * Implementa Action Listener
 */

public class MyPanel extends JPanel implements ActionListener {

        private final JButton b,nuova,elimina,ButExcel,ButCsv,ButTxt,Importa;

        private ArrayList<Conto> lista, copia, NuovaL, Aggiustata;
        //Lista copia dopo la selezione di un periodo
        //Lista dopo una modifica al periodo
        //Lista dopo aggiustamento periodo ricerca

        private final JTextField txt,txt2,EtiExcel,CampoNetto;  //Campo calcolato in cui si inserisce il totale di entrate

        private final JTextField periodo_1,periodo_2;     //campi per inserimento periodo
        private final JButton Giorno, Settimana, Mese, Anno, Periodo, Prossimo, Reset;
        //pulsanti per vari tipi di raggruppamenti

        private final JTable t;
        private final tab tm;

        private ScritturaFile Onfile;

        private int flag=0;
        private int el=-1;               //Elemento che contiene il primo match
        private File Nome_File;            //Attributo che contiene il nome del file da cui andiamo a salvare/leggere

    /**
     *
     * @param listaConto= La lista che contiene i dati che si andranno a leggere da stream di File
     * Gestisce tutti i bottoni e i metodi collegati ad essi
     */

    public MyPanel(ArrayList<Conto> listaConto, File file) {
            Onfile = new ScritturaFile();
            Nome_File=file;
            lista=listaConto;

            // crea la tabella
            tm = new tab(lista);
            //tm.settaValori(lista.size(), tm.getColumnCount());//t.setModel(tm);

            t = new JTable(tm);   // aggiunge la tabella al pannello
            //t.add(new JScrollPane());
            tm.settaValori();               //metodo per assegnare glo oggetti alla tabella

            this.setLayout(new BorderLayout());

            //PRIMO pannello
            JPanel pTab = new JPanel();                 //pannello con tabella e header
        pTab.setLayout(new BorderLayout());         //pTab ha il primo layout

            pTab.add(t.getTableHeader(), BorderLayout.NORTH);       //Header tabella a NORD
            pTab.add(t, BorderLayout.CENTER);                       //Tabella a CENTER
                        //PannelloCalcolo.setLayout(new GridLayout());
            CalcolaEntrate ca = new CalcolaEntrate(lista);
            CampoNetto = new JTextField("Calcolo");
            CampoNetto.setText(String.valueOf(ca.calcolatore()));
            CampoNetto.setEditable(false);
            pTab.add(CampoNetto, BorderLayout.SOUTH);
            this.add(pTab, BorderLayout.NORTH);                 //Il tutto a NORD


            //SECONDO pannello
            JPanel PanDate = new JPanel();      //pannello con campi per ricerca periodo

            /**
             * Pannello con ricerca per data
             */
            PanDate.setLayout(new BorderLayout());
            periodo_1 = new JTextField("Periodo Ricerca", 10);
            periodo_2 = new JTextField("Periodo Fine", 10);
            PanDate.add(periodo_1, BorderLayout.WEST);
            PanDate.add(periodo_2, BorderLayout.CENTER);

            JPanel panGioBott = new JPanel();
            panGioBott.setLayout(new BorderLayout());
            Giorno=new JButton("Ricerca per Giorno");
            Settimana =new JButton("Ricerca per Settimana");
            Mese= new JButton("Ricerca per Mese");
            Anno = new JButton("Ricerca per Anno");
            Periodo=new JButton("Ricerca per Periodo arbitrario");

            PanDate.add(Giorno, BorderLayout.NORTH);
            Giorno.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Aggiustata=perio(periodo_1.getText(), "", 1);     //passo le 2 date e quanti giorni
                    modificatore();
                }
            });
            panGioBott.add(Settimana, BorderLayout.WEST);
            Settimana.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Aggiustata=perio(periodo_1.getText(), "", 7);     //passo le 2 date e quanti giorni
                    modificatore();
                }
            });
            panGioBott.add(Mese, BorderLayout.CENTER);
            Mese.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Aggiustata=perio(periodo_1.getText(), "", 30);     //passo le 2 date e quanti giorni
                    modificatore();
                }
            });
            panGioBott.add(Anno, BorderLayout.EAST);
            Anno.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Aggiustata=perio(periodo_1.getText(), "", 365);     //passo le 2 date e quanti giorni
                    modificatore();
                }
            });
            panGioBott.add(Periodo, BorderLayout.SOUTH);
            Periodo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Aggiustata=perio(periodo_1.getText(), periodo_2.getText(), 0);     //passo le 2 date e quanti giorni
                    modificatore();
                }
            });
            
            PanDate.add(panGioBott,BorderLayout.EAST);

            this.add(PanDate, BorderLayout.EAST);               //Il tutto aggiunto ad EST

            /**
            * Pannello con Ricerca per carattere
            */

            //TERZO pannello
            JPanel pTab2 = new JPanel();                //secondo pannello con altri bottoni
            pTab2.setLayout(new BorderLayout());

            txt = new JTextField("Inserire Match ricerca", 25);
            pTab2.add(txt, BorderLayout.CENTER);

            txt2 = new JTextField("Risultato Match ricerca", 25);                  //Campo risultato ricerca
            txt2.setEditable(false);
            pTab2.add(txt2, BorderLayout.EAST);
            
            b= new JButton("Cerca Un Utente");
            Prossimo= new JButton("Prossimo Match");
            b.addActionListener(new ActionListener(){                   //Bottone che fa partire la ricerca da una stringa
                /**
                 * @param e Evento in caso si cercasse un campo all'interno dei campi salvati
                 */
                public void actionPerformed(ActionEvent e)
                {
                    el =ricerca(el);
                }               //Quando viene cliccato il pulsante ricerca è presente un astion listener apposito
                });
            Prossimo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RicercaSuccessivo(el);
                }
            });

            pTab2.add(b, BorderLayout.NORTH);
            pTab2.add(Prossimo, BorderLayout.WEST);
            //this.add(p, BorderLayout.CENTER);
            this.add(pTab2,BorderLayout.CENTER);                          //Ricerche aggiunte in CENTER

            //pulsante di aggiunta di un nuovo record

            //TERZO pannello
            JPanel pTab3 = new JPanel();                //secondo pannello con altri bottoni
            pTab3.setLayout(new BorderLayout());

            nuova= new JButton("Nuova riga");
            nuova.addActionListener(new ActionListener() {
                /**
                 *
                 * @param e, Pannello che compare quando l'utente clicca nuovo Utente, NON toccare!!
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    class MyPanel2 extends JPanel{
                        private ArrayList<Conto> listaPassata;               //Lista che andremo ad utilizzare
                        private final JButton Aggiungi;                   //bottone per aggiunta di valori a tabella

                        private final JTextField campo1, campo2, campo3;
                        private final JLabel et1, et2, et3;
                        /**
                         *
                         * Pannello con 3 etichette per l'aggiunta di nuovi dati
                         */

                        public MyPanel2(JFrame f) {
                            //listaPassata=Lista;
                            JPanel pannelloInterno = new JPanel();
                            pannelloInterno.setLayout(new BorderLayout());

                            JPanel PanBottoni = new JPanel();
                            JPanel PanEtic = new JPanel();
                            PanBottoni.setLayout(new BorderLayout());
                            PanEtic.setLayout(new BorderLayout());
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            LocalDateTime now = LocalDateTime.now();
                            String odierno=dtf.format(now);
                            campo1= new JTextField(odierno, 15);                     //3 etichette in cui metto i nuovi campi
                            campo2= new JTextField(15);
                            campo3= new JTextField(15);
                            et1= new JLabel("Data");
                            et2= new JLabel("Descrizione");
                            et3= new JLabel("Ammontare");
                            PanEtic.add(et1, BorderLayout.NORTH);
                            PanBottoni.add(campo1, BorderLayout.NORTH);
                            PanEtic.add(et2, BorderLayout.CENTER);
                            PanBottoni.add(campo2, BorderLayout.CENTER);
                            PanEtic.add(et3, BorderLayout.SOUTH);
                            PanBottoni.add(campo3, BorderLayout.SOUTH);

                            this.add(PanEtic, BorderLayout.EAST);
                            this.add(PanBottoni, BorderLayout.CENTER);


                            Aggiungi= new JButton("Aggiungi record a Tabella");         //bottone che va ad aggiungere
                            pannelloInterno.add(Aggiungi,BorderLayout.SOUTH);
                            Aggiungi.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String c1 = campo1.getText();
                                    String c2 = campo2.getText();
                                    String c3 = campo3.getText();
                                    /**
                                     *  Controllo se la data inserita è una data valida
                                     */
                                    if(!isValidDate(c1)){
                                      showMessageDialog(null, "Data non valida", "Errore", ERROR_MESSAGE);
                                      f.dispose();
                                      t.repaint();
                                    }
                                    else{
                                    Conto ogg= new Conto(c1, c2, Integer.parseInt(c3));
                                    lista.add(ogg);                             //Aggiungo informazioni alla lista

                                    CalcolaEntrate ca = new CalcolaEntrate(lista);
                                    CampoNetto.setText(String.valueOf(ca.calcolatore()));
                                    CampoNetto.repaint();

                                    f.dispose();
                                    t.repaint();
                                    //tm.addRow((new Object[]{c1, c2, Integer.parseInt(c3)}));
                                    tm.Cambia();
                                    //String Utente = EtiExcel.getText();   Da usare!
                                    ScritturaFile sc = new ScritturaFile();
                                    sc.ScriviNormale(Nome_File.toString(), lista, false);      //scrivo su file le modifiche
                                    //le scrivo su file
                                    tm.settaValori();
                                    t.repaint();
                                    }

                                    //t.invalidate();
                                }
                            });
                            this.add(pannelloInterno, BorderLayout.SOUTH);
                        }

                }
                    /**
                     * Azione che compie il Secondo pannello, il richiamo del pannello di aggiunta
                     */
                    JFrame f = new JFrame("Inserire nuovo Utente");
                    /*ArrayList<Classe_Conto.Conto> Listanuovo = new ArrayList<>(1);
                    Classe_Conto.Conto Con= new Classe_Conto.Conto("","",0);
                    Listanuovo.add(Con);*/
                    MyPanel2 panel = new MyPanel2(f);
                    f.add(panel);
                    f.pack();
                    f.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    f.setLocationRelativeTo(null);
                    f.setVisible(true);

                }
            });
            pTab3.add(nuova, BorderLayout.EAST);


            elimina= new JButton("Elimina riga");
            elimina.addActionListener(new ActionListener() {
                /**
                 *
                 * @param e = Evento in caso si voglia eliminare dalla tabella un record selezionato
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    ScritturaFile scr= new ScritturaFile();
                    int i = t.getSelectedRow();
                    lista.remove(i);                        //elimino elemento selezionato
                    //String Utente = EtiExcel.getText();    //da usare
                    scr.ScriviNormale(Nome_File.toString(), lista, false);


                    CalcolaEntrate ca = new CalcolaEntrate(lista);
                    CampoNetto.setText(String.valueOf(ca.calcolatore()));
                    CampoNetto.repaint();


                    t.repaint();
                    tm.settaValori();
                    //tm.Cambia();
                    t.repaint();
                    //t.invalidate();
                }
            });
            pTab3.add(elimina, BorderLayout.SOUTH);

            Reset = new JButton("Reset tabella");
            pTab3.add(Reset, BorderLayout.NORTH);
            Reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resettatore();
                }
            });

            this.add(pTab3, BorderLayout.WEST);                                             //IL tutto aggiunto a WEST

        /**
         * Pannello Esportazioni
         */
            //QUARTO Pannello
            JPanel export = new JPanel();
            ButExcel = new JButton("Esporta in formato OpenDocument");
            export.setLayout(new BorderLayout());
            export.add(ButExcel, BorderLayout.NORTH);
            EtiExcel = new JTextField("Nome File Export/Import", 10);
            export.add(EtiExcel, BorderLayout.CENTER);
            ButExcel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String Utente = EtiExcel.getText();
                    File fif = new File(Utente+".ods");
                    ScritturaFile.fillData OnOpe = new ScritturaFile.fillData(t, fif);
                    OnOpe.OpenDoc(fif, lista);          //scrittura su open document
                }
            });
            
            ButCsv = new JButton("Esporta in formato CSV");
            export.add(ButCsv, BorderLayout.EAST);
            ButCsv.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String Utente = EtiExcel.getText();
                    File Ut = new File(Utente+".csv");
                    Onfile = new ScriviCsv();                //Polimorfismo utilizzato con successo
                    if(Ut.exists() || Ut.isDirectory()) {
                        if((JOptionPane.showConfirmDialog(null,"File già esistente, sovrascrivere?", "Attenzione", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION))
                        {
                            //Scrittura anche in caso il file sia già esistente
                            Onfile.ScriviNormale(Utente, lista, false);
                        }
                    }
                    else
                    {
                        Onfile.ScriviNormale(Utente, lista, false);
                    }
                }
            });

            ButTxt = new JButton("Esporta in formato Txt");
            Importa = new JButton("Importa da File");
 
            ButTxt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String Utente = EtiExcel.getText();
                    File Ut = new File(Utente+".txt");
                    //System.out.println(file.getName());
                    Onfile = new ScritturaFile();           //Reset del tipo per polimorfismo
                    if(Ut.exists() || Ut.isDirectory()) {
                        if((JOptionPane.showConfirmDialog(null,"File già esistente, sovrascrivere?", "Attenzione", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION))
                        {
                            //Scrittura anche in caso il file sia già esistente
                            Onfile.ScriviNormale(Utente, lista, false);
                        }
                    }
                    else
                    {
                        Onfile.ScriviNormale(Utente, lista, false);
                    }
                }
            });

            Importa.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    //File fil= new File(EtiExcel.getText());

                    JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView());

                    // invoke the showsOpenDialog function to show the save dialog
                    int r = j.showOpenDialog(null);
                    String posizione = null;

                    // if the user selects a file
                    if (r == JFileChooser.APPROVE_OPTION)
                    {
                        // set the label to the path of the selected file
                        posizione=j.getSelectedFile().getAbsolutePath();
                    }
                    // if the user cancelled the operation

                    File fil= new File(posizione);                     //file da cui leggiamo i dati
                    Scanner scan = null;
                    Nome_File=fil;                                          //////////////!!!!!!!!!!!!!!!
                    try {
                        scan = new Scanner((fil));
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                    //variabili per inserire i valori nell array
                    String Dat, Desc;
                    int ammo;
                    lista.clear();
                    while(scan.hasNextLine())
                    {
                        Dat= scan.nextLine();
                        Desc= scan.nextLine();
                        ammo= Integer.parseInt(scan.nextLine());
                        Conto ogg= new Conto(Dat, Desc, ammo);
                        lista.add(ogg);
                    }
                    /**************************************************/
                    CalcolaEntrate ca = new CalcolaEntrate(lista);
                    CampoNetto.setText(String.valueOf(ca.calcolatore()));
                    CampoNetto.repaint();
                    /**************************************************/

                    tm.Cambia();
                    t.repaint();
                    tm.settaValori();
                    //tm.Cambia();
                    t.repaint();

                }
            });
            export.add(ButTxt, BorderLayout.WEST);
            export.add(Importa, BorderLayout.SOUTH);
            this.add(export, BorderLayout.SOUTH);                               //Il tutto a SOUTH

    }

    /**
     *
     * @param gg I giorni da sottrarre
     * @param passato1 Periodo di inizio
     * @param passato2 Periodo di fine
     * @return Array con periodi aggiustati
     */
    public ArrayList<Conto> sottrai(int gg, LocalDate passato1, LocalDate passato2){
        //proviamo ad ignorare periodo2, facciamo a piccoli passi
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ArrayList<Conto> Listaperiodo=new ArrayList<Conto>();              //Lista che conterrà il periodo effettivo interessato

        LocalDate PeriodoSottratto;
        LocalDate per;
        LocalDate per2;
            //PeriodoSottratto.format(dtf);

        switch (gg) {
            case (1) -> {
                for (Conto c : lista) {
                    String conv = passato1.format(dtf);
                    if (c.getData().equals(conv)) {
                        Listaperiodo.add(c);
                    }
                }
            }
            case (7) -> {
                PeriodoSottratto = passato1.minus(Period.ofDays(7));
                for (Conto c : lista) {
                    per = LocalDate.parse(c.getData(), dtf); //traduzione a tipo periodo
                    if (ControlloDate(PeriodoSottratto, per, 7)) {
                        Listaperiodo.add(c);
                    }
                }
            }
            case (30) -> {
                PeriodoSottratto = passato1.minus(Period.ofDays(30));
                for (Conto c : lista) {
                    per = LocalDate.parse(c.getData(), dtf); //traduzione a tipo periodo
                    if (ControlloDate(PeriodoSottratto, per, 30)) {
                        System.out.println(c.getData() + ";" + c.getDescrizione());
                        Listaperiodo.add(c);
                    }
                }
            }
            case (365) -> {
                PeriodoSottratto = passato1.minus(Period.ofDays(365));
                for (Conto c : lista) {
                    per = LocalDate.parse(c.getData(), dtf); //traduzione a tipo periodo
                    if (ControlloDate(PeriodoSottratto, per, 365)) {
                        Listaperiodo.add(c);
                    }
                }
            }
            case (0) -> {                //da fare per punti bonus!
                for (Conto c : lista) {
                    per = LocalDate.parse(c.getData(), dtf); //Periodo inzio
                    per2 = LocalDate.parse(c.getData(), dtf); //Periodo fine
                    /*if () {
                        Listaperiodo.add(c);
                    }*/
                }
            }
            default -> {
            }
        }

        /************************/

        CalcolaEntrate ca = new CalcolaEntrate(Listaperiodo);
        CampoNetto.setText(String.valueOf(ca.calcolatore()));
        CampoNetto.repaint();
        /************************/

        return Listaperiodo;                    //ritorno la lista aggiustata con i dati di interesse
    }

    /**
     * @param Primo   Primo parametro che specifica Inizio periodo
     * @param Secondo Secondo parametro che specifica quando termina il periodo
     * @param gg      Il periodo di tempo in giorni che si vuole vedere
     * @return
     */

    public ArrayList<Conto> perio(String Primo, String Secondo, int gg){
        //tm.settaValori();
        //t.repaint();
        ArrayList<Conto> Listaperiodo;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate now = LocalDate.now();
        now.format(dtf);
        LocalDate passato1 = LocalDate.parse(Primo, dtf);
        //LocalDate passato2 = LocalDate.parse(Secondo, dtf);
        ArrayList<Conto> rit;
        LocalDate nullo = null;

        if (passato1.isAfter(now)) {
            JOptionPane.showMessageDialog(null, "Back to the future?");
            return null;
        }

        if(gg!=0)
        {
            rit=sottrai(gg, passato1, nullo);
        }
        else
        {
            if ((Secondo.equals("Periodo Fine") || Secondo.isEmpty() || Primo.equals("Periodo Inizio") || (Primo.isEmpty()))) {
                JOptionPane.showMessageDialog(null, "Inserire secondo Periodo");
                return null;
            }
            LocalDate passato2 = LocalDate.parse(Secondo, dtf);
            if (passato2.isAfter(now)) {
                JOptionPane.showMessageDialog(null, "Back to the future?");
                return null;
            }
            else
            {
                rit=sottrai(gg, passato1, passato2);
            }
        }

        return rit;
    }

    /**
     * Funzione che preleva testo dal primo campo e va a ricercare corrispondenze
     * È iniziata una nuova ricerca, si azzerano le ricerche precedenti
     * @param v Indice del prossimo elemento da cui cercare
     */
    public int ricerca(int v)
    {
        int i=0;
        Boolean check=false;
        String key = this.txt.getText();
        if(key.equals(""))
        {
            showMessageDialog(null, "Inserire una chiave di ricerca valida", "Errore", ERROR_MESSAGE);
            return -1;
        }
        NuovaL=lista;
        flush();            //pulisce le classi segnate come già cercate
        //int k2= Integer.parseInt(key);
        for (i=0; i< lista.size(); i++)
        {
            if ((lista.get(i).getDescrizione().toLowerCase()).contains(key.toLowerCase()) || lista.get(i).getData().contains(key) )
            {
                this.txt2.setText("Trovato " + lista.get(i).getDescrizione() + " " + lista.get(i).getData());
                t.changeSelection(i, 3, false , false);
                NuovaL.get(i).setCercato(true);
                NuovaL=lista;
                check=true;
                break;
            }
        }
        if (check) {
            return i;
        }
        else {
            this.txt2.setText("Nessuno Trovato dalla ricerca");
            return v+1;
        }
    }

    /**
     * Metodo che viene usato per poter pulire la lista dai valori già cercati
     */
    private void flush()
    {
        for (int j=0; j<lista.size(); j++)
        {
            lista.get(j).setCercato(false);
            NuovaL.get(j).setCercato(false);
        }
    }

    /**
     * Metodo per la ricerca di un match successivo nella lista
     */

    private void RicercaSuccessivo(int el){
        String key = this.txt.getText();
        if(key.equals("") || el==-1)
        {
            showMessageDialog(null, "Effettuare prima la ricerca per match poi ricercare il prossimo",
                    "Errore", ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < NuovaL.size(); i++) {
            if (((NuovaL.get(i).getDescrizione().toLowerCase()).contains(key.toLowerCase()) || NuovaL.get(i).getData().contains(key))
                    && !NuovaL.get(i).getCercato())
            {
                this.txt2.setText("Trovato " + NuovaL.get(i).getDescrizione() + " " + NuovaL.get(i).getData());
                t.changeSelection(i, 3, false , false);
                NuovaL.get(i).setCercato(true);
                break;
            }
        }
    }

    /**
     * Metodo che Ricopia la lista in una copia, e nella lista originale inserisce i valori ricercati
     */
    private void modificatore()
    {

        copia= new ArrayList<>();                   //istanzio la copia
        String Dat, Desc;
        int ammo;
        for (Conto c:lista) {
            Dat= c.getData();
            Desc= c.getDescrizione();
            ammo= c.getAmmontare();
            Conto ogg= new Conto(Dat, Desc, ammo);
            copia.add(ogg);
        }

        lista.clear();

        flag=1;

        if (Aggiustata.isEmpty())           //se la ricerca non ha prodotto risultato
        {
            /*************/
            CalcolaEntrate ca = new CalcolaEntrate(Aggiustata);
            CampoNetto.setText(String.valueOf(ca.calcolatore()));
            CampoNetto.repaint();
            /************************/

            //tm.Cambia();
            t.repaint();
            tm.settaValori();
            t.repaint();
            return;
        }

                             //una volta copiata la pulisco
        for (Conto c: Aggiustata) {
            Dat= c.getData();
            Desc= c.getDescrizione();
            ammo= c.getAmmontare();
            Conto ogg= new Conto(Dat, Desc, ammo);
            lista.add(ogg);
        }

        /*************/
        CalcolaEntrate ca = new CalcolaEntrate(Aggiustata);
        CampoNetto.setText(String.valueOf(ca.calcolatore()));
        CampoNetto.repaint();
        /************************/

        tm.Cambia();
        t.repaint();
        tm.settaValori();
        t.repaint();


    }

    /**
     * Metodo che viene utilizzato per riportare la lista al cuo contenuto originale 
     * prima della modifica
     */

    private void resettatore()
    {
        if (flag==0 || copia.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Nulla Da Resettare!");
            return;
        }


        String Dat, Desc;
        int ammo;
        lista.clear();
        Aggiustata.clear();

        for (Conto c:copia) {
            Dat= c.getData();
            Desc= c.getDescrizione();
            ammo= c.getAmmontare();
            Conto ogg= new Conto(Dat, Desc, ammo);
            lista.add(ogg);
        }
        copia.clear();                      //pulisco la copia

        //Aggiorno il campo calcolato
        /*************/
        CalcolaEntrate ca = new CalcolaEntrate(lista);
        CampoNetto.setText(String.valueOf(ca.calcolatore()));
        CampoNetto.repaint();
        /************************/

        //lista=copia;
        tm.Cambia();
        t.repaint();
        tm.settaValori();
        t.repaint();
    }

    /**
     * 
     * @param d1 Data di inizio periodo di ricerca
     * @param d2 Data di fine periodo di ricerca
     * @param n Periodo di giorni tra cui scegliere
     * @return r, un flag che controlla se le date passate sono comprese nel range
     */

    private boolean ControlloDate(LocalDate d1, LocalDate d2, int n)
    {
        boolean r=false;

        switch (n) {
            case (7) -> {
                if (Period.between(d1, d2).getDays() <= 7 &&
                        Period.between(d1, d2).getDays() > 0 &&
                        Period.between(d1, d2).getMonths() == 0 &&
                        Period.between(d1, d2).getYears() == 0) {
                    r = true;
                }
            }
            case (30) -> {

                if (Period.between(d1, d2).getDays() >= 0 &&
                        Period.between(d1, d2).getMonths() == 0 &&
                        Period.between(d1, d2).getYears() == 0) {
                    r = true;
                }
            }
            case (365) -> {
                if (Period.between(d1, d2).getDays() >= 0 &&
                        Period.between(d1, d2).getMonths() >= 0 &&
                        Period.between(d1, d2).getYears() == 0) {
                    r = true;
                }
            }
        }
        return r;
    }

    /**
     * @param inDate La stringa che passiamo per poter controllare se 
     * èuna stringa valida per poter essere salvata come data nuova di una 
     * operazione
     * @return Un booleano, in caso true la data è valida, no altrimenti
     *
     */

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param e the event to be processed, è un ascoltatore che non fa nulla
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
