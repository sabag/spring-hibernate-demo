package com.daniel.web;


import com.daniel.datastore.core.StudentRepository;
import com.daniel.datastore.entities.Student;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class StudentController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;


    @GetMapping("/create")
    public ResponseEntity<String> create(){

        Student student = new Student();
        //
        // fill with random values (failsafe values)
        //
        student.setName("Student #" + (long)(Math.random()*1000));
        student.setLongText("LongText #" + (long)(Math.random()*100_000L));

        RestTemplate restTemplate = restTemplateBuilder.build();

        //
        // get some dummy data and use it for the student attributes
        //
        ResponseEntity<JsonNode> res =
            restTemplate.exchange(
                "https://random-word-api.herokuapp.com/word?number=3",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<JsonNode>() {}
            );

        if(res.getStatusCode().equals(HttpStatus.OK)) {
            JsonNode body = res.getBody();
            if(body != null && body.isArray()){
                student.setName(body.get(0).asText());
                student.setLongText(body.get(1).asText());
            }
        }

        //
        // save to data source
        //
        studentRepository.save(student);
        return ResponseEntity.ok("OK");
    }


    @GetMapping("/list")
    public ResponseEntity<List<Student>> list(){
        return ResponseEntity.ok(studentRepository.findAll());
    }
}