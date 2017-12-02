using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Text;
using System.IO;
using System.Collections.Generic;
using System.Resources;

//office 1.24
// State object for receiving data from remote device.
/*public class StateObject
{
    // Client socket.
    public Socket workSocket = null;
    // Size of receive buffer.
    public const int BufferSize = 256;
    // Receive buffer.
    public byte[] buffer = new byte[BufferSize];
    // Received data string.
    public StringBuilder sb = new StringBuilder();
    private const int port = 11000;
    public static string local_Path = "B:\\";
    
}
*/
public class AsynchronousClient
{


    // Client socket.
    public Socket workSocket = null;
    // Size of receive buffer.
    public const int BufferSize = 256;
    // Receive buffer.
    public static byte[] buffer = new byte[BufferSize];
    // Received data string.
    public StringBuilder sb = new StringBuilder();
    private const int port = 11000;
    public static string local_Path = "B:\\";
    // The port number for the remote device.
    
    // ManualResetEvent instances signal completion.
    private static ManualResetEvent connectDone =
        new ManualResetEvent(false);
    private static ManualResetEvent sendDone =
        new ManualResetEvent(false);
    private static ManualResetEvent receiveDone =
        new ManualResetEvent(false);
    
    // The response from the remote device.
    private static String response = String.Empty;

