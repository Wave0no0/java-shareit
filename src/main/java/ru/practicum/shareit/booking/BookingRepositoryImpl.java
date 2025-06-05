package ru.practicum.shareit.booking;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookingRepositoryImpl implements BookingRepository {

    private final Map<Long, Booking> bookings = new HashMap<>();
    private long idCounter = 1;

    @Override
    public Booking save(Booking booking) {
        if (booking.getId() == null) {
            booking.setId(idCounter++);
        }
        bookings.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public Optional<Booking> findById(long id) {
        return Optional.ofNullable(bookings.get(id));
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }
}
