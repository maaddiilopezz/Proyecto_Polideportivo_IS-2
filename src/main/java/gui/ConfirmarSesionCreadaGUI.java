package gui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import domain.Actividad;
import domain.Sala;

public class ConfirmarSesionCreadaGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private Actividad actividad;
    private Sala sala;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    private JLabel labelActividad;
    private JLabel labelSala;
    private JLabel labelFecha;
    private JLabel labelHoraInicio;
    private JLabel labelHoraFin;

    private JButton botonConfirmar;
    private JLabel mostrarActividad;
    private JLabel mostrarSala;
    private JLabel mostrarFecha;
    private JLabel mostrarHoraInicio;
    private JLabel mostrarHoraFIn;

    public ConfirmarSesionCreadaGUI(Actividad actividad, Sala sala, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.actividad = actividad;
        this.sala = sala;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;

        initialize();
    }

    private void initialize() {
        setTitle(ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.Titulo"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(10, 10));

        // Panel para mostrar datos
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(null);

        labelActividad = new JLabel(ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.Actividad"));
        labelActividad.setBounds(79, 20, 66, 21);
        labelSala = new JLabel(ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.Sala"));
        labelSala.setBounds(79, 53, 66, 21);
        labelFecha = new JLabel(ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.Fecha"));
        labelFecha.setBounds(79, 86, 75, 21);
        labelHoraInicio = new JLabel(ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.HoraInicio"));
        labelHoraInicio.setBounds(79, 108, 93, 29);
        labelHoraFin = new JLabel(ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.HoraFin"));
        labelHoraFin.setBounds(79, 139, 66, 29);

        panelDatos.add(labelActividad);
        panelDatos.add(labelSala);
        panelDatos.add(labelFecha);
        panelDatos.add(labelHoraInicio);
        panelDatos.add(labelHoraFin);

        // Labels para mostrar valores elegidos
        mostrarActividad = new JLabel(actividad.getNombreActividad());
        mostrarActividad.setBounds(221, 22, 150, 16);
        panelDatos.add(mostrarActividad);

        mostrarSala = new JLabel(sala.getNombreSala());
        mostrarSala.setBounds(221, 55, 150, 16);
        panelDatos.add(mostrarSala);

        mostrarFecha = new JLabel(fecha.toString());
        mostrarFecha.setBounds(221, 88, 150, 16);
        panelDatos.add(mostrarFecha);

        mostrarHoraInicio = new JLabel(horaInicio.toString());
        mostrarHoraInicio.setBounds(221, 114, 150, 16);
        panelDatos.add(mostrarHoraInicio);

        mostrarHoraFIn = new JLabel(horaFin.toString());
        mostrarHoraFIn.setBounds(221, 145, 150, 16);
        panelDatos.add(mostrarHoraFIn);

        botonConfirmar = new JButton(ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.Confirmar"));
        botonConfirmar.setBounds(136, 184, 107, 29);
        panelDatos.add(botonConfirmar);

        // Acción botón confirmar
        botonConfirmar.addActionListener(e -> {
            try {
                InicioGUI.getBusinessLogic().crearSesion(actividad, sala, fecha, horaInicio, horaFin);
                JOptionPane.showMessageDialog(this, "Sesión confirmada y creada en la base de datos.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear la sesión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton botonAtras = new JButton("\u2190"); // ←
        botonAtras.setBounds(10, 10, 50, 25);
        panelDatos.add(botonAtras);
        botonAtras.addActionListener(e -> {
            new InicioEncargadoGUI().setVisible(true);
            dispose();
        });

        getContentPane().add(panelDatos, BorderLayout.CENTER);

        // Panel para botones adicional si quieres
        JPanel panelBotones = new JPanel();
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }
}
