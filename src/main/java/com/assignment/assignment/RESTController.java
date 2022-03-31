package com.assignment.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RESTController {

    @Autowired
    private LinkRepository linkRepository;

    @PostMapping("/url")
    public String createLink(@RequestBody Map<String, String> values) {
        Map<Integer,Link> links =   (Map<Integer,Link>) linkRepository.findAll();

        if (url.isEmpty()) { // basic validation
            return "No URL provided";
        }

        boolean isPresent = false;
        int indexPresent = 0;
        for(Link link : links) {
            if(link.getLongAddress().equals("https://" + url)) {
                isPresent = true;
                indexPresent = links.indexOf(link);
            }
        }
        if(isPresent) {
            return links.get(indexPresent).getShortAddress();
        }
        else {
            long lastId = links.get(links.size() - 1).getId(); // get last id
            String shortAddress = "http://localhost:9003/" + (lastId + 1);
            Link link = new Link("https://" + url, shortAddress);
            linkRepository.save(link);

            return shortAddress;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLink(@PathVariable Integer id) {
        List<Link> links = (List<Link>)linkRepository.findAll();
        if(id > links.size() || id <= 0) {
            return new ResponseEntity("No such link", HttpStatus.NOT_FOUND);
        }
        else{
            HttpHeaders headers = new HttpHeaders();
            headers.set("Location", links.get(id-1).getLongAddress());
            ResponseEntity<?> responseEntity = new ResponseEntity<>(headers, HttpStatus.PERMANENT_REDIRECT);
            return responseEntity;
        }
    }
}
