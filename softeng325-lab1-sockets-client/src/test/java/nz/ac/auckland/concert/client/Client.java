package nz.ac.auckland.concert.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import nz.ac.auckland.concert.common.Concert;
import nz.ac.auckland.concert.common.Config;
import nz.ac.auckland.concert.common.RequestMessage;
import nz.ac.auckland.concert.common.ResponseMessage;

public class Client {
	
	private static InetAddress _serverAddress;
	
	private Socket _socket;
	private ObjectInputStream _in;
	private ObjectOutputStream _out;

	/**
	 * One time setup operation to create an INetAddress object that's required
	 * to create a Socket connection.
	 */
	@BeforeClass
	public static void getServerAddress() {
		try {
			_serverAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("Error getting server address");
		}
	}
	
	
	/**
	 * Prior to running each test, create a new Socket connection with the 
	 * server and send an initial Hello message that's required as part of the 
	 * communication protocol.
	 */
	@Before
	public void createConnection() throws IOException, ClassNotFoundException {
		_socket = new Socket(_serverAddress, Config.SERVER_PORT);
		_out = new ObjectOutputStream(_socket.getOutputStream());
		_out.flush();
		_in = new ObjectInputStream(_socket.getInputStream());
			
		RequestMessage request = RequestMessage.makeHello();
		_out.writeObject(request);
		ResponseMessage response = (ResponseMessage)_in.readObject();
		assertEquals(ResponseMessage.Status.Success, response.getStatus());
	}
	
	/**
	 * After running each test, send a Clear message so that the server deletes
	 * all Concerts. Then send a Goodbye message to inform the server that the
	 * communication session is being ended. This method then closes the 
	 * connection.
	 */
	@After
	public void destroyConnection() throws IOException, ClassNotFoundException {
		// Send the server a CLEAR command to clear all Concert entries.
		RequestMessage request = RequestMessage.makeClear();
		_out.writeObject(request);
			
		// Wait for confirmation.
		_in.readObject();
			
		// Close the communication session with the server.
		request = RequestMessage.makeGoodbye();
		_out.writeObject(request);
		_socket.close();
	}
	
	/**
	 * Tests that the server processes a Create request correctly.
	 */
	@Test
	public void testCreate() throws IOException, ClassNotFoundException {
		Concert newConcert = new Concert("One Night of Queen", new DateTime(2017, 8, 4, 20, 0)); 
		RequestMessage request = RequestMessage.makeCreate(newConcert);
		
		_out.writeObject(request);
		ResponseMessage response = (ResponseMessage)_in.readObject();
		
		assertEquals(ResponseMessage.Status.Success, response.getStatus());
	}
		
	/**
	 * Tests that the server responds correctly to a Retrieve request.
	 */
	@Test
	public void testFindWithExistingId() throws IOException, ClassNotFoundException {
		// Create and store new Concert object.
		Concert newConcert = new Concert("The Selecter and the Beat", new DateTime(2018, 1, 25, 20, 0)); 
		RequestMessage request = RequestMessage.makeCreate(newConcert);
		
		_out.writeObject(request);
		
		// Read the response.
		ResponseMessage response = (ResponseMessage)_in.readObject();
		Long id = response.getId();
		newConcert = response.getConcert();
		assertEquals(ResponseMessage.Status.Success, response.getStatus());
		
		// Make a Retrieve request.
		request = RequestMessage.makeRetrieve(id);
		_out.writeObject(request);
		
		// Read the response status.
		response = (ResponseMessage)_in.readObject();
		Concert retrievedConcert = response.getConcert();
		assertEquals(ResponseMessage.Status.Success, response.getStatus());
		assertEquals(retrievedConcert, newConcert);
	}
	
