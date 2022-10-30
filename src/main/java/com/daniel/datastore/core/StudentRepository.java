package com.daniel.datastore.core;

import com.daniel.datastore.entities.Student;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByName(String name);

}
