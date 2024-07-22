package demo.spring.service;

import demo.spring.entity.parper;
import demo.spring.mapper.MySearpaper;
import demo.spring.mapper.PaperMapper;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mySearchService")
public class MySearchServiceImp implements MySearchService{
    @Autowired
    private MySearpaper mySearpaper;
    @Autowired
    private PaperMapper paperMapper;
    private List<parper> findAuthorsandFields(List<parper> res){
        for(int i=0;i<res.size();i++){
            res.get(i).setAuthor(this.paperMapper.findAuthors(res.get(i).getPaper_id()));
        }
        for(int i=0;i<res.size();i++){
            res.get(i).setFields(this.paperMapper.findFieldNames(res.get(i).getPaper_id()));
        }
        return res;
    }
    @Override
    public List<parper> Findpaper1(int paper_id,int Page,int Time) {
        return findAuthorsandFields(this.mySearpaper.Findpaper1(paper_id,Page,Time));
    }

    @Override
    public List<parper> Findpaper2(String title,int Page,int Time) {
        return findAuthorsandFields(this.mySearpaper.Findpaper2(title,Page,Time));
    }

    @Override
    public List<parper> Findpaper3(String name,int Page,int Time) {
        return findAuthorsandFields(this.mySearpaper.Findpaper3(name,Page,Time));
    }

    @Override
    public List<parper> Findpaper4(String summary,int Page,int Time) {
        return findAuthorsandFields(this.mySearpaper.Findpaper4(summary,Page,Time));
    }

    @Override
    public List<parper> Findpaper5(String type,int Page,int Time) { return findAuthorsandFields(this.mySearpaper.Findpaper5(type,Page,Time)); }
}
