package nz.ac.auckland.concert.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.ac.auckland.concert.common.Concert;
import nz.ac.auckland.concert.common.ConcertFactory;

public class ConcertFactoryServant implements ConcertFactory{

	private Map<Long, Concert> _concerts;
	private Long numberOfConcerts = (long) 0;
	
	
	public ConcertFactoryServant() throws RemoteException {
		super();
		_concerts = new HashMap<Long, Concert>();
	}
	
	
	@Override
	public Concert createConcert(Concert concert) throws RemoteException {
		Concert newConcert = new ConcertServant(numberOfConcerts, concert.getTitle(), concert.getDate());
		_concerts.put(numberOfConcerts, newConcert);
		numberOfConcerts++;
		return newConcert;
	}

	@Override
	public Concert getConcert(Long id) throws RemoteException {
		Long uid = id;
		Concert concert = _concerts.get(uid);
		if (concert == null) {
			throw new RemoteException();
		}
		return concert;
	}

	@Override
	public boolean updateConcert(Concert concert) throws RemoteException {
		
		if (!_concerts.containsKey(concert.getId())) {
			return false;
		} else {
			_concerts.put(concert.getId(), concert);
			return true;
		}
		
	}

	@Override
	public boolean deleteConcert(Long id) throws RemoteException {
		Long uid = id;

		Concert concert = _concerts.get(uid);
		if (concert == null) {
			return false;
		} else {
			_concerts.remove(uid);
			return true;
		}
		
	}

	@Override
	public List<Concert> getAllConcerts() throws RemoteException {
		List<Concert> concertList = new ArrayList<Concert>(
				_concerts.values());
		return concertList;
	}

	@Override
	public void clear() throws RemoteException {
		_concerts.clear();
	}

}
