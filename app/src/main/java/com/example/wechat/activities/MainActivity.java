package com.example.wechat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wechat.R;
import com.example.wechat.fragments.ChatsFragment;
import com.example.wechat.fragments.FriendsFragment;
import com.example.wechat.fragments.RequestsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity {

    ViewPager pager;
   private FirebaseAuth mAuth;
   private Toolbar toolbar;
   TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("We Chat");
        pager=findViewById(R.id.main_pager);
        tabLayout=findViewById(R.id.main_tab);
        final PagerAdapter pagerAdapter =new FragmentPagerAdapter(getSupportFragmentManager()){


            @Override
            public int getCount() {
                return 3;
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {

               switch (position){
                   case 0:
                       Fragment fragment=new RequestsFragment();
                       return fragment;
                   case 1:
                       Fragment fragment1=new ChatsFragment();
                       return fragment1;
                   case 2:
                       Fragment fragment2=new FriendsFragment();
                       return fragment2;
                       default:
                           return null;

               }

            }


            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {

                switch (position) {
                    case 0:
                        return "Requests";
                    case 1:
                        return "Chats";
                    case 2:
                        return "Friends";
                    default:
                        return super.getPageTitle(position);

                }

            }
        };

        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);


//        tabLayout.getTabAt(0).setText(pagerAdapter.getPageTitle(0));
  //      tabLayout.getTabAt(1).setText(pagerAdapter.getPageTitle(1));
  //      tabLayout.getTabAt(2).setText(pagerAdapter.getPageTitle(2));
    //FirebaseApp.initializeApp(getApplicationContext());
        mAuth=FirebaseAuth.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){

            logout();
        }
       // updateUI(currentUser);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.main_logout){

            mAuth.signOut();
            logout();
        }
        else if (item.getItemId()==R.id.main_setting)
        {
            Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }
    private void logout() {
        Intent intent=new Intent(MainActivity.this,StartPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
