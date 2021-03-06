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
import com.google.gson.*;

import model.*;
import spark.Request;
import spark.Session;

public class Main {
	
	private static Gson g = new GsonBuilder().setPrettyPrinting().create();

	private static Aplikacija aplikacija = new Aplikacija();

	private static String path = new File(new File("").getAbsolutePath() + "/static/data/aplikacija.json").getAbsolutePath();

	private static File file = new File(path);
	
	public static void upisUFajl() {

		try {

			FileWriter writer = new FileWriter(file);
			writer.write(g.toJson(aplikacija));
			writer.close();

		} catch (IOException e) {
			System.out.println("Greska");
		}
	}
	

	public static void citanjeIzFajla() {

		try {

			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String st;
			String json = "";
			while ((st = br.readLine()) != null) json += st;
			aplikacija = g.fromJson(json, Aplikacija.class);


		} catch (FileNotFoundException e) {
			System.out.println("ne valja putanja");
		} catch (IOException e) {
			System.out.println("greska");
		}

	}


	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		port(8081);
		

		citanjeIzFajla();
		upisUFajl();
		//citanjeIzFajla();

		staticFiles.externalLocation(new File("./static").getCanonicalPath());


		//
		// ------------------ KORISNICI --------------------
		//
		
		post("/login", (req, res) -> {
			 res.type("application/json");

			 try {
				 LoginDTO k = g.fromJson(req.body(), LoginDTO.class);

				 if (k.getKorisnickoIme() == null || k.getLozinka() == null) {
					 res.status(400);
					 return res;
				 }
			 	Session ss = req.session(true);
			 	Korisnik korisnik = aplikacija.login(k.getKorisnickoIme(), k.getLozinka());
			 	ss.attribute("user", korisnik);
				res.status(200);
				String json = g.toJson(korisnik, Korisnik.class);
				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(json);
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				jsonObject.addProperty("uloga", korisnik.getClass().toString());
				return jsonObject.toString();
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

			try {
				KorisnikDTO k = g.fromJson(req.body(), KorisnikDTO.class);
				k.setUloga(Uloga.KUPAC);
				KorisnikDTO novi = aplikacija.registracijaKorisnika(k);
				upisUFajl();
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
				return res;
			}
			else if (!(trenutniKorisnik instanceof Administrator)){
				res.status(403);
				return res;
			}

			try {
				KorisnikDTO k = g.fromJson(req.body(), KorisnikDTO.class);
				KorisnikDTO novi = aplikacija.registracijaKorisnika(k);
				upisUFajl();
				res.status(200);
				return g.toJson(novi);
			} catch(Exception e){
				res.status(400);
				return e.getMessage();
			}
		});

		get("/isLoggedIn", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			String loggedIn = "true";
			if (k == null)
				loggedIn = "false";
			else {
				String jsonString = "{'loggedIn':'true'}";
				JsonParser parser = new JsonParser();
				JsonObject jsonObject = (JsonObject) parser.parse(jsonString);
				jsonObject.addProperty("uloga", k.getClass().toString());
				return jsonObject.toString();
			}

			return "{\"loggedIn\":" + loggedIn + "}";
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
				return res;
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

			try {
				KorisnikDTO k = g.fromJson(req.body(), KorisnikDTO.class);
				// ne moze da menja nekog drugog
				if (!trenutniKorisnik.getKorisnickoIme().equals(k.getKorisnickoIme())){
					res.status(400);
					return "";
				}
				Korisnik izmenjeni = aplikacija.izmeniPodatke(k);
				upisUFajl();
				res.status(200);
				return g.toJson(izmenjeni);

			} catch(Exception e){
				res.status(400);
				return e.getLocalizedMessage();
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
			try {
				IzmenaLozinkeDTO dto = g.fromJson(req.body(), IzmenaLozinkeDTO.class);
				aplikacija.izmeniLozinku(trenutniKorisnik.getKorisnickoIme(), dto.getStaraLozinka(), dto.getNovaLozinka());
				upisUFajl();
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
			try {
				String pretraga = req.queryParams("pretraga");
				String uloga = req.queryParams("uloga");
				String tip = req.queryParams("tip"); 		// vrv se misli na tip kupca a ne kip korisnika!!
				String sort = req.queryParams("sort");
				List<KorisnikProsirenoDTO> korisnici = aplikacija.pretraziKorisnike(pretraga, uloga, tip, sort);
				res.status(200);
				return g.toJson(korisnici);
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});

		delete("/obrisiKorisnika/:korisnik", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if (trenutniKorisnik == null) {
				res.status(401);
				return "";
			}
			if (!(trenutniKorisnik instanceof Administrator)) {
				res.status(403);
				return "";
			}
			try {
				String korisnik = req.params("korisnik");
				aplikacija.obrisiKorisnika(korisnik);
				upisUFajl();
				res.status(200);
				return "";
			} catch (Exception e) {
				res.status(400);
				return e.getMessage();
			}
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
			try {
				RestoranDTO dto = g.fromJson(req.body(), RestoranDTO.class);
				aplikacija.dodajRestoran(dto);
				upisUFajl();
				res.status(200);
				return g.toJson(dto);

			} catch(Exception e) {
				res.status(400);
				return e.getMessage();
			}

		});

		// svi restorani
		get("/restorani", (req, res) -> {
			res.type("application/json");
			List<RestoranDTO> restorani = aplikacija.dobaviSveRestorane();
			return g.toJson(restorani);
		});

		get("/pretragaRestorana", (req, res) -> {
			res.type("application/json");
			// pretraga po nazivu, tipu, lokaciji (grad ili drzava), prosecna ocena

			// prikazati naziv tip lokaciju logo prosecnu ocenu

			// sortiranje po nazivu, lokaciji, prosecnoj oceni

			// filter po tipu ili samo otvoreni
			try {
				String pretraga = req.queryParams("pretraga");
				String otvoren = req.queryParams("otvoren");
				String tip = req.queryParams("tip");        // vrv se misli na tip kupca a ne kip korisnika!!
				String sort = req.queryParams("sort");

				List<RestoranDTO> restorani = aplikacija.pretragaRestorana(pretraga, tip, otvoren, sort);
				return g.toJson(restorani);
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
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

		delete("/brisanjeRestorana/:restoran", (req, res) -> {
			res.type("application/json");
			int indicator = isAdministrator(req);
			if (indicator != 1){
				res.status(indicator);
				return "";
			}
			try {
				String restoran = req.params("restoran");
				aplikacija.obrisiRestoran(restoran);
				upisUFajl();
				res.status(200);
				return "";
			} catch(Exception e) {
				res.status(400);
				return e.getMessage();
			}

		});


		//
		// ------------------ ARTIKLI --------------------
		//

		get("/artikli/:idRestorana", (req, res) -> {
			res.type("application/json");
			try {
				String restoran = req.params("idRestorana");
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
			if (!(trenutniKorisnik instanceof Menadzer)) {
				res.status(403);
				return "";
			}

			try {
				Artikal dto = g.fromJson(req.body(), Artikal.class);
				dto.setRestoran(((Menadzer) trenutniKorisnik).getRestoran());
				dto.setAktivan(true);
				Artikal a = aplikacija.dodajArtikalRestoranu(dto);
				upisUFajl();
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
			else if (!(trenutniKorisnik instanceof Administrator)) {
				res.status(403);
				return "";
			}

			try {
				String artikal = req.params("artikal");
				aplikacija.obrisiArtikal(artikal);
				upisUFajl();
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
			try {
				Artikal artikal = g.fromJson(req.body(), Artikal.class);
				String idArtikla = req.params("idArtikla");
				aplikacija.izmeniArtikal(idArtikla, artikal);
				upisUFajl();
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

		// ovo jos malo istestirati ??????????
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
			boolean samoNedostavljene = false;
			try {
				samoNedostavljene = Boolean.parseBoolean(req.queryParams("nedostavljene"));
			} catch (Exception e){}
			if (trenutniKorisnik instanceof Dostavljac){
				List<PorudzbinaDTO> porudzbine = aplikacija.dobaviPorudzbineDostavljaca(korisnickoIme, samoNedostavljene);
				return g.toJson(porudzbine);
			}

			if (trenutniKorisnik instanceof Kupac){
				List<PorudzbinaDTO> porudzbine = aplikacija.dobaviPorudzbineKupca(korisnickoIme, samoNedostavljene);
				return g.toJson(porudzbine);
			}
			if (trenutniKorisnik instanceof Administrator){
				List<PorudzbinaDTO> porudzbine = aplikacija.dobaviSvePorudzbine();
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

			try {
				String porudzbinaId = req.params("porudzbina");
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

			try {
				String porudzbina = req.params("porudzbina");
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

			try {
				String porudzbina = req.params("porudzbina");
				StatusPorudzbine noviStatus = StatusPorudzbine.valueOf(req.params("noviStatus"));
				aplikacija.proveriKorisnikaZaNarudzbenicu(trenutniKorisnik, porudzbina); 	// da li korisnik moze da vidi artikle porudzbenice
				Porudzbina p = aplikacija.promeniStatusPorudzbine(porudzbina, noviStatus, trenutniKorisnik);
				upisUFajl();
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

			try {
				String porudzbina = req.params("porudzbina");
				aplikacija.kreirajZahtevZaDostavom(porudzbina, trenutniKorisnik);
				upisUFajl();
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
				upisUFajl();
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


		get("/pretragaPorudzbina", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik trenutniKorisnik = ss.attribute("user");
			if(trenutniKorisnik == null) {
				res.status(401);
				return "";
			}

			double cenaOd = 0.0;
			double cenaDo = 10000000.0;
			long datumOd = 0;
			long datumDo = 10000000000000000l;
			TipRestorana tipRestorana = null;
			StatusPorudzbine statusPorudzbine = null;
			try{ cenaOd = Double.parseDouble(req.queryParams("cenaOd")); }catch(Exception e){}
			try{ cenaDo = Double.parseDouble(req.queryParams("cenaDo"));} catch(Exception e){}
			try{ datumOd = Long.parseLong(req.queryParams("datumOd")); }catch(Exception e){}
			try{ datumDo = Long.parseLong(req.queryParams("datumDo")); }catch(Exception e){}
			try{ tipRestorana = TipRestorana.valueOf(req.queryParams("tipRestorana"));} catch(Exception e){}
			try{ statusPorudzbine = StatusPorudzbine.valueOf(req.queryParams("statusPorudzbine"));} catch(Exception e){}



			try {
				String restoran = req.queryParams("restoran");

				String sort = req.queryParams("sort");

				List<PorudzbinaDTO> porudzbine = aplikacija.pretragaPorudzbina(trenutniKorisnik,
						restoran, cenaOd, cenaDo, datumOd, datumDo, tipRestorana, statusPorudzbine, sort);
				return g.toJson(porudzbine);
			} catch (Exception e){
				res.status(400);
				return e.getMessage();
			}
		});


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
				upisUFajl();
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
				upisUFajl();
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
			if (!(trenutniKorisnik instanceof Administrator)){
				res.status(403);
				return "";
			}
			try {
				String komentar = req.params("komentar");
				aplikacija.obrisiKomentar(komentar);
				upisUFajl();
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
