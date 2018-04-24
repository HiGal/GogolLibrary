package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.KeywordRepository;
import com.project.glib.model.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KeywordDaoImplementation {
    private static final org.slf4j.Logger log = (org.slf4j.Logger) LoggerFactory.getLogger(KeywordDaoImplementation.class);
    private final KeywordRepository keywordRepository;

    public void add(com.project.glib.model.Logger logger) throws Exception {
        try {
            loggerRepository.save(logger);
            log.info("Log successfully saved. Log details : " + logger);
        } catch (Exception e) {
            log.info("Try to add log with wrong parameters. New log information : " + logger);
            throw new Exception("Can't add this log, some parameters are wrong");
        }
    }

    public void update(com.project.glib.model.Logger logge) throws Exception {
        try {
            loggerRepository.saveAndFlush(logge);
            log.info("Log successfully update. Log details : " + logge);
        } catch (Exception e) {
            log.info("Try to update this log, log don't exist or some new log parameters are wrong. " +
                    "Update log information : " + logge);
            throw new Exception("Can't update this log, log don't exist or some new log parameters are wrong");
        }
    }

    public void remove(long id) throws Exception {
        try {
            log.info("Try to delete log with log id = " + id);
            loggerRepository.delete(id);
        } catch (Exception e) {
            log.info("Try to delete log with wrong log id = " + id);
            throw new Exception("Delete this log not available, log don't exist");
        }
    }

    public Object getById(long id) {
        return loggerRepository.findOne(id);
    }


    public long getId(com.project.glib.model.Logger logger) {
        return logger.getId();
    }


    public List<Logger> getList() {
        return loggerRepository.findAll();
    }
}
