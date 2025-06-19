package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Buku {
    private final IntegerProperty idBuku;
    private final StringProperty judul;
    private final StringProperty penulis;
    private final IntegerProperty salinanTersedia;

    public Buku(int idBuku, String judul, String penulis, int salinanTersedia) {
        this.idBuku = new SimpleIntegerProperty(idBuku);
        this.judul = new SimpleStringProperty(judul);
        this.penulis = new SimpleStringProperty(penulis);
        this.salinanTersedia = new SimpleIntegerProperty(salinanTersedia);
    }

    // Getter untuk Properti (diperlukan oleh TableView)
    public IntegerProperty idBukuProperty() { return idBuku; }
    public StringProperty judulProperty() { return judul; }
    public StringProperty penulisProperty() { return penulis; }
    public IntegerProperty salinanTersediaProperty() { return salinanTersedia; }

    // Getter untuk Nilai (opsional, untuk kemudahan)
    public int getIdBuku() { return idBuku.get(); }
    public String getJudul() { return judul.get(); }
}