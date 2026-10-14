
package com.sahil.pactroomservice.room;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

   @GetMapping("/{id}")
    public ResponseEntity<Room> getRoom(
            @PathVariable String id) {

        return roomRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(
            @RequestBody Room room) {

        Room saved = roomRepository.save(room);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }
}
