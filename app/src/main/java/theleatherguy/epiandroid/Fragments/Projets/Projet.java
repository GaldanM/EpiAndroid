package theleatherguy.epiandroid.Fragments.Projets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Adapters.AdapterModuleExpendableListView;
import theleatherguy.epiandroid.Beans.Event;
import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

/**
 * Created by olivier.medec on 31/01/2016.
 */
public class Projet extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String[]>> listDataChild;
    private String _token;
    private Infos _infos = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_module, container, false);
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if (intent != null)
            _token = intent.getStringExtra("token");

        expListView = (ExpandableListView)inflatedView.findViewById(R.id.expandableModuleListView);
        getInfos();
        return inflatedView;
    }

    private void getModule() {
        RequestParams params = new RequestParams();
        params.put("token", _token);

        EpitechRest.get("marks", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<String[]>>();
                HashMap<String, String> header = new HashMap<String, String>();

                if (getActivity() != null) {
                    theleatherguy.epiandroid.Beans.Projet marks = new Gson().fromJson(response.toString(),
                            theleatherguy.epiandroid.Beans.Projet.class);


                    //getHeader without duplicate
                    for (theleatherguy.epiandroid.Beans.Projet.note mark : marks.notes) {
                        if (_infos.infos.scolaryear.equals(mark.scolaryear))
                            header.put(mark.titlemodule, mark.titlemodule);
                    }

                    //fill header
                    SortedSet<String> keys = new TreeSet<String>(header.keySet());
                    for (String key : keys) {
                        listDataHeader.add(key);
                    }

                    //fill project
                    Log.w("listExpendable", String.valueOf(marks.notes.size()));
                    for (String key : listDataHeader) {
                        List<String[]> ToAdd = new ArrayList<String[]>();
                        Log.w("listExpendable", "header :: " + key);
                        for (int i = 0; i < marks.notes.size(); i++) {
                            String titleModule = marks.notes.get(i).titlemodule;
                            if (key.equals(titleModule)) {
                                Log.w("listExpendable", "Adding :: " + marks.notes.get(i).title);
                                ToAdd.add(new String[]{marks.notes.get(i).title, marks.notes.get(i).final_note});
                            }
                        }
                        listDataChild.put(key, ToAdd);
                    }
                    listAdapter = new AdapterModuleExpendableListView(getActivity(), listDataHeader, listDataChild);
                    expListView.setAdapter(listAdapter);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (getActivity() != null) {
                    if (statusCode >= 400 && statusCode < 500)
                        Toast.makeText(getActivity().getApplicationContext(), "Error from dev, soz !", Toast.LENGTH_LONG).show();
                    else if (statusCode >= 500)
                        Toast.makeText(getActivity().getApplicationContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity().getApplicationContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getInfos() {
        RequestParams params = new RequestParams();
        params.put("token", _token);

        EpitechRest.get("infos", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (getActivity() != null) {
                    _infos = new Gson().fromJson(response.toString(), Infos.class);
                    Log.w("Infos", _infos.infos.location);
                    getModule();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (getActivity() != null) {
                    if (statusCode >= 400 && statusCode < 500)
                        Toast.makeText(getActivity().getApplicationContext(), "Error from dev, soz !", Toast.LENGTH_LONG).show();
                    else if (statusCode >= 500)
                        Toast.makeText(getActivity().getApplicationContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity().getApplicationContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
