/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mzaper.dal.sql;

import hr.mzaper.dal.Repository;
import hr.mzaper.model.Account;
import hr.mzaper.model.Movie;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 *
 * @author mirko
 */
public class SqlRepository implements Repository{
    
    private static final String ID_MOVIE = "IDMovie";
    private static final String TITLE = "Title";
    private static final String PUBLISHED_DATE = "PubDate";
    private static final String DESCRIPTION = "Descript";
    private static final String ORIGINAL_NAME = "OriginalName";
    private static final String DIRECTOR_NAME_SURNAME = "DirectorNameSurname";
    private static final String ACTOR_NAME_SURNAME = "ActorNameSurname";
    private static final String NAME_SURNAME = "NameSurname";
    private static final String DURATION = "Duration";
    private static final String GENRE = "Genre";
    private static final String POSTER = "Poster";
    private static final String LINK = "Link";

    
    private static final String CREATE_MOVIE = "{ CALL createMovie (?,?,?,?,?,?,?,?,?,?) }";
    private static final String UPDATE_MOVIE = "{ CALL updateMovie (?,?,?,?,?,?,?,?,?,?) }";
    private static final String UPDATE_MOVIE_DIRECTOR = "{ CALL updateMovieDirector (?,?) }";
    private static final String DELETE_MOVIE = "{ CALL deleteMovie (?) }";
    private static final String SELECT_MOVIE = "{ CALL selectMovie (?) }";
    private static final String SELECT_MOVIES = "{ CALL selectMovies }";
    private static final String CREATE_ACTOR_MOVIE = "{ CALL createActor (?,?,?) }";
    private static final String CREATE_ACTOR = "{ CALL createActorOnly (?) }";
    private static final String UPDATE_ACTOR = "{ CALL updateActor (?,?) }";
    private static final String DELETE_ACTOR = "{ CALL deleteActor (?) }";
    private static final String SELECT_ALL_ACTORS = "{ CALL selectAllActors }";
    private static final String SELECT_ACTORS = "{ CALL selectActors (?) }";
    private static final String CREATE_DIRECTOR = "{ CALL createDirector (?) }";
    private static final String UPDATE_DIRECTOR = "{ CALL updateDirector (?,?) }";
    private static final String DELETE_DIRECTOR = "{ CALL deleteDirector (?,?) }";
    private static final String SELECT_DIRECTORS = "{ CALL selectDirectors }";
    private static final String CREATE_ACCOUNT = "{ CALL createAccount (?,?,?) }";
    private static final String SELECT_ACCOUNT = "{ CALL selectAccount (?,?,?) }";
    private static final String CLEAR_DATABASE = "{ CALL clearDatabase }";




    @Override
    public int createMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {
            
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getPubDate().format(Movie.DATETIME_FORMATTER));
            stmt.setString(3, movie.getDescript());
            stmt.setString(4, movie.getOriginalName());
            stmt.setString(5, movie.getDirectorNameSurname());
            stmt.setInt(6, movie.getDuration());
            stmt.setString(7, movie.getGenre());
            stmt.setString(8, movie.getPoster());
            stmt.setString(9, movie.getLink());
//            stmt.setString(10, movie.getInCinemasFrom().format(Movie.DATE_FORMATTER));
            stmt.registerOutParameter(10, Types.INTEGER);


