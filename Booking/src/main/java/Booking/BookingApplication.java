package Booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import Booking.service.ShowService;
import Booking.service.TheatreService;

@SpringBootApplication
@EnableAsync
public class BookingApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BookingApplication.class, args);

		TheatreService theatreBean = context.getBean(TheatreService.class);
		theatreBean.readAndPopulateTheatres();

        ShowService showBean = context.getBean(ShowService.class);
		showBean.readAndPopulateShows();
		
	}

	@Bean
    public RestTemplate getRestTemplate() {
      return new RestTemplate();
   }

}
