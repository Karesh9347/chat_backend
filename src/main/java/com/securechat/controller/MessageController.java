package com.securechat.controller;

import com.securechat.domain.Message;
import com.securechat.dto.MessageDTO;
import com.securechat.service.FileStorageService;
import com.securechat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/{chatId}")
    public ResponseEntity<Page<MessageDTO>> getChatMessages(@PathVariable UUID chatId,
            @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(messageService.getChatMessages(chatId, pageable));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/download/")
                .path(fileName)
                .toUriString();

        return ResponseEntity.ok(fileDownloadUri);
    }
}
