package vn.edu_hub.service.constants;

public interface KafkaConfigConstants {
    String GROUP_ID = "facebook";
    String SAVE_USER_TOPIC ="facebook-save-user";
    String DELETE_USER_TOPIC="facebook-delete-user";
    String SMS_TOPIC="facebook-send-sms";
    String EMAIL_TOPIC="facebook-send-email";
    String NOTIFICATION_TOPIC="facebook-send-notifications";
}
