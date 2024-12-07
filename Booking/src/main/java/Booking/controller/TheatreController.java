package Booking.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import Booking.entities.Theatre;
import Booking.service.TheatreService;



@RestController
@RequestMapping("/theatres")
public class TheatreController {
 
    @Autowired
    private TheatreService theatreService;
    
    @GetMapping()
    public ResponseEntity<List<Theatre>> getAllTheatre() {
        List<Theatre> theatres = this.theatreService.getAllTheatres();
        return ResponseEntity.status(HttpStatus.OK).body(theatres);
    }
    
}
