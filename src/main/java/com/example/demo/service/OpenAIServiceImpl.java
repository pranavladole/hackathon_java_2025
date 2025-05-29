package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
	public List<String> getData(String data) {
		// TODO Auto-generated method stub
		System.out.println("inside getdata service");

		String jsonString = data;
		JSONObject obj = new JSONObject(jsonString);
		String content = obj.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");

	
		
		String op = content.replaceAll("```sql|```", "").trim();
		System.out.println("mysql cleaned query" + op);
		Pattern pattern = Pattern.compile("(?i)(SELECT[\\s\\S]*?;)");
		Matcher matcher = pattern.matcher(op);
		String sqlQuery = matcher.find() ? matcher.group(1) : "No SELECT query found";		

//		String op = content.replaceAll("(?s)```sql|```", "").trim();
//		Pattern pattern = Pattern.compile("(?i)(SELECT\\s+[\\s\\S]*?;)");
//		Matcher matcher = pattern.matcher(op);
//		String sqlQuery = matcher.find() ? matcher.group(1).trim() : "No SELECT query found";
//		System.out.println("Extracted SQL query:\n" + sqlQuery);
		
		


		
		
		Query query = entityManager.createNativeQuery(sqlQuery);
		List<?> results = query.getResultList();
		
		System.out.println("mysql query results" + results);
		List<String> stringList = new ArrayList();

		if (!results.isEmpty()) {
			Object first = results.get(0);

			if (first instanceof Object[]) {
				// Multi-column rows
				for (Object row : results) {
					Object[] rowArray = (Object[]) row;
					String joined = Arrays.stream(rowArray).map(String::valueOf).collect(Collectors.joining(", "));
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
			stringList.add("Sorry! I'm not able to fetch data");
			System.out.println("No data returned.");
		}
		return stringList;
	}
}
