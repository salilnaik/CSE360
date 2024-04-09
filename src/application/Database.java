package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Database {
	File[] fileList;
	// Constructor is empty for now
	public Database() {}
	
	// Take in username and password of doctor and check if credentials are valid
	// doctor login info is stored in doctor_login folder where file name is the doctor's username and file contents are the password
	public boolean validateDoctor(String username, String password){
		File folder = new File("doctor_login/"); // doctor login info is stored in doctor_login folder where file name is the doctor's username and file contents are the password
		fileList = folder.listFiles();
		if(fileList != null) {
			for(File file : fileList) {
				if(file.isFile()) {
					if(file.getName().equals(username+".txt")) {
						try {
							Scanner reader = new Scanner(file);
							if(reader.nextLine().equals(password)) {
								reader.close();
								return true;
							}
							reader.close();
							return false;
						} catch(FileNotFoundException e){
							System.out.println(e);
						}
					}
				}
			}
		}
		return false;
	}
	
	// Take in username and password of nurse and check if credentials are valid
	// nurse login info is stored in nurse_login folder where file name is the nurse's username and file contents are the password
	public boolean validateNurse(String username, String password){
		File folder = new File("nurse_login/");
		fileList = folder.listFiles();
		if(fileList != null) {
			for(File file : fileList) {
				if(file.isFile()) {
					if(file.getName().equals(username+".txt")) {
						try {
							Scanner reader = new Scanner(file);
							if(reader.nextLine().equals(password)) {
								reader.close();
								return true;
							}
							reader.close();
							return false;
						} catch(FileNotFoundException e){
							System.out.println(e);
						}
					}
				}
			}
		}
		return false;
	}
	
	// Take in username and password of patient and check if credentials are valid
	// patient login info is stored in patient_login folder where file name is the patient's username and file contents are the password
	public boolean validatePatient(String username, String password){
		File folder = new File("patient_login/");
		fileList = folder.listFiles();
		if(fileList != null) {
			for(File file : fileList) {
				if(file.isFile()) {
					if(file.getName().equals(username+".txt")) {
						try {
							Scanner reader = new Scanner(file);
							if(reader.nextLine().equals(password)) {
								reader.close();
								return true;
							}
							reader.close();
							return false;
						} catch(FileNotFoundException e){
							System.out.println(e);
						}
					}
				}
			}
		}
		return false;
	}
	
	// Take in username and password of patient and create account
	// Will overwrite patient password if patient already has a password in the system
	public void createPatientLogin(String username, String password) {
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File("patient_login/" + username + ".txt"), true));
			writer.write(password + "\n");
			writer.close();
		} catch(IOException e) {
			System.out.println(e);
		}
	}
}
