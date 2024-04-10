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
	
    public void saveMessage(String senderId, String recipientId, String message, boolean fromPatient) {
        String filename = "messages/" + recipientId + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(filename), true))) {
            if (fromPatient) {
                writer.println("Patient (" + senderId + "): " + message);
            } else {
                writer.println("Doctor (" + senderId + "): " + message);
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
		try {
			int visits = 1;
			String username = patient.getPatientId();
			String password = "";
			Scanner reader = new Scanner(new File("patient_login/" + username + ".txt"));
			password = reader.nextLine();
			reader.close();
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File("patient_login/" + username + ".txt"), false));
			writer.write(password + "\n" + patient.getFirstName() + "\n" + patient.getLastName() + "\n" + patient.getPhone() + "\n" + patient.getBirthday() + "\n" + patient.getPharmacy() + "\n---\n" + patient.getInsurance()+"\n");
			writer.close();
			try {
				reader = new Scanner(new File("patient_info/" + username + ".txt"));
				while(reader.hasNextLine()) {
					if(reader.nextLine().contains("----------"))
						visits++;
				}
				reader.close();
			} catch(FileNotFoundException e) {
				System.out.println("No file found for patient, creating new file.");
			}
			writer = new PrintWriter(new FileOutputStream(new File("patient_info/" + patient.getPatientId() + ".txt"), true));
			writer.append("----------Visit " + visits + "----------\n" + "FINDINGS:\n" + patient.getMedicalHistory() + "\nIMMUNIZATIONS/PRESCRIPTIONS:\n" + patient.getImmunizationsPrescriptions() + "\nINTAKE INFO:\n" + patient.getPreviousVisits()+"\n");
			writer.close();
		} catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public Patient getPatientInfo(String patientId) {
		Patient p = new Patient();
		try {
			Scanner reader = new Scanner(new File("patient_login/" + patientId + ".txt"));
			reader.nextLine(); // ignore password
			p.setFirstName(reader.nextLine());
			p.setLastName(reader.nextLine());
			p.setPhone(reader.nextLine());
			p.setBirthday(reader.nextLine());
			p.setPharmacy(reader.nextLine());
			String line = reader.nextLine();
			while(!line.equals("---")) {
				p.setPharmacy(p.getPharmacy() + "\n" + line);
				line = reader.nextLine();
			}
			p.setInsurance(reader.nextLine());
			while(reader.hasNextLine()) {
				p.setInsurance(p.getInsurance() + "\n" + reader.nextLine());
			}
			reader.close();
			
			String header = "";
			reader = new Scanner(new File("patient_info/" + patientId + ".txt"));
			line = reader.nextLine();
			while(reader.hasNextLine()) {
				header = line;
				while(!line.contains("FINDINGS:")) {
					line = reader.nextLine();
				}
				p.setMedicalHistory(p.getMedicalHistory() + "\n" + header);
				line = reader.nextLine();
				while(!line.contains("IMMUNIZATIONS/PRESCRIPTIONS:")) {
					p.setMedicalHistory(p.getMedicalHistory() + "\n" + line);
					line = reader.nextLine();
				}
				p.setImmunizationsPrescriptions(p.getImmunizationsPrescriptions() + "\n" + header);
				line = reader.nextLine();
				while(!line.contains("INTAKE INFO:")) {
					p.setImmunizationsPrescriptions(p.getImmunizationsPrescriptions() + "\n" + line);
					line = reader.nextLine();
				}
				p.setPreviousVisits(p.getPreviousVisits() + "\n" + header);
				line = reader.nextLine();
				while(!line.contains("---") && reader.hasNextLine()) {
					p.setPreviousVisits(p.getPreviousVisits() + "\n" + line);
					line = reader.nextLine();
				}
			}
			p.setPreviousVisits(p.getPreviousVisits() + "\n" + line);
			reader.close();
		}catch(IOException e) {
			System.out.println(e);
		}
		return p;
	}
}