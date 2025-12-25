<template>
    <div class="app-container">
      <!-- 查询表单 -->
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="类型名称" prop="fileTypeName">
          <el-input
            v-model="queryParams.fileTypeName"
            placeholder="请输入类型名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="名称" prop="fileName">
          <el-input
            v-model="queryParams.fileName"
            placeholder="请输入名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="daterangeCreateTime"
            style="width: 240px"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 工具栏 -->
      <el-row :gutter="10" class="mb8">
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <!-- 文件卡片列表 -->
      <div class="file-card-container">
        <div class="file-card-grid">
          <div
            v-for="(item, index) in fileInfoList"
            :key="item.id"
            class="file-card"
            @click="handleCardClick(item)"
          >
            <div class="file-card-header">
              <div class="file-type-icon">
                <i :class="getFileTypeIcon(item.fileType)" class="file-icon"></i>
              </div>
              <div class="file-actions">
                <el-button
                  type="text"
                  size="mini"
                  :icon="isCollected(item.id) ? 'el-icon-star-on' : 'el-icon-star-off'"
                  :class="isCollected(item.id) ? 'collected' : 'uncollected'"
                  @click.stop="handleCollect(item)"
                  :loading="collectingIds.includes(item.id)"
                >
                  {{ isCollected(item.id) ? '已收藏' : '收藏' }}
                </el-button>
              </div>
            </div>

            <div class="file-card-body">
              <h3 class="file-name" :title="item.fileName">{{ item.fileName }}</h3>
              <p class="file-description" v-if="item.description">{{ item.description }}</p>

              <div class="file-meta">
                <span class="file-type">{{ item.fileTypeName }}</span>
                <span class="file-size" v-if="item.fileSize">{{ formatFileSize(item.fileSize) }}</span>
              </div>

              <div class="file-stats">
                <span class="stat-item">
                  <i class="el-icon-view"></i>
                  {{ item.viewCount || 0 }}
                </span>
                <span class="stat-item">
                  <i class="el-icon-download"></i>
                  {{ item.downloadCount || 0 }}
                </span>
                <span class="stat-item">
                  <i class="el-icon-star-on"></i>
                  {{ item.collectCount || 0 }}
                </span>
              </div>
            </div>

            <div class="file-card-footer">
              <div class="file-operations">
                <el-link
                  :download="getFileName(item.fileUrl)"
                  :href="getFilePath(item.fileUrl)"
                  :underline="false"
                  target="_blank"
                  style="margin-right: 10px"
                >
                  <i class="el-icon-download"></i> 下载
                </el-link>
                <el-link
                  :underline="false"
                  :style="{ color: canPreviewFile(item.fileUrl) ? '#67C23A' : '#909399' }"
                  @click.stop="previewFile(item.fileUrl)"
                  :title="canPreviewFile(item.fileUrl) ? '在新标签页中预览文件' : '该文件类型暂不支持预览'"
                  target="_blank"
                >
                  {{ canPreviewFile(item.fileUrl) ? '预览' : '查看' }}
                </el-link>
              </div>
              <div class="file-time">
                {{ parseTime(item.createTime, '{y}-{m}-{d}') }}
              </div>
            </div>
          </div>
        </div>

        <!-- 加载更多 -->
        <div class="load-more-container" v-if="hasMore">
          <el-button
            type="primary"
            :loading="loadingMore"
            @click="loadMore"
            size="small"
          >
            {{ loadingMore ? '加载中...' : '加载更多' }}
          </el-button>
        </div>

        <!-- 无数据提示 -->
        <div class="no-data" v-if="!loading && fileInfoList.length === 0">
          <el-empty description="暂无文件数据"></el-empty>
        </div>
      </div>

      <!-- 全局加载状态 -->
      <div v-if="loading && fileInfoList.length === 0" class="global-loading">
        <el-loading text="加载中..."></el-loading>
      </div>

      <!-- 收藏弹窗 -->
      <el-dialog title="收藏文件" :visible.sync="collectDialogVisible" width="400px" append-to-body>
        <el-form ref="collectForm" :model="collectForm" :rules="collectRules" label-width="80px">
          <el-form-item label="收藏夹" prop="folderId">
            <el-select
              v-model="collectForm.folderId"
              placeholder="请选择收藏夹"
              style="width: 100%"
            >
              <el-option
                v-for="item in collectFolderList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="备注">
            <el-input
              v-model="collectForm.remark"
              type="textarea"
              placeholder="请输入备注（可选）"
              :rows="3"
            />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelCollectDialog">取消</el-button>
          <el-button type="primary" @click="submitCollectForm">确定</el-button>
        </div>
      </el-dialog>
    </div>
  </template>

  <script>
