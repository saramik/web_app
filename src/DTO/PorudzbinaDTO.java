package DTO;

import model.StatusPorudzbine;

public class PorudzbinaDTO {

    private String id;
    private String restoran;
    private long datum;
    private double cena;
    private String imeKupca;
    private String prezimeKupca;
    private StatusPorudzbine status;

    public PorudzbinaDTO(){

    }

    public PorudzbinaDTO(String id, String restoran, long datum, double cena, String imeKupca, String prezimeKupca, StatusPorudzbine status) {
        this.id = id;
        this.restoran = restoran;
        this.datum = datum;
        this.cena = cena;
        this.imeKupca = imeKupca;
        this.prezimeKupca = prezimeKupca;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImeKupca() {
        return imeKupca;
    }

    public void setImeKupca(String imeKupca) {
        this.imeKupca = imeKupca;
    }

    public String getPrezimeKupca() {
        return prezimeKupca;
    }

    public void setPrezimeKupca(String prezimeKupca) {
        this.prezimeKupca = prezimeKupca;
    }

    public StatusPorudzbine getStatus() {
        return status;
    }

    public void setStatus(StatusPorudzbine status) {
        this.status = status;
    }
}
