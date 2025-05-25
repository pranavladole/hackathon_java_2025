package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.controller.OpenAIController;
import com.example.demo.dto.Client;
import com.example.demo.dto.CombinedAIClientResponse;
import com.example.demo.dto.CombinedClient;
import com.example.demo.dto.DateTime;
import com.example.demo.dto.OpenAIRequestDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class PercentageServiceImpl implements PercentageService {

	private final EntityManager entityManager;
	

    public PercentageServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }	
	
    @Value("${queries1.countByClient}")
    private String countByClient; 
	
    @Value("${queries1.percentincrease}")
    private String percentincrease; 
	
    
    @Autowired
    private OpenAIController openAIController;
    
	@SuppressWarnings("unused")
	@Override
	public CombinedAIClientResponse increasePercent(DateTime dateTime) {
		System.out.println("Inside increase percentage service");

		// Fetch today's data
		Query query = entityManager.createNativeQuery(countByClient);
		query.setParameter("startDateTime", dateTime.getStartDate());
		query.setParameter("endDateTime", dateTime.getEndDate());

		List<Object[]> results = query.getResultList();
		List<Client> todayList = new ArrayList<>();

		for (Object[] row : results) {
		    String clientName = String.valueOf(row[0]);
		  
		    long count = ((Number) row[1]).longValue();  // Use correct index for count
		    todayList.add(new Client(clientName, count));
		}
		System.out.println("Today's client service count: " + todayList);

		// Fetch yesterday's data
		Query yquery = entityManager.createNativeQuery(countByClient);
		LocalDateTime yesterdayStart = dateTime.getStartDate().minusDays(1);
		LocalDateTime yesterdayEnd = dateTime.getEndDate().minusDays(1);
		yquery.setParameter("startDateTime", yesterdayStart);
		yquery.setParameter("endDateTime", yesterdayEnd);

		List<Object[]> yresults = yquery.getResultList();
		List<Client> yesterdayList = new ArrayList<>();

		for (Object[] row : yresults) {
		    String clientName = String.valueOf(row[0]);
		   
		    long count = ((Number) row[1]).longValue();  // Use correct index for count
		    yesterdayList.add(new Client(clientName, count));
		}
		System.out.println("Yesterday's client service count: " + yesterdayList);

		// Compare data
		
		Map<String, Long> todayMap = todayList.stream()
	            .collect(Collectors.toMap(Client::getClientname, Client::getCount));

		
		List<CombinedClient> increasedList = new ArrayList<>();

		for (Client ycs : yesterdayList) {
		    String key = ycs.getClientname() ;
		    long todayCount = todayMap.getOrDefault(key, 0L);

		    if (todayCount >= 1.3 * ycs.getCount()) {
		        increasedList.add(new CombinedClient(
		            ycs.getClientname(),
		            todayCount,
		            ycs.getCount()
		        ));
		    }
		}

		System.out.println("Fetching increase in traffic list: " + increasedList);

		// Generate AI response
		CombinedAIClientResponse res;
		if (!increasedList.isEmpty()) {
		     OpenAIRequestDTO openAIRequestDTO = new OpenAIRequestDTO(increasedList, dateTime.getStartDate(), percentincrease);
		    String openapiresponse = "";
		    openapiresponse=   this.openAIController.explainData(openAIRequestDTO);
		    System.out.println("GPT response: " + openapiresponse);

		    res = new CombinedAIClientResponse(openapiresponse, increasedList);
		} else {
		    res = new CombinedAIClientResponse("No increase in traffic found.", increasedList);
		}

		return res;

//		
//		System.out.println("inside increase percentage service");
//		
//		 Query query = entityManager.createNativeQuery(countByClient);
//	        query.setParameter("startDateTime", dateTime.getStartDate());
//	        query.setParameter("endDateTime", dateTime.getEndDate());
//	        
//	        List<Object[]> results = query.getResultList();
//	        List<ClientService> response = new ArrayList<>();
//
//	        for (Object[] row : results) {
//	            String clientBame = String.valueOf(row[0]); // safe conversion for VARCHAR
//	            String servicename = String.valueOf(row[1]); // safe conversion for VARCHAR
//	            long count = ((Number) row[1]).longValue();
//	            response.add(new ClientService(clientBame, servicename, count));
//	        }
//
//	        
//	        System.out.println("inside today client service count "+ response.toString());
//		
//	        
//	        
//	        Query yquery = entityManager.createNativeQuery(countByClient);        
//	        LocalDateTime yesterdayStart = dateTime.getStartDate().minusDays(1);
//	        LocalDateTime yesterdayEnd = dateTime.getEndDate().minusDays(1);
//	        
//	        query.setParameter("startDateTime", yesterdayStart);
//	        query.setParameter("endDateTime", yesterdayEnd);
//	        
//	        List<Object[]> yresults = query.getResultList();
//	        List<ClientService> yresponse = new ArrayList<>();
//
//	        for (Object[] row : yresults) {
//	        	String yclientBame = String.valueOf(row[0]); // safe conversion for VARCHAR
//	        	  String yservicename = String.valueOf(row[1]); // safe conversion for VARCHAR
//	            long ycount = ((Number) row[1]).longValue();
//	            yresponse.add(new ClientService(yclientBame, yservicename, ycount));
//	        }
//	        System.out.println("inside yesterday client service count "+ yresponse.toString());
//	
//	        
//	        Map<String, Long> todayMap = todayList.stream()
//	        	    .collect(Collectors.toMap(
//	        	        cs -> cs.getClientname() + "-" + cs.getServicename(),
//	        	        ClientService::getCount
//	        	    ));
//	        
//	        
//		    List<CombinedClientService> increasepercent = new ArrayList<>();
//
//		    for (ClientService ycs : yesterdayList) {
//		        String key = ycs.getClientname() + "-" + ycs.getServicename();
//		        long todayCount = todayMap.getOrDefault(key, 0L);
//
//		        if (todayCount >= 1.3 * ycs.getCount()) {
//		            increasedList.add(new CombinedClientService(
//		                ycs.getClientname(),
//		                ycs.getServicename(),
//		                todayCount,
//		                ycs.getCount()
//		            ));
//		        }
//		    }
//
//		        System.out.println("fetching increase in traffic list "+increasepercent.toString());
//		    
//		        CombinedAIClientServiceResponse res =null;
//		        if(increasepercent != null) {
//		        	OpenAIRequestDTO openAIRequestDTO = new OpenAIRequestDTO(increasepercent, dateTime.getStartDate(), percentincrease);
//				    String openapiresponse = this.openAIController.explainData(openAIRequestDTO);
//				    System.out.println( "gpt response" + openapiresponse );
//				    
//				    res = new CombinedAIClientServiceResponse(openapiresponse, nuincreasepercentll)
//				 
//		        }else {
//		        	 res = new CombinedAIClientServiceResponse("no data present", increasepercent);
//		        }
//		      
		        
		        
	//	return res;
		    
	}
	
	@Override
	public List<CombinedClient> decreasePercent(DateTime dateTime) {
		System.out.println("inside increase percentage service");
		
		 Query query = entityManager.createNativeQuery(countByClient);
	        query.setParameter("startDateTime", dateTime.getStartDate());
	        query.setParameter("endDateTime", dateTime.getEndDate());
	        
	        List<Object[]> results = query.getResultList();
	        List<Client> response = new ArrayList<>();

	        for (Object[] row : results) {
	            String clientBame = String.valueOf(row[0]); // safe conversion for VARCHAR
	            long count = ((Number) row[1]).longValue();
	            response.add(new Client(clientBame, count));
	        }

	        
	        System.out.println("inside today client service count "+ response.toString());
		
	        
	        
	        Query yquery = entityManager.createNativeQuery(countByClient);        
	        LocalDateTime yesterdayStart = dateTime.getStartDate().minusDays(1);
	        LocalDateTime yesterdayEnd = dateTime.getEndDate().minusDays(1);
	        
	        query.setParameter("startDateTime", yesterdayStart);
	        query.setParameter("endDateTime", yesterdayEnd);
	        
	        List<Object[]> yresults = query.getResultList();
	        List<Client> yresponse = new ArrayList<>();

	        for (Object[] row : yresults) {
	        	String yclientBame = String.valueOf(row[0]); // safe conversion for VARCHAR
	            long count = ((Number) row[1]).longValue();
	            yresponse.add(new Client(yclientBame, count));
	        }
	        System.out.println("inside yesterday client service count "+ yresponse.toString());
	
	        
			Map<String, Long> todayMap = response.stream()
		            .collect(Collectors.toMap(Client::getClientname, Client::getCount));

		    List<CombinedClient> increasepercent = new ArrayList<>();

		    for (Client yc : yresponse) {
		        long todayCount = todayMap.getOrDefault(yc.getClientname(), 0L);
		        if (todayCount <= 0.3 * yc.getCount()) 
		        {
		        	increasepercent.add(new CombinedClient(yc.getClientname(), todayCount,yc.getCount() ));
		        }
		    }
		        System.out.println("fetching increase in traffic list "+increasepercent.toString());
		    
		return increasepercent;	
		}

}
