package edu.monmouth.hw3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import edu.monmouth.owner.Owner;
import edu.monmouth.owner.OwnerException;
import edu.monmouth.vehicle.Boat;
import edu.monmouth.vehicle.Car;
import edu.monmouth.vehicle.Plane;
import edu.monmouth.vehicle.PowerTypes;
import edu.monmouth.vehicle.Vehicle;

public class HW3 {
    public static void main(String[] args) 
    {
        if (args.length != 2) 
        {
            System.err.println( "Please provide the properties file and data file.");
            System.exit(HW3Constants.EXIT_ERROR);
        }
        Properties properties = new Properties();
        try 
        {
            FileInputStream propertyFile = new FileInputStream(args[0]);
            properties.load(propertyFile);
            propertyFile.close();
            properties.list(System.out);
            String logFileName = properties.getProperty(HW3Constants.LOG_FILE_NAME);
            String keyValueDelimiter = properties.getProperty(HW3Constants.KEY_VALUE_DELIMITER);
            String ownerVehicleDelimiter = properties.getProperty(HW3Constants.OWNER_VEHICLE_DELIMITER);
            if (logFileName == null || keyValueDelimiter == null|| ownerVehicleDelimiter == null) 
            {
                System.err.println("A required property is missing.");
                System.exit(HW3Constants.EXIT_ERROR);
            }
            PrintStream log = new PrintStream(new FileOutputStream(logFileName));
            System.setOut(log);
            System.setErr(log);
            System.out.println("Properties loaded successfully.");
            System.out.println();
            runAssignment(args[1], keyValueDelimiter,ownerVehicleDelimiter);
            log.close();
        }
        catch (Exception e) 
        {
            System.err.println("HW3 Error: " + e.getMessage());
            System.exit(HW3Constants.EXIT_ERROR);
        }
        System.exit(HW3Constants.EXIT_SUCCESS);
    }
    public static void runAssignment(String dataFile, String keyValueDelimiter, String ownerVehicleDelimiter) {

        HashMap<Owner, Vehicle> ownerVehicles = new HashMap<Owner, Vehicle>();

        // Read the data file.
        try {
            Scanner input = new Scanner(new FileInputStream(dataFile));

            while (input.hasNextLine()) {
                String line = input.nextLine();

                try {
                    String[] mainFields =
                            line.split(java.util.regex.Pattern.quote(
                                    keyValueDelimiter), -1);

                    if (mainFields.length != 2) {
                        throw new Exception("Incorrect number of sections.");
                    }

                    String[] ownerFields =
                            mainFields[0].split(java.util.regex.Pattern.quote(
                                    ownerVehicleDelimiter), -1);

                    String[] vehicleFields =
                            mainFields[1].split(java.util.regex.Pattern.quote(
                                    ownerVehicleDelimiter), -1);

                    if (ownerFields.length != 2 || vehicleFields.length != 3) {
                        throw new Exception("Incorrect number of fields.");
                    }

                    String name = ownerFields[0].trim();
                    int yearOfBirth =
                            Integer.parseInt(ownerFields[1].trim());

                    String vehicleType = vehicleFields[0].trim();
                    PowerTypes powerType =
                            PowerTypes.valueOf(vehicleFields[1].trim());
                    int vehicleValue =
                            Integer.parseInt(vehicleFields[2].trim());

                    Owner owner = new Owner(name, yearOfBirth);
                    Vehicle vehicle =
                            createVehicle(vehicleType, powerType, vehicleValue);

                    ownerVehicles.put(owner, vehicle);

                } catch (Exception e) {
                    System.err.println("Invalid line discarded: " + line);
                    System.err.println("Reason: " + e.getMessage());
                }
            }

            input.close();

        } catch (Exception e) {
            System.err.println("Could not read data file: " + e.getMessage());
            return;
        }

        // Print the number of mappings.
        System.out.println("Number of mappings: " + ownerVehicles.size());
        System.out.println();

        // Print every owner and vehicle.
        System.out.println("Owner and Vehicle Mappings:");

        for (Map.Entry<Owner, Vehicle> entry : ownerVehicles.entrySet()) {
            System.out.println(entry.getKey() + " --> " + entry.getValue());
        }

        System.out.println();

        // Test an owner that is in the HashMap.
        try {
            Owner existingOwner = new Owner("Alan Turing", 1912);

            System.out.println("Testing an owner in the HashMap:");
            System.out.println("containsKey: "
                    + ownerVehicles.containsKey(existingOwner));

            System.out.println("remove: "
                    + ownerVehicles.remove(existingOwner));

            System.out.println();
            System.out.println("Mappings after removal:");

            for (Map.Entry<Owner, Vehicle> entry : ownerVehicles.entrySet()) 
            {
                System.out.println(entry.getKey() + " --> " + entry.getValue());
            }
        } 
        catch (OwnerException e) 
        {
            System.err.println(e.getMessage());
        }
        System.out.println();
        try 
        {
            Owner missingOwner = new Owner("Nobody Here", 2000);
            System.out.println("Testing an owner NOT in the HashMap:");
            System.out.println("containsKey: " + ownerVehicles.containsKey(missingOwner));
            System.out.println("remove: " + ownerVehicles.remove(missingOwner));
        } 
        catch (OwnerException e) 
        {
            System.err.println(e.getMessage());
        }
        System.out.println();
        testVehicleHashCodes();
    }
    public static Vehicle createVehicle(String type,PowerTypes powerType, int value) throws Exception
    {
        if (type.equalsIgnoreCase("c")) 
        {
            return new Car(powerType, value);
        } 
        else if (type.equalsIgnoreCase("b")) 
        {
            return new Boat(powerType, value);
        }
        else if (type.equalsIgnoreCase("p")) 
        {
            return new Plane(powerType, value);
        }
        else 
        {
            throw new Exception("Invalid vehicle type: " + type);
        }
    }
    public static void testVehicleHashCodes() 
    {
        try 
        {
            System.out.println("Vehicle equals and hashCode tests:");
            Car car1 = new Car(PowerTypes.GASOLINE, 4);
            Car car2 = new Car(PowerTypes.GASOLINE, 4);
            Car car3 = new Car(PowerTypes.GASOLINE, 5);
            System.out.println("Cars equal: " + car1.equals(car2));
            System.out.println("Car hashCodes equal: " + (car1.hashCode() == car2.hashCode()));
            System.out.println("Different cars equal: " + car1.equals(car3));
            System.out.println("Different car hashCodes: " + (car1.hashCode() != car3.hashCode()));
            Boat boat1 = new Boat(PowerTypes.GASOLINE, 100);
            Boat boat2 = new Boat(PowerTypes.GASOLINE, 100);
            Boat boat3 = new Boat(PowerTypes.GASOLINE, 200);
            System.out.println("Boats equal: " + boat1.equals(boat2));
            System.out.println("Boat hashCodes equal: " + (boat1.hashCode() == boat2.hashCode()));
            System.out.println("Different boats equal: " + boat1.equals(boat3));
            System.out.println("Different boat hashCodes: " + (boat1.hashCode() != boat3.hashCode()));
            Plane plane1 = new Plane(PowerTypes.GASOLINE, 300);
            Plane plane2 = new Plane(PowerTypes.GASOLINE, 300);
            Plane plane3 = new Plane(PowerTypes.GASOLINE, 400);
            System.out.println("Planes equal: " + plane1.equals(plane2));
            System.out.println("Plane hashCodes equal: " + (plane1.hashCode() == plane2.hashCode()));
            System.out.println("Different planes equal: " + plane1.equals(plane3));
            System.out.println("Different plane hashCodes: " + (plane1.hashCode() != plane3.hashCode()));
        }
        catch (Exception e) 
        {
            System.err.println("Vehicle test error: " + e.getMessage());
        }
    }
}