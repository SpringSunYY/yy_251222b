<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编号" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型名称" prop="fileTypeName">
        <el-input
          v-model="queryParams.fileTypeName"
          placeholder="请输入类型名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件名称" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文件名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否同意" prop="isAgree">
        <el-select v-model="queryParams.isAgree" placeholder="请选择是否同意" clearable>
          <el-option
            v-for="dict in dict.type.is_agree"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审核时间">
        <el-date-picker
          v-model="daterangeAuditTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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

    <el-row :gutter="10" class="mb8">
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="primary"-->
      <!--          plain-->
      <!--          icon="el-icon-plus"-->
      <!--          size="mini"-->
      <!--          @click="handleAdd"-->
      <!--          v-hasPermi="['manage:auditInfo:add']"-->
      <!--        >新增-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manage:auditInfo:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manage:auditInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['manage:auditInfo:import']"
        >导入
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manage:auditInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="auditInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="编号" align="center" v-if="columns[0].visible" prop="id"/>
      <el-table-column label="用户编号" :show-overflow-tooltip="true" align="center" v-if="columns[1].visible"
                       prop="userName"/>
      <el-table-column label="类型编号" :show-overflow-tooltip="true" align="center" v-if="columns[2].visible"
                       prop="fileTypeId"/>
      <el-table-column label="类型名称" :show-overflow-tooltip="true" align="center" v-if="columns[3].visible"
                       prop="fileTypeName"/>
      <el-table-column label="文件编号" :show-overflow-tooltip="true" align="center" v-if="columns[4].visible"
                       prop="fileId"/>
      <el-table-column label="文件名称" :show-overflow-tooltip="true" align="center" v-if="columns[5].visible"
                       prop="fileName"/>
      <el-table-column label="文件类型" :show-overflow-tooltip="true" align="center" v-if="columns[6].visible"
                       prop="fileType"/>
      <el-table-column label="文件大小" :show-overflow-tooltip="true" align="center" v-if="columns[7].visible"
                       prop="fileSize">
        <template slot-scope="scope">
          <div v-if="scope.row.fileSize">
            {{ formatFileSize(scope.row.fileSize) }}
          </div>
          <div v-else>
            -
          </div>
        </template>
      </el-table-column>
      <el-table-column label="文件" align="center" v-if="columns[8].visible" prop="fileUrl" width="100">
        <template slot-scope="scope">
          <div v-if="scope.row.fileUrl">
            <el-tooltip placement="top" effect="light">
              <div slot="content">
                <div v-for="(file,index) in scope.row.fileUrl.split(',')"
                     :key="index"
                     style="text-align: left;padding: 5px;">
                  <div style="display: flex; align-items: center; gap: 10px;">
                    <span style="cursor: pointer;"> {{ getFileName(file) }} </span>
                    <el-link
                      :download="getFileName(file)"
                      :href="getFilePath(file)"
                      :underline="false"
                      target="_blank"
                      style="font-size: 14px"
                    >
                      <span style="cursor: pointer;"> 下载 </span>
                    </el-link>
                    <el-link
                      :underline="false"
                      :style="{ fontSize: '12px', color: canPreviewFile(file) ? '#67C23A' : '#909399' }"
                      @click="previewFile(file)"
                      :title="canPreviewFile(file) ? '在新标签页中预览文件' : '该文件类型暂不支持预览'"
                      target="_blank"
                    >
                      {{ canPreviewFile(file) ? '预览' : '查看' }}
                    </el-link>
                  </div>
                </div>
              </div>
              <span style="cursor: pointer; color: #409EFF;">文件</span>
            </el-tooltip>
          </div>
          <div v-else>
            -
          </div>
        </template>
      </el-table-column>
      <el-table-column label="申请理由" :show-overflow-tooltip="true" align="center" v-if="columns[9].visible"
                       prop="applyReason"/>
      <el-table-column label="审核人" :show-overflow-tooltip="true" align="center" v-if="columns[10].visible"
                       prop="auditUserName"/>
      <el-table-column label="审核内容" :show-overflow-tooltip="true" align="center" v-if="columns[11].visible"
                       prop="content"/>
      <el-table-column label="是否同意" align="center" v-if="columns[12].visible" prop="isAgree">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.is_agree" :value="scope.row.isAgree"/>
        </template>
      </el-table-column>
      <el-table-column label="审核时间" align="center" v-if="columns[13].visible" prop="auditTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.auditTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" v-if="columns[14].visible" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" v-if="columns[15].visible" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manage:auditInfo:edit']"
          >审核
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manage:auditInfo:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改审核信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件编号" prop="fileId">
          <el-input v-model="form.fileId" placeholder="请输入文件编号"/>
        </el-form-item>
        <el-form-item label="申请理由" prop="applyReason">
          <el-input v-model="form.applyReason" placeholder="请输入申请理由"/>
        </el-form-item>
        <el-form-item label="审核内容">
          <el-input type="textarea" v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="是否同意" prop="isAgree">
          <el-select v-model="form.isAgree" placeholder="请选择是否同意">
            <el-option
              v-for="dict in dict.type.is_agree"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 审核信息导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
                 :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
                 :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport"/>
            是否更新已经存在的审核信息数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
                   @click="importTemplate">下载模板
          </el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listAuditInfo,
  getAuditInfo,
  delAuditInfo,
  addAuditInfo,
  updateAuditInfo,
  importAuditInfo,
  importTemplateAuditInfo
} from "@/api/manage/auditInfo";
import {getToken} from "@/utils/auth";
import {formatFileSize} from "../../../utils/ruoyi";

