package main;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;
import static spark.Spark.staticFiles;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import DTO.*;
import com.google.gson.Gson;

import model.*;
import spark.Request;
import spark.Session;

public class Main {
	
	private static Gson g = new Gson();	
	private static Aplikacija aplikacija = new Aplikacija();


	private static String path = new File(new File("").getAbsolutePath() + "/static/data/aplikacija.json").getAbsolutePath();

    public static String denied = "<h2>Access denied</h2>";
    
	private static File file = new File(path);
	
	private static String odbijeno = "<h1>Odbijeno!</h1>";
	private static String badRequest = "<h1>Los zahtev!</h1>";
	
	public static void upisUFajl() {
		//Create the file
		try {
			if (file.createNewFile())
			{
			  //  System.out.println("File is created!");
			} else {
			   // System.out.println("File already exists.");
			}
			//Write Content
			FileWriter writer = new FileWriter(file);
			writer.write(g.toJson(aplikacija));
			System.out.println(g.toJson(aplikacija));
			writer.close();
			
		} catch (IOException e) {
			System.out.println("Greska");
		}
	}
	

	public static void citanjeIzFajla() {
		aplikacija.getKupci().put("kupac1", new Kupac("kupac1", "lozinka1", "kupkinja", "kupic",Pol.ZENSKI, 1000));
		System.out.println(aplikacija.getKupci().size() + " je velicina");

		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String st; 
			String json = "";
			try {
				while ((st = br.readLine()) != null) {
					json += st;
				}
			} catch (IOException e) {
				System.out.println("greska");
			}
			
			aplikacija = g.fromJson(json, Aplikacija.class);
			
		} catch (FileNotFoundException e) {
			System.out.println("ne valja putanja");
		}
	}
	
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		port(8081);
		

		//citanjeIzFajla();
		//upisUFajl();
		
		staticFiles.externalLocation(new File("./static").getCanonicalPath());


		//
		// ------------------ KORISNICI --------------------
		//
		
		post("/login", (req, res) -> {
			 res.type("application/json");
			 LoginDTO k = g.fromJson(req.body(), LoginDTO.class);

			 if (k.getKorisnickoIme() == null || k.getLozinka() == null) {
			 	res.status(400);
			 	return res;
			 }

			 try {
			 	Session ss = req.session(true);
			 	Korisnik korisnik = aplikacija.login(k.getKorisnickoIme(), k.getLozinka());
			 	ss.attribute("user", korisnik);
			 	res.status(200);
			 	return g.toJson(korisnik);

			 } catch (Exception e){
			 	res.status(400);
			 	return "Losi kredencijali";
			 }

		});

		post("/registracijaKupca", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik != null) {
				res.status(400);
				return "Vec ste ulogovani, ne mozete izvrsiti registraciju";
			}
			KorisnikDTO k = g.fromJson(req.body(), KorisnikDTO.class);

			if (k.getUloga().equals(Uloga.ADMINISTRATOR) || k.getUloga().equals(Uloga.DOSTAVLJAC) ||
				k.getUloga().equals(Uloga.MENADZER)){
				res.status(400);
				return res.body();
			}

			try {
				KorisnikDTO novi = aplikacija.registracijaKorisnika(k);
				res.status(200);
				return g.toJson(novi);

			} catch(Exception e){
				res.status(400);
				return e.getMessage();
			}


		});

		post("/registracijaDM", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return res; 		//?
			}
			else if (!(trenutniKorisnik instanceof Administrator)){
				res.status(403);
				return res;		//?
			}
			KorisnikDTO k = g.fromJson(req.body(), KorisnikDTO.class);
			try {
				KorisnikDTO novi = aplikacija.registracijaKorisnika(k);
				return g.toJson(novi);
			} catch(Exception e){
				res.status(400);
				return e.getMessage();
			}
		});


		get("/logout", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik user = ss.attribute("user");
			if (user != null) {
				ss.invalidate();
			}
			return "{\"loggedOut\": true}";
		});


		get("/mojiPodaci", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return res; 		//?
			}
			res.status(200);
			return g.toJson(trenutniKorisnik);
		});

		put("/mojiPodaci", (req, res) -> {		// izmena podataka
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return res;
			}
			KorisnikDTO k = g.fromJson(req.body(), KorisnikDTO.class);
			// ne moze da menja nekog drugog
			if (!trenutniKorisnik.getKorisnickoIme().equals(k.getKorisnickoIme())){
				res.status(400);
				return "";
			}

			try {
				Korisnik izmenjeni = aplikacija.izmeniPodatke(k);
				res.status(200);
				return g.toJson(izmenjeni);

			} catch(Exception e){
				res.status(400);
				return e.getMessage();
			}

		});

		put("/izmenaLozinke", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return res;
			}
			IzmenaLozinkeDTO dto = g.fromJson(req.body(), IzmenaLozinkeDTO.class);
			try {
				aplikacija.izmeniLozinku(trenutniKorisnik.getKorisnickoIme(), dto.getStaraLozinka(), dto.getNovaLozinka());
				res.status(200);
				return "";

			} catch(Exception e){
				res.status(400);
				return e.getMessage();
			}
		});


		// ako nije kupac, u jsonu nece ni biti polje tipKupca (ne znam zasto)
		get("/sviKorisnici", (req, res) -> {
			res.type("application/json");
			int indicator = isAdministrator(req);
			if (indicator != 1){
				res.status(indicator);
				return "";
			}
			List<KorisnikProsirenoDTO> korisnici = aplikacija.dobaviSveKorisnike();
			res.status(200);
			return g.toJson(korisnici);
		});

		get("/pretragaKorisnika", (req, res) -> {
			res.type("application/json");
			int indicator = isAdministrator(req);
			if (indicator != 1){
				res.status(indicator);
				return "";
			}
			String pretraga = req.queryParams("pretraga");
			String uloga = req.queryParams("uloga");
			String tip = req.queryParams("tip"); 		// vrv se misli na tip kupca a ne kip korisnika!!
			String sort = req.queryParams("sort");

			List<KorisnikProsirenoDTO> korisnici = aplikacija.pretraziKorisnike(pretraga, uloga, tip, sort);
			return g.toJson(korisnici);
		});


		//
		// ------------------ RESTORANI --------------------
		//

		// dobavljanje restorana po nazivu
		get("/restorani/:restoran", (req, res) -> {
			res.type("application/json");
			try {
				String r = req.params("restoran");
				RestoranDTO restoran = aplikacija.dobaviRestoran(r);
				return g.toJson(restoran);
			} catch(Exception e) {
				res.status(200);
				return e.getMessage();
			}
		});

		// dobavljanje restorana za koji je zaduzen menadzer
		get("/restorani/m/mojRestoran", (req, res) -> {
			// null ako mu nije dodeljen restoran
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			else if (!(trenutniKorisnik instanceof Menadzer)) {
				res.status(403);
				return "";
			}
			String r = ((Menadzer) trenutniKorisnik).getRestoran();
			try {
				RestoranDTO restoran = aplikacija.dobaviRestoran(r);
				return g.toJson(restoran);
			} catch(Exception e) {
				res.status(200);
				return e.getMessage();
			}
		});

		post("/kreiranjeRestorana", (req, res) -> {
			// naziv, tip, lokacija, logo, menadzer
			res.type("application/json");
			int indicator = isAdministrator(req);
			if (indicator != 1){
				res.status(indicator);
				return "";
			}
			RestoranDTO dto = g.fromJson(req.body(), RestoranDTO.class);
			try {
				aplikacija.dodajRestoran(dto);
				res.status(200);
				return dto;

			} catch(Exception e) {
				res.status(400);
				return e.getMessage();
			}

		});

		// svi restorani
		get("/restorani", (req, res) -> {
			// prvo oni koji su otvoreni
			List<RestoranDTO> restorani = aplikacija.dobaviSveRestorane();
			return g.toJson(restorani);
		});

		get("/pretragaRestorana", (req, res) -> {
			// pretraga po nazivu, tipu, lokaciji (grad ili drzava), prosecna ocena

			// prikazati naziv tip lokaciju logo prosecnu ocenu

			// sortiranje po nazivu, lokaciji, prosecnoj oceni

			// filter po tipu ili samo otvoreni
			String pretraga = req.queryParams("pretraga");
			String otvoren = req.queryParams("otvoren");
			String tip = req.queryParams("tip"); 		// vrv se misli na tip kupca a ne kip korisnika!!
			String sort = req.queryParams("sort");

			List<RestoranDTO> restorani = aplikacija.pretragaRestorana(pretraga, tip, otvoren, sort);
			return g.toJson(restorani);
		});

		get("/dostupniMenadzeri", (req, res) -> {
			res.type("application/json");
			int indicator = isAdministrator(req);
			if (indicator != 1){
				res.status(indicator);
				return "";
			}
			List<String> menadzeri = aplikacija.dobaviSlobodneMenadzere();
			res.status(200);
			return g.toJson(menadzeri);
		});


		//
		// ------------------ ARTIKLI --------------------
		//

		get("/artikli/:idRestorana", (req, res) -> {
			res.type("application/json");
			String restoran = req.params("idRestorana");
			try {
				List<Artikal> artikli = aplikacija.dobaviArtikleRestorana(restoran);
				res.status(200);
				return g.toJson(artikli);
			}catch(Exception e){
				res.status(400);
				return e.getMessage();
			}

		});

		post("/dodajArtikal", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			else if (!(trenutniKorisnik instanceof Menadzer)) {
				res.status(403);
				return "";
			}
			Artikal dto = g.fromJson(req.body(), Artikal.class);
			dto.setRestoran(((Menadzer) trenutniKorisnik).getRestoran());
			try {
				Artikal a = aplikacija.dodajArtikalRestoranu(dto);
				res.status(200);
				return g.toJson(a);
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});

		delete("/obrisiArtikal/:artikal", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			else if (!(trenutniKorisnik instanceof Menadzer)) {
				res.status(403);
				return "";
			}
			String artikal = req.params("artikal");
			String restoran =((Menadzer) trenutniKorisnik).getRestoran();
			try {
				aplikacija.obrisiArtikal(artikal, restoran);
				res.status(200);
				return "";
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});

		put("/izmeniArtikal/:idArtikla", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			else if (!(trenutniKorisnik instanceof Menadzer)) {
				res.status(403);
				return "";
			}
			Artikal artikal = g.fromJson(req.body(), Artikal.class);
			String idArtikla = req.params("idArtikla");
			try {
				aplikacija.izmeniArtikal(idArtikla, artikal);
				res.status(200);
				return "";
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});



		//
		// ------------------ PORUDZBINE --------------------
		//

		// ovo jos malo istestirati
		get("/porudzbine", (req, res) -> {
			// menadzer svoje
			// dostavljac sve koje su u statusu ceka dostavljaca i sve one za koje su oni zaduzeni
			// kupac svoje, i ovde da moze odabrati nedostavljene
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			String korisnickoIme = trenutniKorisnik.getKorisnickoIme();
			if (trenutniKorisnik instanceof Menadzer) {
				try {
					List<PorudzbinaDTO> porudzbine = aplikacija.dobaviPorudzbineMenadzera(korisnickoIme);
					res.status(200);
					return g.toJson(porudzbine);
				} catch (Exception e){
					res.status(400);
					return e.getMessage();
				}
			}

			if (trenutniKorisnik instanceof Dostavljac){
				List<PorudzbinaDTO> porudzbine = aplikacija.dobaviPorudzbineDostavljaca(korisnickoIme);
				return g.toJson(porudzbine);
			}

			if (trenutniKorisnik instanceof Kupac){
				List<PorudzbinaDTO> porudzbine = aplikacija.dobaviPorudzbineKupca(korisnickoIme);
				return g.toJson(porudzbine);
			}
			return "";
		});

		// dobavljanje jedne porudzbine na osnovu id-ja (bez artikala)
		get("/porudzbine/:porudzbina", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			String porudzbinaId = req.params("porudzbina");
			try {
				aplikacija.proveriKorisnikaZaNarudzbenicu(trenutniKorisnik, porudzbinaId); 	// da li korisnik moze da vidi artikle porudzbenice
				PorudzbinaDTO porudzbina = aplikacija.dobaviPorudzbinu(porudzbinaId);
				res.status(200);
				return g.toJson(porudzbina);
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});

		get("/porudzbinaArtikli/:porudzbina", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			String porudzbina = req.params("porudzbina");
			try {
				aplikacija.proveriKorisnikaZaNarudzbenicu(trenutniKorisnik, porudzbina); 	// da li korisnik moze da vidi artikle porudzbenice
				List<ArtikalPorudzbenica> artikli = aplikacija.dobaviArtiklePorudzbine(porudzbina);
				res.status(200);
				return g.toJson(artikli);
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});

		put("/promeniStatusPorudzbine/:porudzbina/:noviStatus", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			String porudzbina = req.params("porudzbina");
			try {
				StatusPorudzbine noviStatus = StatusPorudzbine.valueOf(req.params("noviStatus"));
				aplikacija.proveriKorisnikaZaNarudzbenicu(trenutniKorisnik, porudzbina); 	// da li korisnik moze da vidi artikle porudzbenice
				Porudzbina p = aplikacija.promeniStatusPorudzbine(porudzbina, noviStatus, trenutniKorisnik);
				res.status(200);
				return g.toJson(p);
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});

		post("/zahtevZaDostavom/:porudzbina", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			if (!(trenutniKorisnik instanceof Dostavljac)){
				res.status(403);
				return "";
			}
			String porudzbina = req.params("porudzbina");
			try {
				aplikacija.kreirajZahtevZaDostavom(porudzbina, trenutniKorisnik);
				res.status(200);
				return "";
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});

		put("/odgovoriNaZahtev/:zahtev", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			if (!(trenutniKorisnik instanceof Menadzer)){
				res.status(403);
				return "";
			}

			try {
				String zahtev = req.params("zahtev");
				boolean odobreno = Boolean.parseBoolean(req.queryParams("odobreno"));
				aplikacija.proveriOdgovorNaZahtev(trenutniKorisnik, zahtev);
				aplikacija.odgovoriNaZahtevZaDostavom(zahtev, odobreno);
				res.status(200);
				return "";
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}

		});

		get("/zahtevi", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			if (!(trenutniKorisnik instanceof Menadzer)){
				res.status(403);
				return "";
			}

			try {
				String restoran = ((Menadzer) trenutniKorisnik).getRestoran();
				if (restoran == null) throw new Exception("Nema restorana za ovog menadzera!");
				List<ZahtevZaDostavom> zahtevi = aplikacija.dobaviZahteveZaRestoran(restoran);
				res.status(200);
				return g.toJson(zahtevi);
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});


		// filter i pretragu uraditi


		//
		// ------------------ KOMENTARI --------------------
		//

		post("/komentar/:restoran", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			if (!(trenutniKorisnik instanceof Kupac)){
				res.status(403);
				return "";
			}
			try {
				Komentar komentar = g.fromJson(req.body(), Komentar.class);
				komentar.setKorisnik(trenutniKorisnik.getKorisnickoIme());
				String restoran = req.params("restoran");
				if (restoran != null)
					komentar.setRestoran(restoran);
				else throw new Exception("Bad request!");
				if  (aplikacija.proveriKorisnikaZaKomentar(trenutniKorisnik, restoran))
					aplikacija.dodajKomentar(komentar);
				else throw new Exception("Ne mozete oceniti taj restoran");
				res.status(200);
				return "";
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});

		put("/resiKomentar/:komentar/:odobren", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			if (!(trenutniKorisnik instanceof Menadzer)){
				res.status(403);
				return "";
			}
			try {
				String komentar = req.params("komentar");
				boolean odobren = Boolean.parseBoolean(req.params("odobren"));
				aplikacija.resiKomentar(komentar, odobren);
				res.status(200);
				return "";
			} catch(Exception e){
				res.status(400);
				return e.getMessage();
			}

		});

		get("/komentari/:restoran", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");

			try {
				String restoran = req.params("restoran");
				List<Komentar> komentari;
				// kupac/dostavljac vide samo odobrene komentare
				if ((trenutniKorisnik instanceof Kupac) || (trenutniKorisnik instanceof Dostavljac) || trenutniKorisnik == null){
					komentari = aplikacija.dobaviKomentareRestorana(restoran, false);
				}
				else {		// ako je menadzer ili administrator, vidi sve komentare
					komentari = aplikacija.dobaviKomentareRestorana(restoran, true);
				}
				res.status(200);
				return g.toJson(komentari);
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});

		// true ili false, da li kupac moze da oceni neki restoran, za front treba
		get("/komentari/kupac/proveri/:restoran", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			if (!(trenutniKorisnik instanceof Kupac)){
				res.status(403);
				return "";
			}
			try {
				String restoran = req.params("restoran");
				boolean sme = aplikacija.proveriKorisnikaZaKomentar(trenutniKorisnik, restoran);
				res.status(200);
				return sme;
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}

		});

		delete("/komentari/:komentar", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			if (!(trenutniKorisnik instanceof Kupac)){
				res.status(403);
				return "";
			}
			try {
				String komentar = req.params("komentar");
				aplikacija.obrisiKomentar(komentar, trenutniKorisnik.getKorisnickoIme());
				res.status(200);
				return "";
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});


	}

	private static int isAdministrator(Request req){
		Session ss = req.session(true);
		Korisnik trenutniKorisnik = ss.attribute("user");
		if(trenutniKorisnik == null) return 401;
		else if (!(trenutniKorisnik instanceof Administrator)) return 403;
		return 1;
	}

	private static int isMenadzer(Request req){
		Session ss = req.session(true);
		Korisnik trenutniKorisnik = ss.attribute("user");
		if(trenutniKorisnik == null) return 401;
		else if (!(trenutniKorisnik instanceof Menadzer)) return 403;
		return 1;
	}

}
