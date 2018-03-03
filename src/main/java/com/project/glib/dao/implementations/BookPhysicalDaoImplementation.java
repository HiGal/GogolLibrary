//package com.project.glib.dao.implementations;
//
//import com.project.glib.dao.interfaces.BookPhysicalRepository;
//import com.project.glib.dao.interfaces.DocumentPhysicalDao;
//import com.project.glib.model.BookPhysical;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class BookPhysicalDaoImplementation implements DocumentPhysicalDao<BookPhysical> {
//    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookPhysicalDaoImplementation.class);
//    private final BookPhysicalRepository bookPhysicalRepository;
//
//    @Autowired
//    public BookPhysicalDaoImplementation(BookPhysicalRepository bookPhysicalRepository) {
//        this.bookPhysicalRepository = bookPhysicalRepository;
//    }
//
//    @Override
//    public void add(BookPhysical bookPhysical) {
//        bookPhysicalRepository.save(bookPhysical);
//        logger.info("Book successfully saved. Book details : " + bookPhysical);
//    }
//
//    @Override
//    public void update(BookPhysical bookPhysical) {
//        bookPhysicalRepository.save(bookPhysical);
//        logger.info("Book successfully update. Book details : " + bookPhysical);
//    }
//
//    @Override
//    public void remove(long bookPhysicalId) {
//        bookPhysicalRepository.delete(bookPhysicalId);
//    }
//
//    @Override
//    public BookPhysical getById(long bookPhysicalId) {
//        return bookPhysicalRepository.findOne(bookPhysicalId);
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public List<BookPhysical> getList() {
//        List<BookPhysical> bookPhysical = bookPhysicalRepository.findAll();
//
//        for (BookPhysical bookPhysicals : bookPhysical) {
//            logger.info("Book list : " + bookPhysicals);
//        }
//
//        return bookPhysical;
//    }
//
//    @Override
//    public long getValidPhysicalId(long bookId) {
//        return bookPhysicalRepository.findAll().stream()
//                .filter(book -> book.getId_book() == bookId)
//                .filter(BookPhysical::isCan_booked)
//                .filter(book -> !book.isIs_reference()).findFirst().get().getId();
//    }
//
//    @Override
//    public void inverseCanBooked(long bookId) {
//        bookPhysicalRepository.findOne(bookId).setCan_booked(
//                !bookPhysicalRepository.findOne(bookId).isCan_booked());
//    }
//
//    @Override
//    public String getShelfById(long bookPhysicalId) {
//        return bookPhysicalRepository.findOne(bookPhysicalId).getShelf();
//    }
//}
