package Common;

import java.io.Serializable;

public class User implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1108449704181736083L;
	private String _name;
	private String _password;
	
	public User()
	{
		_name = "";
		_password = "";
	}
	
	public User(String username, String password)
	{
		_name = username;
		_password = password;
	}
	
	public boolean checkPassword(String password)
	{
		System.out.println(password);
		System.out.println(_password);
		return _password.equals(password);
	}
	
	public String toString()
	{
		return "username: " + _name;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getPassword()
	{
		return _password;
	}
}