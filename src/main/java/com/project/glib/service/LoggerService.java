package com.project.glib.service;

import com.project.glib.dao.implementations.LoggerDaoImplementation;
import com.project.glib.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
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

    public void addLog(long id_user, long doc_phys_id, String action, long date, String type, boolean is_action) throws Exception {
        User user = userService.getById(id_user);
        String act = user.getSurname() + " " + user.getName() + " ( " + user.getRole() + " ) " + " " + action + " ";

        if (is_action) {
            long id = documentPhysicalService.getDocVirIdById(doc_phys_id);
            switch (type) {
                case Document.BOOK:
                    Book book = bookService.getById(id);
                    act += " \" " + book.getTitle() + "\"  by " + book.getAuthor();
                    break;
                case Document.JOURNAL:
                    Journal journal = journalService.getById(id);
                    act += " \" " + journal.getTitle() + "\" by " + journal.getAuthor();
                    break;
                case Document.AV:
                    AudioVideo audioVideo = audioVideoService.getById(id);
                    act += " \" " + audioVideo.getTitle() + "\" by " + audioVideo.getAuthor();
                    break;
            }
        }

        Logger logger = new Logger(act, date);
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

    public List<String> showLoggerForaWeekString() {
        List<Logger> loggerList = showLoggerForaWeek();
        ArrayList<String> stringList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (Logger aLoggerList : loggerList) {
            calendar.setTimeInMillis(aLoggerList.getDate());
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH) % 12 + 1;
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            stringList.add(mDay + "." + mMonth + "." + mYear + " " + aLoggerList.getAction());
        }
        return stringList;
    }
}
