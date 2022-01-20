package com.intelj.StudentManagement.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@NoArgsConstructor
@Getter
@Setter
public class StudentDto {
    private int studentId;
    private String studentName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
    private Date dateOfAdmission;
    private StudentDto.CourseDto[] courseList;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CourseDto{
        private int courseId;
        private String courseName;

    }
}
