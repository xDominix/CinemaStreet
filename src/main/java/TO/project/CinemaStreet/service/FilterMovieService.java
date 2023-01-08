package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.Categories;
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
public class FilterMovieService {
    private ObservableList<Predicate<Movie>> predicates = FXCollections.observableArrayList();

    private FilteredList<Movie> filteredList;
    private ObservableList<Movie>movies ;

    private MovieService movieService;
    private RecommendedMovieService recommendedMovieService;
    public FilterMovieService(MovieService movieService, RecommendedMovieService recommendedMovieService) {
        this.movieService = movieService;
        this.recommendedMovieService = recommendedMovieService;
        movies = FXCollections.observableList(movieService.getAllMovies());
        filteredList = new FilteredList<>(movies, p -> true);
        filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> predicates.isEmpty() ? null : new Predicate<Movie>() {
            @Override
            public boolean test(Movie item) {
                for (Predicate<Movie> predicate : predicates) {
                    if (!predicate.test(item)) {
                        return false;
                    }
                }
                return true;
            }
        }, predicates));
    }
    public void addMovieToFilteredList(Movie movie){
        movies.add(movie);
    }
    public void removeMovieFromFilteredList(Movie movie){
        movies.remove(movie);
    }

//    returns FilteredList<Movie> with all movies and predicates set (make sure to call in initialize method)
    public FilteredList<Movie> getFilteredList(){
//        need to reset, alternative is to make sure new service is created every time view is loaded
        predicates = FXCollections.observableArrayList();
        movies = FXCollections.observableList(movieService.getAllMovies());
        filteredList = new FilteredList<>(movies, p -> true);
        filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> predicates.isEmpty() ? null : new Predicate<Movie>() {
            @Override
            public boolean test(Movie item) {
                for (Predicate<Movie> predicate : predicates) {
                    if (!predicate.test(item)) {
                        return false;
                    }
                }
                return true;
            }
        }, predicates));
        return filteredList;
    }

    public ChangeListener<Boolean> getCheckBoxListener()
    {
        return new ChangeListener<Boolean>() {
            private  Predicate<Movie> predicate;
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Predicate<Movie> oldPredicate = predicate;
                if (newValue)
                {
                    predicate = movie -> recommendedMovieService.isRecommended(movie);
                }
                else {
                    predicate = movie -> true;
                }
                replace(predicates,oldPredicate, predicate);
            }
        };
    }


//    returns a listener for input when searching by title (searchTextField)
    public ChangeListener<String> getNameInputListener(){
        return new ChangeListener<String>() {
            private Predicate<Movie> predicate;
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Predicate<Movie> oldPredicate = predicate;
                predicate = movie -> movie.getName().toLowerCase().contains(newValue.toLowerCase());
                replace(predicates,oldPredicate, predicate);
            }
        };
    }
//    returns a listener for input when searching by category (searchByCategoryComboBox)
    public ChangeListener<Categories> getCategoryInputListener(){
        return new ChangeListener<>() {
            private Predicate<Movie> predicate;
            @Override
            public void changed(ObservableValue<? extends Categories> observable, Categories oldValue, Categories newValue) {
                Predicate<Movie> oldPredicate = predicate;
                if(newValue == null){
                    predicate = movie -> true;
                }else{
                    predicate = movie -> movie.getCategory().equals(newValue.name());
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
