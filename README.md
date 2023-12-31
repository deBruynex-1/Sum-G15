# G15
  
## 答辩要求
- online: 给出访问域名地址 `url`   
- PPT & 演示 (5分钟)  
- 答辩时分别展示：PPT、功能、性能、内存，并且给出源代码地址  

## 技术要求
- Language: JAVA/C/GO/Shell/nodejs/python ...  
- Database: file/mysql/mongodb ...  
- Page: H5/CSS/JS/VUE/REACT ...  
- Server: FTP/APACHE/NGINX/GIT ...

## 项目命题
要求实现一个APP文件检索系统，具体描述如下：  
1. 选择硬盘一个文件夹作为查询的目录，此时将该目录下的所有检索文件 `pdf`/`word` 建立索引，保存到数据库中；  
2. 输入查询的关键字，将符合要求的结果按下面格式显示出来  
3. 点击选择多个查询结果内容，在点击保存可以将数据保存到文本，并下载。  
```  
路径A\子路径B\...\文件1        
-------------------------------  
行号x    包括关键字的内容行     
行号y    包括关键字的内容行  
...  
行号z    包括关键字的内容行  
  
  
路径C\子路径D\...\文件2       
-------------------------------  
行号x    包括关键字的内容行   
行号y    包括关键字的内容行  
...  
行号z    包括关键字的内容行  
  
  
......  
```  
  
## QfileSearch
使用VUE框架搭建的Web程序  
## demo
Java后端  
## fileindex.sql
存放文件名字和路径    
  
## 项目日志
### 0626 
### version 1.0 
完成项目的基本框架的搭建和相关的检索磁盘功能
###  
### 0627 
### version 1.1
在1.0的基础上实现支持单选的索引添加
###  
### 0628 
### version  1.2
在1.1的基础上进行索引添加的多选，基本功能已经实现  
### version  1.3
界面的位置和相关任务的分配已经完成   
实现关键词标红功能，修复无法返回上级目录的bug，美化界面  
### 0629 
### version  1.4 
项目所有工作完成，已成功部署到服务器
