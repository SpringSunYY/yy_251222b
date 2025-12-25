<template>
  <div class="file-preview-container">
    <div class="file-preview">
      <div class="file-preview-header">
        <h3>{{ fileName }}</h3>
        <div class="file-preview-actions">
          <el-button size="small" @click="goBack">返回</el-button>
          <el-button type="primary" size="small" @click="downloadFile">下载</el-button>
        </div>
      </div>

      <div class="file-preview-content" v-loading="loading">
        <!-- TXT文件预览 -->
        <div v-if="fileType === 'txt'" class="txt-preview">
          <pre>{{ fileContent }}</pre>
        </div>

        <!-- DOCX文件预览 -->
        <div v-else-if="fileType === 'docx'" class="docx-preview">
          <div class="document-content" v-html="parsedContent"></div>
        </div>

        <!-- Excel文件预览 -->
        <div v-else-if="fileType === 'xlsx' || fileType === 'xls'" class="excel-preview">
          <div class="excel-table-container">
            <table class="excel-table">
              <tbody>
                <tr v-for="(row, rowIndex) in excelData" :key="rowIndex">
                  <td v-for="(cell, cellIndex) in row" :key="cellIndex" class="excel-cell">
                    {{ cell }}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- iframe预览 -->
        <div v-else-if="['pdf', 'ppt', 'pptx'].includes(fileType)" class="iframe-preview">
          <iframe
            :src="previewUrl"
            width="100%"
            height="100%"
            frameborder="0"
            @load="onIframeLoad"
            @error="onIframeError"
          ></iframe>
        </div>


        <!-- 预览失败 -->
        <div v-else-if="previewFailed" class="preview-failed">
          <div class="preview-failed-message">
            <i class="el-icon-warning file-icon"></i>
            <p>文件预览失败</p>
            <p class="file-info">可能的原因：文件损坏、网络问题或文件格式不兼容</p>
            <p class="file-info">您可以选择下载文件到本地查看</p>
            <el-button type="primary" @click="confirmDownload">下载文件</el-button>
          </div>
        </div>

        <!-- 不支持的文件类型 -->
        <div v-else class="unsupported-preview">
          <div class="unsupported-message">
            <i class="el-icon-document file-icon"></i>
            <p>该文件类型暂不支持在线预览</p>
            <p class="file-info">当前支持预览的文件类型：TXT、DOC、DOCX、PPT、PPTX、PDF、XLS、XLSX</p>
            <el-button type="primary" @click="downloadFile">下载文件</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
import mammoth from 'mammoth';
import * as XLSX from 'xlsx';

