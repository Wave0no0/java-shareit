package ru.practicum.shareit.booking;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState from(String state) {
        try {
            return BookingState.valueOf(state.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}
