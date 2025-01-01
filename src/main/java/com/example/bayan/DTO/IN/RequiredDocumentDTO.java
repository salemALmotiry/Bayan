package com.example.bayan.DTO.IN;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RequiredDocumentDTO {

        // Fields for specific documents
        private String commercialInvoice;     // الفاتورة
        private String packingList;           // قائمة التعبئة
        private String billOfLading;          // بوليصة الشحن
        private String certificateOfOrigin;   // شهادة المنشأ
        private String otherDocument;    // مستندات اضافية

}
