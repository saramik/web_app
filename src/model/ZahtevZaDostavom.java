package model;

public class ZahtevZaDostavom {

    private String id;
    private String porudzbina;
    private String korisnickoImeDostavljaca;
    private StatusZahteva status;
    private String restoran;

    public ZahtevZaDostavom(){
    }

    public ZahtevZaDostavom(String id, String porudzbina, String korisnickoImeDostavljaca, StatusZahteva status, String restoran) {
        this.id = id;
        this.porudzbina = porudzbina;
        this.korisnickoImeDostavljaca = korisnickoImeDostavljaca;
        this.status = status;
        this.restoran = restoran;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPorudzbina() {
        return porudzbina;
    }

    public void setPorudzbina(String porudzbina) {
        this.porudzbina = porudzbina;
    }

    public String getKorisnickoImeDostavljaca() {
        return korisnickoImeDostavljaca;
    }

    public void setKorisnickoImeDostavljaca(String korisnickoImeDostavljaca) {
        this.korisnickoImeDostavljaca = korisnickoImeDostavljaca;
    }

    public StatusZahteva getStatus() {
        return status;
    }

    public void setStatus(StatusZahteva status) {
        this.status = status;
    }

    public String getRestoran() {
        return restoran;
    }

    public void setRestoran(String restoran) {
        this.restoran = restoran;
    }
}
