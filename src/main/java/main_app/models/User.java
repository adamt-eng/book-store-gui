package main_app.models;

import main_app.utils.*;

public abstract class User
{
    protected String username, email, password;

    public User(String username, String email, String password)
    {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public static User login(String username, String password, String accountType)
    {
        for (String user : FileManager.readFile(accountType == "admin" ? Constants.ADMINS_FILE_PATH
                : Constants.READERS_FILE_PATH))
        {
            String[] userDetails = SecurityService.decrypt(user).split(",");

            if (username.contains(userDetails[1]) && password.contains(userDetails[2]))
            {
                if (accountType == "admin")
                {
                    return new Admin(userDetails[1], userDetails[0], userDetails[2]);
                } else
                {
                    return new Reader(userDetails[1], userDetails[0], userDetails[2], userDetails[3], userDetails[4],
                            userDetails[5]);
                }
            }
        }

        return null;
    }
}