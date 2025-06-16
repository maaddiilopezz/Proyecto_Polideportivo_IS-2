package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Actividad {

    @EmbeddedId
    private ActividadId id;

    private double precio;
    private int plazasMaximas;

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Sesion> sesiones = new ArrayList<>();

    // ✅ Constructor vacío necesario para JPA
    public Actividad() {
    }

    public Actividad(String nombreActividad, int gradoExigencia, double precio) {
        this.id = new ActividadId(nombreActividad, gradoExigencia);
        this.precio = precio;
        this.sesiones = new ArrayList<>();
    }

    public String getNombreActividad() {
        return id != null ? id.getNombreActividad() : null;
    }

    public void setNombreActividad(String nombreActividad) {
        if (id == null)
            id = new ActividadId();
        id.setNombreActividad(nombreActividad);
    }

    public int getGradoExigencia() {
        return id != null ? id.getGradoExigencia() : 0;
    }

    public void setGradoExigencia(int gradoExigencia) {
        if (id == null)
            id = new ActividadId();
        id.setGradoExigencia(gradoExigencia);
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getPlazasMaximas() {
        return plazasMaximas;
    }

    public void setPlazasMaximas(int plazasMaximas) {
        this.plazasMaximas = plazasMaximas;
    }

    public List<Sesion> getSesiones() {
        return sesiones;
    }

    public void setSesiones(List<Sesion> sesiones) {
        this.sesiones = sesiones;
    }

    public void addSesion(Sesion sesion) {
        if (sesiones == null) {
            sesiones = new ArrayList<>();
        }
        sesiones.add(sesion);
        sesion.setActividad(this); // Asegura la relación bidireccional
    }

    public void removeSesion(Sesion sesion) {
        sesiones.remove(sesion);
        sesion.setActividad(null); // Limpia la referencia inversa
    }

    @Override
    public String toString() {
        return "Actividad [nombreActividad=" + getNombreActividad() + ", gradoExigencia=" + getGradoExigencia()
                + ", precio=" + precio + ", sesiones=" + sesiones + "]";
    }
}
