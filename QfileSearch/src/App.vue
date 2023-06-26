<template>
  <div>
    <h2>文件浏览器</h2>
    <div class="drives-section">
      <div v-for="drive in drives" :key="drive">
        <button class="drive-button" @click="exploreDrive(drive)">{{ drive }}</button>
      </div>
    </div>
    <div v-if="currentPath" class="files-section">
      <div class="current-path">
        <h3>当前路径: {{ currentPath }}</h3>
        <button class="back-button" @click="goBack">返回上级目录</button>
      </div>
      <div class="files-list">
        <div v-for="file in files" :key="file.path" class="file-item">
          <button v-if="file.directory" class="directory-button" @click="exploreDirectory(file.path)">{{ file.name }}</button>
          <span v-else class="file-span">{{ file.name }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.drives-section {
  margin-bottom: 20px;
  background-color: #f2f2f2;
  padding: 10px;
}

.drive-button,
.directory-button,
.file-span {
  display: inline-block;
  width: 100%;
  height: 25px;
  padding: 5px;
  margin: 5px;
  border: 0.5px solid #000;
  text-align: center;
  cursor: pointer;
}

.drive-button {
  background-color: yellow;
}

.directory-button {
  background-color: yellow;
}

.file-span {
  background-color: green;
  color: #fff;
}

.files-section {
  background-color: #f9f9f9;
  padding: 10px;
}

.current-path {
  position: sticky;
  top: 0;
  background-color: #f9f9f9;
  padding: 10px;
  z-index: 1;
}

.files-list {
  width: 100%;
}

.file-item {
  width: calc(100% - 20px);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.back-button {
  margin-left: 10px;
}

</style>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      drives: [],
      currentPath: '',
      files: [],
      history: [], // 用于存储浏览历史记录
    };
  },
  mounted() {
    this.getDrives();
  },
  methods: {
    getDrives() {
      // 发起后端请求获取驱动器列表
      axios.get('http://localhost:8081/drives')
        .then(response => {
          this.drives = response.data;
        })
        .catch(error => {
          console.error(error);
        });
    },
    exploreDrive(drive) {
      this.currentPath = drive;
      this.getFiles(drive);
    },
    exploreDirectory(path) {
      this.history.push(this.currentPath); // 将当前路径加入历史记录
      this.currentPath = path;
      this.getFiles(path);
    },
    goBack() {
      if (this.history.length > 0) {
        const previousPath = this.history.pop(); // 弹出最后一个历史记录
        this.currentPath = previousPath;
        this.getFiles(previousPath);
      }
    },
    getFiles(path) {
      // 发起后端请求获取指定路径下的文件列表
      axios.get('http://localhost:8081/files', {
        params: {
          path: path
        }
      })
        .then(response => {
          this.files = response.data;
        })
        .catch(error => {
          console.error(error);
        });
    }
  }
};
</script>
