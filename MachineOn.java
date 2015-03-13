import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;


class MachineOn
{
	public static void main(String[] args){
		String Version = "1.0.0";
		String SysOS = "Unkown", IPAddr = "Unkown", MacAddr = "Unkown", HostName = "Unkown";
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
			IPAddr = ip.getHostAddress();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			HostName = InetAddress.getLocalHost().getHostName();
			byte[] mac = network.getHardwareAddress();



			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			MacAddr = sb.toString();


		} catch (UnknownHostException e) {

			e.printStackTrace();

		} catch (SocketException e){

			e.printStackTrace();

		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String SysTime = dateFormat.format(date);


		String UserName = System.getProperty("user.name");
		System.out.println();
		System.out.println("Current MAC address : " + MacAddr);
		System.out.println("Current IP address : " + IPAddr);
		System.out.println("Host Name : " + HostName);
		System.out.println("Machine OS : " + SysOS);
		System.out.println("Machine local time : " + SysTime);
		System.out.println("*UserName : " + UserName +" <- This is the key(IMPORTANT!)");
		System.out.println("Machine local time : " + SysTime);
		
	}
}