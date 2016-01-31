package theleatherguy.epiandroid.Fragments.Modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Adapters.ListAllModulesAdapter;
import theleatherguy.epiandroid.Beans.AllModule;
import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

/**
 * Created by olivier.medec on 28/01/2016.
 */
public class AllModules extends Fragment {
    private String _token = null;
    private ListView listView = null;
    private Infos _infos = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_list_allmodules, container, false);
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if (intent != null)
            _token = intent.getStringExtra("token");

        listView = (ListView)inflatedView.findViewById(R.id.listViewAllModules);
        getInfos();
        return inflatedView;
    }

    public void getAllModules() {
        RequestParams params = new RequestParams();
        params.put("token", _token);
        params.put("scolaryear", _infos.infos.scolaryear);
        params.put("location", _infos.infos.location);
        params.put("course", _infos.infos.course_code);

        EpitechRest.get("allmodules", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (getActivity() != null) {
                    AllModule all = new Gson().fromJson(response.toString(), AllModule.class);
                    List<AllModule.Item> listModule;
                    listModule = new ArrayList<>();
                    for (int i = 0; i < all.items.size(); i++) {
                        if (all.items.get(i).open == 1 || all.items.get(i).status == "notregistered") {
                            listModule.add(all.items.get(i));
                        }
                    }
                    ListAllModulesAdapter adapter = new ListAllModulesAdapter(listModule, getActivity());
                    listView.setAdapter(adapter);
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
                    getAllModules();
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
