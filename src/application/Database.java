package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
	public boolean validatePatient(String username, String password) {
        File folder = new File("patient_login/");
        File[] fileList = folder.listFiles();
        
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile() && file.getName().equals(username + ".txt")) {
                    try (Scanner scanner = new Scanner(file)) {
                        // Search for the line starting with "Password:"
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if (line.startsWith("Password:") && line.substring(9).trim().equals(password)) {
                                return true;
                            }
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("Error reading patient file: " + e.getMessage());
                    }
                }
            }
        }
        return false;
    }
	
	// Method to get patient IDs from the patient_login directory
    public List<String> getPatientIDs() {
        List<String> patientIDs = new ArrayList<>();
        File directory = new File("patient_login");
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        String patientID = fileName.substring(0, fileName.lastIndexOf('.'));
                        patientIDs.add(patientID);
                    }
                }
            }
        }
        return patientIDs;
    }
	
    public void saveMessage(String recipientId, String message, boolean fromPatient) {
        String filename = "messages/" + recipientId + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(filename), true))) {
            if (fromPatient) {
                writer.println("Patient: " + message);
            } else {
                writer.println("Doctor: " + message);
            }
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }
	
	// gets all messages associated with a patient and returns as a string
    public String getMessages(String patientId) {
        String messagesFilePath = "messages/" + patientId + ".txt";
        StringBuilder messages = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(messagesFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                messages.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading messages for patient: " + e.getMessage());
        }
        return messages.toString();
    }
	
	// Account for patient in patient_login folder must already exist
	// If file for patient in patient_info folder not found, a new file will be created for the patient
	public void updatePatientInfo(Patient patient) {
		String filePath = "patient_login/" + patient.getPatientId() + ".txt";
		String password = "";
		PrintWriter writer;
		try(Scanner reader = new Scanner(new File("patient_login/" + patient.getPatientId() + ".txt"))){
			String line;
            while (reader.hasNextLine())
            {
            	line = reader.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].strip().equals("Password")){
                	password = parts[1].strip();
                	reader.close();
                	break;
                }
            }
			reader.close();
		}catch(IOException e) {
			System.out.println(e);
		}
        try
        {
        	writer = new PrintWriter(new FileOutputStream(new File(filePath), true));
            writer.write("First Name: " + patient.getFirstName() + "\n");
            writer.write("Last Name: " + patient.getLastName() + "\n");
            writer.write("Date of Birth: " + patient.getBirthday() + "\n");
            writer.write("Email: " + patient.getEmail() + "\n");
            writer.write("Phone Number: " + patient.getPhone() + "\n");
            writer.write("Pharmacy Information: " + patient.getPharmacy() + "\n"); 
            writer.write("Insurance ID: " + patient.getInsurance() + "\n"); 
            writer.write("Password: " + password + "\n");
        }
        
        catch (IOException ex)
        {
            System.out.println("Error occurred while updating patient info" + ex.getMessage());
        }
	}
	
	public Patient getPatientInfo(String patientId) {
		Patient p = new Patient();
		p.setPatientId(patientId);
		String filePath = "patient_login/" + patientId + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(":");
                if (parts.length == 2 && !parts[0].strip().equals("Password"))
                {
                	switch(parts[0].strip()) {
                	case "First Name":
                		p.setFirstName(parts[1].strip());
                	case "Last Name":
                		p.setLastName(parts[1].strip());
                	case "Date of Birth":
                		p.setBirthday(parts[1].strip());
                	case "Email":
                		p.setEmail(parts[1].strip());
                	case "Phone Number":
                		p.setPhone(parts[1].strip());
                	case "Pharmacy Information":
                		p.setPharmacy(parts[1].strip());
                	case "Insurance ID":
                		p.setInsurance(parts[1].strip());
                	}
                }
            }		
			String header = "";
			Scanner scanner = new Scanner(new File("patient_info/" + patientId + ".txt"));
			if(scanner.hasNextLine())
				line = scanner.nextLine();
			while(scanner.hasNextLine()) {
				header = line;
				while(!line.contains("INTAKE INFO:") && scanner.hasNextLine()) {
					line = scanner.nextLine();
				}
				p.setPreviousVisits(p.getPreviousVisits() + "\n" + header);
				if(!scanner.hasNextLine())
					break;
				line = scanner.nextLine();
				while(!line.contains("FINDINGS:") && scanner.hasNextLine()) {
					p.setPreviousVisits(p.getPreviousVisits() + "\n" + line);
					line = scanner.nextLine();
				}
				p.setMedicalHistory(p.getMedicalHistory() + "\n" + header);
				if(!scanner.hasNextLine())
					break;
				line = scanner.nextLine();
				while(!line.contains("IMMUNIZATIONS/PRESCRIPTIONS:") && scanner.hasNextLine()) {
					p.setMedicalHistory(p.getMedicalHistory() + "\n" + line);
					line = scanner.nextLine();
				}
				p.setImmunizationsPrescriptions(p.getImmunizationsPrescriptions() + "\n" + header);
				if(!scanner.hasNextLine())
					break;
				line = scanner.nextLine();
				while(!line.contains("---") && scanner.hasNextLine()) {
					p.setImmunizationsPrescriptions(p.getImmunizationsPrescriptions() + "\n" + line);
					line = scanner.nextLine();
				}
			}
			p.setPreviousVisits(p.getPreviousVisits() + "\n" + line);
			scanner.close();
		}catch(IOException e) {
			System.out.println(e);
		}
		return p;
	}
}