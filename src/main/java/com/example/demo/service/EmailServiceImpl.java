package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.DemoApplication;
import com.example.demo.dto.CombinedAIClientResponse;
import com.example.demo.dto.CombinedClient;
import com.example.demo.dto.CombinedClientService;
import com.example.demo.dto.CombinedStatusCode;
import com.example.demo.dto.DateTime;
import com.example.demo.dto.StatusCodeCount;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	private final DemoApplication demoApplication;

	@Autowired
	private JavaMailSender mailSender;

	EmailServiceImpl(DemoApplication demoApplication) {
		this.demoApplication = demoApplication;
	}

	@Override
	public void processDateRange(List<StatusCodeCount> ydata, List<StatusCodeCount> data, DateTime dateTime)
			throws Exception {
		System.out.println("inside email service" + ydata.toString());

		StringBuilder ybody = new StringBuilder();
		ybody.append(ydata).append("\n\n");

		StringBuilder body = new StringBuilder();
		body.append(data).append("\n\n");

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

		helper.setFrom("pranavladole98@gmail.com");
		helper.setTo("pranavladole98@gmail.com");
		//

		DateTimeFormatter newdateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = newdateFormatter.format(dateTime.getStartDate());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String startTimeFormat = dateTime.getStartDate().format(formatter);
		String EndTimeFormat = dateTime.getEndDate().format(formatter);

		//

		helper.setSubject(
				"Sending MessageStatus report from " + formattedDate + " " + startTimeFormat + " - " + EndTimeFormat);

		Map<String, Long> todayMap = data.stream()
				.collect(Collectors.toMap(StatusCodeCount::getStatuscode, StatusCodeCount::getCount));

		Map<String, Long> yesterdayMap = ydata.stream()
				.collect(Collectors.toMap(StatusCodeCount::getStatuscode, StatusCodeCount::getCount));

		Set<String> allStatusCodes = new HashSet<>();
		allStatusCodes.addAll(yesterdayMap.keySet());
		allStatusCodes.addAll(todayMap.keySet());

		List<CombinedStatusCode> result = new ArrayList();

		for (String status : allStatusCodes) {
			String servicename = null;
			long today = todayMap.getOrDefault(status, 0L);
			long yesterday = yesterdayMap.getOrDefault(status, 0L);
			result.add(new CombinedStatusCode(servicename, status, yesterday, today));
		}

		// return null;

		// Step 4: Build HTML table
		StringBuilder html = new StringBuilder();
		html.append("<p><strong>Status Report (Yesterday vs Today):</strong></p>");
		html.append("<table border='1' cellpadding='5' cellspacing='0'>");
		html.append("<tr style='background-color: #FFFF99;'>").append("<th>Status Code</th>")
				.append("<th>Yesterday Count</th>").append("<th>Today Count</th>").append("</tr>");

		for (CombinedStatusCode entry : result) {
			html.append("<tr>").append("<td>").append(entry.getStatusCode()).append("</td>").append("<td>")
					.append(entry.getYesterdayCount()).append("</td>").append("<td>").append(entry.getTodayCount())
					.append("</td>").append("</tr>");
		}

		html.append("</table>");
		helper.setText(html.toString(), true); // true = isHtml

		try {
			mailSender.send(mimeMessage);
		} catch (MailAuthenticationException e) {
			System.out.println("exception occured while sending email " + e.getMessage());
			throw new AuthenticationException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void getClientsWithNoTrafficToday(CombinedAIClientResponse combinedAIClientResponse, DateTime datetime)
			throws Exception {

		String copilotResponse = combinedAIClientResponse.getCopilotResponse();

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setSubject("Zero Traffic Clients Report");
		helper.setFrom("pranavladole98@gmail.com");
		helper.setTo("pranavladole98@gmail.com");

		List<CombinedClient> data = combinedAIClientResponse.getClients();
		System.out.println("inside email " + data);

		StringBuilder html = new StringBuilder();

		// Add the copilotResponse wrapped in a paragraph or div
		html.append("<p><strong>Copilot Response:</strong></p>");
		html.append("<p>").append(copilotResponse).append("</p>");
		html.append("<br><br>");

		html.append("<p><strong>Clients with Zero Traffic Today:</strong></p>");
		html.append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse: collapse;'>");

		// Header row with background color
		html.append("<tr style='background-color: #FFCCCC;'>").append("<th>Client ID</th>")
				.append("<th>Yesterday Count</th>").append("<th>Today Count</th>").append("</tr>");

		// Data rows
		for (CombinedClient client : data) {
			html.append("<tr>").append("<td>").append(client.getClientId()).append("</td>").append("<td>")
					.append(client.getYesterdayCount()).append("</td>").append("<td>").append(client.getTodayCount())
					.append("</td>").append("</tr>");
		}
		html.append("</table>");
		// Now set this HTML as email content
		helper.setText(html.toString(), true); // true = enable HTML

		mailSender.send(mimeMessage);
	}

	@Override
	public void sendNewZeroTrafficEmail(List<CombinedClientService> noTrafficClients, DateTime datetime)
			throws Exception {

		
		LocalDateTime startTime = datetime.getStartDate();
		LocalDateTime endTime = datetime.getEndDate();
		
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setSubject("Zero Traffic Clients Report from "+startTime +" to "+ endTime );
		helper.setFrom("pranavladole98@gmail.com");
		helper.setTo("pranavladole98@gmail.com");

		List<CombinedClientService> data = noTrafficClients;
		System.out.println("inside email " + data);

		StringBuilder html = new StringBuilder();

		// Add the copilotResponse wrapped in a paragraph or div
	//	html.append("<p><strong>Copilot Response:</strong></p>");
	//	html.append("<p>").append(copilotResponse).append("</p>");
		html.append("<br><br>");

		html.append("<p><strong>Clients with Zero Traffic Today:</strong></p>");
		html.append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse: collapse;'>");

		// Header row with background color
		html.append("<tr style='background-color: #FFCCCC;'>").append("<th>Client ID</th>")
		        .append("<th>Service Name</th>").append("<th>Channel</th>")
		        .append("<th>Yesterday Count</th>").append("<th>Today Count</th>").append("</tr>");

		// Data rows
		for (CombinedClientService client : data) {
		    html.append("<tr>").append("<td>").append(client.getClientId()).append("</td>").append("<td>")
		            .append(client.getServicename()).append("</td>").append("<td>")
		            .append(client.getChannel()).append("</td>").append("<td>")
		            .append(client.getYesterdayCount()).append("</td>").append("<td>")
		            .append(client.getTodayCount()).append("</td>").append("</tr>");
		}

		html.append("</table>");
		// Now set this HTML as email content
		helper.setText(html.toString(), true); // true = enable HTML

		mailSender.send(mimeMessage);

		// TODO Auto-generated method stub
		
	}

}
