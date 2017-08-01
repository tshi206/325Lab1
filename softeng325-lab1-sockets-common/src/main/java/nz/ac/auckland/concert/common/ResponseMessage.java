package nz.ac.auckland.concert.common;

import java.io.Serializable;
import java.util.List;

/**
 * Class to represent a response message, sent from the Server to a Client.
 * 
 * Similarly to RequestMessage, this class has a set of factory methods to
 * create ResponseMessages.
 *
 */
public class ResponseMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	// Message type.
	public enum Status {Success, Failure}
	
	// Field values.
	private Status _status;
	private String _failureMessage;
	private Long _id;
	private Concert _concert;
	private List<Concert> _concerts;
	
	private ResponseMessage() {	
	}
	
	public static ResponseMessage makeSuccessfulResponse() {
		ResponseMessage replyMessage = new ResponseMessage();
		replyMessage._status = Status.Success;
		
		return replyMessage;
	}
	
	public static ResponseMessage makeSuccessfulCreateResponse(Concert concert) {
		ResponseMessage replyMessage = new ResponseMessage();
		replyMessage._status = Status.Success;
		replyMessage._id = concert.getId();
		replyMessage._concert = concert;
		
		return replyMessage;
	}
	
	public static ResponseMessage makeSuccessfulRetrieveResponse(Concert concert) {
		ResponseMessage replyMessage = new ResponseMessage();
		replyMessage._status = Status.Success;
		replyMessage._concert = concert;
		
		return replyMessage;
	}
	
	public static ResponseMessage makeUnsuccessfulRetrieveResponse() {
		ResponseMessage replyMessage = new ResponseMessage();
		replyMessage._status = Status.Failure;
		replyMessage._failureMessage = "Concert with specified id not found";
		
		return replyMessage;
	}
	
	public static ResponseMessage makeUnsuccessfulUpdateResponse() {
		ResponseMessage replyMessage = new ResponseMessage();
		replyMessage._status = Status.Failure;
		replyMessage._failureMessage = "Concert with specified id not found";
		
		return replyMessage;
	}
	
	public static ResponseMessage makeUnsuccessfulDeleteResponse() {
		ResponseMessage replyMessage = new ResponseMessage();
		replyMessage._status = Status.Failure;
		replyMessage._failureMessage = "Concert with specified id not found";
		
		return replyMessage;
	}
	
	public static ResponseMessage makeListResponse(List<Concert> concerts) {
		ResponseMessage replyMessage = new ResponseMessage();
		replyMessage._status = Status.Success;
		replyMessage._concerts = concerts;
		
		return replyMessage;
	}
	
	public static ResponseMessage makeProtocolErrorResponse() {
		ResponseMessage replyMessage = new ResponseMessage();
		replyMessage._status = Status.Failure;
		replyMessage._failureMessage = "Unexpected message received";
		
		return replyMessage;
	}
	
	public Status getStatus() {
		return _status;
	}
	
	public Long getId() {
		return _id;
	}
	
	public Concert getConcert() {
		return _concert;
	}
	
	public List<Concert> getConcerts() {
		return _concerts;
	}
	
	public String getFailureMessage() {
		return _failureMessage;
	}
}
