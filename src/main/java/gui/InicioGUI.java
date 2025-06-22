package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;

import gui.ResourceBundleUtil;

import java.util.Locale;


public class InicioGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton iniciarSesionBoton;
	private JButton registrarseBoton;
	private JButton continuarComoInvitadoBoton;
	private JRadioButton botonIngles;
	private JRadioButton botonCastellano;
	private JRadioButton botonEuskera;
	BLFacade facade = InicioGUI.getBusinessLogic(); 

	
	private static BLFacade appFacadeInterface;

	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}

	public static void setBussinessLogic (BLFacade facade){
		appFacadeInterface=facade;
	} 
	
	public InicioGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		iniciarSesionBoton = new JButton("Iniciar Sesión");
		iniciarSesionBoton.setBounds(164, 34, 117, 29);
		contentPane.add(iniciarSesionBoton);

		registrarseBoton = new JButton("Registrarse");
		registrarseBoton.setBounds(164, 94, 117, 29);
		contentPane.add(registrarseBoton);

		continuarComoInvitadoBoton = new JButton("Continuar como Invitado");
		continuarComoInvitadoBoton.setBounds(123, 156, 200, 26);
		contentPane.add(continuarComoInvitadoBoton);

		botonIngles = new JRadioButton("English");
		botonIngles.setBounds(43, 223, 100, 23); // Ampliado el ancho de 70 a 100
		contentPane.add(botonIngles);

		botonCastellano = new JRadioButton("Castellano");
		botonCastellano.setBounds(162, 223, 103, 23);
		contentPane.add(botonCastellano);
		botonCastellano.setSelected(true);

		botonEuskera = new JRadioButton("Euskera");
		botonEuskera.setBounds(314, 223, 81, 23);
		contentPane.add(botonEuskera);

		ButtonGroup grupoIdiomas = new ButtonGroup();
		grupoIdiomas.add(botonIngles);
		grupoIdiomas.add(botonCastellano);
		grupoIdiomas.add(botonEuskera);
		
		iniciarSesionBoton.addActionListener(e -> {
			IniciarSesionGUI iniciarVentana = new IniciarSesionGUI();
			iniciarVentana.setVisible(true);
			dispose();
		});

		registrarseBoton.addActionListener(e -> {
			RegistrarGUI registroVentana = new RegistrarGUI();
			registroVentana.setVisible(true);
			dispose();
		});

		continuarComoInvitadoBoton.addActionListener(e -> {
			InicioInvitadoGUI invitadoVentana = new InicioInvitadoGUI();
			invitadoVentana.setVisible(true);
			dispose();
		});
		
		botonIngles.addActionListener(e -> {
			ResourceBundleUtil.setLocale(new Locale("en"));
			actualizarTextos();
		});
		botonCastellano.addActionListener(e -> {
			ResourceBundleUtil.setLocale(new Locale("es"));
			actualizarTextos();
		});
		botonEuskera.addActionListener(e -> {
			ResourceBundleUtil.setLocale(new Locale("eus"));
			actualizarTextos();
		});

	}

	private void actualizarTextos() {
		iniciarSesionBoton.setText(ResourceBundleUtil.getString("InicioGUI.IniciarSesion"));
		registrarseBoton.setText(ResourceBundleUtil.getString("InicioGUI.Registrarse"));
		continuarComoInvitadoBoton.setText(ResourceBundleUtil.getString("InicioGUI.ContinuarComoInvitado"));
		botonIngles.setText("English");
		botonCastellano.setText("Castellano");
		botonEuskera.setText("Euskera");
		setTitle(ResourceBundleUtil.getString("MainGUI.MainTitle"));
	}
}
