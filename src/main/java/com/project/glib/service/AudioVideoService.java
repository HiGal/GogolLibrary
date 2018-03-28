package com.project.glib.service;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.dao.implementations.DocumentPhysicalDaoImplementation;
import com.project.glib.model.AudioVideo;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import org.springframework.stereotype.Service;

@Service
public class AudioVideoService implements DocumentServiceInterface<AudioVideo> {
    public static final String TYPE = Document.AV;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    private final AudioVideoDaoImplementation avDao;
    private final DocumentPhysicalDaoImplementation docPhysDao;

    public AudioVideoService(AudioVideoDaoImplementation avDao, DocumentPhysicalDaoImplementation docPhysDao) {
        this.avDao = avDao;
        this.docPhysDao = docPhysDao;
    }

    @Override
    public void add(AudioVideo audioVideo) throws Exception {
        checkValidParameters(audioVideo);
        try {
            AudioVideo existedAV = avDao.isAlreadyExist(audioVideo);
            if (existedAV == null) {
                avDao.add(audioVideo);
            } else {
                existedAV.setCount(existedAV.getCount() + audioVideo.getCount());
                existedAV.setPrice(audioVideo.getPrice());
                avDao.update(existedAV);
            }
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION);
        }
    }

    @Override
    public void add(AudioVideo audioVideo, String shelf) throws Exception {
        if (shelf.equals("")) throw new Exception(SHELF_EXCEPTION);
        add(audioVideo);
        for (int i = 0; i < audioVideo.getCount(); i++) {
            // TODO add keywords options
            docPhysDao.add(new DocumentPhysical(shelf, true, audioVideo.getId(), Document.AV, null));
        }
    }

    @Override
    public void update(AudioVideo audioVideo) throws Exception {
        checkValidParameters(audioVideo);
        // TODO solve case then librarian change count of AVs
        try {
            avDao.update(audioVideo);
        } catch (Exception e) {
            throw new Exception(UPDATE_EXCEPTION);
        }
    }

    @Override
    public void remove(long audioVideoId) throws Exception {
        try {
            docPhysDao.removeAllByDocId(audioVideoId);
            avDao.remove(audioVideoId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION);
        }
    }

    @Override
    public void checkValidParameters(AudioVideo audioVideo) throws Exception {
        if (audioVideo.getPrice() <= 0) {
            throw new Exception(PRICE_EXCEPTION);
        }

        if (audioVideo.getCount() < 0) {
            throw new Exception(COUNT_EXCEPTION);
        }

        if (audioVideo.getTitle().equals("")) {
            throw new Exception(TITLE_EXCEPTION);
        }

        if (audioVideo.getAuthor().equals("")) {
            throw new Exception(AUTHOR_EXCEPTION);
        }
    }

    @Override
    public boolean isNote(String note) {
        return false;
    }
}
