package com.project.glib.service;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.model.AudioVideo;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AudioVideoService implements DocumentServiceInterface<AudioVideo> {
    public static final String TYPE = Document.AV;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    private final AudioVideoDaoImplementation avDao;
    private final DocumentPhysicalService docPhysService;

    public AudioVideoService(AudioVideoDaoImplementation avDao, DocumentPhysicalService docPhysService) {
        this.avDao = avDao;
        this.docPhysService = docPhysService;
    }

    @Override
    public void add(AudioVideo audioVideo, String shelf) throws Exception {
        if (shelf.equals("")) throw new Exception(SHELF_EXCEPTION);
        add(audioVideo);
        for (int i = 0; i < audioVideo.getCount(); i++) {
            // TODO add keywords options
            docPhysService.add(new DocumentPhysical(shelf, true, audioVideo.getId(), Document.AV, null));
        }
    }

    private void add(AudioVideo audioVideo) throws Exception {
        checkValidParameters(audioVideo);
        try {
            AudioVideo existedAV = isAlreadyExist(audioVideo);
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
    public void update(AudioVideo audioVideo) throws Exception {
        checkValidParameters(audioVideo);
        // TODO solve case then librarian change count of AVs
        try {
            AudioVideo existedAV = isAlreadyExist(audioVideo);
            if (existedAV != null && !existedAV.equals(audioVideo)) {
                existedAV.setCount(existedAV.getCount() + audioVideo.getCount());
                existedAV.setPrice(audioVideo.getPrice());
                audioVideo = existedAV;
            }
            avDao.update(audioVideo);
        } catch (Exception e) {
            throw new Exception(UPDATE_EXCEPTION);
        }
    }

    @Override
    public void remove(long audioVideoId) throws Exception {
        try {
            docPhysService.removeAllByDocIdAndDocType(audioVideoId, Document.AV);
            avDao.remove(audioVideoId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION);
        }
    }

    @Override
    public void removeCopy(long avId, long copyId) throws Exception {
        docPhysService.remove(copyId);
        decrementCountById(avId);
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

    @Override
    public AudioVideo isAlreadyExist(AudioVideo audioVideo) {
        try {
            return avDao.isAlreadyExist(audioVideo);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public AudioVideo getById(long audioVideoId) {
        return avDao.getById(audioVideoId);
    }

    @Override
    public int getCountById(long audioVideoId) throws Exception {
        try {
            return getById(audioVideoId).getCount();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    @Override
    public void decrementCountById(long avId) {
        AudioVideo audioVideo = getById(avId);
        audioVideo.setCount(audioVideo.getCount() - 1);
        avDao.update(audioVideo);
    }

    @Override
    public void incrementCountById(long avId) {
        AudioVideo audioVideo = getById(avId);
        audioVideo.setCount(audioVideo.getCount() + 1);
        avDao.update(audioVideo);
    }

    @Override
    public int getPriceById(long avId) throws Exception {
        try {
            return getById(avId).getPrice();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AudioVideo> getList() {
        try {
            return avDao.getList();
        } catch (NoSuchElementException | NullPointerException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public long getId(AudioVideo audioVideo) throws Exception {
        try {
            return avDao.getId(audioVideo);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }
}
