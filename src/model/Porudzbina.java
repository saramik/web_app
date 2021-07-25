package model;

import java.util.List;

public class Porudzbina {

    private String id;
    private List<ArtikalPorudzbenica> artikli;
    private String restoran;
    private long datum;
    private double cena;
    private String kupac;
    private StatusPorudzbine status;

    public Porudzbina(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ArtikalPorudzbenica> getArtikli() {
        return artikli;
    }

    public void setArtikli(List<ArtikalPorudzbenica> artikli) {
        this.artikli = artikli;
    }

    public String getRestoran() {
        return restoran;
    }

    public void setRestoran(String restoran) {
        this.restoran = restoran;
    }

    public long getDatum() {
        return datum;
    }

    public void setDatum(long datum) {
        this.datum = datum;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public String getKupac() {
        return kupac;
    }

    public void setKupac(String kupac) {
        this.kupac = kupac;
    }

    public StatusPorudzbine getStatus() {
        return status;
    }

    public void setStatus(StatusPorudzbine status) {
        this.status = status;
    }
}
