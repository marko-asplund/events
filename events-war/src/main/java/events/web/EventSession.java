package events.web;

import java.util.List;

import events.domain.Event;


public class EventSession {
	private String currentOperation;
	private Event currentEvent;
	private List<Event> events;

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(Event currentEvent) {
		this.currentEvent = currentEvent;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public String getCurrentOperation() {
		return currentOperation;
	}

	public void setCurrentOperation(String currentOperation) {
		this.currentOperation = currentOperation;
	}

}
