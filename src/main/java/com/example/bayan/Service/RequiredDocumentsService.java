package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.RequiredDocumentDTO;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.Offer;
import com.example.bayan.Model.Post;
import com.example.bayan.Model.RequiredDocuments;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.OfferRepository;
import com.example.bayan.Repostiry.PostRepository;
import com.example.bayan.Repostiry.RequiredDocumentsRepository;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequiredDocumentsService {

    private final RequiredDocumentsRepository requiredDocumentsRepository;
    private final PostRepository postRepository;
    private final AuthRepository authRepository;
    private final OfferRepository offerRepository;


    // Get a documents by Post ID
    public RequiredDocuments getDocumentsByPostId(Integer postId) {
        Post post = postRepository.findPostById(postId);
        if (post == null) {
            throw new ApiException("Post not found");
        }
        List<RequiredDocuments> requiredDocuments = requiredDocumentsRepository.findAllByPostId(postId);
        if (requiredDocuments == null) {
            throw new ApiException("Documents with the given post id not found");
        }
        return (RequiredDocuments) requiredDocuments;
    }

    // add a documents to a post
    public List<RequiredDocuments> addDocumentsToPost(Integer postId, List<RequiredDocumentDTO> dtos) {
        // Fetch the Post by its ID
        Post post = postRepository.findPostById(postId);
        if (post == null) {
            throw new ApiException("Post not found");
        }
        List<RequiredDocuments> documents = new ArrayList<>();

        // Map DTOs to RequiredDocuments and set the Post
        for (RequiredDocumentDTO dto : dtos) {
            if (dto.getCommercialInvoice() != null) {
                documents.add(new RequiredDocuments(null, "Commercial Invoice", dto.getCommercialInvoice(), post));
            }
            if (dto.getPackingList() != null) {
                documents.add(new RequiredDocuments(null, "Packing List", dto.getPackingList(), post));
            }
            if (dto.getBillOfLading() != null) {
                documents.add(new RequiredDocuments(null, "Bill of Lading", dto.getBillOfLading(), post));
            }
            if (dto.getCertificateOfOrigin() != null) {
                documents.add(new RequiredDocuments(null, "Certificate of Origin", dto.getCertificateOfOrigin(), post));
            }
            if (dto.getOtherDocument() != null) {
                documents.add(new RequiredDocuments(null, "Other", dto.getOtherDocument(), post));
            }
        }
        // Save all documents in the repository
        return requiredDocumentsRepository.saveAll(documents);
    }

    // Update an existing document
    public RequiredDocuments updateDocument(Integer docId, RequiredDocuments updatedDocument) {
        RequiredDocuments existingDocument = requiredDocumentsRepository.findRequiredDocumentsById(docId);
        if(existingDocument==null){
            throw new ApiException("Document with the given id not found");
        }
        existingDocument.setDocName(updatedDocument.getDocName());
        existingDocument.setDocUrl(updatedDocument.getDocUrl());
        return requiredDocumentsRepository.save(existingDocument);
    }

    // Delete a document
    public void deleteDocument(Integer docId) {
        RequiredDocuments document = requiredDocumentsRepository.findRequiredDocumentsById(docId);
        if(document==null){
            throw new ApiException("Document with the given id not found");
        }
        requiredDocumentsRepository.delete(document);
    }




    public List<String> uploadFiles(List<MultipartFile> files, Integer postId, Integer userId) {
        if (files.isEmpty())
            throw new ApiException("Please upload files to proceed.");


        MyUser customer = authRepository.findMyUserById(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        Post post = postRepository.findPostByIdAndCustomerId(postId,userId);

        if (post == null) {
            throw new ApiException("Post with ID " + postId + " not found");
        }

        // Check existing files for the post
        List<RequiredDocuments> existingDocuments = requiredDocumentsRepository.findRequiredDocumentsByPostId(postId);
        if (existingDocuments.size() >= 5) {
            throw new ApiException("Cannot add more than 5 files to this post.");
        }

        String uploadDir = "uploads/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        List<String> uploadedFiles = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    if (existingDocuments.size() + uploadedFiles.size() >= 5) {
                        throw new ApiException("Maximum allowed files (5) reached. Cannot add more.");
                    }

                    // Generate a unique filename using postId, userId, and original filename
                    String uniqueFileName = postId + "_" + userId + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir + uniqueFileName);
                    Files.write(filePath, file.getBytes());

                    // Save metadata in the database
                    RequiredDocuments document = new RequiredDocuments();
                    document.setDocName(file.getOriginalFilename()); // Save the original file name
                    document.setDocUrl(filePath.toString());
                    document.setPost(post);
                    requiredDocumentsRepository.save(document);

                    uploadedFiles.add(uniqueFileName);
                }
            }
        } catch (IOException e) {
            throw new ApiException("An error occurred while uploading files: " + e.getMessage());
        }

        return uploadedFiles;
    }

    public List<RequiredDocuments> getFilesByPostAndUser(Integer postId, Integer userId) {
        // Validate post existence

        MyUser customer = authRepository.findMyUserById(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        Post post = postRepository.findPostByIdAndCustomerId(postId,userId);

        if (post == null) {
            throw new ApiException("Post with ID " + postId + " not found");
        }

        // Fetch documents for the post
        List<RequiredDocuments> documents = requiredDocumentsRepository.findRequiredDocumentsByPostId(postId);

        if (documents.isEmpty()) {
            throw new ApiException("No files found for the specified post and user.");
        }

        return documents;
    }

    public Resource downloadFile(String originalFileName, Integer offerId, Integer userId) {
        // Construct the unique file name
        String uniqueFileName = offerId + "_" + userId + "_" + originalFileName;

        MyUser customer = authRepository.findMyUserById(userId);

        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Offer offer = offerRepository.findOfferByIdAndBrokerId(offerId,userId);
        Offer offer2 = offerRepository.findOfferByIdAndPost_CustomerId(offerId,userId);
        if (offer2==null && offer==null){
            throw new ApiException("Offer with ID " + offerId + " not found");
        }


        try {
            // Locate the file in the local uploads directory
            Path filePath = Paths.get("uploads/" + uniqueFileName).normalize();
            File file = filePath.toFile();

            // Check if the file exists and is readable
            if (!file.exists() || !file.canRead()) {
                throw new ApiException("File not found or not readable: " + uniqueFileName);
            }

            // Return the file as a resource
            return (Resource) new FileSystemResource(file);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

}
