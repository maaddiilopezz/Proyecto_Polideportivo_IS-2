package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import domain.Socio;
import domain.Factura;
import java.util.List;
import java.awt.*;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;

public class ConsultarFacturasGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JList<Factura> listaFacturas;
    private BLFacade businessLogic;
    private Socio socio;

    public ConsultarFacturasGUI(Socio socio) {
        this.socio = socio;
        businessLogic = InicioGUI.getBusinessLogic();
        setTitle(ResourceBundleUtil.getString("ConsultarFacturasGUI.Titulo"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        JLabel label = new JLabel(
                ResourceBundleUtil.getString("ConsultarFacturasGUI.Facturas") + ": " + socio.getNombre());
        contentPane.add(label, BorderLayout.NORTH);

        listaFacturas = new JList<>();
        JScrollPane scrollPane = new JScrollPane(listaFacturas);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JButton botonAtras = new JButton("\u2190");
        botonAtras.setBounds(10, 10, 50, 25);
        contentPane.add(botonAtras, BorderLayout.SOUTH);
        botonAtras.addActionListener(e -> {
            new InicioSocioGUI(socio).setVisible(true);
            dispose();
        });

        JButton botonPagar = new JButton(ResourceBundleUtil.getString("ConsultarFacturasGUI.Pagar"));
        contentPane.add(botonPagar, BorderLayout.EAST);
        botonPagar.addActionListener(e -> {
            Factura facturaSeleccionada = listaFacturas.getSelectedValue();
            if (facturaSeleccionada == null) {
                JOptionPane.showMessageDialog(this,
                        ResourceBundleUtil.getString("ConsultarFacturasGUI.SeleccionarFactura"),
                        ResourceBundleUtil.getString("Aviso"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    ResourceBundleUtil.getString("ConsultarFacturasGUI.ConfirmarPago"),
                    ResourceBundleUtil.getString("Confirmar"), JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    businessLogic.pagarFactura(facturaSeleccionada);
                    JOptionPane.showMessageDialog(this,
                            ResourceBundleUtil.getString("ConsultarFacturasGUI.FacturaPagada"),
                            ResourceBundleUtil.getString("PagoRealizado"), JOptionPane.INFORMATION_MESSAGE);
                    cargarFacturas(socio); // Refresca la lista
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ResourceBundleUtil.getString("Error") + ": " + ex.getMessage(),
                            ResourceBundleUtil.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cargarFacturas(socio);
    }

    private void cargarFacturas(Socio socio) {
        DefaultListModel<Factura> model = new DefaultListModel<>();
        List<Factura> facturas = businessLogic.getFacturasDeSocio(socio);
        if (facturas != null) {
            for (Factura f : facturas) {
                model.addElement(f);
            }
        }
        listaFacturas.setModel(model);
    }
}
