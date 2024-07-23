package demo.spring.controller;

import demo.spring.entity.Home;
import demo.spring.entity.HomeFind;
import demo.spring.service.MyHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController {
    @Autowired
    private MyHomeService myHomeService;
    @CrossOrigin
    @PostMapping("/api/home")
    public List<Home> FindHome(@RequestBody HomeFind homeFind) {
        List<Home> SendHome = new ArrayList<>();
        for(int i=0;i<12;i++){
            Home home2 = new Home(0,i+1);
            SendHome.add(home2);
        }
        if(homeFind.getType()==0){
            for (int i=0;i<myHomeService.FindHome(homeFind.getUser_id()).size();i++){
                SendHome.get(myHomeService.FindHome(homeFind.getUser_id()).get(i).getDate()-1).setCount(myHomeService.FindHome(homeFind.getUser_id()).get(i).getCount());
            }
            return SendHome;
        }
        else{
            for (int i=0;i<myHomeService.FindHomeAll().size();i++){
                SendHome.get(myHomeService.FindHomeAll().get(i).getDate()-1).setCount(myHomeService.FindHomeAll().get(i).getCount());
            }
            return SendHome;
        }
    }

}
