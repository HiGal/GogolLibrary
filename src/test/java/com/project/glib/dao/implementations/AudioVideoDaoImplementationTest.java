package com.project.glib.dao.implementations;

import com.project.glib.model.AudioVideo;
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
    private AudioVideo av, avInDao;

    @Before
    public void setup() {
        av = new AudioVideo("title", "author", 400, 2);
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
    public void isAlreadyExistReturnBook() {
        avDao.add(av);
        avInDao = avDao.isAlreadyExist(av);

        assertNotNull(avInDao);
        assertEquals(avInDao, av);
    }

    @Test
    public void incrementById() throws Exception {
        avDao.add(av);
        int count = av.getCount();
        avDao.incrementCountById(av.getId());

        assertTrue(avDao.getCountById(av.getId()) == count + 1);
    }

    @Test
    public void decrementById() throws Exception {
        avDao.add(av);
        int count = av.getCount();
        avDao.decrementCountById(av.getId());

        assertTrue(avDao.getCountById(av.getId()) == count - 1);
    }

    @Test
    public void getIdReturnId() throws Exception {
        avDao.add(av);
        avInDao = avDao.isAlreadyExist(av);

        assertTrue(avDao.getId(av) == av.getId());
    }

    @Test(expected = Exception.class)
    public void getIdReturnException() throws Exception {
        avDao.getId(av);
    }
}