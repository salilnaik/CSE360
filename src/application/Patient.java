package application;

public class Patient {	
	private String patient_id;
	private String first_name;
	private String last_name;
	private String phone_number;
	private String birthday;
	private String medical_history;
	private String immunization_prescriptions;
	private String previous_visits;
	private String pharmacy;
	private String insurance;
	
	// Arguments that must be passed into this constructor include every private variable associated with the patient except the patient id
	public Patient(String first_name, String last_name, String phone_number, String birthday, String medical_history, String ip, String previous_visits, String pharmacy, String insurance) {
		// Patient ID is automatically generated from first name, last name, and birthday without any '-', '/', or '.' characters
		this.patient_id = first_name + last_name + birthday.replaceAll("[-|/|.]", ""); // <- Regex replaces all '-', '/', '.' characters in birthday with ""
		this.first_name = first_name;
		this.last_name = last_name;
		this.phone_number = phone_number;
		this.birthday = birthday;
		this.medical_history = medical_history;
		this.immunization_prescriptions = ip;
		this.previous_visits = previous_visits;
		this.pharmacy = pharmacy;
		this.insurance = insurance;
	}
	
	// Public getters for patient info continue below
	public String getPatientId() {
		return patient_id;
	}
	public String getFirstName() {
		return first_name;
	}
	public String getLastName() {
		return last_name;
	}
	public String getPhone() {
		return phone_number;
	}
	public String getBirthday() {
		return birthday;
	}
	public String getMedicalHistory() {
		return medical_history;
	}
	public String getImmunizationsPrescriptions() {
		return immunization_prescriptions;
	}
	public String getPreviousVisits() {
		return previous_visits;
	}
	public String getPharmacy() {
		return pharmacy;
	}
	public String getInsurance() {
		return insurance;
	}
	
	// public setters for patient info
	public void setPatientId(String patient_id) {
		this.patient_id = patient_id;
	}
	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}
	public void setLastName(String last_name) {
		this.last_name = last_name;
	}
	public void setPhone(String phone_number) {
		this.phone_number = phone_number;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public void setMedicalHistory(String medical_history) {
		this.medical_history = medical_history;
	}
	public void setImmunizationsPrescriptions(String ip) {
		this.immunization_prescriptions = ip;
	}
	public void setPreviousVisits(String previous_visits) {
		this.previous_visits = previous_visits;
	}
	public void setPharmacy(String pharmacy) {
		this.pharmacy = pharmacy;
	}
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
}
