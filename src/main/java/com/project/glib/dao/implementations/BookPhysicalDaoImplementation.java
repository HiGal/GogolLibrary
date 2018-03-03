package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.BookPhysicalRepository;
import com.project.glib.model.BookPhysical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookPhysicalDaoImplementation {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookPhysicalDaoImplementation.class);
    private final BookPhysicalRepository bookPhysicalRepository;

    @Autowired
    public BookPhysicalDaoImplementation(BookPhysicalRepository bookPhysicalRepository) {
        this.bookPhysicalRepository = bookPhysicalRepository;
    }


    public void add(BookPhysical bookPhysical) {
        bookPhysicalRepository.save(bookPhysical);
        logger.info("Book successfully saved. Book details : " + bookPhysical);
    }

    public void update(BookPhysical bookPhysical) {
        bookPhysicalRepository.save(bookPhysical);
        logger.info("Book successfully update. Book details : " + bookPhysical);
    }

    public void remove(long bookPhysicalId) {
        bookPhysicalRepository.delete(bookPhysicalId);
    }


    public BookPhysical getById(long bookPhysicalId) {
        return bookPhysicalRepository.findOne(bookPhysicalId);
    }


    @SuppressWarnings("unchecked")
    public List<BookPhysical> getList() {
        List<BookPhysical> bookPhysical = bookPhysicalRepository.findAll();

        for (BookPhysical bookPhysicals : bookPhysical) {
            logger.info("Book list : " + bookPhysicals);
        }

        return bookPhysical;
    }
}
