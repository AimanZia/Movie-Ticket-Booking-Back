package Booking.service;

import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;

import Booking.entities.Show;
import Booking.exceptions.ResourceNotFoundException;
import Booking.repositories.ShowRepository;
import Booking.repositories.TheatreRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    public Show getShowById(Integer showId) {
        Show show = this.showRepository.findById(showId).orElseThrow((()-> new ResourceNotFoundException("Show", "showId", showId)));
        return show;
    }

    public List<Show> getShowsAtTheatre(Integer theatreId) {
        List<Show> showsByTheatre = this.showRepository.findByTheatreId(theatreId);
        return showsByTheatre;
    }

    public void readAndPopulateShows() {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource("shows.csv").getInputStream()));
            String[] line;
            reader.readNext(); // Skipping header
            while ((line = reader.readNext()) != null) {
                Show show = new Show();
                show.setId(Integer.parseInt(line[0]));
                show.setTheatre(theatreRepository.findById(Integer.parseInt(line[1])).orElse(null));
                show.setTitle(line[2]);
                show.setPrice(Integer.parseInt(line[3]));
                show.setSeatsAvailable(Integer.parseInt(line[4]));
                
                showRepository.save(show);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Show saveShow(Show show) {
       return this.showRepository.save(show);
    }
    
}
