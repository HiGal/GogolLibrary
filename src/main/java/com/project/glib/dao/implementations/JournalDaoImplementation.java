package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.dao.interfaces.JournalRepository;
import com.project.glib.model.Journal;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JournalDaoImplementation implements DocumentDao<Journal> {
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
    public int getCountById(long journalId) {
        return journalRepository.findOne(journalId).getCount();
    }

    @Override
    public void decrementCountById(long journalId) {
        journalRepository.findOne(journalId).setCount(journalRepository.findOne(journalId).getCount() - 1);
    }

    @Override
    public int getPriceById(long journalId) {
        return journalRepository.findOne(journalId).getPrice();
    }

    @Override
    public List<Journal> getListCountNotZero() {
        List<Journal> journals = journalRepository.findAll().stream().filter(journal -> journal.getCount() > 0).collect(Collectors.toList());

        for (Journal journal : journals) {
            logger.info("Journal list : " + journal);
        }

        return journals;
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
