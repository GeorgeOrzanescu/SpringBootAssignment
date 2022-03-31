package com.assignment.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RESTController {

    @Autowired
    private LinkRepository linkRepository;


    private int checkUrl(String url) {
        ArrayList<Link> links = (ArrayList<Link>)linkRepository.findAll();
        System.out.println(links.size());
        boolean isPresent = false;
        int indexPresent = 0;
        for(Link link : links) {
            if(link.getLongAddress().equals("https://" + url)) {
                isPresent = true;
                indexPresent = links.indexOf(link);
            }
        }
        if(isPresent) {
            return indexPresent;
        }
        else {
            return -1;
        }
    }

    @PostMapping("/url")
    public List<String> createLink(@RequestBody Map<String, String> values) {
        List<String> generatedLinks = new ArrayList<>();
        for (Map.Entry<String, String> entry : values.entrySet()) {

                int index = checkUrl(entry.getValue());
                 if(index == -1) {
                     String shortAddress = "http://localhost:9003/" + Long.toString(linkRepository.count()+1);
                     Link link = new Link(entry.getValue(),shortAddress);
                     linkRepository.save(link);
                     generatedLinks.add(shortAddress);
                 }
                 else {
                     generatedLinks.add("http://localhost:9003/" + Long.toString(index));
                 }
        }
        return generatedLinks;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLink(@PathVariable Integer id) {
        if(id > linkRepository.count() || id <= 0) {
            return new ResponseEntity("No such link", HttpStatus.NOT_FOUND);
        }
        else{
            HttpHeaders headers = new HttpHeaders();
            headers.set("Location", linkRepository.findById(Long.valueOf(id)).get().getLongAddress());
            ResponseEntity<?> responseEntity = new ResponseEntity<>(headers, HttpStatus.PERMANENT_REDIRECT);
            return responseEntity;
        }
    }
}
