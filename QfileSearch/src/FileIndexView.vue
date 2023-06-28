<template>
    <div>
      <button class="view-index-button" @click="viewFileIndex">查看文件索引</button>
      <div class="scroll-box">
        <!-- 滚动框内容 -->
      </div>
    </div>
  </template>
  
  <style>
  .view-index-button {
    margin-top: 20px;
  }
  
  .scroll-box {
  width: 500px; /* 设置宽度 */
  height: 300px; /* 设置高度 */
  overflow-y: auto;
  border: 1px solid #000;
  margin: 0 auto;
  max-width: 500px; /* 设置最大宽度 */
}
  </style>
  
  <script>
  import axios from 'axios'
  
  export default {
    methods: {
      viewFileIndex() {
  // 发起后端请求获取文件名列表
  axios.get('http://localhost:8081/fileNames')
  .then(response => {
    const fileNames = response.data;
    // 在滚动框中显示文件名列表
    const scrollBox = document.querySelector('.scroll-box');
    scrollBox.innerHTML = fileNames.join('<br>');
  })
  .catch(error => {
    console.error(error);
  });

}

    }
  };
  </script>
  