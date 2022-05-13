/** Sa se implementeze clasa Cont (double sold, boolean inOperare) pentru a oferi operatiuni de:
 * - depunere(long suma, String mesaj)
 * - retragere(long suma, String mesaj)
 * - getSold()
 *
 * Se se defineasca clasa OperatorBancar, clasa derivata din clasa Thread care sa permita
 * operatiuni pe un obiect de timp cont. Clasa OperatorBancar are urmatoarele atribute:
 *     private Cont cont;
 *     private String nume;
 *     private int timpPregatire;
 *     private long suma;
 *
 * Sa se scrie un program care utilizeaza mai multi operatori bancari ce realizeza operatii
 * in regim concurential pe o resursa comuna reprezentata de un Cont bancar.
 * Programul primeste soldul contului si numarul de operatori ca parametri in linia de comanda.
 *
 */

public class Cont {
    private long sold = 0;
    private boolean inOperare;

    public Cont(long sold) {
        this.sold = sold;
        this.inOperare = false;
    }

    public synchronized void depunere(long suma, String mesaj) {
        // 1. Așteptâm până când putem opera pe cont
        while (inOperare) {
            try {
                System.out.println(mesaj + " contul este in operare. Așteptăm ...");
                wait(); // eliberăm monitorul și așteptăm
            } catch (InterruptedException e) {
            }
        }

        inOperare = true;

        // 2. Operam depunerea
        sold += suma;
        System.out.println(mesaj + " depus cu succes suma " + suma);

        inOperare = false;
        notifyAll();
    }

    public synchronized long retragere(long suma, String mesaj) {
        // 1. Așteptâm până când putem opera pe cont
        while (inOperare) {
            try {
                System.out.println(mesaj + " contul este in operare. Așteptăm ...");
                wait(); // eliberăm monitorul și așteptăm
            } catch (InterruptedException e) {
            }
        }

        inOperare = true;

        // 2. Incercam sa operam retragerea
        long rezultat;
        if (suma <= this.sold) {
            this.sold -= suma;
            System.out.println(mesaj + " retras cu succes suma " + suma + ", sold " + this.sold);
            rezultat = sold;
        } else {
            System.out.println(mesaj + " retragere suma " + suma + " esuata, fonduri insuficiente!");
            rezultat = -1;
        }

        inOperare = false;
        notifyAll();

        return rezultat;
    }


    public synchronized long getSold() {
        return this.sold;
    }

}
