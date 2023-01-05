package me.nerminsehic.groupevent.config;

import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfiguration {

    @Value("${spring.sendgrid.api-key}")
    private String sendgridKey;

    @Bean
    public SendGrid getSendgridInstance() {
        return new SendGrid(sendgridKey);
    }

    @Bean
    public MailSettings getMailSettings() {
        MailSettings mailSettings = new MailSettings();

        Setting bypassListManagement = new Setting();
        bypassListManagement.setEnable(false);

        FooterSetting footerSetting = new FooterSetting();
        footerSetting.setEnable(false);

        Setting sandBoxMode = new Setting();
        sandBoxMode.setEnable(false);

        mailSettings.setBypassListManagement(bypassListManagement);
        mailSettings.setFooterSetting(footerSetting);
        mailSettings.setSandboxMode(sandBoxMode);

        return mailSettings;
    }

    @Bean
    public TrackingSettings getTrackingSettings() {
        TrackingSettings trackingSettings = new TrackingSettings();

        ClickTrackingSetting clickTrackingSetting = new ClickTrackingSetting();
        clickTrackingSetting.setEnable(true);
        clickTrackingSetting.setEnableText(false);

        OpenTrackingSetting openTrackingSetting = new OpenTrackingSetting();
        openTrackingSetting.setEnable(true);
        openTrackingSetting.setSubstitutionTag("%open-track%");

        SubscriptionTrackingSetting subscriptionTrackingSetting = new SubscriptionTrackingSetting();
        subscriptionTrackingSetting.setEnable(false);

        trackingSettings.setOpenTrackingSetting(openTrackingSetting);
        trackingSettings.setClickTrackingSetting(clickTrackingSetting);
        trackingSettings.setSubscriptionTrackingSetting(subscriptionTrackingSetting);

        return trackingSettings;
    }

}
