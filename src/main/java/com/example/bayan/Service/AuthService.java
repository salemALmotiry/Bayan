package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.CustomBrokerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final CustomBrokerRepository customBrokerRepository;

    private final EmailService emailService;

    public void acceptCustomBroker(Integer adminId,Integer customerId) {
        MyUser admin= authRepository.findMyUserById(adminId);
        if (!admin.getRole().equalsIgnoreCase("ADMIN")){
            throw new ApiException("Sorry,you can't");
        }
        CustomsBroker customsBroker=customBrokerRepository.findCustomsBrokerById(customerId);

        if(customsBroker==null){
            throw new ApiException("Customs Broker with this Id"+customerId+" doesn't exist");
        }
        customsBroker.setIsActive(true);
        customBrokerRepository.save(customsBroker);

        sendActivationEmail(customsBroker);
    }

    private void sendActivationEmail(CustomsBroker customsBroker) {
        String userEmail = customsBroker.getUser().getEmail();
        String subject = "تفعيل الحساب: مرحبًا بك كمخلص جمركي!";
        String body = "عزيزي/عزيزتي " + customsBroker.getUser().getFullName() + ",\n\n"
                + "يسعدنا إعلامك بأنه قد تم تفعيل حسابك بنجاح.\n\n"
                + "يمكنك الآن الدخول إلى المنصة والبدء بتقديم خدمات التخليص الجمركي.\n\n"
                + "إذا كانت لديك أي استفسارات أو تحتاج إلى مساعدة، فلا تتردد في التواصل مع فريق الدعم الخاص بنا.\n\n"
                + "مع أطيب التحيات،\n"
                + "فريق بيان";

        emailService.sendEmail(userEmail, subject, body);
    }

      ///reject the Customs Broker
    public void rejectCustomBroker(Integer adminId,Integer customerId) {
        MyUser admin= authRepository.findMyUserById(adminId);
        if (!admin.getRole().equalsIgnoreCase("ADMIN")){
            throw new ApiException("Sorry,you can't");
        }
        CustomsBroker customsBroker=customBrokerRepository.findCustomsBrokerById(customerId);

        if(customsBroker==null){
            throw new ApiException("Customs Broker with this Id"+customerId+" doesn't exist");
        }
        customsBroker.setIsActive(false);
        customBrokerRepository.save(customsBroker);

        sendRejectionEmail(customsBroker);

    }

    private void sendRejectionEmail(CustomsBroker customsBroker) {
        String userEmail = customsBroker.getUser().getEmail();
        String subject = "إشعار رفض الحساب";
        String body = "عزيزي/عزيزتي " + customsBroker.getUser().getFullName() + ",\n\n"
                + "نأسف لإبلاغك بأنه قد تم رفض طلبك لتفعيل حسابك كمخلص جمركي على منصتنا.\n\n"
                + "إذا كنت تعتقد أن هناك خطأ أو لديك أي استفسارات، يرجى التواصل مع فريق الدعم الخاص بنا لمراجعة طلبك.\n\n"
                + "مع أطيب التحيات،\n"
                + "فريق بيان";

        emailService.sendEmail(userEmail, subject, body);
    }
}
