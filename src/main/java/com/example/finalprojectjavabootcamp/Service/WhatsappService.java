package com.example.finalprojectjavabootcamp.Service;


import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Contact;
import com.example.finalprojectjavabootcamp.Model.Subscription;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class WhatsappService {

    @Value("${whatsapp.ultramsg.key}")
    private String whatsappKey;
    private final OkHttpClient client;

    public WhatsappService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }


    public void sendTextMessage(String message, String phoneNumber) throws ApiException {
        RequestBody body = new FormBody.Builder()
                .add("token", whatsappKey)
                .add("to", phoneNumber)
                .add("body", message)
                .build();

        Request request = new Request.Builder()
                .url("https://api.ultramsg.com/instance139636/messages/chat")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                throw new ApiException("فشل إرسال الواتساب، كود الاستجابة: "
                        + response.code() + " | الرد: " + responseBody);
            }

            System.out.println("تم إرسال الرسالة بنجاح: " + responseBody);

        } catch (Exception e) {
            throw new ApiException("خطأ أثناء محاولة الإرسال للواتساب: " + e.getMessage());
        }
    }

    public void sendWelcomeMessage(String phoneNumber, String name) throws ApiException{
        String message = "مرحباً " + name + " 🎉\n" +
                "شكرًا لتسجيلك معنا. نتمنى لك تجربة رائعة!";

        sendTextMessage(message, phoneNumber);
    }

    public void sendContactMessage(Contact contact) throws ApiException {
        String message =
                "📩 رسالة جديدة من نموذج تواصل معنا:\n\n" +
                        "👤 الاسم: " + contact.getName() + "\n" +
                        "📧 البريد: " + contact.getEmail() + "\n" +
                        "📱 الجوال: " + contact.getPhoneNumber() + "\n" +
                        "✉️ الرسالة: " + contact.getContent();


        String adminPhone = "966543298868";

        sendTextMessage(message, adminPhone);
    }

    public void sendSubscriptionCreatedMessage(String phoneNumber, Subscription subscription) throws ApiException {
        String message =
                "✅ تم تفعيل اشتراكك بنجاح!\n\n" +
                        "📦 نوع الاشتراك: " + subscription.getType() + "\n" +
                        "💰 السعر: " + subscription.getPrice() + " ريال\n" +
                        "📅 تاريخ البداية: " + subscription.getStartDate() + "\n" +
                        "📅 تاريخ الانتهاء: " + subscription.getEndDate() + "\n" +
                        "🔖 الحالة: " + subscription.getStatus();

        sendTextMessage(message, phoneNumber);
    }

    public void sendSubscriptionCancelledMessage(String phoneNumber, Subscription subscription) throws ApiException {
        String message =
                "⚠️ تم إلغاء اشتراكك.\n\n" +
                        "📦 نوع الاشتراك: " + subscription.getType() + "\n" +
                        "📅 تاريخ الإلغاء: " + subscription.getEndDate() + "\n" +
                        "🔖 الحالة الحالية: " + subscription.getStatus();

        sendTextMessage(message, phoneNumber);
    }

    public void sendSubscriptionPausedMessage(String phoneNumber, Subscription subscription) throws ApiException {
        String message =
                "⏸️ تم إيقاف اشتراكك مؤقتًا.\n\n" +
                        "📦 نوع الاشتراك: " + subscription.getType() + "\n" +
                        "📅 تاريخ الإيقاف: " + subscription.getEndDate() + "\n" +
                        "📆 الأيام المتبقية: " + subscription.getRemainingDays() + " يوم\n" +
                        "🔖 الحالة الحالية: " + subscription.getStatus();

        sendTextMessage(message, phoneNumber);
    }

    public void sendSubscriptionResumedMessage(String phoneNumber, Subscription subscription) throws ApiException {
        String message =
                "▶️ تم استئناف اشتراكك.\n\n" +
                        "📦 نوع الاشتراك: " + subscription.getType() + "\n" +
                        "📅 تاريخ الاستئناف: " + subscription.getStartDate() + "\n" +
                        "📅 تاريخ الانتهاء الجديد: " + subscription.getEndDate() + "\n" +
                        "🔖 الحالة الحالية: " + subscription.getStatus();

        sendTextMessage(message, phoneNumber);
    }


}
