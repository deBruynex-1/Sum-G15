package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class FileIndexController {
    private final FileIndexService fileIndexService;

    @Autowired
    public FileIndexController(FileIndexService fileIndexService) {
        this.fileIndexService = fileIndexService;
    }
@PostMapping("/addFileIndex")
//public void addFileIndex(@RequestParam(value = "folderPath",required = false) String folderPath) {
public void addFileIndex(@RequestBody FileIndexVo fileIndexVo) {
    if (fileIndexVo.getFolderPath() == null) {
       System.out.print("21311321312");
    } else {
        List<String> filePaths = new ArrayList<>();
        System.out.print(fileIndexVo.getFolderPath()+"\n");
        exploreDirectory(new File(fileIndexVo.getFolderPath()), filePaths);
        System.out.print(filePaths);
        fileIndexService.addFilesToIndex(filePaths);
    }
}

    private void exploreDirectory(File folder, List<String> filePaths) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        exploreDirectory(file, filePaths);
                    } else {
                        filePaths.add(file.getAbsolutePath());
                    }
                }
            }
        }
    }

}