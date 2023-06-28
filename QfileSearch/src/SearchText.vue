<template>
  <div>
    <input type="text" v-model="keyword" placeholder="请输入关键字" class="search-input" />
    <button @click="search" class="search-button">搜索</button>
    <div class="search-results" v-if="results.length > 0">
      <h3>搜索结果：</h3>
      <ul>
        <template v-for="(group, filePath) in mergedResults">
          <li :key="filePath">
            <input type="checkbox" :id="'checkbox-' + filePath" v-model="selectedResults" :value="group" />
            <label :for="'checkbox-' + filePath">文件路径：{{ filePath }}</label><br />
            <hr>
            <template v-for="(line, index) in group">
              行号：{{ line.lineNumber }} 
              <span v-html="highlightKeyword(line.lineContent)"></span><br/>
            </template>
          </li>
        </template>
      </ul>
      <div class="download-button-container">
        <button @click="downloadSelectedResults" class="download-button">下载选中结果</button>
      </div>
    </div>
    <!-- 显示搜索结果的代码 -->
    <div v-else-if="noResults" class="no-results">
      <p>搜索结果不存在</p>
    </div>
  </div>
</template>


<style>
.search-input {
  margin-left: 20px;
  padding: 5px;
  border: none;
  border-radius: 5px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  width: calc(100% - 120px);
  /* 计算宽度，让它和按钮加起来和不存在的框一样长 */
  margin-right: 15px;
}

.search-button {
  padding: 5px 10px;
  color: white;
  background-color: #007bff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.download-button {
  color: white;
  background-color: #007bff;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #0069d9;
}

.search-results {
  padding: 20px;
  border: 2px solid #ced4da;
  border-radius: 5px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  height: 430px;
  overflow-y: auto;
  margin-right: 15px;
  margin-bottom: 20px;
}

.no-results {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 470px;
  font-size: 24px;
  color: #999;
  text-align: center;
  border: 2px solid #ddd;
  border-radius: 5px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
}

.download-button-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.highlighted {
  color: red;
}

hr {
  margin: 10px 0;
  border: none;
  border-top: 1px dashed #0a0a0a;
}

input[type="checkbox"] {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  border: 1px solid #ddd;
  width: 16px;
  height: 16px;
  border-radius: 3px;
  margin-right: 5px;
  position: relative;
  top: 2px;
  cursor: pointer;
}

input[type="checkbox"]::before {
  content: "";
  display: block;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 8px;
  height: 8px;
  background-color: white;
  border-radius: 2px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

input[type="checkbox"]:checked::before {
  background-color: #007bff;
}

</style>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      keyword: '',
      results: [],
      selectedResults: [],
      noResults: false,
    };
  },
  computed: {
    mergedResults() {
      const merged = {};
      this.results.forEach(result => {
        const filePath = result.filePath;
        if (!merged[filePath]) {
          merged[filePath] = [];
        }
        merged[filePath].push(result);
      });
      return merged;
    },

    highlightKeyword() {
      return function(lineContent) {
        const keyword = document.querySelector('.search-input').value;
        const regex = new RegExp(keyword, 'gi');
        return lineContent.replace(regex, '<span class="highlighted">$&</span>');
      };
    }
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
          this.noResults = this.results.length === 0;
        })
        .catch(error => {
          console.error(error);
        });
    },
    downloadSelectedResults() {
      const selectedContent = this.selectedResults
        .map(group => {
          const filePath = group[0].filePath;
          const lines = group.map(line => `行号：${line.lineNumber} ${line.lineContent}`).join('\n');
          return `文件路径：${filePath}\n---------------------------------\n${lines}\n\n`;
        })
        .join('');

      const element = document.createElement('a');
      const file = new Blob([selectedContent], { type: 'text/plain' });
      element.href = URL.createObjectURL(file);
      element.download = 'selected_results.txt';
      document.body.appendChild(element);
      element.click();
      document.body.removeChild(element);
    },
  }
};
</script>