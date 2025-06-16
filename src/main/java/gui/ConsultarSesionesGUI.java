package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import businessLogic.BLFacade;
import domain.Sesion;
import domain.Actividad;
import domain.Socio;
import gui.ReservarGUI;
import gui.ResourceBundleUtil;

public class ConsultarSesionesGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> comboActividades;
    private JComboBox<Integer> comboGradoExigencia;
    private JTable tablaSesiones;
    private DefaultTableModel tablaModelo;
    private Socio socio;
    private List<Sesion> sesionesMostradas;

    public ConsultarSesionesGUI(Socio socio) {
        this.socio = socio;
        setTitle(ResourceBundleUtil.getString("consultarSesiones.titulo"));
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout());

        comboActividades = new JComboBox<>();
        cargarActividadesDesdeBD();

        comboGradoExigencia = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JButton botonBuscar = new JButton(ResourceBundleUtil.getString("consultarSesiones.botonBuscar"));
        JButton botonReservar = new JButton(ResourceBundleUtil.getString("consultarSesiones.botonReservar"));

        panelSuperior.add(new JLabel(ResourceBundleUtil.getString("consultarSesiones.actividad")));
        panelSuperior.add(comboActividades);
        panelSuperior.add(Box.createHorizontalStrut(30));
        panelSuperior.add(new JLabel(ResourceBundleUtil.getString("consultarSesiones.gradoExigencia")));
        panelSuperior.add(comboGradoExigencia);
        panelSuperior.add(Box.createHorizontalStrut(30));
        panelSuperior.add(botonBuscar);
        panelSuperior.add(botonReservar);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {
            ResourceBundleUtil.getString("consultarSesiones.columna.fecha"),
            ResourceBundleUtil.getString("consultarSesiones.columna.horaInicio"),
            ResourceBundleUtil.getString("consultarSesiones.columna.horaFin"),
            ResourceBundleUtil.getString("consultarSesiones.columna.plazasOcupadas"),
            ResourceBundleUtil.getString("consultarSesiones.columna.actividad"),
            ResourceBundleUtil.getString("consultarSesiones.columna.sala"),
            ResourceBundleUtil.getString("consultarSesiones.columna.aforoMaximo")
        };
        tablaModelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaSesiones = new JTable(tablaModelo);
        JScrollPane scrollPane = new JScrollPane(tablaSesiones);
        add(scrollPane, BorderLayout.CENTER);

        sesionesMostradas = new java.util.ArrayList<>();

        // Mostrar todas las sesiones al abrir la ventana
        BLFacade facade = InicioGUI.getBusinessLogic();
        List<Sesion> todasLasSesiones = facade.getSesionesPorFiltros(null, null);
        System.out.println("[DEBUG] ConsultarSesionesGUI abiertas. Sesiones encontradas: " + (todasLasSesiones != null ? todasLasSesiones.size() : 0));
        JOptionPane.showMessageDialog(this, ResourceBundleUtil.getString("consultarSesiones.sesionesEncontradas") + (todasLasSesiones != null ? todasLasSesiones.size() : 0), ResourceBundleUtil.getString("debug"), JOptionPane.INFORMATION_MESSAGE);
        if (todasLasSesiones != null && !todasLasSesiones.isEmpty()) {
            llenarTabla(todasLasSesiones);
        } else {
            tablaModelo.setRowCount(0);
            sesionesMostradas.clear();
        }
        tablaModelo.fireTableDataChanged();
        tablaSesiones.repaint();

        botonBuscar.addActionListener(e -> {
            String actividad = (String) comboActividades.getSelectedItem();
            int exigencia = (int) comboGradoExigencia.getSelectedItem();
            List<Sesion> sesiones = facade.getSesionesPorFiltros(actividad, exigencia);
            if (sesiones.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        ResourceBundleUtil.getString("consultarSesiones.noSesiones1") + actividad +
                                ResourceBundleUtil.getString("consultarSesiones.noSesiones2") + exigencia + ResourceBundleUtil.getString("consultarSesiones.noSesiones3"),
                        ResourceBundleUtil.getString("consultarSesiones.sinResultados"), JOptionPane.INFORMATION_MESSAGE);
                tablaModelo.setRowCount(0);
                sesionesMostradas.clear();
            } else {
                llenarTabla(sesiones);
            }
            tablaModelo.fireTableDataChanged();
            tablaSesiones.repaint();
        });

        botonReservar.addActionListener(e -> {
            int row = tablaSesiones.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, ResourceBundleUtil.getString("consultarSesiones.seleccionaSesion"), ResourceBundleUtil.getString("aviso"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (row >= 0 && row < sesionesMostradas.size()) {
                Sesion sesionSeleccionada = sesionesMostradas.get(row);
                // Comprobar si el socio puede reservar más sesiones
                if (socio.getReservas() == null || socio.getReservas().size() < socio.getMaximoReservas()) {
                    ReservarGUI reservarGUI = new ReservarGUI(socio, sesionSeleccionada);
                    reservarGUI.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, ResourceBundleUtil.getString("consultarSesiones.maximoReservas"), ResourceBundleUtil.getString("consultarSesiones.noPuedesReservar"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Doble click en fila para reservar
        tablaSesiones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && tablaSesiones.getSelectedRow() != -1) {
                    int row = tablaSesiones.getSelectedRow();
                    if (row >= 0 && row < sesionesMostradas.size()) {
                        Sesion sesionSeleccionada = sesionesMostradas.get(row);
                        new ReservarGUI(socio, sesionSeleccionada).setVisible(true);
                    }
                }
            }
        });

        JButton botonAtras = new JButton("\u2190"); // 
        botonAtras.setToolTipText(ResourceBundleUtil.getString("tooltip.atras"));
        botonAtras.setFont(new Font("Arial", Font.PLAIN, 12));
        botonAtras.addActionListener(e -> {
            new InicioSocioGUI(socio).setVisible(true);
            dispose();
        });
        JPanel panelBotonAtras = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonAtras.add(botonAtras);
        add(panelBotonAtras, BorderLayout.SOUTH);
    }

    private void cargarActividadesDesdeBD() {
        comboActividades.removeAllItems();
        BLFacade facade = InicioGUI.getBusinessLogic();
        List<Actividad> actividades = facade.getActividades();
        for (Actividad actividad : actividades) {
            comboActividades.addItem(actividad.getNombreActividad());
        }
    }

    private void llenarTabla(List<Sesion> sesiones) {
        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern(ResourceBundleUtil.getString("formato.fecha"));
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern(ResourceBundleUtil.getString("formato.hora"));
        tablaModelo.setColumnIdentifiers(new String[]{
            ResourceBundleUtil.getString("consultarSesiones.columna.fecha"),
            ResourceBundleUtil.getString("consultarSesiones.columna.horaInicio"),
            ResourceBundleUtil.getString("consultarSesiones.columna.horaFin"),
            ResourceBundleUtil.getString("consultarSesiones.columna.plazasOcupadas"),
            ResourceBundleUtil.getString("consultarSesiones.columna.actividad"),
            ResourceBundleUtil.getString("consultarSesiones.columna.sala"),
            ResourceBundleUtil.getString("consultarSesiones.columna.aforoMaximo")
        });
        tablaModelo.setRowCount(0);
        sesionesMostradas.clear();
        for (Sesion s : sesiones) {
            String actividad = s.getActividad() != null ? s.getActividad().getNombreActividad() : "";
            String sala = s.getSala() != null ? s.getSala().getNombreSala() : "";
            Object[] fila = {
                s.getFechaImparticion().format(formatterFecha),
                s.getHoraInicio().format(formatterHora),
                s.getHoraFinal().format(formatterHora),
                s.getPlazasOcupadas(),
                actividad,
                sala,
                s.getSala() != null ? s.getSala().getAforoMaximo() : "" // Mostrar aforo máximo en la tabla
            };
            tablaModelo.addRow(fila);
            sesionesMostradas.add(s);
        }
    }
}
