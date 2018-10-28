package om_mp3.mega.com.onlinemusic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import om_mp3.mega.com.onlinemusic.fragment.FragmentController;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<FragmentController> controllerList = new ArrayList<>();


    public void setControllerList(@NonNull List<FragmentController> controllerList) {
        this.controllerList.clear();
        this.controllerList.addAll(controllerList);
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {

        return controllerList.get(position);
    }

    @Override
    public int getCount() {
        return controllerList.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return controllerList.get(position).getTitle();
    }

    public ArrayList<FragmentController> getList() {
        return controllerList;
    }
}