            stmt.executeUpdate();
            return stmt.getInt(10);
        }
    }

    @Override
    public void createMovies(List<Movie> movies) throws Exception {
        for (Movie movie:movies){
            int idMovie=createMovie(movie);
            
            if(movie.getActorNameSurname()!=null){
                    for(String actor:movie.getActorNameSurname()){
                    createActor(idMovie,actor);
                }
            }
        }
    }
    

    @Override
    public void updateMovie(int id, Movie data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_MOVIE)) {

            stmt.setInt(1, id);            
            stmt.setString(2, data.getTitle());
            stmt.setString(3, data.getPubDate().format(Movie.DATETIME_FORMATTER));
            stmt.setString(4, data.getDescript());
            stmt.setString(5, data.getOriginalName());
            stmt.setString(6, data.getDirectorNameSurname());
            stmt.setInt(7, data.getDuration());
            stmt.setString(8, data.getGenre());
            stmt.setString(9, data.getPoster());
            stmt.setString(10, data.getLink());

            stmt.executeUpdate();
        }
    }

    @Override
    public void updateMovieActors(int id, List<String> data) throws Exception {
        if(data!=null){
            for(String actor:data){
                createActor(id,actor);
            }
        }
    }
    
    @Override
    public void updateMovieDirector(int id, String data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_MOVIE_DIRECTOR)) {

            stmt.setInt(1, id);            
            stmt.setString(2, data);

            stmt.executeUpdate();
        }
    }
    
    @Override
    public void deleteMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_MOVIE)) {
            
            stmt.setInt(1, id);
            
            stmt.executeUpdate();
        } 
    }

    @Override
    public Optional<Movie> selectMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE)) {
           
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    return Optional.of(new Movie(
                            rs.getInt(ID_MOVIE),
                                rs.getString(TITLE),
                                LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Movie.DATETIME_FORMATTER),
                                rs.getString(DESCRIPTION),
                                rs.getString(ORIGINAL_NAME),
                                rs.getString(DIRECTOR_NAME_SURNAME),
                                null,
                                rs.getInt(DURATION),
                                rs.getString(GENRE),
                                rs.getString(POSTER),
                                rs.getString(LINK),
                                null));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Movie> selectMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES);
                ResultSet rs = stmt.executeQuery()) {
          
            while (rs.next()) {
                movies.add(new Movie(
                                rs.getInt(ID_MOVIE),
                                rs.getString(TITLE),
                                LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Movie.DATETIME_FORMATTER),
                                rs.getString(DESCRIPTION),
                                rs.getString(ORIGINAL_NAME),
                                rs.getString(DIRECTOR_NAME_SURNAME),
                                null,
                                rs.getInt(DURATION),
                                rs.getString(GENRE),
                                rs.getString(POSTER),
                                rs.getString(LINK),
                                null));
            }
        } 
        
        for (Movie movie:movies){
            movie.setActorNameSurname(selectActors(movie.getiDMovie()));
        }
        
        
        return movies;
    }

    @Override
    public int createActor(int idMovie,String actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ACTOR_MOVIE)) {
            
            stmt.setInt(1, idMovie);
            stmt.setString(2, actor);
            stmt.registerOutParameter(3, Types.INTEGER);
            
            stmt.executeUpdate();
            return stmt.getInt(3);
        }
    }
    
    
    @Override
    public void createActor(String data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ACTOR)) {

            stmt.setString(1, data);   

            stmt.executeUpdate();
        }
    }



    @Override
    public void updateActor(String selectedActor, String newActor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_ACTOR)) {

            stmt.setString(1, selectedActor);            
            stmt.setString(2, newActor);

            stmt.executeUpdate();
        }
    }
    

    @Override
    public void deleteActor(String selectedActor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ACTOR)) {

            stmt.setString(1, selectedActor);   

            stmt.executeUpdate();
        }
    }
   
    
    @Override
    public List<String> selectActors(int idMovie) throws Exception {
        List<String> actors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTORS)) {
           
            stmt.setInt(1, idMovie);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    actors.add(rs.getString(ACTOR_NAME_SURNAME));
                }
            }
        }
        
        return actors;
    }

    @Override
    public void clearDatabase() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CLEAR_DATABASE)) {
            
            stmt.executeUpdate();
        } 
    }

    @Override
    public List<String> selectActors() throws Exception {
        List<String> actors = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ALL_ACTORS);
                ResultSet rs = stmt.executeQuery()) {
          
            while (rs.next()) {
                actors.add(rs.getString(NAME_SURNAME));
            }
        } 
        
        
        return actors;
    }


    @Override
    public void createDirector(String data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_DIRECTOR)) {

            stmt.setString(1, data);   

            stmt.executeUpdate();
        }
    }

    
    
    @Override
    public void updateDirector(String selectedDirector, String newDirector) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_DIRECTOR)) {

            stmt.setString(1, selectedDirector);            
            stmt.setString(2, newDirector);

            stmt.executeUpdate();
        }
    }
    
    @Override
    public int deleteDirector(String selectedActor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_DIRECTOR)) {

            stmt.setString(1, selectedActor);  
            stmt.registerOutParameter(2, Types.INTEGER); 

            stmt.executeUpdate();
            return stmt.getInt(2);
        }
    }


    @Override
    public List<String> selectDirectors() throws Exception {
        List<String> directors = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_DIRECTORS);
                ResultSet rs = stmt.executeQuery()) {
          
            while (rs.next()) {
                directors.add(rs.getString(NAME_SURNAME));
            }
        } 
        
        
        return directors;
    }

    @Override
    public int createAccount(Account account) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ACCOUNT)) {
            
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.registerOutParameter(3, Types.INTEGER);
            
            stmt.executeUpdate();
            return stmt.getInt(3);
        }
    }

    @Override
    public int selectAccount(Account account) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACCOUNT)) {
            
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.registerOutParameter(3, Types.INTEGER);
            
            stmt.executeUpdate();
            return stmt.getInt(3);
        }
    }


        
}
