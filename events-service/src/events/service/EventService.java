package events.service;

import java.util.List;

import events.domain.Event;


public interface EventService {
	Event findById(Long id);
	void createEvent(Event event);
	void updateEvent(Event event);
	List<Event> findAllEvents();
	void remove(Long id);
}