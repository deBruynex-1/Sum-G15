<template>
  <div class="app-container">
    <div class="drives-section">
      <h2>磁盘列表</h2>
      <div v-for="drive in drives" :key="drive">
        <button class="drive-button" @click="exploreDrive(drive)">
          <img src="@/assets/disk.png" class="drive-icon" />
          {{ drive }}
        </button>
      </div>

    </div>
    <div class="files-section">
      <div class="current-path">
        <h3 class="title">文件列表</h3>
  <h3 class="path">当前路径: {{ currentPath }}</h3>
        <button class="back-button" @click="goBack">返回上级目录</button>
        <button class="index-button" @click="addFileIndex">添加索引</button>
      </div>
      <div class="files-list-container">
        <div class="files-list">
          <div v-for="file in files" :key="file.path" class="file-item">
            <div class="file-details">
              <div v-if="file.directory" class="directory-button" @click="exploreDirectory(file)">
                <img :src="getImagePath(file)" class="file-image" />
                <span class="file-name">{{ truncateFileName(file.name) }}</span>
              </div>
              <span v-else class="file-span">
                <img :src="getImagePath(file)" class="file-image" />
                <span class="file-name">{{ truncateFileName(file.name) }}</span>
              </span>
            </div>
            <div class="file-checkbox">
              <input type="checkbox" v-model="selectedFiles" :value="file.path" @click="pathDeal(file)"
                class="select-checkbox" />
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="search-section">
      <SearchText />
    </div>
    <div class="centered">
      <div class="index-container">
        <FileIndexView />
      </div>
    </div>
  </div>
</template>

<style>
.app-container {
  display: flex;
  height: 97vh;
  overflow-y: hidden;
  background: linear-gradient(to bottom, #e6e6e6, #f9f9f9);
}
/*三个主组件样式*/
.drives-section {
  width: 12%;
  padding: 10px;
  background-color: #f2f2f2;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.files-section {
  flex: 1;
  padding: 10px;
  background-color: #f9f9f9;
  overflow-y: hidden;
}

.search-section {
  flex: 1;
  background-color: #fff;
  padding: 10px;
  overflow-y: hidden;
}
/*磁盘组件样式*/
.drive-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 80px;
  margin-bottom: 10px;
  padding: 5px;
  background-color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
}

/*磁盘图标*/
.drive-icon {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

/*文件列表组件样式 */
.current-path {
  position: sticky;
  top: 0;
  background-color: #f9f9f9;
  padding: 10px;
  z-index: 1;
}

.current-path .title {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.current-path .path {
  margin: 0;
  font-size: 14px;
  color: #777;
  margin-top: 5px;
}

.current-path {
  position: sticky;
  top: 0;
  background-color: #f9f9f9;
  padding: 10px;
  z-index: 1;
}

.files-list-container {
  max-height: calc(97vh - 100px);
  overflow-y: auto;
}

.files-list {
  width: 90%;
}

.file-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 10px;
  margin-bottom: 10px;
  background-color: #fff;
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
}

.file-details {
  flex-grow: 1;
}

.directory-button {
  display: flex;
  align-items: center;
  width: 100%;
  cursor: pointer;
}

.file-image {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}

.file-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-checkbox {
  flex-shrink: 0;
  margin-left: 10px;
  align-self: flex-start;
}

/*按钮样式*/
.back-button {
  padding: 5px 10px;
  color: white;
  background-color: #223141;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.back-button:hover {
  background-color: #999;
}

.index-button {
  padding: 5px 10px;
  color: white;
  background-color: #007bff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.index-button:hover {
  background-color: #75abdb;
}

.select-checkbox {
  margin-left: 10px;
}



.centered {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 33.33%;
  background-color: #fff;
  padding: 10px;
}

.index-container {
  max-height: calc(100vh - 80px);
  overflow-y: hidden;
  position: fixed;
  top:15px;
}
/*????*/



</style>

<script>
import axios from 'axios';
import FileIndexView from "../src/FileIndexView.vue";
import SearchText from "../src/SearchText.vue";

export default {
  name: "App",
  components: {
    FileIndexView,
    SearchText,
  },
  data() {
    return {
      drives: [],
      currentPath: '',
      files: [],
      history: [],
      selectedFiles: [],
    };
  },
  mounted() {
    this.getDrives();
  },
  methods: {
    getDrives() {
      axios
        .get('http://121.196.234.38:8081/drives')
        .then((response) => {
          this.drives = response.data;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    exploreDrive(drive) {
      this.currentPath = drive;
      this.getFiles(drive);
    },
    exploreDirectory(file) {
      this.history.push(this.currentPath);
      this.currentPath = file.path;
      this.getFiles(file.path);
    },
    pathDeal(file) {
      console.log('this.selectedFiles=', this.selectedFiles);
    },
    goBack() {
      if (this.history.length > 0) {
        const previousPath = this.history.pop();
        this.currentPath = previousPath;
        this.getFiles(previousPath);
      }
    },
    getFiles(path) {
      axios
        .get('http://localhost:8081/files', {
          params: {
            path: path
          }
        })
        .then((response) => {
          this.files = response.data;
          console.log('this.files', this.files);
        })
        .catch((error) => {
          console.error(error);
        });
    },



    addFileIndex() {
      const selectedFolder = this.selectedFiles.toString();
      axios
        .post('http://localhost:8081/addFileIndex', {
          folderPath: selectedFolder
        })
        .then((response) => {
          console.log('文件索引添加成功');
          console.log(selectedFolder);
          this.showToast('文件索引添加成功');
        })
        .catch((error) => {
          console.log(selectedFolder);
          console.error('添加文件索引时出错:', error);
        });
    },


    truncateFileName(fileName) {
      if (fileName.length > 20) {
        return fileName.substr(0, 20) + "...";
      }
      return fileName;
    },
    getImagePath(file) {
      if (file.directory) {
        return require('@/assets/Docu.png');
      } else if (file.name.endsWith(".pdf")) {
        return require('@/assets/pdf.png');
      } else if (file.name.endsWith(".txt")) {
        return require('@/assets/txt.png');
      } else if (file.name.endsWith(".doc")) {
        return require('@/assets/doc.png');
      } else {
        return require('@/assets/empty.png');
      }
    },

    
  },
};
</script>
