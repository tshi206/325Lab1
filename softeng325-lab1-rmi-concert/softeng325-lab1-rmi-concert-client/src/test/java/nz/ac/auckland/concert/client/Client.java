package nz.ac.auckland.concert.client;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import nz.ac.auckland.concert.common.Concert;
import nz.ac.auckland.concert.common.ConcertFactory;
import nz.ac.auckland.concert.common.Config;
import nz.ac.auckland.concert.common.Sender;


public class Client {

	// Proxy object to represent the remote ShapeFactory service.
	private static ConcertFactory _proxy;

	/**
	 * One-time setup method to retrieve the ShapeFactory proxy from the RMI 
	 * Registry.
	 */
	@BeforeClass
	public static void getProxy() {
		try {
			// Instantiate a proxy object for the RMI Registry, expected to be
			// running on the local machine and on a specified port. 
			Registry lookupService = LocateRegistry.getRegistry("localhost", Config.REGISTRY_PORT);

			// Retrieve a proxy object representing the ShapeFactory.
			_proxy = (ConcertFactory)lookupService.lookup(Config.SERVICE_NAME);
		} catch (RemoteException e) {
			System.out.println("Unable to connect to the RMI Registry");
		} catch (NotBoundException e) {
			System.out.println("Unable to acquire a proxy for the Concert service");
		}
	}

	/**
	 * Test that, using the ShapeFactory proxy, we can invoke methods on the 
	 * remote ShapeFactory to create remotely accessible Shapes. This test also
	 * then invokes methods on the remote Shapes objects, via their acquired 
	 * proxies.
	 */
	@Test
	public void testCreate() throws RemoteException {

		// Use the ShapeFactory proxy to create a couple of remote Shape
		// instances. newShape() returns proxies for the new remote Shapes.
		Concert concertA = _proxy.createConcert(new Sender("One Night of Queen", new DateTime(2017, 8, 4, 20, 0)));
		Concert concertB = _proxy.createConcert(new Sender("Spend the Night with Alice Cooper", new DateTime(2017, 10, 27, 19, 0)));

		// Query the new Shape object's ids. the getId() calls are remote
		// method invocations on the Shapes that have been created on the
		// the server.
		System.out.println("ShapeA's Id is " + concertA.getId());
		System.out.println("ShapeB's Id is " + concertB.getId());

		// Query the remote factory.
		List<Concert> remoteConcerts = _proxy.getAllConcerts();

		assertTrue(remoteConcerts.contains(concertA));
		assertTrue(remoteConcerts.contains(concertB));
		assertEquals(2, remoteConcerts.size());

		for(Concert s : remoteConcerts) {
			// First iteration of this loop calls getAllstate() on the
			// same remote Shape object that shapeA acts as a remote
			// reference for, the second iteration on shapeB's remote
			// object.
			System.out.println(s.getTitle().toString()+"   "+s.getDate());
		}
		
		concertB.setDate(new DateTime(2019, 12, 23, 20, 0));
		_proxy.updateConcert(concertB);
		System.out.println(concertB.getTitle().toString()+"   "+concertB.getDate()+"  Record has been updated");
		
		concertA = _proxy.getConcert(new Long(0));
		System.out.println(concertA.getTitle().toString()+"   "+concertA.getDate()+"  One Night of Queen is going to be deleted");
		
		_proxy.deleteConcert(new Long(0));
		for(Concert s : remoteConcerts) {
			// First iteration of this loop calls getAllstate() on the
			// same remote Shape object that shapeA acts as a remote
			// reference for, the second iteration on shapeB's remote
			// object.
			System.out.println(s.getTitle().toString()+"   "+s.getDate()+"  One Night of Queen has been deleted");
		}

		_proxy.clear();
		assertTrue(remoteConcerts.isEmpty());
	}

}
