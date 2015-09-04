package komalis.scheduletosms;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

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

	public Utilisateur(String pseudonyme, String password, String accesstoken)
	{
		m_pseudonyme = pseudonyme;
		m_password = password;
		m_accesstoken = accesstoken;
		getUserIden();
		getDeviceIden();
	}

	public Utilisateur(JsonObject jsonobject)
	{
		m_pseudonyme = jsonobject.getString("pseudonyme");
		m_password = jsonobject.getString("password");
		m_accesstoken = jsonobject.getString("accesstoken");
		m_useriden = jsonobject.getString("useriden");
		m_deviceiden = jsonobject.getString("deviceiden");
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
		jsonobject = jsonobjectbuilder.build();
		return jsonobject;
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
			InputStream responseStream = connection.getInputStream();
			InputStreamReader responseStreamReader = new InputStreamReader(responseStream, "UTF-8");
			JsonReader jsonreader = Json.createReader(responseStreamReader);
			JsonObject jsonobject = jsonreader.readObject();
			setM_useriden(jsonobject.getString("iden"));
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
}
