package say�_tahmin;

import java.awt.BorderLayout;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class server_side extends JFrame {
	private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private ServerSocket server;
    private int totalClients = 100;
    private int port = 6789;
    int i=0;
    int j=0;
    int k=0;
    int m=0;
    int default_sayi;
    int [] default_basamak;
    int t=5;
    int del1=5;
    int del2=5;
    int del3=5;
    int del4=6;
    int server_random_sayi=sayi_�ret();// program �al��t���nda bilgisayar�n tuttu�u say�d�r.
    int birler_basamagi;
    int onlar_basamagi;
    int y�zler_basamagi;
    int binler_basamagi;
    int sayi_bulundu;
	private ArrayList<Integer> temp2;
	private ArrayList<Integer> temp3;
	private ArrayList<Integer> temp4;
	private ArrayList<Integer> temp;
    public static int sayi_�ret() { // 4 basamakl� bir say� �retir
    	Random r = new Random();
		int alt = 1000;
		int �st = 10000;
		int rnd = r.nextInt(�st-alt) + alt;
		int [] random_basamak=basamaklarina_ayir(rnd);
		int x=kontrol(random_basamak);
		if(x==-1) {
			return sayi_�ret();
		}
		else return rnd;
    }
    public static int  kontrol(int random_basamak[]) { // �retilen say�n�n rakamlar�n�n farkl� olup olmad��� kontrol edilir
    	if((random_basamak[0]==random_basamak[1])||(random_basamak[0]==random_basamak[2])||(random_basamak[0]==random_basamak[3])||(random_basamak[1]==random_basamak[2])||(random_basamak[1]==random_basamak[3])||(random_basamak[2]==random_basamak[3])){
    		return -1;
    	}
    	else return 1;
    }
    public void startRunning() //server �al��t�r�ld���nda ilk olarak bu fonksiyon �al���r.
    {
        try
        {
            server=new ServerSocket(port, totalClients);
            while(true)
            {
                try
                {   //kullan�c�n�n server'a ba�lanmas� i�in server haz�rda bekler.
                	//bilgisayar kullan�c�ya oyun ile ilgili bilgileri mesaj olarak g�nderir
                	System.out.println("Birinin Ba�lanmas� i�in bekleniyor");
                    connection=server.accept();
                    System.out.println("�imdi �u ip adresi ba�land�"+connection.getInetAddress().getHostName());                    
                    output = new ObjectOutputStream(connection.getOutputStream());
                    output.flush();
                    input = new ObjectInputStream(connection.getInputStream());
                    output.writeObject("Server : "+"Say� tahmin oyununa ho�geldin, bu oyunun bir ka� kural� vard�r");                 
                    output.writeObject("Server : "+"�ncelikle tahmin etmen gereken say� 4 basamakl�, rakamlar� birbirinden farkl�d�r.");                   
                    output.writeObject("Server : "+"�lk cevap mesaj�n 4 basamakl� bir say� ve 0/0 �r:1234 0/0 olmal�d�r");                    
                    output.writeObject("Server : "+"Daha sonra ben tahmin etti�in say� i�in sana puan verece�im ve tuttu�un say� i�in tahminimi ayn� mesaj i�erisinde g�nderece�im.");
                    output.writeObject("Server : "+"Puanlama sistemi �u �ekilde olmaktad�r: tahmin olarak g�nderdi�in say� ile benim tuttu�um say�da basamak de�erleri ayn� olan her rakam i�in +1 di�er durumlarda ise -1 puan verilecektir.");
                    output.writeObject("Server : "+"G�nderdi�in mesajlar�n format� �u �ekilde olmal�d�r:1589 2/-2");                   
                    output.writeObject("Server : "+"Yani yapt���n tahmin ile bana verdi�in puan aras�nda bir vuru�luk bo�luk b�rakmal�s�n");                 
                    output.writeObject("Server : "+"Haz�rsan Ba�layal�m");
                    whileChatting(); //kullan�c� her mesaj g�nderdi�inde bu fonksiyon �al���r.
                }catch(EOFException eofException)
                {
                	System.out.println("HATA");
                }
            }
        }
        catch(IOException ioException)
        {
                ioException.printStackTrace();
        }
    }
    private void whileChatting() throws IOException
    {
    	String message="";    
        do{
                try
                {
                        message = (String) input.readObject();//Kullan�c�n�n bilgisayara g�nderdi�i mesaj
                        get_user_Message(message);//Kullan�c�dan al�nan mesaj i�lenmek �zere bu fonksiyona g�nderilir.
                        
                 
                }catch(ClassNotFoundException classNotFoundException)
                {
                        
                }
        }while(true);
    }
	private void get_user_Message(String message)
    {
        try
        {      
        	
        	String x=message.substring(9);//Kullan�c�n�n g�nderdi�i mesaj 9.indexten sonra al�n�r.
        	String client_cevap[]=x.split(" ");//al�nan mesaj bo�lu�a g�re ayr�l�p diziye at�l�r.Dizinin 1.eleman� 4 basamakl� tahmin say�s�, ikinci eleman� ise kullan�c�n�n bilgisayara verdi�i puand�r.
        	int client_tahmin_sayisi=Integer.valueOf(client_cevap[0]);//string ifade int'e d�n��t�r�ld�.
        	int client_rakamlari_al[]=new int [4];//kullan�c�n�n g�nderdi�i say�n�n basamak de�erleri bu diziye at�l�r
        	int server_rnd_rakamlari[]=new int[4];// bilgisayar�n tuttu�u say�n�n basamak de�erleri bu diziye at�l�r.
        	client_rakamlari_al=basamaklarina_ayir(client_tahmin_sayisi); //Kullan�c�n�n tuttu�u say�n�n basamak de�erleri elde edilir.
        	server_rnd_rakamlari=basamaklarina_ayir(server_random_sayi); // bilgisayar�n tuttu�u say�n�n basamak de�erleri elde edilir.
        	String serverin_verdigi_puan=puan_ver(server_rnd_rakamlari,client_rakamlari_al); //kullan�c�n�n tahmini ile bilgisayar�n tuttu�u say� kar��la�t�r�l�r ve bilgisayar kullan�c�ya bir puan verir.
        	
        	if(!client_cevap[1].equals("0/-4") && t==5) {//kullan�c� bilgisayar�n tahminine 0/-4 puan�n� verene kadar bilgisayar yeni bir say� �retip denemeye devam eder.
        		default_sayi=sayi_�ret();
        		output.writeObject("Server : "+default_sayi+" "+serverin_verdigi_puan);       			
        	}       	
        	if(client_cevap[1].equals("0/-4")) {//kullan�c� 0/-4 puan�n� verdi�inde art�k �zerinde i�lem yapaca��m�z say� belirlenmi� olur.(default_sayi)
        		default_basamak=basamaklarina_ayir(default_sayi);//�zerinde i�lem yap�lacak say� basamak de�erleri elde edilir.
        		temp=new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));// ge�ici bir arraylist olu�turulur
        		for(int e=0;e<temp.size();e++) {
        			if(default_basamak[0]==temp.get(e)) {
        				temp.remove(e);//array listten mevcut say�n�n birler basama�� ��kart�l�r ��nk� mevcut say� i�in 0/-4 d�nm��t�r ve birler basama��nda mevcut rakam�n olmayaca�� kesindir.
        			}
        		}
            	int yeni_sayi=(default_basamak[3]*1000)+(default_basamak[2]*100)+(default_basamak[1]*10)+temp.get(i); //birler basama��na s�rayla dizideki elemanlar atan�r.
        		output.writeObject("Server : "+yeni_sayi+" "+serverin_verdigi_puan);//her de�i�imde kullan�c�ya bu say� yollan�r
        		i++;
        		t=6;
        	}
        	if(client_cevap[1].equals("1/-3")&& t==6) {//kullan�c� 1/-3 puan�n� verdiyse yukar�daki kontrolden sonra birler basama�� bulunmu� demektir. Art�k onlar basama��na ge�ilebilir.        		
        		if(del1==5) {// Bu kontrol ise kullanici 1 den fazla 1/-3 puan�n� yollarsa tekrar tekrar silme i�lemini yapmamas� i�in
        			temp2=new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));//ge�ici array list olu�turulur.
        			birler_basamagi=temp.get(i-1);//bir �nceki if blo�unda yakalanan birler basama��n� kaybetmemek i�in degiskene at�l�r
        			for(int e=0;e<temp2.size();e++) {// yeni olusturulan arraylistten ilk olarak birler basamagi ��karilir. Rakamlar� farkl� bir say� tahmin etti�imiz i�in birler basamaginda olan sayi baska basamakta olamayacaktir.
            			if(birler_basamagi==temp2.get(e)) {
            				temp2.remove(e);
            			}
            		}
        			if(default_basamak[1]!=birler_basamagi) {// Kullanicidan 0/-4 puanini aldigimiz sayinin birler_basamagi degiskeni ile ayn� olup olmadigi kontrol edilir.Bunun sebebi
        			for(int e=0;e<temp2.size();e++) {        // birler basamagindaki rakam diziden silinmi�ti e�er 0/-4 puan�n� aldigimiz sayinin onlar basamagi bu rakam ile ayn� ise
            			if(default_basamak[1]==temp2.get(e)) {// diziden zaten silinmi� bir rakam� tekrar silmeye �al��m�� olamamak amac� ile bu kontrol yap�lm��t�r.
            				temp2.remove(e);// kontroller sa�lanm��sa 0/-4 puan�n� aldigimiz default sayinin onlar basamagi diziden silinir.
            			}
            		}
        			}
        			del1++;
        		}
    			int yeni_sayi2=(default_basamak[3]*1000)+(default_basamak[2]*100)+(temp2.get(j)*10)+birler_basamagi;//onlar basamagina dizi elemanlari atanir
    			output.writeObject("Server : "+yeni_sayi2+" "+serverin_verdigi_puan);//s�rayla kullan�c�ya yollanir.
    			j++;
    		}
        	if(client_cevap[1].equals("2/-2")&& t==6) {// kullanici 2/-2 puanini verdiyse yukaridaki kontolden sonra onlar basamagi da bulunmu� demektir. Art�k y�zler basamagina ge�ilebilir	
        		if(del2==5) {// Bu kontrol ise kullanici 1 den fazla 2/-2 puan�n� yollarsa tekrar tekrar silme i�lemini yapmamas� i�in
        			temp3=new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));// ge�ici bir dizi olu�turulur
        			onlar_basamagi=temp2.get(j-1);// bulunan onlar basamagi de�i�kene atilir.
        			for(int e=0;e<temp3.size();e++) {//birler basamagi diziden silinir.
            			if(birler_basamagi==temp3.get(e)) {
            				temp3.remove(e);
            			}
            		}
        			for(int e=0;e<temp3.size();e++) {//onlar basamagi diziden silinir
            			if(onlar_basamagi==temp3.get(e)) {
            				temp3.remove(e);
            			}
            		}
        			if(default_basamak[2]!=birler_basamagi && default_basamak[2]!=onlar_basamagi) {//default sayinin y�zler basamaginin bulunan basamak de�erleri ile ayn� olup olmadigi kontrol edilir.
        			for(int e=0;e<temp3.size();e++) {// egerki ayni degil ise silme i�lemi ger�ekle�tirilir.
            			if(default_basamak[2]==temp3.get(e)) {
            				temp3.remove(e);
            			}
            		}
        			}
        			del2++;
        		}
    			int yeni_sayi3=(default_basamak[3]*1000)+(temp3.get(k)*100)+(onlar_basamagi*10)+birler_basamagi;// y�zler basamagina dizi elemanlar� s�rayla yerle�tirilir.
    			output.writeObject("Server : "+yeni_sayi3+" "+serverin_verdigi_puan);//her seferinde kullaniciya mesaj olarak iletilir.
    			k++;
    		}
        	if(client_cevap[1].equals("3/-1")&& t==6) {//Kullanici 3/-1 puanini verdiyse yukaridaki kontrollerden sonra birler,onlar ve y�zler basamagi bulunmu� demektir.    		
        		if(del3==5) {// Bu kontrol ise kullanici 1 den fazla 3/-1 puan�n� yollarsa tekrar tekrar silme i�lemini yapmamas� i�in
        			temp4=new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));//ge�ici bir dizi olu�turulur.
        			y�zler_basamagi=temp3.get(k-1);//y�zler basamagini kaybetmemek i�in de�i�kene atilir.
        			for(int e=0;e<temp4.size();e++) {//birler basamagi arraylistten silinir
            			if(birler_basamagi==temp4.get(e)) {
            				temp4.remove(e);
            			}
            		}
        			for(int e=0;e<temp4.size();e++) {//onlar basamagi array listten silinir
            			if(onlar_basamagi==temp4.get(e)) {
            				temp4.remove(e);
            			}
            		}
        			for(int e=0;e<temp4.size();e++) {//y�zler basamagi array listten silinir.
            			if(y�zler_basamagi==temp4.get(e)) {
            				temp4.remove(e);
            			}
            		}
        			if(default_basamak[3]!=birler_basamagi && default_basamak[3]!=onlar_basamagi&&default_basamak[3]!=y�zler_basamagi) {// default sayinin y�zler basamagi ile tespit edilen basamaklar ayn� m� kontrol edilir.
        				for(int e=0;e<temp4.size();e++) {//eger ayni degilse silme islemini yapabiliriz.Bunu yapmamizin sebebi default olarak belirlenen sayinin y�zler basamagi tespit edilen basamak degerleri ile ayn� olabilir. Bu durumda dizide olmayan bir eleman� silmeye calismis oluruz.      				
        					if(default_basamak[3]==temp4.get(e)) {
        						temp4.remove(e);
        					}       				
        				}
        				}
        			del3++;
        			}      		
        		sayi_bulundu=(temp4.get(m)*1000)+(y�zler_basamagi*100)+(onlar_basamagi*10)+birler_basamagi;
    			output.writeObject("Server : "+sayi_bulundu+" "+serverin_verdigi_puan);
    			m++;
        	}
        	if(client_cevap[1].equals("4/0")&& t==6) {//Kullanici 4/0 puanini vermisse sayi bulunmus demektir.
    			output.writeObject("Server : "+"Tuttugun say�y� buldum="+sayi_bulundu+" "+serverin_verdigi_puan);
        	}       
        	}        
        catch(IOException ioException)
        {
            
        }
        
    }
    public static int[] basamaklarina_ayir(int a) {// parametre olarak aldigi 4 basamakli sayiyinin basamak degerlerini elde eder ve bir diziye atar. Dizi d�nd�r�r.
    	int birler_basamagi=a%10;
    	int �c_haneli_deger=a/10;
    	int onlar_basamagi=�c_haneli_deger%10;
    	int iki_haneli_deger=�c_haneli_deger/10;
    	int y�zler_basamagi=iki_haneli_deger%10;
    	int binler_basamagi=iki_haneli_deger/10;
    	int rakamlar[]=new int [4];
    	rakamlar [0]=birler_basamagi;
    	rakamlar [1]=onlar_basamagi;
    	rakamlar [2]=y�zler_basamagi;
    	rakamlar [3]=binler_basamagi;
		return rakamlar;
    	
    }
    public static String puan_ver(int [] server_sayisi,int [] client_sayisi) {// kullanicinin tahmin sayisi ile bilgisayarin en basta tuttugu sayi karsilastirilir.
    	int pozitif_ipucu=0;
    	int negatif_ipucu=0;
    	for (int i=0;i<4;i++) {
    		if(server_sayisi[i]==client_sayisi[i]) {
    			pozitif_ipucu+=1;
    		}
    		else negatif_ipucu+=-1;
    	}
    	if(pozitif_ipucu==4) {
    		return "TEBR�KLER SAY�Y� B�LD�N�Z";
    	}
		return pozitif_ipucu+"/"+negatif_ipucu;
    }
}
