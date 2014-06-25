package it.unisa.mytraveldiary.entity;

public class HotelRistorante {

	private String tipologia;
	private String nome;
	private String citta;
	private int valutazione;
	private int id;
	private int t_id;
	
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
	
	public HotelRistorante(String tip, String n, String c, int val, int t_i, int i) {
		tipologia=tip;
		nome=n;
		citta=c;
		valutazione=val;
		t_id=t_i;
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
	
	public int getTId() {
		return t_id;
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
	
	public void setTId(int i) {
		t_id=i;
	}
	
	public String toString() {
		String s="";
		
		/*s+="Tipologia: "+tipologia+"\n";
		s+="Nome: "+nome+"\n";
		s+="Città: "+citta+"\n";
		s+="Valutazione: "+valutazione+"\n";
		s+="Id: "+id+"\n";*/
		
		s+=(tipologia+": "+nome+"\n");
		s+="Località: "+citta+"\n";
		s+="Valutazione: ";
		
		switch (valutazione) {
			case 1:
				s+="*";
				break;
				
			case 2:
				s+="**";
				break;
				
			case 3:
				s+="***";
				break;
				
			case 4:
				s+="****";
				break;
				
			case 5:
				s+="*****";
				break;
				
			default:
				s+=" ";
		}
		
		return s;
	}
}