export default {
  name: "AuditInfo",
  dicts: ['is_agree'],
  data() {
    return {
      //表格展示列
      columns: [
        {key: 0, label: '编号', visible: false},
        {key: 1, label: '用户编号', visible: true},
        {key: 2, label: '类型编号', visible: false},
        {key: 3, label: '类型名称', visible: true},
        {key: 4, label: '文件编号', visible: false},
        {key: 5, label: '文件名称', visible: true},
        {key: 6, label: '文件类型', visible: true},
        {key: 7, label: '文件大小', visible: true},
        {key: 8, label: '文件', visible: true},
        {key: 9, label: '申请理由', visible: true},
        {key: 10, label: '审核人', visible: true},
        {key: 11, label: '审核内容', visible: true},
        {key: 12, label: '是否同意', visible: true},
        {key: 13, label: '审核时间', visible: true},
        {key: 14, label: '创建时间', visible: true},
        {key: 15, label: '更新时间', visible: false},
      ],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 审核信息表格数据
      auditInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 更新时间时间范围
      daterangeAuditTime: [],
      // 更新时间时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        fileTypeName: null,
        fileName: null,
        fileType: null,
        fileUrl: null,
        isAgree: null,
        auditTime: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      // 导出地址
      exportUrl: 'manage/auditInfo/export',
      // 审核信息导入参数
      upload: {
        // 是否显示弹出层（审核信息导入）
        open: false,
        // 弹出层标题（审核信息导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的审核信息数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/manage/auditInfo/importData",
        // 下载模板的地址
        templateUrl: 'manage/auditInfo/importTemplate'
      },
      // 表单校验
      rules: {
        userId: [
          {required: true, message: "用户编号不能为空", trigger: "blur"}
        ],
        fileTypeId: [
          {required: true, message: "类型编号不能为空", trigger: "blur"}
        ],
        fileTypeName: [
          {required: true, message: "类型名称不能为空", trigger: "blur"}
        ],
        fileId: [
          {required: true, message: "文件编号不能为空", trigger: "blur"}
        ],
        fileName: [
          {required: true, message: "文件名称不能为空", trigger: "blur"}
        ],
        fileType: [
          {required: true, message: "文件类型不能为空", trigger: "change"}
        ],
        fileSize: [
          {required: true, message: "文件大小不能为空", trigger: "blur"}
        ],
        fileUrl: [
          {required: true, message: "文件不能为空", trigger: "blur"}
        ],
        applyReason: [
          {required: false, message: "申请理由不能为空", trigger: "blur"}
        ],
        isAgree: [
          {required: true, message: "是否同意不能为空", trigger: "change"}
        ],
        createTime: [
          {required: true, message: "创建时间不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    formatFileSize,
    /** 查询审核信息列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeAuditTime && '' != this.daterangeAuditTime) {
        this.queryParams.params["beginAuditTime"] = this.daterangeAuditTime[0];
        this.queryParams.params["endAuditTime"] = this.daterangeAuditTime[1];
      }
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listAuditInfo(this.queryParams).then(response => {
        this.auditInfoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        userId: null,
        fileTypeId: null,
        fileTypeName: null,
        fileId: null,
        fileName: null,
        fileType: null,
        fileSize: null,
        fileUrl: null,
        applyReason: null,
        auditUserId: null,
        content: null,
        isAgree: null,
        auditTime: null,
        createTime: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeAuditTime = [];
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加审核信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAuditInfo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改审核信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAuditInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAuditInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除审核信息编号为"' + ids + '"的数据项？').then(function () {
        return delAuditInfo(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(this.exportUrl, {
        ...this.queryParams
      }, `auditInfo_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "审核信息导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download(this.upload.templateUrl, {}, `auditInfo_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 获取文件名
    getFileName(fileUrl) {
      if (fileUrl.lastIndexOf("/") > -1) {
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
