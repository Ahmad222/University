using System;
using System.Collections;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

// State object for reading client data asynchronously


public class AsynchronousSocketListener
{

    // Data buffer for incoming data.
    public static byte[] bytes = new Byte[1024];

    // Thread signal.
    public static ManualResetEvent allDone = new ManualResetEvent(false);

    // Client  socket.
    public Socket workSocket = null;
    // Size of receive buffer.
    public const int BufferSize = 1024;
    // Receive buffer.
    public byte[] buffer = new byte[BufferSize];
    // Received data string.
    public StringBuilder sb = new StringBuilder();

    // Incoming data from the client.
    public static string data = null;


    public static void StartListening()
    {
        
        // Establish the local endpoint for the socket.
        // The DNS name of the computer
        // running the listener is "host.contoso.com".
        IPHostEntry ipHostInfo = Dns.Resolve(Dns.GetHostName());
        IPAddress ipAddress = ipHostInfo.AddressList[0];
        Console.WriteLine(ipAddress.ToString());
        IPEndPoint localEndPoint = new IPEndPoint(ipAddress, 11000);

        // Create a TCP/IP socket.
        Socket listener = new Socket(AddressFamily.InterNetwork,
            SocketType.Stream, ProtocolType.Tcp);

        // Bind the socket to the local endpoint and listen for incoming connections.
        try
        {
            listener.Bind(localEndPoint);
            listener.Listen(100);

            while (true)
            {
                // Set the event to nonsignaled state.
                // allDone.Reset();

                // Start an asynchronous socket to listen for connections.
                Console.WriteLine("Waiting for a connection...");
                // Program is suspended while waiting for an incoming connection.
                Socket handler = listener.Accept();
                data = null;
                //ThreadStart th = new ThreadStart(fun);
                Thread clientThread = new Thread(new ParameterizedThreadStart(fun));
                clientThread.Start(handler);
                

            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }

        Console.WriteLine("\nPress ENTER to continue...");
        Console.Read();

    }
    public static void fun(object ss)
    {
        Socket s = (Socket)ss;
        Socket handler = s;// state.workSocket;
        bytes = new byte[1024];
        Console.WriteLine("Begin...");
        string prive_path = "D:\\";
        byte[] b1;
        Stream strm;
        while (true)
        {
            // Read data from the client socket. 

            int bytesRec = handler.Receive(bytes);
            //int bytesRead = handler.EndReceive(ar);

            if (bytesRec > 0)
            {
                // There  might be more data, so store the data received so far.
                // state.sb.Append(Encoding.ASCII.GetString(
                //    state.buffer, 0, bytesRead));

                // Check for end-of-file tag. If it is not there, read 
                // more data.
                string content = Encoding.ASCII.GetString(bytes, 0, bytesRec);// state.sb.ToString();
                if (content.IndexOf("<EOF>") > -1)
                {

                    if (content.StartsWith("ls"))
                    {
                        String filess = "";
                        try
                        {
                            // Only get files that begin with the letter "c." 
                            string[] dirsf = Directory.GetFiles(prive_path);
                            string[] dirsd = Directory.GetDirectories(prive_path);
                            //Console.WriteLine("The number of files is {0}.", dirsf.Length + dirsd.Length);

                            foreach (string dir in dirsd)
                            {

                                string[] name = dir.Split('\\');
                                //Console.WriteLine(name[name.Length - 1]);
                                filess += name[name.Length - 1] + "*";

                            }
                            foreach (string dir in dirsf)
                            {

                                string[] name = dir.Split('\\');
                                //Console.WriteLine(name[name.Length - 1]);
                                filess += name[name.Length - 1] + "*";

                            }
                            byte[] msg1 = Encoding.ASCII.GetBytes(filess);
                            handler.Send(msg1);
                            msg1 = null; 
                           
                           // Send(handler, filess);
                        }
                        catch (Exception e)
                        {
                            Console.WriteLine("The process failed: {0}", e.ToString());
                            filess = "The process failed:";
                            //Send(handler, filess);
                            byte[] msg1 = Encoding.ASCII.GetBytes(filess);
                            handler.Send(msg1);
                            msg1 = null; 
                        }
                    }
                    else
                    {
                        if (content.StartsWith("cd"))
                        {
                            
                            string[] strss = content.Split(' ');
                            // each client should have his directory sooo(threads) 
                            // the change in the directory can not be done as I want it should have a policy such that I can go under a specific root only
                            if (content.Split(' ').Length > 1)
                            {
                                if (content.Split(' ')[1] == "..")
                                {
                                    if (prive_path == "D:\\" || prive_path.Split('\\').Length == 1)
                                    {
                                        // Can not go out of D
                                        
                                        
                                        byte[] msg = Encoding.ASCII.GetBytes(prive_path);
                                        handler.Send(msg);


                                    }
                                    else
                                    {
                                        
                                        string[] dirseq1 = prive_path.Split('\\');
                                        string[] dirseq = new string[dirseq1.Length];

                                        for (int i = 0; i < dirseq.Length; i++)
                                        {
                                            if (dirseq1[i] != null && dirseq1[i] != "")
                                            {
                                                dirseq[i] = dirseq1[i];
                                            }
                                        }
                                        prive_path = "";
                                        for (int i = 0; i < dirseq.Length - 1; i++)
                                        {
                                            if (dirseq[i] != "" && dirseq[i] != null)
                                                prive_path += dirseq[i] + "\\";
                                        }

                                       // Console.WriteLine("You are in this directory :  " + prive_path);
                                        byte[] msg = Encoding.ASCII.GetBytes(prive_path);
                                        handler.Send(msg);
                                    }
                                }
                                else
                                {

                                    if (content.Split(' ')[1] == ".")
                                    {
                                        //still in the same directory
                                        //Send(handler, prive_path);
                                        byte[] msg = Encoding.ASCII.GetBytes(prive_path);
                                        handler.Send(msg);

                                    }
                                    else
                                    {

                                        if (content.Split(' ')[1] != null)
                                        {
                                            bool flag = false;
                                            string[] pathfromC = content.Split(' ')[1].Split('\\');
                                            if (pathfromC.Length == 1)
                                            {
                                                string[] dirsd2 = Directory.GetDirectories(prive_path);
                                                string[] dirsd22 = new string[dirsd2.Length];
                                                int i = 0;
                                                foreach (string dir in dirsd2)
                                                {

                                                    string[] name = dir.Split('\\');
                                                    //Console.WriteLine(name[name.Length - 1]);
                                                    dirsd22[i] = name[name.Length - 1];
                                                    i++;
                                                }
                                                foreach (string d in dirsd22)
                                                {
                                                    if (d == content.Split(' ')[1])
                                                    {
                                                        prive_path = prive_path + "\\" + d;
                                                        flag = true;

                                                        break;
                                                    }
                                                }
                                                //Send(handler, prive_path);
                                                byte[] msg = Encoding.ASCII.GetBytes(prive_path);
                                                handler.Send(msg);
                                            }
                                            else
                                            {
                                                if (pathfromC[0] == "D:")
                                                {

                                                    string newpath = "D:";

                                                    for (int pp = 1; pp < pathfromC.Length; pp++)
                                                    {
                                                        flag = false;
                                                        string[] dirsd3 = Directory.GetDirectories(newpath);
                                                        string[] dirsd33 = new string[dirsd3.Length];
                                                        int i = 0;
                                                        foreach (string dir in dirsd3)
                                                        {

                                                            string[] name = dir.Split(':');
                                                            //Console.WriteLine(name[name.Length - 1]);
                                                            dirsd33[i] = name[name.Length - 1];
                                                            i++;
                                                        }
                                                        foreach (string d in dirsd33)
                                                        {
                                                            if (d == pathfromC[pp])
                                                            {
                                                                newpath = newpath + "\\" + d;
                                                                flag = true;
                                                                break;
                                                            }
                                                        }
                                                        if (flag == false)
                                                        {
                                                            //keep the path as it and return that no such directory
                                                            //Send(handler, "F");
                                                            byte[] msg = Encoding.ASCII.GetBytes(prive_path);
                                                            handler.Send(msg);
                                                            break;
                                                        }
                                                    }
                                                    if (flag == true)
                                                    {
                                                        prive_path = newpath;
                                                        //Send(handler, prive_path);
                                                        byte[] msg = Encoding.ASCII.GetBytes(prive_path);
                                                        handler.Send(msg);
                                                        //directory updated
                                                    }
                                                }
                                                else
                                                {
                                                    string[] dirsd4 = Directory.GetDirectories(prive_path);
                                                    bool flag2 = false;
                                                    string added = "";
                                                    for (int pp = 0; pp < pathfromC.Length; pp++)
                                                    {
                                                        flag2 = false;
                                                        dirsd4 = Directory.GetDirectories(prive_path + added);
                                                        string[] dirsd44 = new string[dirsd4.Length];
                                                        int i = 0;
                                                        foreach (string dir in dirsd4)
                                                        {

                                                            string[] name = dir.Split('\\');
                                                            //Console.WriteLine(name[name.Length - 1]);
                                                            dirsd44[i] = name[name.Length - 1];
                                                            i++;
                                                        }
                                                        foreach (string d in dirsd44)
                                                        {
                                                            if (d == pathfromC[pp])
                                                            {
                                                                flag2 = true;
                                                                added += "\\" + pathfromC[pp];
                                                                break;
                                                            }
                                                        }
                                                        if (flag2 == false)
                                                        {
                                                            //No such directory
                                                            //Send(handler, "F");
                                                            byte[] msg = Encoding.ASCII.GetBytes(prive_path);
                                                            handler.Send(msg);
                                                        }
                                                    }
                                                    if (flag == true)
                                                    {
                                                        prive_path = prive_path + added;
                                                        //directory updated
                                                        //Send(handler, prive_path);
                                                        byte[] msg = Encoding.ASCII.GetBytes(prive_path);
                                                        handler.Send(msg);
                                                    }
                                                }
                                            }



                                        }
                                    }
                                }


                            }
                        }
                        else
                        {
                            if(content.StartsWith("jet"))
                            {
                                string fileName = content.Split(' ')[1]; ;// "test1.txt";// "Your File Name";

                                string filePath = prive_path;// @"D:\";//Your File Path;
                                if (!File.Exists(filePath + fileName))
                                {
                                    byte[] msg = Encoding.ASCII.GetBytes("NO");
                                    handler.Send(msg);
                                }
                                else
                                {
                                    byte[] msg = Encoding.ASCII.GetBytes("YES");
                                    handler.Send(msg);
                                    byte[] fileNameByte = Encoding.ASCII.GetBytes(fileName);

                                    File.Copy((filePath + fileName), "B:\\" + fileName);
                                    /*
                                    TcpClient client = new TcpClient("SwtRascal", 5050);
                                    strm = client.GetStream();
                                    b1 = File.ReadAllBytes((filePath + fileName));
                                    strm.Write(b1, 0, b1.Length);
                                    client.Close();
                                     */ 
                                }
                            }
                            else{

                                if (content.StartsWith("get"))
                                {
                                    int MAX_CHUNK_SIZE = (1024 * 5000);
                                    BinaryWriter fileWriter;
                                    FileStream fileReader;
                                    NetworkStream clientStream = new NetworkStream(handler);

                                    string fileName = content.Split(' ')[1]; ;// "test1.txt";// "Your File Name";

                                    string filePath = prive_path;// @"D:\";//Your File Path;
                                    if (!File.Exists(filePath + fileName))
                                    {
                                        byte[] msg = Encoding.ASCII.GetBytes("NO");
                                        handler.Send(msg);
                                    }
                                    else
                                    {
                                        byte[] msg = Encoding.ASCII.GetBytes("YES");
                                        handler.Send(msg);
                                        byte[] fileNameByte = Encoding.ASCII.GetBytes(fileName);

                                        //byte[] fileData = File.ReadAllBytes(filePath + fileName);

                                        //this also reads:
                                        fileReader = new FileStream(filePath + fileName, FileMode.Open, FileAccess.Read);
                                        UInt32 numberOfBytesToRead = (UInt32)fileReader.Length;
                                        String con;
                                        byte[] cona = new byte[2];
                                        if (fileName != null)
                                        {
                                            byte[] fileNameBytes = Encoding.UTF8.GetBytes(fileName);
                                            byte[] fileNameLength = BitConverter.GetBytes(fileName.Length);
                                            byte[] dataSize = BitConverter.GetBytes(numberOfBytesToRead);
                                            //byte[] clientData = new byte[4 + fileName.Length + sizeof(UInt32)];
                                            byte[] clientData = new byte[4 + fileName.Length + dataSize.Length];//filenamebytes.lenth  instead of filename
                                            fileNameLength.CopyTo(clientData, 0);
                                            fileNameBytes.CopyTo(clientData, 4); 	//[External Code]	

                                            dataSize.CopyTo(clientData, (fileName.Length + 4));
                                            string tessst = Encoding.UTF8.GetString(clientData, 4, fileNameBytes.Length);
                                            clientStream.Write(clientData, 0, clientData.Length);
                                            //Flush() use to clear all data from Stream object to future use.
                                            clientStream.Flush();
                                            int len = BitConverter.ToInt32(dataSize, 0);
                                            int fileLength = len;
                                            //int byteToRead = 0;
                                            clientData = new byte[1024 * 5000];
                                            //  while (fileLength != 0 && handler.Connected)
                                            //  {

                                            int byteTotal = 0;
                                            int byteToRead = 0;
                                            // while (byteTotal < length.Length || serverStream.DataAvailable)
                                            while (byteTotal < fileLength)//&& serverStream.DataAvailable)
                                            {
                                                Console.WriteLine("before close" + byteTotal + "     " + fileLength + "    " + byteToRead);
                                                //when it comes to read it stopes why ???
                                                if (clientStream.CanWrite)
                                                {
                                                    if ((fileLength - byteTotal) < clientData.Length)
                                                    {
                                                        byteToRead = (fileLength - byteTotal);
                                                    }
                                                    else
                                                        byteToRead = clientData.Length;

                                                    Console.WriteLine("before close111" + byteTotal + "     " + fileLength + "    " + byteToRead);
                                                    fileReader.Read(clientData, 0, byteToRead);

                                                    //int dataBytes = serverStream.Read(fileData, byteTotal, byteToRead);// (fileData.Length - byteTotal));
                                                    byteTotal += byteToRead;//dataBytes;



                                                    // int bytesRead = byteToRead;
                                                    Console.WriteLine("weweaaaaa");
                                                    if (byteToRead != 0)
                                                    {
                                                        Console.WriteLine("wewe");
                                                        //   int c = clientStream.Read(cona, 0, 2);
                                                        //  con = Encoding.ASCII.GetString(cona, 0, 2);
                                                        // if (con == "OK")
                                                        // {
                                                        Console.WriteLine("innnn");
                                                        clientStream.Write(clientData, 0, byteToRead);
                                                        clientStream.Flush();
                                                        //}
                                                    }
                                                    Console.WriteLine("before close222" + byteTotal + "     " + fileLength + "    " + byteToRead);
                                                }
                                            };


                                        }
                                        Console.WriteLine("before closezzzz");

                                        fileReader.Close();
                                        int cc = clientStream.Read(cona, 0, 2);
                                        con = Encoding.ASCII.GetString(cona, 0, 2);
                                        if (con == "OK")
                                        {
                                            byte[] fin = Encoding.UTF8.GetBytes("ZZZZZ");
                                            clientStream.Write(fin, 0, 5);
                                            clientStream.Flush();
                                        }
                                        //Communication completed, now free all resource
                                        clientStream.Close();
                                        //until here new 
                                    }
                                    /*
                                    Console.WriteLine("Hoon");

                                    string[] strss = content.Split(' ');
                                    if (content.Split(' ').Length > 1)
                                    {
                                        string fileName = content.Split(' ')[1];// "test.txt";// "Your File Name";

                                        string filePath = prive_path;//@"C:\Users\Ahmad\Desktop\porto\";//Your File Path;

                                        byte[] fileNameByte = Encoding.ASCII.GetBytes(fileName);

                                        byte[] fileData = File.ReadAllBytes(filePath + fileName);

                                        byte[] clientData = new byte[4 + fileNameByte.Length + fileData.Length];

                                        byte[] fileNameLen = BitConverter.GetBytes(fileNameByte.Length);

                                        //Console.WriteLine("3bina b array");

                                        fileNameLen.CopyTo(clientData, 0);

                                        fileNameByte.CopyTo(clientData, 4);

                                        fileData.CopyTo(clientData, 4 + fileNameByte.Length);

                                       // Console.WriteLine("8bl alsend");
                                        handler.Send(clientData);
                                       // Console.WriteLine("b3d alsend");
                                    }
                                    */
                                }
                                else
                                {
                                    if (content.StartsWith("put"))
                                    {


                                        //streaming
                                        int MAX_CHUNK_SIZE = (1024 * 5000);
                                        byte[] fileData = new byte[MAX_CHUNK_SIZE];
                                        NetworkStream serverStream = new NetworkStream(handler);

                                        byte[] fileNameLengthBytes = new byte[sizeof(int)];
                                        serverStream.Read(fileNameLengthBytes, 0, 4);
                                        int fileNameLength = BitConverter.ToInt32(fileNameLengthBytes, 0);

                                        byte[] fileName = new byte[fileNameLength];
                                        serverStream.Read(fileName, 0, fileNameLength);

                                        string fileNamee = Encoding.UTF8.GetString(fileName, 0, fileNameLength);

                                        // Get file lengh
                                        byte[] length = new byte[sizeof(UInt32)];
                                        serverStream.Read(length, 0, sizeof(UInt32));
                                        int fileLength = BitConverter.ToInt32(length, 0);

                                        string filePath = prive_path + fileNamee;
                                        int enumeration = 1;
                                        string tempPath = filePath.Substring(0, filePath.IndexOf(".", StringComparison.Ordinal));
                                        string tempExtension = filePath.Substring(filePath.IndexOf(".", StringComparison.Ordinal), (filePath.Length - tempPath.Length));
                                        if (File.Exists(filePath))
                                        {
                                            while (File.Exists(tempPath + tempExtension))
                                            {
                                                tempPath = filePath.Substring(0, filePath.IndexOf(".", StringComparison.Ordinal));
                                                tempPath += "(" + enumeration + ")";
                                                enumeration++;
                                            }
                                            filePath = tempPath + tempExtension;
                                        }
                                        BinaryWriter fileWriter = new BinaryWriter(File.Open(filePath, FileMode.CreateNew));
                                        fileData = new byte[MAX_CHUNK_SIZE];
                                        while (fileLength != 0 && handler.Connected)
                                        {

                                            int byteTotal = 0;
                                            int byteToRead = 0;
                                            // while (byteTotal < length.Length || serverStream.DataAvailable)
                                            while (byteTotal < fileLength && serverStream.DataAvailable)
                                            {
                                                //when it comes to read it stopes why ???
                                                if (serverStream.CanRead)
                                                {
                                                    if (fileLength < fileData.Length)
                                                    {
                                                        byteToRead = fileLength;
                                                    }
                                                    else
                                                        byteToRead = fileData.Length;

                                                    int dataBytes = serverStream.Read(fileData, byteTotal, byteToRead);// (fileData.Length - byteTotal));
                                                    byteTotal += dataBytes;
                                                }
                                            };

                                            int bytesRead = byteTotal;
                                            if (bytesRead != 0)
                                            {

                                                fileWriter.Write(fileData, 0, bytesRead);
                                                fileLength -= (Int32)bytesRead;
                                                if (fileLength != 0)
                                                {
                                                    fileData = fileLength < MAX_CHUNK_SIZE ? new byte[fileLength] : new byte[MAX_CHUNK_SIZE];
                                                }



                                            }


                                        }

                                        fileWriter.Close();

                                        serverStream.Close();
                                        //break;
                                        //untill here

                                        /*
                                        byte[] clientData = new byte[1024 * 5000];

                                        string receivedPath = prive_path;// "B:\\";


                                        int receivedBytesLen = handler.Receive(clientData); /////here should change the receive 
                                        //Console.WriteLine("Hoon");


                                        int fileNameLen = BitConverter.ToInt32(clientData, 0);

                                        string fileName = Encoding.ASCII.GetString(clientData, 4, fileNameLen);


                                        BinaryWriter bWrite = new BinaryWriter(File.Open(receivedPath + fileName, FileMode.Append));

                                        bWrite.Write(clientData, 4 + fileNameLen, receivedBytesLen - 4 - fileNameLen);
                                        bWrite.Close();
                                        //Console.WriteLine("Hooniik");
                                        */
                                    }
                                    else
                                    {
                                        if (content.StartsWith("exit"))
                                        {

                                            handler.Shutdown(SocketShutdown.Both);
                                            handler.Close();
                                            return;

                                        }
                                    }
                                }
                            }
                        }

                    }
                    /*
                    // All the data has been read from the 
                    // client. Display it on the console.
                    Console.WriteLine("Read {0} bytes from socket. \n Data : {1}",
                        content.Length, content);  // receive the name of the file

                    //now look for the file in a specific directory


                    // Echo the data back to the client.
                    Send(handler, content);

                    */
                }
                else
                {
                    bytesRec = handler.Receive(bytes);
                    // Not all data received. Get more.
                    //handler.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                    //new AsyncCallback(ReadCallback), state);
                }
            }
        }

    }

    public static int Main(String[] args)
    {
        StartListening();
        return 0;
    }
}