package com.project.glib.service;

import com.project.glib.dao.implementations.LoggerDaoImplementation;
import com.project.glib.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoggerService implements LoggerActions {
    private final LoggerDaoImplementation loggerDao;
    private final UserService userService;
    private final DocumentPhysicalService documentPhysicalService;
    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService audioVideoService;

    @Autowired
    public LoggerService(LoggerDaoImplementation loggerDao, @Lazy UserService userService,
                         DocumentPhysicalService documentPhysicalService, @Lazy BookService bookService,
                         JournalService journalService, AudioVideoService audioVideoService) {
        this.loggerDao = loggerDao;
        this.userService = userService;
        this.documentPhysicalService = documentPhysicalService;
        this.bookService = bookService;
        this.journalService = journalService;
        this.audioVideoService = audioVideoService;
    }

    public void add(Logger logger) throws Exception {
        loggerDao.add(logger);
    }

    public void addLog(long id_user, long phys_doc_id, String action, long date) throws Exception {
        Logger logger = new Logger(id_user, phys_doc_id, action, date);
        loggerDao.add(logger);
    }

    public List<Logger> showLoggerForaWeek() {
        List<Logger> loggerList = loggerDao.getList();

        for (int i = 0; i < loggerList.size(); i++) {
            if (loggerList.get(i).getDate() < System.currentTimeMillis() - 7 * BookingService.DAY_IN_MILLISECONDS) {
                loggerList.remove(i);
                i -= 2;
            }
        }

        return loggerList;
    }

    public List<String> showLoggerForaWeekString() throws Exception {
        List<Logger> loggerList = showLoggerForaWeek();
        ArrayList<String> stringList = new ArrayList<>();
        for (Logger aLoggerList : loggerList) {
            String s = "";
            s += aLoggerList.getDate();

            // TODO how to check for null this part ->
            if (aLoggerList.getUserId() > 0) {
                User user = userService.getById(aLoggerList.getUserId());
                s += " " + user.getRole() + " " + user.getSurname() + " " + user.getName();
            }

            s += " " + aLoggerList.getAction();

            // TODO how to check for null this part ->
            if (aLoggerList.getPhysDocId() > 0) {
                String type = documentPhysicalService.getTypeById(aLoggerList.getPhysDocId());
                long id = documentPhysicalService.getDocVirIdById(aLoggerList.getPhysDocId());
                switch (type) {
                    case Document.BOOK:
                        Book book = bookService.getById(id);
                        s += " " + book.getTitle() + " " + book.getAuthor();
                    case Document.JOURNAL:
                        Journal journal = journalService.getById(id);
                        s += " " + journal.getTitle() + " " + journal.getAuthor();
                    case Document.AV:
                        AudioVideo audioVideo = audioVideoService.getById(id);
                        s += " " + audioVideo.getTitle() + " " + audioVideo.getAuthor();
                }
            }

            stringList.add(s);
        }
        return stringList;
    }
}
