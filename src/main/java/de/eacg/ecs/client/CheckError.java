package de.eacg.ecs.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckError {
    private String error;
    private String message;

    public String getError() {
        return error;
    }
    public String getMessage() {
        return message;
    }
}
