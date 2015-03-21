import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.URLEncoder;


class MachineOn
{
	public static void main(String[] args){
		String Version = "1.0.0";
		String SysOS = "Unkown", IPAddr = "Unkown", MacAddr = "Unkown", HostName = "Unkown";
		String HTTPKey = "Error";
		System.out.println();
		System.out.println("**Starting MachineOn "+ Version + " by Mizzou1.com" );
		System.out.println("**Author: Guanlong Zhou  @  www.WhoIsLong.com" );
		System.out.println("**Please check status at http://on.mizzou1.com" );
		System.out.println();
		System.out.println("MachineOn Running Analysis..." );
		//Clear the console
		try
		{
			SysOS = System.getProperty("os.name");
		}
		catch (final Exception e)
		{
			//Exception
		}



		InetAddress ip;
		try {

			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			HostName = InetAddress.getLocalHost().getHostName();
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			MacAddr = sb.toString();


		} catch (UnknownHostException e) {

			//e.printStackTrace();

		} catch (SocketException e){

			//e.printStackTrace();

		} catch (NullPointerException e){
			
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String SysTime = dateFormat.format(date);


		String UserName = System.getProperty("user.name");
		System.out.println();
		System.out.println("Current MAC address : " + MacAddr);
		System.out.println("Host Name : " + HostName);
		System.out.println("Machine OS : " + SysOS);
		System.out.println("Machine local time : " + SysTime);
		System.out.println("Connecting to Server... Please wait...");

		HTTPKey=HttpRequest.sendPost("http://on.mizzou1.com/api/GetKey.php", "key=123&v=456");
		if(HTTPKey.toLowerCase().contains("error") ){
			System.out.println("\n**Server Connection Error!");
			System.out.println("**Please check your Internet connection and try again!");
			System.out.println("Program exit.");
			System.exit(0); 
		}

		IPAddr = HttpRequest.sendPost("http://ip.mizzou1.com", "");
		//System.out.println(IPAddr);
		IPAddr = PrefixIP(IPAddr);
		//String CurrentResult = HttpRequest.sendPost("http://on.mizzou1.com/api/PostTest.php", "key=" + URLEncoder.encode(HTTPKey) + "&" + "value=" +URLEncoder.encode("123 456 \n www.sina.com&Me"));
		//String CurrentResult = HttpRequest.sendPost("http://on.mizzou1.com/api/PostTest.php", "key=" + URLEncoder.encode(HTTPKey) + "&" + "value=123123");
		//System.out.println("Input Test: "+ CurrentResult);

		String CurrentResult = HttpRequest.sendPost("http://on.mizzou1.com/api/CreateStatus.php", "key=" + URLEncoder.encode(HTTPKey) + "&" + "OS=" + URLEncoder.encode(SysOS) + "&" + "MAC=" + URLEncoder.encode(" " + MacAddr + " ") + "&" + "IPAddr=" + URLEncoder.encode(IPAddr) + "&" + "UserName=" + URLEncoder.encode(UserName) );

		if(CurrentResult.toLowerCase().contains("error")){
			System.out.println("\n**Server Connection Error!");
			System.out.println("**Please check your Internet connection and try again!");
			System.out.println("Program exit.");
			System.exit(0); 
		}

		String[] IDs = CurrentResult.split(" ", 2);
		String MachineID = IDs[0];
		String RecordID = IDs[1];

		//Test Purpose
		//RecordID = Integer.toString((Integer.parseInt(RecordID)+1));


		System.out.println("\n** Please write down the following information**");
		System.out.println("Current IP: "+ IPAddr);
		//System.out.println("Current Content Test: "+ CurrentResult);
		System.out.println("Machine ID: "+ MachineID);
		System.out.println("UserName : " + UserName);
		System.out.println("Check status at http://on.mizzou1.com/machine.php?mid="+MachineID);
		System.out.println("You can also find your machine by using your username or IP");
		System.out.println();
		//System.out.println("into loop:");
		try {
			while (true) {
				String UpdateResult = HttpRequest.sendPost("http://on.mizzou1.com/api/UpdateStatus.php", "key=" + URLEncoder.encode(HTTPKey) + "&" + "RecordID=" + URLEncoder.encode(RecordID) );
				//System.out.println(UpdateResult);
				if(UpdateResult.toLowerCase().contains("error") || UpdateResult.toLowerCase().contains("restart")){
					//System.out.println("Try reconnection...");
					CurrentResult = HttpRequest.sendPost("http://on.mizzou1.com/api/CreateStatus.php", "key=" + URLEncoder.encode(HTTPKey) + "&" + "OS=" + URLEncoder.encode(SysOS) + "&" + "MAC=" + URLEncoder.encode(" " + MacAddr + " ") + "&" + "IPAddr=" + URLEncoder.encode(IPAddr) + "&" + "UserName=" + URLEncoder.encode(UserName) );
					if(CurrentResult.toLowerCase().contains("error")){
						System.out.println("\nServer Connection Error at " + new Date());
						System.out.println("**Please check your Internet connection and try again!");
						System.out.println("Program exit.");
						System.exit(0); 
					}

					IDs = CurrentResult.split(" ", 2);
					MachineID = IDs[0];
					RecordID = IDs[1];

					//System.out.println("Reconnect success!");
				}

				Thread.sleep(30 * 1000);
			}
		} catch (InterruptedException e) {
			//e.printStackTrace();
			System.out.println("\nServer Connection Error at " + new Date());
			System.out.println("Program exit.");
			System.exit(0); 
		}


	}








	private static String PrefixIP(String input){
		int IPLen = input.length();
		String output = "";
		for(int i =0; i<IPLen; i++){
			if( (input.charAt(i)>='0' && input.charAt(i)<='9') || input.charAt(i)=='.' ){
				output += input.charAt(i);
			}
		}
		return output;
	}

	private static String PrefixNumber(String input){
		int IPLen = input.length();
		String output = "";
		for(int i =0; i<IPLen; i++){
			if( (input.charAt(i)>'0' && input.charAt(i)<'9')){
				output += input.charAt(i);
			}
		}
		return output;
	}

}