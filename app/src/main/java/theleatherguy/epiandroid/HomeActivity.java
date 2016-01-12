package theleatherguy.epiandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Adapters.ListProjectsAdapter;
import theleatherguy.epiandroid.Beans.Event;
import theleatherguy.epiandroid.Beans.Project;
import theleatherguy.epiandroid.EpitechAPI.EpitechRestClient;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	private LinearLayout    layoutToday;
	private ListView        listToday;
	private LinearLayout    layoutTomorrow;
	private ListView        listTomorrow;
	private LinearLayout    layoutRendus;
	private ListView        listRendus;
	private LinearLayout    layoutMarks;
	private ListView        listMarks;

	private String          _token;
	private List<Event>     _today;
	private List<Event>     _tomorrow;
	private List<Project>   _rendus;
	//private List<>   _marks;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		Intent intent = getIntent();
		if (intent != null)
			_token = intent.getStringExtra("token");

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.getMenu().getItem(0).setChecked(true);
		navigationView.setNavigationItemSelectedListener(this);

		listToday = (ListView) findViewById(R.id.listToday);
		listToday.setEmptyView(findViewById(R.id.emptyToday));
		listTomorrow = (ListView) findViewById(R.id.listTomorrow);
		listTomorrow.setEmptyView(findViewById(R.id.emptyTomorrow));
		listRendus = (ListView) findViewById(R.id.listRendus);
		listRendus.setEmptyView(findViewById(R.id.emptyRendus));
		listMarks = (ListView) findViewById(R.id.listMarks);
		listMarks.setEmptyView(findViewById(R.id.emptyMarks));

		/*layoutToday = (LinearLayout) findViewById(R.id.layoutToday);
		layoutTomorrow = (LinearLayout) findViewById(R.id.layoutTomorrow);
		layoutRendus = (LinearLayout) findViewById(R.id.layoutRendus);
		layoutMarks = (LinearLayout) findViewById(R.id.layoutMarks);*/

		getProjects();

		//listToday.setAdapter(new ListEventsAdapter(HomeActivity.this, _today));
		//listTomorrow.setAdapter(new ListEventsAdapter(HomeActivity.this, _tomorrow));
		//listMarks.setAdapter(new ListActivities(HomeActivity.this, _marks));

		Toast.makeText(getApplicationContext(), _token, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		} else
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_home)
		{
			// Handle the camera action
		} else if (id == R.id.nav_alerts)
		{

		} else if (id == R.id.nav_trombi)
		{

		} else if (id == R.id.nav_modules)
		{

		} else if (id == R.id.nav_projects)
		{

		} else if (id == R.id.nav_marks)
		{

		} else if (id == R.id.nav_planning)
		{

		} else if (id == R.id.nav_events)
		{

		} else if (id == R.id.nav_login)
		{

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private void getProjects()
	{
		RequestParams params = new RequestParams();
		params.put("token", _token);

		EpitechRestClient.get("projects", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response)
			{
				List<Project> projs = new Gson().fromJson(response.toString(), new TypeToken<List<Project>>(){}.getType());
				_rendus = new ArrayList<>();
				for (Project proj:projs)
				{
					if (proj.isRegistered == 1
							&& (proj.type.equals("Mini-Projets") || proj.type.equals("Projet")))
						_rendus.add(proj);
				}
				listRendus.setAdapter(new ListProjectsAdapter(HomeActivity.this, _rendus));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{
				if (statusCode >= 400 && statusCode < 500)
					Toast.makeText(getApplicationContext(), "Error from dev, soz !", Toast.LENGTH_LONG).show();
				else if (statusCode >= 500)
					Toast.makeText(getApplicationContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getApplicationContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
			}

		});
	}
}