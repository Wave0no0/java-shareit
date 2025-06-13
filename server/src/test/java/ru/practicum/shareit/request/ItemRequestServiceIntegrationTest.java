package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemRequestServiceIntegrationTest {

    @Autowired
    private ItemRequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository requestRepository;

    private User requestor;
    private User owner;

    @BeforeEach
    void setUp() {
        requestor = userRepository.save(new User(null, "Requester", "requester@example.com"));
        owner = userRepository.save(new User(null, "Owner", "owner@example.com"));
    }

    @Test
    void createAndGetOwnRequests() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need a drill");

        ItemRequestDto created = requestService.create(requestor.getId(), dto);
        assertThat(created.getDescription()).isEqualTo("Need a drill");

        List<ItemRequestResponseDto> requests = requestService.getOwnRequests(requestor.getId());
        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getDescription()).isEqualTo("Need a drill");
        assertThat(requests.get(0).getItems()).isEmpty();
    }

    @Test
    void getAllRequests_shouldReturnOtherUsersRequests() {
        // Запрос от requestor
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need a ladder");
        ItemRequestDto created = requestService.create(requestor.getId(), dto);

        // Вещь от другого пользователя в ответ на запрос
        ItemRequest request = requestRepository.findById(created.getId()).orElseThrow();
        Item item = new Item();
        item.setName("Ladder");
        item.setDescription("Big ladder");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(request);
        itemRepository.save(item);

        List<ItemRequestResponseDto> results = requestService.getAllRequests(owner.getId());
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getItems()).hasSize(1);
        assertThat(results.get(0).getItems().get(0).getName()).isEqualTo("Ladder");
    }

    @Test
    void getById_shouldReturnRequestWithItems() {
        // Создаем запрос
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need something");
        ItemRequestDto created = requestService.create(requestor.getId(), dto);
        ItemRequest request = requestRepository.findById(created.getId()).orElseThrow();

        // Добавляем вещь в ответ
        Item item = new Item();
        item.setName("Hammer");
        item.setDescription("Heavy hammer");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(request);
        itemRepository.save(item);

        // Проверяем получение запроса
        ItemRequestResponseDto result = requestService.getById(owner.getId(), request.getId());
        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getName()).isEqualTo("Hammer");
    }
}
