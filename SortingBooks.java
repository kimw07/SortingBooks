import java.time.Year;
import java.util.*;

public class Main {

    public static class AdjacentCity implements Comparable<AdjacentCity> {
        private final String target;
        private final int distance;

        public AdjacentCity(String city, int dist) {
            this.target = city;
            this.distance = dist;
        }

        public int getDistance() {
            return distance;
        }

        public String getTarget() {
            return target;
        }

        @Override
        public boolean equals(Object o) {
            //checks to see if o is an instance of AdjacentCity
            //if this.target == o.target && this.distance == o.distance return true
            if (o instanceof AdjacentCity) {
                if (this.target == ((AdjacentCity) o).target && this.distance == ((AdjacentCity) o).distance) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int result;
            result = target.hashCode() + distance * 31;
            return result;
        }

        @Override
        public int compareTo(AdjacentCity o) {
            int dist = this.distance - o.distance;
            return dist;
        }

        @Override
        public String toString() {
            return target + ":" + distance;
        }

    }

    public static class ShortestRouteFinder {

        private final Map<String, Set<AdjacentCity>> connections =
                new HashMap<>();

        /**
         * Adds a bidirectional "edge" between city {@code c1} and city
         * {code c2} with distance {@code d}.
         *
         * @param c1 the first city.
         * @param c2 the second city.
         * @param d  the distance between the two cities.
         * @return a shortest route finder reference.
         */
        public ShortestRouteFinder route(String c1, String c2, int d) {
            // this is an undirected network, needs to map both directions
            connections.computeIfAbsent(c1,
                    k -> new HashSet<>()).add(new AdjacentCity(c2, d));
            connections.computeIfAbsent(c2,
                    k -> new HashSet<>()).add(new AdjacentCity(c1, d));
            return this;
        }

        public Map<String, Integer> computeShortestRoutesFrom(String startCity) {
            Map<String, Integer> distance = new HashMap<>();
            PriorityQueue<AdjacentCity> q = new PriorityQueue<>();
            q.add(new AdjacentCity(startCity, 0));
            while (!q.isEmpty()) {
                AdjacentCity current = q.remove();
                int d;
                String t = current.getTarget();
                if (!distance.containsKey(current.target)) {
                    d = current.getDistance();
                    //maps the current target to d in distances
                    distance.put(t, d);
                    //adds a new q for every c in current.target
                    for (AdjacentCity c : connections.get(current.target)) {
                        q.add(new AdjacentCity(c.getTarget(), d + c.getDistance()));
                    }
                }
            }//while
            return distance;
        }//computeShortestRoutesFrom
    }//shortestRouteFinder

    public static class Book implements Comparable<Book> {
        private final String author, title;
        private final Year releaseYear;

        public Book(String author, String title, Year yr) {
            this.author = author;
            this.title = title;
            this.releaseYear = yr;
        }

        @Override
        public int compareTo(Book o) {
            if (this.releaseYear.isBefore(o.releaseYear)) {
                //if this releaseYear is after book release return 1
                return -1;
            }
            if (this.releaseYear.isAfter(o.releaseYear)) {
                //if this releaseYear is before book release return -1
                return 1;
            } else return 0;
        }

        @Override
        public String toString() {
            return title + " (" + releaseYear.toString() + ") by " + author;
        }
    }

    public static <T extends Comparable<T>> List<T> mergeSort(List<T> a) {
        if (a.size() <= 1) {
            return a;
        }
        int midIndex = a.size() / 2;
        List<T> left = new ArrayList<>();
        List<T> right = new ArrayList<>();
        //takes the left half of a and makes left list
        for (int i = 0; i < midIndex; i++) {
            left.add(a.get(i));
        }
        //takes the right half of a and makes right list
        for (int i = midIndex; i < a.size(); i++) {
            right.add(a.get(i));
        }
        left = mergeSort(left);
        right = mergeSort(right);
        return merge(left, right);
    }//mergeSort

    public static <T extends Comparable<T>> List<T> merge(List<T> left, List<T> right) {
        int leftFirst = 0;
        int rightFirst = 0;
        int mergedPos = 0;
        List<T> mergedList = new ArrayList<>();
        while (leftFirst < left.size() && rightFirst < right.size()) {
            //if leftFirst is smaller than rightFirst, leftFirst is added to mergedList at position mergedPos
            if (left.get(leftFirst).compareTo(right.get(rightFirst)) == -1) {
                mergedList.add(mergedPos, left.get(leftFirst));
                leftFirst++;
            }
            //adds rightFirst to mergedList at position mergedPos
            else {
                mergedList.add(mergedPos, right.get(rightFirst));
                rightFirst++;
            }
            mergedPos++;
        }
        while (leftFirst < left.size()) {
            mergedList.add(mergedPos, left.get(leftFirst));
            leftFirst++;
            mergedPos++;
        }
        while (rightFirst < right.size()) {
            mergedList.add(mergedPos, right.get(rightFirst));
            rightFirst++;
            mergedPos++;
        }
        return mergedList;
    }//merge

    public static void main(String[] args) {
        System.out.println("Problem #1: generic sorting");

        List<Integer> a = Arrays.asList(3, 4, 0, 1);
        List<Integer> res = mergeSort(a);
        System.out.println("sorted numbers: " + res);

        List<Book> books = Arrays.asList(
                new Book("harper lee", "to kill a mockingbird", Year.of(1960)), //1960
                new Book("j.d. salinger", "catcher in the rye", Year.of(1951)), //1951
                new Book("fitzgerald", "the great gatsby", Year.of(1925)), //1925
                new Book("w. strieber, j. kunetka", "war day", Year.of(1984))); //1984

        System.out.println("sorted books: ");
        List<Book> sortedBooks = mergeSort(books);
        for (Book b : sortedBooks) {
            System.out.print("\t");
            System.out.println(b);
        }

        System.out.println();
        System.out.println("Problem #2: shortest paths");
        ShortestRouteFinder rb = new ShortestRouteFinder()
                .route("pendleton", "pierre", 2)
                .route("pendleton", "pueblo", 8)
                .route("pendleton", "phoenix", 4)
                .route("phoenix", "pueblo", 3)
                .route("phoenix", "peoria", 4)
                .route("phoenix", "pensacola", 5)
                .route("phoenix", "pittsburgh", 10)
                .route("pueblo", "pierre", 3)
                .route("pueblo", "peoria", 3)
                .route("pittsburgh", "peoria", 5)
                .route("pittsburgh", "pensacola", 4)
                .route("pittsburgh", "princeton", 2)
                .route("pensacola", "princeton", 5);

        Map<String, Integer> shortestRoutesFrom =
                rb.computeShortestRoutesFrom("pendleton");//pendleton

        System.out.println("shortest distance to peoria from pendleton is: " +
                shortestRoutesFrom.get("peoria"));
    }
}
