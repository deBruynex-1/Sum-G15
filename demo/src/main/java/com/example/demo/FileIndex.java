package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

// FileIndex 实体类
@Entity
@Table(name = "filekey")
public class FileIndex {
    // 其他属性...

    @Column(name = "filename")
    private String fileName;
    @Id
    @Column(name = "filepath")
    private String filePath;

    // 其他方法...

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

// FileIndexRepository 接口

