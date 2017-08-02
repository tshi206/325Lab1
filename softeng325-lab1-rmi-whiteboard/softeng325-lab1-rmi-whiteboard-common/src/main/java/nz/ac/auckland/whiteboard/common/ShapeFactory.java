package nz.ac.auckland.whiteboard.common;

import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing a remote factory that creates and manages remotely
 * accessible Shape objects. This interface is intended to be implemented by a
 * class whose instances are also remotely accessible. There is typically one
 * instance of such a factory object for a client/server application. This
 * instance acts as entity that allows clients to discover and create Shape 
 * objects. 
 * 
 */
public interface ShapeFactory extends Remote {

	/**
	 * Creates a new Shape object on the server and returns a reference to it.
	 * @param graphic a description of the state of the new Shape object.
	 * @return a remote reference to the new remotely accessible Shape object.
	 * @throws FullException if the ShapeFactory is full and unable to create
	 * further shapes.
	 */
	Shape newShape(Graphic graphic) throws FullException, RemoteException;
	
	/**
	 * Returns a list of remote references to Shapes created by a 
	 * ShapeFactory implementation.
	 */
	List<Shape> allShapes() throws RemoteException;
	
	/**
	 * Returns a sublist of remote references to Shapes created by a
	 * ShapeFactory object. The sublist contains Shapes indexed from the index
	 * argument through to the last Shapes that the factory has created.
	 * @param index the index position (unique ID) of the first Shape object to
	 * return.
	 */
	List<Shape> shapes(int index) throws RemoteException;
}
