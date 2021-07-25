package model;

public class Korisnik {
	
	private String korisnickoIme;
	private String lozinka;
	private String ime;
	private String prezime;
	private Pol pol;
	private long datumRodjenja;

	public Korisnik(){

	}

	public Korisnik(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, long datumRodjenja) {
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.datumRodjenja = datumRodjenja;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public Pol getPol() {
		return pol;
	}

	public void setPol(Pol pol) {
		this.pol = pol;
	}

	public long getDatumRodjenja() {
		return datumRodjenja;
	}

	public void setDatumRodjenja(long datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	public boolean equals(Object o) {
		
		if(o instanceof Korisnik) {
			Korisnik k = (Korisnik)o;
			
			return k.getKorisnickoIme().equals(korisnickoIme)
					&& k.getLozinka().equals(lozinka);
		}
		
		return false;
	}




}
