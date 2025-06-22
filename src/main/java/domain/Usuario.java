package domain;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;

@MappedSuperclass
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({ Socio.class, Encargado.class })
public abstract class Usuario implements Serializable {
    @Id
    protected String dni;

    protected String nombre;
    protected String mail;
    protected String contraseña;

    public Usuario() {
    }

    public Usuario(String dni, String nombre, String mail, String contraseña) {
        this.dni = dni;
        this.nombre = nombre;
        this.mail = mail;
        this.contraseña = contraseña;
    }

    @XmlID
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public String toString() {
        return "Usuario [dni=" + dni + ", nombre=" + nombre + ", mail=" + mail + "]";
    }
}