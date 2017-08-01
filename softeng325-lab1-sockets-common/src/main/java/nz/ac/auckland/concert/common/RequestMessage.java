package nz.ac.auckland.concert.common;

import java.io.Serializable;

/**
 * Class to represent a request message, sent from the Client to the Server.
 * 
 * This class has a set of factory methods to create RequestMessage instances 
 * of a particular type and with required field values.
 *
 */
public class RequestMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// Type of RequestMessage.
	public enum MessageType {Hello, Goodbye, Create, Retrieve, Update, Delete, List, Clear}
	
	// Fields of a RequestMessage - the type determines which fields will be filled.
	private MessageType _type;
	private Concert _concert;
	private Long _id;
	
	private RequestMessage(MessageType type, Concert concert, Long id) {
		_type = type;
		_concert = concert;
		_id = id;
	}
	
	public static RequestMessage makeHello() {
		RequestMessage request = new RequestMessage(MessageType.Hello, null, null);
		return request;
	}
	
	public static RequestMessage makeGoodbye() {
		RequestMessage request = new RequestMessage(MessageType.Goodbye, null, null);
		return request;
	}
	
	public static RequestMessage makeCreate(Concert concert) throws IllegalArgumentException {
		if(concert.getId() != null) {
			throw new IllegalArgumentException("Concert already has an id");
		}
		RequestMessage request = new RequestMessage(MessageType.Create, concert, null);
		return request;
	}
	
	public static RequestMessage makeRetrieve(Long id) throws IllegalArgumentException {
		if(id == null) {
			throw new IllegalArgumentException("id must be non null");
		}
		RequestMessage request = new RequestMessage(MessageType.Retrieve, null, id);
		return request;
	}
	
	public static RequestMessage makeUpdate(Concert concert) throws IllegalArgumentException {
		if(concert.getId() == null) {
			throw new IllegalArgumentException("Concert must have a non-null id value");
		}
		RequestMessage request = new RequestMessage(MessageType.Update, concert, null);
		return request;
	}
	
	public static RequestMessage makeDelete(Long id) throws IllegalArgumentException {
		if(id == null) {
			throw new IllegalArgumentException("id must be non null");
		}
		RequestMessage request = new RequestMessage(MessageType.Retrieve, null, id);
		return request;
	}
	
	public static RequestMessage makeList() throws IllegalArgumentException {
		RequestMessage request = new RequestMessage(MessageType.List, null, null);
		return request;
	}
	
	public static RequestMessage makeClear() throws IllegalArgumentException {
		RequestMessage request = new RequestMessage(MessageType.Clear, null, null);
		return request;
	}
	
	public MessageType getType() {
		return _type;
	}
	
	public Concert getConcert() {
		return _concert;
	}
	
	public Long getId() {
		return _id;
	}
}
