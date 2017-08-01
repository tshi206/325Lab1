package nz.ac.auckland.concert.server;

import java.io.Console;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.ac.auckland.concert.common.Concert;
import nz.ac.auckland.concert.common.Config;
import nz.ac.auckland.concert.common.RequestMessage;
import nz.ac.auckland.concert.common.ResponseMessage;

/**
 * Simple server that maintains a collection of Concerts, and which implements
 * a protocol allowing clients to make CRUD requests of the server.
 * 
 * Clients are expected to follow a protocol whereby a Hello message is the 
 * first message that they should send. Once acknowledged, clients can then
 * send further messages, as defined by class RequestMessage. At the end of a
 * communication session, clients should send a Goodbye message, informing the
 * server that no further messages will be sent.
 *
 */
public class Server {
	// List of Concerts.
	private Map<Long, Concert> _concerts;
	
	// Unique id of the next concert to create.
	private long _nextId;
	
	// Network connection objects.
	private ServerSocket _socket;
	private Socket _clientConnection;
	private ObjectOutputStream _out;
	private ObjectInputStream _in;

	public Server() {
		_concerts = new HashMap<Long, Concert>();
		_nextId = 1;
	}

	/**
	 * Starts the server, causing it to block while waiting for a connection 
	 * request. Once a connection has been accepted, the server processes 
	 * incoming messages coming over the connection.
	 */
	public void start() throws IOException {
		_socket = new ServerSocket(Config.SERVER_PORT);

		InetAddress serverHost = InetAddress.getLocalHost();
		System.out.println("Server destination: " + serverHost.getHostAddress()
				+ ", " + _socket.getLocalPort());

		// Repeatedly handle requests for processing.
		boolean quit = false;

		while (!quit) {
			try {
				_clientConnection = _socket.accept();
				_out = new ObjectOutputStream(
						_clientConnection.getOutputStream());
				_out.flush();
				_in = new ObjectInputStream(
						_clientConnection.getInputStream());

				RequestMessage request = (RequestMessage) _in.readObject();
				ResponseMessage response = null;

				if(request.getType() != RequestMessage.MessageType.Hello) {
					// Accepted a new connection, but the first message isn't
					// a Hello message - so terminate the connection.
					response = ResponseMessage.makeProtocolErrorResponse();
					_out.writeObject(response);
					_clientConnection.close();
				} else {
					// Accepted a new connection and received the initial 
					// Hello message.
					response = ResponseMessage.makeSuccessfulResponse();
					_out.writeObject(response);
					manageSession();
				}
			} catch(IOException e) {
				quit = true;
			} catch(ClassNotFoundException e) {
				// Server attempted to deserialise an object without having 
				// access to the corresponding class.
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Stops the server.
	 */
	public void shutdown() {
		try {
			// Closing the ServerSocket will cause any accept() call on it to 
			// abort and throw an IOException.
			_socket.close();
			
			// Closing the Socket connection to the client will cause any IO
			// methods to abort with an IOException.
			_clientConnection.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * Processes incoming messages, generating and sending a response back to 
	 * the client.
	 */
	private void manageSession() {
		boolean sessionEnded = false;
		ResponseMessage response = null;
		
		try {
			while(!sessionEnded) {
				// Read next request.
				RequestMessage request = (RequestMessage) _in.readObject();
				System.out.println("Received a new message: " + request.getType());
				
				switch (request.getType()) {
				case Hello: {
					response = ResponseMessage.makeProtocolErrorResponse();
					break;
				}
				case Goodbye: {
					// Acknowledge the client's intention to close the session.
					response = ResponseMessage.makeSuccessfulResponse();
					sessionEnded = true;
					break;
				}
				case Create: {
					Concert concert = request.getConcert();

					// Store the new Concert.
					Long uid = new Long(_nextId++);
					Concert newConcert = new Concert(uid, concert.getTitle(),
							concert.getDate());
					_concerts.put(uid, newConcert);

					response = ResponseMessage
							.makeSuccessfulCreateResponse(newConcert);
					break;
				}
				case Retrieve: {
					Long uid = request.getId();
					Concert concert = _concerts.get(uid);
					if (concert == null) {
						response = ResponseMessage
								.makeUnsuccessfulRetrieveResponse();
					} else {
						response = ResponseMessage
								.makeSuccessfulRetrieveResponse(concert);
					}
					break;
				}
				case Update: {
					Concert concert = request.getConcert();
					if (!_concerts.containsKey(concert.getId())) {
						response = ResponseMessage
								.makeUnsuccessfulUpdateResponse();
					} else {
						_concerts.put(concert.getId(), concert);
						response = ResponseMessage
								.makeSuccessfulResponse();
					}
					break;
				}
				case Delete: {
					Long uid = request.getId();

					Concert concert = _concerts.get(uid);
					if (concert == null) {
						response = ResponseMessage
								.makeUnsuccessfulDeleteResponse();
					} else {
						_concerts.remove(uid);
						response = ResponseMessage
								.makeSuccessfulResponse();
					}
					break;
				}
				case List: {
					List<Concert> concertList = new ArrayList<Concert>(
							_concerts.values());
					response = ResponseMessage.makeListResponse(concertList);
					break;
				}
				case Clear: {
					_concerts.clear();
					response = ResponseMessage.makeSuccessfulResponse();
				}
				}

				// Send response back to the client.
				_out.writeObject(response);
			}
			
			// Close the socket connection with the client.
			_clientConnection.close();
		} catch (IOException e) {
			sessionEnded = true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); 
		} 
	}

	public static void main(String[] args) {
		final Server server = new Server();

		Thread serviceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					server.start();
				} catch (IOException e) {
					System.out.println("Error starting server");
				}
			}
		});
		serviceThread.start();

		Console c = System.console();
		c.readLine("Press enter to stop the server ");

		server.shutdown();
		System.out.println("Server shutting down");

		try {
			serviceThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
