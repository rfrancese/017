package it.unisa.mytraveldiary.entity;

import java.util.Date;

public class Travel {
	
	private String tipologia;
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
					
		tipologia=tipViaggio;
		località=loc;
		dataAndata=dataA;
		dataRitorno=dataR;
		compagniViaggio=compViaggio;
		descrizione=descr;
		id=i;
	}

	
	// Metodi di accesso
	
	public String getTipologiaViaggio() {		
		return tipologia;
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
		return compagniViaggio;
	}
	
	public String getDescrizione(){
		return descrizione;
	}
	
	public int getId(){
		return id;
	}
	
	
	
	// Metodi di modifica
	
	public void setTipologiaViaggio(String tipViaggio) {		
		tipologia=tipViaggio;
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
		s+="Localita: "+località+"\n";
		
		if (dataAndata==null) 
			s+="Data andata:  "+"\n";
		else
			s+="Data andata: "+dataAndata+"\n";
		
		if (dataRitorno==null)
			s+="Data ritorno:  "+"\n";
		else
			s+="Data ritorno: "+dataRitorno+"\n";
		
		s+="Descrizione: "+descrizione+"\n";
		s+="Compagni viaggio: "+compagniViaggio+"\n";
		s+="Id: "+id+"\n";
		
		return s;
	}
}