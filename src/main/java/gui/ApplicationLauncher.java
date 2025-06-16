package gui;

import java.awt.EventQueue;

import javax.swing.UIManager;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;

public class ApplicationLauncher { 
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				// Configuración de la apariencia de la interfaz
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

				DataAccess dataAccess = new DataAccess();  // o como lo hayas definido
		        BLFacade facade = new BLFacadeImplementation(dataAccess); // tu clase que implementa BLFacade

				InicioGUI.setBussinessLogic(facade);

				InicioGUI frame = new InicioGUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}