package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.RequiredDocumentDTO;
import com.example.bayan.Model.Post;
import com.example.bayan.Model.RequiredDocuments;
import com.example.bayan.Repostiry.PostRepository;
import com.example.bayan.Repostiry.RequiredDocumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequiredDocumentsService {

    private final RequiredDocumentsRepository requiredDocumentsRepository;
    private final PostRepository postRepository;


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

}
