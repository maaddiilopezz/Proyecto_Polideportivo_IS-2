package dataAccess;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import configuration.ConfigXML;

import javax.swing.JTextArea;



import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ObjectdbManagerServer extends JDialog {


	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	JTextArea textArea;
	ConfigXML c;
	
	
    private String objectDbpath="src/main/resources/objectdb-2.9.4/bin/objectdb-2.9.4.jar";

	public static void main(String[] args) {
		try {
			
			
			ObjectdbManagerServer dialog = new ObjectdbManagerServer();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public ObjectdbManagerServer() {
	    
		setTitle("objectDBManagerServer: running the database server");
		setBounds(100, 100, 486, 180);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			textArea = new JTextArea();
			contentPanel.add(textArea);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						textArea.append("\n\n\nClosing the database... ");
					    try {
					    	System.out.println("Server close");
					    	 try {
					    		    
					    		    
							    	Runtime.getRuntime().exec("java -cp "+objectDbpath+" com.objectdb.Server -port "+ c.getDatabasePort()+" stop");
							    	
							    } catch (Exception ioe) {
							    	System.out.println (ioe);
							    }

								System.exit(1);
							
						} catch (Exception e1) {
						}
						System.exit(1);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		ConfigXML c=ConfigXML.getInstance();
		
		if (c.isDatabaseLocal()) {
			textArea.append("\nERROR, the database is configured as local");
		}
		else {
        try{
            System.out.println("Lauching objectdb server");

            try {
                String command = "java -cp " + objectDbpath + " com.objectdb.Server -port " + c.getDatabasePort() + " start";
                System.out.println("[DEBUG] Lanzando comando: " + command);
                Process process = Runtime.getRuntime().exec(command);
                // Leer la salida estándar y de error del proceso
                java.io.BufferedReader stdInput = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
                java.io.BufferedReader stdError = new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream()));
                String s;
                textArea.append("\n[INFO] Salida del servidor ObjectDB:\n");
                while ((s = stdInput.readLine()) != null) {
                    textArea.append(s + "\n");
                    System.out.println(s);
                }
                textArea.append("\n[INFO] Errores del servidor ObjectDB:\n");
                while ((s = stdError.readLine()) != null) {
                    textArea.append(s + "\n");
                    System.err.println(s);
                }
            } catch (Exception ioe) {
                System.out.println (ioe);
                textArea.append("\n[ERROR] Error lanzando el servidor ObjectDB: " + ioe.getMessage());
            }

            textArea.append("\nAccess granted to: "+c.getUser());
            textArea.append("\nPress button to exit this database server... ");

        } catch (Exception e) {
            textArea.append("Something has happened in ObjectDbManagerServer: "+e.toString());

        }
		
		}
	}

}

