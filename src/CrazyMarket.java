import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
/*19120205042 @Kerem Akkoyun*/

public class CrazyMarket implements MyQueue<Customer>{
    /**
     *  numberOfCustumer ile verilen sayida
     * musteri hizmet gorene kadar calismaya devam eder*/

    int numberOfCustomer;//benim implemantasyonumda kapasiteyi belirlemek için kullanıldı
    int size;//kaç adet veri var saymak için

    private Node start;
    private Node end;

    ArrayList<Customer> list = new ArrayList<Customer>();//eklenen tüm elemanları tutmak için

    String tekerleme = "O piti piti karamela sepeti "
            + "\nTerazi lastik jimnastik "
            + "\nBiz size geldik bitlendik Hamama gittik temizlendik.";
    String bolunmusTekerleme;//heceleri yani ünlü harfleri tutar

    boolean dequeuCheck = true;
    boolean dequeuCountingCheck = false;

    public CrazyMarket(int numberOfCustomer) {
        this.numberOfCustomer = numberOfCustomer;
        start = null;
        end = null;
        size = 0;
        bolunmusTekerleme = tekerlemeBol(tekerleme);
        simulation();
    }

    public CrazyMarket(int numberOfCustomer, String tekerleme) {
        this.numberOfCustomer = numberOfCustomer;
        start = null;
        end = null;
        size = 0;
        this.tekerleme = tekerleme;
        bolunmusTekerleme = tekerlemeBol(tekerleme);
        simulation();
    }

    public class Node
    {
        Node next;
        Node prev;
        Customer data;

        public Node (Customer data , Node next) {
            this.next = next;
            this.data = data;
        }
    }

    private class CrazyMarketIterator implements Iterator<Customer>{
        Node itr;

        CrazyMarketIterator(){
            itr = start;
        }

        @Override
        public boolean hasNext() {
            if(itr != null)
                return true;
            return false;
        }
        @Override
        public Customer next() {
            Customer temp = itr.data;
            itr = itr.next;
            return temp;
        }
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public boolean isEmpty() { return start == null; }
    @Override
    public boolean enqueue(Customer item) {
        if (start == null){
            Node node = new Node(item,null);
            node.prev = null;
            start = node;
            end = node;
            ++size;
            return true;
        }
        else if(start != null){
            Node node = new Node(item,null);
            end.next = node;
            Node temp = end;
            end = node;
            end.prev = temp;
            ++size;
            return  true;
        }
        return false;
    }
    @Override
    public Customer dequeuNext() {
        if(start == end){
            Customer temp = start.data;
            start = null;
            end = null;
            size = 0;
            return temp;
        }
        else{
            Customer temp =  start.data;
            start = start.next;
            start.prev = null;
            --size;
            return temp;
        }
    }

    @Override
    public Customer dequeuWithCounting(String tekerleme) {
        Node itr = start;

        if(start == null)
            return null;

        for (int i = 0; i < tekerleme.length()-1 ; i++){
            itr = itr.next;
            if(itr == null){
                itr = start;
            }
        }
        Node temp = itr;

        if (itr == start){
            dequeuNext();
        }
        else if(itr == end){
            itr.prev.next = null;
            end = temp.prev;
            size--;
        }
        else{//ortadan bir elemansa
            itr.prev.next = itr.next;
            itr.next.prev = itr.prev;
            size--;
        }
        return temp.data;
    }

    @Override
    public Iterator<Customer> iterator() {
        return new CrazyMarketIterator();
    }

    public int getCapacity(){
        return numberOfCustomer;
    }

    /*ünlü harfleri seçer ve stringe atar*/
    String tekerlemeBol(String tek){
        String temp = new String();

        tek = tek.toLowerCase();

        for (int i = 0; i < tek.length() ; i++){
            if (tek.charAt(i) == 'a' || tek.charAt(i) == 'e' || tek.charAt(i) == 'ı' ||
                    tek.charAt(i) == 'i' || tek.charAt(i) == 'o' || tek.charAt(i) == 'ö' ||
                    tek.charAt(i) == 'u' || tek.charAt(i) == 'ü')
            {
                temp += tek.charAt(i);
            }
        }
        return temp;
    }


    /** kuyrugun basindaki musteriyi yada tekerleme
     * ile buldugu musteriyi return eder
     * burada similasyon yapımda bu fonksiyon bir.ok farklı yerde çağırılabileceği için
     * classta flaglar kullanarak buna göre çağırdım
     * Test fonksiyonlarında yanlış çalışacaktır*/
    public Customer chooseCustomer() {
        if(dequeuCheck){
            return dequeuNext();
        }
        if(dequeuCountingCheck){
            return dequeuWithCounting(bolunmusTekerleme);
        }
        return null;
    }

