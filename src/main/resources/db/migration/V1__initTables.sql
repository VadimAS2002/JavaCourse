CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    creation_date DATE NOT NULL,
    target_date DATE,
    completed BOOLEAN NOT NULL,
    deleted BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    timeStamp TIMESTAMP NOT NULL,
    read BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id)
);
