package com.project.glib.dao;

import com.project.glib.model.AudioVideo;
import com.project.glib.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AudioVideoDaoImplementation implements DocumentDaoInterface<AudioVideo>{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final AudioVideoRepository audioVideoRepository;

    @Autowired
    public AudioVideoDaoImplementation(AudioVideoRepository audioVideoRepository) {
        this.audioVideoRepository = audioVideoRepository;
    }

    @Override
    public void add(AudioVideo audioVideo) {
        audioVideoRepository.save(audioVideo);
        logger.info("AudioVideo successfully saved. AudioVideo details : " + audioVideo);
    }

    @Override
    public void update(AudioVideo audioVideo) {
        audioVideoRepository.save(audioVideo);
        logger.info("AudioVideo successfully update. AudioVideo details : " + audioVideo);
    }

    @Override
    public void remove(long audioVideoId) {
        audioVideoRepository.delete(audioVideoId);
    }

    @Override
    public AudioVideo getById(long audioVideoId) {
        return audioVideoRepository.findOne(audioVideoId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AudioVideo> getList() {
        List<AudioVideo> audioVideos = audioVideoRepository.findAll();

        for (AudioVideo audioVideo : audioVideos){
            logger.info("AudioVideo list : " + audioVideo);
        }

        return audioVideos;
    }
}
