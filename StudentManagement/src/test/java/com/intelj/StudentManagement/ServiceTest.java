package com.intelj.StudentManagement;

import com.intelj.StudentManagement.Models.Student;
import com.intelj.StudentManagement.Repository.StudentRepository;
import com.intelj.StudentManagement.Services.SequenceGeneratorService;
import com.intelj.StudentManagement.Services.StudentService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class ServiceTest {


    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentservice;
    @MockBean
    private SequenceGeneratorService sequenceGeneratorService;
    static Student s1 = new Student(1,"xyz",new Date(),
            new Student.Course[]{new Student.Course(1,"Java"),
                    new Student.Course(2,"Python")});
    static Student s2 = new Student(2,"pqr",new Date(),
            new Student.Course[]{new Student.Course(1,"C"),
                    new Student.Course(2,"C++")});

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

    }
    @Test
    void addStudentServiceTest()
    {
        Mockito.when(sequenceGeneratorService.getSequenceNumber(ArgumentMatchers.anyString())).thenReturn(1);

        Mockito.when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(s1);

        Student addedStudent = studentservice.addStudent(s1);

        Assertions.assertEquals(s1,addedStudent);
    }

    @Test
    void getAllStudentServiceTest()
    {

        List<Student> studentArr = new ArrayList<Student>();
        studentArr.add(s1);
        studentArr.add(s2);

       Mockito.when(studentRepository.findAll()).thenReturn(studentArr);

       Assertions.assertEquals(2,studentservice.getAllStudents().size());
    }

    @Test
    void getStudentByIdServiceTest()
    {
        Mockito.when(studentRepository.findById(ArgumentMatchers.anyInt())).thenReturn(java.util.Optional.of(s1));

        Assertions.assertNotNull(studentservice.findStudentById(s1.getStudentId()));

    }

    @Test
    void updateStudentServiceTest()
    {

        Student updatedStud = s1;
        updatedStud.setStudentName("XYZ*");

        Mockito.when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(updatedStud);

        Assertions.assertEquals("XYZ*",studentservice.updateStudent(updatedStud).getStudentName());

    }

    @Test
    void deleteStudentServiceTest()
    {
        Mockito.when(studentRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(s1));

        studentservice.removeStudent(s1);

        Mockito.verify(studentRepository,Mockito.times(1)).delete(s1);

    }

    @Test
    void deleteStudentByIdService()
    {
        Mockito.when(studentRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(s1));

        studentservice.deleteStudentById(s1.getStudentId());

        Mockito.verify(studentRepository,Mockito.times(1)).deleteById(1);
    }

    @Test
    void sequenceGeneratorServiceTest()
    {
        Mockito.when(sequenceGeneratorService.getSequenceNumber(ArgumentMatchers.anyString())).thenReturn(1);

        Assertions.assertEquals(1,sequenceGeneratorService.getSequenceNumber(ArgumentMatchers.anyString()));
    }

    /**
     * Test for failures of service
     */

    @Test
    void addStudentServiceFailureTest()
    {
        Mockito.when(studentRepository.save(null)).thenReturn(new Exception());

        Student addedStudent = studentservice.addStudent(null);

        Assertions.assertNull(addedStudent);
    }

    @Test
    void getStudentByIdServiceFailureTest()
    {
        Mockito.when(studentRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.ofNullable(null));

        Assertions.assertNull(studentservice.findStudentById(s1.getStudentId()));

    }


    @Test
    void updateStudentServiceFailureTest()
    {

        Mockito.when(studentRepository.save(null)).thenReturn(new Exception());

        Student s = studentservice.updateStudent(null);

        Assertions.assertNull(studentservice.updateStudent(null));

    }

    @Test
    void deleteStudentServiceFailureTest()
    {
        Mockito.when(studentRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.ofNullable(null));

        Assertions.assertFalse(studentservice.removeStudent(s1));

    }

    @Test
    void deleteStudentByIdServiceFailureTest()
    {
        Mockito.when(studentRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.ofNullable(null));

        Assertions.assertFalse(studentservice.deleteStudentById(s1.getStudentId()));
    }


}
