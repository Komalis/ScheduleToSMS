package komalis.scheduletosms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

public class Utilisateur
{
	private String m_pseudonyme;
	private String m_password;
	private String m_accesstoken;
	private String m_useriden;
	private String m_deviceiden;
	private String m_number;

	public Utilisateur(String pseudonyme, String password, String accesstoken, String number)
	{
		m_pseudonyme = pseudonyme;
		m_password = password;
		checkUVHC();
		m_accesstoken = accesstoken;
		getUserIden();
		getDeviceIden();
		m_number = number;
	}

	public Utilisateur(JsonObject jsonobject)
	{
		m_pseudonyme = jsonobject.getString("pseudonyme");
		m_password = jsonobject.getString("password");
		m_accesstoken = jsonobject.getString("accesstoken");
		m_useriden = jsonobject.getString("useriden");
		m_deviceiden = jsonobject.getString("deviceiden");
		m_number = jsonobject.getString("number");
		checkUVHC();
	}

	public JsonObject toJson()
	{
		JsonObject jsonobject = null;
		JsonObjectBuilder jsonobjectbuilder = Json.createObjectBuilder();
		jsonobjectbuilder.add("pseudonyme", m_pseudonyme);
		jsonobjectbuilder.add("password", m_password);
		jsonobjectbuilder.add("accesstoken", m_accesstoken);
		jsonobjectbuilder.add("useriden", m_useriden);
		jsonobjectbuilder.add("deviceiden", m_deviceiden);
		jsonobjectbuilder.add("number", m_number);
		jsonobject = jsonobjectbuilder.build();
		return jsonobject;
	}

	private void checkUVHC()
	{
		try
		{
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
			HttpsURLConnection connection = (HttpsURLConnection) new URL("https://cas.univ-valenciennes.fr/cas/login").openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept-Charset", "gzip, deflate");
			connection.setRequestProperty("Accept-Language", "fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3");
			connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
			connection.setDoOutput(true);
			String postData = "username=" + this.getM_pseudonyme() + "&password=" + this.getM_password() + "&lt=" + getLTUVHC() + "&_eventId=submit&submit=Connexion";
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.write(postData.getBytes("UTF-8"));
			wr.flush();
			wr.close();

			@SuppressWarnings("unused")
			int responseCode = connection.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine + "\n");
			}
			Pattern pattern = Pattern.compile("Connexion réussie");
			Matcher matcher = pattern.matcher(response);
			if (!matcher.find())
			{
				@SuppressWarnings("resource")
				Scanner sc = new Scanner(System.in);
				System.out.println("\nMauvais couple pseudonyme/password...\n");
				System.out.print("[UVHC] Pseudonyme: ");
				this.m_pseudonyme = sc.nextLine();
				System.out.print("[UVHC] Password: ");
				this.m_password = sc.nextLine();
				checkUVHC();
			}
			in.close();
			get("https://vtmob.univ-valenciennes.fr/esup-vtclient-up4/stylesheets/mobile/welcome.xhtml");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private String getLTUVHC() throws IOException
	{
		String lt = null;
		String source = get("https://cas.univ-valenciennes.fr/cas/login");
		Pattern p = Pattern.compile("type=\"hidden\" name=\"lt\" value=\"(.*)\"");
		Matcher m = p.matcher(source);
		m.find();
		lt = m.group(1);
		return lt;
	}

	private String get(String textURL) throws IOException
	{
		String source = null;
		HttpsURLConnection connection = (HttpsURLConnection) new URL(textURL).openConnection();
		connection.setRequestProperty("Accept-Charset", "gzip, deflate");
		connection.setRequestProperty("Accept-Language", "fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3");
		connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
		InputStream responseStream = connection.getInputStream();
		InputStreamReader responseStreamReader = new InputStreamReader(responseStream, "UTF-8");
		BufferedReader responseReader = new BufferedReader(responseStreamReader);
		for (String line; (line = responseReader.readLine()) != null;)
		{
			source += line + "\n";
		}

		return source;
	}

	private void getUserIden()
	{
		try
		{
			HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.pushbullet.com/v2/users/me").openConnection();
			connection.setRequestProperty("Accept-Charset", "gzip, deflate");
			connection.setRequestProperty("Accept-Language", "fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3");
			connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
			connection.setRequestProperty("Access-Token", m_accesstoken);
			int responseCode = connection.getResponseCode();
			if (responseCode != 200)
			{
				System.out.println("\nAccess-Token incorrect\n");
				@SuppressWarnings("resource")
				Scanner sc = new Scanner(System.in);
				System.out.print("[PushBullet] Access-Token: ");
				setM_accesstoken(sc.nextLine());
				getUserIden();
			}
			else
			{
				InputStream responseStream = connection.getInputStream();
				InputStreamReader responseStreamReader = new InputStreamReader(responseStream, "UTF-8");
				JsonReader jsonreader = Json.createReader(responseStreamReader);
				JsonObject jsonobject = jsonreader.readObject();
				setM_useriden(jsonobject.getString("iden"));
			}
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void getDeviceIden()
	{
		try
		{
			HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.pushbullet.com/v2/devices").openConnection();
			connection.setRequestProperty("Accept-Charset", "gzip, deflate");
			connection.setRequestProperty("Accept-Language", "fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3");
			connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
			connection.setRequestProperty("Access-Token", m_accesstoken);
			InputStream responseStream = connection.getInputStream();
			InputStreamReader responseStreamReader = new InputStreamReader(responseStream, "UTF-8");
			JsonReader jsonreader = Json.createReader(responseStreamReader);
			JsonObject jsonobject = jsonreader.readObject();
			JsonArray jsonarray = jsonobject.getJsonArray("devices");
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			int choix = 0;
			while (choix <= 0 || choix > jsonarray.size())
			{
				System.out.println();
				for (int i = 0; i < jsonarray.size(); i++)
				{
					JsonObject joi = jsonarray.getJsonObject(i);
					System.out.println((i + 1) + ") " + joi.getString("nickname"));
				}
				System.out.print("\nChoix: ");
				choix = sc.nextInt();
			}
			setM_deviceiden((jsonarray.getJsonObject(choix - 1).getString("iden")));
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public String getM_pseudonyme()
	{
		return m_pseudonyme;
	}

	public void setM_pseudonyme(String m_pseudonyme)
	{
		this.m_pseudonyme = m_pseudonyme;
	}

	public String getM_password()
	{
		return m_password;
	}

	public void setM_password(String m_password)
	{
		this.m_password = m_password;
	}

	public String getM_accesstoken()
	{
		return m_accesstoken;
	}

	public void setM_accesstoken(String m_accesstoken)
	{
		this.m_accesstoken = m_accesstoken;
	}

	public String getM_useriden()
	{
		return m_useriden;
	}

	public void setM_useriden(String m_useriden)
	{
		this.m_useriden = m_useriden;
	}

	public String getM_deviceiden()
	{
		return m_deviceiden;
	}

	public void setM_deviceiden(String m_deviceiden)
	{
		this.m_deviceiden = m_deviceiden;
	}

	public String getM_number()
	{
		return m_number;
	}

	public void setM_number(String m_number)
	{
		this.m_number = m_number;
	}

}
