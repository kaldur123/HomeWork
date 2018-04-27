package HomeWork;

import HomeWork.Countries.City;
import HomeWork.Countries.Country;
import HomeWork.Countries.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mysql");
        EntityManager em = entityManagerFactory.createEntityManager();

        em.getTransaction().begin();

        try {
            BufferedReader in1 = new BufferedReader(new FileReader("d:/Users.txt"));
            BufferedReader in2 = new BufferedReader(new FileReader("d:/Countries.txt"));
            BufferedReader in3 = new BufferedReader(new FileReader("d:/Cities.txt"));
            String user;
            String country;
            String city;
            Random rd = new Random();
            while ((user = in1.readLine()) != null) {
                country = in2.readLine();
                city = in3.readLine();
                User u = new User();
                u.setFullName(user);
                u.setAge(rd.nextInt(1 + 50));
                if (city != null) {
                    if (country != null) {
                        u.setCity(new City(city, new Country(country)));
                    }
                    else {
                        u.setCity(new City(city, null));
                    }
                }
                else {
                    u.setCity(null);
                }
                em.persist(u);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        em.createQuery("select u from User u", User.class).getResultList().forEach(System.out::println);


        List<Country> countries = em.createQuery("select c from Country c order by c.id desc ", Country.class)
                .getResultList();
        countries.forEach(System.out::println);


        em.createQuery("select ct from City ct order by name", City.class).getResultList().forEach(System.out::println);
        em.createQuery("select u from User u order by fullName desc ", User.class).getResultList().forEach(System.out::println);


        em.createQuery("select c from Country c where c.name like ?1 or c.name like ?2", Country.class).setParameter(1, "A%").setParameter(2, "a%").getResultList().forEach(System.out::println);


        em.createQuery("select ct from City ct where ct.name like ?1 or ct.name like ?2").setParameter(1, "%n_").setParameter(2, "%r_").getResultList().forEach(System.out::println);


        User min = em.createQuery("select u from User u where u.age = (select min(u.age) from User u)", User.class).getSingleResult();

        System.out.println(min);

        Double avg = em.createQuery("select avg(u.age) from User u", Double.class).getSingleResult();
        System.out.println(avg);


        em.createQuery("select u from User u join u.city c", User.class).getResultList().forEach(System.out::println);


        em.createQuery("select u from User u join u.city c where u.id not in (?1)", User.class).setParameter(1, Arrays.asList(2,5,9,12,13,16)).getResultList().forEach(System.out::println);




        em.createQuery("select u from User u join fetch u.city ct join ct.country", User.class).getResultList().forEach(System.out::println);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> queryUser = cb.createQuery(User.class);
        CriteriaQuery<City> queryCity = cb.createQuery(City.class);
        CriteriaQuery<Country> queryCountry = cb.createQuery(Country.class);

        Root<User> rootUser = queryUser.from(User.class);
        Root<City> rootCity = queryCity.from(City.class);
        Root<Country> rootCountry = queryCountry.from(Country.class);

        //1
        queryUser.select(rootUser);

        //2
        Expression<Integer> idExpression = rootCountry.get("id");
        queryCountry.orderBy(cb.desc(idExpression));

        //3
        Expression<String> nameExpression = rootCity.get("name");
        queryCity.orderBy(cb.asc(nameExpression));

        //4
        Expression<String> userName = rootUser.get("fullName");
        queryUser.orderBy(cb.desc(userName));

        //5
        Expression<String> countryName = rootCountry.get("name");
        Predicate countryNamePredicate1 = cb.like(cb.lower(countryName), "a%");
        queryCountry.where(countryNamePredicate1);


        //6
        Predicate cityNamePredicate1 = cb.like(nameExpression, "%n_");
        Predicate cityNamePredicate2 = cb.like(nameExpression, "%r_");
        Predicate allPredicates = cb.or(cityNamePredicate1, cityNamePredicate2);
        queryCity.where(allPredicates);

        //7
        Expression<Integer> ageExpression = rootUser.get("age");
        CriteriaQuery<Integer> cq1 = cb.createQuery(Integer.class);
        cq1.select(cb.min(cq1.from(User.class).get("age")));
        int minAge = em.createQuery(cq1).getSingleResult();
        Predicate agePredicate = cb.equal(ageExpression, minAge);
        queryUser.where(agePredicate);

        //8
        CriteriaQuery cq2 = cb.createQuery(Double.class);
        cq2.select(cb.avg(cq2.from(User.class).get("age")));
        System.out.println(em.createQuery(cq2).getSingleResult());

        //9
        Join<User, City> userCity = rootUser.join("city");
        Predicate userId = cb.not((rootUser.get("id")).in(Arrays.asList(2, 5, 9, 12, 13, 16)));
        queryUser.where(userId);
        rootUser.fetch("city");

        //10
        Join<User, City> userCity1 = rootUser.join("city");
        Join<City, Country> cityCountry = userCity1.join("country");
        rootUser.fetch("city");
        rootCity.fetch("country");






        em.createQuery(queryUser).getResultList().forEach(System.out::println);


        em.getTransaction().commit();

        em.close();
        entityManagerFactory.close();
    }
}
