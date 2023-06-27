package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")

public class FileExplorerController {
    @GetMapping("/drives")
    public List<String> getDrives() {
        System.out.println(5555555);
        File[] drives = File.listRoots();
        List<String> driveNames = new ArrayList<>();
        for (File drive : drives) {
            driveNames.add(drive.getAbsolutePath());
        }
        return driveNames;
    }

    @GetMapping("/files")
    public List<FileNode> getFiles(@RequestParam("path") String path) {
        File dir = new File(path);
        List<FileNode> fileNodes = new ArrayList<>();
        System.out.println(66666);

        if (dir.isDirectory()) {
            exploreDirectory(dir, fileNodes, 0); // 初始深度为0
        }

        return fileNodes;
    }

    private void exploreDirectory(File directory, List<FileNode> fileNodes, int depth) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                FileNode fileNode = new FileNode();
                fileNode.setName(file.getName());
                fileNode.setPath(file.getAbsolutePath());
                fileNode.setDirectory(file.isDirectory());

                fileNodes.add(fileNode);

//                if (file.isDirectory() && depth < 1) { // 修改递归深度为2
//                    exploreDirectory(file, fileNodes, depth + 1);
//                }
            }
        }
    }

}