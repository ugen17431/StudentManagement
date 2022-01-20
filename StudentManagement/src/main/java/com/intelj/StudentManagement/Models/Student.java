package com.intelj.StudentManagement.Models;

import java.util.Date;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Student")
public class Student {

    @Transient //this is not persistent to mongodb
    public static final String SEQUENCE_NO = "user_sequence";

    @Id
    private int studentId;
    private String studentName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
    private Date dateOfAdmission;
    private Course[] courseList;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Course{
        private int courseId;
        private String courseName;

    }

}

