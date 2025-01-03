package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.RequiredDocumentDTO;
import com.example.bayan.Model.RequiredDocuments;
import com.example.bayan.Service.RequiredDocumentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bayan/required-documents")
@RequiredArgsConstructor
public class RequiredDocumentsController {

    private final RequiredDocumentsService requiredDocumentsService;

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

    @PostMapping("/upload-multiple")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            if (files.isEmpty()) {
                return ResponseEntity.badRequest().body("No files were uploaded.");
            }

            String uploadDir = "uploads/";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    Path filePath = Paths.get(uploadDir + file.getOriginalFilename());
                    Files.write(filePath, file.getBytes());
                }
            }

            return ResponseEntity.ok("Files uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // احفظ الملف (مثال لحفظ الملف محليًا)
            String uploadDir = "uploads/";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            Path filePath = Paths.get(uploadDir + file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }
}
