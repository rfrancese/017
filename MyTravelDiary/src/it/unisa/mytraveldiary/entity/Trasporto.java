package it.unisa.mytraveldiary.entity;

public class Trasporto {
	private String tipologia;
	private String compagnia;
	private Localita localitaPartenza;
	private Localita localitaArrivo;
	private int valutazione;
	private int id;
	private int t_id;
	
	public Trasporto(){
	
	}
	
	public Trasporto(String tip, String comp, Localita localitaP, Localita localitaA, int val, int t_i) {
		tipologia=tip;
		compagnia= comp;
		localitaPartenza= localitaP;
		localitaArrivo= localitaA;
		valutazione= val;
		t_id=t_i;
	}
	
	public Trasporto(String tip, String comp, Localita localitaP, Localita localitaA, int val, int t_i, int i) {
		tipologia=tip;
		compagnia= comp;
		localitaPartenza= localitaP;
		localitaArrivo= localitaA;
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
	
	public Localita getLocalitaPartenza(){
		return localitaPartenza;
	}
	
	public Localita getLocalitaArrivo(){
		return localitaArrivo;
	}
	
	public int getValutazione(){
		return valutazione;
	}
	
	public int getTId(){
		return t_id;
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
	
	public void setLocalitaPartenza(Localita localitaP){
		localitaPartenza= localitaP;
	}
	
	public void setLocalitaArrivo(Localita localitaA){
		localitaArrivo= localitaA;
	}
	
	public void setValutazione(int val){
	   valutazione=val;
	}
	
	public void setTId(int t_i){
		t_id=t_i;
	}
	
	public void setId(int i){
		id= i;
	}
	
	public String toString(){
		String s ="";
		
		s+=(tipologia+": "+compagnia+"\n");
		s+="Località partenza: "+localitaPartenza+"\n";
		s+="Località arrivo: "+localitaArrivo+"\n";
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
