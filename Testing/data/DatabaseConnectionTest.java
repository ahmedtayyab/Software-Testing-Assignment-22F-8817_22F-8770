package data;
import dal.DatabaseConnection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    @DisplayName("getInstance should return same object reference")
    void testSingleInstanceReference() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();

        assertSame(instance1, instance2,
                "Both instances should reference the same object");
    }

    @Test
    @DisplayName("Only one instance should exist in memory")
    void testOnlyOneInstanceCreated() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        DatabaseConnection instance3 = DatabaseConnection.getInstance();

        assertSame(instance1, instance2);
        assertSame(instance2, instance3);
    }

    @Test
    @DisplayName("Constructor should be private")
    void testConstructorIsPrivate() throws Exception {
        Constructor<DatabaseConnection> constructor =
                DatabaseConnection.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()),
                "Constructor must be private for Singleton pattern");
    }

    @Test
    @DisplayName("Singleton should remain single instance in multithreaded environment")
    void testSingletonThreadSafety() throws InterruptedException {

        final DatabaseConnection[] instances = new DatabaseConnection[2];
        CountDownLatch latch = new CountDownLatch(2);

        Thread thread1 = new Thread(() -> {
            instances[0] = DatabaseConnection.getInstance();
            latch.countDown();
        });

        Thread thread2 = new Thread(() -> {
            instances[1] = DatabaseConnection.getInstance();
            latch.countDown();
        });

        thread1.start();
        thread2.start();

        latch.await();

        assertSame(instances[0], instances[1],
                "Both threads should receive the same Singleton instance");
    }

    @Test
    @DisplayName("Reflection should not create new instance (if protection exists)")
    void testReflectionDoesNotBreakSingleton() throws Exception {

        DatabaseConnection instance1 = DatabaseConnection.getInstance();

        Constructor<DatabaseConnection> constructor =
                DatabaseConnection.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        DatabaseConnection instance2 = constructor.newInstance();

        assertNotSame(instance1, instance2,
                "Reflection currently breaks Singleton (this indicates weakness)");
    }
}
