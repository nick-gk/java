package instrumente;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*Program intrumente financiare.
 *
 * Cerințe:
 *
 * 1. O clasa instrumente.Instrument, care sa implementeze interfata instrumente.Evaluabil si care contine
 * urmatoarele atribute private:
* symbol - String, o colectie de obiecte de tip Operatiune financiara
* si o clasă internă statică Operatiune (tip - TipOperatiune, data - LocalDate,
* preț - număr cu zecimale, cantitate - întreg).
*
* Clasa instrumente.Instrument va avea cel putin:
*  - constructor cu parametri si fara parametri;
*  - metode de citire pentru toate atributele;
*  - posibilitatea de a adăuga operatiuni la un intrumentul financiar.
*
* 2. Clasa Operatiune trebuie să publica si imutable.
*
* 3. Interfata instrumente.Evaluabil, defineste metoda double valoare(Intrument instrument), care returneaza double si
* primeste ca parametru un Intrument financiar.
*
* 4. Un enum TipOperatiune cu valorile CUMPARARE, VANZARE și o metodă pozitie() care să dea directia operatiunii
* financiare (-1 VANZARE, 1 CUMPARARE).
*
* 5. O clasa Actiune, derivata din Intrumnet si care implementeaza interfata instrumente.Evaluabil. Are in plus un atribut privat double - procentDividend.
*
* 6. O clasa  PortofoliuGenerics<T> ce implementeaza o colectie generica de intrumente financiare. Are un atribut privat portofoliu ce
* stocheaza colectia de obiecte <T> si contructori cu si fara parametri. Contine urmatoarele metode publice:
*  getProtofoliu(),
*  setPortofoliu(),
*  adaugaIntrument(),
*  getIntrument().
*
* Sa se creeze un program pentru popularea colectiei de intrumente financiare cu obiecte de tip Intrument si cu obiecte de tip Aciune.
* Sa de afiseze continutul portofoliului, valoarea acestuia si data primei operatiuni financiare efectuate pentru fiecare intrument
* financiar continut in portofoliu.
*
* Sa se implementeze metoda static void salvarePortofoliu(String caleFisier, PortofoliuGenerics portofoliu) throws IOException.
* Fisierul in care se salveaza portofoliu este un fisier text in formatul:
*
* BCR,918.4,2.5
* CUMPARARE,2022,4,3,200.0,3,CUMPARARE,2022,4,3,250.0,2,VANZARE,2022,4,3,204.0,1
* OMV,896
* CUMPARARE,2022,4,3,200.0,3,CUMPARARE,2022,4,3,250.0,2,VANZARE,2022,4,3,204.0,1
*
* Sa se implementeze metoda static PortofoliuGenerics<Instrument> incarcarePortofoliu(String caleFisier) throws IOException.
* Fisierul de intrare este fisierul text in formatul descris mai sus.
*
* */

public class Instrument implements Evaluabil, Serializable {

    private String simbol;
    private List<Operatiune> operatiuni;

    public Instrument(String simbol, List<Operatiune> operatiuni) {
        this.simbol = simbol;
        this.operatiuni = operatiuni;
    }

    public Instrument() {
        this.simbol = null;
        this.operatiuni = new ArrayList<Operatiune>();
    }

    public String getSimbol() {
        return simbol;
    }

    public void setSimbol(String simbol) {
        this.simbol = simbol;
    }

    public List<Operatiune> getOperatiuni() {
        return operatiuni;
    }

    public void setOperatiuni(List<Operatiune> operatiuni) {
        this.operatiuni = operatiuni;
    }

    public int getNumarOperatiuni() {
        return operatiuni.size();
    }

    @Override
    public double valoare() {
        double valoare = 0.0;
        for(var operatiune: this.operatiuni)
            valoare += operatiune.getCantitate()*operatiune.getPret()*
                    operatiune.getTip().pozitie();
        return valoare;
    }

    @Override
    public String toString() {
//        return "Instrument{" +
//                "simbol='" + simbol + '\'' +
//                "valoare= " + this.valoare() +
//                ", operatiuni=" + operatiuni +
//                '}';

        final StringBuilder strBuilder = new StringBuilder();
        String separator = ",";

        strBuilder.append(simbol.toString()); strBuilder.append(separator);
        strBuilder.append(this.valoare()); strBuilder.append(System.lineSeparator());
        for(Operatiune operatiune : this.operatiuni) {
            strBuilder.append(operatiune.toString());strBuilder.append(separator);
        }
        strBuilder.append(System.lineSeparator());

        return strBuilder.toString();
    }

    public void adaugaOperatiune(Operatiune operatiune)
    {
        this.operatiuni.add(operatiune);
    }


    public final static class Operatiune implements Serializable {
         /* si o clasă internă statică Operatiune (tip - TipOperatiune, data - LocalDate,
            * preț - număr cu zecimale, cantitate - întreg).*/
        private final TipOperatiune tip;
        private final LocalDate data;
        private final double pret;
        private final int cantitate;

        public Operatiune(TipOperatiune tip, LocalDate data, double pret, int cantitate) {
            this.tip = tip;
            this.data = data;
            this.pret = pret;
            this.cantitate = cantitate;
        }

        public TipOperatiune getTip() {
            return tip;
        }

        public LocalDate getData() {
            return data;
        }

        public double getPret() {
            return pret;
        }

        public int getCantitate() {
            return cantitate;
        }

        @Override
        public String toString() {
//            return "Operatiune{" +
//                    "tip=" + tip +
//                    ", data=" + data +
//                    ", pret=" + pret +
//                    ", cantitate=" + cantitate +
//                    '}';

            final StringBuilder strBuilder = new StringBuilder();
            String separator = ",";

            strBuilder.append(tip.toString()); strBuilder.append(separator);
            strBuilder.append(data.getYear()); strBuilder.append(separator);
            strBuilder.append(data.getMonthValue()); strBuilder.append(separator);
            strBuilder.append(data.getDayOfMonth()); strBuilder.append(separator);
            strBuilder.append(pret); strBuilder.append(separator);
            strBuilder.append(cantitate);

            return strBuilder.toString();
        }
    }
}
