/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mzaper.parsers.rss;

import hr.mzaper.factory.ParserFactory;
import hr.mzaper.factory.UrlConnectionFactory;
import hr.mzaper.model.Movie;
import hr.mzaper.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author mirko
 */
public class MovieParser {
    private static final String RSS_URL = "https://www.blitz-cinestar.hr/rss.aspx?najava=1";
    private static final int TIMEOUT = 10000;
    private static final String REQUEST_METHOD = "GET";
    private static final String ATTRIBUTE_URL = "url";
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final Random RANDOM = new Random();
    
    public static List<Movie> parse() throws IOException, XMLStreamException {
        List<Movie> movies = new ArrayList<>();
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL, TIMEOUT, REQUEST_METHOD);
        XMLEventReader reader = ParserFactory.createStaxParser(con.getInputStream());

        Optional<TagType> tagType = Optional.empty();
        Movie movie = null;
        StartElement startElement = null;
        
        
        File dir = new File(DIR);
        for (File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();
        
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    tagType = TagType.from(qName);
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (tagType.isPresent()) {
                        Characters characters = event.asCharacters();
                        String data = characters.getData().trim();
                        switch (tagType.get()) {
                            case ITEM:
                                movie = new Movie();
                                movies.add(movie);
                                break;
                            case TITLE:
                                if (movie != null && !data.isEmpty()) {
                                    movie.setTitle(data);
                                }
                                break;
                            case PUB_DATE:
                                if (movie != null && !data.isEmpty()) {
                                    LocalDateTime publishedDate = LocalDateTime.parse(data, DATETIME_FORMATTER);
                                    movie.setPubDate(publishedDate);
                                }
                                break;
                            case DESCRIPTION:
                                if (movie != null && !data.isEmpty()) {
                                    movie.setDescript(data);
                                }
                                break;
                            case ORIGNAZIV:
                                if (movie != null && !data.isEmpty()) {
                                    movie.setOriginalName(data);
                                }
                                break;
                            case REDATELJ:
                                if (movie != null && !data.isEmpty()) {
                                    movie.setDirectorNameSurname(data);
                                }
                                break;
                            case GLUMCI:
                                if (movie != null && !data.isEmpty()) {
                                    List<String> actors = new ArrayList<>(Arrays.asList(data.split(",")));
                                    actors.replaceAll(String::trim);
                                    movie.setActorNameSurname(actors);
                                }
                                break;
                            case TRAJANJE:
                                if (movie != null && !data.isEmpty()) {
                                    movie.setDuration(Integer.parseInt(data));
                                }
                                break;
                            case ZANR:
                                if (movie != null && !data.isEmpty()) {
                                    movie.setGenre(data);
                                }
                                break;
                            case PLAKAT:
                                if (movie != null && startElement != null) {
                                    handlePoster(movie, data);
                                }
                                break;
                            case LINK:
                                if (movie != null && !data.isEmpty()) {
                                    movie.setLink(data);
                                }
                                break;
                        }
                    }
                    break;
            }
        }
        
        return movies;
    }

    private static void handlePoster(Movie movie, String posterUrl) throws IOException {

        String ext = posterUrl.substring(posterUrl.lastIndexOf("."));
        if (ext.length() > 4) {
            ext = EXT;
        }
        String posterName = Math.abs(RANDOM.nextInt()) + ext;
        String localPosterPath = DIR + File.separator + posterName;

        FileUtils.copyFromUrl(addChar(posterUrl,'s',4), localPosterPath);
        movie.setPoster(localPosterPath);
    }
    
    public static String addChar(String str, char ch, int position) {
        int len = str.length();
        char[] updatedArr = new char[len + 1];
        str.getChars(0, position, updatedArr, 0);
        updatedArr[position] = ch;
        str.getChars(position, len, updatedArr, position + 1);
        return new String(updatedArr);
    }
    
    
    private enum TagType {

        ITEM("item"), 
        TITLE("title"),         
        PUB_DATE("pubDate"),
        DESCRIPTION("description"), 
        ORIGNAZIV("orignaziv"), 
        REDATELJ("redatelj"), 
        GLUMCI("glumci"), 
        TRAJANJE("trajanje"), 
        ZANR("zanr"), 
        PLAKAT("plakat"),
        LINK("link"),
        POCETAK("pocetak");


        private final String name;

        private TagType(String name) {
            this.name = name;
        }

        private static Optional<TagType> from(String name) {
            for (TagType value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }

}
