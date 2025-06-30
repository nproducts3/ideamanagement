-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS evidence_tags;
DROP TABLE IF EXISTS evidence;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS idea_tags;
DROP TABLE IF EXISTS ideas;
DROP TABLE IF EXISTS deployments;
DROP TABLE IF EXISTS environments;
DROP TABLE IF EXISTS api_endpoints;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS tags;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create api_endpoints table
CREATE TABLE IF NOT EXISTS api_endpoints (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    method VARCHAR(10) NOT NULL,
    path VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    version VARCHAR(20) NOT NULL,
    last_tested DATE,
    response_time_ms INT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create ideas table
CREATE TABLE IF NOT EXISTS ideas (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    priority VARCHAR(20),
    status VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    upvotes INT NOT NULL DEFAULT 0,
    comments INT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create tags table
CREATE TABLE IF NOT EXISTS tags (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create idea_tags junction table
CREATE TABLE IF NOT EXISTS idea_tags (
    idea_id VARCHAR(36) NOT NULL,
    tag VARCHAR(50) NOT NULL,
    PRIMARY KEY (idea_id, tag),
    FOREIGN KEY (idea_id) REFERENCES ideas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create likes table
CREATE TABLE IF NOT EXISTS likes (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    idea_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (idea_id) REFERENCES ideas(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_idea (user_id, idea_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create environments table
CREATE TABLE IF NOT EXISTS environments (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create deployments table
CREATE TABLE IF NOT EXISTS deployments (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    environment VARCHAR(50) NOT NULL,
    version VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    deployed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    branch VARCHAR(100) NOT NULL,
    commit_hash VARCHAR(50) NOT NULL,
    health VARCHAR(20) NOT NULL,
    progress VARCHAR(3),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    employee_id VARCHAR(36),
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create evidence table with full structure
CREATE TABLE IF NOT EXISTS evidence (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    type VARCHAR(20) NOT NULL DEFAULT 'FILE',
    category VARCHAR(100) NOT NULL DEFAULT 'general',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    file_name VARCHAR(255),
    file_path VARCHAR(512),
    content_type VARCHAR(100),
    file_size BIGINT,
    url VARCHAR(512),
    idea_id VARCHAR(36),
    project_id VARCHAR(36) NOT NULL,
    uploaded_by VARCHAR(36) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    employee_id VARCHAR(36),
    FOREIGN KEY (idea_id) REFERENCES ideas(id) ON DELETE SET NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create evidence_tags table
CREATE TABLE IF NOT EXISTS evidence_tags (
    evidence_id VARCHAR(36) NOT NULL,
    tag VARCHAR(50) NOT NULL,
    PRIMARY KEY (evidence_id, tag),
    FOREIGN KEY (evidence_id) REFERENCES evidence(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 