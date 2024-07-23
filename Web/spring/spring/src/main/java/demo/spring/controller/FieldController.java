package demo.spring.controller;

import demo.spring.entity.FieldCaculation;
import demo.spring.service.MyFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FieldController {
    @Autowired
    private MyFieldService myFieldService;
    @CrossOrigin
    @PostMapping("/api/Field")
    public List<FieldCaculation> FindField(){
        System.out.println("steadfast");
        List<FieldCaculation> fieldCaculation = new ArrayList<>();
        for(int i=0;i<myFieldService.FindField().size();i++){
            if(!myFieldService.FindField().get(i).getName().equals("全部方向")){
                fieldCaculation.add(myFieldService.FindField().get(i));
            }
        }
        return fieldCaculation;
    }
}
