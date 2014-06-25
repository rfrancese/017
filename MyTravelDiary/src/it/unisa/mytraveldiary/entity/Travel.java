package it.unisa.mytraveldiary.entity;

public class Travel {
	
	private String tipologia;
	private String località;
	private String dataAndata;
	private String dataRitorno;
	private String compagniViaggio;
	private String descrizione;
	private int id;
	
	public Travel(){
	
	}
	
	public Travel(String tipViaggio, String loc, String dataA, String dataR, String compViaggio, 
			      String descr, int i) {
					
		if (tipViaggio==null) 
			tipologia="";
		else
			tipologia=tipViaggio;
		
		if (loc==null) 
			località="";
		else
		località=loc;
		
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
	
	
	
	// Metodi di modifica
	
	public void setTipologiaViaggio(String tipViaggio) {		
		tipologia=tipViaggio;
	}
	
	public void setLocalità(String loc){
		 località= loc;
	}
	
	public void setDataAndata(String dataA){
		dataAndata= dataA;
	}
	
	public void setDataRitorno(String dataR){
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