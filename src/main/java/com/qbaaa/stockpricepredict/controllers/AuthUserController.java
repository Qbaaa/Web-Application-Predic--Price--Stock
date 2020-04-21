package com.qbaaa.stockpricepredict.controllers;

import com.qbaaa.stockpricepredict.models.ERole;
import com.qbaaa.stockpricepredict.models.Role;
import com.qbaaa.stockpricepredict.models.User;
import com.qbaaa.stockpricepredict.repository.RoleRepository;
import com.qbaaa.stockpricepredict.repository.UserRepository;
import com.qbaaa.stockpricepredict.request.LoginRequest;
import com.qbaaa.stockpricepredict.request.RegisterRequest;
import com.qbaaa.stockpricepredict.response.JwtResponse;
import com.qbaaa.stockpricepredict.response.MessageResponse;
import com.qbaaa.stockpricepredict.security.jwt.JwtUtils;
import com.qbaaa.stockpricepredict.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthUserController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest)
    {
        if (userRepository.existsByUsername(registerRequest.getUsername()))
        {
            return new ResponseEntity<>(new MessageResponse("Nazwa użytkownika: " + registerRequest.getUsername() + " jest już zajęta!"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail()))
        {
            return new ResponseEntity<>(new MessageResponse("Email: " + registerRequest.getEmail() + " jest już użyty!"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(registerRequest.getUsername(), registerRequest.getEmail(), encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByRole(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono roli użytkownika."));
                    roles.add(adminRole);

                    break;
                case "user":
                    Role userRole = roleRepository.findByRole(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono roli użytkownika."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("Rejestracja użytkownika przebiegła sukcesem!"), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest)
    {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                                    .map(item -> item.getAuthority()).collect(Collectors.toList());

        return new ResponseEntity<>(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles),HttpStatus.OK);
    }
}
