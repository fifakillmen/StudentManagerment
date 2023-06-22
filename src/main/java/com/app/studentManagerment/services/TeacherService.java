package com.app.studentManagerment.services;

import com.app.studentManagerment.dto.TeacherDto;
import com.app.studentManagerment.entity.user.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface TeacherService {
    Page<TeacherDto> search(String searchText, String type, Pageable pageable);

    String getMSGV();

    Teacher addTeacher(List<String> course,
                       String name,
                       LocalDate dob,
                       String address,
                       MultipartFile avatar);

    TeacherDto updateTeacher(String msgvUpdate, String name, String address, LocalDate dob, MultipartFile avatar, List<String> courses, String email) throws Exception;

    boolean deleteTeacher(String msgv);


    public List<Teacher> getAllTeacherCanTakeClasses(long semesterId,
                                                     long courseId,
                                                      int[] dayOfWeak,
                                                      int[] slot_of_day);
}
