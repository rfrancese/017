package it.unisa.mytraveldiary.entity;

import java.util.ArrayList;
import java.util.Date;

public class Travel {
	
	private String[] tipologiaViaggio = {"Svago", "Lavoro"};
	private boolean svago, lavoro;
	private String località;
	private Date dataAndata;
	private Date dataRitorno;
	private String compagniViaggio;
	private String descrizione;
	private int id;
	
	public Travel(){
	
	}
	
	public Travel(String tipViaggio, String loc, Date dataA, Date dataR, String compViaggio, 
			      String descr, int i) {
					
		if(tipViaggio.equals(tipologiaViaggio[0]))
						svago=true;
					else if(tipViaggio.equals(tipologiaViaggio[1]))
						lavoro=true;
		località= loc;
		dataAndata= dataA;
		dataRitorno= dataR;
		compagniViaggio= compViaggio;
		descrizione= descr;
		id= i;
	}

	
	// Metodi di accesso
	
	public String getTipologiaViaggio() {
		
		String tipViaggio="";
		
		if(svago)
			tipViaggio+= tipologiaViaggio[0];
		
		else if(lavoro)
			tipViaggio+= tipologiaViaggio[1];
		
		return tipViaggio;
	}
	
	public String getLocalità(){
		return località;
	}
	
	public Date getDataAndata(){
		return dataAndata;
	}
	
	public Date getDataRitorno(){
		return dataRitorno;
	}
	
	public String getCompagniViaggio(){
		String compViaggio="";
		
		return compViaggio;
	}
	
	public String getDescrizione(){
		return descrizione;
	}
	
	public int getId(){
		return id;
	}
	
	
	
	// Metodi di modifica
	
	public void setTipologiaViaggio(String tipViaggio) {

		if(tipViaggio.equals(tipologiaViaggio[0]))
			svago=true;
		else if(tipViaggio.equals(tipologiaViaggio[1]))
			lavoro=true;
	}
	
	public void setLocalità(String loc){
		 località= loc;
	}
	
	public void setDataAndata(Date dataA){
		dataAndata= dataA;
	}
	
	public void setDataRitorno(Date dataR){
		dataRitorno= dataR;
	}
	
	public void setCompagniViaggio(String compViaggio){
	   compagniViaggio=compViaggio;
	}
	
	public void setDescrizione(String descr){
		descrizione= descr;
	}
	
	public void setId(int i){
		id= i;
	}


	public String toString() {
		String s="";
		
		s+="Tipologia viaggio: "+getTipologiaViaggio()+"\n";
		s+="Localita: "+località;
		s+="Data andata: "+dataAndata+"\n";
		s+="Data ritorno: "+dataRitorno+"\n";
		s+="Descrizione: "+descrizione+"\n";
		s+="Compagni viaggio: "+compagniViaggio+"\n";
		s+="Id: "+id+"\n";
		
		return s;
	}
}