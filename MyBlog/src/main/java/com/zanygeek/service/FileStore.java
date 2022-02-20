package com.zanygeek.service;

import com.zanygeek.entity.Thumbnail;
import com.zanygeek.entity.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, int blogContentId) throws IOException {
        List<UploadFile> files = new ArrayList<>();
        for (MultipartFile uploadFile : multipartFiles) {
            if (!uploadFile.isEmpty()) {
                files.add(storeFile(uploadFile, blogContentId));
            }
        }
        return files;
    }

    public Thumbnail storeThumbnail(MultipartFile multipartFile, int blogContentId) throws IOException {
        if (multipartFile.isEmpty())
            return null;
        String uploadFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(uploadFileName);

        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        System.out.println(getFullPath(storeFileName));

        return new Thumbnail(uploadFileName, storeFileName, blogContentId);
    }

    public UploadFile storeFile(MultipartFile multipartFile, int blogContentId) throws IOException {
        if (multipartFile.isEmpty())
            return null;
        String uploadFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(uploadFileName);

        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(uploadFileName, storeFileName, blogContentId);
    }

    private String createStoreFileName(String uploadFileName) {
        String uuid = UUID.randomUUID().toString();

        return uuid + extracted(uploadFileName);
    }

    private String extracted(String uploadFileName) {
        int pos = Objects.requireNonNull(uploadFileName).lastIndexOf('.');
        return uploadFileName.substring(pos);
    }
}
