package it.unisa.mytraveldiary.entity;

public class HotelRistorante {

	private String[] tipologia={"Hotel", "Ristorante"};
	private boolean hotel, ristorante;
	private String nome;
	private String citta;
	private int valutazione;
	private int id;
	
	public HotelRistorante() {
		
	}
	
	public HotelRistorante(String tip, String n, String c, int val) {
		if (tip==tipologia[0])
			hotel=true;
		else if(tip==tipologia[1])
			ristorante=true;
		
		nome=n;
		citta=c;
		valutazione=val;
	}
	
	public HotelRistorante(String tip, String n, String c, int val, int i) {
		if (tip==tipologia[0])
			hotel=true;
		else if(tip==tipologia[1])
			ristorante=true;
		
		nome=n;
		citta=c;
		valutazione=val;
		id=i;
	}
	
	// Metodi di accesso
	
	public String getTipologia() {
		String tip="";
		
		if (hotel)
			tip=tipologia[0];
		
		else if(ristorante) 
			tip=tipologia[1];
		
		return tip;
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
		if (tip.equals(tipologia[0]))
			hotel=true;
		else if (tip.equals(tipologia[1]))
			ristorante=true;
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
