package com.gridnine.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class FlightServiceTest {
    FlightService flightService = new FlightServiceImpl();
    Flight flightOne;
    Flight flightTwo;
    Flight flightThree;
    Flight flightFour;
    List<Flight> allFlights;


    @BeforeEach
    void init () {
        Segment firstFlightSegment = new Segment(LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(3L));
        Segment firstFlightSegment1 = new Segment(LocalDateTime.now().plusHours(4L), LocalDateTime.now().plusHours(5L));
        flightOne = new Flight(List.of(firstFlightSegment, firstFlightSegment1));

        Segment secondFlightSegment = new Segment(LocalDateTime.now().minusHours(2L), LocalDateTime.now());
        flightTwo = new Flight(List.of(secondFlightSegment)); // время вылета раньше текущего момента

        Segment thirdFlightSegment = new Segment(LocalDateTime.now().plusHours(1L), LocalDateTime.now().minusHours(2L));
        flightThree = new Flight(List.of(thirdFlightSegment)); // время прилета раньше времени вылета

        Segment fourthFlightSegment = new Segment(LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(3L));
        Segment fourthFlightSegment1 = new Segment(LocalDateTime.now().plusHours(6L), LocalDateTime.now().plusHours(7L));
        flightFour = new Flight(List.of(fourthFlightSegment, fourthFlightSegment1)); // время на земле больше 2-х часов

        allFlights = List.of(flightOne, flightTwo, flightThree, flightFour);
    }

    @Test
    void testExceptingFlightsWithDepartureBeforeNow() {
        List<Flight> flightsWithoutFlightTwo = flightService.exceptFlightsWithDepartureBeforeNow(allFlights);

        Assertions.assertFalse(flightsWithoutFlightTwo.contains(flightTwo));
        Assertions.assertEquals(3, flightsWithoutFlightTwo.size());
    }

    @Test
    void testExceptingFlightsWithArrivalBeforeDeparture() {
        List<Flight> flightsWithoutFlightThree = flightService.exceptFlightsWithArrivalBeforeDeparture(allFlights);

        Assertions.assertFalse(flightsWithoutFlightThree.contains(flightThree));
        Assertions.assertEquals(3, flightsWithoutFlightThree.size());
    }

    @Test
    void testExceptingFlightsWithMoreThanTwoGroundHours() {
        List<Flight> flightsWithoutFlightFour = flightService.exceptFlightsWithMoreThanTwoGroundHours(allFlights);

        Assertions.assertFalse(flightsWithoutFlightFour.contains(flightFour));
        Assertions.assertEquals(1, flightsWithoutFlightFour.size());
    }
}
