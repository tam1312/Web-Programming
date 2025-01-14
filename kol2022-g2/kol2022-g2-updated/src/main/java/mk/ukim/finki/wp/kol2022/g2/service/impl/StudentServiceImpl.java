package mk.ukim.finki.wp.kol2022.g2.service.impl;

import mk.ukim.finki.wp.kol2022.g2.model.Student;
import mk.ukim.finki.wp.kol2022.g2.model.StudentType;
import mk.ukim.finki.wp.kol2022.g2.model.exceptions.InvalidStudentIdException;
import mk.ukim.finki.wp.kol2022.g2.repository.StudentRepository;
import mk.ukim.finki.wp.kol2022.g2.service.CourseService;
import mk.ukim.finki.wp.kol2022.g2.service.StudentService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentServiceImpl implements StudentService, UserDetailsService {
    private final StudentRepository studentRepository;
    private final CourseService courseService;
    private final PasswordEncoder passwordEncoder;
    public StudentServiceImpl(StudentRepository studentRepository, CourseService courseService, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.courseService = courseService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Student> listAll() {
        return this.studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return this.studentRepository.findById(id).orElseThrow(InvalidStudentIdException::new);
    }

    @Override
    public Student create(String name, String email, String password,
                          StudentType type, List<Long> courseId, LocalDate enrollmentDate) {
        Student student = new Student(
                name,
                email,
                passwordEncoder.encode(password),
                type,
                courseId.stream().map(id -> this.courseService.findById(id)).collect(Collectors.toList()),
                enrollmentDate
        );
        return this.studentRepository.save(student);
    }

    @Override
    public Student update(Long id, String name, String email, String password,
                          StudentType type, List<Long> coursesId, LocalDate enrollmentDate) {
        Student student = this.findById(id);
        student.setName(name);
        student.setEmail(email);
        student.setPassword(passwordEncoder.encode(password));
        student.setType(type);
        student.setCourses(coursesId.stream().map(this.courseService::findById).collect(Collectors.toList()));
        student.setEnrollmentDate(enrollmentDate);

        return this.studentRepository.save(student);
    }

    @Override
    public Student delete(Long id) {
        Student student = this.findById(id);
        this.studentRepository.delete(student);
        return student;
    }

    @Override
    public List<Student> filter(Long courseId, Integer yearsOfStudying) {
        if(courseId != null && yearsOfStudying != null){
            return this.studentRepository.findAllByCoursesContainsAndEnrollmentDateBefore(
                    this.courseService.findById(courseId),
                    LocalDate.now().minusYears(yearsOfStudying)
            );
        }else if(courseId != null){
            return this.studentRepository.findAllByCoursesContains(
                    this.courseService.findById(courseId)
            );
        }else if(yearsOfStudying != null){
            return this.studentRepository.findAllByEnrollmentDateBefore(
                    LocalDate.now().minusYears(yearsOfStudying)
            );
        }else{
            return this.studentRepository.findAll();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = this.studentRepository.findByEmail(username);
        return User.builder()
                .username(student.getEmail())
                .password((student.getPassword()))
                .roles(student.getType().name())
                .build();
    }
}
