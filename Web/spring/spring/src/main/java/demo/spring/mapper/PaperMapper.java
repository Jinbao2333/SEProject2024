package demo.spring.mapper;

import demo.spring.entity.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface PaperMapper {
    @Insert("insert into paper(title,conference,date,summary,link,type) values(#{title},#{conference},#{date},#{summary},#{link},#{type})")
    @Options(useGeneratedKeys = true, keyProperty = "paper_id", keyColumn = "paper_id")
    int addPaper(Paper paper);

    @Insert("insert into field(field_name,pid) values(#{field_name},#{pid})")
    int addField(String field_name,int pid);

    @Insert("insert into upload(user_id,upload_date,paper_id,note_id) values(#{user_id},#{upload_date},#{paper_id},#{note_id})")
    @Options(useGeneratedKeys = true, keyProperty = "upload_id", keyColumn = "upload_id")
    int addUpload(Upload upload);

    @Insert("insert into note(content) values(#{content})")
    @Options(useGeneratedKeys = true, keyProperty = "note_id", keyColumn = "note_id")
    int addNote(Note note);

    @Insert("insert ignore into cover values(#{paper_id},#{field_id})")
    int addCover(int paper_id,int field_id);

    @Insert("insert into attach_file values(#{upload_id},#{file_path})")
    int addAttach_File(int upload_id,String file_path);

    @Insert("insert into publish(author_name, paper_id) values(#{author_name},#{paper_id})")
    int addPublish(int paper_id,String author_name);

    @Insert("insert into reference values(#{paper_id},#{reference_id})")
    int addReference(int paper_id,int reference_id);

    @Insert("insert into comment(text,time,super_id,paper_id,user_id) values(#{text},#{time},#{super_id},#{paper_id},#{user_id})")
    int addComment(Comment comment);

    @Select("select * from field")
    @Results(id="fieldMap",value={
            @Result(property = "field_id",column = "field_id",javaType = Integer.class),
            @Result(property = "field_name",column = "field_name",javaType = String.class),
            @Result(property = "pid",column = "pid")
    })
    List<Field> findallField();

    @Select("select field_id from field where pid=#{id}")
    List<Integer> findFieldChildren(int id);

    @Select("select distinct paper_id from cover where field_id=#{field_id}")
    List<Integer> findPaperbyField_id(int field_id);

    @Select("select * from comment natural join user where paper_id = #{paper_id} and super_id = 0")
    @Results(id="multiCommentMap",value={
            @Result(property = "comment_id",column = "comment_id"),
            @Result(property = "text",column = "text"),
            @Result(property = "time",column = "time"),
            @Result(property = "super_id",column = "super_id"),
            @Result(property = "user_id",column = "user_id"),
            @Result(property = "name",column = "username")
    })
    List<MultiComment> findMultiComment(int paper_id);

    @Select("select * from comment natural join user where paper_id = #{paper_id} and super_id = #{super_id}")
    @Results(id="commentMap",value={
            @Result(property = "comment_id",column = "comment_id"),
            @Result(property = "text",column = "text"),
            @Result(property = "time",column = "time"),
            @Result(property = "super_id",column = "super_id"),
            @Result(property = "user_id",column = "user_id"),
            @Result(property = "name",column = "username")
    })
    List<Comment> findComment(int paper_id,int super_id);

    @Select("select paper_id,title,conference,date,group_concat(distinct author_name) as name from paper natural join cover natural join publish where field_id = #{field_id} group by paper_id limit #{offset},#{page_size}")
    @Results(id="paperfieldMap",value={
            @Result(property = "paper_id",column = "paper_id",javaType = Integer.class),
            @Result(property = "title",column = "title",javaType = String.class),
            @Result(property = "date",column = "date",javaType = String.class),
            @Result(property = "conference",column = "conference",javaType = String.class),
            @Result(property = "authors",column = "name",javaType = String.class)
    })
    List<PaperOutline> findPaperbyField(int field_id,int offset,int page_size);

    @Select("select * from paper natural join upload natural join user where paper_id=#{paper_id}")
    PaperDetail findPaperDetail(int field_id);

    @Select("select username from comment natural join user where comment_id = #{super_id}")
    String findRepliedUser(int super_id);

    @Select("select author_name from publish where paper_id = #{paper_id}")
    List<String> findAuthors(int paper_id);

    @Select("select field_id,field_name from cover natural join field where paper_id = #{paper_id}")
    @Results(id="fieldMap2",value={
            @Result(property = "field_id",column = "field_id",javaType = Integer.class),
            @Result(property = "field_name",column = "field_name",javaType = String.class)
    })
    List<Field> findFields(int paper_id);

    @Select("select field_name from cover natural join field where paper_id = #{paper_id}")
    List<String> findFieldNames(int paper_id);

    @Select("select file_path from upload natural join attach_file where paper_id = #{paper_id}")
    List<String> findFiles(int paper_id);

    @Select("select distinct paper_id from upload where user_id=#{user_id}")
    List<Integer> findPaperbyUser_id(int user_id);

    @Select("select paper_id, title from paper")
    List<RefOutline> findAllRef();

    @Select("select note_id,content from upload natural join note where paper_id = #{paper_id}")
    Note findNote(int paper_id);

    @Select("select paper.paper_id as paper_id,title from reference join paper on reference.reference_id=paper.paper_id where reference.paper_id = #{paper_id}")
    @Results(id="referenceMap",value={
            @Result(property = "paper_id",column = "paper_id",javaType = Integer.class),
            @Result(property = "title",column = "title",javaType = String.class)
    })
    List<Paper> findReferences(int paper_id);

    @Select("select pid from field where field_id=#{field_id}")
    int findFieldPid(int field_id);


    @Select("select paper_id,title,conference,date,group_concat(distinct author_name) as name from paper natural join upload natural join publish where user_id = #{user_id} group by paper_id limit #{offset},#{page_size}")
    @Results(id="paperuserMap",value={
            @Result(property = "paper_id",column = "paper_id",javaType = Integer.class),
            @Result(property = "title",column = "title",javaType = String.class),
            @Result(property = "date",column = "date",javaType = String.class),
            @Result(property = "conference",column = "conference",javaType = String.class),
            @Result(property = "authors",column = "name",javaType = String.class)
    })
    List<PaperOutline> findPaperbyUser(int user_id, int offset, int page_size);


    @Update("update field set field_name=#{field_name} where field_id=#{field_id}")
    int modifyFieldName(int field_id,String field_name);

    @Update("update comment set text='评论内容已被原作者删除',user_id=-1 where comment_id=#{comment_id}")
    int deleteComment(int comment_id);

    @Update("update comment set text=#{text},time=#{time} where comment_id=#{comment_id}")
    int modifyComment(Comment comment);

    @Update("update cover set field_id=#{pid} where field_id=#{field_id}")
    int changeFieldPid(int field_id,int pid);

    @Update("update paper set conference=#{conference},date=#{date},link=#{link},title=#{title},summary=#{summary},type=#{type} where paper_id=#{paper_id}")
    int modifyPaper(int paper_id,String conference,String date,String link,String title,String summary,String type);

    @Update("update note,upload set content=#{content} where note.note_id=upload.note_id and upload_id=#{upload_id}")
    int modifyNote(int upload_id,String content);

    @Delete("delete from field where field_id=#{field_id}")
    int deleteField(int field_id);

    @Delete("delete from cover where field_id=#{field_id}")
    int deleteCoverField(int field_id);

    @Delete("delete from publish where paper_id=#{paper_id}")
    int deletePublish(int paper_id);

    @Delete("delete from cover where paper_id=#{paper_id}")
    int deleteCover(int paper_id);

    @Delete("delete from reference where paper_id=#{paper_id}")
    int deleteReferences(int paper_id);

    @Delete("delete from comment where paper_id=#{paper_id}")
    int deleteCommentbyPaper(int paper_id);

    @Delete("delete attach_file from attach_file,upload where attach_file.upload_id=upload.upload_id and paper_id=#{paper_id}")
    int deleteFiles(int paper_id);

    @Delete("delete from upload where paper_id=#{paper_id}")
    int deleteUpload(int paper_id);

    @Delete("delete from paper where paper_id=#{paper_id}")
    int deletePaper(int paper_id);

    @Delete("delete note,upload from note,paper,upload where upload.note_id=note.note_id and paper.paper_id=upload.paper_id and paper.paper_id=#{paper_id}")
    int deleteNoteandUpload(int paper_id);
}
