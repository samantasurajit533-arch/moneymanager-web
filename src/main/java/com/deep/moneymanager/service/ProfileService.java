package com.deep.moneymanager.service;

import com.deep.moneymanager.dto.AuthDTO;
import com.deep.moneymanager.dto.ProfileDTO;
import com.deep.moneymanager.entity.ProfileEntity;
import com.deep.moneymanager.repository.ProfileRepository;
import com.deep.moneymanager.util.JwtUtill;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtill jwtUtill;

    @Value("${app.activation.url}")
    private String activationURL;

    // ================= REGISTER =================
    public ProfileDTO registerProfile(ProfileDTO profileDTO) {

        ProfileEntity newProfile = ProfileEntity.builder()
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(passwordEncoder.encode(profileDTO.getPassword())) // ✅ encode password
                .profileImageUrl(profileDTO.getProfileImagUrl())
                .isActive(false) // ❗ initially inactive
                .activationToken(UUID.randomUUID().toString())
                .build();

        newProfile = profileRepository.save(newProfile);

        // 🔗 Activation link
        String activationLink =activationURL+"/api/auth/activate?token="
                + newProfile.getActivationToken();

        String subject = "Activate your account";
        String body = "Click to activate your account:\n" + activationLink;

        emailService.sendEmail(newProfile.getEmail(), subject, body);

        return toDTO(newProfile);
    }

    // ================= ACTIVATE =================
    public boolean activateProfile(String activationToken) {
        return profileRepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profile.setActivationToken(null); // ✅ invalidate token after use
                    profileRepository.save(profile);
                    return true;
                }).orElse(false);
    }

    // ================= CHECK ACTIVE =================
    public boolean isAccountActive(String email) {
        return profileRepository.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    // ================= LOGIN =================
    public Map<String, Object> authinticateAndGeneralTaken(AuthDTO authDTO) {

        // 🔐 Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDTO.getEmail(),
                        authDTO.getPassword()
                )
        );

        // ✅ Generate JWT
        String token = jwtUtill.generateToken(authDTO.getEmail());

        return Map.of(
                "token", token,
                "user", getPublicProfile(authDTO.getEmail())
        );
    }

    // ================= CURRENT USER =================
    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return profileRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Profile not found with email: " + authentication.getName()
                ));
    }

    // ================= PUBLIC PROFILE =================
    public ProfileDTO getPublicProfile(String email){
        ProfileEntity currentUser;

        if(email == null){
            currentUser = getCurrentProfile();  // ✅ FIX
        } else {
            currentUser = profileRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("profile not found with email:" + email));
        }

        return ProfileDTO.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())   // ✅ FIX
                .profileImagUrl(currentUser.getProfileImageUrl())
                .createdAt(currentUser.getCreatedAt())
                .updatedAt(currentUser.getUpdatedAt())
                .build();
    }

    // ================= DTO CONVERSION =================
    private ProfileDTO toDTO(ProfileEntity entity) {
        return ProfileDTO.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .profileImagUrl(entity.getProfileImageUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}