package com.project.glib.service;

import com.project.glib.dao.implementations.LoggerDaoImplementation;
import com.project.glib.model.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoggerService implements LoggerActions {
    private final LoggerDaoImplementation loggerDao;

    @Autowired
    public LoggerService(LoggerDaoImplementation loggerDao) {
        this.loggerDao = loggerDao;
    }

    public void add(Logger logger) throws Exception {
        loggerDao.add(logger);
    }

    public void addLog(long id_user, long virt_doc_id, long phys_doc_id, String action, long date) throws Exception {
        Logger logger = new Logger(id_user, virt_doc_id, phys_doc_id, action, date);
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
}
