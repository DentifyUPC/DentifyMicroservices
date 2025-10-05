package com.upc.dentify.iam.domain.model.commands;

public record UpdatePasswordCommand(
        String oldPassword,
        String confirmNewPassword,
        String newPassword
) {
    public UpdatePasswordCommand {
        if (oldPassword == null || oldPassword.isBlank()) {
            throw new IllegalArgumentException("Old Password cannot be empty");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New Password cannot be empty");
        }
        if (confirmNewPassword == null || confirmNewPassword.isBlank()) {
            throw new IllegalArgumentException("Confirm New Password cannot be empty");
        }
        if (!newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("New Password and Confirm New Password do not match");
        }
    }
}
