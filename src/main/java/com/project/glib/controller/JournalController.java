package com.project.glib.controller;

import com.project.glib.dao.implementations.DocumentPhysicalDaoImplementation;
import com.project.glib.dao.implementations.JournalDaoImplementation;
import com.project.glib.dao.interfaces.JournalRepository;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import com.project.glib.model.Journal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JournalController {

    private final JournalRepository journalRepository;
    private final JournalDaoImplementation journalDao;
    private final DocumentPhysicalDaoImplementation docPhysDao;

    @Autowired
    public JournalController(JournalRepository journalRepository,
                             JournalDaoImplementation journalDao,
                             DocumentPhysicalDaoImplementation documentPhysicalDao) {
        this.journalRepository = journalRepository;
        this.journalDao = journalDao;
        this.docPhysDao = documentPhysicalDao;
    }

    @RequestMapping(value = "/librarian/add/Journal")
    public String addJournal(@RequestBody Journal journal,
                             @RequestParam(value = "shelf") String shelf,
                             @RequestParam(value = "isReference") boolean flag) {
        try {
            journalDao.add(journal);
            for (int i = 0; i < journal.getCount(); i++) {
                // TODO add keywords options
                docPhysDao.add(
                        new DocumentPhysical(shelf, true, flag, journal.getId(), Document.JOURNAL, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}

