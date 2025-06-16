package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class InicioEncargadoGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	
	public InicioEncargadoGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle(ResourceBundleUtil.getString("InicioEncargadoGUI.Titulo"));
		
		JButton botonAñadirActividad = new JButton(ResourceBundleUtil.getString("InicioEncargadoGUI.AñadirActividad"));
		botonAñadirActividad.setBounds(127, 34, 187, 29);
		contentPane.add(botonAñadirActividad);
		
		JButton botonPlanificarSesiones = new JButton(ResourceBundleUtil.getString("InicioEncargadoGUI.PlanificarSesiones"));
		botonPlanificarSesiones.setBounds(142, 109, 161, 29);
		contentPane.add(botonPlanificarSesiones);
		
		JButton botonEnviarFacturas = new JButton(ResourceBundleUtil.getString("InicioEncargadoGUI.EnviarFacturas"));
		botonEnviarFacturas.setBounds(152, 189, 140, 29);
		contentPane.add(botonEnviarFacturas);
		
		JButton botonAtras = new JButton("\u2190"); // ←
		botonAtras.setBounds(10, 10, 50, 25);
		contentPane.add(botonAtras);
		botonAtras.addActionListener(e -> {
		    new InicioGUI().setVisible(true);
		    dispose();
		});

		// Acción al hacer clic en "Planificar sesiones"
		botonPlanificarSesiones.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("[DEBUG] Botón Planificar Sesiones pulsado");
                CrearSesionDatosGUI crearSesion = new CrearSesionDatosGUI();
                crearSesion.setVisible(true); // Muestra la nueva GUI
                dispose(); // Cierra la ventana actual
            }
        });
		
		botonAñadirActividad.addActionListener(e -> {
            AñadirActividadGUI ventana = new AñadirActividadGUI();
            ventana.setVisible(true);
        });
		
		botonEnviarFacturas.addActionListener(e -> {
            EnviarFacturasGUI ventana = new EnviarFacturasGUI();
            ventana.setVisible(true);
        });
	}
}
