package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.RequiredDocumentDTO;
import com.example.bayan.Model.RequiredDocuments;
import com.example.bayan.Repostiry.RequiredDocumentsRepository;
import com.example.bayan.Service.RequiredDocumentsService;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bayan/required-documents")
@RequiredArgsConstructor
public class RequiredDocumentsController {

    private final RequiredDocumentsService requiredDocumentsService;
    private final RequiredDocumentsRepository requiredDocumentsRepository;

    // get Documents By PostId
    @GetMapping("/get-documents-for-post/{postId}")
    public ResponseEntity<?> getDocumentsByPostId(@PathVariable Integer postId){
        return ResponseEntity.status(200).body(requiredDocumentsService.getDocumentsByPostId(postId));
    }

    //Integer postId, List<RequiredDocumentDTO> dtos
    // add documents for a post
    @PostMapping("/add-document/{postId}")
    public ResponseEntity<?> addDocumentsToPost(@PathVariable Integer postId, @RequestBody @Valid List<RequiredDocumentDTO> dtos){
        requiredDocumentsService.addDocumentsToPost(postId, dtos);
        return ResponseEntity.status(201).body(new ApiResponse("Documents added Successfully"));
    }

    @PutMapping("/update-document/{docId}")
    public ResponseEntity<?> updateDocument(@PathVariable Integer docId,@RequestBody @Valid RequiredDocuments updatedDocument){
        requiredDocumentsService.updateDocument(docId,updatedDocument);
        return ResponseEntity.status(200).body(new ApiResponse("Document updated successfully"));
    }

    @DeleteMapping("/delete/{docId}")
    public ResponseEntity<?> deleteDocument(@PathVariable Integer docId){
        requiredDocumentsService.deleteDocument(docId);
        return ResponseEntity.status(200).body(new ApiResponse("Document deleted successfully"));
    }


    @PostMapping("/upload-multiple/{postId}/{userId}")
    public ResponseEntity<?> uploadMultipleFiles(
            @RequestParam("files") List<MultipartFile> files,
            @PathVariable("postId") Integer postId,
            @PathVariable("userId") Integer userId) {
        List<String> uploadedFiles = requiredDocumentsService.uploadFiles(files, postId, userId);
        return ResponseEntity.ok("Files uploaded successfully: " + uploadedFiles);
    }

    @GetMapping("/get-files/{postId}/{userId}")
    public ResponseEntity<?> getFilesByPostAndUser(@PathVariable("postId") Integer postId, @PathVariable("userId") Integer userId) {
        List<RequiredDocuments> documents = requiredDocumentsService.getFilesByPostAndUser(postId, userId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/download/{postId}/{userId}/{originalFileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("originalFileName") String originalFileName, @PathVariable("postId") Integer postId, @PathVariable("userId") Integer userId) {
        Resource resource = requiredDocumentsService.downloadFile(originalFileName, postId, userId);
        String contentType = "application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
                .body(resource);
    }
}
