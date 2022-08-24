package s351927.oslomet.mappe2.Modul;

public class Bestilling {
    private Long ID;
    private String Date;
    private String Tid;
    public Bestilling() {
    }

    public Bestilling(Long ID, String date, String tid) {
        this.ID = ID;
        Date = date;
        Tid = tid;
    }

    public Bestilling(String date, String tid) {
        Date = date;
        Tid = tid;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTid() {
        return Tid;
    }

    public void setTid(String tid) {
        Tid = tid;
    }
}
