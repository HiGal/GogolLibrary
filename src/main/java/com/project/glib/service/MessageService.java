package com.project.glib.service;

import com.project.glib.dao.implementations.MessageDaoImplementation;
import com.project.glib.dao.interfaces.MessagesRepository;
import com.project.glib.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    public static final String RETURN_DOCUMENT = "Please, return next document(s) to the library: ";
    public static final String CHECKOUT_DOCUMENT = "Please, visit a library and checkout a document:  ";
    public static final String DELETED_QUEUE = "Sorry, but you were deleted from the queue for the next document: ";
    public static final String LATE_DELETED = "Sorry, but you are late to checkout document: ";
    private final MessageDaoImplementation messageDao;
    private final MessagesRepository messagesRepository;
    private final DocumentPhysicalService documentPhysicalService;
    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService audioVideoService;
    private final UserService userService;

    @Autowired
    MessageService(MessageDaoImplementation messageDao,
                   MessagesRepository messagesRepository,
                   DocumentPhysicalService documentPhysicalService,
                   BookService bookService,
                   JournalService journalService,
                   AudioVideoService audioVideoService,
                   @Lazy UserService userService) {
        this.messageDao = messageDao;
        this.messagesRepository = messagesRepository;
        this.documentPhysicalService = documentPhysicalService;
        this.bookService = bookService;
        this.journalService = journalService;
        this.audioVideoService = audioVideoService;
        this.userService = userService;
    }

    @Scheduled(fixedDelay = BookingService.DAY_IN_MILLISECONDS)
    public void deleteReadMes() throws Exception {
        List<Messages> list = messageDao.getList();
        for (Messages mes : list) {
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
        try {
            Messages mes = messagesRepository.findAll().stream()
                    .filter(messages -> messages.getUserId() == id_doc)
                    .filter(messages -> messages.getDocPhysId() == id_user)
                    .findAny().get();
            return mes.getMessage().equals(message);
        } catch (Exception e) {
            return false;
        }
    }

    public List<Messages> getAllByUserID(long userId) {
        return messagesRepository.findAll().stream()
                .filter(messages -> messages.getUserId() == userId)
                .collect(Collectors.toList());
    }


    public void removeAllByUserID(long userId) throws Exception {
        List<Messages> list = messagesRepository.findAll().stream()
                .filter(messages -> messages.getUserId() == userId)
                .collect(Collectors.toList());
        try {
            for (Messages aList : list) {
                messageDao.remove(aList.getId());
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<String> getMessages(String login) {
        try {
            List<Messages> mes = getAllByUserID(userService.getIdByLogin(login));
            ArrayList<String> result = new ArrayList<>();
            for (int i = 0; i < mes.size(); i++) {
                result.add(mes.get(i).getMessage() + createMessage(mes.get(i).getDocPhysId()));
            }
            return result;
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }

    public void sendMessagesToLib(String login) throws Exception {
        List<Messages> messages = getAllByUserID(userService.getIdByLogin(login));
        for (Messages message1 : messages) {
            String message = "User " + login +
                    " read the message: " + message1.getMessage() +
                    createMessage(message1.getDocPhysId());
        }

    }


    public void removeOneByUserID(long userId, long doc_id, String mes) throws Exception {
        List<Messages> list = messagesRepository.findAll().stream()
                .filter(messages -> messages.getUserId() == userId)
                .filter(messages -> messages.getDocPhysId() == doc_id)
                .filter(messages -> messages.getMessage().equals(mes))
                .collect(Collectors.toList());
        if (list.size() != 0) {
            if (list.get(0) != null) {
                try {
                    for (Messages aList : list) {
                        messageDao.remove(aList.getId());
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

    private String createMessage(long idPhys) {
        DocumentPhysical documentPhysical = documentPhysicalService.getById(idPhys);
        String result = "";
        long id = documentPhysical.getDocVirId();
        String type = documentPhysical.getDocType();

        switch (type) {
            case Document.BOOK:
                Book book = bookService.getById(id);
                result = book.getTitle() + book.getAuthor();
                break;
            case Document.JOURNAL:
                Journal journal = journalService.getById(id);
                result = journal.getTitle() + journal.getAuthor();
                break;
            case Document.AV:
                AudioVideo av = audioVideoService.getById(id);
                result = av.getTitle() + av.getAuthor();
                break;
        }

        return result;
    }
}
