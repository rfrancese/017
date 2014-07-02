package it.unisa.mytraveldiary.entity;

public class Museo {

	private String tipologia;
	private String nome;
	private Localita localita;
	private int valutazione;
	private int id;
	private int t_id;

	public Museo() {

	}

	public Museo(String tip, String n, Localita l, int val) {
		if (tip==null)
			tipologia="";
		else
			tipologia=tip;

		if (n==null)
			nome="";
		else
			nome=n; 

		localita=l;
		valutazione=val;
	}

	public Museo(String tip, String n, Localita l, int val, int t_i, int i) {
		if (tip==null)
			tipologia="";
		else
			tipologia=tip;

		if (n==null)
			nome="";
		else
			nome=n; 

		localita=l;
		valutazione=val;
		t_id=i;
		id=i;
	}

	// Metodi di accesso

	public String getTipologia() {
		return tipologia;
	}

	public String getNome() {
		return nome;
	}

	public Localita getLocalita() {
		return localita;
	}

	public int getValutazione() {
		return valutazione;
	}
	
	public int getTId() {
		return t_id;
	}

	public int getId() {
		return id;
	}

	// Metodi di modifica

	public void setTipologia(String tip) {
		if (tip==null)
			tipologia="";
		else
			tipologia=tip;
	}

	public void setNome(String n) {
		if (n==null)
			nome="";
		else
			nome=n;
	}

	public void setLocalita(Localita l) {
		localita=l;
	}

	public void setValutazione(int val) {
		valutazione=val;
	}
	
	public void setTId(int i) {
		t_id=i;
	}

	public void setId(int i) {
		id=i;
	}

	public String toString(){
		String s ="";

		s+=(tipologia+": "+nome+"\n");
		s+="Località : "+localita+"\n";
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

