public class OperatorBancar extends Thread {
    private Cont cont;
    private String nume;
    private int timpPregatire;
    private long suma;

    public OperatorBancar(Cont cont, String nume, int timpPregatire, long suma) {
        this.cont = cont;
        this.nume = nume;
        this.timpPregatire = timpPregatire;
        this.suma = suma;
    }

    @Override
    public void run() {

        while (this.cont.getSold() >= suma) {
            try {
                sleep(this.timpPregatire);
                this.cont.retragere(suma, nume);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Sfarsit operator " + nume);
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            System.out.println("Numar incorect de parametri. Utilizare <nr. operatori> <sold>");
            System.exit(-2);
        }

        final int nrOperatori = Integer.parseInt(args[0]);
        Cont cont = new Cont(Long.parseLong(args[1]));
        System.out.println("Numar operatori: " + nrOperatori);
        System.out.println("Sold initial: " + cont.getSold());

        Thread[] operatori = new OperatorBancar[nrOperatori];

        for (int i=0; i<nrOperatori; i++) {
            operatori[i] = new OperatorBancar(cont, "Operator_"+(i+1),
                    (10*(i+50)), 50+(i*10));
            operatori[i].start();
        }

        for (int i=0; i<nrOperatori; i++) {
            operatori[i].join();
        }

        System.out.println("Sold final: " + cont.getSold());
    }
}
