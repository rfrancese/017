package it.unisa.mytraveldiary.entity;

public class Localita {

	private String nome;
	private int t_id;
	private int id;
	
	public Localita() {
		
	}
	
	public Localita(String n) {
		if (n==null)
			nome="";
		else
			nome=n;
	}
	
	public Localita(String n, int t_i) {
		if (n==null)
			nome="";
		else
			nome=n;
		
		t_id=t_i;
	}
	
	public Localita(String n, int t_i, int i) {
		if (n==null)
			nome="";
		else
			nome=n;
		
		t_id=t_i;
		id=i;
	}
	
	public String getNome() {
		return nome;
	}
	
	public int getTId() {
		return t_id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setNome(String n) {
		if (n==null)
			nome="";
		else 
			nome=n;
	}
	
	public void setTId(int t_i) {
		t_id=t_i;
	}
	
	public void setId(int i) {
		id=i;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
