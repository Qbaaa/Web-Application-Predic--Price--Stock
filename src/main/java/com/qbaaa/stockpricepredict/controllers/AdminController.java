package com.qbaaa.stockpricepredict.controllers;

import com.qbaaa.stockpricepredict.models.ERole;
import com.qbaaa.stockpricepredict.models.Role;
import com.qbaaa.stockpricepredict.models.User;
import com.qbaaa.stockpricepredict.repository.RoleRepository;
import com.qbaaa.stockpricepredict.repository.UserRepository;
import com.qbaaa.stockpricepredict.response.MessageResponse;
import com.qbaaa.stockpricepredict.response.UserResponse;
import com.qbaaa.stockpricepredict.service.PredictPriceStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AdminController {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PredictPriceStockService predictPriceStockService;

    @GetMapping("/admin")
    public ResponseEntity<?> getUsers() {
        Role userRole = roleRepository.findByRole(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Nie ma danej roli uzywkownika!!!"));

        if (userRole.getUsers().isEmpty())
            return new ResponseEntity<>(new MessageResponse("Nie ma żadnego użytkownika do usunięcia!"),
                    HttpStatus.BAD_REQUEST);

        List<UserResponse> listUsers = userRole.getUsers().stream().map(x -> {
            UserResponse obj = new UserResponse(x.getId(),x.getUsername(),x.getEmail());
            return obj;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(listUsers ,HttpStatus.OK);
    }

    @DeleteMapping("/admin/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        User deleteUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Brak użytkownika do usunięcia!!!"));

        userRepository.delete(deleteUser);
        return new ResponseEntity<>(new MessageResponse(("Konto użytkownika: " + deleteUser.getUsername() + " zostało usunięte!")), HttpStatus.OK);
    }

    @PostMapping("/admin/{structure}/{conv1}/{conv2}/{dense1}/{initMode}/{activation}/{optimizer}/{batchSize}/{epoche}")
    public ResponseEntity<?> optimizationNN(@PathVariable String structure, @PathVariable String conv1,
                                            @PathVariable String conv2, @PathVariable String dense1, @PathVariable String initMode,
                                            @PathVariable String activation, @PathVariable String optimizer,
                                            @PathVariable String batchSize, @PathVariable String epoche)
    {
        String result = predictPriceStockService.optimizationNN(structure, conv1, conv2, dense1, initMode, activation, optimizer, batchSize, epoche);
        return new ResponseEntity<>(new MessageResponse(result),HttpStatus.OK);
    }

}