    private static void StartClient()
    {
        // Connect to a remote device.
        try
        {
            // Establish the remote endpoint for the socket.
            // The name of the 
            // remote device is "host.contoso.com".
           // IPHostEntry ipHostInfo = Dns.Resolve("ahmad");
            IPAddress ipAddress = IPAddress.Parse("172.17.29.107");//ipHostInfo.AddressList[0];
            IPEndPoint remoteEP = new IPEndPoint(ipAddress, port);


            // Create a TCP/IP socket.
            Socket client = new Socket(AddressFamily.InterNetwork,
                SocketType.Stream, ProtocolType.Tcp);

            // Connect to the remote endpoint.
            client.Connect(remoteEP);
            /*
            client.BeginConnect(remoteEP,
                new AsyncCallback(ConnectCallback), client);
            connectDone.WaitOne();
            /*
             
             */
            Console.WriteLine("Socket connected to {0}",
                    client.RemoteEndPoint.ToString());
            // Send test data to the remote device.
            // Encode the data string into a byte array.
            
           
            byte[] msg = Encoding.ASCII.GetBytes("This is a test<EOF>");
            int bytesSent, bytesRec;
            /*
           // Send the data through the socket.
           int bytesSent = client.Send(msg);
            
           // Receive the response from the remote device.
           int bytesRec = client.Receive(buffer);
           Console.WriteLine("Echoed test = {0}",
               Encoding.ASCII.GetString(buffer, 0, bytesRec));

          /*/
            // Write the response to the console.
            //Console.WriteLine("Response received : {0}", response);
            
            Console.WriteLine("The allowable commands are:\n ls : list the contents of the current directory at the server \n cd Direcory : change the current directory at the server to Directory ");
            while (true)
            {
                Console.WriteLine("Enter the command: ");
               
                string com = Console.ReadLine();

               
                
                if (com.StartsWith("ls"))
                {
                    msg = Encoding.ASCII.GetBytes(com + " <EOF>");
                    bytesSent = client.Send(msg);
                    // Receive the response from the remote device.
                    bytesRec = client.Receive(buffer);
                    response = Encoding.ASCII.GetString(buffer, 0, bytesRec);


                    // Write the response to the console.
                    string[] files = response.Split('*');
                    Console.WriteLine("The files are: \n");
                    foreach (string f in files)
                    {
                        Console.WriteLine(" {0} ", f);
                    }
                }
                else
                {
                    if (com.StartsWith("cd"))
                    {
                        msg = Encoding.ASCII.GetBytes(com + " <EOF>");
                        bytesSent = client.Send(msg);
                        // Receive the response from the remote device.
                        bytesRec = client.Receive(buffer);
                        response = Encoding.ASCII.GetString(buffer, 0, bytesRec);

                        // Write the response to the console.
                        
                            Console.WriteLine("The directory now is :  "+ response);
                    }
                    else
                    {
                        if (com.StartsWith("lcd"))
                        {
                            // this changes the directory of the client also according to a procedure, this path will be used to store the downladed files


                                string[] strss = com.Split(' ');
                                // each client should have his directory sooo(threads) 
                                // the change in the directory can not be done as I want it should have a policy such that I can go under a specific root only
                                if (com.Split(' ').Length > 1)
                                {
                                    if (com.Split(' ')[1] == "..")
                                    {
                                        //if((local_Path == "B:\\") || (local_Path.Split('\\').Length == 1))
                                        if(local_Path == "B:\\")
                                        {
                                            
                                            Console.WriteLine("You are in the root directory");


                                        }
                                        else
                                        {
                                            string[] dirseq1 = local_Path.Split('\\');
                                            string[] dirseq = new string[dirseq1.Length];
                                            
                                            for (int i = 0; i < dirseq.Length;i++ )
                                            {
                                                if (dirseq1[i] != null && dirseq1[i] !="")
                                                {
                                                    dirseq[i] = dirseq1[i];
                                                }
                                            }
                                            local_Path = "";
                                            for (int i = 0; i < dirseq.Length - 1; i++)
                                            {
                                                if (dirseq[i] != "" && dirseq[i] != null)
                                                    local_Path += dirseq[i] + "\\";
                                            }

                                            Console.WriteLine("You are in this directory :  "+ local_Path);
                                        }
                                    }
                                    else
                                    {

                                        if (com.Split(' ')[1] == ".")
                                        {

                                            Console.WriteLine("You are in this directory :  " + local_Path);
                                        }
                                        else
                                        {

                                            if (com.Split(' ')[1] != null)
                                            {
                                                bool flag = false;
                                                string[] pathfromC = com.Split(' ')[1].Split('\\');
                                                if (pathfromC.Length == 1)
                                                {
                                                    string[] dirsd2 = Directory.GetDirectories(local_Path);
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
                                                        if (d == com.Split(' ')[1])
                                                        {
                                                            local_Path = local_Path + "\\" + d;
                                                            flag = true;

                                                            break;
                                                        }
                                                    }
                                                    Console.WriteLine("The new directory is :  " + local_Path);
                                                }
                                                else
                                                {
                                                    if (pathfromC[0] == "B:")
                                                    {

                                                        string newpath = "B:";

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
                                                                Console.WriteLine("NO such directory " );
                                                                break;
                                                            }
                                                        }
                                                        if (flag == true)
                                                        {
                                                            local_Path = newpath;
                                                            Console.WriteLine("The new directory is :  " + local_Path);
                                                            //directory updated
                                                        }
                                                    }
                                                    else
                                                    {
                                                        string[] dirsd4 = Directory.GetDirectories(local_Path);
                                                        bool flag2 = false;
                                                        string added = "";
                                                        for (int pp = 0; pp < pathfromC.Length; pp++)
                                                        {
                                                            flag2 = false;
                                                            dirsd4 = Directory.GetDirectories(local_Path + added);
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
                                                                Console.WriteLine("No such directory");
                                                            }
                                                            

                                                        }

                                                        if (flag2 == true)
                                                        {
                                                            local_Path = local_Path + added;
                                                            //directory updated
                                                            Console.WriteLine("The new directory is : " + local_Path);
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
                            if (com.StartsWith("put"))
                            {
                                int MAX_CHUNK_SIZE = (1024 * 5000);
                                BinaryWriter fileWriter;
                                FileStream fileReader;
                                NetworkStream clientStream = new NetworkStream(client);
                                msg = Encoding.ASCII.GetBytes(com + " <EOF>");


                                string fileName = com.Split(' ')[1];// "test1.txt";// "Your File Name";

                                string filePath = local_Path;// @"D:\";//Your File Path;

                               

                                //byte[] fileData = File.ReadAllBytes(filePath + fileName);
                                if (!File.Exists(filePath + fileName))
                                {
                                    Console.WriteLine("No such file");
                                }
                                else
                                {
                                    Console.WriteLine("Streaming>>>");
                                    bytesSent = client.Send(msg);
                                    byte[] fileNameByte = Encoding.ASCII.GetBytes(fileName);
                                    //this also reads:
                                    fileReader = new FileStream(filePath + fileName, FileMode.Open, FileAccess.Read);
                                    UInt32 numberOfBytesToRead = (UInt32)fileReader.Length;

                                    if (fileName != null)
                                    {
                                        byte[] fileNameBytes = Encoding.UTF8.GetBytes(fileName);
                                        byte[] fileNameLength = BitConverter.GetBytes(fileName.Length);
                                        byte[] dataSize = BitConverter.GetBytes(numberOfBytesToRead);
                                        //byte[] clientData = new byte[4 + fileName.Length + sizeof(UInt32)];
                                        byte[] clientData = new byte[4 + fileName.Length + dataSize.Length];//filenamebytes.lenth  instead of filename
                                        fileNameLength.CopyTo(clientData, 0);
                                        fileNameBytes.CopyTo(clientData, 4);
                                        dataSize.CopyTo(clientData, (fileName.Length + 4));
                                        string tessst = Encoding.UTF8.GetString(clientData, 4, fileNameBytes.Length);
                                        clientStream.Write(clientData, 0, clientData.Length);
                                        //Flush() use to clear all data from Stream object to future use.
                                        clientStream.Flush();
                                        clientData = new byte[1024 * 5000];
                                        int len = BitConverter.ToInt32(dataSize, 0);
                                        fileReader.Read(clientData, 0, BitConverter.ToInt32(dataSize, 0));
                                        string tesssst = Encoding.UTF8.GetString(clientData, 0, BitConverter.ToInt32(dataSize, 0));
                                        clientStream.Write(clientData, 0, clientData.Length);
                                        clientStream.Flush();
                                    }

                                    fileReader.Close();
                                    //Communication completed, now free all resource
                                    clientStream.Close();
                                    //until here new 
                                    Console.WriteLine("Done");
                                }
                                /*
                                byte[] clientData = new byte[4 + fileNameByte.Length + fileData.Length];

                                byte[] fileNameLen = BitConverter.GetBytes(fileNameByte.Length);

                                Console.WriteLine("3bina b array");

                                fileNameLen.CopyTo(clientData, 0);

                                fileNameByte.CopyTo(clientData, 4);

                                fileData.CopyTo(clientData, 4 + fileNameByte.Length);
                               


                                Console.WriteLine("8bl alsend");
                                client.Send(clientData);
                                Console.WriteLine("b3d alsend");
                                 */ 
                            }
                            else
                            {
                                if (com.StartsWith("jet"))
                                {
                                    string rd;
                                    byte[] b1;
                                    string v;
                                    int m;
                                    msg = Encoding.ASCII.GetBytes(com + " <EOF>");
                                        bytesSent = client.Send(msg);

                                        bytesRec = client.Receive(buffer);
                                        response = Encoding.ASCII.GetString(buffer, 0, bytesRec);
                                        if (response == "NO")
                                        {
                                            Console.WriteLine("No such file");
                                        }
                                        else
                                        {
                                            Console.WriteLine("Streaming");

                                            /*
                                            TcpListener list = new TcpListener(IPAddress.Parse("192.168.1.195"),port);
                                            list.Start();
                                            TcpClient clienta = list.AcceptTcpClient();
                                            //MessageBox.Show("Client trying to connect");
                                            StreamReader sr = new StreamReader(clienta.GetStream());
                                            rd = sr.ReadLine();
                                            v = rd.Substring(rd.LastIndexOf('.') + 1);
                                            m = int.Parse(v);
                                            list.Stop();
                                            client.Close();
                                            */

                                        }

                                }
                                else
                                {
                                    if (com.StartsWith("get"))
                                    {

                                        msg = Encoding.ASCII.GetBytes(com + " <EOF>");
                                        bytesSent = client.Send(msg);

                                        bytesRec = client.Receive(buffer);
                                        response = Encoding.ASCII.GetString(buffer, 0, bytesRec);
                                        if (response == "NO")
                                        {
                                            Console.WriteLine("No such file");
                                        }
                                        else
                                        {
                                            Console.WriteLine("Streaming");
                                            byte[] clientData = new byte[1024 * 5000];

                                            //string receivedPath = local_Path;// "B:\\";
                                            //streaming
                                            int MAX_CHUNK_SIZE = (1024 * 5000);
                                            byte[] fileData = new byte[MAX_CHUNK_SIZE];
                                            NetworkStream serverStream = new NetworkStream(client);

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
                                            string filePath;
                                            if (local_Path.EndsWith("\\"))
                                            {
                                                filePath = local_Path + fileNamee;
                                            }
                                            else
                                            {
                                                filePath = local_Path + "\\" + fileNamee;
                                            }
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
                                            //fileData = new byte[fileLength];
                                            //  while (fileLength != 0 && client.Connected)
                                            // {

                                            int byteTotal = 0;
                                            int byteToRead = 0;
                                            string fin = "";
                                            byte[] con = Encoding.UTF8.GetBytes("OK");
                                            // while (byteTotal < length.Length || serverStream.DataAvailable)
                                            //while (!fin.Equals("ZZZZZ") && (byteTotal < fileLength))
                                            while (byteTotal < fileLength && serverStream.DataAvailable)
                                            {
                                                Console.WriteLine("sdsd  " + byteTotal + "    " + fileLength);
                                                //when it comes to read it stopes why ???
                                                if (serverStream.CanRead)
                                                {
                                                    if ((fileLength - byteTotal) < fileData.Length)
                                                    {
                                                        byteToRead = (fileLength - byteTotal);
                                                    }
                                                    else
                                                        byteToRead = fileData.Length;

                                                    //serverStream.Write(con, 0, 2);
                                                    //serverStream.Flush();
                                                    while (!serverStream.DataAvailable)
                                                    {

                                                    }
                                                    while (!serverStream.CanRead)
                                                    {

                                                    }
                                                    int dataBytes = serverStream.Read(fileData, 0, byteToRead);// (fileData.Length - byteTotal));
                                                    byteTotal += byteToRead;

                                                    fin = Encoding.UTF8.GetString(fileData, 0, 5);
                                                    if (fin.StartsWith("ZZZZZ"))
                                                    {
                                                        break;
                                                    }

                                                    //int bytesRead = byteTotal;
                                                    //fileWriter.Close();
                                                    //fileWriter = new BinaryWriter(File.Open(filePath, FileMode.Append));
                                                    if (byteToRead != 0)
                                                    {

                                                        fileWriter.Write(fileData, 0, byteToRead);

                                                        fileWriter.Flush();
                                                        // fileLength -= (Int32)byteToRead;
                                                        // if (fileLength != 0)
                                                        // {
                                                        //    fileData = fileLength < MAX_CHUNK_SIZE ? new byte[fileLength] : new byte[MAX_CHUNK_SIZE];
                                                        // }



                                                    }
                                                }

                                            };

                                            fileWriter.Close();

                                            serverStream.Close();
                                            Console.WriteLine("DONE");

                                        }


                                    }

                                    else
                                    {
                                        if (com.StartsWith("exit"))
                                        {
                                            break;

                                        }
                                        else
                                        {
                                            if (com.StartsWith("pot"))
                                            {
                                                 string fileName = com.Split(' ')[1];// "test1.txt";// "Your File Name";

                                                string filePath = local_Path;// @"D:\";//Your File Path;

                                                if (!File.Exists(filePath + fileName))
                                                {
                                                    Console.WriteLine("No such file");
                                                }
                                                else
                                                {
                                                    Console.WriteLine("Streaming>>>");
                                                    File.Copy((filePath + fileName), "D:\\" + fileName);
                                                }
                                            }
                                                 
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Release the socket.
            client.Shutdown(SocketShutdown.Both);
            client.Close();

        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }

    private static void ConnectCallback(IAsyncResult ar)
    {
        try
        {
            // Retrieve the socket from the state object.
            Socket client = (Socket)ar.AsyncState;

            // Complete the connection.
            client.EndConnect(ar);

            Console.WriteLine("Socket connected to {0}",
                client.RemoteEndPoint.ToString());

            // Signal that the connection has been made.
            connectDone.Set();
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }
    /*
    private static void Receive(Socket client)
    {
        try
        {
            // Create the state object.
            StateObject state = new StateObject();
            state.workSocket = client;

            // Begin receiving the data from the remote device.
            client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                new AsyncCallback(ReceiveCallback), state);
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }

    private static void ReceiveCallback(IAsyncResult ar)
    {
        try
        {
            // Retrieve the state object and the client socket 
            // from the asynchronous state object.
            StateObject state = (StateObject)ar.AsyncState;
            Socket client = state.workSocket;

            // Read data from the remote device.
            int bytesRead = client.EndReceive(ar);

            if (bytesRead > 0)
            {
                // There might be more data, so store the data received so far.
                state.sb.Append(Encoding.ASCII.GetString(state.buffer, 0, bytesRead));

                // Get the rest of the data.
                client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                    new AsyncCallback(ReceiveCallback), state);
            }
            else
            {
                // All the data has arrived; put it in response.
                if (state.sb.Length > 1)
                {
                    response = state.sb.ToString();
                }
                // Signal that all bytes have been received.
                receiveDone.Set();
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }

    private static void Send(Socket client, String data)
    {
        // Convert the string data to byte data using ASCII encoding.
        byte[] byteData = Encoding.ASCII.GetBytes(data);

        // Begin sending the data to the remote device.
        client.BeginSend(byteData, 0, byteData.Length, 0,
            new AsyncCallback(SendCallback), client);
    }

    private static void SendCallback(IAsyncResult ar)
    {
        try
        {
            // Retrieve the socket from the state object.
            Socket client = (Socket)ar.AsyncState;

            // Complete sending the data to the remote device.
            int bytesSent = client.EndSend(ar);
            Console.WriteLine("Sent {0} bytes to server.", bytesSent);

            // Signal that all bytes have been sent.
            sendDone.Set();
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }
    */
    public static int Main(String[] args)
    {
        StartClient();
        return 0;
    }
}