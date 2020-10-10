/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mzaper.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author mirko
 */
public class Movie {
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private int iDMovie;
    private String title;
    private LocalDateTime pubDate;
    private String descript;
    private String originalName;
    private String directorNameSurname;
    private List<String> actorNameSurname;
    private int duration;
    private String genre;
    private String poster;
    private String link;
    private LocalDateTime inCinemasFrom;
    

    public Movie() {
    }

    public Movie(String title, LocalDateTime pubDate, String descript, String originalName, String directorNameSurname, List<String> actorNameSurname, int duration, String genre, String poster, String link, LocalDateTime inCinemasFrom) {
        this.title = title;
        this.pubDate = pubDate;
        this.descript = descript;
        this.originalName = originalName;
        this.directorNameSurname = directorNameSurname;
        this.actorNameSurname = actorNameSurname;
        this.duration = duration;
        this.genre = genre;
        this.poster = poster;
        this.link = link;
        this.inCinemasFrom = inCinemasFrom;
    }

    

    public Movie(String title, String descript, String originalName, int duration, String genre, String poster, String link) {
        this.title = title;
        this.descript = descript;
        this.originalName = originalName;
        this.duration = duration;
        this.genre = genre;
        this.poster = poster;
        this.link = link;
    }

    public Movie(String title, LocalDateTime pubDate, String descript, String originalName, int duration, String genre, String poster, String link) {
        this.title = title;
        this.pubDate = pubDate;
        this.descript = descript;
        this.originalName = originalName;
        this.duration = duration;
        this.genre = genre;
        this.poster = poster;
        this.link = link;
    }




    
    
    
    public Movie(int iDMovie, String title, LocalDateTime pubDate, String descript, String originalName, String directorNameSurname, List<String> actorNameSurname, int duration, String genre, String poster, String link, LocalDateTime inCinemasFrom) {
        this(title,pubDate,descript,originalName,directorNameSurname,actorNameSurname,duration,genre,poster,link,inCinemasFrom);
        this.iDMovie = iDMovie;
    }

    public void setActorNameSurname(List<String> actorNameSurname) {
        this.actorNameSurname = actorNameSurname;
    }

    public List<String> getActorNameSurname() {
        return actorNameSurname;
    }

    public int getiDMovie() {
        return iDMovie;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getPubDate() {
        return pubDate;
    }

    public String getDescript() {
        return descript;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getDirectorNameSurname() {
        return directorNameSurname;
    }

    public int getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public String getPoster() {
        return poster;
    }

    public String getLink() {
        return link;
    }

    public LocalDateTime getInCinemasFrom() {
        return inCinemasFrom;
    }

    public void setiDMovie(int iDMovie) {
        this.iDMovie = iDMovie;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPubDate(LocalDateTime pubDate) {
        this.pubDate = pubDate;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setDirectorNameSurname(String directorNameSurname) {
        this.directorNameSurname = directorNameSurname;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setInCinemasFrom(LocalDateTime inCinemasFrom) {
        this.inCinemasFrom = inCinemasFrom;
    }

    
    
}
