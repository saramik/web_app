package model;

public class Artikal {

    private String id;
    private String naziv;
    private double cena;
    private TipArtikla tip;
    private double kolicina;
    private String opis;
    private String slika;

    private String restoran;

    public Artikal() {
    }

    public Artikal(String naziv, double cena, TipArtikla tip, double kolicina, String opis, String slika, String restoran) {
        this.naziv = naziv;
        this.cena = cena;
        this.tip = tip;
        this.kolicina = kolicina;
        this.opis = opis;
        this.slika = slika;
        this.restoran = restoran;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public TipArtikla getTip() {
        return tip;
    }

    public void setTip(TipArtikla tip) {
        this.tip = tip;
    }

    public double getKolicina() {
        return kolicina;
    }

    public void setKolicina(double kolicina) {
        this.kolicina = kolicina;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getRestoran() {
        return restoran;
    }

    public void setRestoran(String restoran) {
        this.restoran = restoran;
    }
}