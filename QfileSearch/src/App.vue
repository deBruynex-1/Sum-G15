<template>
  <div class="app-container">
    <div class="drives-section">
      <h2>文件浏览器</h2>
      <div v-for="drive in drives" :key="drive">
        <button class="drive-button" @click="exploreDrive(drive)">{{ drive }}</button>
      </div>
    </div>
    <div class="files-section">
      <div class="current-path">
        <h3>当前路径: {{ currentPath }}</h3>
        <button class="back-button" @click="goBack">返回上级目录</button>
        <button class="index-button" @click="addFileIndex">添加索引</button>
      </div>
      <div class="files-list-container">
        <div class="files-list">
          <div v-for="file in files" :key="file.path" class="file-item">
            <div class="file-details">
              <button v-if="file.directory" class="directory-button" @click="exploreDirectory(file)">{{ file.name }}</button>
              <span v-else class="file-span">{{ file.name }}</span>
            </div>
            <div class="file-checkbox">
              <input type="checkbox" v-model="selectedFiles" :value="file.path" @click="pathDeal(file)" class="select-checkbox" />
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
  height: 100vh;
  overflow: hidden; /* 隐藏溢出部分 */
}

.drives-section {
  width: 12%; /* 调整宽度大小 */
  padding: 10px;
  background-color: #f2f2f2;
}


.files-section {
  flex: 1;
  padding: 10px 10px 10px 5px; /* 调整左边延伸的大小 */
  background-color: #f9f9f9;
  overflow-y: auto;
}

.current-path {
  position: sticky;
  top: 0;
  background-color: #f9f9f9;
  padding: 10px;
  z-index: 1;
}

.files-list-container {
  max-height: calc(100vh - 140px); /* 设置文件列表容器的最大高度，减去头部和底部部分的高度 */
  overflow-y: auto; /* 添加滚动样式 */
}

.files-list {
  width: 100%;
}

.file-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.file-details {
  flex-grow: 1;
}

.file-checkbox {
  flex-shrink: 0;
  margin-left: 10px;
}

.back-button {
  margin-left: 10px;
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
  max-height: calc(100vh - 140px); /* 设置索引容器的最大高度，减去头部和底部部分的高度 */
  overflow-y: auto; /* 添加滚动样式 */
}

.search-section {
  flex: 1;
  background-color: #fff;
  padding: 10px;
}
</style>

<script>
import axios from 'axios'
import FileIndexView from "../src/FileIndexView.vue";
import SearchText from "../src/SearchText.vue";



export default {
  name: "App",
  components: {
    FileIndexView, // 注册文件索引视图组件
    SearchText,
  },
  data() {
    return {
      drives: [],
      currentPath: '',
      files: [],
      history: [], // 用于存储浏览历史记录
      selectedFiles: [], // 用于存储选中的文件夹
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
    exploreDirectory(file) {
      this.history.push(this.currentPath); // 将当前路径加入历史记录
      if(file.directory){
        this.currentPath = file.path ;
      }else{
        this.currentPath = file.path;
      }
      
      this.getFiles(file.path);
    },
    pathDeal(file){
      // if(file.directory){
      //   this.selectedFiles = file.path + file.name;
      // }
      console.log('this.selectedFiles=', this.selectedFiles);
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
          // this.files.forEach(e=>{
          //   if(e.directory){
          //     e.path = e.path + '\\' + e.name;
          //   }
          // })
          console.log('this.files', this.files);
        })
        .catch(error => {
          console.error(error);
        });
    },



    addFileIndex() {
  // 获取选中的文件夹路径
  const selectedFolder = this.selectedFiles.toString();
  // 发起后端请求将选中文件夹下的所有文件路径添加到数据库
  axios.post('http://localhost:8081/addFileIndex', {
  
    folderPath: selectedFolder
    
  })
    .then(response => {
      console.log('文件索引添加成功');
      console.log(selectedFolder);
    })
    .catch(error => {
      console.log(selectedFolder);
      console.error('添加文件索引时出错:', error);
    });
},
  }
};
</script>
