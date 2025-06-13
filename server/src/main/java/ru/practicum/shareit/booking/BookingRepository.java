package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerOrderByStartDesc(User booker);

    List<Booking> findByItemOwnerOrderByStartDesc(User owner);

    List<Booking> findByBookerAndStartBeforeAndEndAfter(User booker, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItemAndBookerAndStatusAndEndBefore(
            Item item,
            User booker,
            BookingStatus status,
            LocalDateTime now
    );

    List<Booking> findByItem_IdIn(List<Long> itemIds);

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    boolean existsByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDateTime before);

    boolean existsByBookerIdAndItemIdAndStatusAndEndBefore(
            Long bookerId, Long itemId, BookingStatus status, LocalDateTime end
    );
}
