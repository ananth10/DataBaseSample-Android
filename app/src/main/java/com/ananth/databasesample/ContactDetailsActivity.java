package com.ananth.databasesample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView mImage;
    private TextView mName;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mLocation;
    String mImageUri="";
    DBHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        mImage=(ImageView)findViewById(R.id.profile);
        mName=(TextView)findViewById(R.id.name);
        mEmail=(TextView)findViewById(R.id.email);
        mPhone=(TextView)findViewById(R.id.phone);
        mLocation=(TextView)findViewById(R.id.location);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Details");
        myDb = new DBHelper(this);
        if(getIntent()!=null)
        {
            mName.setText(getIntent().getStringExtra("name"));
            mEmail.setText(getIntent().getStringExtra("email"));
            mPhone.setText(getIntent().getStringExtra("phone"));
            mLocation.setText(getIntent().getStringExtra("location"));

            if(getIntent().getStringExtra("image")!=null)
            {
                mImageUri=getIntent().getStringExtra("image");
                mImage.setImageURI(Uri.parse(mImageUri.toString()));
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_contact, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        else if(id==R.id.edit)
        {
            Intent intent=new Intent(ContactDetailsActivity.this,CreateContact.class);
            intent.putExtra("type" ,"edit");
            intent.putExtra("name" ,mName.getText().toString());
            intent.putExtra("email",mEmail.getText().toString());
            intent.putExtra("phone",mPhone.getText().toString());
            intent.putExtra("location",mLocation.getText().toString());
            intent.putExtra("image",mImageUri);
            startActivity(intent);

            return true;
        }
        else if(id==R.id.delete)
        {
            showDeleteDialog();

            return true;
        }
        return true;
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteContact)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                myDb.deleteContact(mPhone.getText().toString());
                                Utils.mDeleted = true;
                                startActivity(new Intent(ContactDetailsActivity.this, ContactListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();

                            }
                        })
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub

                            }
                        });

        AlertDialog d = builder.create();
        d.setTitle("Delete Contact?");
        d.show();
    }
}
