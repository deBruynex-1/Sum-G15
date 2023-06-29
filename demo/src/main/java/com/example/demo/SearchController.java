package com.example.demo;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://121.196.234.38:8080")
public class SearchController {
    private final FileIndexRepository fileIndexRepository;

    @Autowired
    public SearchController(FileIndexRepository fileIndexRepository) {
        this.fileIndexRepository = fileIndexRepository;
    }

    @GetMapping("/search")
    public List<SearchResult> searchFiles(@RequestParam("keyword") String keyword) {
        List<SearchResult> searchResults = new ArrayList<>();

        // 查询数据库中的文件索引
        List<FileIndex> fileIndexes = fileIndexRepository.findAll();

        // 遍历每个文件索引
        for (FileIndex fileIndex : fileIndexes) {
            String filePath = fileIndex.getFilePath();
            System.out.println("\n"+filePath+"\n");
            if (filePath.endsWith(".txt")) {
                // 处理txt文件
                System.out.println("开始处理txt文件\n");
                searchResults.addAll(searchTxtFile(filePath, keyword));
            } else if (filePath.endsWith(".doc")) {
                // 处理doc文件
                searchResults.addAll(searchDocFile(filePath, keyword));
            } else if (filePath.endsWith(".docx")) {
                // 处理docx文件
                searchResults.addAll(searchDocxFile(filePath, keyword));
            }
        }
        System.out.println(searchResults+"\n");
        return searchResults;
    }

    private List<SearchResult> searchTxtFile(String filePath, String keyword) {
        List<SearchResult> searchResults = new ArrayList<>();
        System.out.println("执行解析txt\n");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            String line;
            int lineNumber = 0;

            // 逐行读取文件并检查关键字
            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.contains(keyword)) {
                    // 创建SearchResult对象并添加到结果列表
                    SearchResult searchResult = new SearchResult(filePath, lineNumber, line);
                    searchResults.add(searchResult);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 处理文件读取错误
        }
        System.out.println("txt文件的解析结果：" + searchResults);
        return searchResults;
    }


    private List<SearchResult> searchDocFile(String filePath, String keyword) {
        List<SearchResult> searchResults = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             HWPFDocument document = new HWPFDocument(fis);
             WordExtractor extractor = new WordExtractor(document)) {

            String[] paragraphs = extractor.getParagraphText();
            int lineNumber = 0;

            // 遍历段落并检查关键字
            for (String paragraph : paragraphs) {
                lineNumber++;

                if (paragraph.contains(keyword)) {
                    // 创建SearchResult对象并添加到结果列表
                    SearchResult searchResult = new SearchResult(filePath, lineNumber, paragraph);
                    searchResults.add(searchResult);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 处理文件读取错误
        }

        return searchResults;
    }

    private List<SearchResult> searchDocxFile(String filePath, String keyword) {
        List<SearchResult> searchResults = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {

            String text = extractor.getText();
            String[] paragraphs = text.split("\\r?\\n");
            int lineNumber = 0;

            // 遍历段落并检查关键字
            for (String paragraph : paragraphs) {
                lineNumber++;

                if (paragraph.contains(keyword)) {
                    // 创建SearchResult对象并添加到结果列表
                    SearchResult searchResult = new SearchResult(filePath, lineNumber, paragraph);
                    searchResults.add(searchResult);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 处理文件读取错误
        }

        return searchResults;
    }
}
