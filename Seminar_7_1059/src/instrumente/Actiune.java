package instrumente;
import java.io.Serializable;
import java.util.List;


public class Actiune extends Instrument implements Evaluabil, Serializable {
    /*

     * 5. O clasa Actiune, derivata din Intrumnet si care implementeaza
     interfata instrumente.Evaluabil.
      Are in plus un atribut privat double - procentDividend.
     * */
    private double procentDividend;

    public Actiune(String simbol, List<Operatiune> operatiuni, double procentDividend) {
        super(simbol, operatiuni);
        this.procentDividend = procentDividend;
    }

    public Actiune() {
        super();
        this.procentDividend = 0.0;
    }

    public double getProcentDividend() {
        return procentDividend;
    }

    public void setProcentDividend(double procentDividend) {
        this.procentDividend = procentDividend;
    }

    @Override
    public double valoare(){
        double valoare = super.valoare();
        return valoare += (valoare * procentDividend)/100.0;
    }

    @Override
    public String toString() {
//        return "Actiune{" +
//                "procentDividend=" + procentDividend + " " +
//                super.toString() +
//                '}';

        final StringBuilder strBuilder = new StringBuilder();
        String separator = ",";

        strBuilder.append(super.getSimbol().toString()); strBuilder.append(separator);
        strBuilder.append(this.valoare()); strBuilder.append(separator);
        strBuilder.append(this.procentDividend);strBuilder.append(System.lineSeparator());
        for(Operatiune operatiune : this.getOperatiuni()) {
            strBuilder.append(operatiune.toString());strBuilder.append(separator);
        }
        strBuilder.append(System.lineSeparator());

        return strBuilder.toString();
    }
}
