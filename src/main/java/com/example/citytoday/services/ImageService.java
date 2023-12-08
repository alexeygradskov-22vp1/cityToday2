package com.example.citytoday.services;

import com.example.citytoday.models.Image;
import com.example.citytoday.reps.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Long upload(MultipartFile file){
        Image lastRecord=imageRepository.findTopByOrderByIdDesc();
        Long name = lastRecord==null?1:lastRecord.getId()+1;
        Image newImg= new Image();
        newImg.setPath("src/main/resources/static/images/"+name+".jpg");
        Logger logger = LoggerFactory.getLogger(ImageService.class);
        if (!file.isEmpty()) {
            logger.info("Тут");
            try {
                imageRepository.save(newImg);
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(String.valueOf("src/main/resources/static/images/"+name+".jpg"))));

                stream.write(bytes);
                stream.close();


            } catch (Exception e) {
            }
        }
        return name;
    }
    }

