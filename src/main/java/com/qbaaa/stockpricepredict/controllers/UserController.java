package com.qbaaa.stockpricepredict.controllers;

import com.qbaaa.stockpricepredict.models.User;
import com.qbaaa.stockpricepredict.repository.UserRepository;
import com.qbaaa.stockpricepredict.response.CompanyResponse;
import com.qbaaa.stockpricepredict.response.MessageResponse;
import com.qbaaa.stockpricepredict.service.PredictPriceStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PredictPriceStock predictPriceStock;

    @DeleteMapping("/user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        User deleteUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Brak użytkownika o podanym emailu " + email));

        userRepository.delete(deleteUser);
        return new ResponseEntity<>(new MessageResponse(("Konto użytkownika: " + deleteUser.getUsername() + " zostało usunięte!")), HttpStatus.OK);
    }

    @PostMapping("/user/{symbolStock}")
    public ResponseEntity<?> predictPriceStock(@PathVariable String symbolStock){

        CompanyResponse result = predictPriceStock.predictPriceStockOneDays(symbolStock);

        return new ResponseEntity<>(result,HttpStatus.OK);

    }

}
