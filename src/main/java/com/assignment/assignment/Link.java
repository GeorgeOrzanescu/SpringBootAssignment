package com.assignment.assignment;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Link {
    @Id
    private long id;
    private String longAddress;
    private String shortAddress;

    public Link(String longAddress, String shortAddress) {
        this.longAddress = longAddress;
        this.shortAddress = shortAddress;
    }


    public String getLongAddress() {
        return longAddress;
    }
    public String getShortAddress() {
        return shortAddress;
    }

    public long getId() {
        return id;
    }

}
