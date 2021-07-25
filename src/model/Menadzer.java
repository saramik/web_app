package model;

public class Menadzer extends Korisnik{

    private String restoran;

    public Menadzer(){}

    public Menadzer(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, long datumRodjenja) {
        super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja);
    }

    public String getRestoran() {
        return restoran;
    }

    public void setRestoran(String restoran) {
        this.restoran = restoran;
    }
}
