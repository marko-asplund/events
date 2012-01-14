package events.test;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.jpa.AbstractJpaTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import events.domain.Event;
import events.service.EventService;

/*
 * Integration tests for EventService methods.
 * 
 * NB: add events-service/test/setup dir to classpath when running this class as a JUnit test under Eclipse.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml", "classpath:applicationContext-test.xml"})
@TransactionConfiguration
@Transactional
public class EventServiceTest extends AbstractJpaTests {
	@Autowired
	private EventService eventService;
	
	TransactionStatus st = null;
	
	@Override
	@Autowired
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Autowired
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		super.setTransactionManager(transactionManager);
	}
	
	@BeforeTransaction
	public void verifyPreConditions() {
	}
	
	@Test
	@NotTransactional
	public void createEvent() {
		// Spring TestContext setComplete/endTransaction/startNewTransaction didn't seem to work in 2.5.5.
		// work around this with programmatic transaction demarcation.
		st = transactionManager.getTransaction(new DefaultTransactionDefinition());
		Event e = createDummyEvent();
		eventService.createEvent(e);
		transactionManager.commit(st);
		assertNotNull(e.getId());

		// verify that the event exists
		st = transactionManager.getTransaction(new DefaultTransactionDefinition());
		Long id = jdbcTemplate.queryForLong("SELECT id FROM event WHERE id = ?", new Long[] { e.getId() });
		assertEquals(e.getId(), id);

		// clean up
		jdbcTemplate.execute("DELETE FROM event WHERE id = "+id);
		transactionManager.commit(st);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@NotTransactional
	public void removeEvent() {
		// Spring TestContext setComplete/endTransaction/startNewTransaction didn't seem to work in 2.5.5.
		// work around this with programmatic transaction demarcation.
		st = transactionManager.getTransaction(new DefaultTransactionDefinition());
		Set<Long> ids1 = new HashSet<Long>(jdbcTemplate.queryForList("SELECT id FROM event", Long.class));
		Event e = createDummyEvent();
		SimpleJdbcInsert i = new SimpleJdbcInsert(jdbcTemplate).withTableName("event")
			.usingColumns("title", "category", "description", "start", "duration", "version");
		MapSqlParameterSource ps = new MapSqlParameterSource("title", e.getTitle())
			.addValue("category", e.getCategory()).addValue("description", e.getDescription())
			.addValue("start", e.getStartTime()).addValue("duration", e.getDuration())
			.addValue("version", e.getVersion());
		i.execute(ps);
		transactionManager.commit(st);

		st = transactionManager.getTransaction(new DefaultTransactionDefinition());
		Set<Long> ids2 = new HashSet<Long>(jdbcTemplate.queryForList("SELECT id FROM event", Long.class));
		ids2.removeAll(ids1);
		Long id = ids2.iterator().next();
		assertTrue(id != null);

		eventService.remove(id);
		transactionManager.commit(st);
		
		List<Long> ids = jdbcTemplate.queryForList("SELECT id FROM event WHERE id = ?", new Long[] { id });
		assertTrue(ids.size() == 0);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void findAllEvents() {
		Set<Long> ids1 = new HashSet<Long>(jdbcTemplate.queryForList("SELECT id FROM event", Long.class));
		List<Event> events = eventService.findAllEvents();
		Set<Long> ids2 = new HashSet<Long>(ids1.size());
		for(Event e : events)
			ids2.add(e.getId());
		assertTrue(ids1.equals(ids2));		
	}

	private Event createDummyEvent() {
		String title = "title-" + new Random().nextLong();
		Event e = new Event();
		e.setTitle(title);
		e.setCategory("category");
		e.setDescription("description");
		e.setStartTime(new Date());
		e.setDuration(5);
		return e;
	}
	
	@AfterTransaction
	public void verifyPostConditions() {
	}

}
