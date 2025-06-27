package Model;

import java.time.LocalDate;

public class Peminjaman {
    private String idPeminjaman;
    private String idBuku;
    private String judulBuku;
    private LocalDate tanggalPeminjaman;
    private LocalDate tanggalPengembalian;
    private String statusPeminjaman;

    public Peminjaman(String idPeminjaman, String idBuku, String judulBuku, LocalDate tanggalPeminjaman, LocalDate tanggalPengembalian, String statusPeminjaman) {
        this.idPeminjaman = idPeminjaman;
        this.idBuku = idBuku;
        this.judulBuku = judulBuku;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalPengembalian = tanggalPengembalian;
        this.statusPeminjaman = statusPeminjaman;
    }

    // Getters
    public String getIdPeminjaman() { return idPeminjaman; }
    public String getIdBuku() { return idBuku; }
    public String getJudulBuku() { return judulBuku; }
    public LocalDate getTanggalPeminjaman() { return tanggalPeminjaman; }
    public LocalDate getTanggalPengembalian() { return tanggalPengembalian; }
    public String getStatusPeminjaman() { return statusPeminjaman; }
}