/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mzaper.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *
 * @author mirko
 */
public class ActorTransferable implements Transferable {

    public static final DataFlavor ACTOR_FLAVOR = new DataFlavor(String.class, "Actor");
    public static final DataFlavor[] SUPPORTED_FLAVORS = {ACTOR_FLAVOR};
    
    private final String actor;

    
    public ActorTransferable(String actor) {
        this.actor=actor;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return SUPPORTED_FLAVORS;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(ACTOR_FLAVOR);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(ACTOR_FLAVOR)) {
            return actor;
        } 
        throw new UnsupportedFlavorException(flavor);  
    }
    
}
