package theleatherguy.epiandroid.Beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Infos
{
	public class Board
	{
		public class Project
		{
			public String title_module;

			public String acti_title;

			public String begin_acti;

			public String end_acti;

			public int registered;

			public String type_acti;
		}
		public List<Project>    projects;

		public class Mark
		{
			public String   title;
			public String   title_link;
			public String   note;
			public String   noteur;
		}
		public List<Mark>       notes;

		public class Event
		{
			public String   title;

			public String   module;

			public String   timeline_start;

			public String   timeline_end;

			public String   timeline_barre;

			public String   date_inscription;

			public String   salle;

			public String   intervenant;

			public String   token;

			public String   token_link;

			public String   register_link;
		}
		public List<Event>      activites;

		public class Module
		{
			public String   title;

			public String   title_link;

			public String   timeline_start;

			public String   timeline_end;

			public String   timeline_barre;

			public String   date_inscription;
		}
		public List<Module>     modules;
	}
	@SerializedName("board")
	public Board    board;

	public class Message
	{
		public String   title;
		public User     user;
		public String   content;
		public String   date;
		public String   visible;
		@SerializedName("class")
		public String   classe;
	}
	@SerializedName("history")
	public List<Message>    messages;

	public class Info
	{
		public String  login;

		public String  lastname;

		public String  firstname;

		public String  internal_email;

		public String   picture;

		public String   scolaryear;

		public String   promo;

		public String   semester;

		public String   location;

		public Integer  credits;

		public class GPA
		{
			public String   gpa;
			public String   gpa_average;
			public String   cycle;
		}
		public List<GPA>      gpa;

		public class Netsoul
		{
			public Double   active;
			public Double   idle;
			public Double   out_active;
			public Double   out_idle;
			public Double   nslog_norm;
		}
		public Netsoul  nsstat;
	}
	public Info             infos;
}
