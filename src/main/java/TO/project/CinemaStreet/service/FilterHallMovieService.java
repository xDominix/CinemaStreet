package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.Categories;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.Movie;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class FilterHallMovieService {
    private ObservableList<Predicate<HallMovie>> predicates = FXCollections.observableArrayList();

    private FilteredList<HallMovie> filteredList;
    private ObservableList<HallMovie>hallMovies ;

    private HallMovieService hallMovieService;
    public FilterHallMovieService(HallMovieService hallMovieService) {
        this.hallMovieService = hallMovieService;
        hallMovies = FXCollections.observableList(hallMovieService.getAllHallMovies());
        filteredList = new FilteredList<>(hallMovies, p -> true);
        filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> predicates.isEmpty() ? null : new Predicate<HallMovie>() {
            @Override
            public boolean test(HallMovie item) {
                for (Predicate<HallMovie> predicate : predicates) {
                    if (!predicate.test(item)) {
                        return false;
                    }
                }
                return true;
            }
        }, predicates));
    }
    public void addHallMovieToFilteredList(HallMovie hallMovie){
        hallMovies.add(hallMovie);
    }
    public void removeHallMovieFromFilteredList(HallMovie hallMovie){
        hallMovies.remove(hallMovie);
    }

    //    returns FilteredList<Movie> with all movies and predicates set (make sure to call in initialize method)
    public FilteredList<HallMovie> getFilteredList(){
//        need to reset, alternative is to make sure new service is created every time view is loaded
        predicates = FXCollections.observableArrayList();
        hallMovies = FXCollections.observableList(hallMovieService.getAllHallMovies());
        filteredList = new FilteredList<>(hallMovies, p -> true);
        filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> predicates.isEmpty() ? null : new Predicate<HallMovie>() {
            @Override
            public boolean test(HallMovie item) {
                for (Predicate<HallMovie> predicate : predicates) {
                    if (!predicate.test(item)) {
                        return false;
                    }
                }
                return true;
            }
        }, predicates));
        return filteredList;
    }


    //    returns a listener for input when searching by title (searchTextField)
    public ChangeListener<String> getNameInputListener(){
        return new ChangeListener<String>() {
            private Predicate<HallMovie> predicate;
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Predicate<HallMovie> oldPredicate = predicate;
                predicate = hallMovie -> hallMovie.getMovie().getName().toLowerCase().contains(newValue.toLowerCase());
                replace(predicates,oldPredicate, predicate);
            }
        };
    }
    //    returns a listener for input when searching by category (searchByCategoryComboBox)
    public ChangeListener<Categories> getCategoryInputListener(){
        return new ChangeListener<>() {
            private Predicate<HallMovie> predicate;
            @Override
            public void changed(ObservableValue<? extends Categories> observable, Categories oldValue, Categories newValue) {
                Predicate<HallMovie> oldPredicate = predicate;
                if(newValue == null){
                    predicate = hallMovie -> true;
                }else{
//                    check if isnt null and movie category
                    predicate = hallMovie -> {
                        if(hallMovie.getMovie().getCategory() == null){
                            return false;
                        }
                        return hallMovie.getMovie().getCategory().equals(newValue.name());
                    };

                }
                replace(predicates,oldPredicate, predicate);
            }
        };
    }
    private static <T> void replace(ObservableList<T> list, T oldItem, T newItem) {
        if (oldItem == null) {
            if (newItem != null) {
                list.add(newItem);
            }
        } else {
            int index = list.indexOf(oldItem);
            if (newItem == null) {
                if (index >= 0) {
                    list.remove(index);
                }
            } else {
                if (index >= 0) {
                    list.set(index, newItem);
                } else {
                    list.add(newItem);
                }
            }
        }
    }
}