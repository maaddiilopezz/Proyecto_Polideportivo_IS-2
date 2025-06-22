package domain;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Sala implements Serializable {
	@Id
	@XmlID
	private String nombreSala;
	private int aforoMaximo;
	private boolean libre;
	@OneToMany
	@XmlTransient
	private ArrayList<Sesion> sesiones;

	public Sala(String nombreSala, int aforoMaximo, boolean libre) {
		this.nombreSala = nombreSala;
		this.aforoMaximo = aforoMaximo;
		this.libre = libre;
		this.sesiones = new ArrayList<>();

	}

	public Sala() {
		this.sesiones = new ArrayList<>();
	}

	public String getNombreSala() {
		return nombreSala;
	}

	public void setNombreSala(String nombreSala) {
		this.nombreSala = nombreSala;
	}

	public int getAforoMaximo() {
		return aforoMaximo;
	}

	public void setAforoMaximo(int aforoMaximo) {
		this.aforoMaximo = aforoMaximo;
	}

	public boolean isLibre() {
		return libre;
	}

	public void setLibre(boolean libre) {
		this.libre = libre;
	}

	public ArrayList<Sesion> getSesiones() {
		return sesiones;
	}

	public void setSesiones(ArrayList<Sesion> sesiones) {
		this.sesiones = sesiones;
	}

	public void agregarSesion(Sesion sesion) {
		this.sesiones.add(sesion);
	}

	@Override
	public String toString() {
		return "Sala [nombreSala=" + nombreSala + ", aforoMaximo=" + aforoMaximo + ", libre=" + libre + ", sesiones="
				+ sesiones + "]";
	}
}
