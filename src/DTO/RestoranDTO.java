package DTO;

import model.Restoran;

public class RestoranDTO {

    private String naziv;
    private String tip;
    private String logo;
    private String menadzer;
    private long otvorenoOd;
    private long otvorenoDo;
    private double geografskaDuzina;
    private double geografskaSirina;
    private String ulica;
    private String broj;
    private String mesto;
    private String postanskiBroj;
    private double ocena;
    private boolean otvoreno;

    public RestoranDTO(){

    }

    public RestoranDTO(Restoran r) {
        this.naziv = r.getNaziv();
        this.tip = r.getTip().toString();
        this.logo = r.getLogo();
        this.otvorenoOd = r.getOtvorenoOd();
        this.otvorenoDo = r.getOtvorenoDo();
        this.geografskaDuzina = r.getLokacija().getGeografskaDuzina();
        this.geografskaSirina = r.getLokacija().getGeografskaSirina();
        this.ulica = r.getLokacija().getUlica();
        this.broj = r.getLokacija().getBroj();
        this.mesto = r.getLokacija().getMesto();
        this.postanskiBroj = r.getLokacija().getPostanskiBroj();
        this.ocena = r.getOcena();
        this.otvoreno = r.isStatus();
    }

    public boolean isOtvoreno() {

        return otvoreno;
    }

    public void setOtvoreno(boolean otvoreno) {
        this.otvoreno = otvoreno;
    }

    public double getOcena() {
        return ocena;
    }

    public void setOcena(double ocena) {
        this.ocena = ocena;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMenadzer() {
        return menadzer;
    }

    public void setMenadzer(String menadzer) {
        this.menadzer = menadzer;
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

    public double getGeografskaDuzina() {
        return geografskaDuzina;
    }

    public void setGeografskaDuzina(double geografskaDuzina) {
        this.geografskaDuzina = geografskaDuzina;
    }

    public double getGeografskaSirina() {
        return geografskaSirina;
    }

    public void setGeografskaSirina(double geografskaSirina) {
        this.geografskaSirina = geografskaSirina;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getPostanskiBroj() {
        return postanskiBroj;
    }

    public void setPostanskiBroj(String postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
    }
}
