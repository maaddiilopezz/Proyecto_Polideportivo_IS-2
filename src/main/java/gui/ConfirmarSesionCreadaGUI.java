
package gui;

import businessLogic.BLFacade;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;
import domain.Actividad;
import domain.Sala;

public class ConfirmarSesionCreadaGUI extends JFrame {
    BLFacade facade = InicioGUI.getBusinessLogic();

    // Nuevo constructor para recibir solo fecha, horaInicio y horaFin
    public ConfirmarSesionCreadaGUI(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;

        // Panel para seleccionar actividad y sala
        setTitle(ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.Titulo"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(10, 10));

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(null);
        int panelWidth = 400;
        int yOffset = 70; // Para bajar el contenido
        int labelWidth = 100;
        int fieldWidth = 180;
        int labelX = (panelWidth - labelWidth - fieldWidth) / 2;
        int fieldX = labelX + labelWidth;

        JLabel labelActividadSel = new JLabel(ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.Actividad"));
        labelActividadSel.setBounds(labelX, yOffset, labelWidth, 21);
        panelDatos.add(labelActividadSel);
        // actividad
        DefaultComboBoxModel<Actividad> modeloActividades = new DefaultComboBoxModel<>();
        JComboBox<Actividad> comboActividad = new JComboBox<>(modeloActividades);
        comboActividad.setBounds(fieldX, yOffset, fieldWidth, 21);
        // Renderizador para mostrar solo el nombre
        comboActividad.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Actividad) {
                    setText(((Actividad) value).getNombreActividad());
                }
                return this;
            }
        });
        panelDatos.add(comboActividad);

        JLabel labelSalaSel = new JLabel(ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.Sala"));
        labelSalaSel.setBounds(labelX, yOffset + 40, labelWidth, 21);
        panelDatos.add(labelSalaSel);
        DefaultComboBoxModel<Sala> modeloSalas = new DefaultComboBoxModel<>();
        JComboBox<Sala> comboSala = new JComboBox<>(modeloSalas);
        comboSala.setBounds(fieldX, yOffset + 40, fieldWidth, 21);
        comboSala.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Sala) {
                    // Si el nombre ya es "Sala x", lo muestra tal cual, si no, antepone "Sala "
                    String nombre = ((Sala) value).getNombreSala();
                    if (nombre.matches("Sala \\d+")) {
                        setText(nombre);
                    } else {
                        setText("Sala " + nombre.replaceAll("[^0-9]", "").trim());
                    }
                }
                return this;
            }
        });
        panelDatos.add(comboSala);

        
        JLabel labelFechaSel = new JLabel(
                ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.Fecha") + ": " + fecha.toString());
        labelFechaSel.setBounds(labelX, yOffset + 80, labelWidth + fieldWidth, 21);
        panelDatos.add(labelFechaSel);
        JLabel labelHoraInicioSel = new JLabel(
                ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.HoraInicio") + ": " + horaInicio.toString());
        labelHoraInicioSel.setBounds(labelX, yOffset + 110, labelWidth + fieldWidth, 21);
        panelDatos.add(labelHoraInicioSel);
        JLabel labelHoraFinSel = new JLabel(
                ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.HoraFin") + ": " + horaFin.toString());
        labelHoraFinSel.setBounds(labelX, yOffset + 140, labelWidth + fieldWidth, 21);
        panelDatos.add(labelHoraFinSel);

        
        JButton botonConfirmar = new JButton(ResourceBundleUtil.getString("ConfirmarSesionCreadaGUI.Confirmar"));
        botonConfirmar.setBounds((panelWidth - 140) / 2, yOffset + 190, 140, 29);
        panelDatos.add(botonConfirmar);

        
        JButton botonAtras = new JButton("\u2190");
        botonAtras.setBounds(10, 10, 50, 25);
        panelDatos.add(botonAtras);
        botonAtras.addActionListener(e -> {
            new CrearSesionDatosGUI(fecha, horaInicio, horaFin).setVisible(true);
            dispose();
        });
        java.util.List<Actividad> actividades = facade.getActividades();
        Set<Actividad> actividadesUnicas = new LinkedHashSet<>(actividades);
        for (Actividad a : actividadesUnicas)
            comboActividad.addItem(a);

        java.util.List<Sala> salas = facade.getTodasLasSalas();
        Set<Sala> salasUnicas = new LinkedHashSet<>(salas);
        for (Sala s : salasUnicas)
            comboSala.addItem(s);

        // Acción confirmar
        botonConfirmar.addActionListener(e -> {
            Actividad actividadSel = (Actividad) comboActividad.getSelectedItem();
            Sala salaSel = (Sala) comboSala.getSelectedItem();
            if (actividadSel == null || salaSel == null) {
                JOptionPane.showMessageDialog(this, "Selecciona actividad y sala.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                facade.crearSesion(actividadSel, salaSel, fecha.toString(), horaInicio.toString(), horaFin.toString());
                JOptionPane.showMessageDialog(this, "Sesión confirmada y creada en la base de datos.", "Confirmación",
                        JOptionPane.INFORMATION_MESSAGE);
                // Volver al menú del encargado
                new InicioEncargadoGUI().setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear la sesión: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        getContentPane().add(panelDatos, BorderLayout.CENTER);
    }

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

    public ConfirmarSesionCreadaGUI(Actividad actividad, Sala sala, LocalDate fecha, LocalTime horaInicio,
            LocalTime horaFin) {
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

        
        botonConfirmar.addActionListener(e -> {
            try {
                InicioGUI.getBusinessLogic().crearSesion(actividad, sala, fecha.toString(), horaInicio.toString(),
                        horaFin.toString());
                JOptionPane.showMessageDialog(this, "Sesión confirmada y creada en la base de datos.", "Confirmación",
                        JOptionPane.INFORMATION_MESSAGE);
                // Volver al menú del encargado
                new InicioEncargadoGUI().setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear la sesión: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton botonAtras = new JButton("\u2190");
        botonAtras.setBounds(10, 10, 50, 25);
        panelDatos.add(botonAtras);
        botonAtras.addActionListener(e -> {
            new InicioEncargadoGUI().setVisible(true);
            dispose();
        });

        getContentPane().add(panelDatos, BorderLayout.CENTER);

        
        JPanel panelBotones = new JPanel();
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }
}
