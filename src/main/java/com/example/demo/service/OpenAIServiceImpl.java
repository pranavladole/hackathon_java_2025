package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class OpenAIServiceImpl implements OpenAIService {

	private final EntityManager entityManager;
	
	public OpenAIServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }		
    
    @Autowired
    private TransactionRepository transactionRepository;
	
	@Override
	public List<String>  getData(String data) {
		// TODO Auto-generated method stub
		System.out.println("mysql generated query "+ data);
		

			String jsonString = data;
			JSONObject obj = new JSONObject(jsonString);
			String content = obj.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

System.out.println(content);
		
		 String op = content.replaceAll("```sql|```", "").trim();
		 Query query = entityManager.createNativeQuery(op);
		 List<?> results = query.getResultList();
		 List<String> stringList = new ArrayList();

		 if (!results.isEmpty()) {
		     Object first = results.get(0);

		     if (first instanceof Object[]) {
		         // Multi-column rows
		         for (Object row : results) {
		             Object[] rowArray = (Object[]) row;
		             String joined = Arrays.stream(rowArray)
		                                   .map(String::valueOf)
		                                   .collect(Collectors.joining(", "));
		             stringList.add(joined);
		         }
		     } else {
		         // Single-column rows
		         for (Object row : results) {
		             stringList.add(String.valueOf(row));
		         }
		     }

		     // Output or use stringList
		     stringList.forEach(val -> System.out.println("Row: " + val));
		 } else {
		     System.out.println("No data returned.");
		 }
		    return stringList;
	}
}
