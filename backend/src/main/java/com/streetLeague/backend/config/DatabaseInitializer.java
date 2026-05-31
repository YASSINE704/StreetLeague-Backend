package com.streetLeague.backend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Initialize database schema adjustments after Flyway baseline.
 * This ensures the match_id column in player_stats is nullable for AI simulations.
 */
@Component
public class DatabaseInitializer {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initializeDatabase() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            
            // Make match_id nullable to support AI simulation predictions
            String alterTableSQL = "ALTER TABLE player_stats MODIFY COLUMN match_id BIGINT NULL";
            
            try {
                statement.execute(alterTableSQL);
                System.out.println("✓ Database schema updated: match_id column is now nullable");
            } catch (Exception e) {
                // Column may already be nullable, ignore this error
                if (!e.getMessage().contains("same type")) {
                    System.out.println("ℹ match_id column update: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("✗ Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
