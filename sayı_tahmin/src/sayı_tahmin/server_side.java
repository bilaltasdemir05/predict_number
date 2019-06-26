package sayý_tahmin;

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
    int server_random_sayi=sayi_üret();// program çalýþtýðýnda bilgisayarýn tuttuðu sayýdýr.
    int birler_basamagi;
    int onlar_basamagi;
    int yüzler_basamagi;
    int binler_basamagi;
    int sayi_bulundu;
	private ArrayList<Integer> temp2;
	private ArrayList<Integer> temp3;
	private ArrayList<Integer> temp4;
	private ArrayList<Integer> temp;
    public static int sayi_üret() { // 4 basamaklý bir sayý üretir
    	Random r = new Random();
		int alt = 1000;
		int üst = 10000;
		int rnd = r.nextInt(üst-alt) + alt;
		int [] random_basamak=basamaklarina_ayir(rnd);
		int x=kontrol(random_basamak);
		if(x==-1) {
			return sayi_üret();
		}
		else return rnd;
    }
    public static int  kontrol(int random_basamak[]) { // üretilen sayýnýn rakamlarýnýn farklý olup olmadýðý kontrol edilir
    	if((random_basamak[0]==random_basamak[1])||(random_basamak[0]==random_basamak[2])||(random_basamak[0]==random_basamak[3])||(random_basamak[1]==random_basamak[2])||(random_basamak[1]==random_basamak[3])||(random_basamak[2]==random_basamak[3])){
    		return -1;
    	}
    	else return 1;
    }
    public void startRunning() //server çalýþtýrýldýðýnda ilk olarak bu fonksiyon çalýþýr.
    {
        try
        {
            server=new ServerSocket(port, totalClients);
            while(true)
            {
                try
                {   //kullanýcýnýn server'a baðlanmasý için server hazýrda bekler.
                	//bilgisayar kullanýcýya oyun ile ilgili bilgileri mesaj olarak gönderir
                	System.out.println("Birinin Baðlanmasý için bekleniyor");
                    connection=server.accept();
                    System.out.println("Þimdi þu ip adresi baðlandý"+connection.getInetAddress().getHostName());                    
                    output = new ObjectOutputStream(connection.getOutputStream());
                    output.flush();
                    input = new ObjectInputStream(connection.getInputStream());
                    output.writeObject("Server : "+"Sayý tahmin oyununa hoþgeldin, bu oyunun bir kaç kuralý vardýr");                 
                    output.writeObject("Server : "+"Öncelikle tahmin etmen gereken sayý 4 basamaklý, rakamlarý birbirinden farklýdýr.");                   
                    output.writeObject("Server : "+"Ýlk cevap mesajýn 4 basamaklý bir sayý ve 0/0 ör:1234 0/0 olmalýdýr");                    
                    output.writeObject("Server : "+"Daha sonra ben tahmin ettiðin sayý için sana puan vereceðim ve tuttuðun sayý için tahminimi ayný mesaj içerisinde göndereceðim.");
                    output.writeObject("Server : "+"Puanlama sistemi þu þekilde olmaktadýr: tahmin olarak gönderdiðin sayý ile benim tuttuðum sayýda basamak deðerleri ayný olan her rakam için +1 diðer durumlarda ise -1 puan verilecektir.");
                    output.writeObject("Server : "+"Gönderdiðin mesajlarýn formatý þu þekilde olmalýdýr:1589 2/-2");                   
                    output.writeObject("Server : "+"Yani yaptýðýn tahmin ile bana verdiðin puan arasýnda bir vuruþluk boþluk býrakmalýsýn");                 
                    output.writeObject("Server : "+"Hazýrsan Baþlayalým");
                    whileChatting(); //kullanýcý her mesaj gönderdiðinde bu fonksiyon çalýþýr.
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
                        message = (String) input.readObject();//Kullanýcýnýn bilgisayara gönderdiði mesaj
                        get_user_Message(message);//Kullanýcýdan alýnan mesaj iþlenmek üzere bu fonksiyona gönderilir.
                        
                 
                }catch(ClassNotFoundException classNotFoundException)
                {
                        
                }
        }while(true);
    }
	private void get_user_Message(String message)
    {
        try
        {      
        	
        	String x=message.substring(9);//Kullanýcýnýn gönderdiði mesaj 9.indexten sonra alýnýr.
        	String client_cevap[]=x.split(" ");//alýnan mesaj boþluða göre ayrýlýp diziye atýlýr.Dizinin 1.elemaný 4 basamaklý tahmin sayýsý, ikinci elemaný ise kullanýcýnýn bilgisayara verdiði puandýr.
        	int client_tahmin_sayisi=Integer.valueOf(client_cevap[0]);//string ifade int'e dönüþtürüldü.
        	int client_rakamlari_al[]=new int [4];//kullanýcýnýn gönderdiði sayýnýn basamak deðerleri bu diziye atýlýr
        	int server_rnd_rakamlari[]=new int[4];// bilgisayarýn tuttuðu sayýnýn basamak deðerleri bu diziye atýlýr.
        	client_rakamlari_al=basamaklarina_ayir(client_tahmin_sayisi); //Kullanýcýnýn tuttuðu sayýnýn basamak deðerleri elde edilir.
        	server_rnd_rakamlari=basamaklarina_ayir(server_random_sayi); // bilgisayarýn tuttuðu sayýnýn basamak deðerleri elde edilir.
        	String serverin_verdigi_puan=puan_ver(server_rnd_rakamlari,client_rakamlari_al); //kullanýcýnýn tahmini ile bilgisayarýn tuttuðu sayý karþýlaþtýrýlýr ve bilgisayar kullanýcýya bir puan verir.
        	
        	if(!client_cevap[1].equals("0/-4") && t==5) {//kullanýcý bilgisayarýn tahminine 0/-4 puanýný verene kadar bilgisayar yeni bir sayý üretip denemeye devam eder.
        		default_sayi=sayi_üret();
        		output.writeObject("Server : "+default_sayi+" "+serverin_verdigi_puan);       			
        	}       	
        	if(client_cevap[1].equals("0/-4")) {//kullanýcý 0/-4 puanýný verdiðinde artýk üzerinde iþlem yapacaðýmýz sayý belirlenmiþ olur.(default_sayi)
        		default_basamak=basamaklarina_ayir(default_sayi);//üzerinde iþlem yapýlacak sayý basamak deðerleri elde edilir.
        		temp=new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));// geçici bir arraylist oluþturulur
        		for(int e=0;e<temp.size();e++) {
        			if(default_basamak[0]==temp.get(e)) {
        				temp.remove(e);//array listten mevcut sayýnýn birler basamaðý çýkartýlýr çünkü mevcut sayý için 0/-4 dönmüþtür ve birler basamaðýnda mevcut rakamýn olmayacaðý kesindir.
        			}
        		}
            	int yeni_sayi=(default_basamak[3]*1000)+(default_basamak[2]*100)+(default_basamak[1]*10)+temp.get(i); //birler basamaðýna sýrayla dizideki elemanlar atanýr.
        		output.writeObject("Server : "+yeni_sayi+" "+serverin_verdigi_puan);//her deðiþimde kullanýcýya bu sayý yollanýr
        		i++;
        		t=6;
        	}
        	if(client_cevap[1].equals("1/-3")&& t==6) {//kullanýcý 1/-3 puanýný verdiyse yukarýdaki kontrolden sonra birler basamaðý bulunmuþ demektir. Artýk onlar basamaðýna geçilebilir.        		
        		if(del1==5) {// Bu kontrol ise kullanici 1 den fazla 1/-3 puanýný yollarsa tekrar tekrar silme iþlemini yapmamasý için
        			temp2=new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));//geçici array list oluþturulur.
        			birler_basamagi=temp.get(i-1);//bir önceki if bloðunda yakalanan birler basamaðýný kaybetmemek için degiskene atýlýr
        			for(int e=0;e<temp2.size();e++) {// yeni olusturulan arraylistten ilk olarak birler basamagi çýkarilir. Rakamlarý farklý bir sayý tahmin ettiðimiz için birler basamaginda olan sayi baska basamakta olamayacaktir.
            			if(birler_basamagi==temp2.get(e)) {
            				temp2.remove(e);
            			}
            		}
        			if(default_basamak[1]!=birler_basamagi) {// Kullanicidan 0/-4 puanini aldigimiz sayinin birler_basamagi degiskeni ile ayný olup olmadigi kontrol edilir.Bunun sebebi
        			for(int e=0;e<temp2.size();e++) {        // birler basamagindaki rakam diziden silinmiþti eðer 0/-4 puanýný aldigimiz sayinin onlar basamagi bu rakam ile ayný ise
            			if(default_basamak[1]==temp2.get(e)) {// diziden zaten silinmiþ bir rakamý tekrar silmeye çalýþmýþ olamamak amacý ile bu kontrol yapýlmýþtýr.
            				temp2.remove(e);// kontroller saðlanmýþsa 0/-4 puanýný aldigimiz default sayinin onlar basamagi diziden silinir.
            			}
            		}
        			}
        			del1++;
        		}
    			int yeni_sayi2=(default_basamak[3]*1000)+(default_basamak[2]*100)+(temp2.get(j)*10)+birler_basamagi;//onlar basamagina dizi elemanlari atanir
    			output.writeObject("Server : "+yeni_sayi2+" "+serverin_verdigi_puan);//sýrayla kullanýcýya yollanir.
    			j++;
    		}
        	if(client_cevap[1].equals("2/-2")&& t==6) {// kullanici 2/-2 puanini verdiyse yukaridaki kontolden sonra onlar basamagi da bulunmuþ demektir. Artýk yüzler basamagina geçilebilir	
        		if(del2==5) {// Bu kontrol ise kullanici 1 den fazla 2/-2 puanýný yollarsa tekrar tekrar silme iþlemini yapmamasý için
        			temp3=new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));// geçici bir dizi oluþturulur
        			onlar_basamagi=temp2.get(j-1);// bulunan onlar basamagi deðiþkene atilir.
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
        			if(default_basamak[2]!=birler_basamagi && default_basamak[2]!=onlar_basamagi) {//default sayinin yüzler basamaginin bulunan basamak deðerleri ile ayný olup olmadigi kontrol edilir.
        			for(int e=0;e<temp3.size();e++) {// egerki ayni degil ise silme iþlemi gerçekleþtirilir.
            			if(default_basamak[2]==temp3.get(e)) {
            				temp3.remove(e);
            			}
            		}
        			}
        			del2++;
        		}
    			int yeni_sayi3=(default_basamak[3]*1000)+(temp3.get(k)*100)+(onlar_basamagi*10)+birler_basamagi;// yüzler basamagina dizi elemanlarý sýrayla yerleþtirilir.
    			output.writeObject("Server : "+yeni_sayi3+" "+serverin_verdigi_puan);//her seferinde kullaniciya mesaj olarak iletilir.
    			k++;
    		}
        	if(client_cevap[1].equals("3/-1")&& t==6) {//Kullanici 3/-1 puanini verdiyse yukaridaki kontrollerden sonra birler,onlar ve yüzler basamagi bulunmuþ demektir.    		
        		if(del3==5) {// Bu kontrol ise kullanici 1 den fazla 3/-1 puanýný yollarsa tekrar tekrar silme iþlemini yapmamasý için
        			temp4=new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));//geçici bir dizi oluþturulur.
        			yüzler_basamagi=temp3.get(k-1);//yüzler basamagini kaybetmemek için deðiþkene atilir.
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
        			for(int e=0;e<temp4.size();e++) {//yüzler basamagi array listten silinir.
            			if(yüzler_basamagi==temp4.get(e)) {
            				temp4.remove(e);
            			}
            		}
        			if(default_basamak[3]!=birler_basamagi && default_basamak[3]!=onlar_basamagi&&default_basamak[3]!=yüzler_basamagi) {// default sayinin yüzler basamagi ile tespit edilen basamaklar ayný mý kontrol edilir.
        				for(int e=0;e<temp4.size();e++) {//eger ayni degilse silme islemini yapabiliriz.Bunu yapmamizin sebebi default olarak belirlenen sayinin yüzler basamagi tespit edilen basamak degerleri ile ayný olabilir. Bu durumda dizide olmayan bir elemaný silmeye calismis oluruz.      				
        					if(default_basamak[3]==temp4.get(e)) {
        						temp4.remove(e);
        					}       				
        				}
        				}
        			del3++;
        			}      		
        		sayi_bulundu=(temp4.get(m)*1000)+(yüzler_basamagi*100)+(onlar_basamagi*10)+birler_basamagi;
    			output.writeObject("Server : "+sayi_bulundu+" "+serverin_verdigi_puan);
    			m++;
        	}
        	if(client_cevap[1].equals("4/0")&& t==6) {//Kullanici 4/0 puanini vermisse sayi bulunmus demektir.
    			output.writeObject("Server : "+"Tuttugun sayýyý buldum="+sayi_bulundu+" "+serverin_verdigi_puan);
        	}       
        	}        
        catch(IOException ioException)
        {
            
        }
        
    }
    public static int[] basamaklarina_ayir(int a) {// parametre olarak aldigi 4 basamakli sayiyinin basamak degerlerini elde eder ve bir diziye atar. Dizi döndürür.
    	int birler_basamagi=a%10;
    	int üc_haneli_deger=a/10;
    	int onlar_basamagi=üc_haneli_deger%10;
    	int iki_haneli_deger=üc_haneli_deger/10;
    	int yüzler_basamagi=iki_haneli_deger%10;
    	int binler_basamagi=iki_haneli_deger/10;
    	int rakamlar[]=new int [4];
    	rakamlar [0]=birler_basamagi;
    	rakamlar [1]=onlar_basamagi;
    	rakamlar [2]=yüzler_basamagi;
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
    		return "TEBRÝKLER SAYÝYÝ BÝLDÝNÝZ";
    	}
		return pozitif_ipucu+"/"+negatif_ipucu;
    }
}
