package com.deep.moneymanager.controller;


import com.deep.moneymanager.dto.AuthDTO;
import com.deep.moneymanager.dto.ProfileDTO;
import com.deep.moneymanager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;


    @PostMapping("/register")
    public ResponseEntity<ProfileDTO>registerProfile(@RequestBody ProfileDTO profileDTO){

        ProfileDTO registerProfile=profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);

    }
    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token) {
        boolean isActivated=profileService.activateProfile(token);
        if(isActivated){
            return ResponseEntity.ok("profile activated sucessfully");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already used");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDTO authDTO){
        try{
            if(!profileService.isAccountActive(authDTO.getEmail())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "message","Account is not Active. Please activate your account first."
                ));
            }

            Map<String,Object> resp = profileService.authinticateAndGeneralTaken(authDTO);
            return ResponseEntity.ok(resp);

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }

}
