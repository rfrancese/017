package it.unisa.mytraveldiary.entity;

import java.util.ArrayList;

public class Travel {

	private String tipologia;
	private ArrayList<Localita> localita;
	private String dataAndata;
	private String dataRitorno;
	private String compagniViaggio;
	private String descrizione;
	private int u_id;
	private int id;

	public Travel(){

	}

	public Travel(String tipViaggio, ArrayList<Localita> loc, String dataA, String dataR, String compViaggio, 
			String descr, int u_i, int i) {

		if (tipViaggio==null) 
			tipologia="";
		else
			tipologia=tipViaggio;

		localita=loc;

		if (dataA==null) 
			dataAndata="";
		else 
			dataAndata=dataA;

		if (dataR==null) 
			dataRitorno="";
		else
			dataRitorno=dataR;

		if (compViaggio==null) 
			compagniViaggio="";
		else
			compagniViaggio=compViaggio;

		if (descr==null)
			descrizione="";
		else
			descrizione=descr;
		u_id=u_i;
		id=i;
	}


	// Metodi di accesso

	public String getTipologiaViaggio() {		
		return tipologia;
	}

	public ArrayList<Localita> getLocalita(){
		return localita;
	}
	
	public String getLocalitaString() {
		String s="";
		
		for (Localita l: localita)
			s+=(l.toString()+", ");
		
		return s;
	}

	public String getDataAndata(){
		return dataAndata;
	}

	public String getDataRitorno(){
		return dataRitorno;
	}

	public String getCompagniViaggio(){
		return compagniViaggio;
	}

	public String getDescrizione(){
		return descrizione;
	}

	public int getId(){
		return id;
	}

	public int getUId() {
		return u_id;
	}
	
	// Metodi di modifica

	public void setTipologiaViaggio(String tipViaggio) {		
		tipologia=tipViaggio;
	}

	public void setLocalità(ArrayList<Localita> loc){
		localita=loc;
	}

	public void setDataAndata(String dataA){
		if (dataA==null)
			dataAndata="";
		else
			dataAndata=dataA;
	}

	public void setDataRitorno(String dataR){
		if (dataR==null)
			dataRitorno="";
		else
			dataRitorno=dataR;
	}

	public void setCompagniViaggio(String compViaggio){
		if (compViaggio==null)
			compagniViaggio="";
		else
			compagniViaggio=compViaggio;
	}

	public void setDescrizione(String descr){
		if (descr==null)
			descrizione="";
		else
			descrizione=descr;
	}

	public void setId(int i){
		id=i;
	}
	
	public void setUId(int u_i) {
		u_id=u_i;
	}

	public String toString() {
		String s="";

		s+="Tipologia viaggio: "+getTipologiaViaggio()+"\n";
		s+="Localita: ";
		
		if (localita!=null)
			for (Localita l: localita)
				s+=l+"\n";

		s+="Data andata:  "+"\n";
		s+="Data ritorno: "+dataRitorno+"\n";
		s+="Descrizione: "+descrizione+"\n";
		s+="Compagni viaggio: "+compagniViaggio+"\n";
		s+="Id: "+id+"\n";

		return s;
	}
}