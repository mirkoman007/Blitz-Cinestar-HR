/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mzaper.model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author mirko
 */
public class MovieTableModel extends AbstractTableModel{

    private static final String[] COLUMN_NAMES = {"ID Movie", "Title", "Published date", "Description", "Original name", "Director","Duration","Genre","Link"};
    
    private List<Movie> movies;

    public MovieTableModel(List<Movie> movies) {
        this.movies = movies;
    }

    public MovieTableModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column]; 
    }
    
    
    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
//        return Movie.class.getDeclaredFields().length - 1;
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return movies.get(rowIndex).getiDMovie();
            case 1:
                return movies.get(rowIndex).getTitle();
            case 2:
                return movies.get(rowIndex).getPubDate();
            case 3:
                return movies.get(rowIndex).getDescript();
            case 4:
                return movies.get(rowIndex).getOriginalName();
            case 5:
                return movies.get(rowIndex).getDirectorNameSurname();
            case 6:
                return movies.get(rowIndex).getDuration();
            case 7:
                return movies.get(rowIndex).getGenre();
            case 8:
                return movies.get(rowIndex).getLink();
            default:
                throw new RuntimeException("No such column");
        }
    }
    
}
