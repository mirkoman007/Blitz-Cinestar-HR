/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mzaper.dal;

import hr.mzaper.dal.sql.SqlRepository;

/**
 *
 * @author mirko
 */
public class RepositoryFactory {

    public RepositoryFactory() {
        
    }
    
    public static Repository getRepository() throws Exception {
        return new SqlRepository();
    }
}
