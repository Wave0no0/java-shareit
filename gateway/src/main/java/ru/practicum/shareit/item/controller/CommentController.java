package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.CommentClient;
import ru.practicum.shareit.item.dto.CommentDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class CommentController {

    private final CommentClient commentClient;

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody @Valid CommentDto commentDto) {
        return commentClient.addComment(userId, itemId, commentDto);
    }
}
