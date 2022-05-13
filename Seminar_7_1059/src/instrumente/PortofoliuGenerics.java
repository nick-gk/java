package instrumente;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PortofoliuGenerics<T> {
    /*
     * 6. O clasa  PortofoliuGenerics<T> ce implementeaza o colectie generica
     * de intrumente financiare. Are un atribut privat portofoliu ce
     * stocheaza colectia de obiecte <T> si constructori cu si fara parametri.
     * Contine urmatoarele metode publice:
     *  getProtofoliu(),
     *  setPortofoliu(),
     *  adaugaIntrument(),
     *  getIntrument().
     *
     * Sa se creeze un program pentru popularea colectiei de intrumente financiare
     * cu obiecte de tip Instrument si cu obiecte de tip Aciune.
     * Sa de afiseze continutul portofoliului,
     * valoarea acestuia si data primei operatiuni financiare efectuate pentru
     * fiecare instrument
     * financiar continut in portofoliu.
     *
     * */
    private Map<String, T> portofoliu;

    public PortofoliuGenerics(Map<String, T> portofoliu) {
        this.portofoliu = portofoliu;
    }

    public PortofoliuGenerics() {
        this.portofoliu = new HashMap<>();
    }

    public Map<String, T> getPortofoliu() {
        return portofoliu;
    }

    public void setPortofoliu(Map<String, T> portofoliu) {
        this.portofoliu = portofoliu;
    }

    public void adaugaInstrument(String simbol, T instrument)
    {
        this.portofoliu.put(simbol, instrument);
    }

    public T getInstrument(String simbol){
        return this.portofoliu.get(simbol);
    }

    public <T> void afiseazaPortofoliu() {
        for(var entry: portofoliu.entrySet()) {
            System.out.print(entry.getValue().toString());
        }
    }

    public double valoarePortofoliu() {
        double valoare = 0.0;
        for (var entry : portofoliu.entrySet()) {
            if (entry.getValue() instanceof Actiune)
                valoare += ((Actiune)entry.getValue()).valoare();
            else
                valoare += ((Instrument)entry.getValue()).valoare();
        }

        return valoare;
    }
}