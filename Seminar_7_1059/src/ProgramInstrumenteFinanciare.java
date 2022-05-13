import instrumente.Actiune;
import instrumente.Instrument;
import instrumente.PortofoliuGenerics;
import instrumente.TipOperatiune;

import java.io.*;
import java.time.LocalDate;
import java.util.*;


/*
 *Program intrumente financiare.
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
 * 5. O clasa Actiune, derivata din Intrumnet si care implementeaza interfata instrumente.Evaluabil.
 * Are in plus un atribut privat double - procentDividend.
 *
 * 6. O clasa  PortofoliuGenerics<T> ce implementeaza o colectie generica de intrumente financiare. Are un atribut privat portofoliu ce
 * stocheaza colectia de obiecte <T> si contructori cu si fara parametri. Contine urmatoarele metode publice:
 *  getProtofoliu(),
 *  setPortofoliu(),
 *  adaugaIntrument(),
 *  getIntrument(),
 *  afiseazaPortofoliu().
 *
 * Sa se creeze un program pentru popularea colectiei de intrumente financiare cu obiecte de tip Intrument si cu obiecte de tip Actiune.
 * Sa de afiseze continutul portofoliului, valoarea acestuia si data primei operatiuni financiare efectuate pentru fiecare intrument
 * financiar continut in portofoliu.
 *
 * Sa se implementeze metoda private static void salvarePortofoliu(String caleFisier, PortofoliuGenerics portofoliu) throws IOException.
 * Fisierul in care se salveaza portofoliu este un fisier text in formatul:
 *
 * BCR,918.4,2.5
 * CUMPARARE,2022,4,3,200.0,3,CUMPARARE,2022,4,3,250.0,2,VANZARE,2022,4,3,204.0,1
 * OMV,896
 * CUMPARARE,2022,4,3,200.0,3,CUMPARARE,2022,4,3,250.0,2,VANZARE,2022,4,3,204.0,1
 *
 * Sa se implementeze metoda private static PortofoliuGenerics<Instrument> incarcarePortofoliu(String caleFisier) throws IOException.
 * Fisierul de intrare este fisierul text in formatul descris mai sus.
 *
 * */

public class ProgramInstrumenteFinanciare {

    private static void salvarePortofoliu(String caleFisier, PortofoliuGenerics<Instrument> portofoliu) throws IOException {
        // Dacă a fost specificat și un folder
        if (new File(caleFisier).getParentFile() != null) {
            // ne asigurăm că acesta există
            new File(caleFisier).getParentFile().mkdirs();
        }

        // try() cu resurse pentru crearea unei instante de fisier
//        try (var fisier = new BufferedWriter(new FileWriter(caleFisier))) {
        try (var fisier = new PrintWriter(new BufferedWriter(new FileWriter(caleFisier)))) {
            for (var entry : portofoliu.getPortofoliu().entrySet()) {
                fisier.write(entry.getValue().toString());
            }
        }
    }

