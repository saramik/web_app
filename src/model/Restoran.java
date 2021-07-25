package model;

import java.util.ArrayList;
import java.util.List;

public class Restoran {

    private String id;
    private String naziv;
    private TipRestorana tip;
    private List<String> artikli;
    private boolean status; // radi ili ne radi
    private Lokacija lokacija;
    private String logo; // string64?

    private String menadzer;
    private int brojOcena;
    private double ocena;

    private long otvorenoOd;        // u minutima
    private long otvorenoDo;        // u minutima


    public Restoran(){

    }

    public Restoran(String naziv, TipRestorana tip, Lokacija lokacija, String logo, long otvorenoOd, long otvorenoDo) {
        this.naziv = naziv;
        this.tip = tip;
        this.lokacija = lokacija;
        this.logo = logo;
        this.otvorenoOd = otvorenoOd;
        this.otvorenoDo = otvorenoDo;
        // dodato
        this.artikli = new ArrayList<>();
    }

    public long getOtvorenoOd() {
        return otvorenoOd;
    }

    public void setOtvorenoOd(long otvorenoOd) {
        this.otvorenoOd = otvorenoOd;
    }

    public long getOtvorenoDo() {
        return otvorenoDo;
    }

    public void setOtvorenoDo(long otvorenoDo) {
        this.otvorenoDo = otvorenoDo;
    }

    public String getMenadzer() {
        return menadzer;
    }

    public void setMenadzer(String menadzer) {
        this.menadzer = menadzer;
    }

    public int getBrojOcena() {
        return brojOcena;
    }

    public void setBrojOcena(int brojOcena) {
        this.brojOcena = brojOcena;
    }

    public double getOcena() {
        return ocena;
    }

    public void setOcena(double ocena) {
        this.ocena = ocena;
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

    public TipRestorana getTip() {
        return tip;
    }

    public void setTip(TipRestorana tip) {
        this.tip = tip;
    }

    public List<String> getArtikli() {
        return artikli;
    }

    public void setArtikli(List<String> artikli) {
        this.artikli = artikli;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Lokacija getLokacija() {
        return lokacija;
    }

    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
