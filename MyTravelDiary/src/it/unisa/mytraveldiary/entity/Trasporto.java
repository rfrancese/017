package it.unisa.mytraveldiary.entity;

public class Trasporto {
	private String tipologia;
	private String compagnia;
	private String citt‡Partenza;
	private String citt‡Arrivo;
	private int valutazione;
	private int id;
	private int t_id;
	
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
	
	public Trasporto(String tip, String comp, String citt‡P, String citt‡A, int val, int t_i, int i) {
		tipologia=tip;
		compagnia= comp;
		citt‡Partenza= citt‡P;
		citt‡Arrivo= citt‡A;
		valutazione= val;
		t_id=t_i;
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
	
	public int getTId(){
		return t_id;
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
	
	public void setTId(int t_i){
		t_id=t_i;
	}
	
	public String toString(){
		String s ="";
		
		s+=(tipologia+": "+compagnia+"\n");
		s+="Localit‡ partenza: "+citt‡Partenza+"\n";
		s+="Localit‡ arrivo: "+citt‡Arrivo+"\n";
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
