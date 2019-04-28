package com.apl.auction.model;
public class LoginRequestModel
{
    private String ApiKey;

    private String Email;

    public String getApiKey ()
    {
        return ApiKey;
    }

    public void setApiKey (String ApiKey)
    {
        this.ApiKey = ApiKey;
    }

    public String getEmail ()
    {
        return Email;
    }

    public void setEmail (String Email)
    {
        this.Email = Email;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ApiKey = "+ApiKey+", Email = "+Email+"]";
    }
}