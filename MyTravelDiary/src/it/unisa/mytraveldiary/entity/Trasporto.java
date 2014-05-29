package it.unisa.mytraveldiary.entity;

public class Trasporto {
	private String[] tipologia = {"Automobile", "Pullman", "Treno", "Aereo", "Nave"};
	private boolean automobile, pullman, treno, aereo, nave;
	private String compagnia;
	private String citt‡Partenza;
	private String citt‡Ritorno;
	private int valutazione;
	private int id;
	
	public Trasporto(){
	
	}
	
	public Trasporto(String tip, String comp, String citt‡P, String citt‡R, int val, int i) {
					
		if(tip.equals(tipologia[0]))
			automobile=true;
		else if(tip.equals(tipologia[1]))
			pullman=true;
		else if(tip.equals(tipologia[2]))
			treno=true;
		else if(tip.equals(tipologia[3]))
			aereo=true;
		else if(tip.equals(tipologia[4]))
			nave=true;
		
		compagnia= comp;
		citt‡Partenza= citt‡P;
		citt‡Ritorno= citt‡R;
		valutazione= val;
		id= i;
	}

	
	// Metodi di accesso
	
	public String getTipologia() {
		
		String tip="";
		
		if(automobile)
			tip= tipologia[0];
		
		else if(pullman)
			tip= tipologia[1];
		
		else if(treno)
			tip= tipologia[2];
		
		else if(aereo)
			tip= tipologia[3];
		
		else if(nave)
			tip= tipologia[4];
		
		return tip;
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

		if(tip.equals(tipologia[0]))
			automobile=true;
		else if(tip.equals(tipologia[1]))
			pullman=true;
		else if(tip.equals(tipologia[2]))
			treno=true;
		else if(tip.equals(tipologia[3]))
			aereo=true;
		else if(tip.equals(tipologia[4]))
			nave=true;
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
