package com.xy.lifemanage.view.myview;

/**
 * Created by nemo on 2016/5/11 0011.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xy.lifemanage.R;

import java.util.ArrayList;


/**
 * Author: msdx (645079761@qq.com)
 * Time: 14-9-2 上午8:56
 */
public class UserMenu extends PopMenu {
    public UserMenu(Context context) {
        super(context);
    }

    @Override
    protected ListView findListView(View view) {
        return (ListView) view.findViewById(R.id.menu_listview);
    }

    @Override
    protected View onCreateView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_user, null);
        return view;
    }

    @Override
    protected ArrayAdapter<Item> onCreateAdapter(Context context, ArrayList<Item> items) {
        return new ArrayAdapter<Item>(context, R.layout.menu_item, items);
    }
}
