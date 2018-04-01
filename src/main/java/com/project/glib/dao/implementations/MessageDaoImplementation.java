package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.MessagesRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class MessageDaoImplementation implements ModifyByLibrarian<Messages> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(MessageDaoImplementation.class);
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessageDaoImplementation(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Override
    public void add(Messages message) throws Exception {
        try {
            messagesRepository.save(message);
            logger.info("Message successfully saved. Message details : " + message);
        } catch (Exception e) {
            logger.info("Try to add message with wrong parameters. New message information : " + message);
            throw new Exception("Can't add this message, some parameters are wrong");
        }
    }

    @Override
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

    @Override
    public void remove(long messageID) throws Exception {
        try {
            logger.info("Try to delete message with message id = " + messageID);
            messagesRepository.delete(messageID);
        } catch (Exception e) {
            logger.info("Try to delete message with wrong message id = " + messageID);
            throw new Exception("Delete this message not available, message don't exist");
        }
    }

    @Override
    public Messages getById(long id) {
        return messagesRepository.findOne(id);
    }

    @Override
    public long getId(Messages messages) {
        return messages.getId();
    }

    @Override
    public List<Messages> getList() {
        List<Messages> mes = messagesRepository.findAll();
        return mes;
    }
}
