package model;

public class Administrator extends Korisnik {

    public Administrator(){

    }

    public Administrator(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, long datumRodjenja) {
        super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja);
    }
}
