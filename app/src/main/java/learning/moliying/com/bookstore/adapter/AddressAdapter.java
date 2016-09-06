package learning.moliying.com.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.utils.ViewHolder;
import learning.moliying.com.bookstore.vo.Address;

/**
 * Created by Administrator on 2016/8/19.
 */
public class AddressAdapter extends BaseAdapter {
    private Context context;
    private List<Address> addresses;

    public AddressAdapter(Context context, List<Address> addresses) {
        this.context = context;
        this.addresses = addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public Object getItem(int position) {
        return addresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address,null);
        }
        TextView tv_name= (TextView) ViewHolder.getView(convertView,R.id.textView4_username);
        TextView tv_phone = (TextView) ViewHolder.getView(convertView,R.id.textView4_phone);
        TextView tv_default = (TextView) ViewHolder.getView(convertView,R.id.textView4_default);
        TextView tv_address = (TextView) ViewHolder.getView(convertView,R.id.textView4_address);
        Address address = addresses.get(position);
        tv_name.setText(address.getUsername());
        tv_phone.setText(address.getPhoneNumber());
        tv_address.setText(address.getAddress());
        if(address.getIsDefault()) tv_default.setVisibility(View.VISIBLE);
        return convertView;
    }
}
