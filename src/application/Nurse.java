package application;

public class Nurse
{
    private String username;
    private String employeeId;
    private String messages;

    public Doctor(String username, String employeeId)
    {
        this.username = username;
        this.employeeId = employeeId;
        this.messages = "";
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(String employeeId)
    {
        this.employeeId = employeeId;
    }

    public String getMessages()
    {
        return messages;
    }

    public void addMessage(String message)
    {
        this.messages += message + "\n";
    }
}