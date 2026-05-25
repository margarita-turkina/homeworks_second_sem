CREATE TABLE tasks (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    completed       BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP,
    due_date        DATE,
    priority        VARCHAR(20) NOT NULL DEFAULT 'MEDIUM'
);

CREATE TABLE task_tags (
    task_id BIGINT NOT NULL,
    tag     VARCHAR(100) NOT NULL,
    PRIMARY KEY (task_id, tag),
    CONSTRAINT fk_task_tags_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);

CREATE TABLE task_attachments (
    id               BIGSERIAL PRIMARY KEY,
    task_id          BIGINT NOT NULL,
    file_name        VARCHAR(255) NOT NULL,
    stored_file_name VARCHAR(255) NOT NULL,
    content_type     VARCHAR(100),
    size_bytes       BIGINT NOT NULL,
    uploaded_at      TIMESTAMP NOT NULL,
    CONSTRAINT fk_task_attachments_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);

CREATE INDEX idx_tasks_completed_priority ON tasks (completed, priority);
CREATE INDEX idx_tasks_due_date ON tasks (due_date);
CREATE INDEX idx_task_attachments_task_id ON task_attachments (task_id);
