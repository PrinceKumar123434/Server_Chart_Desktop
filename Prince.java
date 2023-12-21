import java.net.*;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
class Prince extends JFrame
{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading=new JLabel("Prince");
    LocalDate date=LocalDate.now();
    LocalTime time=LocalTime.now();
    private JTextArea messagArea=new JTextArea("Today Date: "+date+"\n"+"Current Time: "+time+"\n");
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Robot",Font.BOLD,25);
    // Constructor...!
   public  Prince()
    {
    try
     {
        server=new ServerSocket(7773);
        System.out.println("Server is ready to accept connection");
        System.out.println("Please Wait...!");
        socket= server.accept();

        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(socket.getOutputStream());

        createGUI();
        handleEvents();
        startReading();
        // startWriting();
    } 
    catch (Exception e) 
    {
    e.printStackTrace();
    }
    }
 private void handleEvents() 
  {
    messageInput.addKeyListener(new KeyListener() 
    {

      @Override
      public void keyTyped(KeyEvent e) {}

      @Override
      public void keyPressed(KeyEvent e) {}

      @Override
      public void keyReleased(KeyEvent e) 
      {
      //System.out.println("key released "+e.getKeyCode());
      if(e.getKeyCode()==10)
      {
        // System.out.println("you hava enter");
        String contentToSend=messageInput.getText();
        messagArea.append("Me : "+contentToSend+"\n");
        out.println(contentToSend);
        out.flush();
        messageInput.setText("");
        messageInput.requestFocus();
      }
      }
      
    });
  }
  private void createGUI() 
  {
    //create gui code..
    this.setTitle(" Messager[END]");
    this.setSize(600,650);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //coding for component
    heading.setFont(font);
    messagArea.setFont(font);
    messageInput.setFont(font);

    heading.setHorizontalAlignment(SwingConstants.CENTER);
    heading.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
    messagArea.setEditable(false);
    messageInput.setHorizontalAlignment(SwingConstants.CENTER);

    //frame ke Layout set karenge
    this.setLayout(new BorderLayout());
    //adding the components to frame
    this.add(heading,BorderLayout.NORTH);
    JScrollPane jScrollPane=new JScrollPane(messagArea);
    this.add(jScrollPane,BorderLayout.CENTER);
    this.add(messageInput,BorderLayout.SOUTH);
    this.setVisible(true);

  }

    public void startReading()
    {
     //thread-read karke deta rahega.
     Runnable r1=()->
     {
    System.out.println("Reader started...!");
    try
    {
    while(true)
    {
           String msg=br.readLine();
        if(msg.equals("exit"))
        {
            System.out.println("Prashant terminate the chart");
            JOptionPane.showMessageDialog(this,"Anjali Terminated the chat");
            socket.close();
            messageInput.setEnabled(false);
            socket.close();
            break;
        }
        // System.out.println("Client : "+msg);
         messagArea.append("Prashant: "+msg+"\n");
    }
    }
    catch(Exception e)
    {
        // e.printStackTrace();
        System.out.println("Connection closed...!");
    } };
    new Thread(r1).start();
    }



    public void startWriting()
    {
    // thread-data user lega and the send karega client tak
    Runnable r2=()->
    {
        System.out.println("Writer started...!");
        try
        { 
      while(!socket.isClosed())
      {
         BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
         String content=br1.readLine();
         out.println(content);
         out.flush();  

         if(content.equals("exit"))
         {
            socket.close();
            break;
         }
        } 
        }
        catch(Exception e) 
        {
        //   e.printStackTrace(); 
        System.out.println("Connection closed...!");
        }
  };
//thread start
new Thread(r2).start();

    }
    public static void main(String[] args) 
    {
    System.out.println("My Name is Prince Kumar...!");    
    new Prince();
    }
}