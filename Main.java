/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */

import system.*;

import java.util.Locale;
import java.util.Scanner;
import dataStructures.*;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import system.exceptions.*;

public class Main {

    // General / error messages
    private static final String UNKNOWN_COMMAND = "Unknown command. Type help to see available commands.";
    private static final String SYSTEM_BOUNDS_NOT_DEFINED = "System bounds not defined.";
    private static final String BYE = "Bye!";
    private static final String BOUNDS_ALREADY_EXISTS = "Bounds already exists. Please load it!";
    private static final String INVALID_BOUNDS = "Invalid bounds.";
    private static final String INVALID_SERVICE_TYPE = "Invalid service type!";
    private static final String INVALID_LOCATION = "Invalid location!";
    private static final String INVALID_MENU_PRICE = "Invalid menu price!";
    private static final String INVALID_ROOM_PRICE = "Invalid room price!";
    private static final String INVALID_DISCOUNT_PRICE = "Invalid discount price!";
    private static final String INVALID_CAPACITY = "Invalid capacity!";
    private static final String INVALID_TICKET_PRICE = "Invalid ticket price!";
    private static final String NO_SERVICES_YET = "No services yet!";
    private static final String SERVICE_ALREADY_EXISTS = "%s already exists!";
    private static final String AREA_LOADED = "%s loaded.";
    private static final String AREA_DOES_NOT_EXIST = "Bounds %s does not exist.";
    private static final String AREA_SAVED = "%s saved.";
    private static final String AREA_CREATED = "%s created.";

    // Student and service messages
    private static final String INVALID_STUDENT_TYPE = "Invalid student type!";
    private static final String LODGING_DOES_NOT_EXIST = "lodging %s does not exist!";
    private static final String LODGING_IS_FULL = "lodging %s is full!";
    private static final String STUDENT_ALREADY_EXISTS = "%s already exists!";
    private static final String STUDENT_ADDED = "%s added.";
    private static final String STUDENT_LEFT = "%s has left.";
    private static final String STUDENT_DOES_NOT_EXIST = "%s does not exist!";
    private static final String NO_STUDENTS_FROM_COUNTRY = "No students from %s!";
    private static final String NO_STUDENTS_YET = "No students yet!";
    private static final String GO_SUCCESS = "%s is now at %s.";
    private static final String GO_DISTRACTED = "%s is distracted!";
    private static final String UNKNOWN_LOCATION_FMT = "Unknown %s!";
    private static final String LOCATION_NOT_VALID_FMT = "%s is not a valid service!";
    private static final String EATING_FULL_FMT = "eating %s is full!";
    private static final String ALREADY_THERE = "Already there!";
    private static final String MOVE_SUCCESS = "lodging %s is now %s's home. %s is at home.";
    private static final String THAT_IS_HOME = "That is %s's home!";
    private static final String MOVE_NOT_ACCEPTABLE = "Move is not acceptable for %s!";
    private static final String THIS_ORDER_NOT_EXISTS = "This order does not exists!";
    private static final String SERVICE_DOES_NOT_EXIST = "%s does not exist!";
    private static final String SERVICE_NOT_CONTROL = "%s does not control student entry and exit!";
    private static final String NO_STUDENTS_ON_SERVICE = "No students on %s!";
    private static final String STUDENT_IS_THRIFTY = "%s is thrifty!";
    private static final String STUDENT_NO_VISITED = "%s has not visited any locations!";
    private static final String INVALID_EVALUATION = "Invalid evaluation!";
    private static final String EVALUATION_REGISTERED = "Your evaluation has been registered!";
    private static final String NO_SERVICES = "No services in the system.";
    private static final String SERVICES_SORTED = "Services sorted in descending order";
    private static final String INVALID_STARS = "Invalid stars!";
    private static final String NO_SERVICES_OF_TYPE = "No %s services!";
    private static final String NO_SERVICES_WITH_AVERAGE = "No %s services with average!";
    private static final String RANKED_HEADER = "%s services closer with %d average";
    private static final String ERROR_SAVING_AREA = "Erro saving area: %s";
    private static final String NO_SERVICES_WITH_TAG = "There are no services with this tag!";

