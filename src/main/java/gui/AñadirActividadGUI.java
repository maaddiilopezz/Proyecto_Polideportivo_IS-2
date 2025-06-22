package gui;

import businessLogic.BLFacade;
import exceptions.ActividadAlreadyExistException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AñadirActividadGUI extends JFrame {
    private JTextField textNombre;
    private JComboBox<Integer> comboNivel;
    private JLabel mensaje;
    private BLFacade facade = InicioGUI.getBusinessLogic();

    public AñadirActividadGUI() {
        setTitle(ResourceBundleUtil.getString("AnadirActividadGUI.Titulo"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 250);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNombre = new JLabel(ResourceBundleUtil.getString("AnadirActividadGUI.Nombre"));
        lblNombre.setBounds(30, 30, 120, 25);
        contentPane.add(lblNombre);

        textNombre = new JTextField();
        textNombre.setBounds(170, 30, 180, 25);
        contentPane.add(textNombre);
        textNombre.setColumns(10);

        JLabel lblNivel = new JLabel(ResourceBundleUtil.getString("AnadirActividadGUI.Nivel"));
        lblNivel.setBounds(30, 70, 120, 25);
        contentPane.add(lblNivel);

        comboNivel = new JComboBox<>(new Integer[]{1,2,3,4,5});
        comboNivel.setBounds(170, 70, 60, 25);
        contentPane.add(comboNivel);

        JLabel lblPrecio = new JLabel(ResourceBundleUtil.getString("AnadirActividadGUI.Precio"));
        lblPrecio.setBounds(30, 110, 120, 25);
        contentPane.add(lblPrecio);

        JTextField textPrecio = new JTextField();
        textPrecio.setBounds(170, 110, 80, 25);
        contentPane.add(textPrecio);
        textPrecio.setColumns(10);

        JButton btnCrear = new JButton(ResourceBundleUtil.getString("AnadirActividadGUI.Crear"));
        btnCrear.setBounds(120, 150, 150, 30);
        contentPane.add(btnCrear);

        JButton botonAtras = new JButton("\u2190"); // ←
        botonAtras.setBounds(10, 10, 50, 25);
        contentPane.add(botonAtras);
        botonAtras.addActionListener(e -> {
            new InicioEncargadoGUI().setVisible(true);
            dispose();
        });

        mensaje = new JLabel("");
        mensaje.setBounds(30, 200, 320, 25);
        mensaje.setForeground(Color.RED);
        contentPane.add(mensaje);

        btnCrear.addActionListener(e -> {
            mensaje.setText("");
            String nombre = textNombre.getText().trim();
            Integer nivel = (Integer) comboNivel.getSelectedItem();
            String precioStr = textPrecio.getText().trim();
            if (nombre.isEmpty()) {
                mensaje.setText(ResourceBundleUtil.getString("AnadirActividadGUI.ErrorNombreVacio"));
                return;
            }
            double precio;
            try {
                precio = Double.parseDouble(precioStr);
                if (precio < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                mensaje.setText(ResourceBundleUtil.getString("AnadirActividadGUI.ErrorPrecio"));
                return;
            }
            try {
                facade.crearActividad(nombre, nivel, precio);
                mensaje.setForeground(new Color(0,128,0));
                mensaje.setText(ResourceBundleUtil.getString("AnadirActividadGUI.Creada"));
                textNombre.setText("");
                comboNivel.setSelectedIndex(0);
                textPrecio.setText("");
            } catch (ActividadAlreadyExistException ex) {
                mensaje.setForeground(Color.RED);
                mensaje.setText(ex.getMessage());
            } catch (Exception ex) {
                mensaje.setForeground(Color.RED);
                mensaje.setText(ResourceBundleUtil.getString("AnadirActividadGUI.Error") + ": " + ex.getMessage());
            }
        });
    }
}
