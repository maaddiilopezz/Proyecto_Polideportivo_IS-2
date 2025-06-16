package domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sesion {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate fechaImparticion;
    private LocalTime horaInicio;
    private LocalTime horaFinal;
    private int plazasOcupadas;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "actividad_nombre", referencedColumnName = "id.nombreActividad"),
        @JoinColumn(name = "actividad_nivel", referencedColumnName = "id.gradoExigencia")
    })
    private Actividad actividad;

    @ManyToOne
    private Sala sala;

    // Si necesitas persistir reservas, esta línea requiere definir la relación en Reserva
    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

    public Sesion() {
    	reservas = new ArrayList<Reserva> ();
    }

    public Sesion(LocalDate fechaImparticion, LocalTime horaInicio, LocalTime horaFinal, int plazasOcupadas) {
    	this();
    	this.fechaImparticion = fechaImparticion;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
        this.plazasOcupadas = plazasOcupadas;
    }

    public Sesion(Actividad actividad, Sala sala, LocalDate fechaImparticion, LocalTime horaInicio, LocalTime horaFinal) {
        this(fechaImparticion, horaInicio, horaFinal, 0);
        this.actividad = actividad;
        this.sala = sala;
    }

    public Long getId() {
        return id;
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
                ", horaFinal=" + horaFinal + ", plazasOcupadas=" + plazasOcupadas + ", actividad=" + (actividad != null ? actividad.getNombreActividad() : "null") + "]";
    }
}
