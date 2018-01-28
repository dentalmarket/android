package market.dental.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.dental.adapter.ViewPagerAdapter;
import market.dental.model.Category;
import market.dental.util.Resource;
import market.dental.util.Result;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    MainFragment.OnFragmentInteractionListener{

    private Menu menu;
    private Menu navigationViewMenu;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization
        RequestQueue rq = Volley.newRequestQueue(this);




        // *****************************************************************************************
        //                              VIEW OPERATIONS
        // *****************************************************************************************
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationViewMenu = navigationView.getMenu();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sp_dental_market), Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();


        // *****************************************************************************************
        //                          AJAX - GET CATEGORIES
        // *****************************************************************************************
        StringRequest request = new StringRequest(Request.Method.POST,
                Resource.ajax_get_categories, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {
                    // TODO: result objesinin kontrolü YAPILACAK
                    JSONObject response = new JSONObject(responseString);
                    JSONObject content = response.getJSONObject("content");

                    if(content.has("subCategories")) {
                        List<Category> categoryList = Category.CategoryList(content.getJSONArray("subCategories"));
                        for(Category category : categoryList){
                            MenuItem menuItem = navigationViewMenu.add(R.id.activity_main_drawer_main_group, category.getId(), Menu.NONE,category.getName() );
                            menuItem.setIcon(R.drawable.ic_menu_black_18dp);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"MainActivity >> JSONException >> 120");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result.LOG_TAG_INFO.getResultText(),"MainActivity >> ERROR ON GET DATA >> 121");
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        rq.add(request);


        // *****************************************************************************************
        //                          FRAGMENT
        // *****************************************************************************************
        Fragment fragment = new MainFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main , fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.right_menu_login:
                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.right_menu_profile:
                intent = new Intent(this,ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.right_menu_logout:
                Log.i(Result.LOG_TAG_INFO.getResultText() , "Settings >> logout");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        Bundle bundle = new Bundle();
        bundle.putInt(Resource.KEY_CATEGORY_ID, id);
        Intent intent = new Intent(this,ProductListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        setSettingMenuItem();
    }


    public void setSettingMenuItem(){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sp_dental_market), Context.MODE_PRIVATE);
        int userSessionID = sharedPreferences.getInt(getString(R.string.sp_user_id) , -1);

        if(menu!=null){
            if(userSessionID != -1){
                menu.findItem(R.id.right_menu_login).setVisible(false);
                menu.findItem(R.id.right_menu_profile).setVisible(true);
                menu.findItem(R.id.right_menu_logout).setVisible(true);
            }else{
                menu.findItem(R.id.right_menu_login).setVisible(true);
                menu.findItem(R.id.right_menu_profile).setVisible(false);
                menu.findItem(R.id.right_menu_logout).setVisible(false);
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.i(Result.LOG_TAG_INFO.getResultText(), "MainActivity >> onFragmentInteraction");
    }
}
