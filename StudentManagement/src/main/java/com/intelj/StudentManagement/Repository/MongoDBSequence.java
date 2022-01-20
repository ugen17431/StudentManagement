package com.intelj.StudentManagement.Repository;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "db_sequence")
@NoArgsConstructor
@Getter
public class MongoDBSequence {

    @Id
    private String id; //sequence number
    private int seqNo;


}
