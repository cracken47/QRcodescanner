package com.karan.qrcodescanner;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectTask extends AsyncTask<String, Void , Void> {

    Socket s;
    PrintWriter pw;
    DataOutputStream dos;

    @Override
    protected Void doInBackground(String... message) {


        String messages = message[0];

        //we create a TCPClient object
        try {
            s= new Socket("192.168.4.1",4096 ) ;

            pw = new PrintWriter(s.getOutputStream());

            pw.write(messages);
            pw.flush();
            pw.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


//            @Override
//            //here the messageReceived method is implemented
//            public void messageReceived(String message) {
//                //this method calls the onProgressUpdate
//                publishProgress(message);
//            }
//        });
//        mTcpClient.run();

        return null;
    }

//    @Override
//    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
//        //response received from server
//        Log.d("test", "response " + values[0]);
//        //process server response here....
//
//    }

}
