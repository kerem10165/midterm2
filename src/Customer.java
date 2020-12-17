import java.math.BigDecimal;

public class Customer {
    //datafield tiplerini degistirebilirsiniz
    int arrivalTime; //musteri gelis zamani-
    //bekleme zamanini hesaplamada kullanabilirsiniz
    int removalTime;

    int musteriSira;

    int gelisSuresi;
    int hizmetSuresi;
    int beklemeSuresi;

    @Override
    public String toString() {
        String a;
        a = musteriSira + ". Müşteri Bekleme Süresi: " +beklemeSuresi;
        return a;
    }

    public String cikanElemanlar(){
        String a = musteriSira + ". Müşteri Geliş Süresi: " + gelisSuresi + " Hizmet Süresi: " + hizmetSuresi +
                " Sisteme Giriş: " + arrivalTime + " Sistemden Çıkış: " + removalTime + " Bekleme Süresi: " + beklemeSuresi;

        return a;
    }

    public Customer(int gelisSuresi, int hizmetSuresi, int musteriSira) {
        this.gelisSuresi = gelisSuresi;
        this.hizmetSuresi = hizmetSuresi;
        this.musteriSira = musteriSira;
    }
}








