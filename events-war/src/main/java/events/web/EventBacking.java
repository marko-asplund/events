package events.web;

import java.util.Iterator;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import events.domain.Event;
import events.service.EventService;

import org.apache.log4j.Logger;


public class EventBacking {
	private static final String OUTCOME_EVENTS_LIST = "events-list";
	private static final String OUTCOME_EVENT_EDIT = "event-edit";
	private Logger logger = Logger.getLogger(EventBacking.class);
	private EventService eventService;
	private EventSession eventSession;
	private EventService eventServiceEjb;
	
	public EventBacking() {
	}
	
	public List<Event> getEvents() {
		logger.debug("getEvents");
		logger.debug("ejb: "+eventServiceEjb);
		List<Event> r = eventSession.getEvents();
		if(r==null)
			reloadEvents();
		return eventSession.getEvents();
	}

	private void reloadEvents() {
		List<Event> r = eventService.findAllEvents();
		eventSession.setEvents(r);
	}
	
	public String edit() {
		logger.debug("edit");
		String idString = (String)getExternalContext().getRequestParameterMap().get("eventId");
		int id = new Integer(idString).intValue();
		Event event = getEvent(id);
		eventSession.setCurrentEvent(event);
		eventSession.setCurrentOperation("edit");
		return OUTCOME_EVENT_EDIT;
	}
	
	private Event getEvent(int id) {
		List<Event> events = getEvents();
		Event event = null;
		for(Iterator<Event> i = events.iterator(); i.hasNext(); ) {
			Event e = (Event)i.next();
			if(e.getId().intValue() == id) {
				event = e;
				break;
			}
		}
		return event;
	}
	
	public String create() {
		logger.debug("create");
		eventSession.setCurrentEvent(new Event());
		eventSession.setCurrentOperation("add");
		return OUTCOME_EVENT_EDIT;
	}
	
	public String save() {
		logger.debug("save");
		Event event = eventSession.getCurrentEvent();
		if(event.getId() == null) {
			eventServiceEjb.createEvent(event);
			logger.debug("event created");
		} else
			eventService.updateEvent(event);
		reloadEvents();
		return OUTCOME_EVENTS_LIST;
	}
	
	public String cancel() {
		logger.debug("cancel");
		return OUTCOME_EVENTS_LIST;
	}
	
	public String getMessage() {
		return "hello";
	}
	
	public void setMessage(String m) {
	}
	
	private ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	public EventSession getEventSession() {
		return eventSession;
	}

	public void setEventSession(EventSession eventSession) {
		this.eventSession = eventSession;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public void setEventServiceEjb(EventService eventServiceEjb) {
		this.eventServiceEjb = eventServiceEjb;
	}

}
