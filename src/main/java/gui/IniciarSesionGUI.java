package gui;

import java.awt.Color;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import domain.Usuario;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import gui.RegistrarGUI;
import businessLogic.BLFacade;
import exceptions.SocioAlreadyExistException;
import javax.swing.JRadioButton;
import gui.ResourceBundleUtil;

public class IniciarSesionGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textCorreo;
    private JPasswordField textContraseña;
    private JButton botonAceptar;
    BLFacade facade; 
    private JLabel mensajeError;
    private JLabel mensajeAprobado;
    private JRadioButton botonSocio;
    private JRadioButton botonEncargado;

   
    public IniciarSesionGUI() {
        facade = InicioGUI.getBusinessLogic();
        setTitle(ResourceBundleUtil.getString("IniciarSesionGUI.Titulo"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel correoElectronico = new JLabel(ResourceBundleUtil.getString("IniciarSesionGUI.CorreoElectronico"));
        correoElectronico.setBounds(57, 62, 127, 16);
        contentPane.add(correoElectronico);
        
        JLabel iniciarSesion = new JLabel(ResourceBundleUtil.getString("IniciarSesionGUI.Titulo"));
        iniciarSesion.setBounds(173, 24, 180, 16);
        contentPane.add(iniciarSesion);
        
        JLabel contrasena = new JLabel(ResourceBundleUtil.getString("IniciarSesionGUI.Contrasena"));
        
        contrasena.setBounds(57, 107, 84, 16);
        contentPane.add(contrasena);
        
        textCorreo = new JTextField();
        textCorreo.setBounds(225, 57, 130, 26);
        contentPane.add(textCorreo);
        textCorreo.setColumns(10);
        
        botonAceptar = new JButton(ResourceBundleUtil.getString("IniciarSesionGUI.Aceptar"));
        botonAceptar.setEnabled(false);
        botonAceptar.setBounds(157, 207, 117, 29);
        contentPane.add(botonAceptar);
        
        JButton botonAtras = new JButton("\u2190"); // ←
        botonAtras.setBounds(10, 10, 50, 25);
        contentPane.add(botonAtras);
        
        mensajeAprobado = new JLabel();
        mensajeAprobado.setVisible(false);
        mensajeAprobado.setForeground(new Color(0, 128, 0));
        mensajeAprobado.setBounds(51, 195, 393, 14);
        contentPane.add(mensajeAprobado);
        
        mensajeError = new JLabel();
        mensajeError.setVisible(false);
        mensajeError.setForeground(Color.RED);
        mensajeError.setBounds(51, 175, 393, 14);
        contentPane.add(mensajeError);
        
        textContraseña = new JPasswordField();
        textContraseña.setBounds(225, 102, 137, 26);
        contentPane.add(textContraseña);
        
        textCorreo.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                habilitarBotonAceptar();
            }
        });
        textContraseña.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                habilitarBotonAceptar();
            }
        });
        
        botonSocio = new JRadioButton(ResourceBundleUtil.getString("IniciarSesionGUI.Socio"));
        botonSocio.setBounds(104, 157, 95, 23);
        contentPane.add(botonSocio);
        
        botonEncargado = new JRadioButton(ResourceBundleUtil.getString("IniciarSesionGUI.Encargado"));
        botonEncargado.setBounds(243, 157, 112, 23);
        contentPane.add(botonEncargado);
        
        ButtonGroup group = new ButtonGroup();
        group.add(botonSocio);
        group.add(botonEncargado);
        // Acción del botón Aceptar
        botonAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String correo = textCorreo.getText();
                String contraseña = new String(textContraseña.getPassword());
                mensajeAprobado.setVisible(false);
                mensajeError.setVisible(false);
                if (correo.isEmpty() || contraseña.isEmpty()) {
                    mensajeError.setText(ResourceBundleUtil.getString("IniciarSesionGUI.ErrorCamposVacios"));
                    mensajeError.setVisible(true);
                    return;
                }
                if (!botonSocio.isSelected() && !botonEncargado.isSelected()) {
                    mensajeError.setText(ResourceBundleUtil.getString("IniciarSesionGUI.ErrorSeleccionTipo"));
                    mensajeError.setVisible(true);
                    return;
                }
                Usuario usuario = facade.iniciarSesion(correo, contraseña);
                if (usuario == null) {
                    mensajeError.setText(ResourceBundleUtil.getString("IniciarSesionGUI.Error"));
                    mensajeError.setVisible(true);
                } else if (botonSocio.isSelected()) {
                    if (usuario instanceof domain.Socio) {
                        mensajeAprobado.setText(ResourceBundleUtil.getString("IniciarSesionGUI.ExitoSocio"));
                        mensajeAprobado.setVisible(true);
                        InicioSocioGUI ventanaSocio = new InicioSocioGUI((domain.Socio) usuario);
                        ventanaSocio.setVisible(true);
                        dispose();
                    } else {
                        mensajeError.setText(ResourceBundleUtil.getString("IniciarSesionGUI.ErrorTipoSocio"));
                        mensajeError.setVisible(true);
                    }
                } else if (botonEncargado.isSelected()) {
                    if (usuario instanceof domain.Encargado) {
                        mensajeAprobado.setText(ResourceBundleUtil.getString("IniciarSesionGUI.ExitoEncargado"));
                        mensajeAprobado.setVisible(true);
                        InicioEncargadoGUI ventanaEncargado = new InicioEncargadoGUI();
                        ventanaEncargado.setVisible(true);
                        dispose();
                    } else {
                        mensajeError.setText(ResourceBundleUtil.getString("IniciarSesionGUI.ErrorTipoEncargado"));
                        mensajeError.setVisible(true);
                    }
                }
            }
        });
        botonAtras.addActionListener(e -> {
            new InicioGUI().setVisible(true);
            dispose();
        });
    }

    private void habilitarBotonAceptar() {
        String correo = textCorreo.getText();
        String contraseña = new String(textContraseña.getPassword());
        botonAceptar.setEnabled(!correo.isEmpty() && !contraseña.isEmpty());
    }
}