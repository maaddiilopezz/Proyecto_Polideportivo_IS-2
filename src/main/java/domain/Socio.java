package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
@Entity
public class Socio extends Usuario {
	@Id
	private String dni;
	private String nombre;
	private int maximoReservas;
	private int numReservas;
	private int numeroTarjeta;
	private String contraseña;
	private String mail;
	@OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Reserva> reservas;  // Lista de reservas asociadas al socio
	@OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Factura> facturas;

	public Socio() {
		this.reservas = new ArrayList<>();
		this.facturas = new ArrayList<>();
		this.maximoReservas = 5;
	}

	public Socio(String dni, String nombre, String mail, String contraseña, int numeroTarjeta) {
		super();
		this.dni = dni;
		this.nombre = nombre;
		this.mail = mail;
		this.contraseña = contraseña;
		this.numeroTarjeta = numeroTarjeta;
		this.reservas = new ArrayList<>();
        this.facturas = new ArrayList<>();
        this.maximoReservas = 5;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public int getMaximoReservas() {
		return maximoReservas;
	}
	public void setMaximoReservas(int maximoReservas) {
		this.maximoReservas = maximoReservas;
	}
	public int getNumReservas() {
		return numReservas;
	}
	public void setNumReservas(int numReservas) {
		this.numReservas = numReservas;
	}
	public int getNumeroTarjeta() {
		return numeroTarjeta;
	}
	public void setNumeroTarjeta(int numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}
	public String getContraseña() {
		return contraseña;
	}
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
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

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public void addFactura(Factura factura) {
        facturas.add(factura);
    }

    public void removeFactura(Factura factura) {
        facturas.remove(factura);
    }
	@Override
	public String toString() {
		return "Socio [nombre=" + nombre + ", dni=" + dni + ", maximoReservas=" + maximoReservas + ", numReservas="
				+ numReservas + ", numeroTarjeta=" + numeroTarjeta + ", contraseña=" + contraseña + ", mail=" + mail
				+ "]";
	}

}
