package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.AudioVideoRepository;
import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.AudioVideo;
import com.project.glib.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AudioVideoDaoImplementation implements DocumentDao<AudioVideo> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AudioVideoDaoImplementation.class);
    private static final String TYPE = Document.BOOK;
    private static final String ADD_AV = TYPE + ADD;
    private static final String UPDATE_AV = TYPE + UPDATE;
    private static final String REMOVE_AV = TYPE + REMOVE;
    private static final String LIST = TYPE + ModifyByLibrarian.LIST;
    private final AudioVideoRepository audioVideoRepository;

    @Autowired
    public AudioVideoDaoImplementation(AudioVideoRepository audioVideoRepository) {
        this.audioVideoRepository = audioVideoRepository;
    }

    /**
     * Add new item of AudioVideo in library
     *
     * @param audioVideo new AudioVideo
     */
    @Override
    public void add(AudioVideo audioVideo) {
        audioVideoRepository.saveAndFlush(audioVideo);
        logger.info(ADD_AV + audioVideo);
    }

    /**
     * Update existed AudioVideo or create if it not exist
     *
     * @param audioVideo - updated AudioVideo
     */
    @Override
    public void update(AudioVideo audioVideo) {
        audioVideoRepository.saveAndFlush(audioVideo);
        logger.info(UPDATE_AV + audioVideo);
    }

    /**
     * Remove AudioVideo from library
     *
     * @param audioVideoId id of AudioVideo
     */
    @Override
    public void remove(long audioVideoId) {
        audioVideoRepository.delete(audioVideoId);
        logger.info(REMOVE_AV + audioVideoId);
    }

    /**
     * Checks existence of current AudioVideo in database
     *
     * @param audioVideo instance of AudioVideo
     * @return returns the instance such that all parameters equals to input one
     */
    @Override
    public AudioVideo isAlreadyExist(AudioVideo audioVideo) {
        return audioVideoRepository.findAll().stream()
                .filter(av -> av.getTitle().equals(audioVideo.getTitle()))
                .filter(av -> av.getAuthor().equals(audioVideo.getAuthor()))
                .findFirst().get();
    }

    /**
     * Gets an instance of AudioVideo by its ID
     *
     * @param audioVideoId ID of AudioVideo
     * @return AudioVideo instance
     */
    @Override
    public AudioVideo getById(long audioVideoId) {
        return audioVideoRepository.findOne(audioVideoId);
    }

    /**
     * Gets an ID of AudioVideo instance
     *
     * @param audioVideo instance of AV
     * @return ID of instance of AudioVideo
     */
    @Override
    public long getId(AudioVideo audioVideo) {
        return isAlreadyExist(audioVideo).getId();
    }

    /**
     * Collects all the instances of AudioVideo model from the database to the list
     *
     * @return list of AudioVideo instances
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<AudioVideo> getList() {
        List<AudioVideo> audioVideos = audioVideoRepository.findAll();

        for (AudioVideo audioVideo : audioVideos) {
            logger.info(LIST + audioVideo);
        }

        return audioVideos;
    }
}
