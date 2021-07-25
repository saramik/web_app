package model;

import java.util.List;

public class Korpa {

    private String id;
    private List<ArtikalPorudzbenica> artikli;
    private double cena;

    public Korpa(){

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

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }
}
