package events.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import events.domain.Event;
import events.service.EventService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;


public class EventServiceEjbBean implements javax.ejb.SessionBean, EventService {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(EventServiceEjbBean.class);
	private EventService eventService;

	public EventServiceEjbBean() {
	}
	
	// ==== EventService interface ====
	
	public Event findById(Long id) {
		logger.debug("findById(EJB)");
		return eventService.findById(id);
	}

	public void createEvent(Event event) {
		logger.debug("createEvent(EJB)");
		eventService.createEvent(event);
	}

	public void updateEvent(Event event) {
		logger.debug("createEvent(EJB)");
		eventService.updateEvent(event);
	}

	@SuppressWarnings("unchecked")
	public List findAllEvents() {
		logger.debug("findAllEvents(EJB)");
		return eventService.findAllEvents();
	}
	
	public void remove(Long id) {
		logger.debug("remove(EJB)");
		eventService.remove(id);
	}

	public String getGreeting(String name) {
		return "hello, "+name;
	}

	// ==== EJB lifecycle interface ====
	
	public void ejbCreate() {
	}
	
	public void ejbActivate() throws EJBException, RemoteException {
	}

	public void ejbPassivate() throws EJBException, RemoteException {
	}

	public void ejbRemove() throws EJBException, RemoteException {
	}

	public void setSessionContext(SessionContext context) throws EJBException,
			RemoteException {
		logger.debug("setSessionContext");
		BeanFactoryLocator fl =
			ContextSingletonBeanFactoryLocator.getInstance();
		BeanFactory beanFactory = fl.useBeanFactory("beanFactory").getFactory();
		eventService = (EventService) beanFactory.getBean("eventService");
	}

}
