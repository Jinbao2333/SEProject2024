package demo.spring.service;

import demo.spring.controller.ModifyPaperRequest;
import demo.spring.controller.UploadRequest;
import demo.spring.entity.*;
import demo.spring.mapper.PaperMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("paperService")
public class PaperServiceImp implements PaperService{
    @Autowired
    private PaperMapper paperMapper;

    public int addPaper(Paper paper){
        return this.paperMapper.addPaper(paper);
    }

    @Override
    public List<Field> findallField(){
        List<Field> fields=this.paperMapper.findallField();
        return loopQuery(null,fields);
    }

    private List<Field> loopQuery(Integer pid,List<Field> allFields){
        List<Field> fieldList=new ArrayList<Field>();
        for(Field field:allFields){
            if(pid==null){
                if(field.getPid()==null){
                    fieldList.add(field);
                    field.setChildren(loopQuery(field.getField_id(),allFields));
                }
            }
            else if(pid==field.getPid()){
                fieldList.add(field);
                field.setChildren(loopQuery(field.getField_id(),allFields));
            }
        }
        return fieldList;
    }

    @Override
    public int addField(Field field){
        return this.paperMapper.addField(field.getField_name(),field.getPid());
    }

    @Override
    public int modifyFieldName(Field field){
        return this.paperMapper.modifyFieldName(field.getField_id(),field.getField_name());
    }

    @Override
    public int deleteField(int field_id){
        int pid=this.paperMapper.findFieldPid(field_id);
        if(this.paperMapper.findPaperbyField_id(field_id).size()!=0 && pid == 0){
            return 1;
        }
        if(this.paperMapper.findFieldChildren(field_id).size()!=0)
            return 2;
        return this.doDeleteField(field_id,pid);
    }
    @Transactional
    public int doDeleteField(int field_id,int pid){
        //this.paperMapper.changeFieldPid(field_id,pid);
        this.paperMapper.deleteCoverField(field_id);
        this.paperMapper.deleteField(field_id);
        return 0;
    }

    public int addNote(Note note){return this.paperMapper.addNote(note);}

    public int addUpload(Upload upload){return this.paperMapper.addUpload(upload);}

    public int addCover(int paper_id,int field_id){return this.paperMapper.addCover(paper_id,field_id);}

    public int addAttach_File(int upload_id,String file_path){return this.paperMapper.addAttach_File(upload_id,file_path);}

    public int addPublish(int paper_id,String author_name){return this.paperMapper.addPublish(paper_id,author_name);}

    public int addReference(int paper_id,int reference_id){return this.paperMapper.addReference(paper_id,reference_id);}

    @Override
    @Transactional
    public int upload(UploadRequest uploadRequest){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = df.format(date);
        Paper paper=uploadRequest.paper_merge();
        this.addPaper(paper);
        Note note=new Note();
        note.setContent(uploadRequest.getContent());
        this.addNote(note);
        Upload upload=new Upload();
        upload.setPaper_id(paper.getPaper_id());
        upload.setNote_id(note.getNote_id());
        upload.setUser_id(uploadRequest.getUser_id());
        upload.setUpload_date(str);
        this.addUpload(upload);
        for(int i=0;i<uploadRequest.getAuthors().size();i++){
            this.addPublish(paper.getPaper_id(),uploadRequest.getAuthors().get(i));
        }
        for(int i=0;i<uploadRequest.getField().size();i++) {
            this.addCover(paper.getPaper_id(),uploadRequest.getField().get(i));
        }
        for(int i=0;i<uploadRequest.getReferences().size();i++){
            this.addReference(paper.getPaper_id(),uploadRequest.getReferences().get(i));
        }
        for(int i=0;i<uploadRequest.getFileList().size();i++) {
            this.addAttach_File(upload.getUpload_id(),uploadRequest.getFileList().get(i));
        }
        return 0;
    }

    @Transactional
    public int deletePaper(int paper_id){
        this.paperMapper.deletePublish(paper_id);
        this.paperMapper.deleteCover(paper_id);
        this.paperMapper.deleteCommentbyPaper(paper_id);
        this.paperMapper.deleteFiles(paper_id);
        this.paperMapper.deleteReferences(paper_id);
        this.paperMapper.deleteNoteandUpload(paper_id);
        this.paperMapper.deletePaper(paper_id);
        return 0;
    }

