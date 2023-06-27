package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

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

//            System.out.print(folderPath+"\n");
//            if (folder.isDirectory()) {
//                System.out.print("\n"+"9999999999");
//                exploreDirectoryADD(folder);
//            }
//            else{
//                System.out.println("无文件通过");
//            }
        }
    }


//    private void exploreDirectoryADD(File directory) {
//
//        File[] files = directory.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    exploreDirectoryADD(file);
//                } else {
//                    String fileName = file.getName();
//                    String filePath = file.getAbsolutePath();
//
//                    // 根据文件类型进行筛选，可根据需求进行修改
//                    if (isSupportedFileType(fileName)) {
//                        FileIndex fileIndex = new FileIndex();
//                        fileIndex.setFileName(fileName);
//                        fileIndex.setFilePath(filePath);
//                        System.out.print(fileName+"\n"+filePath);
//                        fileIndexRepository.save(fileIndex);
//                    }
//                }
//            }
//        }
//    }

    private boolean isSupportedFileType(String fileName) {
        // 根据文件名判断是否为支持的文件类型，可根据需求进行修改
        return fileName.endsWith(".txt") || fileName.endsWith(".pdf") || fileName.endsWith(".doc") || fileName.endsWith(".docx");
    }
}
