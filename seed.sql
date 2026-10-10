-- RajMart Seed Data
-- All users password: password123
-- bcrypt hash: $2a$10$iZY.oYEegB1H5YaG.ZE9DeBZkmEZrpyThC4uDQyVJNwZe9xw/kAxy

INSERT INTO users (name, email, password_hash, role) VALUES
('Admin User', 'admin@rajmart.com', '$2a$10$iZY.oYEegB1H5YaG.ZE9DeBZkmEZrpyThC4uDQyVJNwZe9xw/kAxy', 'ADMIN'),
('Raj Seller', 'seller@rajmart.com', '$2a$10$iZY.oYEegB1H5YaG.ZE9DeBZkmEZrpyThC4uDQyVJNwZe9xw/kAxy', 'SELLER'),
('Priya Buyer', 'buyer@rajmart.com', '$2a$10$iZY.oYEegB1H5YaG.ZE9DeBZkmEZrpyThC4uDQyVJNwZe9xw/kAxy', 'BUYER'),
('Kumar Store', 'kumar@rajmart.com', '$2a$10$iZY.oYEegB1H5YaG.ZE9DeBZkmEZrpyThC4uDQyVJNwZe9xw/kAxy', 'SELLER');

INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url) VALUES
(2, 'Wireless Bluetooth Headphones', 'Premium noise-cancelling over-ear headphones with 30hr battery.', 2499.00, 50, 'Electronics', 'https://via.placeholder.com/300x300?text=Headphones'),
(2, 'Cotton Casual T-Shirt', 'Comfortable 100% cotton t-shirt available in multiple colors.', 499.00, 200, 'Fashion', 'https://via.placeholder.com/300x300?text=T-Shirt'),
(2, 'Stainless Steel Water Bottle', '1L insulated bottle keeps drinks cold for 24hrs.', 699.00, 100, 'Home', 'https://via.placeholder.com/300x300?text=Bottle'),
(4, 'Smart Fitness Watch', 'Track steps, heart rate, sleep. Waterproof design.', 3499.00, 30, 'Electronics', 'https://via.placeholder.com/300x300?text=Watch'),
(4, 'Organic Green Tea Pack', '100 tea bags of premium organic green tea.', 299.00, 150, 'Grocery', 'https://via.placeholder.com/300x300?text=Tea'),
(4, 'Leather Wallet', 'Genuine leather bifold wallet with RFID protection.', 899.00, 75, 'Fashion', 'https://via.placeholder.com/300x300?text=Wallet');
