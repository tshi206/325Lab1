package nz.ac.auckland.concert.common;

import java.io.Serializable;
import java.rmi.RemoteException;


import org.joda.time.DateTime;

public class Sender implements Serializable, Concert{

	private static final long serialVersionUID = 1L;
	
	private String _title;
	private DateTime _date;
	
	public Sender(String title, DateTime date) throws RemoteException{
		_title = title;
		_date = date;
	}
	
	
	public void setDate(DateTime date) {
	}
	
	public Long getId() {
		return null;
	}
	
	public String getTitle() {
		return _title;
	}
	
	public DateTime getDate() {
		return _date;
	}

	
	@Override
	public boolean equals(Object other) {
		return false;
	}
	
	@Override 
	public int hashCode() {
		return 1;
	}
}
