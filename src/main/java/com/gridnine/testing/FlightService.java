package com.gridnine.testing;

import java.util.List;

public interface FlightService {

    List<Flight> exceptFlightsWithDepartureBeforeNow(List<Flight> flights);
    List<Flight> exceptFlightsWithArrivalBeforeDeparture(List<Flight> flights);
    List<Flight> exceptFlightsWithMoreThanTwoGroundHours(List<Flight> flights);
}
