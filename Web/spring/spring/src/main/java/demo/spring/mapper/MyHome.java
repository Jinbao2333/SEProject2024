package demo.spring.mapper;

import demo.spring.entity.Home;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface MyHome {
    @Select("select count(*) as count,month(upload_date) as mouths from upload where user_id = #{user_id} group by mouths" )
    @Results(id="Home",value={
            @Result(property = "count",column = "count",javaType = Integer.class),
            @Result(property = "Date",column = "mouths",javaType = Integer.class),

    })
    List<Home> FindHome(int user_id);
    @Select("select count(*) as count,month(upload_date) as mouths from upload group by mouths")
    @Results(id="HomeAll",value={
            @Result(property = "count",column = "count",javaType = Integer.class),
            @Result(property = "Date",column = "mouths",javaType = Integer.class),

    })
    List<Home> FindHomeAll();

}
