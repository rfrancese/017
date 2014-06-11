package it.unisa.mytraveldiary.entity;

public class Trasporto {
	private String tipologia;
	private String compagnia;
	private String citt‡Partenza;
	private String citt‡Arrivo;
	private int valutazione;
	private int id;
	
	public Trasporto(){
	
	}
	
	public Trasporto(String tip, String comp, String citt‡P, String citt‡A, int val, int i) {
		tipologia=tip;
		compagnia= comp;
		citt‡Partenza= citt‡P;
		citt‡Arrivo= citt‡A;
		valutazione= val;
		id= i;
	}

	
	// Metodi di accesso
	
	public String getTipologia() {
		return tipologia;
	}
	
	public String getCompagnia(){
		return compagnia;
	}
	
	public String getCitt‡Partenza(){
		return citt‡Partenza;
	}
	
	public String getCitt‡Arrivo(){
		return citt‡Arrivo;
	}
	
	public int getValutazione(){
		return valutazione;
	}
	
	public int getId(){
		return id;
	}
	
	
	
	// Metodi di modifica
	
	public void setTipologia(String tip) {
		tipologia=tip;
	}
	
	public void setCompagnia(String comp){
		 compagnia= comp;
	}
	
	public void setCitt‡Partenza(String citt‡P){
		citt‡Partenza= citt‡P;
	}
	
	public void setCitt‡Arrivo(String citt‡A){
		citt‡Arrivo= citt‡A;
	}
	
	public void setValutazione(int val){
	   valutazione=val;
	}
	
	public void setId(int i){
		id= i;
	}
	
	public String toString(){
		String s ="";
		
		s+="Tipologia: "+tipologia+"\n";
		s+="Compagnia: "+compagnia+"\n";
		s+="Citt‡Partenza: "+citt‡Partenza+"\n";
		s+="Citt‡Arrivo: "+citt‡Arrivo+"\n";
		s+="Valutazione: "+valutazione+"\n";
		s+="Id: "+id+"\n";
		
		return s;
	}
}
