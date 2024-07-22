package demo.spring.controller;
import demo.spring.entity.parper;
import demo.spring.service.MySearchServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchContrller {
    @Autowired
    private MySearchServiceImp mySearchServiceImp;
    @CrossOrigin
    @RequestMapping(value = "/api/search")
    public List<parper> Findpaper_id(@RequestParam(value = "Page") Integer Page,
                                     @RequestParam(value = "Time") Integer Time,
                                     @RequestParam(value ="value1") String value1,
                                     @RequestParam(value = "paper_id1") String paper_id1) {
        System.out.println(Page+Time);
        if(value1.equals("版号"))
        {
            return mySearchServiceImp.Findpaper1(Integer.parseInt(paper_id1), Page*Time,Time);
        }
        else if(value1.equals("2")){
            return mySearchServiceImp.Findpaper2(paper_id1, Page*Time,Time);
        }
        else if(value1.equals("3"))
        {
            return mySearchServiceImp.Findpaper3(paper_id1, Page*Time,Time);
        }
        else if(value1.equals("4"))
        {
            return mySearchServiceImp.Findpaper4(paper_id1, Page*Time,Time);
        }
        else if(value1.equals("5"))
        {
            return mySearchServiceImp.Findpaper5(paper_id1, Page*Time,Time);
        }
        return null;
    }
}
