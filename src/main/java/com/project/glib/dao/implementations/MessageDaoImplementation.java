package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.MessagesRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class MessageDaoImplementation implements ModifyByLibrarian<Messages> {
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessageDaoImplementation(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Override
    public void add(Messages message) {
        messagesRepository.save(message);
    }

    @Override
    public void update(Messages message) {
        messagesRepository.saveAndFlush(message);
    }

    @Override
    public void remove(long messageID) {
        messagesRepository.delete(messageID);
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
        return messagesRepository.findAll();
    }
}
