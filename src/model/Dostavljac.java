package model;

import java.util.List;

public class Dostavljac extends Korisnik {

    private List<String> porudzbine;

    public Dostavljac(){

    }

    public Dostavljac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, long datumRodjenja) {
        super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja);
    }

    public List<String> getPorudzbine() {
        return porudzbine;
    }

    public void setPorudzbine(List<String> porudzbine) {
        this.porudzbine = porudzbine;
    }
}
