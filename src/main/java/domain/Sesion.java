package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Sesion implements Serializable {
    @Id
    @XmlID
    private String id;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaImparticion;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaInicio;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaFinal;
    private int plazasOcupadas;
    @ManyToOne(cascade = CascadeType.ALL)
    private Actividad actividad;

    @ManyToOne(cascade = CascadeType.ALL)
    private Sala sala; // Si necesitas persistir reservas, esta línea requiere definir la relación en
    // Reserva
    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true)
    @XmlTransient
    private List<Reserva> reservas;

    @Transient
    private List<Socio> listaEspera = new ArrayList<>();

    public Sesion() {
        this.id = UUID.randomUUID().toString();
        reservas = new ArrayList<>();
        listaEspera = new ArrayList<>();
    }
    public List<Socio> getListaEspera() {
        return listaEspera;
    }

    public void setListaEspera(List<Socio> listaEspera) {
        this.listaEspera = listaEspera;
    }

    public void addSocioListaEspera(Socio socio) {
        if (!listaEspera.contains(socio)) {
            listaEspera.add(socio);
        }
    }

    public void removeSocioListaEspera(Socio socio) {
        listaEspera.remove(socio);
    }

    public Sesion(LocalDate fechaImparticion, LocalTime horaInicio, LocalTime horaFinal, int plazasOcupadas) {
        this();
        this.fechaImparticion = fechaImparticion;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
        this.plazasOcupadas = plazasOcupadas;
    }

    public Sesion(Actividad actividad, Sala sala, LocalDate fechaImparticion, LocalTime horaInicio,
            LocalTime horaFinal) {
        this(fechaImparticion, horaInicio, horaFinal, 0);
        this.actividad = actividad;
        this.sala = sala;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getFechaImparticion() {
        return fechaImparticion;
    }

    public void setFechaImparticion(LocalDate fechaImparticion) {
        this.fechaImparticion = fechaImparticion;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    public int getPlazasOcupadas() {
        return plazasOcupadas;
    }

    public void setPlazasOcupadas(int plazasOcupadas) {
        this.plazasOcupadas = plazasOcupadas;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public void addReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public void removeReserva(Reserva reserva) {
        reservas.remove(reserva);
    }

    @Override
    public String toString() {
        return "Sesion [id=" + id + ", fechaImparticion=" + fechaImparticion + ", horaInicio=" + horaInicio +
                ", horaFinal=" + horaFinal + ", plazasOcupadas=" + plazasOcupadas + ", actividad="
                + (actividad != null ? actividad.getNombreActividad() : "null") + "]";
    }
}
