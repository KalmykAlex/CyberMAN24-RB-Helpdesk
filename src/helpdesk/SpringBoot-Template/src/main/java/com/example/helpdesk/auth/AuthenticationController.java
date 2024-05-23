package com.example.helpdesk.auth;

import com.example.helpdesk.config.RhinoService;
import com.example.helpdesk.user.ChangePasswordRequest;
import com.example.helpdesk.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import javax.script.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

@CrossOrigin(origins = "http://**")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final RhinoService rhinoService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException{
        authenticationService.refreshToken(request, response);
    }



    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        int returnCode = userService.resetPassword(changePasswordRequest);

        if(returnCode == 0){
            return ResponseEntity.ok("Password was changed!");
        }

        if(returnCode == 1){
            return ResponseEntity.badRequest().body("User does not exists");
        }

        if (returnCode == 2){
            return ResponseEntity.badRequest().body("Passwords don't match");
        }

        return ResponseEntity.badRequest().body("Unexpected event");
    }

    @PostMapping("/chatBot")
    public ResponseEntity<String> chatBot(@RequestBody ChatRequest chatRequest) throws ScriptException {
        String chat = chatRequest.getChat();
        Object o = rhinoService.chat(chat);
        return ResponseEntity.ok().body(o.toString());
    }

}
