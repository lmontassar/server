package com.backend.server.service;

import com.backend.server.entity.User;
import com.backend.server.entity.VerificationCode;
import com.backend.server.repository.UserRepo;
import com.backend.server.repository.VerificationCodeRepo;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;

@Service
public class VerificatioCodeService {
    @Autowired
    private VerificationCodeRepo verificationCodeRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final SecureRandom random = new SecureRandom();

    private final int LENGTH = 6;
    public String addCode(String email) throws MessagingException {
        User user = userService.findOneByEmail(email);
        if(user==null)return "Email is invalid";
        if(verificationCodeRepo.getByEmail(email)==null){
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setEmail(email);
            StringBuilder code = new StringBuilder(LENGTH);
            for (int i = 0; i < LENGTH; i++) {
                code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            verificationCode.setCode(code.toString());
            verificationCode.setExpiration_date(LocalDate.now().plusDays(2));
            String resetLink = "http://localhost:5173/forgetpassword/"+verificationCode.getCode();
            //Send email
            emailService.sendEmail(
                    email,
                    "Reset Your Password",
                    "<p>Dear " + email + ",</p>"
                            + "<p>We received a request to reset your password. Please use the code below to reset your password:</p>"
                            + "<p><strong>Reset Link :</strong> " + resetLink + "</p>"
                            + "<p>If you did not request a password reset, please ignore this email or contact our support team.</p>"
                            + "<p>For security purposes, this code will expire in 15 minutes.</p>"
                            + "<p>Thank you,<br/>S&D Team</p>"
            );
            verificationCodeRepo.save(verificationCode);
            return "Code Created Successfully";

        }else{
            return "We already sent you verification code";
        }
    }
    @Transactional
    public Boolean codeVerification(String code) {
        VerificationCode verificationCode = verificationCodeRepo.findByCode(code).orElse(null);
        return (verificationCode != null) && verificationCode.getCode().equals(code) && (verificationCode.getExpiration_date().isAfter(LocalDate.now()));
    }
    @Transactional
    public void deleteCode(String code){
        int rowsDeleted = verificationCodeRepo.deleteAllByCode(code);
    }
    public String getEmail(String code){
        VerificationCode ver = verificationCodeRepo.findByCode(code).orElse(null);
        if (ver != null) {
            return ver.getEmail();
        }
        return null;
    }
}
