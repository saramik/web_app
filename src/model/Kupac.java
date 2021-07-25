package model;

import java.util.List;

public class Kupac extends Korisnik{

    private List<String> porudzbine;
    private int sakupljeniBodovi;
    private Korpa korpa;
    private TipKupca tipKupca;

    public Kupac(){

    }

    public Kupac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, long datumRodjenja) {
        super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja);
        tipKupca = new TipKupca();
        tipKupca.setImeTipa(ImeTipa.BRONZANI);
        tipKupca.setTrazeniBroj(100);   // npr
    }

    public List<String> getPorudzbine() {
        return porudzbine;
    }

    public void setPorudzbine(List<String> porudzbine) {
        this.porudzbine = porudzbine;
    }

    public int getSakupljeniBodovi() {
        return sakupljeniBodovi;
    }

    public void setSakupljeniBodovi(int sakupljeniBodovi) {
        this.sakupljeniBodovi = sakupljeniBodovi;
    }

    public Korpa getKorpa() {
        return korpa;
    }

    public void setKorpa(Korpa korpa) {
        this.korpa = korpa;
    }

    public TipKupca getTipKupca() {
        return tipKupca;
    }

    public void setTipKupca(TipKupca tipKupca) {
        this.tipKupca = tipKupca;
    }
}
