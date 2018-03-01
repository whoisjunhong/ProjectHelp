-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 02, 2018 at 10:32 AM
-- Server version: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `projecthelpdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `profile`
--

CREATE TABLE IF NOT EXISTS `profile` (
`id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `gender` varchar(50) NOT NULL,
  `address` varchar(255) NOT NULL,
  `unitno` varchar(32) NOT NULL,
  `usertype` varchar(50) NOT NULL,
  `checklogin` varchar(32) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `profile`
--

INSERT INTO `profile` (`id`, `username`, `name`, `password`, `gender`, `address`, `unitno`, `usertype`, `checklogin`) VALUES
(1, 'admin', 'Administrator', '5f4dcc3b5aa765d61d8327deb882cf99', 'Male', 'somewhere over the rainbow ', '08-1201', 'Legendary', 'Gp6VcZSgkr'),
(2, 'volunteer', 'Volunteer', '5f4dcc3b5aa765d61d8327deb882cf99', 'Male', 'asdf address', '', 'Volunteer', '6efAgxjTls'),
(5, 'elderly', 'Elderly', '5f4dcc3b5aa765d61d8327deb882cf99', 'Male', 'Anderson JC', '01-1301', 'Elderly', 'THr6saqcmq'),
(8, 'asdf', 'asdf', '912ec803b2ce49e4a541068d495ab570', 'Male', 'asdf', 'asdf', 'Volunteer', 'diy1ZZlexW'),
(9, 'asdfg', 'asdfg', 'f68d02946321b7688ee058fffc2ec5f6', 'Male', 'sdfg', 'asfdaf', 'Elderly', 'e58PxvLO01');

-- --------------------------------------------------------

--
-- Table structure for table `request`
--

CREATE TABLE IF NOT EXISTS `request` (
`requestId` int(11) NOT NULL,
  `requesteeId` int(11) NOT NULL,
  `requestee` varchar(255) NOT NULL,
  `gender` varchar(50) NOT NULL,
  `type` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `unitno` varchar(32) NOT NULL,
  `locationLat` varchar(32) NOT NULL,
  `locationLong` varchar(32) NOT NULL,
  `requestDate` date NOT NULL,
  `requestTime` time NOT NULL,
  `status` enum('T','P','F') NOT NULL DEFAULT 'T'
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `request`
--

INSERT INTO `request` (`requestId`, `requesteeId`, `requestee`, `gender`, `type`, `address`, `unitno`, `locationLat`, `locationLong`, `requestDate`, `requestTime`, `status`) VALUES
(1, 5, 'Elderly', 'Male', 'Clean House', 'Nanyang Polytechnic', '01-1342', '1.3801179', '103.8490221', '2018-12-07', '00:23:15', 'F'),
(5, 5, 'Elderly', 'Male', 'Clean House', 'South Canteen', '01-1301', '1.3779379', '103.8493878', '2018-12-12', '00:12:15', 'P'),
(7, 11, 'Ong', 'Male', 'Clean House', 'Nanyang Polytechnic', '08-1201', '1.3801179', '103.8490221', '2018-12-15', '00:12:00', 'F');

-- --------------------------------------------------------

--
-- Table structure for table `scheduled`
--

CREATE TABLE IF NOT EXISTS `scheduled` (
`scheduledId` int(11) NOT NULL,
  `aceptee` int(11) NOT NULL,
  `requestId` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=20 ;

--
-- Dumping data for table `scheduled`
--

INSERT INTO `scheduled` (`scheduledId`, `aceptee`, `requestId`) VALUES
(6, 10, 1),
(7, 10, 3),
(9, 2, 4),
(10, 2, 8),
(11, 2, 8),
(12, 2, 9),
(13, 2, 10),
(14, 2, 11),
(15, 2, 12),
(16, 2, 15),
(17, 2, 1),
(18, 2, 5),
(19, 2, 7);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `profile`
--
ALTER TABLE `profile`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `id` (`id`), ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `request`
--
ALTER TABLE `request`
 ADD PRIMARY KEY (`requestId`);

--
-- Indexes for table `scheduled`
--
ALTER TABLE `scheduled`
 ADD PRIMARY KEY (`scheduledId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `profile`
--
ALTER TABLE `profile`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `request`
--
ALTER TABLE `request`
MODIFY `requestId` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `scheduled`
--
ALTER TABLE `scheduled`
MODIFY `scheduledId` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=20;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
