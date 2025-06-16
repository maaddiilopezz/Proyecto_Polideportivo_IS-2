package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import domain.Reserva;
import domain.Socio;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

public class ConsultarReservasGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private BLFacade businessLogic;
    private Socio socio;

    // Recibe el socio para mostrar sus reservas
    public ConsultarReservasGUI(Socio socio) {
        this.socio = socio;
        businessLogic = new BLFacadeImplementation();

        setTitle(ResourceBundleUtil.getString("consultarReservas.titulo"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 350);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        JLabel label = new JLabel(ResourceBundleUtil.getString("consultarReservas.label") + socio.getNombre());
        label.setFont(new Font("Arial", Font.BOLD, 16));
        contentPane.add(label, BorderLayout.NORTH);

        DefaultListModel<Reserva> reservasModel = new DefaultListModel<>();
        JList<Reserva> listaReservas = new JList<>(reservasModel);
        listaReservas.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(listaReservas);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JButton botonAtras = new JButton("\u2190");
        botonAtras.setToolTipText(ResourceBundleUtil.getString("tooltip.atras"));
        botonAtras.setFont(new Font("Arial", Font.PLAIN, 13));
        botonAtras.addActionListener(e -> {
            new InicioSocioGUI(socio).setVisible(true);
            dispose();
        });
        JPanel panelBotonAtras = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonAtras.add(botonAtras);
        contentPane.add(panelBotonAtras, BorderLayout.SOUTH);

        // Cargar reservas del socio SIEMPRE desde la base de datos
        List<Reserva> reservas = businessLogic.getReservasDeSocio(socio);
        if (reservas != null && !reservas.isEmpty()) {
            for (Reserva r : reservas) {
                reservasModel.addElement(r);
            }
        } else {
            reservasModel.addElement(new Reserva(null, null) {
                public String toString() { return ResourceBundleUtil.getString("consultarReservas.noReservas"); }
            });
        }
    }
}
