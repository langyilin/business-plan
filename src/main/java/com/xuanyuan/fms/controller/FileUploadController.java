package com.xuanyuan.fms.controller;

import com.xuanyuan.common.utils.PropertyHolder;
import com.xuanyuan.common.web.BaseController;
import com.xuanyuan.fms.entity.SysAttachsEntity;
import com.xuanyuan.fms.pojo.FileDesc;
import com.xuanyuan.fms.pojo.FileParam;
import com.xuanyuan.fms.service.SysAttachsServiceI;
import com.xuanyuan.fms.util.Constants;
import com.xuanyuan.fms.util.FileUtils;
import com.xuanyuan.fms.util.SFTPUtil;
import com.xuanyuan.utils.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 附件上传控制层
 * <ul>
 * <li>项目名：基础架构平台</li>
 * <li>版本信息：UM v2.0</li>
 * <li>日期：2016年7月26日-上午9:42:42</li>
 * <li>版权所有(C)2016广东轩辕网络科技股份有限公司-版权所有</li>
 * <li>创建人:姜华</li>
 * <li>创建时间：2016年7月26日-上午9:42:42</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Controller
@RequestMapping(value = "common")
public class FileUploadController extends BaseController{

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    /** 静态文件代理目录*/
    private static String staticProxyDir = PropertyHolder.getString("static.proxy.dir");
    /** 是否远程 */
    private static String isremote = PropertyHolder.getString("upload.remote");

    /** 默认上传文件类型 */
    private static final String FILE_TYPE = "*.jpg;*.jpge;*.gif;*.png;";

    @Autowired
    private SysAttachsServiceI sysAttachsService;

    @PostConstruct
    public void init(){
        Environment env = PropertyHolder.getEnv();
        if(null == env){
            logger.info("Environment is null");
        } else {
            logger.info("Environment ActiveProfiles: {}", env.getActiveProfiles());
        }
        logger.info("staticProxyDir = {}", staticProxyDir);
        logger.info("isremote = {}", isremote);
//        staticProxyDir = PropertyHolder.getString("static.proxy.dir");
//        isremote = PropertyHolder.getString("upload.remote");
    }

    /**
     * 获取附件列表
     * @param sysAttachs
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "attachList")
    public String getAttachList(SysAttachsEntity sysAttachs, HttpServletResponse response) {
        return renderString(response, sysAttachsService.findList(sysAttachs));
    }

    /**
     * 文件下载
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("download")
    public ResponseEntity<byte[]> download(String attachid, HttpServletResponse response) throws IOException {
        SysAttachsEntity attach = sysAttachsService.get(SysAttachsEntity.class, attachid);
        byte[] b = null;
        String fileName = attach.getAttachname();
        if("true".equals(isremote)) {
            SFTPUtil sftp = new SFTPUtil();
            sftp.login();
            String path = attach.getAttachpath();
            Integer index = path.lastIndexOf("/");
            b = sftp.download(staticProxyDir + path.substring(0, index), path.substring(index + 1, path.length()));
            sftp.logout();
        }else {
            String path = (Constants.ROOT_UPLOAD_FOLDER + File.separator + attach.getAttachpath()).replace("\\/", "/");
            File file = new File(path);
            b = org.apache.commons.io.FileUtils.readFileToByteArray(file);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(b, headers, HttpStatus.CREATED);
    }

    /**
     * uploadFileHandler 方法
     * @descript：TODO 单文件上传
     * @param file
     * @param param
     * @param response
     * @return
     * @throws IOException
     * @return FileDesc
     * @author 姜华
     * @date 2016年7月26日-上午9:43:03
     */
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public String uploadFileHandler(@RequestParam("file") MultipartFile file, FileParam param,
                                    HttpServletResponse response) throws IOException{
        FileDesc desc = null;
        if(!file.isEmpty()){
            if(param.getIsRemote() || "true".equals(isremote) || StringUtils.isBlank(isremote)) {
                return renderString(response, remoteUpload(file, param));
            }else {
                return renderString(response, uploadFile(file, param));
            }
        }else{
            desc = new FileDesc(false, "文件为空");
            return renderString(response, desc);
        }
    }

    /**
     * uploadMultiFileHandler 方法
     * @descript：TODO 多文件上传
     * @param files
     * @param param 上传附件参数
     * @param response
     * @return
     * @throws IOException
     * @return List<FileDesc>
     * @author 姜华
     * @date 2016年7月26日-上午9:43:17
     */
    @RequestMapping(value = "/uploadMultiFile",method = RequestMethod.POST)
    @ResponseBody
    public String uploadMultiFileHandler(@RequestParam("file") MultipartFile[] files, FileParam param,
                                         HttpServletResponse response) throws IOException{
        FileDesc desc = null;
        List<FileDesc> fileDescList = new ArrayList<FileDesc>();

        for(MultipartFile file : files){
            if(!file.isEmpty()){
                if("false".equals(isremote) || StringUtils.isBlank(isremote) || param.getIsRemote() == false) {
                    fileDescList.add(uploadFile(file, param));
                }else {
                    fileDescList.add(remoteUpload(file, param));
                }
            }else{
                desc = new FileDesc(false, "文件为空");
                fileDescList.add(desc);
            }
        }
        return renderString(response, fileDescList);
    }

