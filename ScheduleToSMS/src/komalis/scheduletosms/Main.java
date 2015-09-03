package komalis.scheduletosms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

public class Main
{

	public static void main(String[] args)
	{
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		File f = new File("credits.txt");
		if (f.exists() && !f.isDirectory())
		{
			secondLaunch();
		}
		else
		{
			firstLaunch();
			secondLaunch();
		}
	}

	public static void firstLaunch()
	{
		String pseudonyme, password, accesstoken, number;
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.print("[UVHC] Pseudonyme: ");
		pseudonyme = sc.nextLine();
		System.out.print("[UVHC] Password: ");
		password = sc.nextLine();
		System.out.print("[PushBullet] Access-Token: ");
		accesstoken = sc.nextLine();
		System.out.print("[User] Telephone Number: ");
		number = sc.nextLine();
		Utilisateur user = new Utilisateur(pseudonyme, password, accesstoken, number);
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

	public static Utilisateur readSave()
	{
		Utilisateur user = null;
		try
		{
			FileReader fw = new FileReader("credits.txt");
			JsonReader jsonreader = Json.createReader(fw);
			JsonObject jsonobject = jsonreader.readObject();
			user = new Utilisateur(jsonobject);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (JsonParsingException e)
		{
			firstLaunch();
		}
		return user;
	}

	public static void secondLaunch()
	{
		Utilisateur user = readSave();
		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		int choix = 0;
		while (choix != 6)
		{
			choix = 0;
			while (choix <= 0 || choix > 6)
			{
				System.out.println("1) Lancer le système de message automatique");
				System.out.println("2) Obtenir l'emploi du temps du jour par SMS");
				System.out.println("3) Obtenir le prochain cours par SMS");
				System.out.println("4) Reset des informations utilisateurs");
				System.out.println("5) Changer la date");
				System.out.println("6) Quitter");
				System.out.print("\nChoix: ");
				choix = sc.nextInt();
			}
			switch (choix)
			{
				case 1:
					break;
				case 2:
					Journee journee = new Journee(date);
					SMS sms = new SMS(user, journee.toString());
					sms.sendSMS();
					break;
				case 3:
					break;
				case 4:
					firstLaunch();
					secondLaunch();
					break;
				case 5:
					break;
				case 6:
					break;

			}
		}
	}
}
