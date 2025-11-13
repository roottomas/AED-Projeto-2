/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import dataStructures.*;
import dataStructures.exceptions.NoSuchElementException;
import system.exceptions.*;

import java.io.Serializable;

/**
 * Concrete implementation of the Area interface.
 * AreaClass manages students and services within a bounded plane. It stores
 * several auxiliary data structures to support queries by insertion order,
 * rating buckets and price-sorted structures for thrifty students.
 */
@SuppressWarnings("unchecked")
public class AreaClass implements Area, Serializable {
    private static final long serialVersionUID = 0L;

    private final String name;

    // Students
    private SortedMap<String,StudentClass> alphOrderStudents;
    private List<StudentClass> students;
    private Map<String,StudentClass> studentsByName;
    private Map<String,SinglyLinkedList<StudentClass>> studentsByCountry;
    // Services
    private SortedList<ServiceClass>[] servicesByPrice;
    private List<ServiceClass> servicesByInsertion;
    private List<ServiceClass>[][] servicesByRating;
    private List<ServiceClass>[] servicesByEvaluation;
    private Map<String,ServiceClass> servicesByName;

    // Location
    private final PlaneOfLocation locationOfArea;

    /**
     * Create a new AreaClass with the given name and plane.
     *
     * @param name area name
     * @param locationOfArea bounding box (plane)
     */
    public AreaClass(String name, PlaneOfLocation locationOfArea) {
        this.name = name;

        // Initialize student structures
        alphOrderStudents = new AVLSortedMap<>();
        students = new SinglyLinkedList<>();
        studentsByName = new SepChainHashTable<>(50);
        studentsByCountry = new SepChainHashTable<>(50);
        // Initialize service lists
        servicesByPrice = new SortedDoublyLinkedList[3];
        for(int i = 0; i < 3; i++){
            servicesByPrice[i]=new SortedDoublyLinkedList<>(new PriceComparator());
        }

        servicesByInsertion = new SinglyLinkedList<>();
        servicesByName = new ClosedHashTable<>(100);
        // rating buckets: 5 possible averages (1..5) and 3 service types
        servicesByRating = new SinglyLinkedList[5][3];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                servicesByRating[i][j] = new SinglyLinkedList<>();
            }
        }

        // services grouped only by evaluation (to list by evaluation)
        servicesByEvaluation = new SinglyLinkedList[5];
        for (int i = 0; i < 5; i++) {
            servicesByEvaluation[i] = new SinglyLinkedList<>();
        }

        this.locationOfArea = locationOfArea;
    }

    /**
     * Get area name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /*
     * Internal helper predicates
     */
    private boolean hasServices() {
        return !servicesByInsertion.isEmpty();
    }

    private boolean serviceIsOfInvalidType(ServiceType t) {
        return !(ServiceType.eating.equals(t) || ServiceType.lodging.equals(t) || ServiceType.leisure.equals(t));
    }

    private boolean studentIsOfInvalidType(StudentType t) {
        return !(StudentType.thrifty.equals(t) || StudentType.bookish.equals(t) || StudentType.outgoing.equals(t));
    }

    private boolean serviceExists(String name) {
        return getServiceByName(name) != null;
    }

    private boolean studentExists(String name) {
        return getStudentByName(name) != null;
    }

    /*
     * Creational helpers
     */
    private ServiceClass createService(ServiceType type, LocationClass loc, float price, float value, String name) {
        return switch (type) {
            case eating -> new EatingServiceClass(loc, price, (int) value, type, name);
            case lodging -> new LodgingServiceClass(loc, price, (int) value, type, name);
            case leisure -> new LeisureServiceClass(loc, price, value, type, name);
        };
    }

    private StudentClass createStudent(StudentType type, String name, String country, LodgingServiceClass home) {
        return switch (type) {
            case bookish -> new BookishStudentClass(name, country, home, type);
            case outgoing -> new OutgoingStudentClass(name, country, home, type);
            case thrifty -> new ThriftyStudentClass(name, country, home, type);
        };
    }

    /**
     * Given a list of services and a reference location, returns a list
     * containing the service(s) with the minimum distance to the location.
     *
     * @param list services to search
     * @param loc reference location
     * @return list of closest services (could be multiple if tied regarding distance)
     */
    private List<ServiceClass> findClosestServices(List<ServiceClass> list, LocationClass loc) {
        double minDistance = Double.MAX_VALUE;
        List<ServiceClass> closest = new ListInArray<>(50);

        Iterator<ServiceClass> it = list.iterator();

        while (it.hasNext()) {
            ServiceClass service = it.next();
            double dist = loc.distanceTo(service.getLocation());

            if (dist < minDistance) {
                // found strictly closer service -> reset result list
                minDistance = dist;
                closest = new ListInArray<>(50);
                closest.addLast(service);
            } else if (dist == minDistance) {
                // same distance -> tie, add to results
                closest.addLast(service);
            }
        }
        return closest;
    }

    /*
     * Name lookups
     */
    private ServiceClass getServiceByName(String name) {
        return servicesByName.get(name);
    }

    private StudentClass getStudentByName(String name) {
        return studentsByName.get(name);
    }

    /**
     * Return the canonical (cased) name of the service if it exists.
     *
     * @param name input (case-insensitive)
     * @return canonical service name or null
     */
    public String getServiceCasedName(String name) {
        ServiceClass service = getServiceByName(name);
        if (service == null) return null;
        else return service.getName();
    }

    /**
     * Return the canonical (cased) name of the student if it exists.
     *
     * @param name input (case-insensitive)
     * @return canonical student name or null
     */
    public String getStudentCasedName(String name) {
        StudentClass student = getStudentByName(name);
        if (student == null) return null;
        else return student.getName();
    }

    /**
     * Add a new service inside the area boundary.
     *
     * @pre name != null && country != null && lodgingName != null
     * @param type service type (eating, lodging, leisure)
     * @param latitude service latitude
     * @param longitude service longitude
     * @param price service price parameter (meaning depends on type)
     * @param value service value parameter (capacity, discount, etc.)
     * @param name service name (may contain spaces)
     */
    @Override
    public void addService(ServiceType type, long latitude, long longitude, float price, float value, String name)
            throws InvalidServiceException, LocationOutOfBoundsException, ExistingServiceException,
            InvalidMenuPriceException, InvalidRoomPriceException, InvalidCapacityException,
            InvalidTicketPriceException, InvalidDiscountPriceException {

        if (serviceIsOfInvalidType(type)) throw new InvalidServiceException();

        LocationClass serviceLocation = new LocationClass(latitude, longitude);
        if (!locationOfArea.contains(serviceLocation)) throw new LocationOutOfBoundsException();

        if (serviceExists(name.toLowerCase())) throw new ExistingServiceException();

        // validate range/semantics for each service type
        switch (type) {
            case eating -> {
                if (price <= 0) throw new InvalidMenuPriceException();
                if (value <= 0) throw new InvalidCapacityException();
            }
            case lodging -> {
                if (price <= 0) throw new InvalidRoomPriceException();
                if (value <= 0) throw new InvalidCapacityException();
            }
            case leisure -> {
                if (price <= 0) throw new InvalidTicketPriceException();
                if (value < 0 || value > 100) throw new InvalidDiscountPriceException();
            }
        }

        ServiceClass s = createService(type, serviceLocation, price, value, name);

        servicesByInsertion.addLast(s);
        servicesByRating[s.getEvaluationAverage() - 1][type.getIndex()].addLast(s);
        servicesByEvaluation[s.getEvaluationAverage() - 1].addLast(s);
        servicesByName.put(name.toLowerCase(), s);
        servicesByPrice[type.getIndex()].add(s);
    }

    /* --- Simple getters that delegate to Service/Student instances --- */

    public String getServiceName(ServiceClass service) {
        return service.getName();
    }

    public String getServiceType(ServiceClass service) {
        return service.getType().toString();
    }

    public double getServiceLatitude(ServiceClass service) {
        return service.getLatitude();
    }

    public double getServiceLongitude(ServiceClass service) {
        return service.getLongitude();
    }

    public int getServiceAverage(ServiceClass service) {
        return service.getEvaluationAverage();
    }

    public String getStudentName(StudentClass s) {
        return s.getName();
    }

    public String getStudentType(StudentClass s) {
        return s.getType().toString().toLowerCase();
    }

    public String getStudentLocationName(StudentClass s) {
        return s.getCurrentLocation().getName();
    }

    public String getStudentLocationNameByName(String studentName) throws NonExistingStudentException {
        StudentClass s = getStudentByName(studentName);
        if (s == null) throw new NonExistingStudentException();
        ServiceClass loc = s.getCurrentLocation();
        return loc.getName();
    }

    public String getStudentLocationTypeByName(String studentName) throws NonExistingStudentException {
        StudentClass s = getStudentByName(studentName);
        if (s == null) throw new NonExistingStudentException();
        ServiceClass loc = s.getCurrentLocation();
        return loc.getType().toString().toLowerCase();
    }

    public long getStudentLocationLatitudeByName(String studentName) throws NonExistingStudentException {
        StudentClass s = getStudentByName(studentName);
        if (s == null) throw new NonExistingStudentException();
        ServiceClass loc = s.getCurrentLocation();
        return (long) loc.getLatitude();
    }

    public long getStudentLocationLongitudeByName(String studentName) throws NonExistingStudentException {
        StudentClass s = getStudentByName(studentName);
        if (s == null) throw new NonExistingStudentException();
        ServiceClass loc = s.getCurrentLocation();
        return (long) loc.getLongitude();
    }

    /**
     * List services in insertion order.
     *
     * @return iterator over services by insertion order
     */
    @Override
    public Iterator<ServiceClass> listServicesByInsertion() throws NoServicesYetException {
        if (!hasServices()) {
            throw new NoServicesYetException();
        }
        return servicesByInsertion.iterator();
    }

    /**
     * List services ordered by evaluation average (descending).
     *
     * @return iterator over services by evaluation ranking
     */
    @Override
    public Iterator<ServiceClass> listServicesByEvaluation() throws NoServicesYetException {
        if (!hasServices()) {
            throw new NoServicesYetException();
        }

        // Returns an iterator which traverses services from the highest evaluation bucket to lowest.
        return new Iterator<>() {
            private int ratingIdx = 4;
            private Iterator<ServiceClass> bucketIt = null;

            {
                advanceToNextNonEmptyBucket();
            }

            private void advanceToNextNonEmptyBucket() {
                bucketIt = null;
                while (ratingIdx >= 0) {
                    List<ServiceClass> bucket = servicesByEvaluation[ratingIdx];

                    if (bucket != null && !bucket.isEmpty()) {
                        bucketIt = bucket.iterator();
                        return;
                    }
                    ratingIdx--;
                }
            }

            @Override
            public boolean hasNext() {
                if (bucketIt == null) return false;

                if (bucketIt.hasNext()) return true;

                ratingIdx--;

                while (ratingIdx >= 0) {
                    List<ServiceClass> bucket = servicesByEvaluation[ratingIdx];
                    if (bucket != null && !bucket.isEmpty()) {
                        bucketIt = bucket.iterator();
                        return bucketIt.hasNext();
                    }
                    ratingIdx--;
                }
                return false;
            }

            @Override
            public ServiceClass next() throws NoSuchElementException {
                if (!hasNext()) throw new NoSuchElementException();
                return bucketIt.next();
            }

            @Override
            public void rewind() {
                // not implemented for this iterator
            }
        };
    }

    /**
     * Find services of a given textual type (eating|lodging|leisure) that have
     * a particular rounded stars rating and are closest to the given student.
     *
     * @param serviceType textual type
     * @param stars evaluation stars (1..5)
     * @param studentName student name
     * @return iterator of the closest services that match the criteria
     */
    @Override
    public Iterator<ServiceClass> findServicesByRating(String serviceType, int stars, String studentName)
            throws NonExistingStudentException, InvalidServiceException, InvalidEvaluationException,
            NoServicesOfTheTypeException, NoServicesWithAverageException {

        StudentClass s = getStudentByName(studentName);
        if (students.indexOf(s) == -1) throw new NonExistingStudentException();
        ServiceType type;
        try {
            type = ServiceType.valueOf(serviceType);
        } catch (IllegalArgumentException e) {
            throw new InvalidServiceException();
        }

        if (stars < 1 || stars > 5) throw new InvalidEvaluationException();

        // ensure there are services of that type
        if(servicesByPrice[type.getIndex()].isEmpty()) throw new NoServicesOfTheTypeException();

        if (servicesByRating[stars - 1][type.getIndex()].isEmpty())
            throw new NoServicesWithAverageException();

        List<ServiceClass> list = servicesByRating[stars - 1][type.getIndex()];

        // return services closest to the student's current location
        return findClosestServices(list, s.getCurrentCoordinatesLocation()).iterator();
    }

    /**
     * Find the most relevant service (depending on student's type and service type).
     *
     * @param s student name
     * @param serviceType textual service type
     * @return the selected ServiceClass
     */
    @Override
    public ServiceClass findRelevantServiceForStudent(String s, String serviceType)
            throws InvalidServiceException, InvalidStudentException,
            NoEatingTypeServiceException, NoLodgingTypeServiceException, NoLeisureTypeServiceException {

        ServiceType type;

        switch (serviceType.toLowerCase()) {
            case "lodging" -> type = ServiceType.lodging;
            case "leisure" -> type = ServiceType.leisure;
            case "eating" -> type = ServiceType.eating;
            default -> type = null;
        }

        if (serviceIsOfInvalidType(type)) throw new InvalidServiceException();

        StudentClass student = getStudentByName(s);

        if (students.indexOf(student) == -1) throw new InvalidStudentException();

        // Thrifty students prefer the least expensive service (price-sorted structures)

        if (student.getType() == StudentType.thrifty) {
            return switch (type) {
                case eating -> {
                    if (servicesByPrice[type.getIndex()].isEmpty()) throw new NoEatingTypeServiceException();
                    yield servicesByPrice[type.getIndex()].getMin();
                }
                case lodging -> {
                    if (servicesByPrice[type.getIndex()].isEmpty()) throw new NoLodgingTypeServiceException();
                    yield servicesByPrice[type.getIndex()].getMin();
                }
                case leisure -> {
                    if (servicesByPrice[type.getIndex()].isEmpty()) throw new NoLeisureTypeServiceException();
                    yield servicesByPrice[type.getIndex()].getMin();
                }
            };
        }

        // For bookish/outgoing students: search by highest rating bucket, return first by insertion
        int typeIndex = type.getIndex();

        for (int ratingIndex = 4; ratingIndex >= 0; ratingIndex--) {
            List<ServiceClass> bucket = servicesByRating[ratingIndex][typeIndex];
            if (bucket != null && !bucket.isEmpty()) {
                Iterator<ServiceClass> it = bucket.iterator();
                if (it.hasNext()) return it.next();
            }
        }
        switch (type) {
            case eating:
                throw new NoEatingTypeServiceException();
            case lodging:
                throw new NoLodgingTypeServiceException();
            case leisure:
                throw new NoLeisureTypeServiceException();
            default:
                throw new InvalidServiceException();
        }
    }

    /**
     * Add an evaluation (stars + description) to a service.
     *
     * @pre description != null
     * @param stars stars 1..5
     * @param serviceName service name
     * @param description description text
     */
    @Override
    public void starService(int stars, String serviceName, String description)
            throws InvalidEvaluationException, NonExistingServiceException {

        if (stars < 1 || stars > 5) {
            throw new InvalidEvaluationException();
        }

        ServiceClass service = getServiceByName(serviceName);

        if (service == null) {
            throw new NonExistingServiceException();
        }

        int oldRatingIndex = service.getEvaluationAverage() - 1;
        EvaluationEntry eval = new EvaluationEntryClass(stars, description);
        service.addEvaluation(eval);

        int typeIndex = service.getType().getIndex();
        int newRatingIndex = service.getEvaluationAverage() - 1;

        // If average changed then update the buckets
        if (oldRatingIndex != newRatingIndex) {
            servicesByRating[oldRatingIndex][typeIndex].remove(servicesByRating[oldRatingIndex][typeIndex].indexOf(service));
            servicesByRating[newRatingIndex][typeIndex].addLast(service);
            servicesByEvaluation[oldRatingIndex].remove(servicesByEvaluation[oldRatingIndex].indexOf(service));
            servicesByEvaluation[newRatingIndex].addLast(service);
        }
    }

    /**
     * Add a new student to the area.
     *
     * @param type student behaviour type
     * @param name student name
     * @param country student country
     * @param lodgingName home lodging name
     */
    @Override
    public void addStudent(StudentType type, String name, String country, String lodgingName)
            throws InvalidStudentTypeException, NonExistingLodgingServiceException, ExistingStudentException, FullLodgingServiceException {
        if (studentIsOfInvalidType(type)) throw new InvalidStudentTypeException();

        // find lodging by name
        LodgingServiceClass home = null;

        for (int i = 0; i < servicesByInsertion.size(); i++) {
            ServiceClass s = servicesByInsertion.get(i);
            if (s instanceof LodgingServiceClass lodging && lodging.getName().equalsIgnoreCase(lodgingName)) {
                home = lodging;
                break;
            }
        }

        if (home == null) throw new NonExistingLodgingServiceException();
        if (studentExists(name.toLowerCase())) throw new ExistingStudentException();
        if (!home.hasFreeRooms()) throw new FullLodgingServiceException();

        // create student and register them
        StudentClass s = createStudent(type, name, country, home);
        home.addStudent(s);

        students.addLast(s);
        alphOrderStudents.put(name.toLowerCase(),s);
        studentsByName.put(name.toLowerCase(),s);
        SinglyLinkedList<StudentClass> list = studentsByCountry.get(country);
        if (list == null) {
            list = new SinglyLinkedList<>();
            studentsByCountry.put(country, list);
        }
        list.addLast(s);
    }

    /**
     * Remove a student from the area (they leave the system).
     *
     * @param name student name
     */
    @Override
    public void removeStudent(String name) throws NonExistingStudentException {
        if(alphOrderStudents.get(name) == null) throw new NonExistingStudentException();
        StudentClass s = alphOrderStudents.remove(name);
        studentsByName.remove(name);
        students.remove(students.indexOf(s));
        SinglyLinkedList<StudentClass> list = studentsByCountry.get(s.getCountry());
        list.remove(list.indexOf(s));
        if(list.isEmpty()) studentsByCountry.remove(s.getCountry());
        // remove from services they are present in
        LodgingServiceClass home = s.getHome();
        ServiceClass currentLocation = s.getCurrentLocation();
        if (currentLocation instanceof EatingServiceClass e) e.removeStudent(s);
        home.removeStudent(s);
    }

    /**
     * Make a student go to a service (eating or leisure).
     *
     * @param studentName student name
     * @param locationName service name
     */
    @Override
    public void goToLocation(String studentName, String locationName)
            throws UnknownLocationException, NonExistingStudentException, InvalidServiceException,
            AlreadyThereException, EatingServiceFullException, DistractedStudentException {

        StudentClass student = getStudentByName(studentName);
        ServiceClass service = getServiceByName(locationName);

        if (service == null) throw new UnknownLocationException();
        if (student == null) throw new NonExistingStudentException();

        ServiceClass currentService = student.getCurrentLocation();
        if (!(service.getType().equals(ServiceType.eating) || service.getType().equals(ServiceType.leisure)))
            throw new InvalidServiceException();

        if (student.getCurrentLocation().equals(service)) throw new AlreadyThereException();

        if (service instanceof EatingServiceClass && !((EatingServiceClass) service).hasFreeSeats())
            throw new EatingServiceFullException();

        // if currently at an eating service, remove student from that service
        if (currentService instanceof EatingServiceClass e) {
            e.removeStudent(student);
        }
        student.changeLocation(service);
        if (service instanceof EatingServiceClass e) {
            e.addStudent(student);
        }
    }

    /**
     * Change a student's home to another lodging service.
     *
     * @param studentName student name
     * @param lodgingName target lodging name
     */
    @Override
    public void moveStudentHome(String studentName, String lodgingName)
            throws NonExistingLodgingServiceException, NonExistingStudentException, AlreadyAtHomeException,
            FullLodgingServiceException, MoveNotAcceptableException {

        ServiceClass target = getServiceByName(lodgingName);
        if (!(target instanceof LodgingServiceClass home)) throw new NonExistingLodgingServiceException();

        StudentClass student = getStudentByName(studentName);
        if (student == null) throw new NonExistingStudentException();

        if (student.getHome().equals(home))
            throw new AlreadyAtHomeException();

        if (!home.hasFreeRooms()) throw new FullLodgingServiceException();

        // thrifty students may refuse move if new home is more expensive
        if (student instanceof ThriftyStudent tStudent && home.getPrice() >= tStudent.getHome().getPrice()) {
            throw new MoveNotAcceptableException();
        }

        ServiceClass currentService = student.getCurrentLocation();

        if (currentService instanceof EatingServiceClass e) {
            e.removeStudent(student);
        }

        LodgingServiceClass studentHome = student.getHome();
        studentHome.removeStudent(student);
        student.changeHome(home);
        home.addStudent(student);
    }

    /**
     * List locations visited by a student (for bookish/outgoing students).
     *
     * @param studentName student name
     * @return iterator of visited ServiceClass objects
     */
    @Override
    public Iterator<ServiceClass> listVisited(String studentName)
            throws NonExistingStudentException, StudentIsThriftyException, NoVisitedLocationsYetException {

        StudentClass student = getStudentByName(studentName);

        if (student == null) {
            throw new NonExistingStudentException();
        }

        if (student instanceof ThriftyStudent) {
            throw new StudentIsThriftyException();
        }

        LocationStoringStudent lStudent = (LocationStoringStudent) student;

        if (!lStudent.listVisited().hasNext()) {
            throw new NoVisitedLocationsYetException();
        }
        return lStudent.listVisited();
    }

    /**
     * List students coming from a given country.
     *
     * @pre country != null
     * @param country country name (case-insensitive)
     * @return iterator over matching students
     */
    @Override
    public Iterator<StudentClass> listStudentsByCountry(String country) throws NonExistingStudentFromCountryException {
        SinglyLinkedList<StudentClass> list = studentsByCountry.get(country);

        if (list == null || list.isEmpty()) {
            throw new NonExistingStudentFromCountryException();
        }
        return list.iterator();
    }

    /**
     * List all registered students alphabetically.
     *
     * @return iterator of students ordered alphabetically
     */
    @Override
    public Iterator<StudentClass> listAllStudentsAlphabetically() throws NonExistingStudentException {
        if (alphOrderStudents.isEmpty()) {
            throw new NonExistingStudentException();
        }
        return alphOrderStudents.values();
    }

    /**
     * List students in a service either forward ('>') or reverse ('<') order.
     *
     * @param order '>' or '<'
     * @param serviceName service name
     * @return iterator (TwoWayIterator when '<' is used)
     */
    @Override
    public Iterator<StudentClass> listStudentsInService(char order, String serviceName)
            throws NonExistingOrderException, NonExistingServiceException, ServiceDoesNotControlStudentsException,
            NoStudentsOnServiceException {
        if (!(order == '<' || order == '>')) {
            throw new NonExistingOrderException();
        }

        ServiceClass service = getServiceByName(serviceName);

        if (service == null) {
            throw new NonExistingServiceException();
        }

        boolean isEating = service instanceof EatingServiceClass;
        boolean isLodging = service instanceof LodgingServiceClass;

        if (!(isEating || isLodging)) {
            throw new ServiceDoesNotControlStudentsException();
        }

        TwoWayList<StudentClass> studentsAtService = ((StudentsStoringServiceClass) service).getStudents();

        if (studentsAtService.isEmpty()) {
            throw new NoStudentsOnServiceException();
        }

        if (order != '>') {
            TwoWayIterator<StudentClass> twoWay = studentsAtService.twoWayiterator();
            twoWay.fullForward();
            return twoWay;
        }

        return studentsAtService.iterator();
    }

    /**
     * Return an iterator of services that contain the given tag in their evaluations.
     *
     * @pre tag != null
     * @param tag tag to search for
     * @return iterator of services with the tag
     */
    @Override
    public Iterator<ServiceClass> getServicesWithTag(String tag) throws NoServicesWithTagException {
        Predicate<ServiceClass> pred = s -> {
            Evaluation eval = s.getEvaluation();
            return eval.containsTag(tag);
        };

        FilterIterator<ServiceClass> it = new FilterIterator<>(servicesByInsertion.iterator(), pred);

        if (!it.hasNext()) {
            throw new NoServicesWithTagException();
        }
        return it;
    }
}