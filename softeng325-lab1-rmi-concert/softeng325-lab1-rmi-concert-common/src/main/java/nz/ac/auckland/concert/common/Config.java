package nz.ac.auckland.concert.common;

/**
 * Class with configuration settings for the application.
 *
 */
public class Config {
	// Port number that the RMI registry will use.
	public static final int REGISTRY_PORT = 8091;
	
	// Name used to advertise/register the ShapeFactory service in the RMI
	// Registry.
	public static final String SERVICE_NAME = "concert-factory";
}