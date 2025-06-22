package gui;

import java.awt.EventQueue;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import businessLogic.BLFacade;
import domain.Actividad;
import domain.Sala;

import javax.swing.JOptionPane;

public class PlanificarSesionesGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> comboBoxActividades;
    private JComboBox<String> comboBoxSalas;
    private JButton botonAceptar;
    private Date fechaSeleccionada;
    private Time horaInicioSeleccionada;
    private Time horaFinSeleccionada; 
    BLFacade facade = InicioGUI.getBusinessLogic(); 

    public PlanificarSesionesGUI(Date fecha, Time horaInicio, Time horaFin) {
        this.fechaSeleccionada = fecha;
        this.horaInicioSeleccionada = horaInicio;
        this.horaFinSeleccionada = horaFin;
        initialize();
    }

    public PlanificarSesionesGUI() {
        this(new java.sql.Date(System.currentTimeMillis()), java.sql.Time.valueOf("09:00:00"), java.sql.Time.valueOf("10:00:00"));
    }

    public void initialize() {
        setTitle(ResourceBundleUtil.getString("PlanificarSesionesGUI.Titulo"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblResumen = new JLabel("Fecha seleccionada: " + (fechaSeleccionada != null ? fechaSeleccionada.toString() : "-") +
            " | Hora inicio: " + (horaInicioSeleccionada != null ? horaInicioSeleccionada.toString() : "-") +
            " | Hora fin: " + (horaFinSeleccionada != null ? horaFinSeleccionada.toString() : "-"));
        lblResumen.setBounds(20, 60, 400, 20); 
        contentPane.add(lblResumen);

        JLabel lblInfo = new JLabel(ResourceBundleUtil.getString("PlanificarSesionesGUI.Info"));
        lblInfo.setBounds(60, 90, 350, 16); 
        contentPane.add(lblInfo);

        JLabel actividades = new JLabel(ResourceBundleUtil.getString("PlanificarSesionesGUI.Actividad") + "s:");
        actividades.setBounds(88, 125, 90, 16); 
        contentPane.add(actividades);

        JLabel salasLibres = new JLabel(ResourceBundleUtil.getString("PlanificarSesionesGUI.Sala") + "s:");
        salasLibres.setBounds(88, 172, 61, 16); 
        contentPane.add(salasLibres);
        
        comboBoxActividades = new JComboBox<>();
        comboBoxActividades.setBounds(226, 121, 161, 27); 
        contentPane.add(comboBoxActividades);

        comboBoxSalas = new JComboBox<>();
        comboBoxSalas.setBounds(226, 168, 161, 27); 
        contentPane.add(comboBoxSalas);
        
        botonAceptar = new JButton(ResourceBundleUtil.getString("PlanificarSesionesGUI.Aceptar"));
        botonAceptar.setBounds(160, 213, 117, 29); 
        botonAceptar.setEnabled(false);
        contentPane.add(botonAceptar);
        
        JButton botonAtras = new JButton("\u2190");
        botonAtras.setBounds(10, 10, 50, 25);
        contentPane.add(botonAtras);
        botonAtras.addActionListener(e -> {
            new InicioEncargadoGUI().setVisible(true);
            dispose();
        });


        cargarActividades();
        cargarSalasLibres();

        comboBoxActividades.addActionListener(e -> {
            habilitarBotonAceptar();
            cargarSalasLibres();
        });
        comboBoxSalas.addActionListener(e -> habilitarBotonAceptar());

        botonAceptar.addActionListener(e -> {
            try {
                String nombreActividad = (String) comboBoxActividades.getSelectedItem();
                String nombreSala = (String) comboBoxSalas.getSelectedItem();
                if (nombreActividad == null || nombreSala == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona una actividad y una sala.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Actividad actividadSeleccionada = null;
                Sala salaSeleccionada = null;
                for (Actividad a : facade.getActividades()) {
                    if (a.getNombreActividad().equals(nombreActividad)) {
                        actividadSeleccionada = a;
                        break;
                    }
                }
                for (Sala s : facade.getTodasLasSalas()) {
                    if (s.getNombreSala().equals(nombreSala)) {
                        salaSeleccionada = s;
                        break;
                    }
                }
                if (actividadSeleccionada == null || salaSeleccionada == null) {
                    JOptionPane.showMessageDialog(this, "Actividad o sala no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                facade.crearSesion(
                    actividadSeleccionada,
                    salaSeleccionada,
                    ((java.sql.Date) fechaSeleccionada).toLocalDate(),
                    horaInicioSeleccionada.toLocalTime(),
                    horaFinSeleccionada.toLocalTime()
                );
                JOptionPane.showMessageDialog(this, "Sesión creada y añadida correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear la sesión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }

    private void cargarActividades() {
    	comboBoxActividades.removeAllItems();
        List<Actividad> actividades = facade.getActividades();
        for (Actividad actividad : actividades) {
        	comboBoxActividades.addItem(actividad.getNombreActividad());
        }
    }
    
    private void cargarSalasLibres() {
        comboBoxSalas.removeAllItems();

        LocalDate fecha = convertirAFechaLocalDate(this.fechaSeleccionada);
        LocalTime horaInicio = convertirAHoraLocalTime(this.horaInicioSeleccionada);
        LocalTime horaFin = convertirAHoraLocalTime(this.horaFinSeleccionada);

        List<Sala> salasLibres = facade.getTodasLasSalas();
        for (Sala sala : salasLibres) {
            boolean disponible = true;
            List<domain.Sesion> sesionesSala = facade.getSesionesDeSala(sala, fecha);
            for (domain.Sesion sesion : sesionesSala) {
                LocalTime sesionInicio = sesion.getHoraInicio();
                LocalTime sesionFin = sesion.getHoraFinal();
                if (!(horaFin.isBefore(sesionInicio) || horaInicio.isAfter(sesionFin) || horaFin.equals(sesionInicio) || horaInicio.equals(sesionFin))) {
                    disponible = false;
                    break;
                }
            }
            if (disponible) {
                comboBoxSalas.addItem(sala.getNombreSala());
            }
        }
    }

    private void habilitarBotonAceptar() {
        botonAceptar.setEnabled(
            comboBoxActividades.getSelectedItem() != null && 
            comboBoxSalas.getSelectedItem() != null
        );
    }

    public LocalDate convertirAFechaLocalDate(java.sql.Date date) {
        return date.toLocalDate();
    }

    public LocalTime convertirAHoraLocalTime(Time horaInicioSeleccionada2) {
        return horaInicioSeleccionada2.toLocalTime();
    }
}
