package it.unisa.mytraveldiary.entity;

public class Trasporto {
	private String tipologia;
	private String compagnia;
	private String citt‡Partenza;
	private String citt‡Ritorno;
	private int valutazione;
	private int id;
	
	public Trasporto(){
	
	}
	
	public Trasporto(String tip, String comp, String citt‡P, String citt‡R, int val, int i) {
		tipologia=tip;
		compagnia= comp;
		citt‡Partenza= citt‡P;
		citt‡Ritorno= citt‡R;
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
	
	public String getCitt‡Ritorno(){
		return citt‡Ritorno;
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
	
	public void setCitt‡Ritorno(String citt‡R){
		citt‡Ritorno= citt‡R;
	}
	
	public void setValutazione(int val){
	   valutazione=val;
	}
	
	public void setId(int i){
		id= i;
	}
}
