package com.intelj.StudentManagement.Repository;

import com.intelj.StudentManagement.Models.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepository extends MongoRepository<Student,Integer>{

}
