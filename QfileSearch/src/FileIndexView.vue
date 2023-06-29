<template>
  <div>
    <button class="view-index-button" @click="viewFileIndex">查看文件索引</button>
    <div class="scroll-box">
      <div class="file-item" v-for="file in files" :key="file.name">
        <div class="file-icon">
          <img :src="getFileIconSrc(file.name)" alt="文件图标">
        </div>
        <div class="file-name">
          {{ file.name }}
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.view-index-button  {
  padding: 5px 10px;
  color: white;
  background-color: #223141;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.view-index-button:hover {
  background-color: #999;
}


.scroll-box {
  width: 300px;
  height: 80vh;
  overflow-y: scroll;
  border: 1px solid #000;
  margin: 0 auto;
  max-width: 500px;
}

.file-item {
  display: flex;
  align-items: center;  /* 垂直居中对齐 */
  padding: 1px;
  border-bottom: 1px solid #ccc;
  justify-content: center; /* 水平居中对齐 */
}

.file-icon {
  margin-right: 10px;
}

.file-icon img {
  display: inline-block;
  width: 24px;
  height: 24px;
}

.file-name {
  flex-grow: 1;
}
</style>

<script>
import axios from 'axios';
import docIcon from '@/assets/doc.png';
import docxIcon from '@/assets/docx.png';
import txtIcon from '@/assets/txt.png';
import pdfIcon from '@/assets/pdf.png';
import errorIcon from '@/assets/empty.png';

export default {
  data() {
    return {
      files: []
    };
  },
  methods: {
    viewFileIndex() {
      // 发起后端请求获取文件名列表
      axios.get('http://121.196.234.38:8081/fileNames')
        .then(response => {
          const fileNames = response.data;
          // 将文件名列表转换为文件对象数组
          this.files = fileNames.map(fileName => {
            return { name: fileName, type: this.getFileExtension(fileName) };
          });
        })
        .catch(error => {
          console.error(error);
        });
    },
    getFileExtension(fileName) {
      return fileName.split('.').pop().toLowerCase();
    },
    getFileIconSrc(fileName) {
      const extension = this.getFileExtension(fileName);

      // 根据文件类型返回相应的图标路径
      switch (extension) {
        case 'doc':
          return docIcon; // 假设图标文件(doc.png)位于src/assets目录下
        case 'txt':
          return txtIcon;
        case 'pdf':
          return pdfIcon;
        case 'docx':
          return docxIcon;
        default:
          return errorIcon;
      }
    }
  }
};
</script>
