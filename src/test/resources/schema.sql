CREATE TABLE IF NOT EXISTS tasks (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    due_date    TIMESTAMP NULL,
    created_by  BIGINT NULL,

    status      VARCHAR(20) NULL,
    priority    VARCHAR(20) NULL,

    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated     BOOLEAN NULL,

    CONSTRAINT chk_tasks_status
        CHECK (status IN ('PENDING','IN_PROGRESS','DONE') OR status IS NULL),
    CONSTRAINT chk_tasks_priority
        CHECK (priority IN ('LOW','MEDIUM','HIGH') OR priority IS NULL)
);