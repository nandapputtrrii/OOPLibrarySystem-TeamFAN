package Model;
public class Mahasiswa {
    private String nama;
    private String nim;
    private String email;

    public Mahasiswa(String nama, String nim, String email) {
        this.nama = nama;
        this.nim = nim;
        this.email = email;
    }

    // Ensure these methods are public
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

// Re-uploaded for visibility on GitHub
