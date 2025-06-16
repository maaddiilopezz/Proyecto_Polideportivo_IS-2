package exceptions;
public class SocioAlreadyExistException extends Exception {
 private static final long serialVersionUID = 1L;
 

     public SocioAlreadyExistException() {
         super("El socio ya existe con ese nombre y contraseña.");
     }

     public SocioAlreadyExistException(String message) {
         super(message);
     }
 }