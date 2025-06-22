package gui;

import java.awt.EventQueue;
import java.net.URL;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;

public class ApplicationLauncherWebService { 
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				// Configuraci�n de la apariencia de la interfaz
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

				URL url = new URL("http://127.0.0.1:9999/ws/Rides?wsdl");
				QName qname = new QName("http://businessLogic/","BLFacadeImplementationService");
				Service service = Service.create(url, qname);
				BLFacade facade=
				service.getPort(BLFacade.class);

				InicioGUI.setBussinessLogic(facade);

				InicioGUI frame = new InicioGUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}