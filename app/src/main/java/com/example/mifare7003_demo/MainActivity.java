package com.example.mifare7003_demo;

import android.os.Bundle;
import android.pt.mifare.Mifare;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	Mifare mifare = null;

	boolean open_flg = false;	
	
	EditText edit_password = null;
	
	EditText edit_write_data = null;
	
	EditText edit_read_data = null;
	
	EditText edit_sel_block = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		
		edit_password = (EditText) findViewById(R.id.edit_passwd);
		
		edit_write_data = (EditText) findViewById(R.id.edit_writedata);
		
		edit_read_data = (EditText) findViewById(R.id.edit_readdata); 

		edit_sel_block = (EditText) findViewById(R.id.et_selblock); 

	}       
       
       
    public void open(View view)
    {
    	
    	
		if (open_flg)
		{
			 Messagebox(this, "is opend") ; 
			 return;  
		}
		
		mifare = new Mifare();
	   
	   
	   int ret =  mifare.open();
	   
	   if (ret == 0) {
		   
		   
		   Messagebox(this, "open success!!") ;
		   open_flg = true; 
		   
	    }
	   else {
		   
		   
		   Messagebox(this, "open fail!!") ;
		   open_flg = false; 		   
		
	  }
    }
    
    
    public void seek(View view)
    {
    	
		if (!open_flg)
		{
			 Messagebox(this, "please open first") ;
			 return;  
		}
		 
		
    	byte [] data = new byte[4];  
    	
    	int ret  = mifare.seek(data);      
    	
    	if (ret== 0) {  
			
    		   String  string1 ="";  
    		
		       for (int i = 0; i < 4; i++) {      

		    	   string1 += Integer.toHexString(data[i]&0xff)+" ";   
				  
			   }    
		       
		       
		       Messagebox(this, "find a Mifare card UID=="+string1); 
    		   
		}
    	else
    	{
    		Messagebox(this, "not find Mifare card");       
    	}
    	
    	
    }

    public void verifypassword(View view)
    {
    	
		if (!open_flg)
		{
			 Messagebox(this, "please open first") ;
			 return; 
		}	    	
    	
    	String str_password = edit_password.getText().toString();
    	
    	String str_block = edit_sel_block.getText().toString();
    	
    	int   int_block = Integer.parseInt(str_block);  
    	
    	
    	
    	if (str_password.equals("")) {
    		
    		Messagebox(this, "please input password!!!"); 	
    		return;
		} 	    
		
    	String passwd_str_array[] = str_password.split(",");
    	
    	byte[] passwd_byte = new byte[passwd_str_array.length]; 
    	
    	
   	    for (int i = 0; i < passwd_byte.length; i++) { 
    		
   	    	passwd_byte[i] = (byte) Integer.parseInt(passwd_str_array[i], 16);   
   	    	        
		}      	 
    	
        //NOTE: verify_type: you can fill 0X60 or 0x61,based on the actual card to fill in
   	    //      block_index: which mifare block you want read or write
   	    //      more informatin please refer to <7003 Mifare Manual> 
   	    
   	    
   	    
		int ret  = mifare.verifyMifareCard(0x60, int_block, passwd_byte, passwd_byte.length); 
		
		if (ret == 0) {
			
			Messagebox(this, "verify success  ") ; 
		}
		else {
			
			Messagebox(this, "verify fail  "+ ret ) ;   
		}
    }    
    
    
    public void writedata(View view)
    {
    	
		if (!open_flg)
		{
			 Messagebox(this, "please open first") ;
			 return; 
		}	 	
    	
    	
	   	   String write_data_str = edit_write_data.getText().toString();
	      	
	   	    if (write_data_str.equals("")) { 
	   		
	   		  Messagebox(this, "please input apdu cmd"); 	
	   		  
	   		  return;   
	   		  
			}   	      	
		
	     	String write_data_str_array[] = write_data_str.split(","); 

	   	
	     	byte[] write_data_byte = new byte[write_data_str_array.length];  
	   	
	     	for (int i = 0; i < write_data_byte.length; i++) { 
	   		
	     		write_data_byte[i] = (byte) Integer.parseInt(write_data_str_array[i], 16);
			} 
	   	

	    	String str_block = edit_sel_block.getText().toString();
	    	
	    	int   int_block = Integer.parseInt(str_block);  	     	
	     	
	     	
	   	    int ret  = mifare.writeMifareCard(int_block, write_data_byte, write_data_byte.length);		
		
		 
			if (ret == 0) { 
				
				Messagebox(this, "write success") ; 
			}
			else { 
				
				Messagebox(this, "write fail") ;  
			}
    }    
    
    
    public void readdata(View view) 
    {
    	
		if (!open_flg)
		{
			 Messagebox(this, "please open first") ;
			 return; 
		}		
    	
    	
		 byte[] out_data = new byte[1024];  
		 
	     String str_block = edit_sel_block.getText().toString();
	    	
	     int   int_block = Integer.parseInt(str_block);  
	     
	      
		 int len  = mifare.readMifareCard(int_block, out_data);   

		   	if (len<=0)
		   	{ 
		   		Messagebox(this, "read fail") ; 
		   		return;   
		   	}

			 String str_recv= new String(); 

			  for (int i = 0; i < len; i++) {           
		    
				  str_recv += Integer.toHexString(out_data[i]&0xff)+",";                       
			   }                  
			        
			   edit_read_data.setText(str_recv);
    }      

    public void close(View view)
    {
		if (!open_flg)
		{
			 Messagebox(this, "is closed") ;
			 return;  
		}

	   int ret =  mifare.close();
	   
	   if (ret == 0) {
		   
		   
		   Messagebox(this, "close success!!") ;
		   open_flg = false; 
		    
	    }
	   else {
		   
		   
		   Messagebox(this, "close fail!!") ;
		   open_flg = true;  		   
		     
	   }  	
    	
    	
    }

	public static void Messagebox(Context context,String info)
	{
		Builder builder = new Builder(context);    
		builder.setTitle("title");   
		builder.setMessage(info);     
		builder.setPositiveButton("yes", null);     
		builder.show();               
	}

}

 