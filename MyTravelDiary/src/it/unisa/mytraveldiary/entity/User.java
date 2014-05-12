package it.unisa.mytraveldiary.entity;

public class User {

	private String username;
	private String password;
	private String nome;
	private String cognome;
	private String localita;
	private int id;
	
	public User() {
		
	}
	
	public User(String u, String p, String n, String c, String l) {
		username=u;
		password=p;
		setNome(n);
		setCognome(c);
		setLocalita(l);
	}
	
	public User(String u, String p, String n, String c, String l, int i) {
		username=u;
		password=p;
		setNome(n);
		setCognome(c);
		setLocalita(l);
		id=i;
	}
	
	// Metodi di accesso
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public String getLocalita() {
		return localita;
	}
	
	public int getId() {
		return id;
	}
	
	// Metodi di modifica
	
	public void setUsername(String u) {
		username=u;
	}
	
	public void setPassword(String p) {
		password=p;
	}
	
	public void setNome(String n) {
		nome=n;
	}
	
	public void setCognome(String c) {
		cognome=c;
	}
	
	public void setLocalita(String l) {
		localita=l;
	}
	
	public void setId(int i) {
		id=i;
	}
}
