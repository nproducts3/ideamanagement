-- Insert a default admin user if not exists
INSERT INTO users (id, username, email, full_name, role, created_at)
SELECT '00000000-0000-0000-0000-000000000001', 'admin', 'admin@example.com', 'System Administrator', 'ADMIN', NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Insert some default tags if they don't exist
INSERT INTO tags (id, name)
SELECT '00000000-0000-0000-0000-000000000002', 'feature'
WHERE NOT EXISTS (SELECT 1 FROM tags WHERE name = 'feature');

INSERT INTO tags (id, name)
SELECT '00000000-0000-0000-0000-000000000003', 'bug'
WHERE NOT EXISTS (SELECT 1 FROM tags WHERE name = 'bug');

INSERT INTO tags (id, name)
SELECT '00000000-0000-0000-0000-000000000004', 'enhancement'
WHERE NOT EXISTS (SELECT 1 FROM tags WHERE name = 'enhancement'); 