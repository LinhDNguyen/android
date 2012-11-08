package linh.myserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyServer extends Activity {
	ServerSocket ss = null;
	String mClientMsg = "";
	Thread myCommsThread = null;
	protected static final int MSG_ID = 0x1337;
	public static final int SERVERPORT = 11900;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView tv = (TextView) findViewById(R.id.TextView01);
		ImageView iv = (ImageView) findViewById(R.id.imageView1);
		tv.setText("Nothing from client yet");
		this.myCommsThread = new Thread(new CommsThread());
		this.myCommsThread.start();

		// Force screen on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		iv.setImageResource(R.drawable.ic_launcher);
	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			// make sure you close the socket upon exiting
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Handler myUpdateHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ID:
				TextView tv = (TextView) findViewById(R.id.TextView01);
				tv.setText(mClientMsg);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	class CommsThread implements Runnable {
		public void run() {
			Socket s = null;
			try {
				ss = new ServerSocket(SERVERPORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {
				Message m = new Message();
				m.what = MSG_ID;
				try {
					if (s == null)
						s = ss.accept();
					BufferedReader input = new BufferedReader(
							new InputStreamReader(s.getInputStream()));
					String st = null;
					st = input.readLine();

					mClientMsg = st;
					myUpdateHandler.sendMessage(m);
					if(st.contains("send"))
						Toast.makeText(getParent(), mClientMsg, 2).show();
					if (st.contains("exit")) {
						s.close();
						s = null;
					} else if (st.contains("send")) {
						int filesize = 6022386; // filesize temporary hardcoded
						int bytesRead;
						int current = 0;
						// receive file:
						String fname = st.substring(4).trim();
						String strArr[] = fname.split("[\\/]");
						fname = strArr[strArr.length - 1];

						byte[] mybytearray = new byte[filesize];
						InputStream is = s.getInputStream();
						FileOutputStream fos = new FileOutputStream(Environment
								.getExternalStorageDirectory().getPath()
								+ "/" + fname);
						BufferedOutputStream bos = new BufferedOutputStream(fos);
						bytesRead = is.read(mybytearray, 0, mybytearray.length);
						current = bytesRead;

						// thanks to A. Cádiz for the bug fix
						do {
							bytesRead = is.read(mybytearray, current,
									(mybytearray.length - current));
							if (bytesRead >= 0)
								current += bytesRead;
						} while (bytesRead > -1);

						bos.write(mybytearray, 0, current);
						bos.flush();
						bos.close();

						mClientMsg = "File: " + fname + " RECEIVED!!!";
						myUpdateHandler.sendMessage(m);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}