package com.gridnine.testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

		FlightService flightService = new FlightServiceImpl();

		//1. Список всех полетов

		List<Flight> flights = FlightBuilder.createFlights();
		System.out.println("All flights");
		System.out.println(flights);

		//2. Исключаем полеты, у которых вылет до текущего времени

		System.out.println("Except flights with departure before current moment");
		System.out.println(flightService.exceptFlightsWithDepartureBeforeNow(flights));

		//3. Исключаем полеты, у которых есть сегменты с датой прилета раньше даты вылета

		System.out.println("Except flights with arrival before departure");
		System.out.println(flightService.exceptFlightsWithArrivalBeforeDeparture(flights));

		//3. Исключаем полеты, где общее время, проведенное на земле, превышает 2 часа

		System.out.println("Except flights with more than two hours on the ground");
		System.out.println(flightService.exceptFlightsWithMoreThanTwoGroundHours(flights));
	}
}
