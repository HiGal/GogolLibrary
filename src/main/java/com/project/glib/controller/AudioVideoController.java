package com.project.glib.controller;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.dao.implementations.DocumentPhysicalDaoImplementation;
import com.project.glib.dao.interfaces.AudioVideoRepository;
import com.project.glib.model.AudioVideo;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AudioVideoController {

    private final AudioVideoRepository audioVideoRepository;
    private final DocumentPhysicalDaoImplementation docPhysDao;
    private final AudioVideoDaoImplementation audioVideoDao;

    @Autowired
    public AudioVideoController(AudioVideoRepository audioVideoRepository, DocumentPhysicalDaoImplementation docPhysDao, AudioVideoDaoImplementation audioVideoDao) {
        this.audioVideoRepository = audioVideoRepository;
        this.docPhysDao = docPhysDao;
        this.audioVideoDao = audioVideoDao;
    }

    @RequestMapping(value = "/librarian/add/AV")
    public String addAV(@RequestBody AudioVideo audioVideo,
                        @RequestParam(value = "shelf") String shelf,
                        @RequestParam(value = "isReference") boolean flag) {
        try {
            audioVideoDao.add(audioVideo);
            for (int i = 0; i < audioVideo.getCount(); i++) {
                // TODO add keywords options
                docPhysDao.add(
                        new DocumentPhysical(shelf, true, flag, audioVideo.getId(), Document.AV, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
