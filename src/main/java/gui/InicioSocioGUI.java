package gui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import domain.Socio;

public class InicioSocioGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Socio socio;

	public InicioSocioGUI(Socio socio) {
		this.socio = socio;
		setTitle(ResourceBundleUtil.getString("InicioSocioGUI.Titulo"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton botonConsultarSesiones = new JButton(ResourceBundleUtil.getString("InicioSocioGUI.ConsultarSesiones"));
		botonConsultarSesiones.setBounds(146, 58, 155, 29);
		contentPane.add(botonConsultarSesiones);
		
		JButton botonConsultarReservas = new JButton(ResourceBundleUtil.getString("InicioSocioGUI.ConsultarReservas"));
		botonConsultarReservas.setBounds(146, 120, 155, 29);
		contentPane.add(botonConsultarReservas);
		
		JButton botonConsultarFacturas = new JButton(ResourceBundleUtil.getString("InicioSocioGUI.ConsultarFacturas"));
		botonConsultarFacturas.setBounds(146, 188, 155, 29);
		contentPane.add(botonConsultarFacturas);
		
		JLabel labelQueHacer = new JLabel(ResourceBundleUtil.getString("InicioSocioGUI.QueHacer"));
		labelQueHacer.setBounds(164, 21, 137, 16);
		contentPane.add(labelQueHacer);
		
		JButton botonAtras = new JButton("\u2190");
		botonAtras.setBounds(10, 10, 50, 25);
		contentPane.add(botonAtras);

		botonConsultarSesiones.addActionListener(e -> {
			try {
				System.out.println("Botón Consultar Sesiones pulsado");
				ConsultarSesionesGUI nuevaVentana = new ConsultarSesionesGUI(socio);
				nuevaVentana.setVisible(true);
			dispose();
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error al abrir ConsultarSesionesGUI: " + ex.getMessage());
			}
		});
		
		botonConsultarReservas.addActionListener(e -> {
			ConsultarReservasGUI nuevaVentana = new ConsultarReservasGUI(socio);
			nuevaVentana.setVisible(true);
			dispose();  
		});
		
		botonConsultarFacturas.addActionListener(e -> {
			ConsultarFacturasGUI nuevaVentana = new ConsultarFacturasGUI(socio);
			nuevaVentana.setVisible(true);
			dispose();  
		});
		
		botonAtras.addActionListener(e -> {
			new InicioGUI().setVisible(true);
			dispose();
		});
	}
}
