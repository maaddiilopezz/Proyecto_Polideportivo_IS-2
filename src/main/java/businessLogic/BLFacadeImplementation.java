package businessLogic;
import java.time.LocalDate;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import domain.Usuario;
import domain.Socio;
import domain.Sala;
import domain.Actividad;
import domain.Sesion;
import domain.Reserva;
import domain.Factura;
import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebMethod;
import javax.jws.WebService;
import dataAccess.DataAccess;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import exceptions.SocioAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {
	DataAccess dbManager;
	private dataAccess.DataAccess dataAccess;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager=new DataAccess();
		    this.dataAccess = dbManager;
		//dbManager.close();

		
	}
	
    public BLFacadeImplementation(DataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		ConfigXML c=ConfigXML.getInstance();
		
		dbManager=da;		
	}

    @Override
    public Socio registrarSocio(String dni, String nombre, String mail, String contraseña, int numeroTarjeta) throws SocioAlreadyExistException {
        return dbManager.registrarSocio(dni, nombre, mail, contraseña, numeroTarjeta);
    }
    
    @Override
    public Usuario iniciarSesion(String correo, String contraseña) {
        return dbManager.iniciarSesion(correo, contraseña);
    }
    
    // Método para obtener las actividades
    @Override
    public List<Actividad> getActividades() {
        return dbManager.getActividades();
    }
    private boolean mismaFecha(LocalDate fecha1, LocalDate fecha2) {
        // Comparar si las fechas son iguales
        return fecha1.isEqual(fecha2);
    }

    private boolean haySolape(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        // Verificar si hay solapamiento entre dos intervalos de tiempo
        return !inicio1.isAfter(fin2) && !inicio2.isAfter(fin1);
    }
    // Método para obtener las salas libres
  
	public List<Sala> getTodasLasSalas(){
		return dbManager.getTodasLasSalas();
	}
	
    @Override
    public List<Sesion> getSesionesPorFiltros(String actividad, Integer gradoExigencia) {
        List<Sesion> sesionesFiltradas = new ArrayList<>();
        List<Actividad> actividades = dbManager.getActividades();
        LocalDate hoy = LocalDate.now();
        LocalDate lunes = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);
        LocalDate domingo = lunes.plusDays(6);
        for (Actividad a : actividades) {
            boolean cumpleActividad = (actividad == null || actividad.isEmpty()) || a.getNombreActividad().equalsIgnoreCase(actividad);
            boolean cumpleExigencia = (gradoExigencia == null) || a.getGradoExigencia() == gradoExigencia;
            if (cumpleActividad && cumpleExigencia) {
                for (Sesion sesion : a.getSesiones()) {
                    LocalDate fecha = sesion.getFechaImparticion();
                    if (fecha != null && !fecha.isBefore(lunes) && !fecha.isAfter(domingo)) {
                        sesionesFiltradas.add(sesion);
                    }
                }
            }
        }
        return sesionesFiltradas;
    }

    @Override
    public String getTipoUsuario(String correo) {
        return dbManager.getTipoUsuario(correo);
    }
    @Override
    public void crearSesion(Actividad actividad, Sala sala, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        dbManager.crearSesion(actividad, sala, fecha, horaInicio, horaFin);
    }
    
    @Override
    public List<Sesion> getSesionesDeActividad(Actividad actividad) {
        // Delegamos la llamada a DataAccess
        return dbManager.getSesionesDeActividad(actividad);
    }

    @Override
    public List<Reserva> getReservasDeSocio(Socio socio) {
        return dbManager.getReservasDeSocio(socio);
    }
    
    @Override
    public void reservarSesion(Socio socio, Sesion sesion) throws Exception {
        System.out.println("[DEBUG] BLFacadeImplementation.reservarSesion called");
        // Usar SIEMPRE la misma instancia para todas las operaciones
        dbManager.reservarSesion(socio, sesion);
    }
	
	@Override
	public Sesion getSesion(Reserva reserva) {
		return reserva.getSesion();
	}
	@Override
	public int getPlazasMaximas(Actividad actividad) {
		return actividad.getPlazasMaximas();
	}
	
	public void close() {
		DataAccess dB4oManager=new DataAccess();

		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
    @WebMethod	
	 public void initializeBD(){
    	dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}

	// Permite pagar una factura desde la capa de negocio
    @Override
    public void pagarFactura(domain.Factura factura) throws Exception {
        dbManager.pagarFactura(factura);
    }

	@Override
	public List<Factura> getFacturasDeSocio(Socio socio) {
		return dbManager.getFacturasDeSocio(socio);
	}
	
	public Actividad crearActividad(String nombre, int nivelExigencia) throws exceptions.ActividadAlreadyExistException {
        return dataAccess.crearActividad(nombre, nivelExigencia);
    }
	public Actividad crearActividad(String nombre, int nivelExigencia, double precio) throws exceptions.ActividadAlreadyExistException {
        return dbManager.crearActividad(nombre, nivelExigencia, precio);
    }

    @Override
    public List<Socio> getSocios() {
        return dbManager.getSocios();
    }

    @Override
    public List<Factura> mandarFacturas(Socio socio, java.time.LocalDate fechaIgnorada) {
        // El parámetro fecha ya no se usa, solo se envían todas las facturas pendientes
        return dbManager.mandarFacturas(socio);
    }

	@Override
	public List<Sesion> getSesionesDeSala(Sala sala, LocalDate fecha) {
		return dbManager.getSesionesDeSala(sala, fecha);
	}
	@Override
	public List<Factura> getFacturasPendientesDeSocio(Socio socio) {
        return dbManager.getFacturasPendientesDeSocio(socio);
    }
	@Override
    public void cancelarReserva(Reserva reserva) throws Exception {
        dbManager.cancelarReserva(reserva);
    }
}

