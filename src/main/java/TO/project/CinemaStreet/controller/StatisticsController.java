package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Categories;
import TO.project.CinemaStreet.service.HallMovieService;
import TO.project.CinemaStreet.service.MovieService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class StatisticsController
{
    @Autowired
    private ConfigurableApplicationContext springContext;
    @FXML private CategoryAxis xYearAxis ;
    @FXML private NumberAxis yYearAxis ;
    @FXML private LineChart<String, Number> yearChart ;
    List<String[]> yearRes= new ArrayList<>();

    @FXML private CategoryAxis xMovieAxis ;
    @FXML private NumberAxis yMovieAxis ;
    @FXML private BarChart<String, Number> movieChart ;
    List<String[]> movieRes= new ArrayList<>();

    @FXML private CategoryAxis xCategoryAxis ;
    @FXML private NumberAxis yCategoryAxis ;
    @FXML private BarChart<String, Number> categoryChart ;
    List<String[]> categoryRes = new ArrayList<>();

    private HallMovieService hallMovieService;
    private MovieService movieService;
    public StatisticsController(HallMovieService hallMovieService,MovieService movieService) {
        this.hallMovieService = hallMovieService;
        this.movieService = movieService;

    }

    @FXML
    public void initialize()
    {
        generateYearChart();
        generateCategoryChart();
        generateMovieChart();
    }

    @FXML
    public void exportYear() throws IOException
    {
        File csvFile = new File("yearStats.csv");
        FileWriter fileWriter = new FileWriter(csvFile);

        fileWriter.write("Year, Sold tickets\n");
        for (String[] data : yearRes) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                line.append(data[i]);
                if (i != data.length - 1) {
                    line.append(',');
                }
            }
            line.append("\n");
            fileWriter.write(line.toString());
        }
        fileWriter.close();
    }
    @FXML
    public void exportCategory() throws IOException
    {
        File csvFile = new File("categoryStats.csv");
        FileWriter fileWriter = new FileWriter(csvFile);

        fileWriter.write("Category, Number of movies\n");
        for (String[] data : categoryRes) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                line.append(data[i]);
                if (i != data.length - 1) {
                    line.append(',');
                }
            }
            line.append("\n");
            fileWriter.write(line.toString());
        }
        fileWriter.close();


    }
    @FXML
    public void exportMovie() throws IOException
    {
        File csvFile = new File("movieStats.csv");
        FileWriter fileWriter = new FileWriter(csvFile);

        fileWriter.write("Movie title, Sold tickets\n");
        for (String[] data : movieRes) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                line.append(data[i]);
                if (i != data.length - 1) {
                    line.append(',');
                }
            }
            line.append("\n");
            fileWriter.write(line.toString());
        }
        fileWriter.close();
    }

    void generateYearChart()
    {
        yearChart.setTitle("Roczna Sprzedaz Biletow");
        yYearAxis.setLabel("Ilosc sprzedanych biletow");
        xYearAxis.setLabel("Rok");

        Integer[] years = {2019,2020,2021,2022,2023};

        XYChart.Series series = new XYChart.Series();
        Map<Integer,Integer> map = new HashMap<>();  for (Integer year :  years) { map.put(year,0);  }
        hallMovieService.getAllHallMovies().forEach(hm->{
            Integer year = hm.getDate().getYear();
            if(map.containsKey(year)){
                Integer temp = map.get(year);
                map.put(year,temp+hm.getSeatsTaken());
            }
        });
        for (Integer key : map.keySet()) {
            series.getData().add(new XYChart.Data(key.toString(), map.get(key)));
            yearRes.add(new String[]{key.toString(),map.get(key).toString()});
        }
        yearChart.getData().add(series);
    }
    void generateCategoryChart()
    {
        categoryChart.setTitle("Popularnosc Kategorii");
        yCategoryAxis.setLabel("Ilosc filmow");
        xCategoryAxis.setLabel("Nazwa kategorii");

        XYChart.Series series = new XYChart.Series();
        Map<String,Integer> map = new HashMap<>(); //category,amount
        movieService.getAllMovies().forEach(m->{
            if(map.containsKey(m.getCategory()))
                map.put(m.getCategory(),map.get(m.getCategory())+1);
            else
                map.put(m.getCategory(),1);
        });
        for (String key : map.keySet()) {
            series.getData().add(new XYChart.Data(key,map.get(key)));
            categoryRes.add(new String[]{key,map.get(key).toString()});
        }
        categoryChart.getData().add(series);
    }
    void generateMovieChart()
    {
        movieChart.setTitle("Popularnosc Filmow");
        yMovieAxis.setLabel("Ilosc sprzedanych biletow");
        xMovieAxis.setLabel("Tytul filmu");

        XYChart.Series series = new XYChart.Series();
        movieService.getAllMovies().forEach(m->{
            AtomicInteger counter = new AtomicInteger();
            counter.set(0);
            hallMovieService.getHallMoviesByMovie(m).forEach(hm->{
                counter.addAndGet(hm.getSeatsTaken());
            });
            series.getData().add(new XYChart.Data(m.getName(),counter.intValue()));
            movieRes.add(new String[]{m.getName(),counter.toString()});
        });
        movieChart.getData().add(series);
    }
    public void throwError() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/NoPermissionView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Scene scene = new Scene(loader.load(), 300, 100);
            Stage stage = new Stage();
            stage.setTitle("Błąd!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

