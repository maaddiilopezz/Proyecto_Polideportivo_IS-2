package domain;


import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlIDREF;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)

public class Factura implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double precio;
    private String codigoFactura;
    private Date fechaFactura;
    private ArrayList<Reserva> reservas; // Lista de reservas asociadas a la factura @ManyToOne
    @XmlIDREF
    private Socio socio;
    private boolean enviada = false; // Indica si la factura ha sido enviada al socio

    // Constructor vacío necesario para JPA
    public Factura() {
        this.reservas = new ArrayList<>();
        this.enviada = false;
    }

    public Factura(double precio, String codigoFactura, Date fechaFactura) {
        this();
        this.precio = precio;
        this.codigoFactura = codigoFactura;
        this.fechaFactura = fechaFactura;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "Factura [id=" + id + ", precio=" + precio + ", codigoFactura=" + codigoFactura + ", fechaFactura=" + fechaFactura
                + ", enviada=" + enviada + ", reservas=" + reservas + "]";
    }
}
