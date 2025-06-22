package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Reserva implements Serializable {
	@Id
	@XmlID
	private String idReserva;
	private Date fechaReserva;
	private boolean admitido;
	@ManyToOne
	private Sesion sesion;
	@ManyToOne
	@XmlIDREF
	private Socio socio;

	public Reserva() {
	}

	public Reserva(String idReserva, Date fechaReserva, boolean admitido) {
		this.idReserva = idReserva;
		this.fechaReserva = fechaReserva;
		this.admitido = admitido;
	}

	public Reserva(Socio socio, Sesion sesion) {
		this.socio = socio;
		this.sesion = sesion;
		this.idReserva = UUID.randomUUID().toString();
		this.fechaReserva = new Date();
		this.admitido = true;
	}

	public String getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(String idReserva) {
		this.idReserva = idReserva;
	}

	public Date getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	public boolean isAdmitido() {
		return admitido;
	}

	public void setAdmitido(boolean admitido) {
		this.admitido = admitido;
	}

	public Sesion getSesion() {
		return sesion;
	}

	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}

	public Socio getSocio() {
		return socio;
	}

	public void setSocio(Socio socio) {
		this.socio = socio;
	}

	@Override
	public String toString() {
		if (sesion == null)
			return "Reserva sin sesión";
		String actividad = sesion.getActividad() != null ? sesion.getActividad().getNombreActividad() : "";
		String sala = sesion.getSala() != null ? sesion.getSala().getNombreSala() : "";
		String fecha = sesion.getFechaImparticion() != null ? sesion.getFechaImparticion().toString() : "";
		String horaIni = sesion.getHoraInicio() != null ? sesion.getHoraInicio().toString() : "";
		String horaFin = sesion.getHoraFinal() != null ? sesion.getHoraFinal().toString() : "";
		return String.format("%s | %s | %s | %s-%s | ReservaID: %s", actividad, sala, fecha, horaIni, horaFin,
				idReserva);
	}

}
