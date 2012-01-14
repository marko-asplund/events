package events.ejb;

import java.rmi.RemoteException;
import java.util.List;

import events.domain.Event;


public abstract interface EventServiceEjb extends javax.ejb.EJBObject {
	void createEvent(Event event) throws RemoteException;
	void updateEvent(Event event) throws RemoteException;
	@SuppressWarnings("unchecked")
	List findAllEvents() throws RemoteException;
	String getGreeting(String name) throws RemoteException;
}
