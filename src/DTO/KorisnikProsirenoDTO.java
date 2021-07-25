package DTO;

import model.ImeTipa;
import model.Pol;
import model.Uloga;

public class KorisnikProsirenoDTO {

    private String korisnickoIme;
    private String ime;
    private String prezime;
    private Pol pol;
    private long datumRodjenja;
    private Uloga uloga;
    private ImeTipa tipKupca;
    private int sakupljeniBodovi;


    public KorisnikProsirenoDTO(){

    }

    public KorisnikProsirenoDTO(String korisnickoIme, String ime, String prezime, Pol pol, long datumRodjenja, Uloga uloga, ImeTipa tipKupca, int sakupljeniBodovi) {
        this.korisnickoIme = korisnickoIme;
        this.ime = ime;
        this.prezime = prezime;
        this.pol = pol;
        this.datumRodjenja = datumRodjenja;
        this.uloga = uloga;
        this.tipKupca = tipKupca;
        this.sakupljeniBodovi = sakupljeniBodovi;
    }

    public ImeTipa getTipKupca() {
        return tipKupca;
    }

    public void setTipKupca(ImeTipa tipKupca) {
        this.tipKupca = tipKupca;
    }

    public int getSakupljeniBodovi() {
        return sakupljeniBodovi;
    }

    public void setSakupljeniBodovi(int sakupljeniBodovi) {
        this.sakupljeniBodovi = sakupljeniBodovi;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Pol getPol() {
        return pol;
    }

    public void setPol(Pol pol) {
        this.pol = pol;
    }

    public long getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(long datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public Uloga getUloga() {
        return uloga;
    }

    public void setUloga(Uloga uloga) {
        this.uloga = uloga;
    }

}
