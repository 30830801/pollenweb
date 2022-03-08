package com.pollen.pollenweb.controller.file;

import com.pollen.pollenweb.result.JsonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.ValueConstants;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("file")
@Slf4j
public class FileController {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/");


    @Value("${file.fileUploadDir}")
    private String uploadFilePath;

    @Value("${file.fileUploadDir}")
    private String downloadFilePath;

    @Value("${file.fileUploadDir}")
    private String deleteFilePath;

    @RequestMapping("/upload")
    public JsonResult httpUpload(@RequestParam("files") MultipartFile files[]){
        String url = uploadFilePath;
        for(int i=0;i<files.length;i++){
            String fileName = files[i].getOriginalFilename();  // 文件名
            File dest = new File(uploadFilePath +'/'+ fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                files[i].transferTo(dest);
            } catch (Exception e) {
                log.error("{}",e);
                return JsonResult.builder().data(e).message("上传异常！").code("-1").build();
            }
        }
        return JsonResult.builder().data(url).message("上传成功！").code("1").build();
    }

    @RequestMapping("/download")
    public JsonResult fileDownLoad(HttpServletResponse response, @RequestParam("fileName") String fileName){
        File file = new File(downloadFilePath +'/'+ fileName);
        if(!file.exists()){
            return JsonResult.builder().data(null).message("下载文件不存在！").code("-2").build();
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName );

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os  = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            log.error("{}",e);
            return JsonResult.builder().data(e).message("下载失败！").code("-3").build();
        }
        return JsonResult.builder().data(null).message("下载成功").code("2").build();
    }

//    @Scheduled(cron="0 0 3 * * ?")
    private void deleteFiles(){
        deleteFile(new File(deleteFilePath));
    }

    public void deleteFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            log.info("暂无文件");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            //打印文件名
            String name = f.getName();
            log.info(name);
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()){
                deleteFile(f);
            }else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }


}