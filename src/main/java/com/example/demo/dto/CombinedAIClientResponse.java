package com.example.demo.dto;

import java.util.List;

public class CombinedAIClientResponse {

    private String copilotResponse;
    private List<CombinedClient> clients;

    // Constructor
    public CombinedAIClientResponse(String copilotResponse, List<CombinedClient> clients) {
        this.copilotResponse = copilotResponse;
        this.clients = clients;
    }

    // Getters and Setters
    public String getCopilotResponse() {
        return copilotResponse;
    }

    public void setCopilotResponse(String copilotResponse) {
        this.copilotResponse = copilotResponse;
    }

    public List<CombinedClient> getClients() {
        return clients;
    }

    public void setClients(List<CombinedClient> clients) {
        this.clients = clients;
    }

}
