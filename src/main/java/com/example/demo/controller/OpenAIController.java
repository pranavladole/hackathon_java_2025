package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
// OpenAIController.java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.CombinedClient;
import com.example.demo.dto.OpenAIRequest;
import com.example.demo.dto.OpenAIRequestDTO;
import com.example.demo.service.OpenAIService;

import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class OpenAIController {

    @Value("${apiKey}")
    private String apiKey;
    
    
    @Value("${queries1.DBdescription}")
    private String DBdescription;
    
    @Autowired
    private OpenAIService openAIService;
    

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generate(@RequestBody OpenAIRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", request.getPrompt());

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("messages", List.of(message));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        // Extract result
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        String output = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");

        Map<String, String> result = new HashMap<>();
        result.put("output_text", output);

        return ResponseEntity.ok(result);
    }
    
    
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/chat")
    public ResponseEntity<String> getChatResponse(@RequestBody Map<String, String> request) {
    	
    	System.out.println("DB description " + DBdescription);
        // get DB query
    	System.out.println("received request is "+ DBdescription +" "+request);
    	
    	
        String prompt =  DBdescription + request.get("message");
      

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
            "model", "gpt-4o-mini",
            "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        String apiUrl = "https://api.openai.com/v1/chat/completions";
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

        
        // make DB call to get data
        System.out.println("printing controller res" + response.getBody());
        List<String> obj =   this.openAIService.getData(response.getBody());
       
        System.out.println("received final list "+ obj);
        String result = obj.stream()
                .collect(Collectors.joining(", "));

        

     return new ResponseEntity<String>(result,HttpStatusCode.valueOf(200));
    }

    //@PostMapping("/explain")
    public String explainData(@RequestBody OpenAIRequestDTO request) {
        // Convert clients + date/time + question into a prompt string
        StringBuilder prompt = new StringBuilder();
        prompt.append("Data for date/time: ").append(request.getDateTime()).append("\n");
        prompt.append("Clients data:\n");
        for (CombinedClient client : request.getClients()) {
            prompt.append(client.getClientId())
                  .append(": TodayCount = ").append(client.getTodayCount())
                  .append(", YesterdayCount = ").append(client.getYesterdayCount())
                  .append("\n");
        }
        prompt.append("Question: ").append(request.getQuestion());

        System.out.println("prompt is " +prompt.toString());
        String openAIResponse = callOpenAI(prompt.toString());
       // String openAIResponse = null;
        return openAIResponse;
    }
    
    private String callOpenAI(String prompt) {
       
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
            "model", "gpt-4o-mini",
            "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        String apiUrl = "https://api.openai.com/v1/chat/completions";
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
        JSONObject jsonObject = new JSONObject(response);
        System.out.println(jsonObject.toString());
        
        String jsonResponse = response.getBody();
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
		try {
			rootNode = objectMapper.readTree(jsonResponse);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Accessing choices.message.content
        String messageContent = rootNode.get("choices").get(0).get("message").get("content").asText();
        System.out.println("Message Content: " + messageContent);
        
        return "OpenAI response for prompt: " + messageContent;  // Replace with actual call
    }
    
    
}
