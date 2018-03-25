package com.project.glib.dao.implementations;

import com.project.glib.model.Document;
import com.project.glib.model.Journal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JournalDaoImplementationTest {
    @Autowired
    private JournalDaoImplementation journalDao;
    @Autowired
    private DocumentPhysicalDaoImplementation docPhysDao;
    private Journal journal, journalInDao;
    private String shelf1, shelf2;
    private int count1, count2;

    @Before
    public void setup() {
        journal = new Journal("title", "author", "name", 2,
                "editor", Document.REFERENCE, 200, 3);
        shelf1 = "Shelf1";
        shelf2 = "Shelf2";
        count1 = 2;
        count2 = 4;
    }

    @After
    public void tearDown() {
        try {
            journalDao.remove(journal.getId());
        } catch (Exception ignore) {
        }
    }

    @Test
    public void isAlreadyExistReturnNull() {
        journalInDao = journalDao.isAlreadyExist(journal);

        assertNull(journalInDao);
    }

    @Test
    public void isAlreadyExistReturnBook() throws Exception {
        journalDao.add(journal, shelf1);
        journalInDao = journalDao.isAlreadyExist(journal);

        assertNotNull(journalInDao);
        assertEquals(journalInDao, journal);
    }

    @Test
    public void addNotExistedJournal() throws Exception {
        journalDao.add(journal, shelf1);
        journalInDao = journalDao.isAlreadyExist(journal);

        assertEquals(journalInDao, journal);
        assertTrue(docPhysDao.getCount(journalInDao.getId(), Document.JOURNAL) == journalInDao.getCount());
    }

    @Test
    public void addExistedJournal() throws Exception {
        journal.setCount(count1);
        journalDao.add(journal, shelf1);
        journal.setCount(count2);
        journalDao.add(journal, shelf2);
        journalInDao = journalDao.isAlreadyExist(journal);

        assertTrue(journalInDao.getCount() == count1 + count2);
        assertTrue(docPhysDao.getCount(journalInDao.getId(), Document.JOURNAL) == journalInDao.getCount());
    }

    @Test(expected = Exception.class)
    public void addNullShelf() throws Exception {
        journalDao.add(journal, "");
    }

    @Test
    public void update() throws Exception {
        // don't change count when using update
        journalDao.add(journal, shelf1);

        journal.setTitle("title2");
        journal.setAuthor("author2");
        journal.setEditor("editor2");
        journal.setIssue(4);
        journal.setName("name2");
        journal.setNote(Document.REFERENCE);
        journal.setPrice(200);

        journalDao.update(journal);
        journalInDao = journalDao.isAlreadyExist(journal);

        assertEquals(journalInDao, journal);
        assertTrue(docPhysDao.getCount(journalInDao.getId(), Document.JOURNAL) == journalInDao.getCount());

    }

    @Test
    public void remove() throws Exception {
        journalDao.add(journal, shelf1);
        journalDao.remove(journal.getId());

        journalInDao = journalDao.isAlreadyExist(journal);
        assertNull(journalInDao);
        assertTrue(docPhysDao.getCount(journal.getId(), Document.JOURNAL) == 0);
    }

    @Test
    public void getIdReturnId() throws Exception {
        journalDao.add(journal, shelf1);
        journalInDao = journalDao.isAlreadyExist(journal);

        assertTrue(journalDao.getId(journal) == journal.getId());
    }

    @Test(expected = Exception.class)
    public void getIdReturnException() throws Exception {
        journalDao.getId(journal);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException1() throws Exception {
        journal.setTitle("");
        journalDao.checkValidParameters(journal);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException2() throws Exception {
        journal.setAuthor("");
        journalDao.checkValidParameters(journal);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException3() throws Exception {
        journal.setEditor("");
        journalDao.checkValidParameters(journal);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException4() throws Exception {
        journal.setIssue(0);
        journalDao.checkValidParameters(journal);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException5() throws Exception {
        journal.setName("");
        journalDao.checkValidParameters(journal);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException6() throws Exception {
        journal.setNote("NOT_NOTE");
        journalDao.checkValidParameters(journal);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException7() throws Exception {
        journal.setPrice(-1);
        journalDao.checkValidParameters(journal);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException9() throws Exception {
        journal.setCount(-10);
        journalDao.checkValidParameters(journal);
    }
}