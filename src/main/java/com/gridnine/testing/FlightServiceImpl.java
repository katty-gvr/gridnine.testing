package com.gridnine.testing;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {
    @Override
    public List<Flight> exceptFlightsWithDepartureBeforeNow(List<Flight> flights) {
        return flights.stream()
            .filter(flight -> flight.getSegments().stream()
                .noneMatch(segment -> segment.getDepartureDate().isBefore(LocalDateTime.now())))
            .collect(Collectors.toList());
    }

    @Override
    public List<Flight> exceptFlightsWithArrivalBeforeDeparture(List<Flight> flights) {
        return flights.stream()
            .filter(flight -> flight.getSegments().stream()
                .noneMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate())))
            .collect(Collectors.toList());
    }

    @Override
    public List<Flight> exceptFlightsWithMoreThanTwoGroundHours(List<Flight> flights) {
        return flights.stream()
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
            .collect(Collectors.toList());
    }
}
