package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Image;
import com.vubq.joyboystore.enums.EImageType;
import com.vubq.joyboystore.repositories.ImageRepository;
import com.vubq.joyboystore.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image save(Image image) {
        return this.imageRepository.save(image);
    }

    @Override
    public List<Image> saveAll(List<Image> images) {
        return this.imageRepository.saveAll(images);
    }

    @Override
    public void deleteAll(List<Image> images) {
        this.imageRepository.deleteAll(images);
    }

    @Override
    public List<Image> getAllBySecondaryId(String secondaryId) {
        return this.imageRepository.findAllBySecondaryId(secondaryId);
    }

    @Override
    public List<Image> findAllBySecondaryIdAndType(String secondaryId, EImageType type) {
        return this.imageRepository.findAllBySecondaryIdAndType(secondaryId, type);
    }

    @Override
    public List<String> getAllUrlBySecondaryId(String secondaryId) {
        return this.imageRepository.getAllUrlBySecondaryId(secondaryId);
    }
}
