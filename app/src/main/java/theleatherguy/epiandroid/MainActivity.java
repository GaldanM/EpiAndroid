package theleatherguy.epiandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import theleatherguy.epiandroid.Fragments.Home.Home;
import theleatherguy.epiandroid.Fragments.Modules.Modules;
import theleatherguy.epiandroid.Fragments.Notifs.Notifs;
import theleatherguy.epiandroid.Fragments.Planning.Planning;
import theleatherguy.epiandroid.Fragments.Trombi.Trombi;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	DrawerLayout        _drawer;
	NavigationView      _nav;

	private Integer     id;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		_drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, _drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		_drawer.setDrawerListener(toggle);
		toggle.syncState();

		this.id = 0;
		_nav = (NavigationView) findViewById(R.id.nav_view);
		_nav.getMenu().getItem(0).setChecked(true);
		_nav.setNavigationItemSelectedListener(this);
		_nav.getMenu().performIdentifierAction(R.id.nav_home, 0);
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
			if (_nav.getMenu().getItem(0).isChecked())
				super.onBackPressed();
			else
			{
				_nav.getMenu().getItem(0).setChecked(true);
				_nav.getMenu().performIdentifierAction(R.id.nav_home, 0);
			}
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
		//int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		Fragment fragment = null;
		int id = item.getItemId();

		if (this.id != id)
		{
			switch (id)
			{
				case R.id.nav_home:
					fragment = new Home();
					break;
				case R.id.nav_notifs:
					fragment = new Notifs();
					break;
				case R.id.nav_trombi:
					fragment = new Trombi();
					break;
				case R.id.nav_modules:
					fragment = new Modules();
					break;
				case R.id.nav_projects:
					break;
				case R.id.nav_planning:
					fragment = new Planning();
					break;
				case R.id.nav_login:
				{
					SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
					SharedPreferences.Editor edit = pref.edit();
					edit.remove("login");
					edit.remove("pass");
					edit.apply();
					Intent intent = new Intent(MainActivity.this, LoginActivity.class);
					finish();
					startActivity(intent);
				}
				default:
					fragment = new Home();
			}
			this.id = id;
			FragmentManager manager = getSupportFragmentManager();
			manager.beginTransaction()
					.replace(R.id.frameLayout, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.commit();

			item.setChecked(true);
			setTitle(item.getTitle());
		}
		_drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}