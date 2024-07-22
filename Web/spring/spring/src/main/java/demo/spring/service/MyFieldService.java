package demo.spring.service;

import demo.spring.entity.FieldCaculation;
import demo.spring.mapper.MyField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("MyFieldImp")
public class MyFieldService implements MyFieldImp{
    @Autowired
    private MyField myField;
    @Override
    public List<FieldCaculation> FindField() {
        return this.myField.FindField();
    }
}
