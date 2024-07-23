package demo.spring.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.org.apache.xpath.internal.operations.Mult;
import demo.spring.entity.Comment;
import demo.spring.entity.Field;
import demo.spring.entity.Result;
import demo.spring.entity.relationship;
import demo.spring.service.FindRelationshipService;
import demo.spring.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class PaperController {
    @Autowired
    private PaperService paperService;
    @Autowired
    private FindRelationshipService findRelationshipService;
    @CrossOrigin
    @PostMapping("/api/paper/upload")
    public Result upload(@RequestBody UploadRequest uploadRequest) {
        try {
                this.paperService.upload(uploadRequest);
            } catch (Exception e) {
                return Result.fail("添加论文失败", null);
            }
        return Result.success("成功",uploadRequest.paper_merge());
    }

    @CrossOrigin
    @PostMapping("/api/paper/modify")
    public Result modifypaper(@RequestBody ModifyPaperRequest request) {
        try {
            this.paperService.modifyPaper(request);
        } catch (Exception e) {
            return Result.fail("修改失败", null);
        }
        return Result.success("修改成功",null);
    }

    @CrossOrigin
    @PostMapping("/api/allfield")
    public Result allfield() {
        List<Field> res=new ArrayList<Field>();
        try {
            res=this.paperService.findallField();
        } catch (Exception e) {
            return Result.fail("查询失败", null);
        }
        return Result.success("查询成功",res);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/comment")
    public Result comment(@RequestBody Comment comment) {
        try {
            paperService.addComment(comment);
            return Result.success("评论成功",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("评论失败",null);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/getcomment")
    public Result comment(@RequestParam("paper_id") Integer paper_id) {
        try {
            return Result.success("查询成功",this.paperService.findComment(paper_id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("查询失败",null);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/file/upload")
    public Result uploadfile(MultipartHttpServletRequest request){
        List<MultipartFile> files = request.getFiles("file");
        List<String> res=new ArrayList<String>();
        try {
            SFTPConfigModel sftpConfigModel=new SFTPConfigModel();
            SFTPUtil sftpUtil=new SFTPUtil(sftpConfigModel.getDefaultConfig());
            sftpUtil.login();
            System.out.println(files.get(0).getOriginalFilename());
            for(MultipartFile file:files){
                System.out.println(file.getOriginalFilename());
                String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String fileName = UUID.randomUUID().toString().replace("-","") + "@" + file.getOriginalFilename();
                sftpUtil.upload(sftpConfigModel.getUploadUrl(),fileName,file.getInputStream());
                res.add(fileName);
            }
            sftpUtil.logout();
            return Result.success("成功",res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("失败",null);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/file/download")
    public byte[] downloadfile(@RequestBody DownloadRequest downloadRequest){
        try {
            SFTPConfigModel sftpConfigModel=new SFTPConfigModel();
            SFTPUtil sftpUtil=new SFTPUtil(sftpConfigModel.getDefaultConfig());
            sftpUtil.login();
            byte[] res=sftpUtil.download(sftpConfigModel.getUploadUrl(),downloadRequest.getServerfile());
            //sftpUtil.logout();

            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("batchUpload")
    public String batchUpload(MultipartHttpServletRequest request){
        List<MultipartFile> files = request.getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); i++) {
            file = files.get(i);
            String filePath = "f:/upload/";
            if (!file.isEmpty()){
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(filePath+file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
                } catch (IOException e) {
                    stream = null;
                    return "第"+i+"个文件上传失败："+e.getMessage();
                }
            }else {
                return "第"+i+"个文件上传失败因为文件为空";
            }
        }
        return "上传成功";
    }

    @CrossOrigin
    @RequestMapping(value = "/api/findpaperbyfield")
    public Result findpaperbyfield(@RequestParam("field_id") Integer field_id,
                                   @RequestParam("page_no") Integer page_no,
                                   @RequestParam("page_size") Integer page_size) {
        try {
            return Result.success("查询成功",this.paperService.findPaperbyField(field_id,page_no,page_size));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("查询失败",null);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/paper")
    public Result findpaperbyfield(@RequestParam("paper_id") Integer paper_id) {
        try {
            return Result.success("查询成功",this.paperService.findPaperDetail(paper_id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("查询失败",null);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/comment/delete")
    public Result deletecomment(@RequestParam("comment_id") Integer comment_id) {
        try {
            return Result.success("删除评论成功",this.paperService.deleteComment(comment_id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除评论失败",null);
    }

    @CrossOrigin
    @PostMapping(value="/api/paper/delete")
    public Result deletepaper(@RequestParam(value = "paper_id") Integer paper_id,@RequestParam(value="user_id") Integer user_id,HttpServletRequest servletRequest) {
        HttpSession session=servletRequest.getSession();
        int role_id=(Integer) session.getAttribute("admin");
        try {
            if(findRelationshipService.Findrelationship(paper_id).getUser_id() == user_id||role_id==1)
                return Result.success("删除论文成功",this.paperService.deletePaper(paper_id));
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.fail("改论文已被引用",null);
        }
        return Result.fail("您不是该论文的上传者",null);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/comment/modify")
    public Result modifycomment(@RequestBody Comment comment) {
        try {
            return Result.success("修改评论成功",this.paperService.modifyComment(comment));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改评论失败",null);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/findpaperbyuser")
    public Result findpaperbyuser(@RequestParam("user_id") Integer user_id,
                                  @RequestParam("page_no") Integer page_no,
                                  @RequestParam("page_size") Integer page_size) {
        try {
            return Result.success("查询成功",this.paperService.findPaperbyUser(user_id,page_no,page_size));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("查询失败",null);
    }
    @CrossOrigin
    @RequestMapping(value = "/api/findRef")
    public Result findRef() {
        try {
            return Result.success("查询成功",this.paperService.findAllRef());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("查询失败",null);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/getsession")
    public Object getsession(HttpSession session) {
        return session.getAttribute("role");
    }
}
