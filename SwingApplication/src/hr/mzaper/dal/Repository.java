/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mzaper.dal;

import hr.mzaper.model.Account;
import hr.mzaper.model.Movie;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author mirko
 */
public interface Repository {
    int createMovie(Movie movie) throws Exception;
    void createMovies(List<Movie> movies) throws Exception;   
    void updateMovie(int id, Movie data) throws Exception;
    void updateMovieActors(int id, List<String> data) throws Exception;
    void updateMovieDirector(int id, String data) throws Exception;
    void deleteMovie(int id) throws Exception;
    Optional<Movie> selectMovie(int id) throws Exception;
    List<Movie> selectMovies() throws Exception;
    
    int createActor(int idMovie,String actor) throws Exception;
    void createActor(String data) throws Exception;
    void updateActor(String selectedActor, String newActor) throws Exception;
    void deleteActor(String selectedActor) throws Exception;
    List<String> selectActors(int idMovie) throws Exception;
    List<String> selectActors() throws Exception;

    
    void createDirector (String data) throws Exception;
    void updateDirector(String selectedDirector, String newDirector) throws Exception;
    int deleteDirector(String selectedActor) throws Exception;
    List<String> selectDirectors() throws Exception;
    
    
    int createAccount(Account account) throws Exception;
    int selectAccount(Account account) throws Exception;
    
    void clearDatabase() throws Exception;

    



}
