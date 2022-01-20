package com.intelj.StudentManagement.Services;

import java.util.Objects;

import com.intelj.StudentManagement.Repository.MongoDBSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;


@Service
public class SequenceGeneratorService {

    @Autowired
    private MongoOperations mongoOperations;


    //logic to generate id in integer
    public int getSequenceNumber(String seqNumber)
    {
        //get sequence number from the db_sequence database
        Query query = new Query(Criteria.where("id").is(seqNumber));

        //update or increment by one
        Update update = new Update().inc("seqNo",1);

        MongoDBSequence counter = mongoOperations.findAndModify(query, update, options().returnNew(true).upsert(true),MongoDBSequence.class);

        return !Objects.isNull(counter)?counter.getSeqNo():1;
    }
}
