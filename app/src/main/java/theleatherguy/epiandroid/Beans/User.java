package theleatherguy.epiandroid.Beans;

import java.util.List;

public class User
{
	public String   login;

	public String   picture;

	public String   firstname;

	public String lastname;

	public String internal_email;

	public class UserInfo
	{
		public class Email
		{
			public String value;
		}
		public Email email;

		public class Phone
		{
			public String   value;
		}
		public Phone    telephone;

		public class Birthday
		{
			public String   value;
		}
		public Birthday birthday;
	}
	public UserInfo userinfo;

	public Integer promo;

	public String course_code;

	public Integer semester;

	public Integer studentyear;

	public String location;

	public Integer  credits;

	public List<Event> events;

	public List<Infos.Info.GPA> gpa;

	public List<Infos.Info.GPA> averageGPA;

	public class Spice
	{
		public String available_spice;
		public Integer consumed_spice;
	}
	public Spice spice;

	public Infos.Info.Netsoul nsstat;

	public String url;
}
