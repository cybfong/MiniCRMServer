-- ============================================
-- V2 - Insert demo data
-- ============================================

-- ============================================
-- USERS
-- ============================================

INSERT INTO users (user_name,
                   full_name,
                   email,
                   role,
                   enabled,
                   created_at,
                   updated_at)
VALUES ('admin',
        'System Administrator',
        'admin@minicrm.com',
        'ADMIN',
        true,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),

       ('john.smith',
        'John Smith',
        'john.smith@minicrm.com',
        'USER',
        true,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),

       ('mary.wong',
        'Mary Wong',
        'mary.wong@minicrm.com',
        'USER',
        true,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

-- ============================================
-- CUSTOMERS
-- ============================================

INSERT INTO customers (customer_name,
                       industry,
                       website,
                       phone,
                       email,
                       address,
                       city,
                       country,
                       status,
                       deleted,
                       created_at,
                       updated_at,
                       created_by,
                       updated_by)
VALUES ('Acme Corporation',
        'Technology',
        'https://www.acme.com',
        '+81-3-1234-5678',
        'contact@acme.com',
        '1-1 Marunouchi',
        'Tokyo',
        'Japan',
        'ACTIVE',
        false,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT user_id FROM users WHERE user_name = 'john.smith'),
        (SELECT user_id FROM users WHERE user_name = 'john.smith')),

       ('Global Finance Ltd',
        'Financial Services',
        'https://www.globalfinance.com',
        '+852-2345-6789',
        'info@globalfinance.com',
        '88 Queen''s Road',
        'Hong Kong',
        'Hong Kong',
        'ACTIVE',
        false,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT user_id FROM users WHERE user_name = 'mary.wong'),
        (SELECT user_id FROM users WHERE user_name = 'mary.wong')),

       ('Tokyo Trading Co.',
        'Trading',
        'https://www.tokyotrading.com',
        '+81-3-9876-5432',
        'sales@tokyotrading.com',
        '2-2 Shibuya',
        'Tokyo',
        'Japan',
        'LEAD',
        false,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT user_id FROM users WHERE user_name = 'john.smith'),
        (SELECT user_id FROM users WHERE user_name = 'john.smith')),

       ('Pacific Logistics',
        'Logistics',
        'https://www.pacificlogistics.com',
        '+65-6123-4567',
        'contact@pacificlogistics.com',
        '10 Marina Boulevard',
        'Singapore',
        'Singapore',
        'INACTIVE',
        false,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT user_id FROM users WHERE user_name = 'mary.wong'),
        (SELECT user_id FROM users WHERE user_name = 'mary.wong'));

-- ============================================
-- CONTACTS
-- ============================================

INSERT INTO contacts (first_name,
                      last_name,
                      email,
                      phone,
                      job_title,
                      deleted,
                      created_at,
                      updated_at,
                      customer_id)
VALUES ('David',
        'Brown',
        'david.brown@acme.com',
        '+81-90-1111-2222',
        'CTO',
        false,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT customer_id FROM customers WHERE customer_name = 'Acme Corporation')),

       ('Emily',
        'Johnson',
        'emily.johnson@acme.com',
        '+81-90-3333-4444',
        'Project Manager',
        false,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT customer_id FROM customers WHERE customer_name = 'Acme Corporation')),

       ('Michael',
        'Chan',
        'michael.chan@globalfinance.com',
        '+852-9123-4567',
        'Head of Technology',
        false,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT customer_id FROM customers WHERE customer_name = 'Global Finance Ltd')),

       ('Sarah',
        'Lee',
        'sarah.lee@tokyotrading.com',
        '+81-80-5555-6666',
        'Business Development Manager',
        false,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT customer_id FROM customers WHERE customer_name = 'Tokyo Trading Co.')),

       ('James',
        'Wilson',
        'james.wilson@pacificlogistics.com',
        '+65-8123-4567',
        'Operations Manager',
        false,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT customer_id FROM customers WHERE customer_name = 'Pacific Logistics'));

-- ============================================
-- NOTES
-- ============================================

INSERT INTO notes (note_text,
                   created_at,
                   updated_at,
                   created_by,
                   customer_id,
                   updated_by)
VALUES ('Initial meeting completed. Customer is interested in our enterprise solution.',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT user_id FROM users WHERE user_name = 'john.smith'),
        (SELECT customer_id FROM customers WHERE customer_name = 'Acme Corporation'),
        (SELECT user_id FROM users WHERE user_name = 'john.smith')),

       ('Technical requirements document has been requested from the customer.',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT user_id FROM users WHERE user_name = 'john.smith'),
        (SELECT customer_id FROM customers WHERE customer_name = 'Acme Corporation'),
        (SELECT user_id FROM users WHERE user_name = 'john.smith')),

       ('Customer requested a product demonstration next month.',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT user_id FROM users WHERE user_name = 'mary.wong'),
        (SELECT customer_id FROM customers WHERE customer_name = 'Global Finance Ltd'),
        (SELECT user_id FROM users WHERE user_name = 'mary.wong')),

       ('Potential new customer. Follow-up call required.',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        (SELECT user_id FROM users WHERE user_name = 'john.smith'),
        (SELECT customer_id FROM customers WHERE customer_name = 'Tokyo Trading Co.'),
        (SELECT user_id FROM users WHERE user_name = 'john.smith'));

-- ============================================
-- USER CREDENTIALS (all passwords_hash values are user_name + "@123" hashed with bcrypt 12 rounds)
-- ============================================

INSERT INTO user_credentials (user_id, password_hash, created_at, updated_at)
VALUES ((SELECT user_id FROM users WHERE user_name = 'admin'),
        '$2a$12$A71aRVXRWjE3pDB4FeINuO94z9cx.5S3oDHs3hI10XyPOcTM0qcPG',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ((SELECT user_id FROM users WHERE user_name = 'john.smith'),
        '$2a$12$t6e8BXoAFn8nvJVQWEHRzeogbEHO8I/BqA6vav6PMv2SzpIr371lq',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ((SELECT user_id FROM users WHERE user_name = 'mary.wong'),
        '$2a$12$cw97pC.IGTsQKL5T.jSZ7e6ir/0DpU3Q74fcPMFVFQGYqUMSwzWoe',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);