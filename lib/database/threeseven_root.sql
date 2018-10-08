-- phpMyAdmin SQL Dump
-- version 4.8.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 30, 2018 at 08:52 PM
-- Server version: 10.1.33-MariaDB
-- PHP Version: 7.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `threeseven`
--

-- --------------------------------------------------------

--
-- Table structure for table `access_types`
--

CREATE TABLE `access_types` (
  `access_type_ID` int(11) NOT NULL,
  `description` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `access_types`
--

INSERT INTO `access_types` (`access_type_ID`, `description`) VALUES
(1, 'administrator'),
(2, 'employee');

-- --------------------------------------------------------

--
-- Table structure for table `audit_logs`
--

CREATE TABLE `audit_logs` (
  `audit_log_ID` int(11) NOT NULL,
  `user_ID` int(11) NOT NULL,
  `date` varchar(50) NOT NULL,
  `time` varchar(50) NOT NULL,
  `action` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `item_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `supplier_ID` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `price` double(10,2) NOT NULL,
  `unit_ID` int(11) NOT NULL,
  `status` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `models`
--

CREATE TABLE `models` (
  `model_ID` int(11) NOT NULL,
  `description` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `preventive_maintenance_transactions`
--

CREATE TABLE `preventive_maintenance_transactions` (
  `maintenance_ID` int(11) NOT NULL,
  `vehicle_ID` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `date` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `purchase_orders`
--

CREATE TABLE `purchase_orders` (
  `purchase_order_ID` int(11) NOT NULL,
  `purchase_order_date` varchar(25) NOT NULL,
  `supplier_ID` int(11) NOT NULL,
  `date_needed` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `purchase_order_details`
--

CREATE TABLE `purchase_order_details` (
  `purchase_order_ID` int(11) NOT NULL,
  `item_ID` int(11) NOT NULL,
  `quantity` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stocks`
--

CREATE TABLE `stocks` (
  `item_ID` int(11) NOT NULL,
  `quantity` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stock_adjustment_transactions`
--

CREATE TABLE `stock_adjustment_transactions` (
  `stock_adjustment_ID` int(11) NOT NULL,
  `stock_adjustment_date` varchar(25) NOT NULL,
  `date_encoded` varchar(25) NOT NULL,
  `date_approved` varchar(25) NOT NULL,
  `comments` varchar(255) NOT NULL,
  `status` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stock_adjustment_transaction_details`
--

CREATE TABLE `stock_adjustment_transaction_details` (
  `stock_adjustment_ID` int(11) NOT NULL,
  `item_ID` int(11) NOT NULL,
  `system_quantity` int(5) NOT NULL,
  `new_quantity` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stock_in_transactions`
--

CREATE TABLE `stock_in_transactions` (
  `stock_in_ID` int(11) NOT NULL,
  `stock_in_date` varchar(25) NOT NULL,
  `supplier_ID` int(11) NOT NULL,
  `invoice_num` int(11) NOT NULL,
  `invoice_date` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stock_in_transactions_with_po`
--

CREATE TABLE `stock_in_transactions_with_po` (
  `stock_in_ID` int(11) NOT NULL,
  `purchase_order_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stock_in_transaction_details`
--

CREATE TABLE `stock_in_transaction_details` (
  `stock_in_ID` int(11) NOT NULL,
  `item_ID` int(11) NOT NULL,
  `quantity` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stock_out_transactions`
--

CREATE TABLE `stock_out_transactions` (
  `stock_out_ID` int(11) NOT NULL,
  `date` varchar(25) NOT NULL,
  `released_to` varchar(50) NOT NULL,
  `remarks` varchar(255) NOT NULL,
  `status` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stock_out_transaction_details`
--

CREATE TABLE `stock_out_transaction_details` (
  `stock_out_ID` int(11) NOT NULL,
  `item_ID` int(11) NOT NULL,
  `quantity` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `supplier_ID` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `representative_ID` int(11) NOT NULL,
  `location` varchar(255) NOT NULL,
  `status` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `supplier_representatives`
--

CREATE TABLE `supplier_representatives` (
  `representative_ID` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `contact_number` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `unit_of_measurements`
--

CREATE TABLE `unit_of_measurements` (
  `unit_ID` int(11) NOT NULL,
  `description` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_ID` int(11) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `access_type_ID` int(11) NOT NULL,
  `status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vehicles`
--

CREATE TABLE `vehicles` (
  `vehicle_ID` int(11) NOT NULL,
  `plate_number` varchar(20) NOT NULL,
  `mv_number` varchar(30) NOT NULL,
  `engine_number` varchar(30) NOT NULL,
  `chassis_number` varchar(30) NOT NULL,
  `model_ID` int(11) NOT NULL,
  `category_ID` int(11) NOT NULL,
  `encumbered_to` varchar(30) NOT NULL,
  `insurance_from` varchar(30) NOT NULL,
  `insurance_to` varchar(30) NOT NULL,
  `status` varchar(50) NOT NULL,
  `image` longblob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vehicle_categories`
--

CREATE TABLE `vehicle_categories` (
  `category_ID` int(11) NOT NULL,
  `description` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `access_types`
--
ALTER TABLE `access_types`
  ADD PRIMARY KEY (`access_type_ID`);

--
-- Indexes for table `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD PRIMARY KEY (`audit_log_ID`),
  ADD KEY `user_ID` (`user_ID`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `supplier_ID` (`supplier_ID`,`unit_ID`),
  ADD KEY `unit_ID` (`unit_ID`);

--
-- Indexes for table `models`
--
ALTER TABLE `models`
  ADD PRIMARY KEY (`model_ID`);

--
-- Indexes for table `preventive_maintenance_transactions`
--
ALTER TABLE `preventive_maintenance_transactions`
  ADD PRIMARY KEY (`maintenance_ID`),
  ADD KEY `plate_number` (`vehicle_ID`),
  ADD KEY `vehicle_ID` (`vehicle_ID`);

--
-- Indexes for table `purchase_orders`
--
ALTER TABLE `purchase_orders`
  ADD PRIMARY KEY (`purchase_order_ID`),
  ADD KEY `supplier_ID` (`supplier_ID`);

--
-- Indexes for table `purchase_order_details`
--
ALTER TABLE `purchase_order_details`
  ADD KEY `purchase_order_ID` (`purchase_order_ID`),
  ADD KEY `item_ID` (`item_ID`);

--
-- Indexes for table `stocks`
--
ALTER TABLE `stocks`
  ADD PRIMARY KEY (`item_ID`),
  ADD KEY `item_ID` (`item_ID`);

--
-- Indexes for table `stock_adjustment_transactions`
--
ALTER TABLE `stock_adjustment_transactions`
  ADD PRIMARY KEY (`stock_adjustment_ID`);

--
-- Indexes for table `stock_adjustment_transaction_details`
--
ALTER TABLE `stock_adjustment_transaction_details`
  ADD KEY `stock_adjustment_ID` (`stock_adjustment_ID`,`item_ID`),
  ADD KEY `item_ID` (`item_ID`);

--
-- Indexes for table `stock_in_transactions`
--
ALTER TABLE `stock_in_transactions`
  ADD PRIMARY KEY (`stock_in_ID`),
  ADD KEY `supplier_ID` (`supplier_ID`);

--
-- Indexes for table `stock_in_transactions_with_po`
--
ALTER TABLE `stock_in_transactions_with_po`
  ADD PRIMARY KEY (`stock_in_ID`),
  ADD KEY `stock_in_ID` (`stock_in_ID`,`purchase_order_ID`),
  ADD KEY `purchase_order_ID` (`purchase_order_ID`);

--
-- Indexes for table `stock_in_transaction_details`
--
ALTER TABLE `stock_in_transaction_details`
  ADD KEY `stock_in_ID` (`stock_in_ID`,`item_ID`),
  ADD KEY `item_ID` (`item_ID`);

--
-- Indexes for table `stock_out_transactions`
--
ALTER TABLE `stock_out_transactions`
  ADD PRIMARY KEY (`stock_out_ID`);

--
-- Indexes for table `stock_out_transaction_details`
--
ALTER TABLE `stock_out_transaction_details`
  ADD KEY `stock_out_ID` (`stock_out_ID`,`item_ID`),
  ADD KEY `item_ID` (`item_ID`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_ID`),
  ADD KEY `representative_ID` (`representative_ID`);

--
-- Indexes for table `supplier_representatives`
--
ALTER TABLE `supplier_representatives`
  ADD PRIMARY KEY (`representative_ID`);

--
-- Indexes for table `unit_of_measurements`
--
ALTER TABLE `unit_of_measurements`
  ADD PRIMARY KEY (`unit_ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_ID`),
  ADD KEY `access_type_ID` (`access_type_ID`);

--
-- Indexes for table `vehicles`
--
ALTER TABLE `vehicles`
  ADD PRIMARY KEY (`vehicle_ID`),
  ADD KEY `model_ID` (`model_ID`,`category_ID`),
  ADD KEY `category_ID` (`category_ID`);

--
-- Indexes for table `vehicle_categories`
--
ALTER TABLE `vehicle_categories`
  ADD PRIMARY KEY (`category_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `access_types`
--
ALTER TABLE `access_types`
  MODIFY `access_type_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `audit_logs`
--
ALTER TABLE `audit_logs`
  MODIFY `audit_log_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `items`
--
ALTER TABLE `items`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `models`
--
ALTER TABLE `models`
  MODIFY `model_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `preventive_maintenance_transactions`
--
ALTER TABLE `preventive_maintenance_transactions`
  MODIFY `maintenance_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `purchase_orders`
--
ALTER TABLE `purchase_orders`
  MODIFY `purchase_order_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `stock_adjustment_transactions`
--
ALTER TABLE `stock_adjustment_transactions`
  MODIFY `stock_adjustment_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `stock_in_transactions`
--
ALTER TABLE `stock_in_transactions`
  MODIFY `stock_in_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `stock_out_transactions`
--
ALTER TABLE `stock_out_transactions`
  MODIFY `stock_out_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `supplier_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `supplier_representatives`
--
ALTER TABLE `supplier_representatives`
  MODIFY `representative_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `unit_of_measurements`
--
ALTER TABLE `unit_of_measurements`
  MODIFY `unit_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `vehicles`
--
ALTER TABLE `vehicles`
  MODIFY `vehicle_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `vehicle_categories`
--
ALTER TABLE `vehicle_categories`
  MODIFY `category_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD CONSTRAINT `audit_logs_ibfk_1` FOREIGN KEY (`user_ID`) REFERENCES `users` (`user_ID`);

--
-- Constraints for table `items`
--
ALTER TABLE `items`
  ADD CONSTRAINT `items_ibfk_1` FOREIGN KEY (`supplier_ID`) REFERENCES `suppliers` (`supplier_ID`),
  ADD CONSTRAINT `items_ibfk_2` FOREIGN KEY (`unit_ID`) REFERENCES `unit_of_measurements` (`unit_ID`);

--
-- Constraints for table `preventive_maintenance_transactions`
--
ALTER TABLE `preventive_maintenance_transactions`
  ADD CONSTRAINT `preventive_maintenance_transactions_ibfk_1` FOREIGN KEY (`vehicle_ID`) REFERENCES `vehicles` (`vehicle_ID`);

--
-- Constraints for table `purchase_order_details`
--
ALTER TABLE `purchase_order_details`
  ADD CONSTRAINT `purchase_order_details_ibfk_1` FOREIGN KEY (`purchase_order_ID`) REFERENCES `purchase_orders` (`purchase_order_ID`),
  ADD CONSTRAINT `purchase_order_details_ibfk_2` FOREIGN KEY (`item_ID`) REFERENCES `items` (`item_id`);

--
-- Constraints for table `stocks`
--
ALTER TABLE `stocks`
  ADD CONSTRAINT `stocks_ibfk_1` FOREIGN KEY (`item_ID`) REFERENCES `items` (`item_id`);

--
-- Constraints for table `stock_adjustment_transaction_details`
--
ALTER TABLE `stock_adjustment_transaction_details`
  ADD CONSTRAINT `stock_adjustment_transaction_details_ibfk_1` FOREIGN KEY (`item_ID`) REFERENCES `items` (`item_id`),
  ADD CONSTRAINT `stock_adjustment_transaction_details_ibfk_2` FOREIGN KEY (`stock_adjustment_ID`) REFERENCES `stock_adjustment_transactions` (`stock_adjustment_ID`);

--
-- Constraints for table `stock_in_transactions`
--
ALTER TABLE `stock_in_transactions`
  ADD CONSTRAINT `stock_in_transactions_ibfk_1` FOREIGN KEY (`supplier_ID`) REFERENCES `suppliers` (`supplier_ID`);

--
-- Constraints for table `stock_in_transactions_with_po`
--
ALTER TABLE `stock_in_transactions_with_po`
  ADD CONSTRAINT `stock_in_transactions_with_po_ibfk_1` FOREIGN KEY (`purchase_order_ID`) REFERENCES `purchase_orders` (`purchase_order_ID`),
  ADD CONSTRAINT `stock_in_transactions_with_po_ibfk_2` FOREIGN KEY (`stock_in_ID`) REFERENCES `stock_in_transactions` (`stock_in_ID`);

--
-- Constraints for table `stock_in_transaction_details`
--
ALTER TABLE `stock_in_transaction_details`
  ADD CONSTRAINT `stock_in_transaction_details_ibfk_1` FOREIGN KEY (`item_ID`) REFERENCES `items` (`item_id`),
  ADD CONSTRAINT `stock_in_transaction_details_ibfk_2` FOREIGN KEY (`stock_in_ID`) REFERENCES `stock_in_transactions` (`stock_in_ID`);

--
-- Constraints for table `stock_out_transaction_details`
--
ALTER TABLE `stock_out_transaction_details`
  ADD CONSTRAINT `stock_out_transaction_details_ibfk_1` FOREIGN KEY (`stock_out_ID`) REFERENCES `stock_out_transactions` (`stock_out_ID`),
  ADD CONSTRAINT `stock_out_transaction_details_ibfk_2` FOREIGN KEY (`item_ID`) REFERENCES `items` (`item_id`);

--
-- Constraints for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD CONSTRAINT `suppliers_ibfk_1` FOREIGN KEY (`representative_ID`) REFERENCES `supplier_representatives` (`representative_ID`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`access_type_ID`) REFERENCES `access_types` (`access_type_ID`);

--
-- Constraints for table `vehicles`
--
ALTER TABLE `vehicles`
  ADD CONSTRAINT `vehicles_ibfk_1` FOREIGN KEY (`category_ID`) REFERENCES `vehicle_categories` (`category_ID`),
  ADD CONSTRAINT `vehicles_ibfk_2` FOREIGN KEY (`model_ID`) REFERENCES `models` (`model_ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
