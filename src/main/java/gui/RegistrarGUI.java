package gui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.Socio;
import exceptions.SocioAlreadyExistException;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class RegistrarGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textDNI;
    private JTextField textNombre;
    private JTextField textNumeroTarjeta;
    private JPasswordField textContraseña;
    private JButton botonCrearCuenta;
    private JLabel mensajeError;
    private JLabel mensajeAprobado;
    BLFacade facade = InicioGUI.getBusinessLogic();
    private JLabel correoElectronico;
    private JTextField textCorreoElectronico;

    public RegistrarGUI() {
        setTitle(ResourceBundleUtil.getString("RegistrarGUI.Titulo"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 320);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel dni = new JLabel(ResourceBundleUtil.getString("RegistrarGUI.DNI"));
        dni.setBounds(51, 101, 61, 16);
        contentPane.add(dni);

        JLabel nombre = new JLabel(ResourceBundleUtil.getString("RegistrarGUI.Nombre"));
        nombre.setBounds(51, 28, 61, 16);
        contentPane.add(nombre);

        JLabel contrasena = new JLabel(ResourceBundleUtil.getString("RegistrarGUI.Contraseña"));
        contrasena.setBounds(51, 129, 82, 16);
        contentPane.add(contrasena);

        JLabel numeroTarjeta = new JLabel(ResourceBundleUtil.getString("RegistrarGUI.NumeroTarjeta"));
        numeroTarjeta.setBounds(51, 169, 120, 16);
        contentPane.add(numeroTarjeta);

        textDNI = new JTextField();
        textDNI.setBounds(243, 96, 130, 26);
        contentPane.add(textDNI);
        textDNI.setColumns(10);

        textNombre = new JTextField();
        textNombre.setBounds(243, 23, 130, 26);
        contentPane.add(textNombre);
        textNombre.setColumns(10);

        textContraseña = new JPasswordField();
        textContraseña.setBounds(243, 124, 130, 26);
        contentPane.add(textContraseña);

        textNumeroTarjeta = new JTextField();
        textNumeroTarjeta.setBounds(243, 164, 130, 26);
        contentPane.add(textNumeroTarjeta);
        textNumeroTarjeta.setColumns(10);

        // Añadir KeyListeners para controlar la habilitación del botón
        KeyAdapter keyListener = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String tarjeta = textNumeroTarjeta.getText();

                if (!tarjeta.matches("\\d*")) {
                    mensajeError.setText("La tarjeta solo puede contener números.");
                    mensajeError.setVisible(true);
                } else {
                    mensajeError.setVisible(false);
                }

                habilitarBotonCrearCuenta();
            }
        };
        textDNI.addKeyListener(keyListener);
        textNombre.addKeyListener(keyListener);
        textContraseña.addKeyListener(keyListener);
        textNumeroTarjeta.addKeyListener(keyListener);

        botonCrearCuenta = new JButton(ResourceBundleUtil.getString("RegistrarGUI.CrearCuenta"));
        botonCrearCuenta.setEnabled(false);
        botonCrearCuenta.setBounds(161, 239, 130, 29);
        contentPane.add(botonCrearCuenta);

        mensajeError = new JLabel();
        mensajeError.setVisible(false);
        mensajeError.setForeground(Color.RED);
        mensajeError.setBounds(51, 197, 393, 14);
        contentPane.add(mensajeError);

        mensajeAprobado = new JLabel();
        mensajeAprobado.setVisible(false);
        mensajeAprobado.setForeground(new Color(0, 128, 0));
        mensajeAprobado.setBounds(51, 223, 393, 14);
        contentPane.add(mensajeAprobado);
        
        correoElectronico = new JLabel(ResourceBundleUtil.getString("RegistrarGUI.CorreoElectronico"));
        correoElectronico.setBounds(51, 63, 120, 16);
        contentPane.add(correoElectronico);
        
        textCorreoElectronico = new JTextField();
        textCorreoElectronico.setBounds(243, 58, 130, 26);
        contentPane.add(textCorreoElectronico);
        textCorreoElectronico.setColumns(10);
        mensajeError.setVisible(true);

        botonCrearCuenta.addActionListener(e -> {
            mensajeAprobado.setVisible(false);
            mensajeError.setVisible(false);

            try {
                String email = textCorreoElectronico.getText();
                // Validación básica de email
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    mensajeError.setText("Correo electrónico inválido.");
                    mensajeError.setVisible(true);
                    return;
                }

                int numTarjeta = Integer.parseInt(textNumeroTarjeta.getText());
                // Validar que la hora de fin sea posterior a la de inicio en la planificación de sesiones
                // (esto se haría en la GUI de planificación, pero aquí te muestro cómo sería la validación)
                // Si tienes campos de hora de inicio y fin, deberías hacer algo como:
                // if (horaFin.isBefore(horaInicio) || horaFin.equals(horaInicio)) {
                //     mensajeError.setText("La hora de fin debe ser posterior a la de inicio.");
                //     mensajeError.setVisible(true);
                //     return;
                // }

                Socio nuevo = facade.registrarSocio(
                        textDNI.getText(),
                        textNombre.getText(),
                        email,
                        new String(textContraseña.getPassword()),
                        numTarjeta);

                if (nuevo == null) {
                    mensajeError.setText("Error al registrar el usuario. (Revisa la consola para más detalles)");
                    mensajeError.setVisible(true);
                    System.err.println("[ERROR][RegistrarGUI] El método registrarSocio devolvió null. Puede haber un error en la base de datos o en la lógica de registro.");
                } else {
                    mensajeAprobado.setText("¡Registro exitoso!");
                    mensajeAprobado.setVisible(true);
                    // Limpiar campos tras registro exitoso
                    textDNI.setText("");
                    textNombre.setText("");
                    textCorreoElectronico.setText("");
                    textContraseña.setText("");
                    textNumeroTarjeta.setText("");
                    botonCrearCuenta.setEnabled(false);
                    textDNI.requestFocusInWindow();
                }
            } catch (NumberFormatException ex) {
                mensajeError.setText("Número de tarjeta inválido.");
                mensajeError.setVisible(true);
                ex.printStackTrace();
            } catch (SocioAlreadyExistException ex) {
                mensajeError.setText(ex.getMessage());
                mensajeError.setVisible(true);
                ex.printStackTrace();
            } catch (Exception ex) {
                mensajeError.setText("Ocurrió un error inesperado: " + ex.getMessage());
                mensajeError.setVisible(true);
                ex.printStackTrace();
            }
        });
        
        JButton botonAtras = new JButton("\u2190"); // ←
        botonAtras.setBounds(10, 10, 50, 25);
        contentPane.add(botonAtras);
        botonAtras.addActionListener(e -> {
            new IniciarSesionGUI().setVisible(true);
            dispose();
        });
    }

    private void habilitarBotonCrearCuenta() {
        String dni = textDNI.getText();
        String nombre = textNombre.getText();
        String mail = textCorreoElectronico.getText();
        String contraseña = new String(textContraseña.getPassword());
        String tarjeta = textNumeroTarjeta.getText();

        boolean todosLlenos = !dni.isEmpty() && !nombre.isEmpty() && !mail.isEmpty()&&!contraseña.isEmpty() && !tarjeta.isEmpty();
        boolean tarjetaValida = true;

        try {
            Integer.parseInt(tarjeta);
        } catch (NumberFormatException e) {
            tarjetaValida = false;
        }

        botonCrearCuenta.setEnabled(todosLlenos && tarjetaValida);
    }
}
