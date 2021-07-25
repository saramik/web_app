package model;

public class ArtikalPorudzbenica {

    private String artikal;
    private String nazivArtikla;
    private double kolicina;
    private double cena;

    public ArtikalPorudzbenica(){

    }

    public ArtikalPorudzbenica(String artikal, String nazivArtikla, double kolicina, double cena) {
        this.artikal = artikal;
        this.nazivArtikla = nazivArtikla;
        this.kolicina = kolicina;
        this.cena = cena;
    }

    public String getNazivArtikla() {
        return nazivArtikla;
    }

    public void setNazivArtikla(String nazivArtikla) {
        this.nazivArtikla = nazivArtikla;
    }

    public String getArtikal() {
        return artikal;
    }

    public void setArtikal(String artikal) {
        this.artikal = artikal;
    }

    public double getKolicina() {
        return kolicina;
    }

    public void setKolicina(double kolicina) {
        this.kolicina = kolicina;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }
}
