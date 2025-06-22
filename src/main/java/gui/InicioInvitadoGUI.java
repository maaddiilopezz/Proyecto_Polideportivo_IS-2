package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import domain.Actividad;
import domain.Sesion;
import businessLogic.BLFacade;
import java.awt.*;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

public class InicioInvitadoGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<Actividad> comboActividades;
    private JTable tablaSesiones;
    private DefaultTableModel modeloTabla;
    private BLFacade facade;

    public InicioInvitadoGUI() {
        facade = InicioGUI.getBusinessLogic();

        setTitle(ResourceBundleUtil.getString("inicioInvitado.titulo"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel(new BorderLayout(10, 10));
        setContentPane(contentPane);

        comboActividades = new JComboBox<>();
        contentPane.add(comboActividades, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(new String[] {
                ResourceBundleUtil.getString("inicioInvitado.columna.fecha"),
                ResourceBundleUtil.getString("inicioInvitado.columna.horaInicio"),
                ResourceBundleUtil.getString("inicioInvitado.columna.horaFin"),
                ResourceBundleUtil.getString("inicioInvitado.columna.sala")
        });
        tablaSesiones = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaSesiones);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JButton botonAtras = new JButton("\u2190");
        botonAtras.setToolTipText(ResourceBundleUtil.getString("tooltip.atras"));
        botonAtras.setBounds(10, 10, 50, 25);
        contentPane.add(botonAtras, BorderLayout.SOUTH);
        botonAtras.addActionListener(e -> {
            new InicioGUI().setVisible(true);
            dispose();
        });
        comboActividades.removeAllItems(); // Limpiar elementos previos para evitar duplicados
        cargarActividades();

        comboActividades.addActionListener(e -> {
            Actividad actividadSeleccionada = (Actividad) comboActividades.getSelectedItem();
            if (actividadSeleccionada != null) {
                cargarSesiones(actividadSeleccionada);
            }
        });

        if (comboActividades.getItemCount() > 0) {
            comboActividades.setSelectedIndex(0); // Para cargar automáticamente
        }
    }

    private void cargarActividades() {
        List<Actividad> actividades = facade.getActividades();

        // Usar LinkedHashSet para eliminar duplicados manteniendo el orden
        Set<Actividad> actividadesUnicas = new LinkedHashSet<>(actividades);

        // Agregar al combobox solo las actividades únicas
        for (Actividad actividad : actividadesUnicas) {
            comboActividades.addItem(actividad);
        }
    }

    private void cargarSesiones(Actividad actividad) {
        List<Sesion> sesiones = facade.getSesionesDeActividad(actividad);
        modeloTabla.setRowCount(0);
        for (Sesion s : sesiones) {
            modeloTabla.addRow(new Object[] {
                    s.getFechaImparticion(),
                    s.getHoraInicio(),
                    s.getHoraFinal(),
                    s.getSala() != null ? s.getSala().getNombreSala()
                            : ResourceBundleUtil.getString("inicioInvitado.salaNoAsignada")
            });
        }
    }

}