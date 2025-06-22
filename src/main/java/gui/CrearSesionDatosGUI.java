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

        JLabel jLabelHoraInicio = new JLabel(ResourceBundleUtil.getString("PlanificarSesionesGUI.HoraInicio"));
        jLabelHoraInicio.setBounds(50, 260, 120, 25); 
        this.getContentPane().add(jLabelHoraInicio);

        String[] horasInicio = new String[14]; // 8:00 a 21:00
        for (int i = 0; i < 14; i++) {
            int hora = 8 + i;
            horasInicio[i] = String.format("%02d:00", hora);
        }
        spinnerHoraInicio = new JSpinner(new javax.swing.SpinnerListModel(horasInicio));
        spinnerHoraInicio.setBounds(180, 260, 100, 25);
        this.getContentPane().add(spinnerHoraInicio);

        JLabel jLabelHoraFin = new JLabel(ResourceBundleUtil.getString("PlanificarSesionesGUI.HoraFin"));
        jLabelHoraFin.setBounds(50, 300, 120, 25); 
        this.getContentPane().add(jLabelHoraFin);

        String[] horasFin = new String[14]; 
        for (int i = 0; i < 14; i++) {
            int hora = 9 + i;
            horasFin[i] = String.format("%02d:00", hora);
        }
        spinnerHoraFin = new JSpinner(new javax.swing.SpinnerListModel(horasFin));
        spinnerHoraFin.setBounds(180, 300, 100, 25); 
        this.getContentPane().add(spinnerHoraFin);

        jCalendar1.setBounds(new Rectangle(300, 50, 225, 150));
        this.getContentPane().add(jCalendar1);

        JButton botonAceptar = new JButton(ResourceBundleUtil.getString("PlanificarSesionesGUI.Aceptar"));
        botonAceptar.setBounds(249, 354, 117, 29);
        getContentPane().add(botonAceptar);

        JButton botonAtras = new JButton("\u2190");
        botonAtras.setBounds(10, 10, 50, 25);
        this.getContentPane().add(botonAtras);
        botonAtras.addActionListener(e -> {
            new InicioEncargadoGUI().setVisible(true);
            dispose();
        });

        botonAceptar.addActionListener(e -> {
            try {
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
                java.time.LocalDate fechaLocal = fechaSeleccionada.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                // Abrir ConfirmarSesionCreadaGUI pasando fecha, horaInicio y horaFin
                ConfirmarSesionCreadaGUI confirmarSesionCreadaGUI = new ConfirmarSesionCreadaGUI(fechaLocal, horaInicio, horaFin);
                confirmarSesionCreadaGUI.setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }

    public LocalDate convertirAFechaLocalDate(Date date) {
        return date.toLocalDate();
    }
   

    public LocalTime convertirAHoraLocalTime(Time horaSqlInicio) {
        return horaSqlInicio.toLocalTime();
    }

    public CrearSesionDatosGUI(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        initGUI();
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

    public CrearSesionDatosGUI() {
        this(null, null, null);
    }
}
