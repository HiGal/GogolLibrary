//package com.project.glib.dao.implementations;
//
//import com.project.glib.model.*;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class DocumentPhysicalDaoImplementationTest {
//    @Autowired
//    private DocumentPhysicalDaoImplementation docPhysDao;
//    @Autowired
//    private BookDaoImplementation bookDao;
//    @Autowired
//    private JournalDaoImplementation journalDao;
//    @Autowired
//    private AudioVideoDaoImplementation avDao;
//    private DocumentPhysical docPhys1, docPhys2, docPhys3;
//    private Book book;
//    private Journal journal;
//    private AudioVideo av;
//
//    @Before
//    public void setup() throws Exception {
//        String shelf = "SHELF";
//        book = new Book("title", "author", "publisher",
//                "edition", 2017, Document.DEFAULT_NOTE, 100, 0);
//        journal = new Journal("title", "author", "name", 2,
//                "editor", Document.REFERENCE, 200, 0);
//        av = new AudioVideo("title", "author", 400, 0);
//
//        bookDao.add(book, shelf);
//        journalDao.add(journal, shelf);
//        avDao.add(av, shelf);
//
//        docPhys1 = new DocumentPhysical(shelf, true, book.getId(), Document.BOOK, null);
//        docPhys2 = new DocumentPhysical(shelf, true, journal.getId(), Document.JOURNAL, null);
//        docPhys3 = new DocumentPhysical(shelf, true, av.getId(), Document.AV, null);
//    }
//
//    @After
//    public void teamDown() throws Exception {
//        bookDao.remove(book.getId());
//        journalDao.remove(journal.getId());
//        avDao.remove(av.getId());
//
//        try {
//            docPhysDao.remove(docPhys1.getId());
//        } catch (Exception ignore) {
//        }
//
//        try {
//            docPhysDao.remove(docPhys2.getId());
//        } catch (Exception ignore) {
//        }
//
//        try {
//            docPhysDao.remove(docPhys3.getId());
//        } catch (Exception ignore) {
//        }
//    }
//
//    @Test
//    public void add() throws Exception {
//        add(docPhys1);
//        add(docPhys2);
//        add(docPhys3);
//    }
//
//    private void add(DocumentPhysical docPhys) throws Exception {
//        int countBefore = docPhysDao.getCount(docPhys);
//        docPhysDao.add(docPhys);
//        int countAfter = docPhysDao.getCount(docPhys);
//
//        assertEquals(docPhysDao.getById(docPhys.getId()), docPhys);
//        assertTrue(countAfter == countBefore + 1);
//    }
//
//    @Test
//    public void update() throws Exception {
//        update(docPhys1);
//        update(docPhys2);
//        update(docPhys3);
//    }
//
//    private void update(DocumentPhysical docPhys) throws Exception {
//        docPhysDao.add(docPhys);
//        // do smth with docPhys
//        docPhysDao.update(docPhys);
//        assertEquals(docPhysDao.getById(docPhys.getId()), docPhys);
//    }
//
//    @Test
//    public void removeSuccessful() throws Exception {
//        removeSuccessful(docPhys1);
//        removeSuccessful(docPhys2);
//        removeSuccessful(docPhys3);
//    }
//
//    private void removeSuccessful(DocumentPhysical docPhys) throws Exception {
//        docPhysDao.add(docPhys);
//        int countBefore = docPhysDao.getCount(docPhys);
//        docPhysDao.remove(docPhys.getId());
//        int countAfter = docPhysDao.getCount(docPhys);
//
//        assertNull(docPhysDao.getById(docPhys.getId()));
//        assertTrue(countAfter == countBefore - 1);
//    }
//
//    @Test(expected = Exception.class)
//    public void removeException1() throws Exception {
//        docPhysDao.remove(docPhys1.getId());
//    }
//
//    @Test(expected = Exception.class)
//    public void removeException2() throws Exception {
//        docPhysDao.remove(docPhys2.getId());
//    }
//
//    @Test(expected = Exception.class)
//    public void removeException3() throws Exception {
//        docPhysDao.remove(docPhys3.getId());
//    }
//
//}