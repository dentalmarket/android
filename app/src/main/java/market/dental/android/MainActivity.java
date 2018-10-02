package market.dental.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import market.dental.model.Category;
import market.dental.util.Resource;
import market.dental.util.Result;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    MainFragment.OnFragmentInteractionListener,
                    ProductListFragment.OnFragmentInteractionListener{

    private Menu menu;
    private Menu navigationViewMenu;
    private NavigationView navigationView;
    private AlertDialog progressDialog;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private Context context;
    private HashSet<Target> targetList = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization
        context = this;
        requestQueue = Volley.newRequestQueue(this);

        // *****************************************************************************************
        //                              VIEW OPERATIONS
        // *****************************************************************************************

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationViewMenu = navigationView.getMenu();

        AlertDialog.Builder progressDialogBuilder = new AlertDialog.Builder(this);
        progressDialogBuilder.setCancelable(false);
        progressDialogBuilder.setView(getLayoutInflater().inflate(R.layout.dialog_progressbar,null));
        progressDialog = progressDialogBuilder.create();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {

        // Eğer notification ile categoryId değeri geldi ise ilgili category ürünler listelenir
        Bundle bundle = getIntent().getExtras();
        try{
            if(bundle.containsKey(Resource.KEY_MESSAGE_CATEGORY_ID)){
                int categoryId = Integer.parseInt(bundle.getString(Resource.KEY_MESSAGE_CATEGORY_ID));
                getIntent().removeExtra(Resource.KEY_MESSAGE_CATEGORY_ID);
                getCategoryList(categoryId);
            }else{
                getCategoryList(-1);
            }

        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> Bundle Exception");
            getCategoryList(-1);
        }

        super.onResume();
    }

    @Override
    protected void onStop() {
        if (requestQueue != null) {
            requestQueue.cancelAll(this.getClass().getName());
        }
        super.onStop();
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
            case R.id.right_menu_messages:
                intent = new Intent(this,ConversationListActivity.class);
                startActivity(intent);
                break;
            case R.id.right_menu_recent:
                Bundle bundle = new Bundle();
                bundle.putBoolean(Resource.KEY_GET_RECENT_PRODUCTS, true);
                bundle.putString(Resource.KEY_FRAGMENT_TITLE, getString(R.string.right_menu_recent_products));

                intent = new Intent(this,ProductListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.right_menu_logout:
                userLogout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        getCategoryList(item.getItemId());
        return true;
    }

    public void getCategoryList(final int categoryId){

        // *****************************************************************************************
        //                          AJAX - GET SUBCATEGORIES
        // *****************************************************************************************
        stringRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_categories, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {

                    JSONObject response = new JSONObject(responseString);
                    if(Result.SUCCESS.checkResult(new Result(response))){
                        JSONObject content = response.getJSONObject("content");
                        if(content.has("subCategories")) {
                            List<Category> categoryList = Category.CategoryList(content.getJSONArray("subCategories"));
                            if(categoryList.size() > 0){

                                // Menü temizlenir
                                navigationViewMenu.clear();
                                targetList.clear();

                                // Üst kategory için menu gerekiyor ise eklenir
                                if(categoryId!=-1){
                                    MenuItem menuItem = navigationViewMenu.add(R.id.activity_main_drawer_main_group, -1, Menu.NONE, "Üst Kategori" );
                                    menuItem.setIcon(R.drawable.menu_item_arrow_up_black_24dp);
                                }

                                // Kategori menüsü eklenir
                                for(final Category category : categoryList){
                                    final MenuItem menuItem = navigationViewMenu.add(R.id.activity_main_drawer_main_group, category.getId(), Menu.NONE,category.getName());
                                    final Target target = new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                                            BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                                            // mBitmapDrawable.setBounds(0,0,24,24);
                                            menuItem.setIcon(mBitmapDrawable);
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable drawable) {
                                            menuItem.setIcon(R.drawable.ic_menu_black_18dp);
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable drawable) {
                                            menuItem.setIcon(R.drawable.ic_menu_black_18dp);
                                        }
                                    };

                                    // GarbageCollector yüzünden atılmasın diye liste içerisine yerleştirildi
                                    targetList.add(target);

                                    if(category.getIcon()!=null && category.getIcon().contains("http")){
                                        Picasso.with(context)
                                                .load(category.getIcon().replaceFirst("http" , "https"))
                                                //.memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                                                .into(target);
                                    }else{
                                        Picasso.with(context)
                                                .load("https://dental.market/assets/images/categories/1506941351.png")
                                                //.memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                                                .into(target);
                                    }

                                }
                            }
                        }


                        Bundle bundle = new Bundle();
                        bundle.putInt(Resource.KEY_CATEGORY_ID, categoryId);

                        Fragment fragment = ( (categoryId!=-1) ?
                                ProductListFragment.newInstance() : MainFragment.newInstance() );
                        fragment.setArguments(bundle);

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_main , fragment)
                                .commitAllowingStateLoss();

                    }else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                        redirectLoginActivity();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                    Crashlytics.log(Log.ERROR , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_categories + " >> Exception");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(Result. LOG_TAG_INFO.getResultText(),"MainActivity >> ERROR ON GET DATA >> 121");
                Toast.makeText(context, getString(R.string.unexpected_network_error) , Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                params.put("parentId", String.valueOf(categoryId));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        stringRequest.setTag(this.getClass().getName());
        requestQueue.add(stringRequest);
    }


    /**
     * User Logout
     *
     * Kullanıcı logout olması durumunda API TOKEN değeri update edilir. Default API TOKEN set edilir
     * Kullanıcının sessionId değeri silinir
     * Sayfa sağ üst köşesindeki menu update edilir
     */
    public void userLogout(){

        progressDialog.show();
        // *****************************************************************************************
        //                          AJAX - LOGOUT
        // *****************************************************************************************
        stringRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_logout, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {

                try {

                    JSONObject response = new JSONObject(responseString);
                    if(Result.SUCCESS.checkResult(new Result(response))){

                        // remove API TOKEN & sessionId from shared Preference
                        Resource.setDefaultAPITOKEN();
                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sp_dental_market), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(getString(R.string.sp_user_id));
                        editor.commit();

                        // redirect to login page
                        Intent intent = new Intent(context,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(Result.LOG_TAG_INFO.getResultText(),"Exception");
                } finally {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                redirectLoginActivity();
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
        stringRequest.setTag(this.getClass().getName());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.i(Result.LOG_TAG_INFO.getResultText(), "MainActivity >> onFragmentInteraction");
    }
}
