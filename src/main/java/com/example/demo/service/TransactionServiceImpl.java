package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.controller.OpenAIController;
import com.example.demo.dto.Client;
import com.example.demo.dto.ClientService;
import com.example.demo.dto.CombinedAIClientResponse;
import com.example.demo.dto.CombinedClient;
import com.example.demo.dto.CombinedClientService;
import com.example.demo.dto.CombinedStatusCode;
import com.example.demo.dto.DateTime;
import com.example.demo.dto.OpenAIRequestDTO;
import com.example.demo.dto.ServiceStatusKey;
import com.example.demo.dto.StatusCount;
import com.example.demo.repository.TransactionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

	private final EntityManager entityManager;

	@Value("${queries.countByStatusCode}")
	private String groupedQuery;

	@Value("${queries.countByClient}")
	private String countByClient;
	
	@Value("${queries.countByClientService}")
	private String countByClientService;
	
	@Value("${queries.countByClientquestion}")
	private String countByClientquestion;

	@Value("${queries.countBetweenDates}")
	private String countQuery;

	@Autowired
	private OpenAIController openAIController;

	public TransactionServiceImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	public EmailService emailService;

	@Override
	public List<CombinedStatusCode> angularprocessDateRange(DateTime dateTime) throws Exception {
		Query query = entityManager.createNativeQuery(groupedQuery);
		query.setParameter("startDateTime", dateTime.getStartDate());
		query.setParameter("endDateTime", dateTime.getEndDate());

		List<Object[]> todayresults = query.getResultList();
		System.out.println("today's list");
		for (Object[] row : todayresults) {
			System.out.print(Arrays.toString(row));
		}

		// yesterday count

		System.out.println("fetching results for yesterday");
		LocalDateTime yesterdayStart = dateTime.getStartDate().minusDays(1);
		LocalDateTime yesterdayEnd = dateTime.getEndDate().minusDays(1);

		Query yesterdayquery = entityManager.createNativeQuery(groupedQuery);
		System.out.println("get yesterday details" + yesterdayEnd);
		yesterdayquery.setParameter("startDateTime", yesterdayStart);
		yesterdayquery.setParameter("endDateTime", yesterdayEnd);

		List<Object[]> yesterdayresults = yesterdayquery.getResultList();

		System.out.println("yesterdayresults list");
		for (Object[] row : yesterdayresults) {
			System.out.print(Arrays.toString(row));
		}

		// StatusCodeCount
		Map<ServiceStatusKey, StatusCount> yesterdayMap = new HashMap<>();

		for (Object[] row : yesterdayresults) {
			String serviceName = String.valueOf(row[0]);
			String status = String.valueOf(row[1]);
			Long count = ((Number) row[2]).longValue();
			ServiceStatusKey key = new ServiceStatusKey(serviceName, status);
			StatusCount value = new StatusCount(count);
			yesterdayMap.put(key, value);
		}
		List<CombinedStatusCode> combinedResults = new ArrayList<>();
		for (Object[] row : todayresults) {
			String servicename = String.valueOf(row[0]);
			String status = String.valueOf(row[1]);
			long todayCount = ((Number) row[2]).longValue();

			ServiceStatusKey key = new ServiceStatusKey(servicename, status);
			StatusCount data = yesterdayMap.get(key);
			long yesterdayCount = (data != null) ? data.getCount() : 0L;

			combinedResults.add(new CombinedStatusCode(servicename, status, todayCount, yesterdayCount));
		}

		List<CombinedStatusCode> list = combinedResults;

		// Create a new sorted list
		List<CombinedStatusCode> sortedList = list.stream()
				.sorted(Comparator.comparing(CombinedStatusCode::getServicename)).collect(Collectors.toList());

		// Print the sorted list
		sortedList.forEach(item -> System.out.println(item));
		return list;
	}

	@Override
	public CombinedAIClientResponse angularprocessZeroTraffic(DateTime dateTime) {

		Query query = entityManager.createNativeQuery(countByClient);
		query.setParameter("startDateTime", dateTime.getStartDate());
		query.setParameter("endDateTime", dateTime.getEndDate());

		List<Object[]> results = query.getResultList();
		List<Client> response = new ArrayList<>();

		for (Object[] row : results) {
			String status = String.valueOf(row[0]); // safe conversion for VARCHAR
			long count = ((Number) row[1]).longValue();
			response.add(new Client(status, count));
		}
		System.out.println("today client count" + response.toString());

		System.out.println("fetching results for yesterday");

		// LocalDateTime yesterdayStart = dateTime.getStartDate().minusDays(1);
		LocalDateTime yesterdayEnd = dateTime.getEndDate().minusDays(1);

		LocalDate date = yesterdayEnd.toLocalDate();
		LocalDateTime startOfDay = date.atStartOfDay();

		Query yesterdayquery = entityManager.createNativeQuery(countByClient);
		System.out.println("get yesterday details" + yesterdayEnd);

		yesterdayquery.setParameter("startDateTime", startOfDay);
		yesterdayquery.setParameter("endDateTime", yesterdayEnd);

		System.out.println("yesterday details date " + startOfDay + " " + yesterdayEnd);
		List<Object[]> yesterdayresults = yesterdayquery.getResultList();
		List<Client> yresponse = new ArrayList<>();

		for (Object[] row : yesterdayresults) {
			String status = String.valueOf(row[0]); // safe conversion for VARCHAR
			long count = ((Number) row[1]).longValue();
			yresponse.add(new Client(status, count));
		}

		System.out.println("fetching results for yesterday" + yresponse.toString());

		Map<String, Long> todayMap = response.stream()
				.collect(Collectors.toMap(Client::getClientname, Client::getCount));

		List<CombinedClient> noTrafficClients = new ArrayList<>();

		for (Client yc : yresponse) {
			long todayCount = todayMap.getOrDefault(yc.getClientname(), 0L);
			if (todayCount == 0 && yc.getCount() > 0) {
				noTrafficClients.add(new CombinedClient(yc.getClientname(), 0, yc.getCount()));
			}
			if (noTrafficClients.isEmpty()) {
				System.out.println("no zero traffic today");
			}
		}

		noTrafficClients.forEach(System.out::println);
		OpenAIRequestDTO openAIRequestDTO = new OpenAIRequestDTO(noTrafficClients, dateTime.getStartDate(),
				countByClientquestion);
		String openapiresponse = this.openAIController.explainData(openAIRequestDTO);
		CombinedAIClientResponse res = new CombinedAIClientResponse(openapiresponse, noTrafficClients);

		try {
			this.emailService.getClientsWithNoTrafficToday(res, dateTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;

	}

	@Override
	public List<CombinedClientService> processZeroTraffic(DateTime dateTime) {
		// TODO Auto-generated method stub
		
		Query query = entityManager.createNativeQuery(countByClientService);
		query.setParameter("startDateTime", dateTime.getStartDate());
		query.setParameter("endDateTime", dateTime.getEndDate());

		List<Object[]> results = query.getResultList();
		System.out.println("getting results" + results);;
		List<ClientService> response = new ArrayList<>();

		for (Object[] row : results) {
			String clientid = String.valueOf(row[0]); // safe conversion for VARCHAR
			String servicename = String.valueOf(row[1]); // safe conversion for VARCHAR
			String channel = String.valueOf(row[2]); // safe conversion for VARCHAR
			long count = ((Number) row[3]).longValue();
			response.add(new ClientService(clientid, servicename,channel,count));
		}
		System.out.println("today client count" + response.toString());
		
		System.out.println("fetching results for yesterday");

		// LocalDateTime yesterdayStart = dateTime.getStartDate().minusDays(1);
		LocalDateTime yesterdayEnd = dateTime.getEndDate().minusDays(1);

		LocalDate date = yesterdayEnd.toLocalDate();
		LocalDateTime startOfDay = date.atStartOfDay();

		Query yesterdayquery = entityManager.createNativeQuery(countByClientService);
		System.out.println("get yesterday details" + yesterdayEnd);

		yesterdayquery.setParameter("startDateTime", startOfDay);
		yesterdayquery.setParameter("endDateTime", yesterdayEnd);

		System.out.println("yesterday details date " + startOfDay + " " + yesterdayEnd);
		List<Object[]> yesterdayresults = yesterdayquery.getResultList();
		List<ClientService> yresponse = new ArrayList<>();

		for (Object[] row : yesterdayresults) {
			String clientid = String.valueOf(row[0]); // safe conversion for VARCHAR
			String servicename = String.valueOf(row[1]); // safe conversion for VARCHAR
			String channel = String.valueOf(row[2]); // safe conversion for VARCHAR
			long count = ((Number) row[3]).longValue();
			yresponse.add(new ClientService(clientid, servicename,channel,count));
		}

		System.out.println("fetching results for yesterday" + yresponse.toString());

		//
		Map<String, Long> todayMap = response.stream()
			    .collect(Collectors.toMap(
			        cs -> cs.getClientname() + "-" + cs.getServicename() + "-" + cs.getChannel(),
			        ClientService::getCount
			    ));

		List<CombinedClientService> noTrafficClients = new ArrayList<>();
		
		for (ClientService yc : yresponse) {
		    String key = yc.getClientname() + "-" + yc.getServicename() + "-" + yc.getChannel();
		    long todayCount = todayMap.getOrDefault(key, 0L);

		    if (todayCount == 0 && yc.getCount() > 0) {
		        noTrafficClients.add(new CombinedClientService(
		            yc.getClientname(),
		            yc.getServicename(),
		            yc.getChannel()
		            ,0,
		            yc.getCount()
		            
		        ));
		    }
		}

		if (noTrafficClients.isEmpty()) {
			noTrafficClients.add(new CombinedClientService( "no hits" ,"no hits" ,"no hits" ,0,0));
		    System.out.println("No zero traffic clients today.");
		}

		try {
			this.emailService.sendNewZeroTrafficEmail(noTrafficClients,dateTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		 System.out.println("zero traffic clients today." + noTrafficClients);
		return noTrafficClients;
	}
}
