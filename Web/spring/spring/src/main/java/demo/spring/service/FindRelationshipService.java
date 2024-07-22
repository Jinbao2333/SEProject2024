package demo.spring.service;

import demo.spring.entity.relationship;
import demo.spring.mapper.Myrelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("FindRelationshipImp")
public class FindRelationshipService implements FindRelationshipImp{
    @Autowired
    private Myrelation myrelation;
    @Override
    public relationship Findrelationship(int paper_id){
        return myrelation.Findrelationship(paper_id);
    }
}
