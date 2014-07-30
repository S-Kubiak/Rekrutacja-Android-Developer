package com.example.rekrutacjaredexperts.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;

import com.example.rekrutacjaredexperts.json.JsonManager;

public class FileManager {
	String saveDir;
	
	JsonManager jsonManager = JsonManager.getSingletonInstance();
	
	private static FileManager singletonInstance;
	 
    // SingletonExample prevents any other class from instantiating
    private FileManager() {
    }
 
    // Providing Global point of access
    public static FileManager getSingletonInstance() {
        if (null == singletonInstance) {
            singletonInstance = new FileManager();
        }
        return singletonInstance;
    }
	
	public void startDownload() {
    	Thread thread = new Thread(new Runnable(){
    	    @Override
    	    public void run() {
    	        try {
    	            

    	    		// TODO Auto-generated method stub
    	        	String fileURL = "https://dl.dropboxusercontent.com/u/6556265/test.json";
    	            saveDir = Environment.getExternalStorageDirectory().toString()+"/Download";
    	            
    	            
    	            try {
    	               downloadFile(fileURL, saveDir);
    	            } catch (IOException ex) {
    	                ex.printStackTrace();
    	            }
    	        	
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }
    	    }
    	});

    	thread.start(); 
	}
	
	public void downloadFile(String fileURL, String saveDir)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
 
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();
 
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
 
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);
 
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;
             
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
            
            
 
            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                
            }
                
            outputStream.close();
            inputStream.close();
            System.out.println("File downloaded");
            //jsonManager.readJson();
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }
}