    /**Bu fonksiyonda similasyon yapılacak
     * kasa zamanı kasadan çıkacak müşterinin zamanını belirtir current time de müşterinin girdiği zamanı göstermekte
     * j ise current time'ı tutuyor ve üstüne eklenecek elemanın zamanını ekliyor eğer eklenen elemanın zamanı kasa zamanından büyükse eleman bir sonraki adımda da aynı kontrol yapılana kadar tutuluyor ve eleman alımı yapılmıyor
     * */
    void simulation()
    {
        Random rd = new Random();
        int currentTime = 0;
        int kasaZamani = 0;//sıradaki elemanın kasadan çıktığı zamanı belirtmek için kullanılır
        int gelis,hizmet;
        int musteriSay = 1;//eklenen müşterinin sırası için
        Customer onceden = null;
        boolean flag = false;//ilk eleman
        boolean flag1= true;
        /*son gelen müşterininin geliş zamanı iki olsun currnet time 5 diğer müşterinin kasadan çıkış zamanı da 6 olsun müşteri 7. saniye gelir
        bu flag ve Customer onceden ile birlikte bu müşteriyi tutuyoruz ve sonraki zamanda ekliyoruz*/

        System.out.println("Kasada İşlem Gören Müşteriler\n" +
                "***********************************************************************************************************");

        for (int i = 0; i < numberOfCustomer ; i++) {
            /*gelen elemanın geliş süresi 0 hizmet süresi 1 olsun diğer eleman da geliş: 2 hizmet 2 olsun 1. eleman kasadan çıktığında
            * sırada eleman olmayacaktır ve sistem boş olacaktır (diğer eleman yolda) ve burada 2. elemanın gelişini bekleyecektir
            * 2. eleman ise onceden adlı değişkende tutuluyor*/
            if (start == null) {
                gelis = rd.nextInt(3);
                hizmet = 1 + rd.nextInt(3);
                Customer c =null;
                if (flag1){//bu sadece ilk müşteriyi eklemek için kullanılıyor
                    c = new Customer(gelis, hizmet, musteriSay++);
                    flag1 = false;
                }
                else if (flag){
                    c = onceden;
                    onceden = null;
                    flag = false;
                }

                currentTime += c.gelisSuresi;
                kasaZamani = currentTime + c.hizmetSuresi;
                c.arrivalTime = currentTime;
                c.removalTime = kasaZamani;
                c.beklemeSuresi = 0;//bu blokta tüm elemanlar beklemeden işleme alınacaktır
                enqueue(c);
                list.add(c);//sonunda tüm elamanları yazdırmak için ArrayList kullandım
            }
            else {//sırada eleman varsa
                Customer c = start.data;
                currentTime = kasaZamani;
                int tempBekleme = currentTime - c.arrivalTime;
                if (tempBekleme <= 10) {
                    dequeuCountingCheck =true;//counting ile yapacak demek
                    dequeuCheck = false;
                    c = chooseCustomer();
                    dequeuCountingCheck =false;//bir sonraki bu fonksiyonla karşılaştığında işlem yapma demek
                }

                kasaZamani = currentTime + c.hizmetSuresi;
                c.removalTime = kasaZamani;
                c.beklemeSuresi = currentTime - c.arrivalTime;

                if (dequeuCheck == false)
                    System.out.println(c.cikanElemanlar());

                if(onceden != null){
                    if (onceden.arrivalTime <= kasaZamani){
                        enqueue(onceden);
                        currentTime = onceden.arrivalTime;
                        list.add(onceden);
                        onceden = null;
                        flag = false;
                    }
                }
            }

            int j = currentTime;
            if(flag == false) {//eleman ekleme yapıyoruz ama önceden kalmış eleman yoksa
                while (j <= kasaZamani) {
                    gelis = rd.nextInt(3);
                    hizmet = 1 + rd.nextInt(3);
                    Customer c = new Customer(gelis, hizmet, musteriSay++);
                    j += gelis;
                    c.arrivalTime = j;
                    if (j > kasaZamani) {
                        flag = true;//önceden gelen eleman var demektir
                        onceden = c;
                        break;
                    }
                    enqueue(c);
                    list.add(c);
                }
            }
            Customer temp = chooseCustomer();
            if(temp != null)
                System.out.println(temp.cikanElemanlar());
            temp = null;
            dequeuCountingCheck =false;
            dequeuCheck = true;
        }

        System.out.println("***********************************************************************************************************"+
                "\n\n" + "Tüm Müşteriler\n" +
                "***********************************************************************************************************");
        for (Customer c : list){
            System.out.println(c.musteriSira + ". Müşteri Geliş Süresi: " + + c.gelisSuresi + " Hizmet Süresi: " + c.hizmetSuresi
                    + " Sisteme Giriş: " + c.arrivalTime);
        }
        System.out.println("***********************************************************************************************************"
                +"\n\nMüşterilerin Bekleme Zamanları\n" +
                "***********************************************************************************************************");
        for (Customer c : this){
            c.beklemeSuresi = kasaZamani - c.arrivalTime;
            System.out.println(c);
        }
        System.out.println("***********************************************************************************************************");
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        CrazyMarket market = new CrazyMarket(10);
    }
}