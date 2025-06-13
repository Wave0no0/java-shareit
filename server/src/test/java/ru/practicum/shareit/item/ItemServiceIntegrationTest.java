package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private User owner;
    private User booker;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(new User(null, "Owner", "owner@email.com"));
        booker = userRepository.save(new User(null, "Booker", "booker@email.com"));

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Desc");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
    }

    @Test
    void getByOwner_shouldReturnItemWithBookings() {
        List<ItemDto> items = itemService.getByOwner(owner.getId());

        assertThat(items).hasSize(1);

        ItemDto dto = items.get(0);
        assertThat(dto.getName()).isEqualTo("Test Item");
        assertThat(dto.getLastBooking()).isNotNull();
        assertThat(dto.getLastBooking().getBookerId()).isEqualTo(booker.getId());
        assertThat(dto.getNextBooking()).isNull(); // нет будущих бронирований
    }
}
