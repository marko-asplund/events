package events.service;

import java.util.List;

import org.apache.log4j.Logger;

import events.domain.Event;

public class EventServiceImpl implements EventService {
	private static final Logger logger = Logger.getLogger(EventServiceImpl.class);
	private EventDAO eventDAO;
	
	public Event findById(Long id) {
		logger.debug("findById");
		return eventDAO.findById(id);
	}

	public void createEvent(Event event) {
		logger.debug("createEvent");
		eventDAO.create(event);
	}

	public void updateEvent(Event event) {
		logger.debug("updateEvent");
		eventDAO.update(event);
	}
	
	public List<Event> findAllEvents() {
		logger.debug("findAllEvents");
		return eventDAO.getAllEvents();
	}

	public void remove(Long id) {
		logger.debug("remove");
		eventDAO.remove(id);
	}

	public void setEventDAO(EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}


}
