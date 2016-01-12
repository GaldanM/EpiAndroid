package theleatherguy.epiandroid.Beans;

import com.google.gson.annotations.SerializedName;

public class Project
{
	@SerializedName("title_module")
	public String module;

	@SerializedName("acti_title")
	public String project;

	@SerializedName("begin_acti")
	public String begin;

	@SerializedName("end_acti")
	public String end;

	@SerializedName("registered")
	public int isRegistered;

	@SerializedName("type_acti")
	public String type;
}
