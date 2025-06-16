package gui;

import businessLogic.BLFacade;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.toedter.calendar.JCalendar;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import domain.Actividad;
import domain.Sala;

public class CrearSesionDatosGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    BLFacade facade = InicioGUI.getBusinessLogic(); 
    private JSpinner spinnerHoraInicio;
    private JSpinner spinnerHoraFin;
   
    private JCalendar jCalendar1 = new JCalendar();
    private JScrollPane scrollPaneEvents = new JScrollPane();

    private Actividad actividad;
    private Sala sala;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    private void initGUI() {
        this.getContentPane().setLayout(null);
        this.setSize(new Dimension(700, 500));
        this.setTitle(ResourceBundleUtil.getString("PlanificarSesionesGUI.Titulo"));

        // Label hora inicio
        JLabel jLabelHoraInicio = new JLabel(ResourceBundleUtil.getString("PlanificarSesionesGUI.HoraInicio"));
        jLabelHoraInicio.setBounds(50, 260, 120, 25); // Más abajo aún
        this.getContentPane().add(jLabelHoraInicio);

        // Spinner hora inicio con SpinnerListModel
        String[] horasInicio = new String[14]; // 8:00 a 21:00
        for (int i = 0; i < 14; i++) {
            int hora = 8 + i;
            horasInicio[i] = String.format("%02d:00", hora);
        }
        spinnerHoraInicio = new JSpinner(new javax.swing.SpinnerListModel(horasInicio));
        spinnerHoraInicio.setBounds(180, 260, 100, 25); // Más abajo aún
        this.getContentPane().add(spinnerHoraInicio);

        // Label hora fin
        JLabel jLabelHoraFin = new JLabel(ResourceBundleUtil.getString("PlanificarSesionesGUI.HoraFin"));
        jLabelHoraFin.setBounds(50, 300, 120, 25); // Más abajo aún
        this.getContentPane().add(jLabelHoraFin);

        // Spinner hora fin con SpinnerListModel
        String[] horasFin = new String[14]; // 9:00 a 22:00
        for (int i = 0; i < 14; i++) {
            int hora = 9 + i;
            horasFin[i] = String.format("%02d:00", hora);
        }
        spinnerHoraFin = new JSpinner(new javax.swing.SpinnerListModel(horasFin));
        spinnerHoraFin.setBounds(180, 300, 100, 25); // Más abajo aún
        this.getContentPane().add(spinnerHoraFin);

        // Calendario
        jCalendar1.setBounds(new Rectangle(300, 50, 225, 150));
        this.getContentPane().add(jCalendar1);

        // Botón Aceptar
        JButton botonAceptar = new JButton(ResourceBundleUtil.getString("PlanificarSesionesGUI.Aceptar"));
        botonAceptar.setBounds(249, 354, 117, 29);
        getContentPane().add(botonAceptar);

        // Botón Atrás
        JButton botonAtras = new JButton("\u2190"); // ←
        botonAtras.setBounds(10, 10, 50, 25);
        this.getContentPane().add(botonAtras);
        botonAtras.addActionListener(e -> {
            new InicioEncargadoGUI().setVisible(true);
            dispose();
        });

        // Acción al hacer clic en el botón "Aceptar"
        botonAceptar.addActionListener(e -> {
            try {
                // Solo recoge fecha y horas
                if (spinnerHoraInicio.getValue() == null || spinnerHoraFin.getValue() == null || jCalendar1.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Por favor, selecciona fecha y horas.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String horaInicioStr = (String) spinnerHoraInicio.getValue();
                String horaFinStr = (String) spinnerHoraFin.getValue();
                java.time.LocalTime horaInicio = java.time.LocalTime.parse(horaInicioStr);
                java.time.LocalTime horaFin = java.time.LocalTime.parse(horaFinStr);
                if (!horaFin.isAfter(horaInicio)) {
                    JOptionPane.showMessageDialog(this, "La hora de fin debe ser posterior a la de inicio.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                java.util.Date fechaSeleccionada = jCalendar1.getDate();
                java.sql.Date fechaSql = new java.sql.Date(fechaSeleccionada.getTime());
                // Abre PlanificarSesionesGUI pasando fecha y horas
                PlanificarSesionesGUI planificarSesionesGUI = new PlanificarSesionesGUI(fechaSql, java.sql.Time.valueOf(horaInicio), java.sql.Time.valueOf(horaFin));
                planificarSesionesGUI.setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }

    // Método para convertir java.sql.Date a java.time.LocalDate
    public LocalDate convertirAFechaLocalDate(Date date) {
        return date.toLocalDate();
    }
   

    // Método para convertir java.sql.Date a java.time.LocalTime
    public LocalTime convertirAHoraLocalTime(Time horaSqlInicio) {
        return horaSqlInicio.toLocalTime();
    }

    // Cambia el constructor para recibir solo fecha y horas
    public CrearSesionDatosGUI(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        initGUI();
        // Rellenar los campos de fecha y hora
        if (jCalendar1 != null && fecha != null) {
            java.util.Date utilDate = java.sql.Date.valueOf(fecha);
            jCalendar1.setDate(utilDate);
        }
        if (spinnerHoraInicio != null && horaInicio != null) {
            spinnerHoraInicio.setValue(String.format("%02d:00", horaInicio.getHour()));
        }
        if (spinnerHoraFin != null && horaFin != null) {
            spinnerHoraFin.setValue(String.format("%02d:00", horaFin.getHour()));
        }
        this.setLocationRelativeTo(null);
        this.repaint();
    }

    // Constructor por defecto para permitir abrir la ventana sin argumentos
    public CrearSesionDatosGUI() {
        this(null, null, null);
    }
}
