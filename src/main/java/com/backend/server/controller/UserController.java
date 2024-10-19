package com.backend.server.controller;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.server.config.JWT;
import com.backend.server.entity.LoginRequest;
import com.backend.server.entity.LoginResponse;
import com.backend.server.entity.User;
import com.backend.server.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RestController
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    UserService userSer;

    @Autowired
    private JWT myJwt;

    @Autowired 
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOneById(@PathVariable ("id")Long id){
        try{
            User u = userSer.findById(id) ;
            return ResponseEntity.accepted().body(u);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/get/all")
    public ResponseEntity<?> getAll(){
        try{
            List<User> u = userSer.findAll() ;
            return ResponseEntity.accepted().body(u);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<?> SignUp(@RequestBody User u){
        try{
            JSONObject ExistResponse = new JSONObject();

            ExistResponse.put("message", "Your e-mail or username already used!");
            ExistResponse.put("status", "error");
            if( userSer.findOneByEmail(u.getEmail()) != null || userSer.findOneByEmail(u.getEmail()) != null )
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ExistResponse.toString()); // 406
            u.setStatus(User.Status.BLOCKED);
            u.setRole(User.Role.USER);
            u.setCreatedAt(LocalDateTime.now());
            u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
            userSer.save(u);
            return ResponseEntity.accepted().build();
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest l){
        try{
            User u ;
            if(l.getEmail() != null) u = userSer.findOneByEmail(l.getEmail()) ;
            else if(l.getUsername() != null)  u = userSer.findOneByUsername(l.getUsername()) ;
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            if( bCryptPasswordEncoder.matches(l.getPassword(), u.getPassword()) ){
                String jwt = myJwt.generateToken(u);
                LoginResponse Lr = new LoginResponse(jwt);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(Lr);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/google-login")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            String email = principal.getAttribute("email");
            String lastname = principal.getAttribute("family_name");
            String firstname = principal.getAttribute("given_name");

            User userInfo = new User();
            userInfo.setEmail(email);
            userInfo.setFirstname(firstname);
            userInfo.setLastname(lastname);
            User user = userSer.findOrCreateUser(userInfo);

            String jwtToken = myJwt.generateToken(user);
            LoginResponse Lr = new LoginResponse(jwtToken);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Lr);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> BlockOneById(@PathVariable("id") Long id,@RequestBody User u){
        try{
            if(u.getPassword() != null)
                u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
            User UserAfterUpdates = userSer.updateById(id,u);
            return ResponseEntity.accepted().body(UserAfterUpdates);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}