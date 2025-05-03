package Booking.service;

import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;

import Booking.entities.Theatre;
import Booking.repositories.TheatreRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    public List<Theatre> getAllTheatres() {
       List<Theatre> theatres = this.theatreRepository.findAll();
       return theatres;
    }
    
    public void readAndPopulateTheatres() {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource("theatres.csv").getInputStream()));
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                Theatre theatre = theatreRepository.findById(Integer.parseInt(line[0])).orElse(new Theatre());
               theatre.setId(Integer.parseInt(line[0]));
               theatre.setName(line[1]);
               theatre.setLocation(line[2]);
               theatreRepository.save(theatre);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
