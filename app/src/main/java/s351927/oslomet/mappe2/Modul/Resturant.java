package s351927.oslomet.mappe2.Modul;

public class Resturant {
    private  Long ID;
    private  String Navn;
    private  String Telefonnr;
    private  String Adresse;
    private  String Type;

    public Resturant() {
    }

    public Resturant(Long ID, String navn, String telefonnr, String adresse, String type) {
        this.ID = ID;
        Navn = navn;
        Telefonnr = telefonnr;
        Adresse = adresse;
        Type = type;
    }

    public Resturant(String navn, String telefonnr, String adresse, String type) {
        Navn = navn;
        Telefonnr = telefonnr;
        Adresse = adresse;
        Type = type;
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

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
