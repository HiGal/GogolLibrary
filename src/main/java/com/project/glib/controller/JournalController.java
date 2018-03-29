//package com.project.glib.controller;
//
//import com.project.glib.model.Journal;
//import com.project.glib.service.JournalService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class JournalController {
//    private final JournalService journalService;
//
//    @Autowired
//    public JournalController(JournalService journalService) {
//        this.journalService = journalService;
//    }
//
//    @RequestMapping(value = "/librarian/add/Journal")
//    public String addJournal(@RequestBody Journal journal,
//                             @RequestParam(value = "shelf") String shelf) {
//        try {
//            journalService.add(journal, shelf);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "";
//    }
//
//}
//
