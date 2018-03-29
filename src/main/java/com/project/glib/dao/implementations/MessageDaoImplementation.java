package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.MessagesRepository;
import com.project.glib.model.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MessageDaoImplementation {
    public static final String RETURN_DOCUMENT = "Please, return next document(s) to the library: ";
    public static final String CHECKOUT_DOCUMENT = "Please, visit a library and checkout a document:  ";
    private static final Logger logger = (Logger) LoggerFactory.getLogger(MessageDaoImplementation.class);
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessageDaoImplementation(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public void add(Messages message) throws Exception {
        try {
            messagesRepository.save(message);
            logger.info("Message successfully saved. Message details : " + message);
        } catch (Exception e) {
            logger.info("Try to add message with wrong parameters. New message information : " + message);
            throw new Exception("Can't add this message, some parameters are wrong");
        }
    }

    public void update(Messages message) throws Exception {
        try {
            messagesRepository.saveAndFlush(message);
            logger.info("Message successfully update. Message details : " + message);
        } catch (Exception e) {
            logger.info("Try to update this message, message don't exist or some new message parameters are wrong. " +
                    "Update message information : " + message);
            throw new Exception("Can't update this message, message don't exist or some new message parameters are wrong");
        }
    }

    public void remove(long messageID) throws Exception {
        try {
            logger.info("Try to delete message with message id = " + messageID);
            messagesRepository.delete(messageID);
        } catch (Exception e) {
            logger.info("Try to delete message with wrong message id = " + messageID);
            throw new Exception("Delete this message not available, message don't exist");
        }
    }

    public void addMes(long id_user, long id_doc, String type, String message) throws Exception {
        Messages messages = new Messages(id_user, message, id_doc, type);
        try {
            if (!alreadyHasThisMessage(id_user, id_doc, message)) {
                add(messages);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean alreadyHasThisMessage(long id_user, long id_doc, String message) {
        Messages messag = messagesRepository.findAll().stream()
                .filter(messages -> messages.getId_user() == id_doc)
                .filter(messages -> messages.getId_doc() == id_user)
                .findAny().get();
        if (messag != null) {
            return messag.getMessage().equals(message);
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
                remove(aList.getId());
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void removeOneByUserID(long userId, long doc_id) throws Exception {
        List<Messages> list = messagesRepository.findAll().stream()
                .filter(messages -> messages.getId_user() == userId)
                .filter(messages -> messages.getId_doc() == doc_id)
                .collect(Collectors.toList());
        if (list != null) {
            if (list.get(0) != null) {
                try {
                    remove(list.get(0).getId());
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
