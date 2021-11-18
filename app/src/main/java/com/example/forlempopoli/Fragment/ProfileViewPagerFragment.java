package com.example.forlempopoli.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.forlempopoli.R;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class ProfileViewPagerFragment extends Fragment {
    ViewPagerAdapter profileadapter;
    int visiblePosition;

    private SparseArray<Fragment> registeredFragments;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_view_pager, container, false);
        ViewPager profile_viewpager = view.findViewById(R.id.profile_viewpager);
        setupViewPager(profile_viewpager);
        TabLayout profile_tabs = view.findViewById(R.id.profile_tabs);
        profile_tabs.setupWithViewPager(profile_viewpager);
        registeredFragments = new SparseArray<>();
        makeActionOverflowMenuShown();


        // Attach the page change listener inside the activity
        profile_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Fragment fragment = profileadapter.getRegisteredFragment(position);
                visiblePosition = position;
                if (fragment instanceof BankandbillingFragment) {
                    ((BankandbillingFragment) fragment).showBankBillingdetails();
                } else if (fragment instanceof DeliveryAddressFragment) {
                    ((DeliveryAddressFragment) fragment).deliveryaddressDetails();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return view;
    }

    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(getActivity());
            @SuppressLint("SoonBlockedPrivateApi") Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            Log.d("", e.getLocalizedMessage());
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        profileadapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager());
        profileadapter.addFrag(new BankandbillingFragment(), "General Profile");
        profileadapter.addFrag(new DeliveryAddressFragment(), "Address Details");
        viewPager.setAdapter(profileadapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();// close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            // Causes adapter to reload all Fragments when
            // notifyDataSetChanged is called
            notifyDataSetChanged();
            return POSITION_NONE;
        }
    }
}