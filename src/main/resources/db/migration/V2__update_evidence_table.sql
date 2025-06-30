-- Update evidence table to match the entity structure
ALTER TABLE evidence 
ADD COLUMN title VARCHAR(255) NOT NULL DEFAULT 'Untitled',
ADD COLUMN description TEXT,
ADD COLUMN type VARCHAR(20) NOT NULL DEFAULT 'FILE',
ADD COLUMN category VARCHAR(100) NOT NULL DEFAULT 'general',
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
ADD COLUMN url VARCHAR(512),
ADD COLUMN project_id VARCHAR(36) NOT NULL,
ADD COLUMN uploaded_by VARCHAR(36) NOT NULL,
ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
ADD COLUMN employee_id VARCHAR(36),
ADD COLUMN file_size BIGINT,
ADD COLUMN content_type VARCHAR(100);

-- Rename filename column to file_name to match entity
ALTER TABLE evidence CHANGE COLUMN filename file_name VARCHAR(255);

-- Add foreign key constraints
ALTER TABLE evidence 
ADD CONSTRAINT fk_evidence_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
ADD CONSTRAINT fk_evidence_uploaded_by FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE CASCADE,
ADD CONSTRAINT fk_evidence_employee FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE SET NULL;

-- Create evidence_tags table
CREATE TABLE IF NOT EXISTS evidence_tags (
    evidence_id VARCHAR(36) NOT NULL,
    tag VARCHAR(50) NOT NULL,
    PRIMARY KEY (evidence_id, tag),
    FOREIGN KEY (evidence_id) REFERENCES evidence(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 