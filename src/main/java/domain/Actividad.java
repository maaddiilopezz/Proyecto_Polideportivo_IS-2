package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Actividad implements Serializable {
    @Id
    @XmlID
    private String id;

    private String nombreActividad;
    private int gradoExigencia;
    private double precio;
    private int plazasMaximas;
    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @XmlTransient
    private List<Sesion> sesiones = new ArrayList<>();

    // ✅ Constructor vacío necesario para JPA
    public Actividad() {
        this.id = UUID.randomUUID().toString();
    }

    public Actividad(String nombreActividad, int gradoExigencia, double precio) {
        this(); // Llama al constructor vacío para generar el ID
        this.nombreActividad = nombreActividad;
        this.gradoExigencia = gradoExigencia;
        this.precio = precio;
        this.sesiones = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public int getGradoExigencia() {
        return gradoExigencia;
    }

    public void setGradoExigencia(int gradoExigencia) {
        this.gradoExigencia = gradoExigencia;
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Actividad actividad = (Actividad) o;
        return id == actividad.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Actividad [id=" + id + ", nombreActividad=" + nombreActividad + ", gradoExigencia=" + gradoExigencia
                + ", precio=" + precio + ", plazasMaximas=" + plazasMaximas + "]";
    }
}
