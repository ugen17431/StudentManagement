package com.intelj.StudentManagement.Services;

import java.util.List;
import java.util.Optional;

import com.intelj.StudentManagement.Models.Student;
import com.intelj.StudentManagement.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentService {
     @Autowired
    private StudentRepository studentRepo;
     @Autowired
     private SequenceGeneratorService sequenceGeneratorService;

    public List<Student> getAllStudents()
    {
        return studentRepo.findAll();
    }

    public Student updateStudent(Student student)
    {
        try
        {
            return studentRepo.save(student);
        }
        catch(Exception e)
        {
            return null;
        }

    }

    public Student addStudent(Student student)
    {
        /**
         * Before Adding the student to mongodb we are autoincrementing the student id
         * by logic written in SequenceGeneratorService Class
         */
        try {
            student.setStudentId(sequenceGeneratorService.getSequenceNumber(Student.SEQUENCE_NO));
            return studentRepo.save(student);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean removeStudent(Student student)
    {
            if(studentRepo.findById(student.getStudentId()).isPresent())
            {
                    studentRepo.delete(student);
                    return true;
            }
            return false;
    }

    public Student findStudentById(int id)
    {
        try
        {
            Optional<Student> student = studentRepo.findById(id);
            return student.isPresent() ? student.get() : null;
        }
        catch(Exception e)
        {
            return null;
        }

    }

    public boolean deleteStudentById(int id)
    {
            if(studentRepo.findById(id).isPresent())
            {
                studentRepo.deleteById(id);
                return true;
            }
            return false;
    }

}

