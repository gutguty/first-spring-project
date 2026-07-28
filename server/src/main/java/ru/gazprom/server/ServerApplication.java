package ru.gazprom.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.repository.CardRepository;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(CardRepository cardRepository) {
		return args -> {
			if (cardRepository.count() == 0) {
				cardRepository.save(new Card(null, "Shoes", 1000, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRzHOCK4Kxge3YHD3ZPcRyHYpw3noooeVpfSjUiSziSaA&s=10", null));
				cardRepository.save(new Card(null, "Shirts", 2000, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRzHOCK4Kxge3YHD3ZPcRyHYpw3noooeVpfSjUiSziSaA&s=10", null));
				cardRepository.save(new Card(null, "Trousers", 3000, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRzHOCK4Kxge3YHD3ZPcRyHYpw3noooeVpfSjUiSziSaA&s=10", null));
				cardRepository.save(new Card(null, "Hats", 4000, "https://img.magnific.com/free-photo/set-with-fashionable-women-s-clothing-jeans-sweater_169016-3214.jpg?semt=ais_hybrid&w=740&q=80", null));
				cardRepository.save(new Card(null, "Sweater", 2500, "https://img.magnific.com/free-photo/set-with-fashionable-women-s-clothing-jeans-sweater_169016-3214.jpg?semt=ais_hybrid&w=740&q=80", null));
				cardRepository.save(new Card(null, "Belt", 3200, "https://img.magnific.com/free-photo/set-with-fashionable-women-s-clothing-jeans-sweater_169016-3214.jpg?semt=ais_hybrid&w=740&q=80", null));
				cardRepository.save(new Card(null, "Bag", 500, "https://img.magnific.com/free-photo/set-with-fashionable-women-s-clothing-jeans-sweater_169016-3214.jpg?semt=ais_hybrid&w=740&q=80", null));
				cardRepository.save(new Card(null, "Gloves", 750, "https://img.magnific.com/free-photo/set-with-fashionable-women-s-clothing-jeans-sweater_169016-3214.jpg?semt=ais_hybrid&w=740&q=80", null));
			}
		};
	}
}
