package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileIndexService {
    private final FileIndexRepository fileIndexRepository;

    @Autowired
    public FileIndexService(FileIndexRepository fileIndexRepository) {
        this.fileIndexRepository = fileIndexRepository;
    }

    public void addFilesToIndex(List<String> folderPaths) {
        for (String folderPath : folderPaths) {
            File folder = new File(folderPath);
            String fileName = folder.getName();
            String filePath = folder.getAbsolutePath();
            // 根据文件类型进行筛选，可根据需求进行修改
            if (isSupportedFileType(fileName)) {
                FileIndex fileIndex = new FileIndex();
                fileIndex.setFileName(fileName);
                fileIndex.setFilePath(filePath);
                System.out.print(fileName+"\n"+filePath);
                fileIndexRepository.save(fileIndex);
            }

        }
    }
    private boolean isSupportedFileType(String fileName) {
        // 根据文件名判断是否为支持的文件类型，可根据需求进行修改
        return fileName.endsWith(".txt") || fileName.endsWith(".pdf") || fileName.endsWith(".doc") || fileName.endsWith(".docx");
    }



}
