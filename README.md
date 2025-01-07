# Bayan System

**Bayan** is a specialized platform aimed at facilitating customs clearance processes by connecting clients, whether individuals or companies, with reliable customs brokers. **Bayan** provides an advanced shipment management system, supported by innovative tools designed to save time and effort, offering a secure and easy customs experience infused with trust.

---

## 🛠 Key Features

- **Connecting Customs Brokers**  
  Easily connect with accredited and reliable customs brokers to receive professional clearance services.

- **Advanced Shipment Management**  
  An integrated system for managing shipments, including tracking, document organization, and process optimization.

- **Real-Time Tracking**  
  Monitor shipment statuses instantly with full transparency.

- **Automated Documentation**  
  Tools for creating and managing customs documents automatically, reducing errors and saving time.


- **Analytics and Reporting**  
  Comprehensive tools for insights and reports on shipment operations to improve performance.

---

## 🌍 منصة بيان

**بيان** هي منصة متخصصة تهدف إلى تسهيل عمليات التخليص الجمركي من خلال ربط العملاء، سواء كانوا أفرادًا أو شركات، بالمخلصين الجمركيين الموثوقين. توفر **بيان** نظامًا متطورًا لإدارة الشحنات، مدعومًا بأدوات مبتكرة تُعنى بتوفير الوقت والجهد، لتقديم تجربة جمركية آمنة وسهلة يتخللها الثقة.

---

### ⭐️ الميزات الرئيسية

- **ربط المخلصين الجمركيين**  
  الاتصال بمخلصين جمركيين معتمدين وموثوقين للحصول على خدمات تخليص احترافية.

- **إدارة الشحنات المتقدمة**  
  نظام شامل لإدارة الشحنات يتضمن التتبع وتنظيم الوثائق وتحسين العمليات.

- **التتبع في الوقت الحقيقي**  
  متابعة حالة الشحنات بشفافية كاملة.

- **توثيق آلي**  
  أدوات آلية لتوثيق المستندات الجمركية، تقلل الأخطاء وتوفر الوقت.

- **معالجة مدفوعات آمنة**  
  نظام دفع آمن بخيارات متعددة لتلبية احتياجات العملاء.

- **دعم العملاء**  
  فريق دعم مخصص لتقديم المساعدة في الاستفسارات أو المشاكل.

- **تحليلات وتقارير**  
  أدوات شاملة للحصول على رؤى قيمة وتحسين الأداء.

---

## 🔗 Links and Resources

- [Figma Design](https://www.figma.com/design/zTIO7kQz6k6514lARuOtXo/Untitled1?node-id=0-1&p=f&t=1OL4NiM8cWGly5It-0)  
- [Postman API](https://documenter.getpostman.com/view/40740226/2sAYJAcwpL)  


---
## Diagram
![image](https://github.com/user-attachments/assets/f8633fc7-8efe-4132-9a1e-bdd2a552a960)

## User cases 
![image](https://github.com/user-attachments/assets/44bb88da-2b41-4cf2-82bd-64e8a707ab90)
![image](https://github.com/user-attachments/assets/50c32047-16e0-4025-84fb-0b57b66e7916)
![image](https://github.com/user-attachments/assets/3ae585af-cfe2-4b43-a2aa-85fa439262da)
![image](https://github.com/user-attachments/assets/8eed383e-5e78-4a1d-9e8d-17e3fdf6f797)

----
## 👩‍💻 My Work on the Project

As part of developing the **Bayan** platform, I implemented the following:

### 🚀 Features Developed

1. **CRUD Operations**  
   - Shipments  
   - Customs Brokers  
   - Clients  
   - Transactions  

2. **Extra Functionalities**  
## قائمة النقاط النهائية (API Endpoints)

| **طريقة HTTP** | **المسار النسبي**                                    | **ميثود الخدمة**                |
|----------------|------------------------------------------------------|----------------------------------|
| POST           | `/broker-rate-customer/{orderId}`                    | `brokerRateCustomer`             |
| POST           | `/customer-rate-broker/{orderId}`                    | `customerReviewBroker`           |
| PUT            | `/broker-update-review-customer/{reviewId}`          | `updateBrokerRating`             |
| PUT            | `/customer-update-review-broker/{reviewId}`          | `updateCustomerReview`           |
| GET            | `/broker-reviews/{brokerId}`                          | `allReviewsOnCustomBroker`       |
| GET            | `/customer/{customerId}/reviews`                      | `allReviewsOnCustomer`           |
| GET            | `/customer/{customerId}/average-rating`               | `allAverageOnCustomer`           |
| POST           | `/add-carrier/{orderId}`                              | `addCarrier`                     |
| PUT            | `/update-status/{deliveryId}`                         | `updateStatus`                   |
| POST           | `/track-air-shipment`                                 | `trackAirShipment`               |
| POST           | `/track-sea-container`                                | `trackSeaContainer`              |
| POST           | `/track-by-carrier/{deliveryId}/{orderId}`            | `trackByCarrier`                 |
| PUT            | `/cancel-order/{orderId}`                             | `cancelOrder`                    |
| PUT            | `/cancel-order-broker/{orderId}`                      | `cancelOrderBroker`              |
| PUT            | `/set-payment-completed/{orderId}`                    | `setPaymentCompleted`            |
| PUT            | `/set-payment-waiting-for-approve/{orderId}`          | `setPaymentWaitingForApprove`   |
| GET            | `/my-orders`                                          | `myOrders`                       |
| GET            | `/order-details/customer/{orderId}`                   | `orderDetailsForCustomer`        |
| GET            | `/order-details/broker/{orderId}`                     | `orderDetailsForBroker`          |
| GET            | `/my-orders-as-broker`                                | `myOrdersAsBroker`               |
| POST           | `/upload-multiple/{postId}`                           | `uploadMultipleFiles`            |
| GET            | `/get-files/{postId}`                                 | `getFilesByPostAndUser`          |
| GET            | `/get-files-broker/{postId}/{customerId}`             | `getFilesByPostAndUserForBroker` |
| GET            | `/download/{offerId}/{documentId}`                     | `downloadFile`                   |
| GET            | `/download-for-customer/{postId}/{documentId}`         | `downloadFileForCustomer`        |
| POST           | `/calculate-cbm`                                      | `calculateCbm`                   |

## القسم الأول

| **المجلد**   | **النوع**   | **الاسم**              |
|--------------|-------------|------------------------|
| IN           | Offer       | OfferDTO              |
| IN           | Offer       | OfferForManyOrder     |
| IN           | Offer       | OfferWithDeliveryDTO  |
| IN           | Post        | AddressDTO            |
| IN           | Post        | CbmDTO                |
| IN           | Post        | ChatMessagesDTO       |
| IN           | Post        | CustomerDTO           |
| OUT          | Post        | PostDTO               |
| OUT          | Post        | SubscriptionPostDTO   |
| OUT          | Post        | AddressDTO            |
| OUT          | Post        | BorderDTO             |
| OUT          | Post        | BrokerRentalsDTO      |

## القسم الثالث

| **الاسم**       |
|------------------|
| Notification     |
| Offer            |
| Orders           |
| Post             |


3. **API Development**  
   Designed and implemented APIs for shipment tracking and management.

4. **Testing**  
   Conducted comprehensive testing of Repository to ensure system reliability.

---

## 📂 Tags

- **#CustomsClearance**  
- **#ShipmentManagement**  
- **#RealTimeTracking**  
- **#SecurePayments**  
- **#CustomerSupport**  
- **#Analytics**  
- **#API**  
- **#Testing**