    @Transactional
    public int modifyPaper(ModifyPaperRequest request){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = df.format(date);
        this.paperMapper.modifyPaper(request.getOrigin_paper_id(),request.getConference(),request.getDate(),request.getLink(),request.getTitle(),request.getSummary(),request.getType());
        this.paperMapper.modifyNote(request.getOrigin_upload_id(),request.getContent());
        //this.paperMapper.modifyUploadTime(request.getOrigin_upload_id(),str);
        this.paperMapper.deletePublish(request.getOrigin_paper_id());
        for(int i=0;i<request.getAuthors().size();i++){
            this.addPublish(request.getOrigin_paper_id(),request.getAuthors().get(i));
        }
        this.paperMapper.deleteCover(request.getOrigin_paper_id());
        for(int i=0;i<request.getField().size();i++) {
            this.addCover(request.getOrigin_paper_id(),request.getField().get(i));
        }
        this.paperMapper.deleteReferences(request.getOrigin_paper_id());
        for(int i=0;i<request.getReferences().size();i++){
            this.addReference(request.getOrigin_paper_id(),request.getReferences().get(i));
        }
        this.paperMapper.deleteFiles(request.getOrigin_paper_id());
        for(int i=0;i<request.getFileList().size();i++) {
            this.addAttach_File(request.getOrigin_upload_id(),request.getFileList().get(i));
        }
        return 0;
    }

    public int addComment(Comment comment){
        return this.paperMapper.addComment(comment);
    }

    public List<MultiComment> findComment(int paper_id){
        List<MultiComment> res=this.paperMapper.findMultiComment(paper_id);
        List<Comment> comments;
        for(int i=0;i<res.size();i++){
            comments=this.paperMapper.findComment(paper_id,res.get(i).getComment_id());
            for(int j=0;j<comments.size();j++){
                comments.get(j).setTo(this.paperMapper.findRepliedUser(comments.get(j).getSuper_id()));
            }
            res.get(i).setReply(comments);
        }
        return res;
    }

    public List<Object> findPaperbyField(int field_id, int page_no, int page_size){
        Integer page_total=this.paperMapper.findPaperbyField_id(field_id).size();
        List<PaperOutline> paperOutlines=this.paperMapper.findPaperbyField(field_id,page_no*page_size,page_size);
        List<Object> res = new ArrayList<Object>();
        res.add(page_total);
        res.add(paperOutlines);
        return res;
    }
    public List<Object> findPaperbyUser(int user_id, int page_no, int page_size){
        Integer page_total=this.paperMapper.findPaperbyUser_id(user_id).size();
        List<PaperOutline> paperOutlines=this.paperMapper.findPaperbyUser(user_id,page_no*page_size,page_size);
        List<Object> res = new ArrayList<Object>();
        res.add(page_total);
        res.add(paperOutlines);
        return res;
    }

    public PaperDetail findPaperDetail(int paper_id){
        PaperDetail paperDetail=this.paperMapper.findPaperDetail(paper_id);
        paperDetail.setAuthors(this.paperMapper.findAuthors(paper_id));
        Note note=this.paperMapper.findNote(paper_id);
        paperDetail.setNote_id(note.getNote_id());
        paperDetail.setContent(note.getContent());
        paperDetail.setFields(this.paperMapper.findFields(paper_id));
        paperDetail.setReferences(this.paperMapper.findReferences(paper_id));
        paperDetail.setFiles(this.paperMapper.findFiles(paper_id));
        return paperDetail;
    }

    public int deleteComment(int comment_id){
        return this.paperMapper.deleteComment(comment_id);
    }

    public int modifyComment(Comment comment){
        return this.paperMapper.modifyComment(comment);
    }

    @Override
    public List<Object> findAllRef() {
        List<RefOutline> RefOutlines=this.paperMapper.findAllRef();
        List<Object> res = new ArrayList<Object>();
        res.add(RefOutlines);
        return res;
    }

}
