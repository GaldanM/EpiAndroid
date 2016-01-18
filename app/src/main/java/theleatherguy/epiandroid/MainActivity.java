package theleatherguy.epiandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.Fragments.Home.Home;
import theleatherguy.epiandroid.Fragments.Home.HomeToday;
import theleatherguy.epiandroid.Fragments.Modules.Modules;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	FragmentTabHost     _tabHost;
	DrawerLayout        _drawer;
	NavigationView      _nav;

	private View        headerNav;
	private TextView    headerLogin;
	private ImageView   headerPicture;
	private TextView    headerGpa;
	private TextView    headerCity;
	private TextView    headerCredit;
	private TextView    headerPromo;

	private String      _token;
	private String      _login;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		if (intent != null)
		{
			_token = intent.getStringExtra("token");
			_login = intent.getStringExtra("login");
		}

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		_drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, _drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		_drawer.setDrawerListener(toggle);
		toggle.syncState();

		_nav = (NavigationView) findViewById(R.id.nav_view);
		_nav.getMenu().getItem(0).setChecked(true);
		_nav.setNavigationItemSelectedListener(this);
		_nav.getMenu().performIdentifierAction(R.id.nav_home, 0);

		headerNav = _nav.getHeaderView(0);
		headerPicture = (ImageView) headerNav.findViewById(R.id.intraPic);
		headerCredit = (TextView) headerNav.findViewById(R.id.txtCredit);
		headerPromo = (TextView) headerNav.findViewById(R.id.txtPromo);
		headerLogin = (TextView) headerNav.findViewById(R.id.txtLogin);
		headerGpa = (TextView) headerNav.findViewById(R.id.txtGpa);
		headerCity = (TextView) headerNav.findViewById(R.id.txtCity);

		getUserInfo();
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		Fragment fragment = null;
		int id = item.getItemId();

		switch (id)
		{
			case R.id.nav_home:
				fragment = new Home();
				break;
			case R.id.nav_alerts:
				break;
			case R.id.nav_trombi:
				break;
			case R.id.nav_modules:
				break;
			case R.id.nav_projects:
				break;
			case R.id.nav_marks:
				break;
			case R.id.nav_planning:
				fragment = new Modules();
				break;
			case R.id.nav_events:
				break;
			case R.id.nav_login:
				break;
			default:
				fragment = new Home();
		}

		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction()
				.replace(R.id.frameLayout, fragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();

		item.setChecked(true);
		setTitle(item.getTitle());
		_drawer.closeDrawer(GravityCompat.START);

		return true;
	}

	private void getUserInfo()
	{
		RequestParams params = new RequestParams();
		params.put("token", _token);
		params.put("user", _login);

		EpitechRest.get("user", params, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				Infos.Info info = new Gson().fromJson(response.toString(), Infos.Info.class);
				Picasso.with(getApplicationContext()).load(info.picture).into(headerPicture);
				headerCredit.setText(Integer.toString(info.credits));
				headerPromo.setText(info.promo);
				headerLogin.setText(info.login);
				headerGpa.setText(info.gpa.get(0).gpa);
				headerCity.setText(info.location);
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