package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.service.HallMovieService;
import TO.project.CinemaStreet.service.HallService;
import TO.project.CinemaStreet.service.MovieService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.*;

@Controller
public class StatisticsController
{

    @FXML
    private CategoryAxis xAxis ;
    @FXML private NumberAxis yAxis ;
    @FXML private LineChart<String, Number> lineChart ;

    private HallMovieService hallMovieService;
    private MovieService movieService;
    public StatisticsController(HallMovieService hallMovieService,MovieService movieService) {
        this.hallMovieService = hallMovieService;
        this.movieService = movieService;

    }

    @FXML
    public void initialize() {
        generateLineChart();
    }

    void generateLineChart()
    {
        lineChart.setTitle("Sprzedanych biletow");
        yAxis.setLabel("Ilosc");
        xAxis.setLabel("Rok");

        Integer[] years = {2019,2020,2021,2022,2023};

        movieService.getAllMovies().forEach(m->{
            XYChart.Series series = new XYChart.Series();
            series.setName(m.getName());
            Map<Integer,Integer> map = new HashMap<>();  for (Integer year :  years) { map.put(year,0);  }
            hallMovieService.getHallMoviesByMovie(m).forEach(hm->{
                Integer year = hm.getDate().getYear();
                if(map.containsKey(year)){
                    Integer temp = map.get(year);
                    map.put(year,temp+hm.getSeatsTaken());
                }
            });
            for (Integer key : map.keySet()) {
                series.getData().add(new XYChart.Data(key.toString(), map.get(key)));
            }
            lineChart.getData().add(series);
        });
    }
}
