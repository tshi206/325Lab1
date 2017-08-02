package nz.ac.auckland.whiteboard.server;

import java.io.Console;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import nz.ac.auckland.whiteboard.common.Config;
import nz.ac.auckland.whiteboard.common.ShapeFactory;

/**
 * Simple Java RMI server that creates a remotely accessible ShapeFactory 
 * object and registers it with RMI's Naming service.
 *
 */
public class Server {
	
	public static void main(String[] args) {
		try {
			final int MAX_SHAPES = 100;
			
			// Create the Registry on the localhost.
			Registry lookupService = LocateRegistry.createRegistry(Config.REGISTRY_PORT);
					
			// Instantiate ShapeFactoryServant.
			ShapeFactory service = new ShapeFactoryServant(MAX_SHAPES);
					
			// Advertise the ShapeFactory service using the Registry.
			lookupService.rebind(Config.SERVICE_NAME, service);
			
			Console c = System.console();
			c.readLine("Press Enter to shutdown the server ");
			lookupService.unbind(Config.SERVICE_NAME);
			
			System.out.println(
					"The Whiteboard service is no longer bound in the RMI registry. " 
					+ "Waiting for lease to expire.");
			
		} catch(RemoteException e) {
			System.out.println("Unable to start or register proxy with the RMI Registry");
			e.printStackTrace();
		} catch(NotBoundException e) {
			System.out.println("Unable to remove proxy from the  RMI Registry");
		}
	}
}