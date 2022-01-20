package com.intelj.StudentManagement;


import com.intelj.StudentManagement.Models.Student;
import com.intelj.StudentManagement.Repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import com.intelj.StudentManagement.Services.SequenceGeneratorService;

import java.util.Date;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class RepositoryIntegrationTest {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    static  int id ;
    //to test add student in repository
    @Test
    @Rollback(value = false)
    @Order(1)
    void addStudentTest()
    {
        //creating new student
        Student student = new Student();
        student.setStudentName("TestUser");

        student.setDateOfAdmission(new Date());

        //creating course array
        Student.Course[] courseArr = new Student.Course[2];
        courseArr[0] = new Student.Course(1,"Java");
        courseArr[1] = new Student.Course(2,"Python");
        student.setCourseList(courseArr);

        student.setStudentId(sequenceGeneratorService.getSequenceNumber(Student.SEQUENCE_NO));

        //saving student to repository
        Student addedStudent = studentRepository.save(student);
        id = addedStudent.getStudentId();

        //Testing
        Assertions.assertNotNull(addedStudent);
    }

    //test to update student
    @Test
    @Order(2)
    @Rollback(value = false)
    void updateStudentTest()
    {
        Student student = studentRepository.findById(id).get();
       student.setStudentName("Test User 2");
        Student updatedStudent = studentRepository.save(student);
        System.out.println("Update "+updatedStudent);
        Assertions.assertEquals("Test User 2",updatedStudent.getStudentName());
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    void getStudentByIdTest()
    {
        Optional<Student> s = studentRepository.findById(id);
        Assertions.assertTrue(s.isPresent());
    }



    //test to test all students
    @Test
    @Order(4)
    @Rollback(value = false)
    void getAllStudentsTest()
    {
        Assertions.assertNotEquals(0,studentRepository.findAll().size());
    }





    //to test delete student by id in repository
    @Test
    @Order(5)
    @Rollback(value = false)
    void deleteStudentByIdTest()
    {
        studentRepository.deleteById(id);
        Assertions.assertFalse(studentRepository.findById(id).isPresent());
    }
}
