package it.unisa.mytraveldiary.entity;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Travel {
	
	private String[] tipologiaViaggio = {"Svago", "Lavoro"};
	private String località;
	private GregorianCalendar dataAndata;
	private GregorianCalendar dataRitorno;
	private ArrayList<User> compagniViaggio;
	private String descrizione;
	private int id;
	
	public Travel(){
	
	}
	
	public Travel (String[] tipViaggio, String loc, GregorianCalendar dataA, GregorianCalendar dataR, 
			ArrayList<User> compViaggio, String descr, int i) {
		tipologiaViaggio= tipViaggio;
		località= loc;
		dataAndata= dataA;
		dataRitorno= dataR;
		compagniViaggio= compViaggio;
		descrizione= descr;
		id= i;
	}

	
	// Metodi di accesso
	
	public String[] getTipologiaViaggio() {
		return tipologiaViaggio;
	}
	
	public String getLocalità(){
		return località;
	}
	
	public GregorianCalendar getDataAndata(){
		return dataAndata;
	}
	
	public GregorianCalendar getDataRitorno(){
		return dataRitorno;
	}
	
	public ArrayList<User> getCompagniViaggio(){
		return compagniViaggio;
	}
	
	public String getDescrizione(){
		return descrizione;
	}
	
	public int getId(){
		return id;
	}
	
	
	
	// Metodi di modifica
	
	public void setTipologiaViaggio(String[] tipViaggio) {
		tipologiaViaggio= tipViaggio;
	}
	
	public void setLocalità(String loc){
		 località= loc;
	}
	
	public void setDataAndata(GregorianCalendar dataA){
		dataAndata= dataA;
	}
	
	public void setDataRitorno(GregorianCalendar dataR){
		dataRitorno= dataR;
	}
	
	public void setCompagniViaggio(ArrayList<User> compViaggio){
		compagniViaggio= compViaggio;
	}
	
	public void setDescrizione(String descr){
		descrizione= descr;
	}
	
	public void setId(int i){
		id= i;
	}

}
