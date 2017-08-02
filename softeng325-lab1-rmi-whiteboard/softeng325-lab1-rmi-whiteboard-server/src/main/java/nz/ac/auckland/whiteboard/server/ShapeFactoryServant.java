package nz.ac.auckland.whiteboard.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import nz.ac.auckland.whiteboard.common.FullException;
import nz.ac.auckland.whiteboard.common.Graphic;
import nz.ac.auckland.whiteboard.common.Shape;
import nz.ac.auckland.whiteboard.common.ShapeFactory;


/**
 * An implementation of the ShapeFactory interface. A ShapeFactoryServant 
 * instance is a remotely accessible object that creates and stores
 * references to remotely accessible Shape objects. Within a client/server
 * application, a single ShapeFactoryServant object runs on the server; the 
 * Shape objects created by the factory also reside on the server. Clients 
 * acquire remote references to the Shape objects from the factory. 
 * 
 */
public class ShapeFactoryServant extends UnicastRemoteObject implements ShapeFactory { 

	private static final long serialVersionUID = 1L;
	
	private List<Shape> _shapes;	// List of Shapes created a ShapeFactoryServant.
	private final int _maxShapes;   // Capacity of a ShapeFactoryServant.
	
	/**
	 * Creates a ShapeFactoryServant object. 
	 * @param maxShapes the factory's capacity in terms of the maximum number 
	 * of shape objects that can be created.
	 * @throws RemoteException if the server-side RMI run-time cannot create 
	 * the ShapeFactoryServant instance. Construction can fail if the RMI 
	 * runtime has insufficient resources to host the new object.
	 */
	public ShapeFactoryServant(int maxShapes) throws RemoteException {
		super();
		_shapes = new ArrayList<Shape>();
		_maxShapes = maxShapes;
	}
	
	/**
	 * @see common.ShapeFactory#newShape()
	 */
	public synchronized Shape newShape(Graphic graphic) throws FullException, RemoteException {
		int numberOfShapes = _shapes.size();
		
		if(numberOfShapes == _maxShapes) {
			throw new FullException();
		}
		Shape newShape = new ShapeServant(graphic, numberOfShapes);
		_shapes.add(newShape);
		return newShape;
		
	}

	/**
	 * @see common.ShapeFactory#allShapes()
	 */
	public synchronized List<Shape> allShapes() throws RemoteException {
		return _shapes;
	}

	/**
	 * @see common.ShapeFactory#shapes(int)
	 */
	public synchronized List<Shape> shapes(int index) throws RemoteException {
		return _shapes.subList(index, _shapes.size() - 1);
	} 

}
