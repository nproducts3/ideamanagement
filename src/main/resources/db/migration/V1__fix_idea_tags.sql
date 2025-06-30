-- Drop the tag_id column if it exists
ALTER TABLE idea_tags DROP COLUMN IF EXISTS tag_id;

-- Ensure the correct schema for idea_tags
-- (Optional: add primary key and foreign key if not present)
-- ALTER TABLE idea_tags ADD PRIMARY KEY (idea_id, tag);
-- ALTER TABLE idea_tags ADD CONSTRAINT fk_idea_tags_idea FOREIGN KEY (idea_id) REFERENCES ideas(id) ON DELETE CASCADE; 