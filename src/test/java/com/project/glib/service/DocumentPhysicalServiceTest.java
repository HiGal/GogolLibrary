package com.project.glib.service;

import com.project.glib.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.project.glib.service.DocumentPhysicalService.EXIST_EXCEPTION;
import static com.project.glib.service.ModifyByLibrarianService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentPhysicalServiceTest {
    @Autowired
    private DocumentPhysicalService docPhysService;
    @Autowired
    private BookService bookService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private AudioVideoService avService;
    private DocumentPhysical docPhys1, docPhys2, docPhys3;
    private Book book;
    private Journal journal;
    private AudioVideo av;

    @Before
    public void setup() throws Exception {
        String shelf = "SHELF";
        book = new Book("title", "author", "publisher",
                "edition", 2017, Document.DEFAULT_NOTE, 100, 0, "picture", null);
        journal = new Journal("title", "author", "name", 2,
                "editor", Document.REFERENCE, 200, 0, "picture", null);
        av = new AudioVideo("title", "author", 400, 0, "picture", null);

        bookService.add(book, shelf);
        journalService.add(journal, shelf);
        avService.add(av, shelf);

        docPhys1 = new DocumentPhysical(shelf, true, book.getId(), Document.BOOK);
        docPhys2 = new DocumentPhysical(shelf, true, journal.getId(), Document.JOURNAL);
        docPhys3 = new DocumentPhysical(shelf, true, av.getId(), Document.AV);
    }

    @After
    public void teamDown() throws Exception {
        bookService.remove(book.getId());
        journalService.remove(journal.getId());
        avService.remove(av.getId());

        try {
            docPhysService.remove(docPhys1.getId());
        } catch (Exception ignore) {
        }

        try {
            docPhysService.remove(docPhys2.getId());
        } catch (Exception ignore) {
        }

        try {
            docPhysService.remove(docPhys3.getId());
        } catch (Exception ignore) {
        }
    }

    @Test
    public void add() throws Exception {
        add(docPhys1);
        add(docPhys2);
        add(docPhys3);
    }

    private void add(DocumentPhysical docPhys) throws Exception {
        int countBefore = docPhysService.getCount(docPhys);
        docPhysService.add(docPhys);
        int countAfter = docPhysService.getCount(docPhys);

        assertEquals(docPhys, docPhysService.getById(docPhys.getId()));
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    public void update() throws Exception {
        update(docPhys1);
        update(docPhys2);
        update(docPhys3);
    }

    private void update(DocumentPhysical docPhys) throws Exception {
        docPhysService.add(docPhys);
        // do smth with docPhys
        docPhysService.update(docPhys);
        assertEquals(docPhys, docPhysService.getById(docPhys.getId()));
    }

    @Test
    public void removeSuccessful() throws Exception {
        removeSuccessful(docPhys1);
        removeSuccessful(docPhys2);
        removeSuccessful(docPhys3);
    }

    private void removeSuccessful(DocumentPhysical docPhys) throws Exception {
        docPhysService.add(docPhys);
        int countBefore = docPhysService.getCount(docPhys);
        docPhysService.remove(docPhys.getId());
        int countAfter = docPhysService.getCount(docPhys);

        assertNull(docPhysService.getById(docPhys.getId()));
        assertEquals(countBefore - 1, countAfter);
    }

    @Test
    public void removeException() {
        removeException(docPhys1);
        removeException(docPhys2);
        removeException(docPhys3);
    }

    private void removeException(DocumentPhysical docPhys) {
        try {
            docPhysService.remove(docPhys.getId());
        } catch (Exception e) {
            assertEquals(DocumentPhysicalService.REMOVE_EXCEPTION + docPhys.getId(), e.getMessage());
        }
    }

    @Test
    public void getIdReturnId() throws Exception {
        getIdReturnId(docPhys1);
        getIdReturnId(docPhys2);
        getIdReturnId(docPhys3);
    }

    private void getIdReturnId(DocumentPhysical docPhys) throws Exception {
        docPhysService.add(docPhys);
        assertEquals(docPhys.getId(), docPhysService.getId(docPhys));
    }

    @Test
    public void getIdReturnException() {
        getIdReturnException(docPhys1);
        getIdReturnException(docPhys2);
        getIdReturnException(docPhys3);
    }

    private void getIdReturnException(DocumentPhysical docPhys) {
        try {
            docPhysService.getId(docPhys);
        } catch (Exception e) {
            assertEquals(EXIST_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void removeByDocIdAndDocTypeSuccessful() throws Exception {
        removeByDocIdAndDocTypeSuccessful(docPhys1);
        removeByDocIdAndDocTypeSuccessful(docPhys2);
        removeByDocIdAndDocTypeSuccessful(docPhys3);
    }

    private void removeByDocIdAndDocTypeSuccessful(DocumentPhysical docPhys) throws Exception {
        docPhysService.add(new DocumentPhysical("shelf1", true, docPhys.getDocVirId(),
                docPhys.getDocType()));
        docPhysService.add(new DocumentPhysical("shelf2", true, docPhys.getDocVirId(),
                docPhys.getDocType()));
        docPhysService.add(new DocumentPhysical("shelf3", true, docPhys.getDocVirId(),
                docPhys.getDocType()));


        docPhysService.removeAllByDocVirIdAndDocType(docPhys.getDocVirId(), docPhys.getDocType());

        try {
            docPhysService.getValidPhysId(docPhys.getDocVirId(), docPhys.getDocType());
        } catch (Exception e) {
            assertEquals(EXIST_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void removeByDocIdAndDocTypeReturnException() {
        removeByDocIdAndDocTypeReturnException(docPhys1);
        removeByDocIdAndDocTypeReturnException(docPhys2);
        removeByDocIdAndDocTypeReturnException(docPhys3);
    }

    private void removeByDocIdAndDocTypeReturnException(DocumentPhysical docPhys) {
        try {
            docPhysService.removeAllByDocVirIdAndDocType(docPhys.getDocVirId(), docPhys.getDocType());
        } catch (Exception e) {
            assertEquals(EXIST_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void getValidPhysIdSuccessful() throws Exception {
        getValidPhysIdSuccessful(docPhys1);
        getValidPhysIdSuccessful(docPhys2);
        getValidPhysIdSuccessful(docPhys3);
    }

    private void getValidPhysIdSuccessful(DocumentPhysical docPhys) throws Exception {
        docPhys.setCanBooked(true);
        docPhysService.add(docPhys);

        long validId = docPhysService.getValidPhysId(docPhys.getDocVirId(), docPhys.getDocType());
        assertEquals(docPhys.getId(), validId);
    }

    @Test
    public void getValidPhysIdReturnException() throws Exception {
        getValidPhysIdReturnException(docPhys1);
        getValidPhysIdReturnException(docPhys2);
        getValidPhysIdReturnException(docPhys3);
    }

    private void getValidPhysIdReturnException(DocumentPhysical docPhys) throws Exception {
        docPhys.setCanBooked(false);
        docPhysService.add(docPhys);

        try {
            docPhysService.getValidPhysId(docPhys.getDocVirId(), docPhys.getDocType());
        } catch (Exception e) {
            assertEquals(e.getMessage(), EXIST_EXCEPTION);
        }
    }

    @Test
    public void checkValidParameters() {
        checkValidParameters(docPhys1);
        checkValidParameters(docPhys2);
        checkValidParameters(docPhys3);
    }

    private void checkValidParameters(DocumentPhysical docPhys) {
        checkValidParameters1(docPhys);
        checkValidParameters2(docPhys);
        checkValidParameters3(docPhys);
    }

    private void checkValidParameters1(DocumentPhysical docPhys) {
        long docVirId = docPhys.getDocVirId();
        try {
            docPhys.setDocVirId(0);
            docPhysService.checkValidParameters(docPhys);
        } catch (Exception e) {
            docPhys.setDocVirId(docVirId);
            assertEquals(ID_EXCEPTION, e.getMessage());
        }
    }

    private void checkValidParameters2(DocumentPhysical docPhys) {
        String docType = docPhys.getDocType();
        try {
            docPhys.setDocType("NOT_TYPE");
            docPhysService.checkValidParameters(docPhys);
        } catch (Exception e) {
            docPhys.setDocType(docType);
            assertEquals(TYPE_EXCEPTION, e.getMessage());
        }
    }

    private void checkValidParameters3(DocumentPhysical docPhys) {
        String shelf = docPhys.getShelf();
        try {
            docPhys.setShelf("");
            docPhysService.checkValidParameters(docPhys);
        } catch (Exception e) {
            docPhys.setShelf(shelf);
            assertEquals(SHELF_EXCEPTION, e.getMessage());
        }
    }
}