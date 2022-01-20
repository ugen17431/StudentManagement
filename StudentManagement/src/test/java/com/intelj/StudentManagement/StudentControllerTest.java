package com.intelj.StudentManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.intelj.StudentManagement.Controllers.StudentController;
import com.intelj.StudentManagement.DTO.StudentDto;
import com.intelj.StudentManagement.Models.Student;
import com.intelj.StudentManagement.Services.StudentService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT
)
class StudentControllerTest {
    MockMvc mockMvc;
    @MockBean
    private StudentService studentService;
    @Autowired
    private StudentController studentController;




   static Student s1 = new Student(1,"xyz",new Date(),
            new Student.Course[]{new Student.Course(1,"Java"),
                    new Student.Course(2,"Python")});
   static Student s2 = new Student(2,"pqr",new Date(),
            new Student.Course[]{new Student.Course(1,"C"),
                    new Student.Course(2,"C++")});


    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }
    @Test
    @WithMockUser(username = "xyz",password ="xyz",roles="USER")
    void getAllStudentsTest() throws Exception {
        List<Student> studentArr = new ArrayList<Student>();
        studentArr.add(s1);
        studentArr.add(s2);

        Mockito.when(studentService.getAllStudents()).thenReturn(studentArr);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/getAllStudents")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("xyz","xyz"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].studentName", Matchers.is("pqr")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].courseList",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].courseList[0].courseName",Matchers.is("C")));
    }
    @Test
    @WithMockUser(username = "xyz",password ="xyz",roles="USER")
    void getStudentByIdTest() throws Exception {
        Mockito.when(studentService.findStudentById(ArgumentMatchers.anyInt())).thenReturn(s1);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/getStudent/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success",Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",Matchers.is("Id "+s1.getStudentId()+" Retrieved Successfully")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.studentDto.studentId",Matchers.is(s1.getStudentId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.studentDto.studentName",Matchers.is(s1.getStudentName())));
    }
    @Test
    @WithMockUser(username = "abc",password ="abc",roles="ADMIN")
    void saveStudentTest() throws Exception {

        String content = objectWriter.writeValueAsString(s1);

        Mockito.when(studentService.addStudent(ArgumentMatchers.any(Student.class))).thenReturn(s1);

        MockHttpServletRequestBuilder mocReq = MockMvcRequestBuilders.post("/api/addStudent")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content);
        mockMvc.perform(mocReq)
                .andExpect(status().isOk()).andExpect((MockMvcResultMatchers.jsonPath("$",notNullValue())))
                .andExpect((MockMvcResultMatchers.jsonPath("$.success",Matchers.is(true))));
    }
     @Test
     @WithMockUser(username = "abc",password ="abc",roles="ADMIN")
     void deleteStudentTest() throws Exception {

         String content = objectWriter.writeValueAsString(s2);

         Mockito.when(studentService.removeStudent(ArgumentMatchers.any(Student.class))).thenReturn(true);

         MockHttpServletRequestBuilder mocReq = MockMvcRequestBuilders.delete("/api/removeStudent")
                 .contentType(MediaType.APPLICATION_JSON).content(content);
         mockMvc.perform(mocReq).andExpect(status().isOk())
                 .andExpect((MockMvcResultMatchers.jsonPath("$", notNullValue())))
                 .andExpect((MockMvcResultMatchers.jsonPath("$.success",Matchers.is(true))));
     }


    @Test
    @WithMockUser(username = "abc",password ="abc",roles="ADMIN")
    void updateStudentTest() throws Exception {
        Student updatedStud = s1;
        updatedStud.setStudentName("AB-");
        String content = objectWriter.writeValueAsString(updatedStud);

        Mockito.when(studentService.updateStudent(ArgumentMatchers.any(Student.class))).thenReturn(updatedStud);

        MockHttpServletRequestBuilder mocReq = MockMvcRequestBuilders.put("/api/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mocReq).andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$",notNullValue())))
                .andExpect((MockMvcResultMatchers.jsonPath("$.success",Matchers.is(true))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.studentDto.studentName",Matchers.is("AB-")));
    }
    @Test
    @WithMockUser(username = "abc",password ="abc",roles="ADMIN")
    void deleteStudentByIdTest() throws Exception {

        Mockito.when(studentService.deleteStudentById(ArgumentMatchers.anyInt())).thenReturn(true);

        MockHttpServletRequestBuilder mocReq = MockMvcRequestBuilders.delete("/api/removeStudent/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mocReq).andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$",notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success",Matchers.is(true)));

    }

    /**
     * Test's For failure of controller
     */

    @Test
    @WithMockUser(username = "xyz",password ="xyz",roles="USER")
    void getStudentByIdFailureTest() throws Exception {

        StudentDto studentDto = null;

        Mockito.when(studentService.findStudentById(ArgumentMatchers.anyInt())).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/getStudent/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success",Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",Matchers.is("Id "+s1.getStudentId()+" Not Retrieved")));
    }

    @Test
    @WithMockUser(username = "abc",password ="abc",roles="ADMIN")
    void saveStudentFailureTest() throws Exception {

        String content = objectWriter.writeValueAsString(s1);

        Mockito.when(studentService.addStudent(ArgumentMatchers.any(Student.class))).thenReturn(null);

        MockHttpServletRequestBuilder mocReq = MockMvcRequestBuilders.post("/api/addStudent")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content);
        mockMvc.perform(mocReq)
                .andExpect(status().isOk()).andExpect((MockMvcResultMatchers.jsonPath("$",notNullValue())))
                .andExpect((MockMvcResultMatchers.jsonPath("$.success",Matchers.is(false))));
    }

    @Test
    @WithMockUser(username = "abc",password ="abc",roles="ADMIN")
    void deleteStudentFailureTest() throws Exception {

        String content = objectWriter.writeValueAsString(s2);

        Mockito.when(studentService.removeStudent(ArgumentMatchers.any(Student.class))).thenReturn(false);

        MockHttpServletRequestBuilder mocReq = MockMvcRequestBuilders.delete("/api/removeStudent")
                .contentType(MediaType.APPLICATION_JSON).content(content);
        mockMvc.perform(mocReq).andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$", notNullValue())))
                .andExpect((MockMvcResultMatchers.jsonPath("$.success",Matchers.is(false))));
    }


    @Test
    @WithMockUser(username = "abc",password ="abc",roles="ADMIN")
    void updateStudentFailureTest() throws Exception {
        String content = objectWriter.writeValueAsString(s2);

        Mockito.when(studentService.updateStudent(ArgumentMatchers.any(Student.class))).thenReturn(null);

        MockHttpServletRequestBuilder mocReq = MockMvcRequestBuilders.put("/api/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mocReq).andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$",notNullValue())))
                .andExpect((MockMvcResultMatchers.jsonPath("$.success",Matchers.is(false))));
    }
    @Test
    @WithMockUser(username = "abc",password ="abc",roles="ADMIN")
    void deleteStudentByIdFailureTest() throws Exception {

        Mockito.when(studentService.deleteStudentById(ArgumentMatchers.anyInt())).thenReturn(false);

        MockHttpServletRequestBuilder mocReq = MockMvcRequestBuilders.delete("/api/removeStudent/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mocReq).andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$",notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success",Matchers.is(false)));

    }


}
