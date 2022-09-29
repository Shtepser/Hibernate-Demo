import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.ac.uniyar.databasescourse.entities.Student;
import ru.ac.uniyar.databasescourse.entities.Subject;
import ru.ac.uniyar.databasescourse.entities.Teacher;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Main {

    private SessionFactory sessionFactory;

    private Session openSession() {
        if (sessionFactory == null) {
            final Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Student.class);
            configuration.addAnnotatedClass(Teacher.class);
            configuration.addAnnotatedClass(Subject.class);
            sessionFactory = configuration.buildSessionFactory(new StandardServiceRegistryBuilder().build());
        }
        return sessionFactory.openSession();
    }

    private void fillDB() {
        Session session = openSession();
        session.getTransaction().begin();
        Subject subj1 = new Subject("Математический анализ");
        Subject subj2 = new Subject("Линейная алгебра");
        session.persist(subj1);
        session.persist(subj2);
        Teacher t1 = new Teacher("Иванов", "к.ф.-м.н.", "доцент");
        session.persist(t1);
        Teacher t2 = new Teacher("Петров", null, "старший преподаватель");
        session.persist(t2);
        Student s = new Student("Сидоров",
                new GregorianCalendar(2002, Calendar.FEBRUARY, 12).getTime(),
                "Пушкина 12", "99-99-99", t2);
        s.getSubjects().add(subj1);
        s.getSubjects().add(subj2);
        session.persist(s);
        Student s2 = new Student("Васильев", new GregorianCalendar(2002, Calendar.MARCH, 12).getTime(),
                "Лермонтова 22", "88-88-88", t2);
        session.persist(s2);
        session.getTransaction().commit();
    }

    private void queriesDemo() {
        gettingStudentQueryDemo();
        countingStudentsQueryDemo();
        countingStudentsByTeachersDemo();
    }

    private void gettingStudentQueryDemo() {
        Session session = openSession();

        System.out.println("1. Поиск студента по фамилии");
        String studentName = "Сидоров";
        Query<Student> q1 = session.createQuery("FROM Student WHERE name = :studentName",
                Student.class);
        List<Student> result = q1.setParameter("studentName", studentName).list();
        result.forEach((student) -> {
            System.out.printf("%s, name: %s, subjects:", student,
                    student.getName());
            student.getSubjects().forEach((subject -> {
                System.out.printf(" %s", subject.getName());
            }));
            System.out.print('\n');
        });

        System.out.println("То же самое, но с помощью Criteria API");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Student> criteria = builder.createQuery(Student.class);
        Root<Student> studentRoot = criteria.from(Student.class);
        criteria.select(studentRoot)
                .where(builder
                        .equal(studentRoot.get("name"), studentName));
        result = session.createQuery(criteria).list();
        result.forEach((student) -> {
            System.out.printf("%s, name: %s, subjects:", student,
                    student.getName());
            student.getSubjects().forEach((subject -> {
                System.out.printf(" %s", subject.getName());
            }));
            System.out.print('\n');
        });

        session.close();
    }

    private void countingStudentsQueryDemo() {
        Session session = openSession();
        System.out.println("2. Подсчёт количества студентов");
        Query<Integer> q2 = openSession().createQuery("SELECT COUNT(*) FROM Student",
                Integer.class);
        System.out.println(q2.uniqueResult());

        System.out.println("То же самое, но с помощью Criteria API");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Student> studentRoot = criteria.from(Student.class);
        criteria.select(builder.count(studentRoot));
        System.out.println(session.createQuery(criteria).getSingleResult());

        session.close();
    }

    private void countingStudentsByTeachersDemo() {
        Session session = openSession();
        System.out.println("3. Количество студентов у преподавателей");
        Query<Teacher> q3 = session.createQuery(
                "FROM Teacher t",
                Teacher.class);
        List<Teacher> result3 = q3.list();
        result3.forEach((teacher) -> System.out.printf("%s: %d\n", teacher.getName(), teacher.getStudents().size()));
        session.close();
    }

    public void modifyObjects() {
        Session session = openSession();
        session.beginTransaction();
        Student student = session.get(Student.class, 1);
        System.out.printf("Student: %s", student.getName());
        if (student.getAdvisor() != null) {
            System.out.printf("; Руководитель: %s\n", student.getAdvisor().getName());
        }
        else
            System.out.println();
        student.setAdvisor(null);
        session.getTransaction().commit();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.fillDB();
        main.queriesDemo();
        main.modifyObjects();
    }
}
