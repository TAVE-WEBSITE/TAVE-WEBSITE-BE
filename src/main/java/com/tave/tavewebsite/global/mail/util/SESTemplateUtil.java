package com.tave.tavewebsite.global.mail.util;

import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SESTemplateUtil {

    @Value("${cloud.aws.ses.username}")
    private String mail;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SendTemplatedEmailRequest createTemplatedEmailRequest(String recipient, String templateName,
                                                                 Map<String, String> templateData)
            throws Exception {

        String jsonData = objectMapper.writeValueAsString(templateData);

        return new SendTemplatedEmailRequest()
                .withSource(mail)
                .withDestination(new Destination().withToAddresses(recipient))
                .withTemplate(templateName)
                .withTemplateData(jsonData);
    }

}
