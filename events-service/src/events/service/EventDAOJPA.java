package events.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;


import events.domain.Event;

@Repository
public class EventDAOJPA implements EventDAO {
	private static final Logger logger = Logger.getLogger(EventDAOJPA.class);
	
	@PersistenceContext
    private EntityManager em;

	public Event findById(Long id) {
		return em.find(Event.class, id);
	}
	
	public void create(Event event) {
		em.persist(event);
	}

	@SuppressWarnings("unchecked")
	public List<Event> getAllEvents() {
		logger.debug("getAllEvents");
		Query q = em.createQuery("SELECT e FROM Event e ORDER BY e.id DESC");
		return (List<Event>)q.getResultList();
	}

	public void update(Event event) {
		em.merge(event);
	}
	
	public void remove(Long id) {
		Event e = findById(id);
		em.remove(e);
	}

}
