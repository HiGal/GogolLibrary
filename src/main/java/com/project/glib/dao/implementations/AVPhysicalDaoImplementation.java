//package com.project.glib.dao.implementations;
//
//import com.project.glib.dao.interfaces.AVPhysicalRepository;
//import com.project.glib.dao.interfaces.DocumentPhysicalDao;
//import com.project.glib.model.AVPhysical;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//public class AVPhysicalDaoImplementation implements DocumentPhysicalDao<AVPhysical> {
//    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookPhysicalDaoImplementation.class);
//    private final AVPhysicalRepository avPhysicalRepository;
//
//    @Autowired
//    public AVPhysicalDaoImplementation(AVPhysicalRepository avPhysicalRepository) {
//        this.avPhysicalRepository = avPhysicalRepository;
//    }
//
//    @Override
//    public void add(AVPhysical avPhysical) {
//        avPhysicalRepository.save(avPhysical);
//        logger.info("AV successfully saved. AV details : " + avPhysical);
//    }
//
//    @Override
//    public void update(AVPhysical avPhysical) {
//        avPhysicalRepository.save(avPhysical);
//        logger.info("AV successfully update. AV details : " + avPhysical);
//    }
//
//    @Override
//    public void remove(long avPhysicalId) {
//        avPhysicalRepository.delete(avPhysicalId);
//    }
//
//    @Override
//    public AVPhysical getById(long avPhysicalId) {
//        return avPhysicalRepository.findOne(avPhysicalId);
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public List<AVPhysical> getList() {
//        List<AVPhysical> avPhysical = avPhysicalRepository.findAll();
//
//        for (AVPhysical avPhysicals : avPhysical) {
//            logger.info("AV list : " + avPhysicals);
//        }
//
//        return avPhysical;
//    }
//
//    @Override
//    public long getValidPhysicalId(long avId) {
//        return avPhysicalRepository.findAll().stream()
//                .filter(av -> av.getId_av() == avId)
//                .filter(AVPhysical::isCan_booked)
//                .filter(av -> !av.isIs_reference()).findFirst().get().getId();
//    }
//
//    @Override
//    public void inverseCanBooked(long avId) {
//        avPhysicalRepository.findOne(avId).setCan_booked(
//                !avPhysicalRepository.findOne(avId).isCan_booked());
//    }
//
//    @Override
//    public String getShelfById(long avPhysicalId) {
//        return avPhysicalRepository.findOne(avPhysicalId).getShelf();
//    }
//}
