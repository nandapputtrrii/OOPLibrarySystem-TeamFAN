-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 19 Jun 2025 pada 10.05
-- Versi server: 10.4.28-MariaDB
-- Versi PHP: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `perpustakaan`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admin`
--

CREATE TABLE `admin` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `admin`
--

INSERT INTO `admin` (`username`, `password`) VALUES
('0', '0'),
('admin', 'admin');

-- --------------------------------------------------------

--
-- Struktur dari tabel `buku`
--

CREATE TABLE `buku` (
  `id_buku` int(11) NOT NULL,
  `judul` varchar(255) NOT NULL,
  `penulis` varchar(100) DEFAULT NULL,
  `tanggal_terbit` date DEFAULT NULL,
  `isbn` bigint(20) DEFAULT NULL,
  `kategori` varchar(100) DEFAULT NULL,
  `total_salinan` int(11) DEFAULT 0,
  `salinan_tersedia` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `buku`
--

INSERT INTO `buku` (`id_buku`, `judul`, `penulis`, `tanggal_terbit`, `isbn`, `kategori`, `total_salinan`, `salinan_tersedia`) VALUES
(1, 'Laskar Pelangi', 'Andrea Hirata', '2005-09-01', 9789793062792, 'Novel', 5, 2),
(2, 'Ayat-Ayat Cinta', 'Habiburrahman El Shirazy', '2004-12-01', 9789793062809, 'Novel', 3, 2),
(3, 'Negeri 5 Menara', 'Ahmad Fuadi', '2009-02-01', 9786020331435, 'Novel', 4, 2),
(4, 'Bumi Manusia', 'Pramoedya Ananta Toer', '1980-08-01', 9789799731240, 'Novel', 6, 2),
(5, 'Cantik Itu Luka', 'Eka Kurniawan', '2002-01-01', 9786020318059, 'Novel', 10, 10);

-- --------------------------------------------------------

--
-- Struktur dari tabel `member`
--

CREATE TABLE `member` (
  `id_member` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `alamat` text DEFAULT NULL,
  `no_hp` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `fakultas` varchar(100) DEFAULT NULL,
  `prodi` varchar(100) DEFAULT NULL,
  `nim` int(20) DEFAULT NULL,
  `gambar` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `member`
--

INSERT INTO `member` (`id_member`, `nama`, `alamat`, `no_hp`, `email`, `password`, `fakultas`, `prodi`, `nim`, `gambar`) VALUES
(2, 'El', 'Malang', '1234', 'elpro', '1234', 'FH', 'Ilmu Hukum', 2012, NULL),
(3, 'nanda', 'Malang', '123', 'nanda', '1234', 'VOKASI', 'D3 Keuangan dan Perbankan', 20241, NULL),
(4, 'Rivanpro', NULL, '123', 'rivanpro', '1234', 'Fakultas Teknik', 'Informatika', 202411, NULL);

-- --------------------------------------------------------

--
-- Struktur dari tabel `peminjaman`
--

CREATE TABLE `peminjaman` (
  `id_peminjaman` varchar(20) NOT NULL,
  `id_buku` varchar(50) NOT NULL,
  `judul_buku` varchar(255) NOT NULL,
  `nama_peminjam` varchar(255) NOT NULL,
  `nim_peminjam` varchar(20) NOT NULL,
  `tanggal_peminjaman` date NOT NULL,
  `tanggal_pengembalian` date DEFAULT NULL,
  `status_peminjaman` enum('Dipinjam','Dikembalikan','Terlambat') DEFAULT 'Dipinjam',
  `tanggal_ajukan` datetime DEFAULT current_timestamp(),
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `denda` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `peminjaman`
--

INSERT INTO `peminjaman` (`id_peminjaman`, `id_buku`, `judul_buku`, `nama_peminjam`, `nim_peminjam`, `tanggal_peminjaman`, `tanggal_pengembalian`, `status_peminjaman`, `tanggal_ajukan`, `created_at`, `updated_at`, `denda`) VALUES
('FXX005', '3', 'Negeri 5 Menara', 'Farel', '202410', '2025-06-18', '2025-06-19', 'Dikembalikan', '2025-06-18 16:07:08', '2025-06-18 09:07:08', '2025-06-19 06:04:44', 0.00),
('FXX006', '4', 'Bumi Manusia', 'Farel', '202410', '2025-06-19', '2025-06-19', 'Dikembalikan', '2025-06-19 12:57:23', '2025-06-19 05:57:23', '2025-06-19 07:44:14', 0.00),
('FXX007', '1', 'Laskar Pelangi', 'Farel', '202410', '2025-06-19', '2025-06-20', 'Dipinjam', '2025-06-19 14:43:48', '2025-06-19 07:43:48', '2025-06-19 07:43:48', 0.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `peminjaman_backup`
--

CREATE TABLE `peminjaman_backup` (
  `id_peminjaman` int(11) NOT NULL DEFAULT 0,
  `id_buku` varchar(50) NOT NULL,
  `judul_buku` varchar(255) NOT NULL,
  `nama_peminjam` varchar(255) NOT NULL,
  `nim_peminjam` varchar(20) NOT NULL,
  `tanggal_peminjaman` date NOT NULL,
  `tanggal_pengembalian` date DEFAULT NULL,
  `status_peminjaman` enum('Dipinjam','Dikembalikan','Terlambat') DEFAULT 'Dipinjam',
  `tanggal_ajukan` datetime DEFAULT current_timestamp(),
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` int(11) NOT NULL,
  `id_member` int(11) DEFAULT NULL,
  `id_buku` int(11) DEFAULT NULL,
  `tanggal_pinjam` date DEFAULT NULL,
  `tanggal_kembali` date DEFAULT NULL,
  `status` enum('dipinjam','dikembalikan') DEFAULT 'dipinjam'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`id_buku`);

--
-- Indeks untuk tabel `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`id_member`);

--
-- Indeks untuk tabel `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD PRIMARY KEY (`id_peminjaman`);

--
-- Indeks untuk tabel `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `id_member` (`id_member`),
  ADD KEY `id_buku` (`id_buku`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `buku`
--
ALTER TABLE `buku`
  MODIFY `id_buku` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `member`
--
ALTER TABLE `member`
  MODIFY `id_member` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id_transaksi` int(11) NOT NULL AUTO_INCREMENT;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `transaksi_ibfk_1` FOREIGN KEY (`id_member`) REFERENCES `member` (`id_member`),
  ADD CONSTRAINT `transaksi_ibfk_2` FOREIGN KEY (`id_buku`) REFERENCES `buku` (`id_buku`);

DELIMITER $$
--
-- Event
--
CREATE DEFINER=`root`@`localhost` EVENT `update_status_peminjaman_terlambat` ON SCHEDULE EVERY 1 MINUTE STARTS '2025-06-18 15:50:06' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE peminjaman
  SET status_peminjaman = 'Terlambat'
  WHERE tanggal_pengembalian < CURDATE()
    AND status_peminjaman = 'Dipinjam'$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
