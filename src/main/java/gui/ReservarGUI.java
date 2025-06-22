package gui;

import domain.Sesion;
import domain.Socio;
import businessLogic.BLFacade;
import javax.swing.*;
import java.awt.*;

public class ReservarGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Sesion sesion;
    private Socio socio;
    private BLFacade facade;

    public ReservarGUI(Socio socio, Sesion sesion) {
        this.socio = socio;
        this.sesion = sesion;
        this.facade = gui.InicioGUI.getBusinessLogic();
        setTitle(ResourceBundleUtil.getString("ReservarGUI.Titulo"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 400);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        mainPanel.add(panel, BorderLayout.CENTER);

        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Font labelFont = new Font("Arial", Font.BOLD, 13);
        Font valueFont = new Font("Arial", Font.PLAIN, 13);

        JLabel labelActividad = new JLabel(ResourceBundleUtil.getString("ReservarGUI.Actividad") + ": ");
        labelActividad.setFont(labelFont);
        panel.add(labelActividad, gbc);
        gbc.gridx = 1;
        JLabel valorActividad = new JLabel(sesion.getActividad().getNombreActividad());
        valorActividad.setFont(valueFont);
        panel.add(valorActividad, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel labelFecha = new JLabel(ResourceBundleUtil.getString("ReservarGUI.Fecha") + ": ");
        labelFecha.setFont(labelFont);
        panel.add(labelFecha, gbc);
        gbc.gridx = 1;
        JLabel valorFecha = new JLabel(sesion.getFechaImparticion().toString());
        valorFecha.setFont(valueFont);
        panel.add(valorFecha, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel labelHoraInicio = new JLabel(ResourceBundleUtil.getString("ReservarGUI.HoraInicio") + ": ");
        labelHoraInicio.setFont(labelFont);
        panel.add(labelHoraInicio, gbc);
        gbc.gridx = 1;
        JLabel valorHoraInicio = new JLabel(sesion.getHoraInicio().toString());
        valorHoraInicio.setFont(valueFont);
        panel.add(valorHoraInicio, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel labelHoraFin = new JLabel(ResourceBundleUtil.getString("ReservarGUI.HoraFin") + ": ");
        labelHoraFin.setFont(labelFont);
        panel.add(labelHoraFin, gbc);
        gbc.gridx = 1;
        JLabel valorHoraFin = new JLabel(sesion.getHoraFinal().toString());
        valorHoraFin.setFont(valueFont);
        panel.add(valorHoraFin, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel labelSala = new JLabel(ResourceBundleUtil.getString("ReservarGUI.Sala") + ": ");
        labelSala.setFont(labelFont);
        panel.add(labelSala, gbc);
        gbc.gridx = 1;
        JLabel valorSala = new JLabel(sesion.getSala() != null ? sesion.getSala().getNombreSala() : "No asignada");
        valorSala.setFont(valueFont);
        panel.add(valorSala, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel labelAforoMax = new JLabel(ResourceBundleUtil.getString("ReservarGUI.AforoMaximo") + ": ");
        labelAforoMax.setFont(labelFont);
        panel.add(labelAforoMax, gbc);
        gbc.gridx = 1;
        JLabel valorAforoMax = new JLabel(sesion.getSala() != null ? String.valueOf(sesion.getSala().getAforoMaximo()) : "null");
        valorAforoMax.setFont(valueFont);
        panel.add(valorAforoMax, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel labelPlazasOcupadas = new JLabel(ResourceBundleUtil.getString("ReservarGUI.PlazasOcupadas") + ": ");
        labelPlazasOcupadas.setFont(labelFont);
        panel.add(labelPlazasOcupadas, gbc);
        gbc.gridx = 1;
        JLabel valorPlazasOcupadas = new JLabel(String.valueOf(sesion.getPlazasOcupadas()));
        valorPlazasOcupadas.setFont(valueFont);
        panel.add(valorPlazasOcupadas, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton botonReservar = new JButton(ResourceBundleUtil.getString("ReservarGUI.Titulo"));
        botonReservar.setFont(new Font("Arial", Font.BOLD, 18)); // Aumentar tamaño de fuente
        botonReservar.setPreferredSize(new Dimension(180, 40)); // Aumentar tamaño del botón
        panel.add(botonReservar, gbc);

        gbc.gridy++;
        JLabel mensaje = new JLabel("");
        mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        mensaje.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(mensaje, gbc);

        botonReservar.addActionListener(e -> {
            System.out.println("[DEBUG] ReservarGUI: Botón reservar pulsado");
            try {
                if (sesion.getSala() == null) {
                    JOptionPane.showMessageDialog(this, "Error: la sala de esta sesión no está definida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int aforoMaximo = sesion.getSala().getAforoMaximo();
                if (aforoMaximo == 0) {
                    JOptionPane.showMessageDialog(this, "No se ha definido el aforo máximo de la sala.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println("[DEBUG] ReservarGUI: Llamando a facade.reservarSesion");
                facade.reservarSesion(socio, sesion);
                JOptionPane.showMessageDialog(this, "¡Reserva realizada con éxito!", "Reserva confirmada", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al reservar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton botonAtras = new JButton("\u2190"); // ←
        botonAtras.setFont(new Font("Arial", Font.PLAIN, 12));
        botonAtras.addActionListener(e -> {
            this.dispose();
        });
        JPanel panelBotonAtras = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonAtras.add(botonAtras);

        mainPanel.add(panelBotonAtras, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }
}
