package komalis.scheduletosms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main
{

	public static void main(String[] args)
	{
		File f = new File("credits.txt");
		if (f.exists() && !f.isDirectory())
		{
			;
		}
		else
		{
			firstLaunch();
		}
	}

	public static void firstLaunch()
	{
		String pseudonyme, password, accesstoken;
		Scanner sc = new Scanner(System.in);
		System.out.print("[UVHC] Pseudonyme: ");
		pseudonyme = sc.nextLine();
		System.out.print("[UVHC] Password: ");
		password = sc.nextLine();
		System.out.print("[PushBullet] Access-Token: ");
		accesstoken = sc.nextLine();
		Utilisateur user = new Utilisateur(pseudonyme, password, accesstoken);
		try
		{
			FileWriter fw = new FileWriter("credits.txt");
			fw.write(user.toJson().toString());
			fw.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
