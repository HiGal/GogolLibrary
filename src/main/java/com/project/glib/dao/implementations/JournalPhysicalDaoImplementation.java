//package com.project.glib.dao.implementations;
//
//import com.project.glib.dao.interfaces.BookPhysicalRepository;
//import com.project.glib.dao.interfaces.DocumentPhysicalDao;
//import com.project.glib.dao.interfaces.JournalPhysicalRepository;
//import com.project.glib.model.BookPhysical;
//import com.project.glib.model.JournalPhysical;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class JournalPhysicalDaoImplementation implements DocumentPhysicalDao<JournalPhysical> {
//    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookPhysicalDaoImplementation.class);
//    private final JournalPhysicalRepository journalPhysicalRepository;
//
//    @Autowired
//    public JournalPhysicalDaoImplementation(JournalPhysicalRepository journalPhysicalRepository) {
//        this.journalPhysicalRepository = journalPhysicalRepository;
//    }
//
//    @Override
//    public void add(JournalPhysical journalPhysical) {
//        journalPhysicalRepository.save(journalPhysical);
//        logger.info("Journal successfully saved. Journal details : " + journalPhysical);
//    }
//
//    @Override
//    public void update(JournalPhysical journalPhysical) {
//        journalPhysicalRepository.save(journalPhysical);
//        logger.info("Journal successfully update. Journal details : " + journalPhysical);
//    }
//
//    @Override
//    public void remove(long journalPhysicalId) {
//        journalPhysicalRepository.delete(journalPhysicalId);
//    }
//
//    @Override
//    public JournalPhysical getById(long journalPhysicalId) {
//        return journalPhysicalRepository.findOne(journalPhysicalId);
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public List<JournalPhysical> getList() {
//        List<JournalPhysical> journalPhysicals = journalPhysicalRepository.findAll();
//
//        for (JournalPhysical journalPhysical : journalPhysicals) {
//            logger.info("Journal list : " + journalPhysical);
//        }
//
//        return journalPhysicals;
//    }
//
//    @Override
//    public long getValidPhysicalId(long journalId) {
//        return journalPhysicalRepository.findAll().stream()
//                .filter(journal -> journal.getId_journal() == journalId)
//                .filter(JournalPhysical::isCan_booked)
//                .filter(journal -> !journal.isIs_reference()).findFirst().get().getId();
//    }
//
//    @Override
//    public void inverseCanBooked(long journalId) {
//        journalPhysicalRepository.findOne(journalId).setCan_booked(
//                !journalPhysicalRepository.findOne(journalId).isCan_booked());
//    }
//
//    @Override
//    public String getShelfById(long journalPhysicalId) {
//        return journalPhysicalRepository.findOne(journalPhysicalId).getShelf();
//    }
//}
//
