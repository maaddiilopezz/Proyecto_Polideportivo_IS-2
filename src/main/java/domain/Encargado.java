package domain;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Entity
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Encargado extends Usuario {

    public int nEmpleado = 0;

    public Encargado() {
        super();
    }

    public Encargado(String dni, String nombre, String mail, String contraseña) {
        super(dni, nombre, mail, contraseña);
    }

    @Override
    public String toString() {
        return "Encargado [nombre=" + getNombre() + ", dni=" + getDni() + ", contraseña=" + getContraseña() + ", mail="
                + getMail() + ", nEmpleado=" + nEmpleado + "]";
    }

}