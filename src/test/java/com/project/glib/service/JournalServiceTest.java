package com.project.glib.service;

import com.project.glib.model.Document;
import com.project.glib.model.Journal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.project.glib.service.DocumentServiceInterface.*;
import static com.project.glib.service.JournalService.EXIST_EXCEPTION;
import static com.project.glib.service.ModifyByLibrarianService.SHELF_EXCEPTION;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JournalServiceTest {
    @Autowired
    private JournalService journalService;
    @Autowired
    private DocumentPhysicalService docPhysService;
    private Journal journal, journalInDao;
    private String shelf1, shelf2;
    private int count1, count2;

    @Before
    public void setup() {
        journal = new Journal("title", "author", "name", 2,
                "editor", Document.REFERENCE, 200, 3, "picture", null);
        shelf1 = "Shelf1";
        shelf2 = "Shelf2";
        count1 = 2;
        count2 = 4;
    }

    @After
    public void tearDown() {
        try {
            journalService.remove(journal.getId());
        } catch (Exception ignore) {
        }
    }

    @Test
    public void isAlreadyExistReturnNull() {
        journalInDao = journalService.isAlreadyExist(journal);

        assertNull(journalInDao);
    }

    @Test
    public void isAlreadyExistReturnBook() throws Exception {
        journalService.add(journal, shelf1);
        journalInDao = journalService.isAlreadyExist(journal);

        assertNotNull(journalInDao);
        assertEquals(journal, journalInDao);
    }

    @Test
    public void addNotExistedJournal() throws Exception {
        journalService.add(journal, shelf1);
        journalInDao = journalService.isAlreadyExist(journal);

        assertEquals(journal, journalInDao);
        assertEquals(docPhysService.getCount(journalInDao.getId(), Document.JOURNAL), journalInDao.getCount());
    }

    @Test
    public void addExistedJournal() throws Exception {
        journal.setCount(count1);
        journalService.add(journal, shelf1);
        journal.setCount(count2);
        journalService.add(journal, shelf2);
        journalInDao = journalService.isAlreadyExist(journal);

        assertEquals(count1 + count2, journalInDao.getCount());
        assertEquals(docPhysService.getCount(journalInDao.getId(), Document.JOURNAL), journalInDao.getCount());
    }

    @Test
    public void addNullShelf() {
        try {
            journalService.add(journal, "");
        } catch (Exception e) {
            assertEquals(SHELF_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void update() throws Exception {
        // don't change count when using update
        journalService.add(journal, shelf1);

        journal.setTitle("title2");
        journal.setAuthor("author2");
        journal.setEditor("editor2");
        journal.setIssue(4);
        journal.setName("name2");
        journal.setNote(Document.REFERENCE);
        journal.setPrice(200);

        journalService.update(journal);
        journalInDao = journalService.isAlreadyExist(journal);

        assertEquals(journal, journalInDao);
        assertEquals(docPhysService.getCount(journalInDao.getId(), Document.JOURNAL), journalInDao.getCount());

    }

    @Test
    public void remove() throws Exception {
        journalService.add(journal, shelf1);
        journalService.remove(journal.getId());

        journalInDao = journalService.isAlreadyExist(journal);
        assertNull(journalInDao);
        assertEquals(0, docPhysService.getCount(journal.getId(), Document.JOURNAL));
    }

    @Test
    public void getIdReturnId() throws Exception {
        journalService.add(journal, shelf1);
        journalInDao = journalService.isAlreadyExist(journal);

        assertEquals(journal.getId(), journalService.getId(journal));
    }

    @Test
    public void getIdReturnException() {
        try {
            journalService.getId(journal);
        } catch (Exception e) {
            assertEquals(EXIST_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void decrementCountById() throws Exception {
        journalService.add(journal, shelf1);

        int before = journalService.getCountById(journal.getId());
        journalService.decrementCountById(journal.getId());

        assertEquals(before - 1, journalService.getCountById(journal.getId()));
    }

    @Test
    public void incrementCountById() throws Exception {
        journalService.add(journal, shelf1);

        int before = journalService.getCountById(journal.getId());
        journalService.incrementCountById(journal.getId());

        assertEquals(before + 1, journalService.getCountById(journal.getId()));
    }

    @Test
    public void checkValidParametersReturnException1() {
        try {
            journal.setTitle("");
            journalService.checkValidParameters(journal);
        } catch (Exception e) {
            assertEquals(TITLE_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException2() {
        try {
            journal.setAuthor("");
            journalService.checkValidParameters(journal);
        } catch (Exception e) {
            assertEquals(AUTHOR_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException3() {
        try {
            journal.setEditor("");
            journalService.checkValidParameters(journal);
        } catch (Exception e) {
            assertEquals(EDITOR_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException4() {
        try {
            journal.setIssue(0);
            journalService.checkValidParameters(journal);
        } catch (Exception e) {
            assertEquals(ISSUE_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException5() {
        try {
            journal.setName("");
            journalService.checkValidParameters(journal);
        } catch (Exception e) {
            assertEquals(NAME_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException6() {
        try {
            journal.setNote("NOT_NOTE");
            journalService.checkValidParameters(journal);
        } catch (Exception e) {
            assertEquals(NOTE_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException7() {
        try {
            journal.setPrice(-1);
            journalService.checkValidParameters(journal);
        } catch (Exception e) {
            assertEquals(PRICE_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException9() {
        try {
            journal.setCount(-10);
            journalService.checkValidParameters(journal);
        } catch (Exception e) {
            assertEquals(COUNT_EXCEPTION, e.getMessage());
        }
    }
}