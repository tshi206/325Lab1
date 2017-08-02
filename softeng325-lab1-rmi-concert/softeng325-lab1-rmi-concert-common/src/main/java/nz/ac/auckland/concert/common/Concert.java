package nz.ac.auckland.concert.common;

import java.rmi.Remote;

import org.joda.time.DateTime;

public interface Concert extends Remote{
	
	public void setDate(DateTime date);
	
	public Long getId();
	
	public String getTitle();
	
	public DateTime getDate();

	
	@Override
	public boolean equals(Object other);
	
	@Override 
	public int hashCode();
}
