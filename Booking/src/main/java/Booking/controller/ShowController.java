package Booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Booking.entities.Show;
import Booking.payload.ShowDto;
import Booking.service.ShowService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping("/{showId}")
    public ResponseEntity<ShowDto> getShowById(@PathVariable Integer showId){
        Show show = this.showService.getShowById(showId);
        ShowDto showDto = showToShowDto(show);
        return ResponseEntity.status(HttpStatus.OK).body(showDto);
    }

    @GetMapping("/theatres/{theatreId}")
    public ResponseEntity<List<ShowDto>> getShowsAtTheatre(@PathVariable Integer theatreId){
        List<Show> showsAtTheatre = this.showService.getShowsAtTheatre(theatreId);
        List<ShowDto> showDtoList = showsAtTheatre.stream().map(this::showToShowDto).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(showDtoList);
    }
    
    private ShowDto showToShowDto(Show show){
         ShowDto showDto = new ShowDto();
         showDto.setId(show.getId());
         showDto.setTheatreId(show.getTheatre().getId());
         showDto.setTitle(show.getTitle());
         showDto.setPrice(show.getPrice());
         showDto.setSeatsAvailable(show.getSeatsAvailable());
         return showDto;
    }
}
