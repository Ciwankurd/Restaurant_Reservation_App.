package s351927.oslomet.mappe2.Modul;

public class Kontakt {
    private Long ID;
    private String Navn;
    private String Telefonnr;

    public Kontakt() {
    }

    public Kontakt(Long ID, String navn, String telefonnr) {
        this.ID = ID;
        Navn = navn;
        Telefonnr = telefonnr;
    }

    public Kontakt(String navn, String telefonnr) {
        Navn = navn;
        Telefonnr = telefonnr;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNavn() {
        return Navn;
    }

    public void setNavn(String navn) {
        Navn = navn;
    }

    public String getTelefonnr() {
        return Telefonnr;
    }

    public void setTelefonnr(String telefonnr) {
        Telefonnr = telefonnr;
    }
}
