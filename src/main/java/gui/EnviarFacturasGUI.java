package gui;

import businessLogic.BLFacade;
import domain.Socio;
import domain.Factura;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

public class EnviarFacturasGUI extends JFrame {
    private BLFacade facade = InicioGUI.getBusinessLogic();
    private JComboBox<Socio> comboSocios;
    private JTextField textFecha;
    private JTextArea areaFacturas;
    private JButton botonEnviar;

    public EnviarFacturasGUI() {
        setTitle(ResourceBundleUtil.getString("EnviarFacturasGUI.Titulo"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 400);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblFecha = new JLabel(ResourceBundleUtil.getString("EnviarFacturasGUI.Fecha"));
        lblFecha.setVisible(false);
        lblFecha.setBounds(30, 30, 180, 25);
        contentPane.add(lblFecha);

        textFecha = new JTextField();
        textFecha.setVisible(false);
        textFecha.setBounds(220, 30, 120, 25);
        contentPane.add(textFecha);

        JLabel lblSocio = new JLabel(ResourceBundleUtil.getString("EnviarFacturasGUI.Socio"));
        lblSocio.setBounds(30, 70, 80, 25);
        contentPane.add(lblSocio);

        comboSocios = new JComboBox<>();
        comboSocios.setBounds(120, 70, 220, 25);
        contentPane.add(comboSocios);
        comboSocios.addActionListener(e -> mostrarFacturasPendientesSocioSeleccionado());

        JButton botonCargar = new JButton(ResourceBundleUtil.getString("EnviarFacturasGUI.CargarSocios"));
        botonCargar.setBounds(350, 70, 120, 25);
        contentPane.add(botonCargar);

        areaFacturas = new JTextArea();
        areaFacturas.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaFacturas);
        scroll.setBounds(30, 120, 440, 170);
        contentPane.add(scroll);

        botonEnviar = new JButton(ResourceBundleUtil.getString("EnviarFacturasGUI.Enviar"));
        botonEnviar.setBounds(170, 310, 150, 30);
        contentPane.add(botonEnviar);
        botonEnviar.addActionListener(e -> enviarFacturas());

        botonCargar.addActionListener(e -> cargarSocios());

        JButton botonAtras = new JButton("\u2190");
        botonAtras.setBounds(10, 10, 50, 25);
        contentPane.add(botonAtras);
        botonAtras.addActionListener(e -> {
            new InicioEncargadoGUI().setVisible(true);
            dispose();
        });

        cargarSocios();
    }

    private void cargarSocios() {
        comboSocios.removeAllItems();
        List<Socio> socios = facade.getSocios();
        Set<Socio> sociosUnicos = new LinkedHashSet<>(socios);
        for (Socio socio : sociosUnicos) {
            comboSocios.addItem(socio);
        }

        if (comboSocios.getItemCount() > 0) {
            comboSocios.setSelectedIndex(0);
        }
    }

    private void mostrarFacturasPendientesSocioSeleccionado() {
        areaFacturas.setText("");
        Socio socio = (Socio) comboSocios.getSelectedItem();
        if (socio == null) {
            areaFacturas.setText("Debes seleccionar un socio.");
            return;
        }
        try {
            List<Factura> facturas = facade.getFacturasPendientesDeSocio(socio);
            if (facturas.isEmpty()) {
                areaFacturas.setText("No hay facturas pendientes para este socio.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Factura f : facturas) {
                    sb.append(f.toString()).append("\n");
                }
                areaFacturas.setText(sb.toString());
            }
        } catch (Exception ex) {
            areaFacturas.setText("Error: " + ex.getMessage());
        }
    }

    private void enviarFacturas() {
        areaFacturas.setText("");
        Socio socio = (Socio) comboSocios.getSelectedItem();
        if (socio == null) {
            areaFacturas.setText("Debes seleccionar un socio.");
            return;
        }
        try {
            List<Factura> facturas = facade.mandarFacturas(socio, null);
            if (facturas.isEmpty()) {
                areaFacturas.setText("No hay facturas pendientes para enviar a este socio.");
            } else {
                JOptionPane.showMessageDialog(this, "Facturas enviadas correctamente.", "Envío realizado",
                        JOptionPane.INFORMATION_MESSAGE);
                mostrarFacturasPendientesSocioSeleccionado();
                mostrarFacturasPendientesSocioSeleccionado();
            }
        } catch (Exception ex) {
            areaFacturas.setText("Error: " + ex.getMessage());
        }
    }
}
