package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(new User(null, "Owner", "owner@example.com"));
        booker = userRepository.save(new User(null, "Booker", "booker@example.com"));

        item = new Item();
        item.setName("Bike");
        item.setDescription("Fast bike");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);
    }

    @Test
    void createAndApproveBooking() {
        BookingCreateDto createDto = new BookingCreateDto();
        createDto.setItemId(item.getId());
        createDto.setStart(LocalDateTime.now().plusDays(1));
        createDto.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto created = bookingService.create(booker.getId(), createDto);

        assertThat(created).isNotNull();
        assertThat(created.getItem().getId()).isEqualTo(item.getId());
        assertThat(created.getBooker().getId()).isEqualTo(booker.getId());
        assertThat(created.getStatus()).isEqualTo(BookingStatus.WAITING);

        BookingDto approved = bookingService.approve(owner.getId(), created.getId(), true);

        assertThat(approved.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }
}
