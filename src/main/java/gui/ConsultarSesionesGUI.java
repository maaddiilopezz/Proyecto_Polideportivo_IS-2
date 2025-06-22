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

public class ConsultarSesionesGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> comboActividades;
    private JComboBox<Integer> comboGradoExigencia;
    private JTable tablaSesiones;
    private DefaultTableModel tablaModelo;
    private List<Sesion> sesionesMostradas;
    private Socio socio;

    public ConsultarSesionesGUI(Socio socio) {
        this.socio = socio;
        setTitle("Consultar Sesiones");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout());

        comboActividades = new JComboBox<>();
        cargarActividadesDesdeBD();

        comboGradoExigencia = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JButton botonBuscar = new JButton("Buscar");
        JButton botonReservar = new JButton("Reservar");

        panelSuperior.add(new JLabel("Actividad"));
        panelSuperior.add(comboActividades);
        panelSuperior.add(Box.createHorizontalStrut(30));
        panelSuperior.add(new JLabel("Grado de Exigencia"));
        panelSuperior.add(comboGradoExigencia);
        panelSuperior.add(Box.createHorizontalStrut(30));
        panelSuperior.add(botonBuscar);
        panelSuperior.add(botonReservar);

        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {
            "Fecha",
            "Hora Inicio",
            "Hora Fin",
            "Plazas Ocupadas",
            "Actividad",
            "Sala",
            "Aforo Maximo"
        };
        tablaModelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaSesiones = new JTable(tablaModelo);
        JScrollPane scrollPane = new JScrollPane(tablaSesiones);
        add(scrollPane, BorderLayout.CENTER);

        sesionesMostradas = new java.util.ArrayList<>();

        BLFacade facade = InicioGUI.getBusinessLogic();
        List<Sesion> todasLasSesiones = facade.getSesionesPorFiltros(null, null);
        System.out.println("[DEBUG] ConsultarSesionesGUI abiertas. Sesiones encontradas: " + (todasLasSesiones != null ? todasLasSesiones.size() : 0));
        JOptionPane.showMessageDialog(this, "Sesiones encontradas: " + (todasLasSesiones != null ? todasLasSesiones.size() : 0), "Debug", JOptionPane.INFORMATION_MESSAGE);
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
                        "No se encontraron sesiones para la actividad " + actividad +
                                " con grado de exigencia " + exigencia + ".",
                        "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Por favor, selecciona una sesión para reservar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (row >= 0 && row < sesionesMostradas.size()) {
                Sesion sesionSeleccionada = sesionesMostradas.get(row);
                // Comprobar si el socio puede reservar más sesiones
                if (this.socio.getReservas() == null || this.socio.getReservas().size() < this.socio.getMaximoReservas()) {
                    ReservarGUI reservarGUI = new ReservarGUI(this.socio, sesionSeleccionada);
                    reservarGUI.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Has alcanzado el máximo de reservas permitidas.", "No puedes reservar", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tablaSesiones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println("[DEBUG] Doble click detectado: " + evt.getClickCount());
                int row = tablaSesiones.getSelectedRow();
                System.out.println("[DEBUG] Fila seleccionada: " + row);
                if (evt.getClickCount() == 2 && row != -1) {
                    if (row >= 0 && row < sesionesMostradas.size()) {
                        Sesion sesionSeleccionada = sesionesMostradas.get(row);
                        System.out.println("[DEBUG] Sesion seleccionada: " + sesionSeleccionada);
                        new ReservarGUI(ConsultarSesionesGUI.this.socio, sesionSeleccionada).setVisible(true);
                    } else {
                        System.out.println("[DEBUG] Fila fuera de rango de sesionesMostradas");
                    }
                }
            }
        });

        JButton botonAtras = new JButton("\u2190"); //  ȉ
        botonAtras.setToolTipText("Atrás");
        botonAtras.setFont(new Font("Arial", Font.PLAIN, 12));
        botonAtras.addActionListener(e -> {
            new InicioSocioGUI(this.socio).setVisible(true);
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
        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
        tablaModelo.setColumnIdentifiers(new String[]{
            "Fecha",
            "Hora Inicio",
            "Hora Fin",
            "Plazas Ocupadas",
            "Actividad",
            "Sala",
            "Aforo Maximo"
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
