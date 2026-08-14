DELETE FROM cards;
DELETE FROM categories;

ALTER TABLE categories ALTER COLUMN id RESTART WITH 1;

INSERT INTO categories (name, created_at, updated_at, created_by, updated_by)
VALUES
    ('Shoes', NOW(), NOW(), 'mockUser', 'mockUser'),
    ('Clothes', NOW(), NOW(), 'mockUser', 'mockUser'),
    ('Hats', NOW(), NOW(), 'mockUser', 'mockUser');