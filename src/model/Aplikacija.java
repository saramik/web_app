package model;

import DTO.KorisnikDTO;
import DTO.KorisnikProsirenoDTO;
import DTO.PorudzbinaDTO;
import DTO.RestoranDTO;

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
	private HashMap<String, ZahtevZaDostavom> zahteviZaDostavom = new HashMap<>();

	public Aplikacija(){
        kupci.put("kupac1", new Kupac("kupac1", "lozinka", "ana", "sasic",Pol.ZENSKI, 1000));
        Kupac k = new Kupac("kupac2", "lozinka", "kupkinja", "kupic",Pol.ZENSKI, 199);
        k.setSakupljeniBodovi(200);
        kupci.put("kupac2", k);
        administratori.put("admin", new Administrator("admin", "admin", "admin", "admin",Pol.ZENSKI, 1000));
        dostavljaci.put("dost", new Dostavljac("dost", "admin", "d", "d",Pol.ZENSKI, 1000));
        menadzeri.put("menadzerko", new Menadzer("menadzerko", "lozinka", "d", "d",Pol.ZENSKI, 1000));

        menadzeri.get("menadzerko").setRestoran("r1");

        menadzeri.put("m", new Menadzer("m", "lozinka", "d", "d",Pol.ZENSKI, 1000));
        menadzeri.get("m").setRestoran("r2");

        Lokacija l = new Lokacija();
        Restoran r = new Restoran("r1", TipRestorana.ITALIJANSKI, l, "", 480l, 900l);
        r.setMenadzer("menadzerko");
        restorani.put(r.getNaziv(), r);

        Restoran r2 = new Restoran("r2", TipRestorana.KINESKI, l, "", 480l, 900l);
        restorani.put(r2.getNaziv(), r2);
        r2.setMenadzer("m");

        Artikal a = new Artikal("a1", 10.0, TipArtikla.JELO, 0.0, "opis a1", "", "r1");
        a.setId("123");
        // TODO: novom dodeliti id
        r.getArtikli().add("123");
        artikli.put("123", a);


        Porudzbina p = new Porudzbina("12345", "r1", 1000, 100.0, "kupac1", StatusPorudzbine.CEKA_DOSTAVLJACA);
        porudzbine.put(p.getId(), p);
        this.kupci.get("kupac1").getPorudzbine().add("12345");
        p.getArtikli().add(new ArtikalPorudzbenica("111", "artikal1", 1, 123));
        p.getArtikli().add(new ArtikalPorudzbenica("222", "artikal2", 1, 123));


        Porudzbina pe = new Porudzbina("54321", "r1", 20000, 150.0, "kupac2", StatusPorudzbine.U_PRIPREMI);
        porudzbine.put(pe.getId(), pe);
        this.kupci.get("kupac2").getPorudzbine().add("54321");


        Porudzbina p2 = new Porudzbina("123", "r2", 5, 10.0, "kupac2", StatusPorudzbine.DOSTAVLJENA);
        porudzbine.put(p2.getId(), p2);
        this.kupci.get("kupac2").getPorudzbine().add("123");

        Porudzbina p3 = new Porudzbina("321", "r1", 123123, 3300.0, "kupac2", StatusPorudzbine.DOSTAVLJENA);
        porudzbine.put(p3.getId(), p3);
        this.kupci.get("kupac2").getPorudzbine().add("321");

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
	        if (!r.isAktivan()) continue;
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
            if (!r.isAktivan()) continue;
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

    public boolean obrisiRestoran(String restoran) throws Exception {
	    Restoran r = restorani.get(restoran);
	    if (r == null) throw new Exception("Nepostojeci restoran!");
	    r.setAktivan(false);
	    this.menadzeri.get(r.getMenadzer()).setRestoran(null);
	    return true;
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
	        if (this.artikli.get(artikal).isAktivan()) artikli.add(this.artikli.get(artikal));
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
        artikal.setAktivan(true);
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
	        if (this.artikli.get(s).getNaziv().equals(naziv) && this.artikli.get(s).isAktivan()) return false;
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

        this.artikli.get(artikal).setAktivan(false);
        //this.restorani.get(restoran).getArtikli().remove(artikal);
        return true;
    }


    private boolean proveriArtikal(Artikal artikal) {
	    // TODO: proveriti da li su vrednosti dozvoljene
	    return true;
    }

    public List<PorudzbinaDTO> dobaviPorudzbineMenadzera(String korisnickoIme) throws Exception {
	    String restoran = this.menadzeri.get(korisnickoIme).getRestoran();
	    if (restoran == null) throw new Exception("Ovom menadzeru nije dodeljen restoran!");
	    List<PorudzbinaDTO> porudzbineDTO = new ArrayList<>();
	    for (Porudzbina p: this.porudzbine.values()){
	        if (p.getRestoran().equals(restoran)) porudzbineDTO.add(toPorudzbinaDTO(p));
        }
	    return porudzbineDTO;
    }

    public List<PorudzbinaDTO> dobaviPorudzbineDostavljaca(String korisnickoIme, boolean samoNedostavljene) {
        List<PorudzbinaDTO> porudzbineDTO = new ArrayList<>();
	    // njegove porudzbine
        for (String p: this.dostavljaci.get(korisnickoIme).getPorudzbine()){
	        Porudzbina porudzbina = this.porudzbine.get(p);
	        if (samoNedostavljene && porudzbina.getStatus().equals(StatusPorudzbine.DOSTAVLJENA)) continue;
            porudzbineDTO.add(toPorudzbinaDTO(porudzbina));
        }
        // porudzbine koje nemaju dostavljaca
	    for (Porudzbina p: porudzbine.values()){
	        if (p.getStatus().equals(StatusPorudzbine.CEKA_DOSTAVLJACA))
                porudzbineDTO.add(toPorudzbinaDTO(p));
        }
	    return porudzbineDTO;
    }

    // kad menadzer odobri, onda se dodaje u listu dostavljaca (i promeni se status)


    // nedostavljene su filter
    public List<PorudzbinaDTO> dobaviPorudzbineKupca(String korisnickoIme, boolean samoNedostavljene) {
        List<PorudzbinaDTO> porudzbineDTO = new ArrayList<>();

        for (String p: kupci.get(korisnickoIme).getPorudzbine()){
            Porudzbina porudzbina = this.porudzbine.get(p);
            if (samoNedostavljene && porudzbina.getStatus().equals(StatusPorudzbine.DOSTAVLJENA)) continue;
            porudzbineDTO.add(toPorudzbinaDTO(porudzbina));
        }
        return porudzbineDTO;
    }

    private PorudzbinaDTO toPorudzbinaDTO(Porudzbina porudzbina){
	    Kupac k = kupci.get(porudzbina.getKupac());
	    PorudzbinaDTO dto = new PorudzbinaDTO(porudzbina.getId(), porudzbina.getRestoran(), porudzbina.getDatum(),
                porudzbina.getCena(), k.getIme(), k.getPrezime(), porudzbina.getStatus());
	    return dto;
    }

    public boolean proveriKorisnikaZaNarudzbenicu(Korisnik trenutniKorisnik, String porudzbina) throws Exception {
	    if (trenutniKorisnik instanceof Administrator) throw new Exception("Administrator ne moze da vidi porudbenice!");

	    if (!this.porudzbine.containsKey(porudzbina))
	        throw new Exception("Nepostojeca porudzbina!");

	    Porudzbina p = this.porudzbine.get(porudzbina);

	    if (trenutniKorisnik instanceof Menadzer){
	        if (((Menadzer) trenutniKorisnik).getRestoran() == null) throw new Exception("Nedozvoljen pristup!");
	        if (this.menadzeri.get(trenutniKorisnik.getKorisnickoIme()).getRestoran().equals(p.getRestoran()))
	            return true;
	        throw new Exception("Menadzer ne moze da vidi porudzbinu koja nije iz njegovog restorana!");
        }

        if (trenutniKorisnik instanceof Dostavljac) {
            if (this.porudzbine.get(porudzbina).getStatus().equals(StatusPorudzbine.CEKA_DOSTAVLJACA)) return true;
            if (this.dostavljaci.get(trenutniKorisnik.getKorisnickoIme()).getPorudzbine().contains(porudzbina))
                return true;
            throw new Exception("Porudzbenica nije dostupna dostavljacu!");
        }

        if (trenutniKorisnik instanceof Kupac){
	        if (this.kupci.get(trenutniKorisnik.getKorisnickoIme()).getPorudzbine().contains(porudzbina))
	            return true;
	        throw new Exception("Kupac moze da vidi samo svoje porudzbine!");
        }
	    throw new Exception("Desila se neka greska!");
    }


    public List<ArtikalPorudzbenica> dobaviArtiklePorudzbine(String porudzbina) throws Exception {

	    if (!this.porudzbine.containsKey(porudzbina))
	        throw new Exception("Nepostojeca porudzbina!");
	    return this.porudzbine.get(porudzbina).getArtikli();

    }

    public PorudzbinaDTO dobaviPorudzbinu(String porudzbinaId) throws Exception {
	    if (!this.porudzbine.containsKey(porudzbinaId)) throw new Exception("Nepostojeca porudzbina!");
	    return toPorudzbinaDTO(this.porudzbine.get(porudzbinaId));
    }

    public Porudzbina promeniStatusPorudzbine(String porudzbina, StatusPorudzbine noviStatus, Korisnik korisnik) throws Exception {
        if (!this.porudzbine.containsKey(porudzbina)) throw new Exception("Nepostojeca porudzbina!");

        Porudzbina p = this.porudzbine.get(porudzbina);

        if (korisnik instanceof Menadzer){
            if (p.getStatus().equals(StatusPorudzbine.OBRADA)){
                if (!noviStatus.equals(StatusPorudzbine.U_PRIPREMI)){
                    throw new Exception("Neodgovarajuci redosled statusa porudzbine!");
                }
                p.setStatus(StatusPorudzbine.U_PRIPREMI);
                return p;
            }
            else if (p.getStatus().equals(StatusPorudzbine.U_PRIPREMI)) {
                if (!noviStatus.equals(StatusPorudzbine.CEKA_DOSTAVLJACA)){
                    throw new Exception("Neodgovarajuci redosled statusa porudzbine!");
                }
                p.setStatus(StatusPorudzbine.CEKA_DOSTAVLJACA);
                return p;
            }
            else if (p.getStatus().equals(StatusPorudzbine.CEKA_DOSTAVLJACA)) {
                if (!noviStatus.equals(StatusPorudzbine.U_TRANSPORTU)){
                    throw new Exception("Neodgovarajuci redosled statusa porudzbine!");
                }
                p.setStatus(StatusPorudzbine.U_TRANSPORTU);
                return p;
            }
            else throw new Exception("Nedozvoljeno!");
        }
        else if (korisnik instanceof Dostavljac){
            if (p.getStatus().equals(StatusPorudzbine.U_TRANSPORTU)) {
                if (!noviStatus.equals(StatusPorudzbine.DOSTAVLJENA)){
                    throw new Exception("Neodgovarajuci redosled statusa porudzbine!");
                }
                p.setStatus(StatusPorudzbine.DOSTAVLJENA);
                return p;
            }
            else throw new Exception("Nedozvoljeno!");

        }
        else if (korisnik instanceof Kupac){
            if (p.getStatus().equals(StatusPorudzbine.OBRADA)) {
                if (!noviStatus.equals(StatusPorudzbine.OTKAZANA)){
                    throw new Exception("Neodgovarajuci redosled statusa porudzbine!");
                }
                p.setStatus(StatusPorudzbine.OTKAZANA);
                return p;
            }
            else throw new Exception("Nedozvoljeno!");
        }
        else throw new Exception("Nedozvoljeno!");
    }

    public boolean kreirajZahtevZaDostavom(String porudzbina, Korisnik trenutniKorisnik) throws Exception {

	    if (this.porudzbine.containsKey(porudzbina)) throw new Exception("Nepostojeca porudzba!");

        Porudzbina p = this.porudzbine.get(porudzbina);
        if (!p.getStatus().equals(StatusPorudzbine.CEKA_DOSTAVLJACA))
            throw new Exception("Nije moguce kreirati zahtev za dostavu za porudzbinu koja nema status CEKA_DOSTAVLJACA");
        Random rand = new Random();
        String id = rand.nextInt(2000) + "";
        ZahtevZaDostavom zahtev = new ZahtevZaDostavom(id, porudzbina,
                trenutniKorisnik.getKorisnickoIme(), Status.NA_CEKANJU, p.getRestoran());
        this.zahteviZaDostavom.put(id, zahtev);
        return true;
    }

    public boolean odgovoriNaZahtevZaDostavom(String zahtev, boolean odobreno) throws Exception {

        ZahtevZaDostavom z = this.zahteviZaDostavom.get(zahtev);
        if (z == null) throw new Exception("Nepostojeci zahtev!");

        if (!odobreno){
            z.setStatus(Status.ODBIJENO);
            return false;
        }
        // odobreno je
        z.setStatus(Status.PRIHVACENO);
        Dostavljac dostavljac = dostavljaci.get(z.getKorisnickoImeDostavljaca());
        dostavljac.getPorudzbine().add(z.getPorudzbina());
        porudzbine.get(z.getPorudzbina()).setStatus(StatusPorudzbine.U_TRANSPORTU);
        // odbiti sve ostale zahteve za istu narudzbinu
        for (ZahtevZaDostavom zd: this.zahteviZaDostavom.values()){
            if (!zd.getId().equals(zahtev) && zd.getPorudzbina().equals(z.getPorudzbina())
                    && zd.getStatus().equals(Status.NA_CEKANJU))
                zd.setStatus(Status.ODBIJENO);
        }
       return true;
    }

    public List<ZahtevZaDostavom> dobaviZahteveZaRestoran(String restoran){
	    // proveri da li menadzer ima pristup tom restoranu
	    List<ZahtevZaDostavom> zahtevi = new ArrayList<>();
	    for (ZahtevZaDostavom zahtev: zahteviZaDostavom.values()){
	        if (zahtev.getRestoran().equals(restoran)) zahtevi.add(zahtev);
        }
	    return zahtevi;
    }

    public void proveriOdgovorNaZahtev(Korisnik trenutniKorisnik, String zahtev) throws Exception {
        String restoran = this.menadzeri.get(trenutniKorisnik.getKorisnickoIme()).getRestoran();
        ZahtevZaDostavom z = this.zahteviZaDostavom.get(zahtev);
        if (!z.getRestoran().equals(restoran)) throw new Exception("Menadzer nema pristup ovom restoranu!");
	}

    public boolean proveriKorisnikaZaKomentar(Korisnik trenutniKorisnik, String restoran) throws Exception {
	    // da li ima ijednu porudzbinu iz tog restorana koja je dostavljena
        // ako ima i ako nema komentar na taj restoran, moze da oceni
        if (!this.restorani.containsKey(restoran)) throw new Exception("Nema tog restorana!");
        boolean dostavljeno = false;
        for (String porudzbina: kupci.get(trenutniKorisnik.getKorisnickoIme()).getPorudzbine()){
            Porudzbina p = this.porudzbine.get(porudzbina);
            if (p.getStatus().equals(StatusPorudzbine.DOSTAVLJENA) && p.getRestoran().equals(restoran)) {
                dostavljeno = true;
                break;
            }
        }
        if (!dostavljeno) return false;
        for (Komentar k: komentari.values()){       // i ako je kom neobrisan, status prihvacen
            if (!k.getAktivan()) continue;
            if (k.getRestoran().equals(restoran) && k.getKorisnik().equals(trenutniKorisnik.getKorisnickoIme()) &&
                !k.getStatus().equals(Status.ODBIJENO))
                return false;
        }
        return true;
    }

    public void dodajKomentar(Komentar komentar) {
	    // id, postavi status, restoran proveri
        komentar.setStatus(Status.NA_CEKANJU);
        komentar.setAktivan(true);
        komentar.setId(new Random().nextInt(2000) + "");
        this.komentari.put(komentar.getId(), komentar);
    }

    public List<Komentar> dobaviKomentareRestorana(String restoran, boolean svi){

	    List<Komentar> komentari = new ArrayList<Komentar>();
	    for (Komentar k: this.komentari.values()){
	        if (k.getRestoran().equals(restoran)){
	            if (!k.getAktivan()) continue;   // ako je obrisan
                if (!svi){  // samo prihvaceni
                    if (k.getStatus().equals(Status.PRIHVACENO)) komentari.add(k);
                }
                else {
                    komentari.add(k);
                }
	        }
        }
	    return komentari;
    }

    public boolean obrisiKomentar(String komentar, String korisnickoIme) throws Exception {
	    Komentar k = komentari.get(komentar);
	    if (k == null)  throw new Exception("Nepostojeci komentar!");
	    if (!k.getKorisnik().equals(korisnickoIme)) throw new Exception("Ne mozete obrisati ovaj komentar!");
	    k.setAktivan(false);
	    // promeniti ocenu restorana
        Restoran r = restorani.get(k.getRestoran());
        if (r.getBrojOcena() == 1){
            r.setBrojOcena(0);
            r.setOcena(0.0);
            return true;
        }
        int stariBrojOcena = r.getBrojOcena();
        double staraOcena = r.getOcena();
        double novaOcena = (staraOcena * stariBrojOcena - k.getOcena())/(stariBrojOcena - 1);
        r.setOcena(novaOcena);
        r.setBrojOcena(stariBrojOcena - 1);
        return true;
    }

    public boolean resiKomentar(String komentar, boolean prihvacen) throws Exception {
	    Komentar k = this.komentari.get(komentar);

	    if (!k.getStatus().equals(Status.NA_CEKANJU))
	        throw new Exception("Ovaj komentar nije na cekanju!");

	    if (!prihvacen){
	        k.setStatus(Status.ODBIJENO);
	        return true;
        }
	    k.setStatus(Status.PRIHVACENO);
	    // uvrsti ocenu
        Restoran r = this.restorani.get(k.getRestoran());
        int stariBrojOcena = r.getBrojOcena();
        double stariProsek = r.getOcena();
        r.setBrojOcena(stariBrojOcena + 1);
        double noviProsek = (stariProsek * stariBrojOcena + k.getOcena())/(stariBrojOcena + 1);
        r.setOcena(noviProsek);
        return true;
    }

    public RestoranDTO dobaviRestoran(String r) throws Exception {
	    if (!this.restorani.containsKey(r)) throw new Exception("Nepostojeci restoran!");
	    return new RestoranDTO(this.restorani.get(r));
    }

    public List<PorudzbinaDTO> pretragaPorudzbina(Korisnik korisnik, String restoran, Double cenaOd, Double cenaDo, Long datumOd, Long datumDo, TipRestorana tipRestorana, StatusPorudzbine statusPorudzbine, String sort) throws Exception {
        List<PorudzbinaDTO> porudzbine = this.porudzbineNaOsnovuKorisnika(korisnik);
        List<PorudzbinaDTO> porudzbinePretrazene = new ArrayList<>();
        for (PorudzbinaDTO p: porudzbine){
            if (restoran != null && !restoran.equals("")){
                if (!p.getRestoran().toLowerCase().contains(restoran.toLowerCase())) continue;
            }
            if (cenaOd != null) {
                if (p.getCena() < cenaOd) continue;
            }
            if (cenaDo != null){
                if (p.getCena() > cenaDo) continue;
            }
            if (datumOd != null){
                if (p.getDatum() < datumOd) continue;
            }
            if (datumDo != null) {
                if (p.getDatum() > datumDo) continue;
            }
            if (tipRestorana != null){
                TipRestorana tr = this.restorani.get(p.getRestoran()).getTip();
                if (!tipRestorana.equals(tr)) continue;
            }
            if (statusPorudzbine != null) {
                if (!p.getStatus().equals(statusPorudzbine)) continue;
            }
            porudzbinePretrazene.add(p);
        }
        sortirajPorudzbine(porudzbinePretrazene, sort);
        return porudzbinePretrazene;
	}

	public List<PorudzbinaDTO> porudzbineNaOsnovuKorisnika(Korisnik korisnik) throws Exception {

        if (korisnik instanceof Kupac) return this.dobaviPorudzbineKupca(korisnik.getKorisnickoIme(), false);
        if (korisnik instanceof Dostavljac) return this.dobaviPorudzbineDostavljaca(korisnik.getKorisnickoIme(), false);
        if (korisnik instanceof Menadzer) return this.dobaviPorudzbineMenadzera(korisnik.getKorisnickoIme());
        // admin vidi sve porudzbine
        List<PorudzbinaDTO> porudzbine = new ArrayList<>();
        for (Porudzbina p: this.porudzbine.values()){
               porudzbine.add(toPorudzbinaDTO(p));
        }
        return porudzbine;
    }


    private void sortirajPorudzbine(List<PorudzbinaDTO> porudzbine, String sort) {

        if (sort.equals("imeRestoranaRastuce")) {
            Collections.sort(porudzbine, (PorudzbinaDTO p1, PorudzbinaDTO p2) -> {
                return p1.getRestoran().compareToIgnoreCase(p2.getRestoran());
            });
        } else if (sort.equals("imeRestoranaOpadajuce")) {
            Collections.sort(porudzbine, (PorudzbinaDTO p1, PorudzbinaDTO p2) -> {
                return p2.getRestoran().compareToIgnoreCase(p1.getRestoran());
            });
        }
        else if (sort.equals("cenaOpadajuce")){
            Collections.sort(porudzbine, (PorudzbinaDTO p1, PorudzbinaDTO p2) -> Double.compare(p2.getCena(), p1.getCena()));
        }
        else if (sort.equals("cenaRastuce")){
            Collections.sort(porudzbine, (PorudzbinaDTO p1, PorudzbinaDTO p2) -> Double.compare(p1.getCena(), p2.getCena()));
        }
        else if (sort.equals("datumOpadajuce")){
            Collections.sort(porudzbine, (PorudzbinaDTO p1, PorudzbinaDTO p2) -> Long.compare(p2.getDatum(), p1.getDatum()));
        }
        else if (sort.equals("datumRastuce")){
            Collections.sort(porudzbine, (PorudzbinaDTO p1, PorudzbinaDTO p2) -> Long.compare(p1.getDatum(), p2.getDatum()));
        }
    }

    // mozda svi zahtevi za jednu porudzbinu
}




