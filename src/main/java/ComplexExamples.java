import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.out;

public class ComplexExamples {

    static class Person {
        final int id;

        final String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person person)) return false;
            return getId() == person.getId() && getName().equals(person.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getName());
        }

        @Override
        public String toString() {
            //  Raw data:  Amelia (5)
            return name + " (" + id + ")";
        }
    }

    private static Person[] RAW_DATA = new Person[]{
            new Person(0, "Harry"),
            new Person(0, "Harry"), // дубликат
            new Person(1, "Harry"), // тёзка
            new Person(2, "Harry"),
            new Person(3, "Emily"),
            new Person(4, "Jack"),
            new Person(4, "Jack"),
            new Person(5, "Amelia"),
            new Person(5, "Amelia"),
            new Person(6, "Amelia"),
            new Person(7, "Amelia"),
            new Person(8, "Amelia"),
    };
                /*  Raw data:

                0 - Harry
                0 - Harry
                1 - Harry
                2 - Harry
                3 - Emily
                4 - Jack
                4 - Jack
                5 - Amelia
                5 - Amelia
                6 - Amelia
                7 - Amelia
                8 - Amelia

                **************************************************
             */

    public static void main(String[] args) {

        out.println("Raw data: \n");
        for (Person person : RAW_DATA) {
            out.println(person.id + " - " + person.name);
        }
        out.println("\n**************************************************\n");

        out.println("Duplicate filtered, grouped by name, sorted by name and id:");
        sortedByNameAndIdGroupedByName(RAW_DATA);

        //Убрать дубликаты, отсортировать по идентификатору, сгруппировать по имени
        out.println("\n Duplicate filtered, grouped by name, sorted id:");
        sortedByIdGroupedByName(RAW_DATA);

        out.println("\n**************************************************\n");

        int[] array = {3, 4, 2, 7};
        int sum = 10;
        out.print("Вычисляем пару числе:");
        extracted(array, sum);
        out.printf(" которые дают сумму = %d, из массива: %s", sum, Arrays.toString(array));

        out.println("\n**************************************************\n");
        out.println("Реализовать функцию нечеткого поиска:");

        out.println(fuzzySearch("car", "ca6$$#_rtwheel")); // true
        out.println(fuzzySearch("cwhl", "cartwheel")); // true
        out.println(fuzzySearch("cwhee", "cartwheel")); // true
        out.println(fuzzySearch("cartwheel", "cartwheel")); // true
        out.println(fuzzySearch("cwheeel", "cartwheel")); // false
        out.println(fuzzySearch("lw", "cartwheel")); // false

    }


    /*
     Task2
         [3, 4, 2, 7], 10 -> [3, 7] - вывести пару именно в скобках, которые дают сумму - 10
      */
    private static void extracted(int[] array, int sum) {
        IntStream.range(0, array.length)
                .forEach(i -> IntStream.range(i, array.length)
                        .filter(j -> (i != j) && ((array[i] + array[j]) == sum))
                        .forEach(j -> out.printf("[%d, %d]", array[i], array[j])));
    }

    /*
Task1
   Убрать дубликаты, отсортировать по идентификатору, сгруппировать по имени
   Что должно получиться
       Key: Amelia
       Value:4
       Key: Emily
       Value:1
       Key: Harry
       Value:3
       Key: Jack
       Value:1
*/
    private static void sortedByIdGroupedByName(Person[] person) {

        List<Person> personList = Arrays.asList(person);
        Map<String, Long> map = personList.stream()
                .distinct()
                .sorted(Comparator.comparingInt(Person::getId))
                .collect(Collectors.groupingBy(Person::getName, Collectors.counting()));
        map.forEach((key, value) -> out.println("Key: " + key + " \n" + "Value:" + value));

    }

            /*        Duplicate filtered, grouped by name, sorted by name and id:
                    Amelia:
                    1 - Amelia (5)
                    2 - Amelia (6)
                    3 - Amelia (7)
                    4 - Amelia (8)
                    Emily:
                    1 - Emily (3)
                    Harry:
                    1 - Harry (0)
                    2 - Harry (1)
                    3 - Harry (2)
                    Jack:
                    1 - Jack (4)
                    */

    private static void sortedByNameAndIdGroupedByName(Person[] person) {

        Map<String, TreeSet<Person>> treeSetMap = Arrays.stream(person)
                .collect(Collectors.groupingBy(Person::getName,
                        Collectors.toCollection(() -> new TreeSet<>((Comparator.comparingInt(Person::getId))))));

        for (String key : treeSetMap.keySet()) {
            out.println(key + ":");
            TreeSet<Person> treeSet = treeSetMap.get(key);
            int count = 1;
            for (Person persons : treeSet) {
                out.println(count + " - " + persons);
                count++;
            }
        }
    }

            /*
            Task3
                Реализовать функцию нечеткого поиска

                        fuzzySearch("car", "ca6$$#_rtwheel"); // true
                        fuzzySearch("cwhl", "cartwheel"); // true
                        fuzzySearch("cwhee", "cartwheel"); // true
                        fuzzySearch("cartwheel", "cartwheel"); // true
                        fuzzySearch("cwheeel", "cartwheel"); // false
                        fuzzySearch("lw", "cartwheel"); // false
             */

    private static boolean fuzzySearch(String str1, String str2) {

        if (str1.isEmpty()) {
            return true;
        }
        int i = 0;
        for (int j = 0; j < str2.length(); ++j) {
            char s = str1.charAt(i);
            char t = str2.charAt(j);
            if (s == t) {
                ++i;
                if (i == str1.length()) {
                    return true;
                }
            }
        }
        return false;
    }


}