export default {
  name: "FilePreview",
  data() {
    return {
      loading: true,
      previewFailed: false,
      fileName: "",
      fileType: "",
      fileUrl: "",
      fileContent: "",
      parsedContent: "",
      excelData: [],
      previewUrl: "",
      iframeTimeout: null,
      baseUrl: process.env.VUE_APP_BASE_API
    };
  },
  created() {
    this.initFileView();
  },
  beforeDestroy() {
    // 清理iframe超时定时器
    if (this.iframeTimeout) {
      clearTimeout(this.iframeTimeout);
      this.iframeTimeout = null;
    }
  },
  methods: {
    initFileView() {
      const { fileUrl } = this.$route.query;
      if (!fileUrl) {
        this.$modal.msgError("文件路径不存在");
        this.goBack();
        return;
      }

      console.log('文件URL:', fileUrl);
      this.fileUrl = fileUrl;
      this.fileName = this.getFileName(fileUrl);
      this.fileType = this.getFileType(fileUrl);

      console.log('文件名:', this.fileName);
      console.log('文件类型:', this.fileType);

      // 根据文件类型进行处理
      if (this.fileType === 'txt') {
        this.loadTxtContent();
      } else if (this.fileType === 'docx') {
        this.loadDocxContent();
      } else if (this.fileType === 'xlsx' || this.fileType === 'xls') {
        this.loadExcelContent();
      } else if (['pdf', 'ppt', 'pptx'].includes(this.fileType)) {
        // 这些文件类型在浏览器中可以正常iframe预览
        this.loadFileIframePreview();
      } else {
        // 其他文件类型（如doc）或未知类型，直接标记为预览失败
        this.previewFailed = true;
        this.loading = false;
      }
    },

    loadFilePreview() {
      // 对于不支持解析的文件类型，直接在新标签页打开
      const fullUrl = `${this.baseUrl}${this.fileUrl}`;
      console.log('在新标签页中打开文件:', fullUrl);
      window.open(fullUrl, '_blank');
      this.goBack();
    },

    loadFileIframePreview() {
      // 对于支持iframe预览的文件类型
      const fullUrl = `${this.baseUrl}${this.fileUrl}`;
      this.previewUrl = fullUrl;
      console.log('使用iframe预览文件:', fullUrl);

      // 设置超时检测，如果iframe在10秒内没有加载完成，认为预览失败
      this.iframeTimeout = setTimeout(() => {
        console.log('iframe预览超时，标记为预览失败');
        this.previewFailed = true;
        this.previewUrl = '';
        this.loading = false;
      }, 10000); // 10秒超时

      this.loading = false;
    },

    loadTxtContent() {
      // 加载TXT文件内容
      const fullUrl = `${this.baseUrl}${this.fileUrl}`;
      console.log('TXT文件完整URL:', fullUrl);

      fetch(fullUrl, {
        headers: {
          'Authorization': `Bearer ${getToken()}`
        }
      })
      .then(response => {
        console.log('TXT文件响应:', response);
        return response.text();
      })
      .then(content => {
        console.log('TXT文件内容长度:', content.length);
        this.fileContent = content;
        this.loading = false;
      })
      .catch(error => {
        console.error('加载TXT文件内容失败:', error);
        this.previewFailed = true;
        this.loading = false;
      });
    },

    async loadDocxContent() {
      // 加载并解析DOCX文件
      const fullUrl = `${this.baseUrl}${this.fileUrl}`;
      console.log('DOCX文件完整URL:', fullUrl);

      try {
        const response = await fetch(fullUrl, {
          headers: {
            'Authorization': `Bearer ${getToken()}`
          }
        });

        if (!response.ok) {
          throw new Error('网络请求失败');
        }

        const arrayBuffer = await response.arrayBuffer();

        const result = await mammoth.convertToHtml({ arrayBuffer });
        this.parsedContent = result.value;

        console.log('DOCX文件解析完成');
        this.loading = false;
      } catch (error) {
        console.error('加载DOCX文件内容失败:', error);
        this.previewFailed = true;
        this.loading = false;
      }
    },

    async loadExcelContent() {
      // 加载并解析Excel文件
      const fullUrl = `${this.baseUrl}${this.fileUrl}`;
      console.log('Excel文件完整URL:', fullUrl);

      try {
        const response = await fetch(fullUrl, {
          headers: {
            'Authorization': `Bearer ${getToken()}`
          }
        });

        if (!response.ok) {
          throw new Error('网络请求失败');
        }

        const arrayBuffer = await response.arrayBuffer();

        const workbook = XLSX.read(arrayBuffer, { type: 'array' });

        // 获取第一个工作表
        const sheetName = workbook.SheetNames[0];
        const worksheet = workbook.Sheets[sheetName];

        // 转换为JSON格式
        const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

        this.excelData = jsonData;

        console.log('Excel文件解析完成，行数:', jsonData.length);
        this.loading = false;
      } catch (error) {
        console.error('加载Excel文件内容失败:', error);
        this.previewFailed = true;
        this.loading = false;
      }
    },

    getFileName(url) {
      if (url.lastIndexOf("/") > -1) {
        return url.slice(url.lastIndexOf("/") + 1);
      } else {
        return url;
      }
    },

    getFileType(url) {
      const fileName = this.getFileName(url);
      const lastDotIndex = fileName.lastIndexOf('.');
      if (lastDotIndex > -1) {
        return fileName.slice(lastDotIndex + 1).toLowerCase();
      }
      return '';
    },

    onIframeLoad() {
      // iframe成功加载，清除超时定时器
      console.log('iframe加载成功');
      if (this.iframeTimeout) {
        clearTimeout(this.iframeTimeout);
        this.iframeTimeout = null;
      }
    },

    onIframeError() {
      // iframe加载失败
      console.log('iframe加载失败');
      if (this.iframeTimeout) {
        clearTimeout(this.iframeTimeout);
        this.iframeTimeout = null;
      }
      this.previewFailed = true;
      this.previewUrl = '';
    },

    confirmDownload() {
      this.$confirm(`确定要下载文件 "${this.fileName}" 吗？`, '下载确认', {
        confirmButtonText: '确定下载',
        cancelButtonText: '取消',
        type: 'warning',
        customClass: 'download-confirm-dialog'
      }).then(() => {
        this.downloadFile();
      }).catch(() => {
        // 用户取消下载
      });
    },

    downloadFile() {
      console.log('开始下载文件');
      console.log('基础URL:', this.baseUrl);
      console.log('文件URL:', this.fileUrl);
      console.log('完整下载URL:', `${this.baseUrl}${this.fileUrl}`);

      const link = document.createElement('a');
      link.href = `${this.baseUrl}${this.fileUrl}`;
      link.download = this.fileName;
      link.target = '_blank';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    goBack() {
      // 返回到上一页
      this.$router.go(-1);
    }
  }
};
</script>

<style scoped lang="scss">
.file-preview-container {
  height: 100vh;
  background: #fff;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.file-preview {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;

  .file-preview-header {
    padding: 12px 16px;
    border-bottom: 1px solid #ddd;
    display: flex;
    justify-content: space-between;
    align-items: center;

    h3 {
      margin: 0;
      font-size: 18px;
      font-weight: 600;
      color: #1f2937;
      display: flex;
      align-items: center;
      gap: 8px;

      &::before {
        content: '📄';
        font-size: 20px;
      }
    }

    .file-preview-actions {
      display: flex;
      gap: 8px;

      .el-button {
        border-radius: 6px;
        font-weight: 500;

        &:hover {
          transform: translateY(-1px);
        }
      }
    }
  }

  .file-preview-content {
    flex: 1;
    padding: 16px;
    height: 100%;

    .txt-preview {
      height: 100%;
      overflow-y: auto;
      font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
      font-size: 14px;
      line-height: 1.5;
      color: #000;
      white-space: pre-wrap;
      word-wrap: break-word;
    }

    .docx-preview {
      height: 100%;
      overflow-y: auto;

      .document-content {
        font-family: 'Microsoft YaHei', 'SimSun', sans-serif;
        line-height: 1.6;
        color: #000;
        font-size: 14px;

        p {
          margin: 0 0 10px 0;
        }

        h1, h2, h3, h4, h5, h6 {
          font-weight: bold;
          margin: 16px 0 8px 0;
        }

        ul, ol {
          margin: 10px 0;
          padding-left: 20px;

          li {
            margin: 4px 0;
          }
        }

        table {
          border-collapse: collapse;
          width: 100%;
          margin: 12px 0;
        }

        table td, table th {
          border: 1px solid #000;
          padding: 6px 8px;
          text-align: left;
        }

        table th {
          font-weight: bold;
        }
      }
    }

    .excel-preview {
      height: 100%;
      overflow-y: auto;

      .excel-table-container {
        .excel-table {
          border-collapse: collapse;
          width: 100%;
          font-family: 'Microsoft YaHei', sans-serif;
          font-size: 13px;

          .excel-cell {
            border: 1px solid #000;
            padding: 6px 8px;
            text-align: left;

            &:first-child {
              font-weight: bold;
            }
          }
        }
      }
    }


    .iframe-preview {
      width: 100%;
      height: 100%;

      iframe {
        width: 100%;
        height: 100%;
        border: none;
      }
    }

    .preview-failed {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 300px;

      .preview-failed-message {
        text-align: center;

        .file-icon {
          font-size: 32px;
          color: #f5a623;
          margin-bottom: 12px;
        }

        p {
          color: #666;
          margin-bottom: 6px;
          font-size: 14px;
          font-weight: 500;
        }

        .file-info {
          color: #999;
          font-size: 12px;
          margin-bottom: 16px;
        }

        .el-button {
          padding: 8px 16px;
          font-size: 13px;
        }
      }
    }

    .unsupported-preview {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 300px;

      .unsupported-message {
        text-align: center;

        .file-icon {
          font-size: 32px;
          color: #ccc;
          margin-bottom: 12px;
        }

        p {
          color: #666;
          margin-bottom: 6px;
          font-size: 14px;
        }

        .file-info {
          color: #999;
          font-size: 12px;
          margin-bottom: 16px;
        }

        .el-button {
          padding: 8px 16px;
          font-size: 13px;
        }
      }
    }
  }
}
</style>