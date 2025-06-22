package domain;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> implements Serializable {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

    // Constructor vacío necesario para JAXB
    public LocalTimeAdapter() {
        // Constructor vacío necesario para la serialización
    }

    @Override
    public LocalTime unmarshal(String timeString) throws Exception {
        if (timeString == null || timeString.trim().isEmpty()) {
            return null;
        }
        return LocalTime.parse(timeString, formatter);
    }

    @Override
    public String marshal(LocalTime localTime) throws Exception {
        if (localTime == null) {
            return null;
        }
        return localTime.format(formatter);
    }
}
