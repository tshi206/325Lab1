package nz.ac.auckland.whiteboard.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nz.ac.auckland.whiteboard.common.Graphic;
import nz.ac.auckland.whiteboard.common.Shape;


/**
 * An implementation of the Shape interface. A ShapeServant instance is a 
 * remotely accessible object that represents a particular Shape on a shared 
 * whiteboard. 
 * 
 */
public class ShapeServant extends UnicastRemoteObject implements Shape {

	private static final long serialVersionUID = 1L;
	
	private Graphic _state;
	private int _id;
	
	/**
	 * Creates a new ShapeServant instance. 
	 * @param graphic the state of the new ShapeServant object.
	 * @param id the unique ID of the new ShapeServant object.
	 * @throws RemoteException if the ShapeServant instance cannot be created. 
	 * This can happen if the RMI run-time does not have sufficient resources
	 * (e.g. sockets) to host an additional remote object.
	 */
	public ShapeServant(Graphic graphic, int id) throws RemoteException {
		super();
		this._state = graphic;
		this._id = id;
	}

	/**
	 * @see common.Shape#getAllState()
	 */
	public synchronized Graphic getAllState() throws RemoteException {
		return _state;
	}

	/**
	 * @see common.Shape#getId()
	 */
	public synchronized int getId() throws RemoteException {
		return _id;
	}
}
