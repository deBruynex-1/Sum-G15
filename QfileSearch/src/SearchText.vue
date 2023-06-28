<template>
  <div>
    <input type="text" v-model="keyword" placeholder="请输入关键字" />
    <button @click="search">搜索</button>
    <div v-if="results.length > 0">
      <h3>搜索结果：</h3>
      <ul>
        <li v-for="(result, index) in results" :key="index">
          <input type="checkbox" :id="'checkbox-' + index" v-model="selectedResults" :value="result" />
          <label :for="'checkbox-' + index">文件路径：{{ result.filePath }}</label><br />
          ---------------------------------<br />
          行号：{{ result.lineNumber }} {{ result.lineContent }}<br />
        </li>
      </ul>
      <button @click="downloadSelectedResults">下载选中结果</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      keyword: '',
      results: [],
      selectedResults: []
    };
  },
  methods: {
    search() {
      // 发起后端请求进行搜索
      axios
        .get('http://localhost:8081/search', {
          params: {
            keyword: this.keyword
          }
        })
        .then(response => {
          this.results = response.data;
        })
        .catch(error => {
          console.error(error);
        });
    },
    downloadSelectedResults() {
      const selectedContent = this.selectedResults
        .map(result => {
          return `文件路径：${result.filePath}\n---------------------------------\n行号：${result.lineNumber}           ${result.lineContent}\n\n`;
        })
        .join('');

      const element = document.createElement('a');
      const file = new Blob([selectedContent], { type: 'text/plain' });
      element.href = URL.createObjectURL(file);
      element.download = 'selected_results.txt';
      document.body.appendChild(element);
      element.click();
      document.body.removeChild(element);
    }
  }
};
</script>
