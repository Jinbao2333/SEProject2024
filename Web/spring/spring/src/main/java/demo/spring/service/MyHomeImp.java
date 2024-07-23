package demo.spring.service;

import demo.spring.entity.Home;

import java.util.List;

public interface MyHomeImp {
    public List<Home> FindHome(int user_id);
    public List<Home> FindHomeAll();

}
