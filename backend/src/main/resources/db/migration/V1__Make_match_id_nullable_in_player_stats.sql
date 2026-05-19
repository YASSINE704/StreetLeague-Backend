-- Make match_id column nullable in player_stats table
-- This allows saving AI simulation predictions that are not tied to actual matches

ALTER TABLE player_stats 
MODIFY COLUMN match_id BIGINT NULL;
