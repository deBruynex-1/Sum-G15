package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileIndexRepository extends JpaRepository<FileIndex, Long> {

    // 添加文件索引
    FileIndex save(FileIndex fileIndex);

    // 根据文件名查询文件索引
    FileIndex findByfileName(String fileName);

    // 根据文件路径查询文件索引
    FileIndex findByfilePath(String filePath);



    // 删除文件索引
    void delete(FileIndex fileIndex);

    // 获取所有文件索引
    List<FileIndex> findAll();

    // 根据条件排序查询文件索引列表（示例：按文件名升序）

}
