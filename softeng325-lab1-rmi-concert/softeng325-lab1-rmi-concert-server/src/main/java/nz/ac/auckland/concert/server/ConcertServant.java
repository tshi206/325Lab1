package nz.ac.auckland.concert.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import nz.ac.auckland.concert.common.Concert;



public class ConcertServant extends UnicastRemoteObject implements Concert{

	private static final long serialVersionUID = 1L;
	
	private Long _id;
	private String _title;
	private DateTime _date;
	
	protected ConcertServant(Long id, String title, DateTime date) throws RemoteException{
		_id = id;
		_title = title;
		_date = date;
	}
	
	public ConcertServant(String title, DateTime date) throws RemoteException{
		this(null, title, date);
	}
	
	public void setDate(DateTime date) {
		_date = date;
	}
	
	public Long getId() {
		return _id;
	}
	
	public String getTitle() {
		return _title;
	}
	
	public DateTime getDate() {
		return _date;
	}

	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Concert))
            return false;
        if (other == this)
            return true;

        Concert rhs = (Concert) other;
        return new EqualsBuilder().
        	append(_id, rhs.getId()).
            append(_title, rhs.getTitle()).
           
            isEquals();
	}
	
	@Override 
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
				append(getClass().getName()).
	            append(_id).
	            append(_title).
	            toHashCode();
	}
}
