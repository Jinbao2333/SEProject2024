package demo.spring.service;

import demo.spring.entity.Home;
import demo.spring.mapper.MyHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MyHomeImp")
public class MyHomeService implements MyHomeImp{
    @Autowired
    private MyHome myhome;
    @Override
    public List<Home> FindHome(int use_id){return this.myhome.FindHome(use_id);}

    @Override
    public List<Home> FindHomeAll() {return this.myhome.FindHomeAll();}

}