	/**
	 * Tests that the server responds correctly to a Retrieve request for a
	 * non-existing Concert.
	 */
	@Test
	public void testFindWithoutExistingId() throws IOException, ClassNotFoundException {
		// Make a Retrieve request.
		RequestMessage request = RequestMessage.makeRetrieve(new Long(-1));
		_out.writeObject(request);
				
		// Read the response status.
		ResponseMessage response = (ResponseMessage)_in.readObject();
		assertEquals(ResponseMessage.Status.Failure, response.getStatus());
	}
	
	/**
	 * Tests that the server updates an existing Concert as expected.
	 */
	@Test
	public void testUpdate() throws IOException, ClassNotFoundException {
		// Create and store new Concert object.
		Concert concert = new Concert("Spend the Night with Alice Cooper", new DateTime(2017, 10, 27, 19, 0)); 
		RequestMessage request = RequestMessage.makeCreate(concert);
		
		_out.writeObject(request);
		
		// Read the response.
		ResponseMessage response = (ResponseMessage)_in.readObject();
		concert = response.getConcert();
		assertEquals(ResponseMessage.Status.Success, response.getStatus());
		
		// Attempt to update the Concert.
		concert.setDate(new DateTime(2017, 10, 28, 19, 0));
		
		request = RequestMessage.makeUpdate(concert); 
		_out.writeObject(request);
		
		response = (ResponseMessage)_in.readObject();
		assertEquals(ResponseMessage.Status.Success, response.getStatus());
	}
	
	/**
	 * Tests that the server deletes an existing Concert.
	 */
	@Test
	public void testDelete() throws IOException, ClassNotFoundException {
		// Create and store new Concert object.
		Concert concert = new Concert("The Selecter and the Beat", new DateTime(2018, 1, 25, 20, 0)); 
		RequestMessage request = RequestMessage.makeCreate(concert);
				
		_out.writeObject(request);
				
		// Read the response.
		ResponseMessage response = (ResponseMessage)_in.readObject();
		Long id = response.getId();
		assertEquals(ResponseMessage.Status.Success, response.getStatus());
				
		// Attempt to delete the Concert.
		request = RequestMessage.makeDelete(id);
		_out.writeObject(request);
				
		response = (ResponseMessage)_in.readObject();
		assertEquals(ResponseMessage.Status.Success, response.getStatus());
	}
	
	/**
	 * Tests that the server responds correctly to a request to delete a non-
	 * existent Concert.
	 */
	@Test
	public void testDeleteWithInvalidId() throws IOException, ClassNotFoundException {
		RequestMessage request = RequestMessage.makeDelete(new Long(20));
		_out.writeObject(request);
		
		ResponseMessage response = (ResponseMessage)_in.readObject();
		assertEquals(ResponseMessage.Status.Failure, response.getStatus());
	}
	
	/**
	 * Tests that the server returns an empty List when it doesn't have any 
	 * Concerts.
	 */
	@Test
	public void testListWithEmptyServer() throws IOException, ClassNotFoundException {
		RequestMessage request = RequestMessage.makeList();
		_out.writeObject(request);
		
		ResponseMessage response = (ResponseMessage)_in.readObject();
		assertEquals(ResponseMessage.Status.Success, response.getStatus());
		assertEquals(0,response.getConcerts().size());
	}
		
	/**
	 * Tests that the server returns a List containing all Concerts stored by
	 * the server.
	 */
	@Test
	public void testList() throws IOException, ClassNotFoundException {
		// Create and store new Concert object.
		Concert concert = new Concert("One Night of Queen", new DateTime(2017, 8, 4, 20, 0)); 
		RequestMessage request = RequestMessage.makeCreate(concert);
		
		if(request.getConcert() == null) {
			System.out.println("Null in client");
		}
						
		_out.writeObject(request);
						
		// Read the response.
		ResponseMessage response = (ResponseMessage)_in.readObject();
		assertEquals(ResponseMessage.Status.Success, response.getStatus());
		
		request = RequestMessage.makeList();
		_out.writeObject(request);
		
		response = (ResponseMessage)_in.readObject();
		assertEquals(ResponseMessage.Status.Success, response.getStatus());
		assertEquals(1,response.getConcerts().size());
	}

}
