package komalis.scheduletosms;

import java.util.Calendar;

public class Cours
{
	private String m_type;
	private String m_id;
	private Calendar m_start;
	private Calendar m_end;
	private String m_horaire;
	private String m_prof;
	private String m_salle;

	public Cours(String type, String id, String horaire, String prof, String salle, Calendar day)
	{
		m_type = type;
		m_id = id;
		m_start = (Calendar) day.clone();
		m_start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaire.split("-")[0].split(":")[0]));
		m_start.set(Calendar.MINUTE, Integer.parseInt(horaire.split("-")[0].split(":")[1]));
		m_start.set(Calendar.SECOND, 0);
		m_end = (Calendar) day.clone();
		m_end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaire.split("-")[1].split(":")[0]));
		m_end.set(Calendar.MINUTE, Integer.parseInt(horaire.split("-")[1].split(":")[1]));
		m_end.set(Calendar.SECOND, 0); 
		m_horaire = horaire;
		m_prof = prof;
		m_salle = salle;
	}

	public String getM_type()
	{
		return m_type;
	}

	public void setM_type(String m_type)
	{
		this.m_type = m_type;
	}

	public String getM_id()
	{
		return m_id;
	}

	public void setM_id(String m_id)
	{
		this.m_id = m_id;
	}

	public Calendar getM_start()
	{
		return m_start;
	}

	public void setM_start(Calendar m_start)
	{
		this.m_start = m_start;
	}

	public Calendar getM_end()
	{
		return m_end;
	}

	public void setM_end(Calendar m_end)
	{
		this.m_end = m_end;
	}

	public String getM_horaire()
	{
		return m_horaire;
	}

	public void setM_horaire(String m_horaire)
	{
		this.m_horaire = m_horaire;
	}

	public String getM_prof()
	{
		return m_prof;
	}

	public void setM_prof(String m_prof)
	{
		this.m_prof = m_prof;
	}

	public String getM_salle()
	{
		return m_salle;
	}

	public void setM_salle(String m_salle)
	{
		this.m_salle = m_salle;
	}

}