    /**
     * Program entry point. Initialize scanner and start command loop.
     *
     * @pre args != null
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner in = new  Scanner(System.in);
        runCommand(in);
        in.close();
    }

    /**
     * Primary loop: parse commands and dispatch to handlers.
     *
     * @pre in != null
     * The loop stops when the EXIT command is entered. If there is a current
     * area loaded, it is saved on exit.
     *
     * @param in Scanner connected to stdin
     */
    private static void runCommand(Scanner in) {
        boolean exitCommand = false;
        AreaClass currentArea = null;

        do{
            String[] line = in.nextLine().split(" ");
            CommandType command;
            try {
                command = CommandType.getEnum(line[0].toUpperCase());
                switch(command){
                    case CMD_BOUNDS:
                        currentArea = processBounds(line, currentArea);
                        break;
                    case CMD_SAVE:
                        processSave(currentArea);
                        break;
                    case CMD_LOAD:
                        currentArea = processLoad(line);
                        break;
                    case CMD_SERVICE:
                        processService(line, currentArea);
                        break;
                    case CMD_SERVICES:
                        processServices(currentArea);
                        break;
                    case CMD_STUDENT:
                        processStudent(in, currentArea, line);
                        break;
                    case CMD_STUDENTS:
                        processStudents(line, currentArea);
                        break;
                    case CMD_LEAVE:
                        processLeave(line, currentArea);
                        break;
                    case CMD_GO:
                        processGo(in, currentArea, line);
                        break;
                    case CMD_MOVE:
                        processMove(in, currentArea, line);
                        break;
                    case CMD_USERS:
                        processUsers(line, currentArea);
                        break;
                    case CMD_STAR:
                        processStar(line,in,currentArea);
                        break;
                    case CMD_WHERE:
                        processWhere(line, currentArea);
                        break;
                    case CMD_VISITED:
                        processVisited(line, currentArea);
                        break;
                    case CMD_RANKING:
                        processRanking(currentArea);
                        break;
                    case CMD_RANKED:
                        processRanked(line, currentArea);
                        break;
                    case CMD_TAG:
                        processTag(line, currentArea);
                        break;
                    case CMD_FIND:
                        processFind(line, in, currentArea);
                        break;
                    case CMD_HELP:
                        System.out.printf(CommandDescription.getHelpMessage());
                        break;
                    case CMD_EXIT:
                        exitCommand = true;
                        if(currentArea != null) saveCurrentArea(currentArea);
                        System.out.println(BYE);
                        break;
                    case null:
                        for(int i = 0; i < line.length; i++){System.out.println(UNKNOWN_COMMAND);}
                        break;
                    default:
                        break;
                }
            } catch (IllegalArgumentException ignored) {
            }
        } while(!exitCommand);
    }

    /**
     * Handle the 'bounds' command: create a new area and save any previous one.
     *
     * @pre line.length >= 6
     * @param line tokenized command line
     * @param currentArea the currently loaded area (may be null)
     * @return the new AreaClass instance or the unchanged currentArea on error
     */
    private static AreaClass processBounds(String[] line, AreaClass currentArea) {
        // if there is an existing area, save it before creating a new one
        if (currentArea != null) {
            saveCurrentArea(currentArea);
        }

        long topLat = Long.parseLong(line[1]);
        long leftLong = Long.parseLong(line[2]);
        long bottomLat = Long.parseLong(line[3]);
        long rightLong = Long.parseLong(line[4]);

        // reassemble the area name (may contain spaces)
        StringBuilder nameBuilder = new StringBuilder();

        for (int i = 5; i < line.length; i++) {
            nameBuilder.append(line[i]);
            if (i < line.length - 1) nameBuilder.append(" ");
        }

        String name = nameBuilder.toString();

        // basic validation of coordinates
        if (topLat <= bottomLat || rightLong <= leftLong) {
            System.out.println(INVALID_BOUNDS);
            return currentArea;
        }

        File checkFile = new File(name.toLowerCase().replace(" ", "_") + ".ser");

        if (checkFile.exists()) {
            System.out.println(BOUNDS_ALREADY_EXISTS);
            return currentArea;
        }

        Location left = new LocationClass(topLat,leftLong);
        Location right = new LocationClass(bottomLat,rightLong);
        PlaneOfLocation plane = new PlaneOfLocationClass(left, right);
        currentArea = new AreaClass(name, plane);

        System.out.printf((AREA_CREATED) + "%n", name);

        return currentArea;
    }

