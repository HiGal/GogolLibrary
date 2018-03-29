//package com.project.glib.controller;
//
//import com.project.glib.model.AudioVideo;
//import com.project.glib.service.AudioVideoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class AudioVideoController {
//
//    private final AudioVideoService audioVideoService;
//
//    @Autowired
//    public AudioVideoController(AudioVideoService audioVideoService) {
//        this.audioVideoService = audioVideoService;
//    }
//
//    @RequestMapping(value = "/librarian/add/AV")
//    public String addAV(@RequestBody AudioVideo audioVideo,
//                        @RequestParam(value = "shelf") String shelf) {
//        try {
//            audioVideoService.add(audioVideo, shelf);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "";
//    }
//}
