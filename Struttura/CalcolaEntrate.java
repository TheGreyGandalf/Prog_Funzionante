
package Struttura;

import Classe_Conto.Conto;

import java.util.ArrayList;

class CalcolaEntrate{
    ArrayList<Conto> calcolo;
    CalcolaEntrate(ArrayList<Conto> c){
        calcolo=c;
    }

    int calcolatore()
    {
        int totale=0;

        for (Conto c:calcolo) {
            totale += c.getAmmontare();
        }
        return totale;
    }

}

