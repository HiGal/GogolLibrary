package com.project.glib.service;

import com.project.glib.dao.implementations.MessageDaoImplementation;
import com.project.glib.dao.interfaces.MessagesRepository;
import com.project.glib.model.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sun.misc.resources.Messages_es;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    public static final String RETURN_DOCUMENT = "Please, return next document(s) to the library: ";
    public static final String CHECKOUT_DOCUMENT = "Please, visit a library and checkout a document:  ";
    public static final String DELETED_QUEUE = "Sorry, but you were deleted from the queue for the next document: ";
    public static final String LATE_DELETED = "Sorry, but you are late to checkout document: ";
    private static final long DAY_IN_MILLISECONDS = 86400000000000L;
    private final MessageDaoImplementation messageDao;
    private final MessagesRepository messagesRepository;

    @Autowired
    MessageService(MessageDaoImplementation messageDao, MessagesRepository messagesRepository) {
        this.messageDao = messageDao;

        this.messagesRepository = messagesRepository;
    }

    @Scheduled(fixedDelay = DAY_IN_MILLISECONDS)
    private void deleteReadMes() throws Exception {
        List<Messages> list = messageDao.getList();
        for (int i = 0; i < list.size(); i++) {
            Messages mes = list.get(i);
            if (!(mes.getMessage().equals(RETURN_DOCUMENT) ||
                    mes.getMessage().equals(CHECKOUT_DOCUMENT))) {
                if (mes.getIsRead()) {
                    messageDao.remove(mes.getId());
                }
            }
        }
    }

    public void addMes(long id_user, long id_doc, String type, String message) throws Exception {
        Messages messages = new Messages(id_user, message, id_doc, type, false);
        try {
            if (!alreadyHasThisMessage(id_user, id_doc, message)) {
                messageDao.add(messages);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean alreadyHasThisMessage(long id_user, long id_doc, String message) {
        Messages mes = messagesRepository.findAll().stream()
                .filter(messages -> messages.getId_user() == id_doc)
                .filter(messages -> messages.getId_doc() == id_user)
                .findAny().get();
        if (mes != null) {
            return mes.getMessage().equals(message);
        } else {
            return false;
        }
    }

    public List<Messages> getAllByUserID(long userId) {
        return messagesRepository.findAll().stream()
                .filter(messages -> messages.getId_user() == userId)
                .collect(Collectors.toList());
    }

    public void removeAllByUserID(long userId) throws Exception {
        List<Messages> list = messagesRepository.findAll().stream()
                .filter(messages -> messages.getId_user() == userId)
                .collect(Collectors.toList());
        try {
            for (Messages aList : list) {
                messageDao.remove(aList.getId());
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void removeOneByUserID(long userId, long doc_id, String mes) throws Exception {
        List<Messages> list = messagesRepository.findAll().stream()
                .filter(messages -> messages.getId_user() == userId)
                .filter(messages -> messages.getId_doc() == doc_id)
                .filter(messages -> messages.getMessage().equals(mes))
                .collect(Collectors.toList());
        if (list != null) {
            if (list.get(0) != null) {
                try {
                    for (int i = 0; i < list.size(); i++) {
                        messageDao.remove(list.get(i).getId());
                    }

                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }
            } else {
                throw new Exception("There is no messages for user " + userId + " about document " + doc_id);
            }
        } else {
            throw new Exception("There is no messages for user " + userId + " about document " + doc_id);
        }
    }
}
