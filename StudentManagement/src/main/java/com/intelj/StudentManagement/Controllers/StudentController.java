package com.intelj.StudentManagement.Controllers;

import com.intelj.StudentManagement.DTO.StudentDto;
import com.intelj.StudentManagement.Models.JsonResponse;
import com.intelj.StudentManagement.Models.Student;
import com.intelj.StudentManagement.Services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SecurityRequirement(name = "Student Management API")
@RestController
@RequestMapping("/api")
public class StudentController {
    @Autowired
    private StudentService studentService;
     @Autowired
    private JsonResponse jsonResponse;
     @Autowired
     private ModelMapper modelMapper;

    @Operation(summary = "Get all students")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/getAllStudents" , produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Student> getAllStudents()
    {
        return  studentService.getAllStudents();
    }

    @Operation(summary = "Add student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/addStudent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse addStudent(@RequestBody StudentDto studentDto)
    {
        //converting DTO to Entity
        Student student = modelMapper.map(studentDto,Student.class);

        //converting Entity to DTO
        StudentDto studentDtoFromService = studentService.addStudent(student) == null ? null
                : modelMapper.map(studentService.addStudent(student),StudentDto.class);


        if(studentDtoFromService != null)
        {
            jsonResponse.setSuccess(true);
            jsonResponse.setMessage(student.getStudentName()+" Added Successfully");
            jsonResponse.setStudentDto(studentDtoFromService);
        }
        else
        {
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage(student.getStudentName()+" Not Added");
            jsonResponse.setStudentDto(studentDto);
        }
        return jsonResponse;

    }

    @Operation(summary = "Update a student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/updateStudent" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse updateStudent(@RequestBody StudentDto studentDto)
    {
        //converting DTO to Entity
        Student student = modelMapper.map(studentDto,Student.class);

        //converting Entity to DTO
        StudentDto studentDtoFromService = studentService.updateStudent(student) == null ? null
                : modelMapper.map(studentService.updateStudent(student),StudentDto.class);

        if(studentDtoFromService != null)
        {
            jsonResponse.setSuccess(true);
            jsonResponse.setMessage(student.getStudentName()+" Updated Successfully");
            jsonResponse.setStudentDto(studentDtoFromService);
        }
        else
        {
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage(student.getStudentName()+" Not Updated");
            jsonResponse.setStudentDto(studentDto);
        }
        return jsonResponse;

    }

    @Operation(summary = "Remove a student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/removeStudent" , produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse removeStudent(@RequestBody StudentDto studentDto)
    {
        //converting DTO to Entity
        Student student = modelMapper.map(studentDto,Student.class);

        boolean deleteResponse = studentService.removeStudent(student);

        if(deleteResponse)
        {
            jsonResponse.setSuccess(true);
            jsonResponse.setMessage(student.getStudentName()+" Deleted Successfully");
            jsonResponse.setStudentDto(studentDto);
        }
        else
        {
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage(student.getStudentName()+" Not Deleted");
            jsonResponse.setStudentDto(studentDto);
        }
        return jsonResponse;
    }

    @Operation(summary = "Get a student by its id")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(path = "/getStudent/{id}" , produces=MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse findStudentById(@PathVariable("id") int studentId)
    {

        //converting Entity to DTO
        StudentDto studentDto = studentService.findStudentById(studentId)==null?null
                :modelMapper.map(studentService.findStudentById(studentId),StudentDto.class);


        if(studentDto!=null)
        {
            jsonResponse.setSuccess(true);
            jsonResponse.setMessage("Id "+studentId+" Retrieved Successfully");
            jsonResponse.setStudentDto(studentDto);
        }
        else
        {
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage("Id "+studentId+" Not Retrieved");
            jsonResponse.setStudentDto(null);
        }
        return jsonResponse;
    }


    @Operation(summary = "Remove a student by its id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/removeStudent/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse removeStudentById(@PathVariable("id") int studentId)
    {
        //convert Entity to DTO
        StudentDto studentDto = studentService.findStudentById(studentId) == null ? null
                : modelMapper.map( studentService.findStudentById(studentId),StudentDto.class);

        boolean deleteResponse = studentService.deleteStudentById(studentId);
        if(deleteResponse)
        {
            jsonResponse.setSuccess(true);
            jsonResponse.setMessage("Id "+studentId+" Deleted Successfully");
            jsonResponse.setStudentDto(studentDto);
        }
        else
        {
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage("Id "+studentId+" Not Deleted");
            jsonResponse.setStudentDto(studentDto);
        }
        return jsonResponse;
    }



}
