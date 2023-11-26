package dev.pankaj.observabilitydemo;

import dev.pankaj.observabilitydemo.post.JsonPlaceHolderService;
import dev.pankaj.observabilitydemo.post.Post;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@SpringBootApplication
public class ObservabilityDemoApplication {
	private static final Logger log = LoggerFactory.getLogger(ObservabilityDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ObservabilityDemoApplication.class, args);
	}

	@Bean
	JsonPlaceHolderService jsonPlaceHolderService() {
		RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com");
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(
				RestClientAdapter.create(restClient)).build();
		return factory.createClient(JsonPlaceHolderService.class);
	}
	// @Bean
	// CommandLineRunner commandLineRunner(JsonPlaceHolderService jsonPlaceHolderService, ObservationRegistry observationRegistry) {
	// 	return args -> {
	// 		Observation.createNotStarted("posts.load-all-posts",observationRegistry)
	// 				.lowCardinalityKeyValue("author","pankaj")
	// 				.contextualName("post.load-all-posts")
	// 				.observe(jsonPlaceHolderService::findAll);
	// 		List<Post> posts = jsonPlaceHolderService.findAll();
	// 		log.info("Posts: {}", posts.size());
	// 		System.out.println(jsonPlaceHolderService.findById(1));
	// 	};
	// }

	@Bean
	@Observed(name="posts.load-all-posts",contextualName = "post.find-all")
	CommandLineRunner commandLineRunner(JsonPlaceHolderService jsonPlaceHolderService, ObservationRegistry observationRegistry) {
		return args -> {
			jsonPlaceHolderService.findAll();
		};
	}

}
