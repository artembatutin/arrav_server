package com.rageps.net.database;

import com.rageps.Arrav;
import com.rageps.GameConstants;
import com.rageps.net.database.pool.ConnectionPool;

/**
 * A repository of databases
 * 
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
public class DatabaseRepository {

    /**
     * The scores database connection.
     */
    private Database score;

    /**
     * The donation database connection.
     */
    private Database donation;

    /**
     * The punishments database connection.
     */
    private Database punishments;
    
    
    public DatabaseRepository() {

        if(GameConstants.SQL_CONNECTED) {
            int amtCpu = Runtime.getRuntime().availableProcessors();
            try {
                score = new Database(Arrav.DEBUG ? "192.99.101.90" : "127.0.0.1", "score", Arrav.DEBUG ? "edge_avro" : "root", Arrav.DEBUG ? "%GL5{)hAJBU(MB3h" : "rooty412JlW", amtCpu);
            } catch(Exception e) {
                e.printStackTrace();
            }
            try {
                donation = new Database(Arrav.DEBUG ? "192.99.101.90" : "127.0.0.1", "store", Arrav.DEBUG ? "edge_avro" : "root", Arrav.DEBUG ? "%GL5{)hAJBU(MB3h" : "rooty412JlW", amtCpu);
            } catch(Exception e) {
                e.printStackTrace();
            }

            try {
                punishments = new Database(Arrav.DEBUG ? "192.99.101.90" : "127.0.0.1", "punishment", Arrav.DEBUG ? "edge_avro" : "root", Arrav.DEBUG ? "%GL5{)hAJBU(MB3h" : "rooty412JlW", amtCpu);
            } catch(Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Gets the highscores database connection pool.
     */
    public ConnectionPool getScore() {
        if(score == null)
            return null;
        return score.getPool();
    }

    /**
     * Gets the donation database connection pool.
     */
    public ConnectionPool getDonation() {
        if(donation == null)
            return null;
        return donation.getPool();
    }
    /**
     * Gets the punishments database connection pool.
     */
    public ConnectionPool getPunishment() {
        if(punishments == null)
            return null;
        return punishments.getPool();
    }
    
}
