package com.intelj.StudentManagement.Models;

import com.intelj.StudentManagement.DTO.StudentDto;
import lombok.*;
import org.springframework.stereotype.Component;




@Component
@NoArgsConstructor
@Setter
@Getter
public class JsonResponse {
    private boolean success;

    private String message;

    private StudentDto studentDto;

}
