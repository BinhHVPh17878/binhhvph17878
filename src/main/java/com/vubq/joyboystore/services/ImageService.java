package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.Image;
import com.vubq.joyboystore.enums.EImageType;

import java.util.List;

public interface ImageService {

    Image save(Image image);

    List<Image> saveAll(List<Image> images);

    void deleteAll(List<Image> images);

    List<Image> getAllBySecondaryId(String secondaryId);

    List<Image> findAllBySecondaryIdAndType(String secondaryId, EImageType type);

    List<String> getAllUrlBySecondaryId(String secondaryId);
}
