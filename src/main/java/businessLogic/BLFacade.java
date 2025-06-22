package businessLogic;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.Date;

import domain.Usuario;
import domain.Socio;
import domain.Actividad;
import domain.Sala;
import domain.Sesion;
import domain.Reserva;
import domain.Factura;
import java.util.List;

import exceptions.SocioAlreadyExistException;
import exceptions.ActividadAlreadyExistException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.swing.JLabel;

/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade {

	@WebMethod
	public Socio registrarSocio(String dni, String nombre, String mail, String contraseña, int numeroTarjeta)
			throws SocioAlreadyExistException;

	@WebMethod
	public Usuario iniciarSesion(String correo, String contraseña);

	@WebMethod
	public List<Sala> getTodasLasSalas();

	@WebMethod
	public List<Actividad> getActividades(); // si no hay restricción, devuelves todas

	@WebMethod
	public String getTipoUsuario(String correo);

	@WebMethod
	public void crearSesion(Actividad actividad, Sala sala, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin);

	@WebMethod
	public List<Sesion> getSesionesDeActividad(Actividad actividad);

	@WebMethod
	public List<Sesion> getSesionesPorFiltros(String actividad, Integer gradoExigencia);

	@WebMethod
	public List<Reserva> getReservasDeSocio(Socio socio);

	@WebMethod
	public void reservarSesion(Socio socio, Sesion sesion) throws Exception;

	@WebMethod
	public Sesion getSesion(Reserva reserva);

	@WebMethod
	public int getPlazasMaximas(Actividad actividad);

	@WebMethod
	public void initializeBD();

	@WebMethod
	public void pagarFactura(domain.Factura factura) throws Exception;

	@WebMethod
	public List<Factura> getFacturasDeSocio(Socio socio);

	// @WebMethod
	// public Actividad crearActividad(String nombre, int nivelExigencia) throws
	// ActividadAlreadyExistException;

	@WebMethod
	public Actividad crearActividad(String nombre, int nivelExigencia, double precio)
			throws exceptions.ActividadAlreadyExistException;

	@WebMethod
	public List<Socio> getSocios();

	@WebMethod
	public List<Factura> mandarFacturas(Socio socio, java.time.LocalDate fechaSemanal);

	@WebMethod
	public List<Sesion> getSesionesDeSala(Sala sala, LocalDate fecha);

	@WebMethod
	public List<Factura> getFacturasPendientesDeSocio(Socio socio);

	@WebMethod
	public void close();

	@WebMethod
	public void cancelarReserva(Reserva reserva) throws Exception;
}
