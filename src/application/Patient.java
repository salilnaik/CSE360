package application;

public class Patient {	
	private String patient_id = "";
	private String first_name = "";
	private String last_name = "";
	private String phone_number = "";
	private String birthday = "";
	private String medical_history = ""; // Will store previous health issues and doctor's findings
	private String immunizations_prescriptions = ""; // Will store immunizations and prescriptions
	private String previous_visits = ""; // Will store allergy info, health concerns, vitals (empty if under 12)
	private String pharmacy = "";
	private String insurance = "";
	private String email = "";
	
	// No arguments in constructor
	public Patient() {}
	
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
		return immunizations_prescriptions;
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
	public String getEmail() {
		return email;
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
		this.immunizations_prescriptions = ip;
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
	public void setEmail(String email) {
		this.email = email;
	}
}