    /**
     * 用于本地上传
     * uploadFile 方法
     * @descript：TODO
     * @param file
     * @param param
     * @return
     * @throws IOException
     * @return FileDesc
     * @author 姜华
     * @date 2016年8月31日-下午5:51:11
     */
    private FileDesc uploadFile(MultipartFile file, FileParam param) throws IOException {
        FileDesc desc = null;
        if(StringUtils.isNotBlank(param.getPath())) {
            param.setPath(StringEscapeUtils.unescapeJavaScript(param.getPath()));
        }
        Path folderPath = Paths.get(Constants.ROOT_UPLOAD_FOLDER + File.separator + param.getPath());
        if(Files.notExists(folderPath)){
            Files.createDirectories(folderPath);
        }
        String fileName = FileUtils.renameFileName(file.getOriginalFilename());
        String filepath = param.getPath() + "/" + fileName;
        Path filePath = Paths.get(Constants.ROOT_UPLOAD_FOLDER + File.separator + filepath);

        String fileType = param.getFileType();
        if(StringUtils.isBlank(fileType)){
            fileType = FILE_TYPE;
        }

        String suffix = getPicSuffix(filepath);
        if(StringUtils.isBlank(fileType) || fileType.indexOf(suffix) < 0) {
            desc = new FileDesc(false, "文件类型与限制类型不匹配");
            return desc;
        }

        if(!Files.notExists(filePath)){
            desc = new FileDesc(false, "文件已存在");
            return desc;
        }

        try {
            InputStream in = file.getInputStream();
            OutputStream out = Files.newOutputStream(filePath);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) > 0){
                out.write(b, 0, len);
            }
            // 数据插入附件表
            String attachid = "";

            filepath = "/attachs/" + filepath;
            if(param.getIsSave()) {
                attachid = saveAttachs(file, param, filepath);
            }
            desc = new FileDesc(true, "", filepath, attachid);
            desc.setSize(file.getSize());
            desc.setType(suffix);
            desc.setName(fileName);
            desc.setOriginalFilename(file.getOriginalFilename());
            return desc;
        }catch (IOException e){
            e.printStackTrace();
            desc = new FileDesc(false, "系统内部错误，请稍后重试");
            return desc;
        }
    }

    /**
     * 远程上传图片
     * remoteUpload 方法
     * @descript：TODO
     * @param file
     * @param param
     * @return
     * @return FileDesc
     * @author 姜华
     * @date 2016年8月31日-下午6:05:10
     */
    private FileDesc remoteUpload(MultipartFile file, FileParam param) {
        FileDesc desc = null;
        String fileName = FileUtils.renameFileName(file.getOriginalFilename());
        if(StringUtils.isNotBlank(param.getPath())) {
            param.setPath(StringEscapeUtils.unescapeJavaScript(param.getPath()));
        }
        String filepath = param.getPath() + "/" + fileName;
        String fileType = param.getFileType();
        String suffix = getPicSuffix(fileName);
        try {
            if(StringUtils.isBlank(fileType)){
                fileType = FILE_TYPE;
            }
            if(fileType.indexOf(suffix) >= 0) {
                InputStream in = file.getInputStream();
                SFTPUtil sftp = new SFTPUtil();
                sftp.login();
                sftp.upload(staticProxyDir + "/" + param.getPath(), fileName, in);;
                sftp.logout();
                // 数据插入附件表
                String attachid = "";

                //filepath = PropertyHolder.getString("static.path") + filepath;
                if(param.getIsSave()) {
                    attachid = saveAttachs(file, param, filepath);
                }
                desc = new FileDesc(true, "", filepath, attachid);
                desc.setSize(file.getSize());
                desc.setType(suffix);
                desc.setName(fileName);
                desc.setOriginalFilename(file.getOriginalFilename());
            }else {
                desc = new FileDesc(false, "文件类型与限制类型不匹配");
            }
            return desc;
        }catch (IOException e){
            e.printStackTrace();
            desc = new FileDesc(false, "系统内部错误，请稍后重试");
            return desc;
        }
    }

    /**
     *
     * saveAttachs 方法
     * @descript：TODO 保存附件到数据库
     * @param file
     * @param param
     * @param filepath
     * @return
     * @return boolean
     * @author 姜华
     * @date 2016年7月26日-下午4:02:24
     */
    private String saveAttachs(MultipartFile file, FileParam param, String filepath){
        SysAttachsEntity attachs = new SysAttachsEntity();
        try{
            attachs.setExtname(FileUtils.getExtensionName(file.getOriginalFilename()));
            attachs.setAppid(param.getAppId());
            attachs.setApptype(param.getAppType());
            attachs.setAttachsize(file.getSize());
            attachs.setUserid(param.getUserid());
            attachs.setUsername(param.getUsername());
            attachs.setActivityid(param.getActivityid());
            attachs.setShoworder(param.getShoworder());
            attachs.setAttachname(file.getOriginalFilename());
            attachs.setAttachpath(filepath);
            attachs.setCreatedate(new Date());
            sysAttachsService.save(attachs);
        }catch (Exception e){
            return null;
        }
        return attachs.getAttachid();
    }

    private static String getPicSuffix(String imPath){
        if (imPath == null || imPath.indexOf(".") == -1){
            return ""; //如果图片地址为null或者地址中没有"."就返回""
        }
        return imPath.substring(imPath.lastIndexOf(".") + 1).trim().toLowerCase();
    }

}