import {
  listFileInfoPublic
} from "@/api/manage/fileInfo";
import { addCollectInfo, delCollectInfoByFileId } from "@/api/manage/collectInfo";
import { listCollectFolder } from "@/api/manage/collectFolder";
  import { formatFileSize } from "@/utils/ruoyi";
  import { listFileType } from "@/api/manage/fileType";

  export default {
    name: "FileGallery",
    dicts: ['is_public'],
  data() {
    return {
      // 遮罩层
      loading: false,
      loadingMore: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 文件信息列表
      fileInfoList: [],
      // 是否还有更多数据
      hasMore: true,
      // 更新时间时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        fileTypeName: null,
        fileName: null,
        fileType: null,
        isPublic: null,
        createTime: null,
      },
      // 收藏夹列表
      collectFolderList: [],
      collectFolderLoading: false,
      // 收藏弹窗
      collectDialogVisible: false,
      collectForm: {
        folderId: null,
        remark: ''
      },
      collectRules: {
        folderId: [
          {required: true, message: "请选择收藏夹", trigger: "change"}
        ]
      },
      // 当前要收藏的文件
      currentCollectFile: null,
      // 正在收藏的文件ID列表
      collectingIds: []
    };
  },
  created() {
    this.getList();
    this.getCollectFolderList();
  },
    methods: {
      formatFileSize,
      /** 查询文件信息列表 */
      getList() {
        if (this.queryParams.pageNum === 1) {
          this.loading = true;
        } else {
          this.loadingMore = true;
        }

        this.queryParams.params = {};
        if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
          this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
          this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
        }

        listFileInfoPublic(this.queryParams).then(response => {
          if (this.queryParams.pageNum === 1) {
            this.fileInfoList = response.rows;
          } else {
            this.fileInfoList = [...this.fileInfoList, ...response.rows];
          }

          this.total = response.total;
          this.hasMore = this.fileInfoList.length < this.total;
          this.loading = false;
          this.loadingMore = false;
        }).catch(() => {
          this.loading = false;
          this.loadingMore = false;
        });
      },

      /** 获取收藏夹列表 */
      getCollectFolderList() {
        this.collectFolderLoading = true;
        const userId = this.$store.getters.userId || 1;
        listCollectFolder({
          pageNum: 1,
          pageSize: 1000,
          userId: userId
        }).then(response => {
          this.collectFolderList = response.rows;
          this.collectFolderLoading = false;
        }).catch(() => {
          this.collectFolderList = [];
          this.collectFolderLoading = false;
        });
      },

      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },

      /** 重置按钮操作 */
      resetQuery() {
        this.daterangeCreateTime = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },

      /** 加载更多 */
      loadMore() {
        if (!this.hasMore || this.loadingMore) return;
        this.queryParams.pageNum++;
        this.getList();
      },

      /** 处理卡片点击 */
      handleCardClick(item) {
        // 可以跳转到详情页面或预览文件
        if (this.canPreviewFile(item.fileUrl)) {
          this.previewFile(item.fileUrl);
        }
      },

      /** 处理收藏 */
      handleCollect(item) {
        if (this.collectingIds.includes(item.id)) return;

        const isCollected = this.isCollected(item.id);

        if (isCollected) {
          // 取消收藏
          this.$modal.confirm(`确定要取消收藏文件"${item.fileName}"吗？`).then(() => {
            this.collectingIds.push(item.id);

            delCollectInfoByFileId(item.id).then(() => {
              // 更新本地状态
              item.isCollect = false;
              this.$modal.msgSuccess('取消收藏成功');
            }).catch(() => {
              this.$modal.msgError('取消收藏失败');
            }).finally(() => {
              this.collectingIds = this.collectingIds.filter(id => id !== item.id);
            });
          });
        } else {
          // 添加收藏 - 显示收藏弹窗
          if (this.collectFolderList.length === 0) {
            this.$modal.confirm('您还没有收藏夹，是否前往创建收藏夹？', '提示', {
              confirmButtonText: '去创建',
              cancelButtonText: '取消'
            }).then(() => {
              // 这里可以跳转到收藏夹管理页面
              this.$message.info('请先创建收藏夹');
            });
            return;
          }

          this.currentCollectFile = item;
          this.collectForm.folderId = this.collectFolderList[0].id; // 默认选择第一个收藏夹
          this.collectForm.remark = '';
          this.collectDialogVisible = true;
        }
      },

      /** 提交收藏 */
      submitCollectForm() {
        this.$refs.collectForm.validate(valid => {
          if (valid) {
            this.collectingIds.push(this.currentCollectFile.id);

            const collectData = {
              fileId: this.currentCollectFile.id,
              fileName: this.currentCollectFile.fileName,
              fileTypeId: this.currentCollectFile.fileTypeId,
              fileTypeName: this.currentCollectFile.fileTypeName,
              fileType: this.currentCollectFile.fileType,
              fileSize: this.currentCollectFile.fileSize,
              folderId: this.collectForm.folderId,
              remark: this.collectForm.remark,
              userId: this.$store.getters.userId || 1
            };

            addCollectInfo(collectData).then(() => {
              // 更新本地状态
              this.currentCollectFile.isCollect = true;
              this.collectDialogVisible = false;
              this.$modal.msgSuccess('收藏成功');
            }).catch(() => {
              this.$modal.msgError('收藏失败');
            }).finally(() => {
              this.collectingIds = this.collectingIds.filter(id => id !== this.currentCollectFile.id);
              this.currentCollectFile = null;
            });
          }
        });
      },

      /** 取消收藏弹窗 */
      cancelCollectDialog() {
        this.collectDialogVisible = false;
        this.currentCollectFile = null;
        this.collectForm.folderId = null;
        this.collectForm.remark = '';
      },

      /** 判断文件是否已收藏 */
      isCollected(fileId) {
        const file = this.fileInfoList.find(item => item.id === fileId);
        return file ? file.isCollect : false;
      },

      /** 获取文件图标 */
      getFileTypeIcon(fileType) {
        const typeIcons = {
          'pdf': 'el-icon-document-checked',
          'doc': 'el-icon-document-copy',
          'docx': 'el-icon-document-copy',
          'xls': 'el-icon-s-data',
          'xlsx': 'el-icon-s-data',
          'ppt': 'el-icon-s-management',
          'pptx': 'el-icon-s-management',
          'txt': 'el-icon-document',
          'jpg': 'el-icon-picture',
          'jpeg': 'el-icon-picture',
          'png': 'el-icon-picture',
          'gif': 'el-icon-picture',
          'zip': 'el-icon-files',
          'rar': 'el-icon-files'
        };
        return typeIcons[fileType] || 'el-icon-document';
      },

      // 获取文件名
      getFileName(fileUrl) {
        if (fileUrl && fileUrl.lastIndexOf("/") > -1) {
          return fileUrl.slice(fileUrl.lastIndexOf("/") + 1);
        } else {
          return fileUrl;
        }
      },

      // 获取文件路径
      getFilePath(fileUrl) {
        return process.env.VUE_APP_BASE_API + fileUrl;
      },

      // 获取文件类型
      getFileType(fileUrl) {
        const fileName = this.getFileName(fileUrl);
        const lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > -1) {
          return fileName.slice(lastDotIndex + 1).toLowerCase();
        }
        return '';
      },

      // 判断文件是否支持预览
      canPreviewFile(fileUrl) {
        const fileType = this.getFileType(fileUrl);
        // 支持预览的文件类型
        return ['txt', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'pdf'].includes(fileType);
      },

      // 预览文件
      previewFile(fileUrl) {
        this.$router.push({
          path: '/file-preview',
          query: {
            fileUrl: fileUrl
          }
        });
      }
    }
  };
  </script>

  <style lang="scss" scoped>
  .file-card-container {
    .file-card-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 20px;
      margin-bottom: 20px;
    }

    .file-card {
      border: 1px solid #e4e7ed;
      border-radius: 8px;
      background: #fff;
      transition: all 0.3s ease;
      cursor: pointer;
      overflow: hidden;

      &:hover {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        border-color: #409eff;
        transform: translateY(-2px);
      }

      .file-card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px 15px 0 15px;

        .file-type-icon {
          .file-icon {
            font-size: 24px;
            color: #409eff;
          }
        }

        .file-actions {
          .el-button {
            padding: 5px;

            &.collected {
              color: #e6a23c;
            }

            &.uncollected {
              color: #909399;
            }
          }
        }
      }

      .file-card-body {
        padding: 15px;

        .file-name {
          font-size: 16px;
          font-weight: 500;
          color: #303133;
          margin: 0 0 8px 0;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .file-description {
          font-size: 14px;
          color: #606266;
          margin: 0 0 12px 0;
          line-height: 1.4;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
          line-clamp: 2;
        }

        .file-meta {
          display: flex;
          justify-content: space-between;
          margin-bottom: 12px;

          .file-type {
            font-size: 12px;
            color: #909399;
            background: #f5f7fa;
            padding: 2px 8px;
            border-radius: 4px;
          }

          .file-size {
            font-size: 12px;
            color: #909399;
          }
        }

        .file-stats {
          display: flex;
          gap: 15px;
          margin-bottom: 12px;

          .stat-item {
            font-size: 12px;
            color: #909399;
            display: flex;
            align-items: center;
            gap: 4px;

            i {
              font-size: 14px;
            }
          }
        }
      }

      .file-card-footer {
        padding: 0 15px 15px 15px;
        border-top: 1px solid #f5f7fa;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .file-operations {
          .el-link {
            font-size: 12px;
          }
        }

        .file-time {
          font-size: 12px;
          color: #909399;
        }
      }
    }

    .load-more-container {
      text-align: center;
      margin: 20px 0;
    }

    .no-data {
      text-align: center;
      margin: 40px 0;
    }

    .global-loading {
      text-align: center;
      margin: 40px 0;
    }
  }

  // 响应式设计
  @media (max-width: 768px) {
    .file-card-grid {
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 15px;
    }
  }

  @media (max-width: 480px) {
    .file-card-grid {
      grid-template-columns: 1fr;
    }
  }
  </style>
