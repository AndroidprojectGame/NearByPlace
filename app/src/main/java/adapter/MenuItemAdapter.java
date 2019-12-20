package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.lgsocietyhub.com.nearbyplace.FindPlaceActivity;
import app.lgsocietyhub.com.nearbyplace.R;
import model.MenuModelClass;

/**
 * Created by mappsupport on 08-05-2018.
 */

public class MenuItemAdapter extends BaseAdapter
{
    private Context mContext;
    private final String[] itemText;
    private final int[] itemImage;


    public MenuItemAdapter(Context c,String[] itemText,int[] itemImage ) {
        mContext = c;
        this.itemImage = itemImage;
        this.itemText = itemText;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return itemText.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.menu_gridview_item_layout, null);
            TextView textView = (TextView) grid.findViewById(R.id.tv_menuText);
            ImageView imageView = (ImageView)grid.findViewById(R.id.iv_menuItem);
            CardView cardView=(CardView)grid.findViewById(R.id.card_view);
            textView.setText(itemText[position]);
            imageView.setImageResource(itemImage[position]);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Item"+itemText[position], Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}
