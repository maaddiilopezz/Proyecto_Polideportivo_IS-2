package domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.ArrayList;
@Entity
public class Factura {
	@Id
    private double precio;
    private String codigoFactura;
    private Date fechaFactura;
    private ArrayList<Reserva> reservas; // Lista de reservas asociadas a la factura

    @ManyToOne
    private Socio socio;
    
    private boolean enviada = false; // Indica si la factura ha sido enviada al socio

    public Factura(double precio, String codigoFactura, Date fechaFactura) {
        this.precio = precio;
        this.codigoFactura = codigoFactura;
        this.fechaFactura = fechaFactura;
        this.reservas = new ArrayList<>();
        this.enviada = false;

    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCodigoFactura() {
        return codigoFactura;
    }

    public void setCodigoFactura(String codigoFactura) {
        this.codigoFactura = codigoFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }
    
    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }

    public void addReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public void removeReserva(Reserva reserva) {
        reservas.remove(reserva);
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }
    
    public boolean isEnviada() {
        return enviada;
    }

    public void setEnviada(boolean enviada) {
        this.enviada = enviada;
    }

    @Override
    public String toString() {
        return "Factura [precio=" + precio + ", codigoFactura=" + codigoFactura + ", fechaFactura=" + fechaFactura
                + ", enviada=" + enviada + ", reservas=" + reservas + "]";
    }
}

