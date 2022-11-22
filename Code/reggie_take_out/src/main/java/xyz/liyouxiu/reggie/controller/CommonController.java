package xyz.liyouxiu.reggie.controller;

/**
 * @author liyouxiu
 * @date 2022/11/22 10:25
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.liyouxiu.reggie.common.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * 文件的上传与下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件的上传
     * 文件名要和前台保持一致
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info("文件的上传{}",file.toString());
        //获取原始上传的文件名
        String originalFilename = file.getOriginalFilename();
        //使用UUID重写生成文件名称，防止文件名称重复造成文件覆盖
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+suffix;
        //创建目录对象
        File dir=new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //不存在需要创建
            dir.mkdir();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //输入流，读取文件内容
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            byte[] bytes=new byte[1024];
            int len=0;
            while((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
