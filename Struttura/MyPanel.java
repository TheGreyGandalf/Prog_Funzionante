package Struttura;

import Classe_Conto.Conto;                  //classi dei package
import Scrittura_File.ScritturaFile;
//import org.apache.poi.sl.draw.geom.Path;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MyPanel extends JPanel implements ActionListener, DocumentListener {

        private JButton b;
        private JButton nuova, elimina, ButExcel, ButCsv, ButTxt;

        private ArrayList<Conto> lista;

        public JTextField txt, txt2, EtiExcel;

        private JTable t;
        private tab tm;

        private JLabel l;

    /**
     *
     * @param listaConto= La lista che contiene i dati che si andranno a leggere da stream di File
     */
    public MyPanel(ArrayList<Conto> listaConto) {
            //super();

            //this.setLayout(new BorderLayout());
            //Vector v = new Vector();
            lista=listaConto;


            //TableModel dataModel = new Struttura.tab(lista);
            // crea la tabella
            tm = new tab(lista);
            //tm.settaValori(lista.size(), tm.getColumnCount());//t.setModel(tm);

            t = new JTable(tm);   // aggiunge la tabella al pannello
            //t.add(new JScrollPane());
            tm.settaValori();

            //DefaultTableModel tm = new DefaultTableModel();

            //Scrittura_File.ScritturaFile.fillData ajeje = new Scrittura_File.ScritturaFile.fillData(t, new File("spatagarru.xls"));
            //ajeje.scriv();

            JPanel pTab = new JPanel();                 //pannello con tabella e header
            pTab.setLayout(new BorderLayout());
            //pTab.setLayout(new BorderLayout());

            pTab.add(t, BorderLayout.CENTER);
            pTab.add(t.getTableHeader(), BorderLayout.NORTH);
            //pTab.add(new JScrollPane(t));
            this.add(pTab, BorderLayout.NORTH);


            JPanel pTab2 = new JPanel();                //secondo pannello con altri bottoni
            pTab2.setLayout(new BorderLayout());
            txt = new JTextField("", 25);
            pTab2.add(txt, BorderLayout.CENTER);
            this.add(txt, BorderLayout.CENTER);

            txt2 = new JTextField("", 25);
            txt2.setEditable(false);
            pTab2.add(txt2, BorderLayout.CENTER);
            this.add(txt2, BorderLayout.CENTER);


            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            b= new JButton("Cerca Un Utente");
            pTab2.add(b, BorderLayout.CENTER);
            b.addActionListener(new ActionListener(){                   //Bottone che fa partire la ricerca da una stringa
                /**
                 *
                 * @param e Evento in caso si cercasse un campo all'interno dei campi salvati
                 */
                public void actionPerformed(ActionEvent e)
                {
                    ricerca();
                }               //Quando viene cliccato il pulsante ricerca è presente un astion listener apposito
                });
            this.add(b, BorderLayout.CENTER);

            //pulsante di aggiunta di un nuovo record

            nuova= new JButton("Nuova riga");
            p.add(nuova, BorderLayout.EAST);
            nuova.addActionListener(new ActionListener() {
                /**
                 *
                 * @param e, Pannello che compare quando l'utente clicca nuovo Utente
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    class MyPanel2 extends JPanel{
                        private ArrayList<Conto> listaPassata;               //Lista che andremo ad utilizzare
                        private JButton Aggiungi;                   //bottone per aggiunta di valori a tabella

                        private JTextField campo1, campo2, campo3;
                        private JLabel et1, et2, et3;

                        /**
                         *
                         * Pannello con 3 etichette per l'aggiunta di nuovi dati
                         */

                        public MyPanel2(/*ArrayList<Classe_Conto.Conto> Lista*/JFrame f) {
                            //listaPassata=Lista;
                            JPanel pannelloInterno = new JPanel();
                            pannelloInterno.setLayout(new BorderLayout());
                            /*                                          !!!Commentata poichè è bella ma difficile da implementare!!!
                            Struttura.tab tm = new Struttura.tab(listaPassata);                     //NON FUNZIONANTE
                            JTable t = new JTable(tm);//tabella che si apre in caso si voglia aggiungere

                            pannelloInterno.setLayout(new BorderLayout());//Pannello con tabella vuota e bottone
                            //pTab.setLayout(new BorderLayout());
                            pannelloInterno.add(t, BorderLayout.CENTER);
                            pannelloInterno.add(t.getTableHeader(), BorderLayout.NORTH);
                            this.add(pannelloInterno, BorderLayout.NORTH);
                             */

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
                                    Conto ogg= new Conto(c1, c2, Integer.parseInt(c3));
                                    lista.add(ogg);                             //Aggiungo informazioni alla lista
                                    f.dispose();
                                    t.repaint();
                                    //tm.addRow((new Object[]{c1, c2, Integer.parseInt(c3)}));
                                    tm.Cambia();

                                    ScritturaFile sc = new ScritturaFile();
                                    sc.ScriviNormale("Struttura/dati.txt", lista, "\n", false);      //scrivo su file le modifiche
                                    //le scrivo su file
                                    tm.settaValori();
                                    t.repaint();

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
            this.add(nuova, BorderLayout.EAST);


            elimina= new JButton("Elimina riga");
            p.add(elimina, BorderLayout.SOUTH);
            elimina.addActionListener(new ActionListener() {
                /**
                 *
                 * @param e = Evento in caso si voglia eliminare dalla tabella un record selezionato
                 */
                @Override
                public void actionPerformed(ActionEvent e) {        //NON FUNZIONANTE
                    ScritturaFile scr= new ScritturaFile();
                    int i = t.getSelectedRow();
                    lista.remove(i);                        //elimino elemento selezionato

                    scr.ScriviNormale("Struttura/dati.txt", lista, "\n", false);
                    t.repaint();
                    tm.settaValori();
                    //tm.Cambia();
                    t.repaint();
                    //t.invalidate();
                }
            });
            this.add(elimina, BorderLayout.SOUTH);

        /**
         * Pannello Esportazioni
         */

            JPanel export = new JPanel();
            ButExcel = new JButton("Esporta in formato Excel");
            export.setLayout(new BorderLayout());
            export.add(ButExcel, BorderLayout.SOUTH);
            EtiExcel = new JTextField("Nome File Esportazione", 10);
            export.add(EtiExcel, BorderLayout.CENTER);
            ButExcel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String Utente = EtiExcel.getText();
                    File fif = new File(Utente+".xls");
                    ScritturaFile.fillData OnExc = new ScritturaFile.fillData(t, fif);
                    OnExc.scriv();
                }
            });
            this.add(export, BorderLayout.CENTER);

            JPanel export2 = new JPanel();
            ButCsv = new JButton("Esporta in formato CSV");
            export2.setLayout(new BorderLayout());
            export2.setLayout(new BorderLayout());
            export2.add(ButCsv, BorderLayout.CENTER);
            ButExcel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String Utente = EtiExcel.getText();
                    ScritturaFile OnCsv = new ScritturaFile();
                    OnCsv.ScriviNormale(Utente+".csv", lista,",", false);
                }
            });
            this.add(export2, BorderLayout.CENTER);


            JPanel export3 = new JPanel();
            ButTxt = new JButton("Esporta in formato Txt");
            export3.setLayout(new BorderLayout());
            export3.setLayout(new BorderLayout());
            export3.add(ButTxt, BorderLayout.CENTER);
            ButExcel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String Utente = EtiExcel.getText();
                    ScritturaFile OnCsv = new ScritturaFile();
                    OnCsv.ScriviNormale(Utente+".txt", lista,"\n", false);
                }
            });
            this.add(export3, BorderLayout.CENTER);

    }

    /**
     * Funzione che preleva testo dal primo campo e va a ricercare corrispondenze
     */

    public void ricerca()
    {
        String key = this.txt.getText();
        //int k2= Integer.parseInt(key);
        for (int i=0; i< lista.size(); i++)
        {
            if (lista.get(i).getDescrizione().contains(key) || lista.get(i).getData().contains(key) )
            {
                this.txt2.setText("Trovato " + lista.get(i).getDescrizione() + " " + lista.get(i).getData());
                t.changeSelection(i, 3, false , false);
                return;
            }
            this.txt2.setText("Nessuno Trovato dalla ricerca");
        }
    }


    /**
     *
     * @param e the event to be processed, è un ascoltatore che non fa nulla
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    }


    @Override
    public void insertUpdate(DocumentEvent e) {

    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    /*public static TableModel getModel() {
        return model;
    }


    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();

        // **** printf added below
        System.out.printf("[row: %d, column: %d]%n", row, column);

        model = (TableModel1) e.getSource();

        String columnName = model.getColumnName(column);

        // **** println added below
        System.out.println("columnName: " + columnName);

        Object data = model.getValueAt(row, column);

        if (model.getValueAt(row, column) == Boolean.TRUE) {
            System.out.println("Condition working");

            // *** AIOOBE called here
            model.removeRow(row);
        }


    }

     */
}
