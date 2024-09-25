package mk.ukim.finki.wp.kol2023.g2.service.impl;

import mk.ukim.finki.wp.kol2023.g2.model.Genre;
import mk.ukim.finki.wp.kol2023.g2.model.Movie;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidMovieIdException;
import mk.ukim.finki.wp.kol2023.g2.repository.MovieRepository;
import mk.ukim.finki.wp.kol2023.g2.service.DirectorService;
import mk.ukim.finki.wp.kol2023.g2.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final DirectorService directorService;

    public MovieServiceImpl(MovieRepository movieRepository, DirectorService directorService) {
        this.movieRepository = movieRepository;
        this.directorService = directorService;
    }

    @Override
    public List<Movie> listAllMovies() {
        return this.movieRepository.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return this.movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
    }

    @Override
    public Movie create(String name, String description, Double rating, Genre genre, Long director) {
        Movie movie = new Movie(name,description,rating,genre,this.directorService.findById(director));
        return this.movieRepository.save(movie);
    }

    @Override
    public Movie update(Long id, String name, String description, Double rating, Genre genre, Long director) {
        Movie movie = this.findById(id);
        movie.setName(name);
        movie.setDescription(description);
        movie.setRating(rating);
        movie.setGenre(genre);
        movie.setDirector(this.directorService.findById(director));

        return this.movieRepository.save(movie);
    }

    @Override
    public Movie delete(Long id) {
        Movie movie = this.findById(id);
        this.movieRepository.delete(movie);
        return movie;
    }

    @Override
    public Movie vote(Long id) {
        Movie movie = this.findById(id);
        movie.setVotes(movie.getVotes() + 1);
        return this.movieRepository.save(movie);
    }

    @Override
    public List<Movie> listMoviesWithRatingLessThenAndGenre(Double rating, Genre genre) {
        if(rating != null && genre != null){
            return this.movieRepository.findAllByGenreAndRatingLessThan(genre,rating);
        }else if(rating != null){
            return this.movieRepository.findAllByRatingLessThan(rating);
        }else if(genre != null){
            return this.movieRepository.findAllByGenre(genre);
        }else{
            return this.movieRepository.findAll();
        }
    }
}
