package it.unisa.mytraveldiary.entity;

public class User {

	private String username;
	private String password;
	private int id;
	
	public User() {
		
	}
	
	public User(String u, String p) {
		username=u;
		password=p;
	}
	
	public User(String u, String p, int i) {
		username=u;
		password=p;
		id=i;
	}
	
	// Metodi di accesso
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
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
	
	public void setId(int i) {
		id=i;
	}
	
}
