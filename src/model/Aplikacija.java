package model;

import DTO.KorisnikDTO;
import DTO.KorisnikProsirenoDTO;
import DTO.RestoranDTO;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Aplikacija {

	
	private HashMap<String, Administrator> administratori = new HashMap<String, Administrator>();
	private HashMap<String, Menadzer> menadzeri = new HashMap<String, Menadzer>();
	private HashMap<String, Dostavljac> dostavljaci = new HashMap<String, Dostavljac>();
	private HashMap<String, Kupac> kupci = new HashMap<String, Kupac>();;
	private HashMap<String, Restoran> restorani = new HashMap<String, Restoran>();
	private HashMap<String, Artikal> artikli = new HashMap<String, Artikal>();
	private HashMap<String, Porudzbina> porudzbine = new HashMap<String, Porudzbina>();
	private HashMap<String, Komentar> komentari = new HashMap<String, Komentar>();

	public Aplikacija(){
        kupci.put("kupac1", new Kupac("kupac1", "lozinka1", "ana", "sasic",Pol.ZENSKI, 1000));
        Kupac k = new Kupac("akupac2", "lozinka1", "kupkinja", "kupic",Pol.ZENSKI, 199);
        k.setSakupljeniBodovi(200);
        kupci.put("akupac2", k);
        administratori.put("admin", new Administrator("admin", "admin", "admin", "admin",Pol.ZENSKI, 1000));
        dostavljaci.put("dost", new Dostavljac("dost", "admin", "d", "d",Pol.ZENSKI, 1000));
        menadzeri.put("menadzerko", new Menadzer("menadzerko", "menadzer", "d", "d",Pol.ZENSKI, 1000));

        menadzeri.get("menadzerko").setRestoran("r1");

        Lokacija l = new Lokacija();
        Restoran r = new Restoran("r1", TipRestorana.ITALIJANSKI, l, "", 480l, 900l);
        restorani.put(r.getNaziv(), r);

        Artikal a = new Artikal("a1", 10.0, TipArtikla.JELO, 0.0, "opis a1", "", "r1");
        a.setId("123");
        // TODO: novom dodeliti id
        r.getArtikli().add("123");
        artikli.put("123", a);

    }

    public KorisnikDTO registracijaKorisnika(KorisnikDTO korisnikDTO) throws Exception {

	    String korisnickoIme = korisnikDTO.getKorisnickoIme();
	    if (this.administratori.containsKey(korisnickoIme) || this.menadzeri.containsKey(korisnickoIme)
            || this.dostavljaci.containsKey(korisnickoIme) || this.kupci.containsKey(korisnickoIme)) {
	        throw new Exception("Korisnik sa datim korisnickim imenom vec postoji!");
        }

	    if (korisnikDTO.getUloga().equals(Uloga.MENADZER)){
	        this.menadzeri.put(korisnickoIme, new Menadzer(korisnikDTO.getKorisnickoIme(), korisnikDTO.getLozinka(),
                    korisnikDTO.getIme(), korisnikDTO.getPrezime(), korisnikDTO.getPol(), korisnikDTO.getDatumRodjenja()));
        }
	    else if (korisnikDTO.getUloga().equals(Uloga.DOSTAVLJAC)) {
            this.dostavljaci.put(korisnickoIme, new Dostavljac(korisnikDTO.getKorisnickoIme(), korisnikDTO.getLozinka(),
                    korisnikDTO.getIme(), korisnikDTO.getPrezime(), korisnikDTO.getPol(), korisnikDTO.getDatumRodjenja()));
	    }
	    else if(korisnikDTO.getUloga().equals(Uloga.KUPAC)){
	        this.kupci.put(korisnickoIme, new Kupac(korisnikDTO.getKorisnickoIme(), korisnikDTO.getLozinka(),
                    korisnikDTO.getIme(), korisnikDTO.getPrezime(), korisnikDTO.getPol(), korisnikDTO.getDatumRodjenja()));
	    }
	    else  {
            throw new Exception("Neodgovarajuca uloga!");
        }

	    return korisnikDTO;
    }


    public Korisnik login(String korisnickoIme, String lozinka) throws Exception {

	    if (this.administratori.containsKey(korisnickoIme)){
	        Administrator admin = this.administratori.get(korisnickoIme);
	        if (admin.getLozinka().equals(lozinka)) return admin;
	        else throw new Exception("Losi kredencijali!");
        }
	    if (this.menadzeri.containsKey(korisnickoIme)){
            Menadzer menadzer = this.menadzeri.get(korisnickoIme);
            if (menadzer.getLozinka().equals(lozinka)) return menadzer;
            else throw new Exception("Losi kredencijali!");
        }

        if (this.dostavljaci.containsKey(korisnickoIme)){
            Dostavljac dostavljac = this.dostavljaci.get(korisnickoIme);
            if (dostavljac.getLozinka().equals(lozinka)) return dostavljac;
            else throw new Exception("Losi kredencijali!");
        }
        if (this.kupci.containsKey(korisnickoIme)){
            Kupac kupac = this.kupci.get(korisnickoIme);
            if (kupac.getLozinka().equals(lozinka)) return kupac;
            else throw new Exception("Losi kredencijali!");
        }

	    throw new Exception("Korisnik nije pronadjen!");

    }


    public HashMap<String, Administrator> getAdministratori() {
        return administratori;
    }

    public void setAdministratori(HashMap<String, Administrator> administratori) {
        this.administratori = administratori;
    }

    public HashMap<String, Menadzer> getMenadzeri() {
        return menadzeri;
    }

    public void setMenadzeri(HashMap<String, Menadzer> menadzeri) {
        this.menadzeri = menadzeri;
    }

    public HashMap<String, Dostavljac> getDostavljaci() {
        return dostavljaci;
    }

    public void setDostavljaci(HashMap<String, Dostavljac> dostavljaci) {
        this.dostavljaci = dostavljaci;
    }

    public HashMap<String, Kupac> getKupci() {
        return kupci;
    }

    public void setKupci(HashMap<String, Kupac> kupci) {
        this.kupci = kupci;
    }

    public HashMap<String, Restoran> getRestorani() {
        return restorani;
    }

    public void setRestorani(HashMap<String, Restoran> restorani) {
        this.restorani = restorani;
    }

    public HashMap<String, Artikal> getArtikli() {
        return artikli;
    }

    public void setArtikli(HashMap<String, Artikal> artikli) {
        this.artikli = artikli;
    }

    public HashMap<String, Porudzbina> getPorudzbine() {
        return porudzbine;
    }

    public void setPorudzbine(HashMap<String, Porudzbina> porudzbine) {
        this.porudzbine = porudzbine;
    }

    public HashMap<String, Komentar> getKomentari() {
        return komentari;
    }

    public void setKomentari(HashMap<String, Komentar> komentari) {
        this.komentari = komentari;
    }

    public List<KorisnikProsirenoDTO> dobaviSveKorisnike() {

	    List<KorisnikProsirenoDTO> korisnici = new ArrayList<KorisnikProsirenoDTO>();

	    for (String s: administratori.keySet()){
	        Administrator a = administratori.get(s);
	        korisnici.add(new KorisnikProsirenoDTO(s, a.getIme(), a.getPrezime(), a.getPol(), a.getDatumRodjenja(), Uloga.ADMINISTRATOR, null, -1));
        }
        for (String s: menadzeri.keySet()){
            Menadzer m = menadzeri.get(s);
            korisnici.add(new KorisnikProsirenoDTO(s, m.getIme(), m.getPrezime(), m.getPol(), m.getDatumRodjenja(), Uloga.MENADZER, null, -1));
        }
        for (String s: dostavljaci.keySet()){
            Dostavljac d = dostavljaci.get(s);
            korisnici.add(new KorisnikProsirenoDTO(s, d.getIme(), d.getPrezime(), d.getPol(), d.getDatumRodjenja(), Uloga.DOSTAVLJAC, null, -1));
        }

        for (String s: kupci.keySet()){
            Kupac k = kupci.get(s);
            korisnici.add(new KorisnikProsirenoDTO(s, k.getIme(), k.getPrezime(), k.getPol(), k.getDatumRodjenja(), Uloga.KUPAC, k.getTipKupca().getImeTipa(), k.getSakupljeniBodovi()));
        }

	    return korisnici;
    }

    public List<KorisnikProsirenoDTO> pretraziKorisnike(String pretraga, String uloga, String tip, String sort) {
	    List<KorisnikProsirenoDTO> korisnici = new ArrayList<>();

	    for (KorisnikProsirenoDTO k: this.dobaviSveKorisnike()){
	        if (!(k.getKorisnickoIme().contains(pretraga) || k.getIme().contains(pretraga) || k.getPrezime().contains(pretraga))){
	            continue;
            }
	        if (uloga != null && !uloga.equals("")) {
                if (!k.getUloga().equals(Uloga.valueOf(uloga))) {
                    continue;
                }
            }if (tip != null && !tip.equals("")) {
                if (!k.getTipKupca().equals(ImeTipa.valueOf(tip))) {
                    continue;
                }
            }
            korisnici.add(k);
	    }

        sortirajKorisnike(korisnici, sort);


        return korisnici;
    }

    private void sortirajKorisnike(List<KorisnikProsirenoDTO> korisnici, String sort){

	    if (sort.equals("imeRastuce")){
            Collections.sort(korisnici, (KorisnikProsirenoDTO k1, KorisnikProsirenoDTO k2) ->{
                return k1.getIme().compareToIgnoreCase(k2.getIme());
            });
        }
	    else if (sort.equals("imeOpadajuce")){
            Collections.sort(korisnici, (KorisnikProsirenoDTO k1, KorisnikProsirenoDTO k2) ->{
                return k2.getIme().compareToIgnoreCase(k1.getIme());
            });
        }
	    else if (sort.equals("prezimeRastuce")){
            Collections.sort(korisnici, (KorisnikProsirenoDTO k1, KorisnikProsirenoDTO k2) ->{
                return k1.getPrezime().compareToIgnoreCase(k2.getPrezime());
            });
        }
        else if (sort.equals("prezimeOpadajuce")){
            Collections.sort(korisnici, (KorisnikProsirenoDTO k1, KorisnikProsirenoDTO k2) ->{
                return k2.getPrezime().compareToIgnoreCase(k1.getPrezime());
            });
        }
        else if (sort.equals("korisnickoImeRastuce")){
            Collections.sort(korisnici, (KorisnikProsirenoDTO k1, KorisnikProsirenoDTO k2) ->{
                return k1.getKorisnickoIme().compareToIgnoreCase(k2.getKorisnickoIme());
            });
        }
        else if (sort.equals("korisnickoImeOpadajuce")){
            Collections.sort(korisnici, (KorisnikProsirenoDTO k1, KorisnikProsirenoDTO k2) ->{
                return k2.getPrezime().compareToIgnoreCase(k1.getKorisnickoIme());
            });
        }
        else if (sort.equals("bodoviOpadajuce")){
            Collections.sort(korisnici, (KorisnikProsirenoDTO k1, KorisnikProsirenoDTO k2) -> Integer.compare(k2.getSakupljeniBodovi(), k1.getSakupljeniBodovi()));
        }
        else if (sort.equals("bodoviRastuce")) {
            Collections.sort(korisnici, Comparator.comparingInt(KorisnikProsirenoDTO::getSakupljeniBodovi));
        }


    }

    public Korisnik izmeniPodatke(KorisnikDTO k) {

	    String username = k.getKorisnickoIme();

	    if (k.getIme().isEmpty() || k.getPrezime().isEmpty()){  // jos neke provere
	        return null;
        }

	    Korisnik korisnik = this.dobaviKorisnikaPoKorisnickomImenu(username);
	    if (korisnik == null){
	        return null;
        }
	    korisnik.setDatumRodjenja(k.getDatumRodjenja());
	    korisnik.setIme(k.getIme());
	    korisnik.setPol(k.getPol());
	    korisnik.setPrezime(k.getPrezime());
        return korisnik;
    }

    public boolean izmeniLozinku(String korisnickoIme, String staraLozinka, String novaLozinka) throws Exception {

	    Korisnik k = dobaviKorisnikaPoKorisnickomImenu(korisnickoIme);

	    if (k == null) {
	        throw new Exception("Ne postoji korisnik");
        }

	    if (k.getLozinka().equals(staraLozinka)){
	        k.setLozinka(novaLozinka);
	        return true;
        }
	    else {
	        throw new Exception("Nece moci");
        }

    }

    public Korisnik dobaviKorisnikaPoKorisnickomImenu(String korisnickoIme){

	    if (this.administratori.containsKey(korisnickoIme)) {
            return this.administratori.get(korisnickoIme);
        }
        if (this.menadzeri.containsKey(korisnickoIme)){
            return this.menadzeri.get(korisnickoIme);
        }
        if (this.dostavljaci.containsKey(korisnickoIme)){
            return this.dostavljaci.get(korisnickoIme);
        }
        if (this.kupci.containsKey(korisnickoIme)){
            return this.kupci.get(korisnickoIme);
        }

	    return null;
    }

    public RestoranDTO dodajRestoran(RestoranDTO dto) throws Exception {

	    if (this.restorani.containsKey(dto.getNaziv())){
	        throw new Exception("Vec postoji restoran sa tim nazivom!");
        }

	    if (!proveriRestoranDTO(dto)){
	        throw new Exception("Nevalidan DTO!");
        }

	    if (!this.menadzeri.containsKey(dto.getMenadzer())){
	        throw new Exception("Ne postoji menadzer sa datim id-jem!");
        }
	    Menadzer menadzer = this.menadzeri.get(dto.getMenadzer());

	    if (menadzer.getRestoran() != null) {
	        throw new Exception("Ovaj mendazer vec upravlja nekim restoranom!");
	    }

	    menadzer.setRestoran(dto.getNaziv());
	    Lokacija lokacija = new Lokacija(dto.getGeografskaDuzina(), dto.getGeografskaSirina(),
                dto.getUlica(), dto.getBroj(), dto.getMesto(), dto.getPostanskiBroj());
	    Restoran restoran = new Restoran(dto.getNaziv(), TipRestorana.valueOf(dto.getTip()), lokacija, dto.getLogo(),
                dto.getOtvorenoOd(), dto.getOtvorenoDo());
	    restoran.setArtikli(new ArrayList<String>());
	    restoran.setOcena(0.0);
	    restoran.setBrojOcena(0);
	    restorani.put(restoran.getNaziv(), restoran);
	    return dto;
    }

    private static boolean proveriRestoranDTO(RestoranDTO dto){
	    // TODO
	    return true;
    }

    public List<RestoranDTO> pretragaRestorana(String pretraga, String tip, String otvoren, String sort){

	    List<Restoran> restorani = new ArrayList<Restoran>();

	    long trenutno = trenutnoVremeMinuti();

	    for (Restoran r: this.restorani.values()){
	        if (pretraga != null && !pretraga.equals("")){
	            if (!(r.getNaziv().contains(pretraga) || r.getTip().toString().contains(pretraga) ||
                r.getLokacija().getMesto().contains(pretraga))) continue;
            }
	        if (otvoren.equals("true")){
	            if (r.getOtvorenoOd() > trenutno || r.getOtvorenoDo() < trenutno) continue;
            }
	        if (tip != null && !tip.equals("")){
	            if (!r.getTip().toString().equals(tip)) continue;
            }
	        if (r.getOtvorenoOd() > trenutno || r.getOtvorenoDo() < trenutno) r.setStatus(false);
	        else r.setStatus(true);
	        restorani.add(r);
        }
	    if (sort != null && !sort.equals(""))
	        sortirajRestorane(restorani, sort);

	    return toDTO(restorani);

    }


    private static long trenutnoVremeMinuti(){
        Long vreme = new Date().getTime();
        // da li je otvoren
        LocalTime trenutno = LocalTime.now();
        int sati = trenutno.getHour();
        int minuti = trenutno.getMinute();

        long trenutnoLong = sati * 60 + minuti;
        return trenutnoLong;
    }

    public List<RestoranDTO> dobaviSveRestorane() {

	    List<Restoran> restoraniOtvoreni = new ArrayList<Restoran>();
        List<Restoran> restoraniZatvoreni = new ArrayList<Restoran>();

        long trenutnoLong = trenutnoVremeMinuti();

        for (Restoran r: this.restorani.values()){
            if (trenutnoLong > r.getOtvorenoOd() && trenutnoLong < r.getOtvorenoDo()) {
                r.setStatus(true);
                restoraniOtvoreni.add(r);
            }
            else {
                r.setStatus(false);
                restoraniZatvoreni.add(r);
            };
        }

        sortirajRestorane(restoraniOtvoreni, "ocenaOpadajuce");
        sortirajRestorane(restoraniZatvoreni, "ocenaOpadajuce");

        List<Restoran> restorani = Stream.concat(restoraniOtvoreni.stream(), restoraniZatvoreni.stream())
                .collect(Collectors.toList());

        return toDTO(restorani);
	}

	private static List<RestoranDTO> toDTO(List<Restoran> restorani){
	    List<RestoranDTO> dto = new ArrayList<RestoranDTO>();
	    for (Restoran r: restorani){
	        dto.add(new RestoranDTO(r));
        }
	    return dto;
    }

	public static void sortirajRestorane(List<Restoran> restorani, String sort){
        if (sort.equals("ocenaOpadajuce")){
            Collections.sort(restorani, (Restoran r1, Restoran r2) -> Double.compare(r2.getOcena(), r1.getOcena()));
        }
        else if (sort.equals("ocenaRastuce")){
            Collections.sort(restorani, (Restoran r1, Restoran r2) -> Double.compare(r1.getOcena(), r2.getOcena()));
        }
        else if (sort.equals("nazivRastuce")){
            Collections.sort(restorani, (Restoran r1, Restoran r2) ->{
                return r1.getNaziv().compareToIgnoreCase(r2.getNaziv());
            });
        }
        else if (sort.equals("nazivOpadajuce")){
            Collections.sort(restorani, (Restoran r1, Restoran r2) ->{
                return r2.getNaziv().compareToIgnoreCase(r1.getNaziv());
            });
        }
        else if (sort.equals("lokacijaRastuce")){
            Collections.sort(restorani, (Restoran r1, Restoran r2) ->{
                return r1.getLokacija().getMesto().compareToIgnoreCase(r2.getLokacija().getMesto());
            });
        }
        else if (sort.equals("lokacijaOpadajuce")){
            Collections.sort(restorani, (Restoran r1, Restoran r2) ->{
                return r2.getLokacija().getMesto().compareToIgnoreCase(r1.getLokacija().getMesto());
            });
        }
    }

    public List<String> dobaviSlobodneMenadzere() {
	    List<String> menadzeri = new ArrayList<String>();
	    for (Menadzer m: this.menadzeri.values()){
	        if (m.getRestoran() == null){
	            menadzeri.add(m.getKorisnickoIme());
            }
        }
	    return menadzeri;
    }

    public List<Artikal> dobaviArtikleRestorana(String restoran) throws Exception {
	    if (!this.restorani.containsKey(restoran)){
	        throw new Exception("Ne postoji zadati restoran!");
        }
	    List<Artikal> artikli = new ArrayList<Artikal>();
	    for (String artikal: this.restorani.get(restoran).getArtikli()){
	        artikli.add(this.artikli.get(artikal));
        }
	    return artikli;
    }

    public Artikal dodajArtikalRestoranu(Artikal artikal) throws Exception {
        if (!this.restorani.containsKey(artikal.getRestoran())){
            throw new Exception("Ne postoji zadati restoran!");
        }
        if (!proveriArtikal(artikal)){
            throw new Exception("Nevalidan novi artikal");
        }
        Restoran r = this.restorani.get(artikal.getRestoran());
        if (r.getArtikli().contains(artikal.getNaziv())){
            throw new Exception("Vec postoji artikal sa odabranim imenom, izaberite drugo ime!");
        }
        Random rand = new Random();
        String id = rand.nextInt(20000) + "";
        artikal.setId(id);
        this.artikli.put(id, artikal);
        r.getArtikli().add(artikal.getId());
        return artikal;
    }

    // TODO: videti da li treba dozvoliti izmenu naziva artikla
    public Artikal izmeniArtikal(String id, Artikal artikal) throws Exception {

	    if (!proveriArtikal(artikal)){
            throw new Exception("Nevalidan izmenjeni artikal!");
        }
        if (!this.artikli.containsKey(id)){
            throw new Exception("Ovaj artikal ne postoji!");
        }
//        if (!this.artikli.get(artikal.getNaziv()).getRestoran().equals(artikal.getRestoran())) {
//            throw new Exception("Nije dozvoljeno menjanje restorana kom artikal pripada!");
//        }
        Artikal a = artikli.get(id);
        if (!nazivJeJedinstven(id, a.getRestoran(), artikal.getNaziv()))
            throw new Exception("Vec postoji proizvod tog naziva u restoranu!");
        a.setNaziv(artikal.getNaziv());
        a.setCena(artikal.getCena());
        a.setKolicina(artikal.getKolicina());
        a.setOpis(artikal.getOpis());
        a.setSlika(artikal.getSlika());
        a.setTip(artikal.getTip());
	    return artikal;
    }

    private boolean nazivJeJedinstven(String id, String restoran, String naziv) {
	    for (String s: this.restorani.get(restoran).getArtikli()){
	        if (s.equals(id)) continue; // da ne bismo racunali da je sam taj artikal ima isti naziv
	        if (this.artikli.get(s).getNaziv().equals(naziv)) return false;
        }
	    return true;
    }

    public boolean obrisiArtikal(String artikal, String restoran) throws Exception {

        if (!this.artikli.containsKey(artikal)){
            throw new Exception("Ovaj artikal ne postoji!");
        }

        if (!this.artikli.get(artikal).getRestoran().equals(restoran)){
            throw new Exception("Moguce je obrisati artikal samo iz restorana kojim menadzer rukovodi!");
        }

        // TODO: zabraniti brisanje ako je artikal u nekoj narudzbini

        this.artikli.remove(artikal);
        this.restorani.get(restoran).getArtikli().remove(artikal);
        return true;
    }


    private boolean proveriArtikal(Artikal artikal) {
	    // TODO: proveriti da li su vrednosti dozvoljene
	    return true;
    }
	
}



