package exceptions;

public class ActividadAlreadyExistException extends Exception {
    public ActividadAlreadyExistException(String message) {
        super(message);
    }

    public ActividadAlreadyExistException() {
        super();
    }
}
