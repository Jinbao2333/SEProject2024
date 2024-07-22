package demo.spring.mapper;

import demo.spring.entity.FieldCaculation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface MyField {
    @Select("select count(*) as value,field_name as name from cover join field on cover.field_id = field.field_id group by cover.field_id")
    @Results(id="Field",value={
            @Result(property = "value",column = "value",javaType = Integer.class),
            @Result(property = "name",column = "name",javaType = String.class),

    })
    List<FieldCaculation> FindField();
}
