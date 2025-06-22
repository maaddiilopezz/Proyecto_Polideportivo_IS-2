package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Sesion implements Serializable {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    @Id
    @XmlID
    private String id;
    private String fechaImparticion;
    private String horaInicio;
    private String horaFinal;
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
    }    public Sesion(LocalDate fechaImparticion, LocalTime horaInicio, LocalTime horaFinal, int plazasOcupadas) {
        this();
        this.fechaImparticion = fechaImparticion != null ? fechaImparticion.format(DATE_FORMATTER) : null;
        // Forzar formato compatible con ObjectDB como en las sesiones de inicialización
        this.horaInicio = horaInicio != null ? convertirHoraAFormatoObjectDB(horaInicio) : null;
        this.horaFinal = horaFinal != null ? convertirHoraAFormatoObjectDB(horaFinal) : null;
        this.plazasOcupadas = plazasOcupadas;
    }
    
    /**
     * Convierte LocalTime al formato que usa ObjectDB internamente
     * Simula el formato "Thu Jan 01 HH:mm:ss CET 1970"
     */
    private String convertirHoraAFormatoObjectDB(LocalTime hora) {
        if (hora == null) return null;
        // Crear el formato compatible con las sesiones de inicialización
        return String.format("Thu Jan 01 %02d:%02d:%02d CET 1970", 
                           hora.getHour(), hora.getMinute(), hora.getSecond());
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
        if (fechaImparticion == null) {
            return null;
        }

        try {
            // Intentar parsear con formato ISO primero
            return LocalDate.parse(fechaImparticion, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            // Si falla, intentar parsear desde el formato original
            LocalDate fecha = parseFechaFromString(fechaImparticion);
            if (fecha != null) {
                // Migrar la fecha al formato correcto
                this.fechaImparticion = fecha.format(DATE_FORMATTER);
                return fecha;
            }

            System.err.println("Error parseando fecha: " + fechaImparticion);
            return null;
        }
    }

    private String convertMonthToNumber(String monthName) {
        switch (monthName.toLowerCase()) {
            case "jan":
                return "01";
            case "feb":
                return "02";
            case "mar":
                return "03";
            case "apr":
                return "04";
            case "may":
                return "05";
            case "jun":
                return "06";
            case "jul":
                return "07";
            case "aug":
                return "08";
            case "sep":
                return "09";
            case "oct":
                return "10";
            case "nov":
                return "11";
            case "dec":
                return "12";
            default:
                return "01";
        }
    }

    private LocalTime parseTimeFromString(String timeStr) {
        if (timeStr == null)
            return null;

        try {
            // Si contiene información de fecha completa, extraer solo la hora
            if (timeStr.contains(" ")) {
                // Formato: "Thu Jan 01 11:00:00 CET 1970" -> extraer "11:00:00"
                String[] parts = timeStr.split(" ");
                if (parts.length >= 4) {
                    String timePart = parts[3]; // "11:00:00"
                    // Si tiene formato HH:mm:ss, tomar solo HH:mm
                    if (timePart.contains(":")) {
                        String[] timeParts = timePart.split(":");
                        if (timeParts.length >= 2) {
                            return LocalTime.parse(String.format("%s:%s", timeParts[0], timeParts[1]));
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public void setFechaImparticion(LocalDate fechaImparticion) {
        this.fechaImparticion = fechaImparticion != null ? fechaImparticion.format(DATE_FORMATTER) : null;
    }

    // Métodos auxiliares para trabajar directamente con String
    public String getFechaImparticionString() {
        return fechaImparticion;
    }

    public void setFechaImparticionString(String fechaImparticion) {
        // Si la fecha no está en formato ISO, intentar convertirla
        if (fechaImparticion != null && !fechaImparticion.matches("\\d{4}-\\d{2}-\\d{2}")) {
            try {
                LocalDate fecha = parseFechaFromString(fechaImparticion);
                this.fechaImparticion = fecha != null ? fecha.format(DATE_FORMATTER) : fechaImparticion;
            } catch (Exception e) {
                this.fechaImparticion = fechaImparticion;
            }
        } else {
            this.fechaImparticion = fechaImparticion;
        }
    }

    private LocalDate parseFechaFromString(String fechaStr) {
        if (fechaStr == null)
            return null;

        try {
            // Si contiene información de hora, extraer solo la fecha
            if (fechaStr.contains(" ")) {
                String[] parts = fechaStr.split(" ");
                if (parts.length >= 6) {
                    String year = parts[5];
                    String month = convertMonthToNumber(parts[1]);
                    String day = String.format("%02d", Integer.parseInt(parts[2]));
                    return LocalDate.parse(year + "-" + month + "-" + day);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public LocalTime getHoraInicio() {
        if (horaInicio == null) {
            return null;
        }

        try {
            // Intentar parsear con formato ISO primero
            return LocalTime.parse(horaInicio, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            // Si falla, intentar parsear desde el formato original
            LocalTime tiempo = parseTimeFromString(horaInicio);
            if (tiempo != null) {
                // Migrar la hora al formato correcto
                this.horaInicio = tiempo.format(TIME_FORMATTER);
                return tiempo;
            }

            System.err.println("Error parseando hora inicio: " + horaInicio);
            return null;
        }
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio != null ? horaInicio.format(TIME_FORMATTER) : null;
    }

    public LocalTime getHoraFinal() {
        if (horaFinal == null) {
            return null;
        }

        try {
            // Intentar parsear con formato ISO primero
            return LocalTime.parse(horaFinal, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            // Si falla, intentar parsear desde el formato original
            LocalTime tiempo = parseTimeFromString(horaFinal);
            if (tiempo != null) {
                // Migrar la hora al formato correcto
                this.horaFinal = tiempo.format(TIME_FORMATTER);
                return tiempo;
            }

            System.err.println("Error parseando hora final: " + horaFinal);
            return null;
        }
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal != null ? horaFinal.format(TIME_FORMATTER) : null;
    }

    // Métodos auxiliares para trabajar directamente con String
    public String getHoraInicioString() {
        return horaInicio;
    }

    public void setHoraInicioString(String horaInicio) {
        // Si la hora no está en formato ISO, intentar convertirla
        if (horaInicio != null && !horaInicio.matches("\\d{2}:\\d{2}")) {
            try {
                LocalTime tiempo = parseTimeFromString(horaInicio);
                this.horaInicio = tiempo != null ? tiempo.format(TIME_FORMATTER) : horaInicio;
            } catch (Exception e) {
                this.horaInicio = horaInicio;
            }
        } else {
            this.horaInicio = horaInicio;
        }
    }

    public String getHoraFinalString() {
        return horaFinal;
    }

    public void setHoraFinalString(String horaFinal) {
        // Si la hora no está en formato ISO, intentar convertirla
        if (horaFinal != null && !horaFinal.matches("\\d{2}:\\d{2}")) {
            try {
                LocalTime tiempo = parseTimeFromString(horaFinal);
                this.horaFinal = tiempo != null ? tiempo.format(TIME_FORMATTER) : horaFinal;
            } catch (Exception e) {
                this.horaFinal = horaFinal;
            }
        } else {
            this.horaFinal = horaFinal;
        }
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
