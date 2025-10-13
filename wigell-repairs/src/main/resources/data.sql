-- ===============================================
-- TECHNICIANS
-- ===============================================
INSERT INTO technicians (name, expertise) VALUES
('Anna Karlsson', 'Car Repairs'),
('Björn Lind', 'Electronics'),
('Clara Svensson', 'Appliances'),
('David Holm', 'General Maintenance');

-- ===============================================
-- CUSTOMERS
-- ===============================================
INSERT INTO customers (username, email) VALUES
('john_doe', 'john@example.com'),
('emma_johansson', 'emma@example.com'),
('peter_nilsson', 'peter@example.com'),
('sofia_larsson', 'sofia@example.com');

-- ===============================================
-- REPAIR SERVICES
-- ===============================================
INSERT INTO repair_services (name, type, price_sek, technician_id) VALUES
('Brake Replacement', 'CAR', 2500, 1),
('Engine Diagnostics', 'CAR', 1800, 1),
('Smartphone Screen Repair', 'ELECTRONICS', 1200, 2),
('Laptop Motherboard Repair', 'ELECTRONICS', 2200, 2),
('Washing Machine Service', 'APPLIANCES', 1500, 3),
('Refrigerator Gas Refill', 'APPLIANCES', 1300, 3),
('General Repair Consultation', 'OTHER', 800, 4);

-- ===============================================
-- BOOKINGS
-- ===============================================
-- Note: total_price_eur roughly based on 1 EUR ≈ 11.2 SEK
INSERT INTO bookings (customer_name, service_id, date, total_price_sek, total_price_eur, canceled, created_at) VALUES
('john_doe', 1, '2025-10-15', 2500, 223.21, false, NOW()),
('emma_johansson', 3, '2025-11-02', 1200, 107.14, false, NOW()),
('peter_nilsson', 6, '2025-09-10', 1300, 116.07, true, NOW()),
('sofia_larsson', 4, '2025-10-25', 2200, 196.43, false, NOW()),
('john_doe', 2, '2025-08-20', 1800, 160.71, false, NOW());
