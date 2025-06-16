package domain;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class ActividadId implements Serializable {
    private String nombreActividad;
    private int gradoExigencia;

    public ActividadId() {}
    public ActividadId(String nombreActividad, int gradoExigencia) {
        this.nombreActividad = nombreActividad;
        this.gradoExigencia = gradoExigencia;
    }
    public String getNombreActividad() { return nombreActividad; }
    public void setNombreActividad(String nombreActividad) { this.nombreActividad = nombreActividad; }
    public int getGradoExigencia() { return gradoExigencia; }
    public void setGradoExigencia(int gradoExigencia) { this.gradoExigencia = gradoExigencia; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActividadId that = (ActividadId) o;
        return gradoExigencia == that.gradoExigencia &&
                java.util.Objects.equals(nombreActividad, that.nombreActividad);
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(nombreActividad, gradoExigencia);
    }
}
