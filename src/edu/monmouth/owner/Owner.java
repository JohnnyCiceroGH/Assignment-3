package edu.monmouth.owner;

public class Owner 
{
    private String name;
    private int yearOfBirth;
    public Owner(String name, int yearOfBirth) throws OwnerException
    {
        setName(name);
        setYearOfBirth(yearOfBirth);
    }
    public String getName() 
    {
        return name;
    }
    public void setName(String name) throws OwnerException 
    {
        if (name == null || name.trim().length() == 0) 
        {
            throw new OwnerException("Owner name cannot be empty.");
        }
        this.name = name;
    }
    public int getYearOfBirth() 
    {
        return yearOfBirth;
    }
    public void setYearOfBirth(int yearOfBirth) throws OwnerException 
    {
        if (yearOfBirth < 1000) 
        {
            throw new OwnerException("Year of birth must be at least 1000.");
        }
        this.yearOfBirth = yearOfBirth;
    }
    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        Owner other = (Owner) obj;
        return name.equals(other.name) && yearOfBirth == other.yearOfBirth;
    }
    @Override
    public int hashCode() 
    {
        int result = 1;
        result = 31 * result + name.hashCode();
        result = 31 * result + yearOfBirth;
        return result;
    }
    @Override
    public String toString() 
    {
        return "Owner- name=" + name + ", yearOfBirth=" + yearOfBirth + "-";
    }
}