    /**
     * Serialize the current area to disk using Java serialization.
     *
     * @param currentArea area to save (may be null)
     */
    private static void saveCurrentArea(AreaClass currentArea) {
        if (currentArea == null) return;

        String fileName = currentArea.getName().toLowerCase().replace(" ", "_") + ".ser";

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(currentArea);
        }
        catch (IOException e) {
            System.err.printf((ERROR_SAVING_AREA) + "%n", e.getMessage());

        }
    }

    /**
     * Save command wrapper that prints messages and checks preconditions.
     *
     * @param currentArea current area (may be null)
     */
    private static void processSave(AreaClass currentArea){
        if (currentArea == null) {
            System.out.println(SYSTEM_BOUNDS_NOT_DEFINED);
        }
        else {
            saveCurrentArea(currentArea);
            System.out.printf((AREA_SAVED) + "%n", currentArea.getName());
        }
    }

    /**
     * Load an area from disk.
     *
     * @pre line.length >= 2
     * @param line tokenized command line
     * @return loaded AreaClass or null on failure
     */
    private static AreaClass processLoad(String[] line) {
        // reconstruct name from tokens
        StringBuilder nameBuilder = new StringBuilder();

        for (int i = 1; i < line.length; i++) {
            nameBuilder.append(line[i]);
            if (i < line.length - 1) nameBuilder.append(" ");
        }

        String areaName = nameBuilder.toString();
        String fileName = areaName.toLowerCase().replace(" ", "_") + ".ser";

        File file = new File(fileName);

        if (!file.exists()) {
            System.out.printf((AREA_DOES_NOT_EXIST) + "%n", areaName);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            AreaClass loadedArea = (AreaClass) ois.readObject();

            System.out.printf((AREA_LOADED) + "%n", loadedArea.getName());

            return loadedArea;
        }
        catch (IOException | ClassNotFoundException e) {
            System.err.printf((ERROR_SAVING_AREA) + "%n", e.getMessage());
            return null;
        }
    }

    /**
     * Handle 'service' command: parse service parameters and call area.addService.
     *
     * @pre line.length >= 7 && currentArea != null
     * @param line tokenized command line
     * @param currentArea the currently loaded area
     */
    private static void processService(String[] line, Area currentArea) {
        ServiceType type;
        String StringType = line[1].toLowerCase();

        type = switch (StringType) {
            case "eating" -> ServiceType.eating;
            case "lodging" -> ServiceType.lodging;
            case "leisure" -> ServiceType.leisure;
            default -> null;
        };

        long lat = Long.parseLong(line[2]);
        long lon = Long.parseLong(line[3]);
        float price = Float.parseFloat(line[4]);
        float value = Float.parseFloat(line[5]);

        // reassemble service name (may contain spaces)
        StringBuilder name = new StringBuilder();
        name.append(line[6]);

        for (int i = 7; i < line.length; i++) {
            name.append(" ");
            name.append(line[i]);
        }

        String n = name.toString();

        try {
            currentArea.addService(type, lat, lon, price, value, n);
            System.out.println(StringType + " " + n + " added.");
        }
        catch (InvalidServiceException e) {
            System.out.println(INVALID_SERVICE_TYPE);
        }
        catch (LocationOutOfBoundsException e) {
            System.out.println(INVALID_LOCATION);
        }
        catch (InvalidMenuPriceException e) {
            System.out.println(INVALID_MENU_PRICE);
        }
        catch (InvalidRoomPriceException e) {
            System.out.println(INVALID_ROOM_PRICE);
        }
        catch (InvalidTicketPriceException e) {
            System.out.println(INVALID_TICKET_PRICE);
        }
        catch (InvalidDiscountPriceException e) {
            System.out.println(INVALID_DISCOUNT_PRICE);
        }
        catch (InvalidCapacityException e) {
            System.out.println(INVALID_CAPACITY);
        }
        catch (ExistingServiceException e) {
            // if a service with the same name exists, print the canonical cased name
            n = currentArea.getServiceCasedName(n.toLowerCase());
            System.out.printf((SERVICE_ALREADY_EXISTS) + "%n", n);
        }
    }

    /**
     * Handle 'services' command: list all services by insertion order.
     *
     * @pre currentArea.listServicesByInsertion() != null
     * @param currentArea the current area
     */
    private static void processServices(Area currentArea) {
        if (currentArea == null) {
            System.out.println(SYSTEM_BOUNDS_NOT_DEFINED);
            return;
        }
        try {
            Iterator<ServiceClass> it = currentArea.listServicesByInsertion();
            while (it.hasNext()) {
                ServiceClass s = it.next();
                System.out.printf(currentArea.getServiceName(s) + ": " + currentArea.getServiceType(s) +
                        " (%.0f" + "," + " %.0f).\n", currentArea.getServiceLatitude(s), currentArea.getServiceLongitude(s));
            }
        }
        catch (NoServicesYetException e) {
            System.out.println(NO_SERVICES_YET);
        }
    }

    /**
     * Handle 'student' command: read next three lines (name, country, lodging) from input.
     *
     * @pre in != null && line.length >= 2 && currentArea != null
     * @param in Scanner to read additional lines
     * @param currentArea current area
     * @param line tokenized command line
     */
    private static void processStudent(Scanner in, Area currentArea, String[] line) {

        StudentType type;
        String typeString = line[1].trim().toLowerCase();

        type = switch (typeString) {
            case "bookish" -> StudentType.bookish;
            case "outgoing" -> StudentType.outgoing;
            case "thrifty" -> StudentType.thrifty;
            default -> null;
        };

        String name = in.nextLine();
        String country = in.nextLine().toLowerCase();
        String lodgingName = in.nextLine();

        try {
            currentArea.addStudent(type, name, country, lodgingName);
            System.out.printf((STUDENT_ADDED) + "%n", name);
        }
        catch (InvalidStudentTypeException e) {
            System.out.println(INVALID_STUDENT_TYPE);
        }
        catch (NonExistingLodgingServiceException e) {
            System.out.printf((LODGING_DOES_NOT_EXIST) + "%n", lodgingName);
        }
        catch (FullLodgingServiceException e) {
            System.out.printf((LODGING_IS_FULL) + "%n", lodgingName);
        }
        catch (ExistingStudentException e) {
            name = currentArea.getStudentCasedName(name.toLowerCase());
            System.out.printf((STUDENT_ALREADY_EXISTS) + "%n", name);
        }
    }

    private static String buildString(int startIdx, String[] line){
        StringBuilder sb = new StringBuilder();

        for (int i = startIdx; i < line.length; i++) {
            if (i > startIdx) sb.append(" ");
            sb.append(line[i]);
        }

        return sb.toString();
    }
    /**
     * Handle 'leave' command: remove a student by name (possibly multi-token).
     *
     * @pre line.length >= 2 && currentArea != null
     * @param line tokenized command line
     * @param currentArea current area
     */
    private static void processLeave(String[] line, Area currentArea) {
        String name = currentArea.getStudentCasedName(buildString(1,line).toLowerCase());
        if (name == null) name = buildString(1,line);

        try {
            currentArea.removeStudent(name.toLowerCase());
            System.out.printf((STUDENT_LEFT) + "%n", name);
        }
        catch (NonExistingStudentException e) {
            System.out.printf((STUDENT_DOES_NOT_EXIST) + "%n", name);
        }
    }

    /**
     * Handle 'students' command: either "students all" or "students <country>".
     *
     * @pre line.length >= 2 && currentArea != null
     * @param line tokenized command line
     * @param currentArea current area
     */
    private static void processStudents(String[] line, Area currentArea) {
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(line[1]);

        for (int i = 2; i < line.length; i++) {
            nameBuilder.append(" ");
            nameBuilder.append(line[i]);
        }

        String arg = nameBuilder.toString();

        if(currentArea == null){
            System.out.println(SYSTEM_BOUNDS_NOT_DEFINED);
            return;
        }

        try {
            Iterator<StudentClass> it;

            if (arg.equalsIgnoreCase("all")) {
                it = currentArea.listAllStudentsAlphabetically();
            } else {
                it = currentArea.listStudentsByCountry(arg.toLowerCase());
            }

            while (it.hasNext()) {
                StudentClass s = it.next();
                System.out.printf("%s: %s at %s.%n",
                        currentArea.getStudentName(s),
                        currentArea.getStudentType(s),
                        currentArea.getStudentLocationName(s));
            }

        }
        catch (NonExistingStudentException e) {
            System.out.println(NO_STUDENTS_YET);

        }
        catch (NonExistingStudentFromCountryException e) {
            System.out.printf((NO_STUDENTS_FROM_COUNTRY) + "%n", arg);
        }
    }

    /**
     * Handle 'go' command. Reads the destination service name from the next input line.
     *
     * @pre in != null && line.length >= 2 && currentArea != null
     * @param in scanner to read the location line
     * @param currentArea current area
     * @param line tokenized command line (first token is "go" and rest are student name)
     */
    private static void processGo(Scanner in, AreaClass currentArea, String[] line) {
        String studentName = currentArea.getStudentCasedName(buildString(1,line).toLowerCase());

        if(studentName == null) studentName = buildString(1,line);

        String locationNamein = in.nextLine().trim();
        String locationName = currentArea.getServiceCasedName(locationNamein.toLowerCase());

        if(locationName == null) locationName = locationNamein;

        try {
            currentArea.goToLocation(studentName.toLowerCase(), locationName.toLowerCase());
            System.out.printf((GO_SUCCESS) + "%n", studentName, locationName);

        }
        catch (UnknownLocationException e) {
            System.out.printf((UNKNOWN_LOCATION_FMT) + "%n", locationName);

        }
        catch (NonExistingStudentException e) {
            System.out.printf((STUDENT_DOES_NOT_EXIST) + "%n", studentName);

        }
        catch (InvalidServiceException e) {
            System.out.printf((LOCATION_NOT_VALID_FMT) + "%n", locationName);

        }
        catch (AlreadyThereException e) {
            System.out.println(ALREADY_THERE);

        }
        catch (EatingServiceFullException e) {
            System.out.printf((EATING_FULL_FMT) + "%n", locationName);

        }
        catch (DistractedStudentException e) {
            String success = String.format(GO_SUCCESS, studentName, locationName);
            String warning = String.format(GO_DISTRACTED, studentName);
            System.out.println(success + " " + warning);
        }
    }

    /**
     * Handle 'move' command to change a student's home. Reads lodging name from next input line.
     *
     * @pre in != null && line.length >= 2 && currentArea != null
     * @param in scanner to read lodging name
     * @param currentArea current area
     * @param line tokenized command line (first token is "move" and rest are student name)
     */
    private static void processMove(Scanner in, AreaClass currentArea, String[] line) {
        String studentName = currentArea.getStudentCasedName(buildString(1,line).toLowerCase());

        if (studentName == null) studentName = buildString(1,line);

        String lodgingName = in.nextLine().trim();

        if (currentArea.getServiceCasedName(lodgingName) != null) lodgingName = currentArea.getServiceCasedName(lodgingName);

        try {
            currentArea.moveStudentHome(studentName.toLowerCase(), lodgingName.toLowerCase());
            System.out.printf((MOVE_SUCCESS) + "%n", lodgingName, studentName, studentName);

        }
        catch (NonExistingLodgingServiceException e) {
            System.out.printf((LODGING_DOES_NOT_EXIST) + "%n", lodgingName);

        }
        catch (NonExistingStudentException e) {
            System.out.printf((STUDENT_DOES_NOT_EXIST) + "%n", studentName);

        }
        catch (AlreadyAtHomeException e) {
            System.out.printf((THAT_IS_HOME) + "%n", studentName);

        }
        catch (FullLodgingServiceException e) {
            System.out.printf((LODGING_IS_FULL) + "%n", lodgingName);

        }
        catch (MoveNotAcceptableException e) {
            System.out.printf((MOVE_NOT_ACCEPTABLE) + "%n", studentName);
        }
    }

    /**
     *
     * Handle 'users' command: list students in a service.
     *
     * order = '>' for forward order, '<' for reverse order
     *
     * @pre line.length >= 3 && currentArea != null
     * @param line tokenized command line
     * @param currentArea current area
     */
    private static void processUsers(String[] line, AreaClass currentArea) {
        String orderStr = line[1].trim();
        char order = orderStr.charAt(0);

        String serviceName = currentArea.getServiceCasedName(buildString(2,line).toLowerCase());

        if(serviceName == null) serviceName = buildString(2,line);

        try {
            if (order== '>') {
                // forward iterator
                Iterator<StudentClass> it = currentArea.listStudentsInService(order, serviceName.toLowerCase());

                while (it.hasNext()) {
                    StudentClass s = it.next();
                    System.out.printf("%s: %s%n", currentArea.getStudentName(s), currentArea.getStudentType(s));
                }
            } else {
                // reverse iteration via two-way iterator - print in reverse order
                TwoWayIterator<StudentClass> it = (TwoWayIterator<StudentClass>) currentArea.listStudentsInService(order, serviceName.toLowerCase());

                while (it.hasPrevious()) {
                    StudentClass s = it.previous();
                    System.out.printf("%s: %s%n", currentArea.getStudentName(s), currentArea.getStudentType(s));
                }
            }
        }
        catch (NonExistingOrderException e) {
            System.out.println(THIS_ORDER_NOT_EXISTS);
        }
        catch (NonExistingServiceException e) {
            System.out.printf((SERVICE_DOES_NOT_EXIST) + "%n", serviceName);
        }
        catch (ServiceDoesNotControlStudentsException e) {
            System.out.printf((SERVICE_NOT_CONTROL) + "%n", serviceName);
        }
        catch (NoStudentsOnServiceException e) {
            System.out.printf((NO_STUDENTS_ON_SERVICE) + "%n", serviceName);
        }
    }

    /**
     * Handle 'where' command: print student's current location and coordinates.
     *
     * @pre line.length >= 2 && currentArea != null
     * @param line tokenized command line (student name parts)
     * @param currentArea current area
     */
    private static void processWhere(String[] line, AreaClass currentArea) {
        String studentName = currentArea.getStudentCasedName(buildString(1,line).toLowerCase());
        if(studentName == null) studentName = buildString(1,line);
        String lowername = studentName.toLowerCase();
        try {
            String locName = currentArea.getStudentLocationNameByName(lowername);
            String locType = currentArea.getStudentLocationTypeByName(lowername);
            long lat = currentArea.getStudentLocationLatitudeByName(lowername);
            long lon = currentArea.getStudentLocationLongitudeByName(lowername);

            System.out.printf("%s is at %s %s (%d, %d).%n",
                    studentName, locName, locType, lat, lon);
        }
        catch (NonExistingStudentException e) {
            System.out.printf((STUDENT_DOES_NOT_EXIST) + "%n", studentName);
        }
    }

    /**
     * Handle 'visited' command: list visited services for a given student.
     *
     * @pre line.length >= 2 && currentArea != null
     * @param line tokenized command line (student name)
     * @param currentArea current area
     */
    private static void processVisited(String[] line, AreaClass currentArea) {
        String studentName = currentArea.getStudentCasedName(buildString(1,line).toLowerCase());

        if(studentName == null) studentName = buildString(1,line);

        try {
            Iterator<ServiceClass> it = currentArea.listVisited(studentName.toLowerCase());
            while (it.hasNext()) {
                ServiceClass s = it.next();
                System.out.println(currentArea.getServiceName(s));
            }
        }
        catch (NonExistingStudentException e) {
            System.out.printf((STUDENT_DOES_NOT_EXIST) + "%n", studentName);
        }
        catch (StudentIsThriftyException e) {
            System.out.printf((STUDENT_IS_THRIFTY) + "%n", studentName);
        }
        catch (NoVisitedLocationsYetException e) {
            System.out.printf((STUDENT_NO_VISITED) + "%n", studentName);
        }
    }

    /**
     * Handle 'star' command: add an evaluation to a service.
     *
     * @pre line.length >= 3 && in != null %% currentArea != null
     * @param line tokenized command line
     * @param in scanner to read description
     * @param currentArea current area
     */
    private static void processStar(String[] line, Scanner in, AreaClass currentArea) {
        int stars = Integer.parseInt(line[1]);

        String serviceName = buildString(2,line);
        String description = in.nextLine();

        try {
            currentArea.starService(stars, serviceName.toLowerCase(), description);
            System.out.println(EVALUATION_REGISTERED);

        }
        catch (InvalidEvaluationException e) {
            System.out.println(INVALID_EVALUATION);
        }
        catch (NonExistingServiceException e) {
            System.out.printf((SERVICE_DOES_NOT_EXIST) + "%n", serviceName);
        }
    }

    /**
     * Handle 'ranking' command: print services sorted by evaluation.
     *
     * @pre currentArea != null
     * @param currentArea current area
     */
    private static void processRanking(AreaClass currentArea) {
        try {
            Iterator<ServiceClass> it = currentArea.listServicesByEvaluation();
            System.out.println(SERVICES_SORTED);

            while (it.hasNext()) {
                ServiceClass s = it.next();
                System.out.printf("%s: %d%n",
                        currentArea.getServiceName(s),
                        currentArea.getServiceAverage(s));
            }
        }
        catch (NoServicesYetException e){
            System.out.println(NO_SERVICES);
        }
    }

    /**
     *
     * Handle 'ranked' command: find services of given type and stars closest to a student.
     *
     * @pre line.length >= 4 && currentArea != null
     * @param line tokenized command line
     * @param currentArea current area
     */
    private static void processRanked(String[] line, AreaClass currentArea) {
        String typeStr = line[1].trim().toLowerCase();
        int stars = Integer.parseInt(line[2].trim());

        String studentName = currentArea.getStudentCasedName(buildString(3,line).toLowerCase());

        if(studentName == null) studentName = buildString(3,line);

        try {
            Iterator<ServiceClass> it = currentArea.findServicesByRating(typeStr, stars, studentName.toLowerCase());
            System.out.printf((RANKED_HEADER) + "%n", typeStr, stars);

            while (it.hasNext()) {
                ServiceClass s = it.next();
                System.out.println(currentArea.getServiceName(s));
            }
        }
        catch (InvalidServiceException e) {
            System.out.println(INVALID_SERVICE_TYPE);
        }
        catch (InvalidEvaluationException e) {
            System.out.println(INVALID_STARS);
        }
        catch (NonExistingStudentException e) {
            System.out.printf((STUDENT_DOES_NOT_EXIST) + "%n", studentName);
        }
        catch (NoServicesOfTheTypeException e) {
            System.out.printf((NO_SERVICES_OF_TYPE) + "%n", typeStr);
        }
        catch (NoServicesWithAverageException e) {
            System.out.printf((NO_SERVICES_WITH_AVERAGE) + "%n", typeStr);
        }
    }

    /**
     * Handle 'tag' command: list services whose evaluations contain a tag.
     *
     * @pre line.length >= 2 && currentArea != null
     * @param line tokenized command line (tag may contain spaces)
     * @param currentArea current area
     */
    private static void processTag(String[] line, AreaClass currentArea) {
        String tag = buildString(1,line);

        try {
            Iterator<ServiceClass> it = currentArea.getServicesWithTag(tag);

            while (it.hasNext()) {
                ServiceClass s = it.next();
                System.out.println(currentArea.getServiceType(s) + " " + currentArea.getServiceName(s));
            }
        }
        catch (NoServicesWithTagException e){
            System.out.println(NO_SERVICES_WITH_TAG);
        }
    }

    /**
     * Handle 'find' command: find the most relevant service for a student of a given type.
     *
     * @pre line.length >= 2 && in != null && currentArea != null
     * @param line tokenized command line (student name)
     * @param in  scanner to read service type
     * @param currentArea current area
     */
    private static void processFind(String[] line, Scanner in, AreaClass currentArea) {
        String studentName = buildString(1,line).trim();
        String serviceType = in.nextLine().trim();

        try {
            ServiceClass service = currentArea.findRelevantServiceForStudent(studentName.toLowerCase(), serviceType);
            System.out.println(currentArea.getServiceName(service));
        }
        catch (InvalidServiceException ex) {
            System.out.println(INVALID_SERVICE_TYPE);
        }
        catch (NonExistingStudentException ex) {
            System.out.printf((STUDENT_DOES_NOT_EXIST) + "%n", studentName);
        }
        catch (NoEatingTypeServiceException ex) {
            System.out.printf((NO_SERVICES_OF_TYPE) + "%n", "eating");
        }
        catch (NoLodgingTypeServiceException ex) {
            System.out.printf((NO_SERVICES_OF_TYPE) + "%n", "lodging");
        }
        catch (NoLeisureTypeServiceException ex) {
            System.out.printf((NO_SERVICES_OF_TYPE) + "%n", "leisure");
        }
    }
}