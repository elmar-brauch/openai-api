package de.bsi.chatgpt;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ChatGptController {
	
	private static final String MAIN_PAGE = "index";
	
	@GetMapping(path = "/")
	public String index() {
		return MAIN_PAGE;
	}
	
	@PostMapping(path = "/")
	public String chat(Model model, @ModelAttribute ChatMessageDTO dto) {
		try {
			model.addAttribute("request", dto.message());
			model.addAttribute("response", chatWithGpt3(dto.message()));
		} catch (Exception e) {
			model.addAttribute("response", "Error in communication with OpenAI ChatGPT API.");
		}
		return MAIN_PAGE;
	}
	
	@Autowired private ObjectMapper jsonMapper;
	@Value("${openai.api_key}") private String openaiApiKey;
	private HttpClient client = HttpClient.newHttpClient();
	private static final URI CHATGPT_URI = URI.create("https://api.openai.com/v1/completions");
	
	private String chatWithGpt3(String message) throws Exception {
		var request = HttpRequest.newBuilder()
				.uri(CHATGPT_URI)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
				.POST(chatMessageAsPostBody(message))
				.build();
		var responseBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
		var completionResponse = jsonMapper.readValue(responseBody, CompletionResponse.class);
		return completionResponse.firstAnswer().orElseThrow();
	}
	
	private BodyPublisher chatMessageAsPostBody(String message) throws JsonProcessingException {
		var completion = CompletionRequest.defaultWith(message);
		return BodyPublishers.ofString(jsonMapper.writeValueAsString(completion));
	}
	
}
