package com.itheima.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
public static String readFromStream(InputStream in){
	ByteArrayOutputStream out=new ByteArrayOutputStream();
	byte[] bf=new byte[1024];
	int length=0;
	String result = null;
	try {
		if ((length=in.read(bf))!=1) {
			out.write(bf, 0, length);			
		}
		result=out.toString();
		in.close();
		out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return result;	
}
}
