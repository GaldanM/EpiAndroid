package theleatherguy.epiandroid.Beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import theleatherguy.epiandroid.Beans.Event;
import theleatherguy.epiandroid.Beans.Info;
import theleatherguy.epiandroid.Beans.Mark;
import theleatherguy.epiandroid.Beans.Message;
import theleatherguy.epiandroid.Beans.Module;
import theleatherguy.epiandroid.Beans.Project;

public class Infos
{
	public List<Project>    projects;

	public List<Mark>       notes;

	public List<Event>      activites;

	public List<Module>     modules;

	@SerializedName("history")
	public List<Message>    messages;

	public Info             infos;
}
