package com.project.glib.dao.implementations;

import com.project.glib.model.AudioVideo;
import com.project.glib.model.Document;
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
public class AudioVideoDaoImplementationTest {
    @Autowired
    private AudioVideoDaoImplementation avDao;
    @Autowired
    private DocumentPhysicalDaoImplementation docPhysDao;
    private AudioVideo av, avInDao;
    private String shelf1, shelf2;
    private int count1, count2;

    @Before
    public void setup() {
        av = new AudioVideo("title", "author", 400, 2);
        shelf1 = "Shelf1";
        shelf2 = "Shelf2";
        count1 = 2;
        count2 = 4;
    }

    @After
    public void tearDown() {
        try {
            avDao.remove(av.getId());
        } catch (Exception ignore) {
        }
    }

    @Test
    public void isAlreadyExistReturnNull() {
        avInDao = avDao.isAlreadyExist(av);

        assertNull(avInDao);
    }

    @Test
    public void isAlreadyExistReturnBook() throws Exception {
        avDao.add(av, shelf1);
        avInDao = avDao.isAlreadyExist(av);

        assertNotNull(avInDao);
        assertEquals(avInDao, av);
    }

    @Test
    public void addNotExistedBook() throws Exception {
        avDao.add(av, shelf1);
        avInDao = avDao.isAlreadyExist(av);

        assertEquals(avInDao, av);
        assertTrue(docPhysDao.getCount(avInDao.getId(), Document.AV) == avInDao.getCount());
    }

    @Test
    public void addExistedBook() throws Exception {
        av.setCount(count1);
        avDao.add(av, shelf1);
        av.setCount(count2);
        avDao.add(av, shelf2);
        avInDao = avDao.isAlreadyExist(av);

        assertTrue(avInDao.getCount() == count1 + count2);
        assertTrue(docPhysDao.getCount(avInDao.getId(), Document.AV) == avInDao.getCount());
    }

    @Test(expected = Exception.class)
    public void addNullShelf() throws Exception {
        avDao.add(av, "");
    }

    @Test
    public void update() throws Exception {
        // don't change count when using update
        avDao.add(av, shelf1);

        av.setTitle("title2");
        av.setAuthor("author2");
        av.setPrice(200);

        avDao.update(av);
        avInDao = avDao.isAlreadyExist(av);

        assertEquals(avInDao, av);
        assertTrue(docPhysDao.getCount(avInDao.getId(), Document.AV) == avInDao.getCount());

    }

    @Test
    public void remove() throws Exception {
        avDao.add(av, shelf1);
        avDao.remove(av.getId());

        avInDao = avDao.isAlreadyExist(av);
        assertNull(avInDao);
        assertTrue(docPhysDao.getCount(av.getId(), Document.AV) == 0);
    }

    @Test
    public void getIdReturnId() throws Exception {
        avDao.add(av, shelf1);
        avInDao = avDao.isAlreadyExist(av);

        assertTrue(avDao.getId(av) == av.getId());
    }

    @Test(expected = Exception.class)
    public void getIdReturnException() throws Exception {
        avDao.getId(av);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException1() throws Exception {
        av.setTitle("");
        avDao.checkValidParameters(av);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException2() throws Exception {
        av.setAuthor("");
        avDao.checkValidParameters(av);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException3() throws Exception {
        av.setPrice(-1);
        avDao.checkValidParameters(av);
    }

    @Test(expected = Exception.class)
    public void checkValidParametersReturnException4() throws Exception {
        av.setCount(-10);
        avDao.checkValidParameters(av);
    }
}