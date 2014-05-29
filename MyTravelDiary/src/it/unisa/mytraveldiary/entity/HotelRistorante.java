package it.unisa.mytraveldiary.entity;

public class HotelRistorante {

	private String tipologia;
	private String nome;
	private String citta;
	private int valutazione;
	private int id;
	
	public HotelRistorante() {
		
	}
	
	public HotelRistorante(String tip, String n, String c, int val) {
		tipologia=tip;
		nome=n;
		citta=c;
		valutazione=val;
	}
	
	public HotelRistorante(String tip, String n, String c, int val, int i) {
		tipologia=tip;
		nome=n;
		citta=c;
		valutazione=val;
		id=i;
	}
	
	// Metodi di accesso
	
	public String getTipologia() {		
		return tipologia;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getCitta() {
		return citta;
	}
	
	public int getValutazione() {
		return valutazione;
	}
	
	public int getId() {
		return id;
	}
	
	// Metodi di modifica
	
	public void setTipologia(String tip) {
		tipologia=tip;
	}
	
	public void setNome(String n) {
		nome=n;
	}
	
	public void setCitta(String c) {
		citta=c;
	}
	
	public void setValutazione(int val) {
		valutazione=val;
	}
	
	public void setId(int i) {
		id=i;
	}
}
