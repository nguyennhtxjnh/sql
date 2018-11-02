package day11.thaonguyen.demosql;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    // Declaring layout button, edit texts
    ImageView img ;
    Button login;
    EditText username,password;
    String b;
    ProgressBar progressBar;
    // End Declaring layout button, edit texts

    // Declaring connection variables
    Connection con;
    String un,pass,db,ip;
    //End Declaring connection variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting values from button, texts and progress bar
        img = (ImageView) findViewById(R.id.img1);
        Picasso.with(this)
                .load("https://png.pngtree.com/element_origin_min_pic/16/11/13/2ea65f141144fc37b77c95e4030bf1b2.jpg")
                .into(img);
        login = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        // progressBar = (ProgressBar) findViewById(R.id.pro);
        // End Getting values from button, texts and progress bar

        // Declaring Server ip, username, database name and password
        ip = "192.168.1.106:1433";
        db = "testAnd";
        un = "sa";
        pass = "nguyen123";
        // Declaring Server ip, username, database name and password

        // Setting up the function when button login is clicked
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute("");
            }
        });
        //End Setting up the function when button login is clicked
    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

//        @Override
//        protected void onPreExecute()
//        {
//            progressBar.setVisibility(View.VISIBLE);
//        }

        @Override
        protected void onPostExecute(String r)
        {
//            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(MainActivity.this , "Login Successfull" , Toast.LENGTH_LONG).show();
                //finish();
            }
        }
        @Override
        protected String doInBackground(String... params)
        {
            String usernam = username.getText().toString();
            String passwordd = password.getText().toString();
            if(usernam.trim().equals("")|| passwordd.trim().equals(""))
                z = "Please enter Username and Password";
            else
            {
                try
                {
                    con = connectionclass(un, pass, db, ip);        // Connect to database
                    if (con == null)
                    {
                        z = "Check Your Internet Access!";
                    }
                    else
                    {
                        // Change below query according to your own database.
                        String query = "select * from imagePust where Name= '" + usernam.toString() + "' and Password = '"+ passwordd.toString() +"'  ";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {

                            b = rs.getString("Image");

                            z = "Login successful";
                            isSuccess=true;
                            con.close();
                        }
                        else
                        {
                            z = "Invalid Credentials!";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;

        }

    }


    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server)
    {
       // Toast.makeText(MainActivity.this, "in connect", Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server +";databaseName="+ database + ";user=" + user+ ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            //Log.e("error here 1 : ", se.getMessage() );
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
          //  Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
           // Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }


}
