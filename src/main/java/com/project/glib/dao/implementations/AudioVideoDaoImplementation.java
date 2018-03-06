package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.AudioVideoRepository;
import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.model.AudioVideo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AudioVideoDaoImplementation implements DocumentDao<AudioVideo> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AudioVideoDaoImplementation.class);
    private final AudioVideoRepository audioVideoRepository;

    @Autowired
    public AudioVideoDaoImplementation(AudioVideoRepository audioVideoRepository) {
        this.audioVideoRepository = audioVideoRepository;
    }

    /**
     * Add new item of AudioVideo in library
     *
     * @param audioVideo new AudioVideo
     * @throws Exception
     */
    @Override
    public void add(AudioVideo audioVideo) throws Exception {
        try {
            audioVideoRepository.save(audioVideo);
            logger.info("AudioVideo successfully saved. AudioVideo details : " + audioVideo);
        } catch (Exception e) {
            logger.info("Try to add AV with wrong parameters. New AV information : " + audioVideo);
            throw new Exception("Can't add this AudioVideo, some parameters are wrong");
        }
    }

    /**
     * Update existed AudioVideo or create if it not exist
     *
     * @param audioVideo - updated AudioVideo
     * @throws Exception
     */
    @Override
    public void update(AudioVideo audioVideo) throws Exception {
        try {
            audioVideoRepository.saveAndFlush(audioVideo);
            logger.info("AudioVideo successfully update. AudioVideo details : " + audioVideo);
        } catch (Exception e) {
            logger.info("Try to update this AV, AV don't exist or some new AV parameters are wrong. " +
                    "Update AV information : " + audioVideo);
            throw new Exception("Can't update this AV, AV don't exist or some new AV parameters are wrong");
        }
    }

    /**
     * Remove AudioVideo from library
     * @param audioVideoId id of AudioVideo
     * @throws Exception
     */
    @Override
    public void remove(long audioVideoId) throws Exception {
        try {
            logger.info("Try to delete AV with AV id = " + audioVideoId);
            audioVideoRepository.delete(audioVideoId);
        } catch (Exception e) {
            logger.info("Try to delete AV with wrong AV id = " + audioVideoId);
            throw new Exception("Delete this AV not available, AV don't exist");
        }
    }

    /**
     * get AudioVideo by it id
     * @param audioVideoId id of AudioVideo
     * @return AudioVideo object
     * @throws Exception
     */
    @Override
    public AudioVideo getById(long audioVideoId) throws Exception {
        try {
            logger.info("Try to get count of AV with AV id = " + audioVideoId);
            return audioVideoRepository.findOne(audioVideoId);
        } catch (Exception e) {
            logger.info("Try to get count of AV with wrong AV id = " + audioVideoId);
            throw new Exception("Information not available, AV don't exist");
        }
    }

    /**
     * get how many copies of AudioVideo we already have in library
     * @param audioVideoId id of AudioVideo
     * @return count of copies
     * @throws Exception
     */
    @Override
    public int getCountById(long audioVideoId) throws Exception {
        try {
            logger.info("Try to get count of AV with AV id = " + audioVideoId);
            return audioVideoRepository.findOne(audioVideoId).getCount();
        } catch (Exception e) {
            logger.info("Try to get count of AV with wrong AV id = " + audioVideoId);
            throw new Exception("Information not available, AV don't exist");
        }
    }

    /**
     * set count to count-1 for AudioVideo
     * @param avId id of AudioVideo
     * @throws Exception
     */
    @Override
    public void decrementCountById(long avId) throws Exception {
        try {
            logger.info("Try to decrement count of AV with AV id = " + avId);
            audioVideoRepository.findOne(avId).setCount(audioVideoRepository.findOne(avId).getCount() - 1);
        } catch (Exception e) {
            logger.info("Try to decrement count of AV with wrong AV id = " + avId);
            throw new Exception("Information not available, AV don't exist");
        }
    }

    /**
     * get price of AudioVideo by id
     * @param avId id of AudioVideo
     * @return price of book
     * @throws Exception
     */
    @Override
    public int getPriceById(long avId) throws Exception {
        try {
            logger.info("Try to get price of AV with AV id = " + avId);
            return audioVideoRepository.findOne(avId).getPrice();
        } catch (Exception e) {
            logger.info("Try to get price of AV with wrong AV id = " + avId);
            throw new Exception("Information not available, AV don't exist");
        }
    }

    /**
     * get list of all AudioVideo
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<AudioVideo> getList() {
        List<AudioVideo> audioVideos = audioVideoRepository.findAll();

        for (AudioVideo audioVideo : audioVideos) {
            logger.info("AudioVideo list : " + audioVideo);
        }

        return audioVideos;
    }

    /**
     * get list of all AudioVideo with count bigger than zero or renewed
     * @return list of AudioVideo with count bigger than zero or renewed
     */
    @Override
    public List<AudioVideo> getListCountNotZeroOrRenewed() {
        List<AudioVideo> audioVideos = audioVideoRepository.findAll().stream().filter(audioVideo -> audioVideo.getCount() > 0).collect(Collectors.toList());

        for (AudioVideo audioVideo : audioVideos) {
            logger.info("AudioVideo list : " + audioVideo);
        }

        return audioVideos;
    }
}
