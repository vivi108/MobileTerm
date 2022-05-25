package com.example.mobileterm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandAdapter extends BaseExpandableListAdapter {

    private Context context;
    private int groupLayout = 0;
    private int chlidLayout = 0;
    private ArrayList<myGroup> DataList;
    private LayoutInflater myinf = null;

    public ExpandAdapter(Context context, int groupLay, int chlidLay, ArrayList<myGroup> DataList) {
        this.DataList = DataList;
        this.groupLayout = groupLay;
        this.chlidLayout = chlidLay;
        this.context = context;
        this.myinf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = myinf.inflate(this.groupLayout, parent, false);
        }
        TextView groupName = (TextView) convertView.findViewById(R.id.expand_parent_name_tv);
        groupName.setText(DataList.get(groupPosition).groupName);
        return convertView;
    }


    @Override

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
        convertView = myinf.inflate(this.chlidLayout, parent, false);}
         TextView childName = (TextView)convertView.findViewById(R.id.expand_child_title_tv);
         TextView childId = (TextView)convertView.findViewById(R.id.expand_child_did);

         childName.setText(DataList.get(groupPosition).child.get(childPosition));
         childId.setText(DataList.get(groupPosition).child.get(childPosition));
        childId.setVisibility(View.GONE);
         return convertView;}

        @Override
        public boolean hasStableIds ()
        {
            return true;
        }
        @Override
        public boolean isChildSelectable ( int groupPosition, int childPosition)
        {
            return true;
        }
        @Override
        public Object getChild ( int groupPosition, int childPosition){


        return DataList.get(groupPosition).child.get(childPosition);
     }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return DataList.get(groupPosition).child.size();
    }

    @Override
    public myGroup getGroup(int groupPosition) {

    return DataList.get(groupPosition);}

    @Override
    public int getGroupCount() {
        return DataList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


}
