package domain;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Encargado extends Usuario {
	@Id
	private String nombre;
	private String dni;
	private String contraseña;
	private String mail;
	
	public Encargado (String nombre, String dni, String contraseña, String mail) {
		this.nombre = nombre;
		this.mail = mail;
		this.contraseña = contraseña;
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public String getContraseña() {
		return contraseña;
	}
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	@Override
	public String toString() {
		return "Encargado [nombre=" + nombre + ", dni=" + dni + ", contraseña=" + contraseña + ", mail=" + mail + "]";
	}
 


}
