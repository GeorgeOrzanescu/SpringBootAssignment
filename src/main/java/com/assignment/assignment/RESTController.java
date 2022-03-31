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


    // HELPER METHODS
    private int getLastId() {
        ArrayList<Link> links = (ArrayList<Link>)linkRepository.findAll();
        if (links.size() == 0) {
            return 0;
        }
        return (int)links.get(links.size() - 1).getId();
    }


    private int checkUrl(String url) {
        ArrayList<Link> links = (ArrayList<Link>) linkRepository.findAll();
        boolean isPresent = false;
        int indexPresent = 0;
        for (Link link : links) {
            if (link.getLongAddress().equals("https://" + url)) {
                isPresent = true;
                indexPresent = (int)link.getId();
            }
        }
        if (isPresent) {
            return indexPresent;
        } else {
            return -1;
        }
    }

    @PostMapping("/url")
    public List<String> createLink(@RequestBody Map<String, String> values) {
        List<String> generatedLinks = new ArrayList<>();

        for (Map.Entry<String, String> entry : values.entrySet()) {

            int index = checkUrl(entry.getValue());
            if (linkRepository.count() == 0) {  // if there are no links in the database
                String shortAddress = "http://localhost:9003/" + Integer.toString(1);
                Link link = new Link("https://" + entry.getValue(), shortAddress);
                linkRepository.save(link);
                generatedLinks.add(shortAddress);
            }
            if (index == -1) { // if the link is not present in the database
                String shortAddress = "http://localhost:9003/" + Integer.toString(getLastId() + 1);
                Link link = new Link("https://" + entry.getValue(), shortAddress);
                linkRepository.save(link);
                generatedLinks.add(shortAddress);
            } else {  // if the link is present in the database
                generatedLinks.add("http://localhost:9003/" + Long.toString(index));
            }
        }
        return generatedLinks;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLink(@PathVariable Integer id) {
        if (id > linkRepository.count() || id <= 0) {
            return new ResponseEntity("No such link", HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Location", linkRepository.findById(Long.valueOf(id)).get().getLongAddress());
            ResponseEntity<?> responseEntity = new ResponseEntity<>(headers, HttpStatus.PERMANENT_REDIRECT);
            return responseEntity;
        }
    }
}
