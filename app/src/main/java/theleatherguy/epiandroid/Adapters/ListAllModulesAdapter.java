package theleatherguy.epiandroid.Adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import junit.framework.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import theleatherguy.epiandroid.Beans.AllModule;
import theleatherguy.epiandroid.Beans.Infos;
import theleatherguy.epiandroid.EpitechAPI.EpitechRest;
import theleatherguy.epiandroid.R;

/**
 * Created by olivier.medec on 30/01/2016.
 */
public class ListAllModulesAdapter extends BaseAdapter {
    private List<AllModule.Item> _modules;
    private LayoutInflater mInflater;
    private Context mContext;
    private String _token;

    public ListAllModulesAdapter(List<AllModule.Item> modules, Context context) {
        _modules = modules;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return _modules.size();
    }

    @Override
    public Object getItem(int position) {
        return _modules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;
        TextView moduleName;

        layoutItem = (LinearLayout)mInflater.inflate(R.layout.items_all_modules_list, parent, false);
        moduleName = (TextView)layoutItem.findViewById(R.id.moduleName);

        Button btnSuscribe = (Button)layoutItem.findViewById(R.id.btnSubscription);
        btnSuscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Suscribe(position);
            }
        });

        moduleName.setText(_modules.get(position).title);
        return layoutItem;
    }

    private void Suscribe(int position) {
        Log.w("listModule", "you clicked on " + _modules.get(position).title);
        RequestParams params = new RequestParams();
        params.put("token", _token);
        params.put("scolaryear", _modules.get(position).scolaryear);
        params.put("codemodule", _modules.get(position).code);
        params.put("codeinstance", _modules.get(position).codeinstance);

        EpitechRest.post("module", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (mContext != null) {
                    try {
                        Log.w("listModule", response.get("login").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (mContext != null) {
                    if (statusCode >= 400 && statusCode < 500)
                        Toast.makeText(mContext.getApplicationContext(), "Error from dev, soz !", Toast.LENGTH_LONG).show();
                    else if (statusCode >= 500)
                        Toast.makeText(mContext.getApplicationContext(), "Server downn, try again later", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(mContext.getApplicationContext(), Integer.toString(statusCode), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
