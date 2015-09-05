package komalis.scheduletosms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.net.ssl.HttpsURLConnection;

public class SMS
{
	private JsonObject m_smsinfo;
	private Utilisateur m_utilisateur;
	private String m_message;

	public SMS(Utilisateur utilisateur, String message)
	{
		m_utilisateur = utilisateur;
		m_message = message;
		buildJSON();
	}

	private void buildJSON()
	{
		JsonObject jo = null;
		JsonObjectBuilder job = Json.createObjectBuilder();
		JsonObjectBuilder job2 = Json.createObjectBuilder();
		job2.add("type", "messaging_extension_reply");
		job2.add("package_name", "com.pushbullet.android");
		job2.add("source_user_iden", m_utilisateur.getM_useriden());
		job2.add("target_device_iden", m_utilisateur.getM_deviceiden());
		job2.add("conversation_iden", m_utilisateur.getM_number());
		job2.add("message", this.m_message);
		job.add("type", "push");
		job.add("push", job2);
		jo = job.build();
		m_smsinfo = jo;
	}

	public void sendSMS()
	{
		post("https://api.pushbullet.com/v2/ephemerals", this.m_smsinfo.toString());
	}

	private void post(String textURL, String textPost)
	{
		try
		{
			HttpsURLConnection connection = (HttpsURLConnection) new URL(textURL).openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept-Charset", "gzip, deflate");
			connection.setRequestProperty("Accept-Language", "fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3");
			connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
			connection.setRequestProperty("Access-Token", m_utilisateur.getM_accesstoken());

			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.write(textPost.getBytes("UTF-8"));
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
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public JsonObject getM_smsinfo()
	{
		return m_smsinfo;
	}

	public void setM_smsinfo(JsonObject m_smsinfo)
	{
		this.m_smsinfo = m_smsinfo;
	}

	public Utilisateur getM_utilisateur()
	{
		return m_utilisateur;
	}

	public void setM_utilisateur(Utilisateur m_utilisateur)
	{
		this.m_utilisateur = m_utilisateur;
	}

	public String getM_message()
	{
		return m_message;
	}

	public void setM_message(String m_message)
	{
		this.m_message = m_message;
	}

}
