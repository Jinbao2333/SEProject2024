package demo.spring.mapper;

import demo.spring.entity.relationship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface Myrelation {
    @Select("select user_id from upload where paper_id = #{paper_id}")
    @Results(id="relationship",value={
            @Result(property = "user_id",column = "user_id",javaType = Integer.class),
    })
    relationship Findrelationship(int paper_id);
}
