package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.DocumentDaoInterface;
import com.project.glib.dao.interfaces.JournalRepository;
import com.project.glib.model.Journal;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JournalDaoImplementation implements DocumentDaoInterface<Journal> {
    private static final org.slf4j.Logger logger = (org.slf4j.Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final JournalRepository journalRepository;

    @Autowired
    public JournalDaoImplementation(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Override
    public void add(Journal journal) {
        journalRepository.save(journal);
        logger.info("Journal successfully saved. Journal details : " + journal);
    }

    @Override
    public void update(Journal journal) {
        journalRepository.save(journal);
        logger.info("Journal successfully update. Journal details : " + journal);
    }

    @Override
    public void remove(long journalId) {
        journalRepository.delete(journalId);
    }

    @Override
    public Journal getById(long journalId) {
        return journalRepository.findOne(journalId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Journal> getList() {
        List<Journal> journals = journalRepository.findAll();

        for (Journal journal : journals) {
            logger.info("Journal list : " + journal);
        }

        return journals;
    }
}
