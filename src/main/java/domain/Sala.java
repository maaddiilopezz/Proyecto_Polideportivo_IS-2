package domain;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Sala {
	@Id
	private String nombreSala;
	private int aforoMaximo;
	private boolean libre;
	@OneToMany
    private ArrayList<Sesion> sesiones;

	
	public Sala(String nombreSala, int aforoMaximo, boolean libre) {
		this.nombreSala = nombreSala;
		this.aforoMaximo = aforoMaximo;
		this.libre = libre;
        this.sesiones = new ArrayList<>();

	}
	public Sala() {
		
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
        return "Sala [nombreSala=" + nombreSala + ", aforoMaximo=" + aforoMaximo + ", libre=" + libre + ", sesiones=" + sesiones + "]";
    }
}
