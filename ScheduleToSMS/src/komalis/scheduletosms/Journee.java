package komalis.scheduletosms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class Journee
{
	private ArrayList<Cours> m_cours;
	private Calendar m_date;

	public Journee(Calendar date)
	{
		try
		{
			m_date = date;
			refreshJournee();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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

	public static void post(String textURL, String textPost) throws IOException
	{
		HttpsURLConnection connection = (HttpsURLConnection) new URL(textURL).openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Accept-Charset", "gzip, deflate");
		connection.setRequestProperty("Accept-Language", "fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3");
		connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");

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

	private void postDay(Calendar day) throws IOException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String postData = "formCal%3Adate=" + sdf.format(day.getTime()).replaceAll("/", "%2F") + "&org.apache.myfaces.trinidad.faces.FORM=formCal&_noJavaScript=false&javax.faces.ViewState=%211&source=formCal%3AhiddenLink";
		String postData1 = "org.apache.myfaces.trinidad.faces.FORM=redirectForm&_noJavaScript=false&javax.faces.ViewState=%211&source=redirectForm%3AgoCal";
		post("https://vtmob.univ-valenciennes.fr/esup-vtclient-up4/stylesheets/mobile/welcome.xhtml", postData1);
		get("https://vtmob.univ-valenciennes.fr/esup-vtclient-up4/stylesheets/mobile/calendar.xhtml");
		post("https://vtmob.univ-valenciennes.fr/esup-vtclient-up4/stylesheets/mobile/calendar.xhtml", postData);
	}

	public void refreshJournee() throws IOException
	{
		postDay(this.m_date);
		ArrayList<Cours> cours = new ArrayList<>();
		String source = get("https://vtmob.univ-valenciennes.fr/esup-vtclient-up4/stylesheets/mobile/welcome.xhtml");
		Pattern p = Pattern.compile("<li data-role=\"list-divider\"[\n]*.*>(.*)<span");
		Pattern p2 = Pattern.compile("ui-li-count\">(.*)</span>");
		Pattern p3 = Pattern.compile("(?:&#9;)*.*<h3>\n(?:&#9;)*(.*)");
		Pattern p4 = Pattern.compile("(?:&#9;)*.*<p><strong>\n(?:&#9;)*(?: )*(.*)");
		Pattern p5 = Pattern.compile("([0-9]{1,2}:[0-9]{2}-[0-9]{1,2}:[0-9]{2})");
		Matcher m = p.matcher(source);
		String type = null;
		String id = null;
		String horaire = null;
		String prof = null;
		String salle = null;
		while (m.find())
		{
			id = m.group(1);
			m.usePattern(p2);
			if (m.find())
			{
				type = m.group(1);
				m.usePattern(p3);
				if (m.find())
				{
					salle = m.group(1);
					m.usePattern(p4);
					if (m.find())
					{
						prof = m.group(1);
						m.usePattern(p5);
						if (m.find())
						{
							horaire = m.group(1);
							cours.add(new Cours(type, id, horaire, prof, salle, this.m_date));
						}
					}
				}
			}
			m.usePattern(p);
		}
		this.m_cours = cours;
	}

	@Override
	public String toString()
	{
		String source = "";
		for (Cours cours : m_cours)
		{
			source += cours + "\n\n";
		}
		if(m_cours.size() == 0)
		{
			source = "Pas de cours aujourd'hui!";
		}
		else
		{
			source = source.substring(0, source.length() - 2);
		}
		return source;
	}

	public ArrayList<Cours> getM_cours()
	{
		return m_cours;
	}

	public void setM_cours(ArrayList<Cours> m_cours)
	{
		this.m_cours = m_cours;
	}

	public Calendar getM_date()
	{
		return m_date;
	}

	public void setM_date(Calendar m_date)
	{
		this.m_date = m_date;
	}
}
