package sayý_tahmin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class client_side extends javax.swing.JFrame {
	 	private ObjectOutputStream output_stream;
	    private ObjectInputStream input_stream;
	    private String message="";
	    private String serverIP;
	    private Socket connection;
	    private int port = 6789;
	    private javax.swing.JTextArea chatArea;
   	 	private javax.swing.JButton jButton1;
   	 	private javax.swing.JLabel jLabel1;
   	 	private javax.swing.JLabel jLabel2;
   	 	private javax.swing.JPanel jPanel1;
   	 	private javax.swing.JScrollPane jScrollPane1;
   	 	private javax.swing.JTextField jTextField1;
   	 	private javax.swing.JLabel durum;
   	 	public client_side() { //constructor metod
   	 		arayüz_client();
   	 		this.setTitle("Client");
   	 		this.setVisible(true);
   	 		setSize(450,450);
   	 		durum.setVisible(true);
   	 		serverIP = "127.0.0.1";
   	 	}
		private void arayüz_client() {//mesajlaþma uygulamasýnýn arayüzü oluþturulur
	    	 
	    	 jPanel1 = new javax.swing.JPanel();
	    	 jTextField1 = new javax.swing.JTextField();
	    	 jButton1 = new javax.swing.JButton();
	    	 jScrollPane1 = new javax.swing.JScrollPane();
	    	 chatArea = new javax.swing.JTextArea();
	    	 chatArea.setLineWrap(true);
	    	 jLabel2 = new javax.swing.JLabel();
	    	 durum = new javax.swing.JLabel();
	    	 durum.setForeground(Color.MAGENTA);
	    	 jLabel1 = new javax.swing.JLabel();
	    	 setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	         setResizable(true);
	         jPanel1.setLayout(null);
	         jTextField1.addActionListener(new java.awt.event.ActionListener() {
	             public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	 sendMessage(jTextField1.getText());// kullanicinin girdigi mesaj enter tusuna basilirsa sendMessage fonksiyonuna atilir.
		             jTextField1.setText("");// ve textfield temizlenir.
	             }
	         });
	         jPanel1.add(jTextField1);
	         jTextField1.setBounds(30, 50, 270, 30);
	         jButton1.setText("GÖNDER");
	         jButton1.addActionListener(new java.awt.event.ActionListener() {
	             public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	 sendMessage(jTextField1.getText());// kullanicinin girdigi mesaj butona basilirsa sendMessage fonksiyonuna atilir.
	            	 jTextField1.setText("");// ve textfield temizlenir.
	             }
	         });
	         jPanel1.add(jButton1);
	         jButton1.setBounds(310, 50, 80, 30);
	         jButton1.setSize(110, 30);
	         chatArea.setColumns(20);
	         chatArea.setRows(5);
	         jScrollPane1.setViewportView(chatArea);
	         jPanel1.add(jScrollPane1);
	         jScrollPane1.setBounds(30, 110, 360, 270);
	         jLabel2.setText("Tahmininiz Nedir");
	         jLabel2.setForeground(Color.MAGENTA);
	         jPanel1.add(jLabel2);
	         jLabel2.setBounds(30, 30, 150, 20);
	         durum.setText("...");
	         jPanel1.add(durum);
	         durum.setBounds(30, 80, 300, 40);
	         jLabel1.setForeground(Color.black);
	         jPanel1.add(jLabel1);
	         jLabel1.setBounds(0, 0, 420, 410);
	         javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	         getContentPane().setLayout(layout);
	         layout.setHorizontalGroup(
	                 layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                 .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
	             );
	             layout.setVerticalGroup(
	                 layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                 .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
	             );

	             setSize(new java.awt.Dimension(414, 428));
	             setLocationRelativeTo(null);
	    }
		public void iletisim_kur() {
			try
		       {
		            durum.setText("Baðlantý için bekleniyor");
		            try
		            {
		                connection = new Socket(InetAddress.getByName(serverIP),port);//socket ile baglanti kurulur.
		            }catch(IOException ioEception)
		            {
		                    JOptionPane.showMessageDialog(null,"Server þuan kapalý","UYARI",JOptionPane.WARNING_MESSAGE);
		            }
		            durum.setText("Þu server ile baðlantý kuruldu: " + connection.getInetAddress().getHostName());
		            output_stream = new ObjectOutputStream(connection.getOutputStream());
		            output_stream.flush();
		            input_stream = new ObjectInputStream(connection.getInputStream());

		            iletisim_süreci();
		       }
			catch(IOException ioException)
		       {
		            ioException.printStackTrace();
		       }
		}
		 private void iletisim_süreci() throws IOException
		    {
		      jTextField1.setEditable(true);
		      do{
		              try
		              {
		                      message = (String) input_stream.readObject();//bilgisayarin gönderdigi mesaj input stream ile alýnýr.
		                      chatArea.append("\n"+message);//bilgisayarin gönderdigi mesaj chatareada gösterilir.
		              }
		              catch(ClassNotFoundException classNotFoundException)
		              {
		            	  System.out.print("HATA");
		              }
		      }while(!message.equals("Client - END"));
		    }
		private void sendMessage(String message)
	    {
	        try
	        {
	            output_stream.writeObject("Client - " + message);// Kullanicinin gönderdigi mesaj
	            output_stream.flush();
	            chatArea.append("\nClient - "+message);//kullanicinin mesajinin chatareada gözükmesi
	        }
	        catch(IOException ioException)
	        {
	            chatArea.append("\n Unable to Send Message");
	        }
	    }

}
