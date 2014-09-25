/**
 * 
 */
package com.liubo.birthdaynotice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 *<一句话功能简述>
 *<功能详细描述>
 *@author liubo
 *@version
*/
public class MyListAdapter extends BaseAdapter
{
    
    private List<String> data;
    private Context context;
    LayoutInflater inflater;
    
    /**
     * 
     */
    public MyListAdapter(List<String> data,Context context)
    {
        this.data = data;
        this.context = context;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return data.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View layout = null;
        if(null == convertView)
        {
            View v = inflater.inflate(R.layout.list_item_layout, null);
            layout = v;
        }else{
            layout = convertView;
        }
        TextView name = (TextView)layout.findViewById(R.id.name);
        TextView day = (TextView)layout.findViewById(R.id.day);
        String content = data.get(position);
        Button delete = (Button)layout.findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                String info = data.get(position);
                Tool.deleteInfo(info, context);
                String infStr = ShPrefUtils.getInstance(context).get(UserValue.infoKey, "");
                List<String> datas = new ArrayList<String>();
                if(!Tool.isTrimEmpty(infStr))
                {
                    datas = Arrays.asList(infStr.split(";"));
                }
                data = datas;
                notifyDataSetChanged();
            }
        });
        name.setText(content.split(":")[0]);
        day.setText(content.split(":")[1]);
        return layout;
    }

    

}
