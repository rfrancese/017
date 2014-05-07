package it.unisa.mytraveldiary.entity;

public class Museo {

	private String[] tipologia={"Storico", "Archeologico", "Arte", "Scientifico", "Marittimo o oceanografico", "Militari o di guerra"};
	private boolean storico, archeologico, arte, scientifico, marittimo, militari;
	private String nome;
	private String citta;
	private int valutazione;
	private int id;
	
	public Museo() {
		
	}
	
	public Museo(String tip, String n, String c, int val) {
		if (tip==tipologia[0])
			storico=true;
		else if (tip==tipologia[1])
			archeologico=true;
		else if (tip==tipologia[2])
			arte=true;
		else if (tip==tipologia[3])
			scientifico=true;
		else if (tip==tipologia[4])
			marittimo=true;
		else if (tip==tipologia[5])
			militari=true;
		
		nome=n; 
		citta=c;
		valutazione=val;
	}
	
	public Museo(String tip, String n, String c, int val, int i) {
		if (tip==tipologia[0])
			storico=true;
		else if (tip==tipologia[1])
			archeologico=true;
		else if (tip==tipologia[2])
			arte=true;
		else if (tip==tipologia[3])
			scientifico=true;
		else if (tip==tipologia[4])
			marittimo=true;
		else if (tip==tipologia[5])
			militari=true;
		
		nome=n; 
		citta=c;
		valutazione=val;
		id=i;
	}
	
	// Metodi di accesso
	
		public String getTipologia() {
			String tip="";
			
			if (storico)
				tip=tipologia[0];
			
			else if(archeologico) 
				tip=tipologia[1];
			
			else if(arte) 
				tip=tipologia[2];
			
			else if(scientifico) 
				tip=tipologia[3];
			
			else if(marittimo) 
				tip=tipologia[4];
			
			else if(militari) 
				tip=tipologia[5];
			
			return tip;
		}
		
		public String getNome() {
			return nome;
		}
		
		public String getCittà() {
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
			if (tip==tipologia[0])
				storico=true;
			else if (tip==tipologia[1])
				archeologico=true;
			else if (tip==tipologia[2])
				arte=true;
			else if (tip==tipologia[3])
				scientifico=true;
			else if (tip==tipologia[4])
				marittimo=true;
			else if (tip==tipologia[5])
				militari=true;
		}
		
		public void setNome(String n) {
			nome=n;
		}
		
		public void setCittà(String c) {
			citta=c;
		}
		
		public void setValutazione(int val) {
			valutazione=val;
		}
		
		public void setId(int i) {
			id=i;
		}
}

