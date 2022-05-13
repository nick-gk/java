package instrumente;

public enum TipOperatiune {
   /*
     * 4. Un enum TipOperatiune cu valorile CUMPARARE, VANZARE și o metodă pozitie() care să dea directia operatiunii
     * financiare (-1 VANZARE, 1 CUMPARARE).
    */
    CUMPARARE("CUMPARARE"),
    VANZARE("VANZARE");

    private int directie = 1;
    private final String tip;

    TipOperatiune(String tip) {
        this.tip = tip;
        if (this.tip.equalsIgnoreCase("CUMPARARE"))
            this.directie = 1;
        if (this.tip.equalsIgnoreCase("VANZARE"))
            this.directie = -1;
    }

    public int pozitie()
    {
        return this.directie;
    }

    public String getTip() { return this.tip; }
}