    private static void salvarePortofoliuBinar(String caleFisier, PortofoliuGenerics<Instrument> portofoliu)
            throws IOException {
        // Dacă a fost specificat și un folder
        if (new File(caleFisier).getParentFile() != null) {
            // ne asigurăm că acesta există
            new File(caleFisier).getParentFile().mkdirs();
        }

        try (var fisier=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(caleFisier)))) {
            for (var entry:portofoliu.getPortofoliu().entrySet()
            ) {
                //Atentie la ierarhia de clase!
                if (entry.getValue() instanceof Actiune) {
                    fisier.writeInt(2);
                    fisier.writeUTF(entry.getKey()); //acelasi lucru cu fisier.writeUTF(entry.getValue().getSimbol());
                    fisier.writeDouble(((Actiune)entry.getValue()).getProcentDividend());
                } else if (entry.getValue() instanceof Instrument) {
                    fisier.writeInt(1);
                    fisier.writeUTF(entry.getValue().getSimbol());

                }

                //Scriem numarul de operatiuni
                fisier.writeInt(entry.getValue().getNumarOperatiuni());
                //Scriem fiecare operatiune
                for (int indexOperatiune=0;indexOperatiune<entry.getValue().getNumarOperatiuni();indexOperatiune++) {
                    var operatiune=entry.getValue().getOperatiuni().get(indexOperatiune);
                    fisier.writeUTF(operatiune.getTip().toString());
                    fisier.writeInt(operatiune.getData().getYear());
                    fisier.writeInt(operatiune.getData().getMonthValue());
                    fisier.writeInt(operatiune.getData().getDayOfMonth());
                    fisier.writeDouble(operatiune.getPret());
                    fisier.writeInt(operatiune.getCantitate());
                }
            }
        }
    }

    //!!Clasele trebuie sa fie Serializable
    private static void salvarePortofoliuObiect(String caleFisier, PortofoliuGenerics<Instrument> portofoliu) throws IOException {
        // Dacă a fost specificat și un folder
        if (new File(caleFisier).getParentFile() != null) {
            // ne asigurăm că acesta există
            new File(caleFisier).getParentFile().mkdirs();
        }

        try(var fisier=new ObjectOutputStream(new FileOutputStream(caleFisier))) {
            fisier.writeObject(Integer.valueOf(portofoliu.getPortofoliu().entrySet().size()));
            for (var entry : portofoliu.getPortofoliu().entrySet()
            ) {
                fisier.writeObject(entry.getValue());
            }
        }
    }

    private static PortofoliuGenerics<Instrument> incarcarePortofoliu(String caleFisier) throws IOException {
        PortofoliuGenerics<Instrument> portofoliu = new PortofoliuGenerics<>();

        try (var fisierScanner = new Scanner(new BufferedReader(new FileReader(caleFisier)))) {
            while (fisierScanner.hasNext()) {
                String linie = fisierScanner.nextLine();
                Scanner linieScanner = new Scanner(linie);
                linieScanner.useDelimiter("[\\,]+");

                // citeste simbol
                String simbol = linieScanner.next();
//                double valoare = linieScanner.nextDouble();
                Locale setariLocale = linieScanner.locale();
                System.out.println(linieScanner.locale().toString());
                linieScanner.useLocale(Locale.ITALIAN);
                System.out.println(linieScanner.locale().toString());

                linieScanner.useLocale(Locale.US);
                double valoare = Double.valueOf(linieScanner.next());
                // daca procentul de dividend ramane -1.0 atunci avem un Intrument
                double procentDividend = -1.0;
                if (linieScanner.hasNext()) {
                    // avem o instanta de Actiune si citim procentul de dividend
//                    procentDividend = linieScanner.nextDouble();
                    procentDividend = Double.valueOf(linieScanner.next());
                }

                linie = fisierScanner.nextLine();
                linieScanner = new Scanner(linie);
                linieScanner.useDelimiter("[\\,]+");
                List<Instrument.Operatiune> operatiuni = new ArrayList<>();
                while (linieScanner.hasNext()) {
                    TipOperatiune tip = TipOperatiune.valueOf(linieScanner.next());
                    LocalDate data = LocalDate.of(linieScanner.nextInt(), linieScanner.nextInt(), linieScanner.nextInt());
                    double pret = linieScanner.nextDouble();
                    int cantitate = linieScanner.nextInt();
                    Instrument.Operatiune operatiune = new Instrument.Operatiune(tip, data, pret, cantitate);
                    operatiuni.add(operatiune);
                }
                if (procentDividend == -1.0) {
                    // creem un Instrument si il adaugam in potofoliu
                    Instrument instrument = new Instrument(simbol, operatiuni);
                    portofoliu.adaugaInstrument(simbol, instrument);
                } else {
                    // creem o Actiune si o adaugam in portofoliu
                    Actiune actiune = new Actiune(simbol, operatiuni, procentDividend);
                    portofoliu.adaugaInstrument(simbol, actiune);
                }
            }
        }

        return portofoliu;
    }

    private static PortofoliuGenerics<Instrument> incarcarePortofoliuBinar(String caleFisier) {
        PortofoliuGenerics<Instrument> portofoliu=new PortofoliuGenerics<>();
        try (var fisier=new DataInputStream(new BufferedInputStream(new FileInputStream(caleFisier)))) {
            while (true) {
                int tipInstrument=fisier.readInt();
                double procentDividend=-1;
                String simbol=fisier.readUTF();
                if (tipInstrument==2) {
                    procentDividend=fisier.readDouble();
                }
                int numarOperatiuni=fisier.readInt();
                List<Instrument.Operatiune> operatiuni=new ArrayList<>();
                for (int indexOperatiune=0;indexOperatiune<numarOperatiuni;indexOperatiune++) {
                    TipOperatiune tip=TipOperatiune.valueOf(fisier.readUTF());
                    int an=fisier.readInt();
                    int luna=fisier.readInt();
                    int zi=fisier.readInt();
                    double pret=fisier.readDouble();
                    int cantitate=fisier.readInt();
                    Instrument.Operatiune operatiune = new Instrument.Operatiune(tip, LocalDate.of(an,luna,zi), pret, cantitate);
                    operatiuni.add(operatiune);
                }
                if (tipInstrument == 1){
                    Instrument instrument=new Instrument(simbol,operatiuni);
                    portofoliu.adaugaInstrument(simbol,instrument);

                } else if (tipInstrument == 2) {
                    portofoliu.adaugaInstrument(simbol, new Actiune(simbol,operatiuni, procentDividend));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException eof) {
            System.out.println("Am citit EOF");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return portofoliu;
    }

    private static PortofoliuGenerics<Instrument> incarcarePortofoliuObiect(String caleFisier) {
        PortofoliuGenerics<Instrument> portofoliu = new PortofoliuGenerics<>();
        try (var fisier = new ObjectInputStream(new FileInputStream(caleFisier))) {
            Integer numarInstrumente = (Integer) fisier.readObject();
            for (int indexInstrument=0;indexInstrument<numarInstrumente;indexInstrument++) {
                Object obiect=fisier.readObject();
                if (obiect instanceof Actiune) {
                    Actiune actiune=(Actiune)obiect;
                    portofoliu.adaugaInstrument(actiune.getSimbol(), actiune);
                } else if(obiect instanceof Instrument){
                    Instrument instrument=(Instrument) obiect;
                    portofoliu.adaugaInstrument(instrument.getSimbol(), instrument);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return portofoliu;
    }

    public static void main(String args[]) throws IOException {
        /*
         * Sa se creeze un program pentru popularea colectiei de
         * intrumente financiare cu obiecte de tip Intrument si cu obiecte de tip Aciune.
         * Sa de afiseze continutul portofoliului, valoarea acestuia si
         * data primei operatiuni financiare efectuate pentru fiecare intrument
         * financiar continut in portofoliu.
         *
         * */
        PortofoliuGenerics<Instrument> portofoliu = new PortofoliuGenerics<>();

        Instrument i1 = new Instrument();
        i1.setSimbol("BRD");
        Instrument.Operatiune op1 = new Instrument.Operatiune(TipOperatiune.CUMPARARE,
                LocalDate.of(2021, 1, 14),200,3);
        Instrument.Operatiune op2 = new Instrument.Operatiune(TipOperatiune.CUMPARARE,
                LocalDate.of(2020, 3, 21),250,2);
        Instrument.Operatiune op3 = new Instrument.Operatiune(TipOperatiune.VANZARE,
                LocalDate.now(),204,1);
        List<Instrument.Operatiune> opList = new ArrayList<>();

        Collections.addAll(opList,op1,op2,op3);
        i1.setOperatiuni(opList);
        portofoliu.adaugaInstrument(i1.getSimbol(),i1);
        Actiune a1 = new Actiune("BCR",opList,2.5);
        portofoliu.adaugaInstrument(a1.getSimbol(),a1);

        double valoare = 0.0;
        for(var entry: portofoliu.getPortofoliu().entrySet()) {
            valoare += entry.getValue().valoare();
            LocalDate data = Collections.min(entry.getValue().getOperatiuni(),
                    new Comparator<Instrument.Operatiune>() {
                        @Override
                        public int compare(Instrument.Operatiune o1,
                                           Instrument.Operatiune o2) {
                            return o1.getData().compareTo(o2.getData());
                        }
                    }).getData();
            System.out.println(entry.getValue().toString() + "Data primei tranzactii " + data);
        }

        System.out.println("Valoarea portofoliului este: " + valoare);

        //Salvare in fisier text
        salvarePortofoliu("./Portofolii/Recente/PortofoliuInstrumente.csv", portofoliu);

        //Incarcare din fisier text
        portofoliu = incarcarePortofoliu("./Portofolii/Recente/PortofoliuInstrumente.csv");
        System.out.println("\nPortofoliu incarcat din fisier text:\n");
        portofoliu.afiseazaPortofoliu();

        //Salvare in fisier binar
        salvarePortofoliuBinar("./Portofolii/Recente/PortofoliuInstrumente.dat", portofoliu);

        //Incarcare din fisier binar
        PortofoliuGenerics<Instrument> portofoliuBinar=new PortofoliuGenerics<>();
        portofoliuBinar=incarcarePortofoliuBinar("./Portofolii/Recente/PortofoliuInstrumente.dat");
        System.out.println("\nPortofoliu incarcat din fisier binar:\n");
        portofoliuBinar.afiseazaPortofoliu();

        //Salvare obiecte Java in fisier binar
        salvarePortofoliuObiect("./Portofolii/Recente/PortofoliuInstrumente.obj", portofoliu);

        //Incarcare obiecte Java din fisier binar
        PortofoliuGenerics<Instrument> portofoliuObiect=new PortofoliuGenerics<>();
        portofoliuObiect=incarcarePortofoliuObiect("./Portofolii/Recente/PortofoliuInstrumente.obj");
        System.out.println("\nPortofoliu de obiecte Java incarcate din fisier binar:\n");
        portofoliuObiect.afiseazaPortofoliu();
    }
}
