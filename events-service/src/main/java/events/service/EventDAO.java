package events.service;

import java.util.List;

import events.domain.Event;

public interface EventDAO {
	Event findById(Long id);
	void create(Event event);
	void update(Event event);
	List<Event> getAllEvents();
	void remove(Long id);
}
