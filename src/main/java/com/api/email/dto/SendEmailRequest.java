package com.api.email.dto;

import jakarta.validation.constraints.NotBlank;

public record SendEmailRequest(Integer emailId,
                               @NotBlank(message = "Recipients have to be informed")
                               String recipients,
                               @NotBlank(message = "Subject have to be informed")
                               String subject,
                               @NotBlank(message = "Message have to be informed")
                               String message) {
}
