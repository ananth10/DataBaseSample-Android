package com.ananth.databasesample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView mList;
    DBHelper myDb;
    ArrayList<HashMap<String, String>> mContactList = new ArrayList<>();
    private ContactAdapter mAdapter;
    private LinearLayout mNoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mList = (ListView) findViewById(R.id.list);
        mNoResult = (LinearLayout) findViewById(R.id.no_result_lay);
        try {
            mContactList.clear();
            myDb = new DBHelper(this);
            mContactList = myDb.getAllContacts();
            Log.i("list", "numofrows" + myDb.getAllContacts());
            if (mContactList.size() > 0) {
                mList.setVisibility(View.VISIBLE);
                mNoResult.setVisibility(View.GONE);
                mAdapter = new ContactAdapter(ContactListActivity.this);
                mList.setAdapter(mAdapter);
            } else {
                mList.setVisibility(View.GONE);
                mNoResult.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.mInserted) {
            Toast.makeText(getApplicationContext(),
                    "Your details created Successfully", Toast.LENGTH_SHORT).show();
            Utils.mInserted = false;
            mContactList = myDb.getAllContacts();
            if (mContactList.size() > 0) {
                mList.setVisibility(View.VISIBLE);
                mNoResult.setVisibility(View.GONE);
                if (mList.getAdapter() != null) {
                    mAdapter.notifyDataSetChanged();
                    System.out.println("adpter not null");
                } else {
                    mAdapter = new ContactAdapter(ContactListActivity.this);
                    mList.setAdapter(mAdapter);
                    System.out.println("adpter  null");
                }
            } else {
                mList.setVisibility(View.GONE);
                mNoResult.setVisibility(View.VISIBLE);
            }
        }
        else if(Utils.mUpdated)
        {
            Toast.makeText(getApplicationContext(),
                    "Your details updated Successfully", Toast.LENGTH_SHORT).show();
            Utils.mUpdated = false;
            mContactList = myDb.getAllContacts();
            if (mContactList.size() > 0) {
                mList.setVisibility(View.VISIBLE);
                mNoResult.setVisibility(View.GONE);
                if (mList.getAdapter() != null) {
                    mAdapter.notifyDataSetChanged();
                    System.out.println("adpter not null");
                } else {
                    mAdapter = new ContactAdapter(ContactListActivity.this);
                    mList.setAdapter(mAdapter);
                    System.out.println("adpter  null");
                }
            } else {
                mList.setVisibility(View.GONE);
                mNoResult.setVisibility(View.VISIBLE);
            }
        }

        else if(Utils.mDeleted)
        {
            Toast.makeText(getApplicationContext(),
                    "Your contact deleted Successfully", Toast.LENGTH_SHORT).show();
            Utils.mDeleted = false;
            mContactList = myDb.getAllContacts();
            if (mContactList.size() > 0) {
                mList.setVisibility(View.VISIBLE);
                mNoResult.setVisibility(View.GONE);
                if (mList.getAdapter() != null) {
                    mAdapter.notifyDataSetChanged();
                    System.out.println("adpter not null");
                } else {
                    mAdapter = new ContactAdapter(ContactListActivity.this);
                    mList.setAdapter(mAdapter);
                    System.out.println("adpter  null");
                }
            } else {
                mList.setVisibility(View.GONE);
                mNoResult.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.create) {

            Intent io = new Intent(ContactListActivity.this, CreateContact.class);
            io.putExtra("type","create");
            startActivity(io);
            return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_contact, menu);
        return true;
    }

    public class ContactAdapter extends BaseAdapter {

        private Context context;
        LayoutInflater inflater;


        // Constructor to initialize values
        public ContactAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {

            // Number of times getView method call depends upon
            // gridValues.length
            return mContactList.size();
        }

        @Override
        public Object getItem(int position) {

            return mContactList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }


        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // LayoutInflator to call external grid_item.xml file
            final viewHold viewholder;
            if (convertView == null) {
                viewholder = new viewHold();
                // convertView = new View(context);
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.contacts_list_item, null);
                viewholder.mName = (TextView) convertView
                        .findViewById(R.id.name);
                viewholder.mLocation = (TextView) convertView
                        .findViewById(R.id.location);
                viewholder.mEmail = (TextView) convertView
                        .findViewById(R.id.email);
                viewholder.mPhone = (TextView) convertView
                        .findViewById(R.id.phone);
                viewholder.mImage = (ImageView) convertView.findViewById(R.id.profile);

                convertView.setTag(viewholder);
            } else {

                viewholder = (viewHold) convertView.getTag();
            }

            try {

                viewholder.mName.setText(mContactList.get(position).get("name"));
                viewholder.mPhone.setText(mContactList.get(position).get("phone"));
                viewholder.mLocation.setText(mContactList.get(position).get("location"));
                viewholder.mEmail.setText(mContactList.get(position).get("email"));
                if (!TextUtils.isEmpty(mContactList.get(position).get("image"))) {
                    viewholder.mImage.setImageURI(Uri.parse(mContactList.get(position).get("image").toString()));
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("reg123:" + mContactList.get(position).get("phone"));
                        Intent in = new Intent(ContactListActivity.this, ContactDetailsActivity.class);
                        in.putExtra("name", mContactList.get(position).get("name"));
                        in.putExtra("email", mContactList.get(position).get("email"));
                        in.putExtra("phone", mContactList.get(position).get("phone"));
                        in.putExtra("location", mContactList.get(position).get("location"));
                        in.putExtra("image", mContactList.get(position).get("image").toString());
                        startActivity(in);
                    }
                });

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return convertView;
        }

        class viewHold {
            public TextView mName, mLocation, mEmail, mPhone;
            public ImageView mImage;
        }
    }
}
