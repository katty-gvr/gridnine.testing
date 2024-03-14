package com.gridnine.testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

		//1. Список всех полетов

		List<Flight> flights = FlightBuilder.createFlights();
		System.out.println("All flights");
		System.out.println(flights);

		//2. Исключаем полеты, у которых вылет до текущего времени

		List<Flight> exceptDepartureBefore =
			flights.stream()
					.filter(flight -> flight.getSegments().stream()
						.noneMatch(segment -> segment.getDepartureDate().isBefore(LocalDateTime.now())))
					.toList();

		System.out.println("Except flights with departure before current moment");
		System.out.println(exceptDepartureBefore);

		//3. Исключаем полеты, у которых есть сегменты с датой прилета раньше даты вылета

		List<Flight> exceptArrivalBeforeDeparture =
			flights.stream()
					.filter(flight -> flight.getSegments().stream()
						.noneMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate())))
					.toList();

		System.out.println("Except flights with arrival before departure");
		System.out.println(exceptArrivalBeforeDeparture);

		//3. Исключаем полеты, где общее время, проведенное на земле, превышает 2 часа

		List<Flight> exceptGroundTimeMoreThanTwoHours = flights.stream()
			.filter(flight -> flight.getSegments().size() > 1)
			.filter(flight -> {
				long groundTime = 0;
				LocalDateTime previousArrival = null;
				for (Segment segment : flight.getSegments()) {
					if (previousArrival != null) {
						Duration timeOnGround = Duration.between(previousArrival, segment.getDepartureDate());
						groundTime += timeOnGround.toHours();
						if (groundTime > 2) {
							return false; // Если общее время на земле превышает 2 часа, исключаем перелет
						}
					}
					previousArrival = segment.getArrivalDate();
				}
				return true; // Если общее время на земле не превышает 2 часов, оставляем перелет
			})
			.toList();

		System.out.println("Except flights with more than two hours on the ground");
		System.out.println(exceptGroundTimeMoreThanTwoHours);
	}
}
