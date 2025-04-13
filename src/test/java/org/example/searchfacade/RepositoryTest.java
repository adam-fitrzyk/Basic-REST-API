package org.example.searchfacade;

import java.util.ArrayList;
import static java.util.Collections.sort;
import static java.util.Arrays.asList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.example.searchfacade.model.Event;
import org.example.searchfacade.model.EventRepository;
import org.example.searchfacade.model.MongoDBConnection;
import org.example.searchfacade.model.User;
import org.example.searchfacade.model.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

@Disabled("Disabled due to school firewall")
@ExtendWith(MockitoExtension.class)
public class RepositoryTest {

    // INTRODUCE MOCKING HERE SO DATABASE CONNECTION IS UNNECESSARY

    private static UserRepository user_repository;
    private static EventRepository event_repository;

    @BeforeAll
    static void loadRepositories() {
        System.out.println(">>> REPOSITORY UTILITIES TESTS");
        user_repository = UserRepository.getInstance();
        event_repository = EventRepository.getInstance();
    }

    @AfterAll
    static void closeRepositories() {
        MongoDBConnection.getInstance().close();
    }

    /* Test UserRepository class */
    @Test
    void testUserRepository() {
        System.out.println(">> Testing UserRepository: ");
        
        // Test findAll()
        var find_all_result = new ArrayList<String>();
        var users_found = user_repository.findAll();
        var find_all_expected = asList(
            "User[_id=507f191e810c19729de860e0, user=user1@sample.io, workstation=192.168.1.10]",
            "User[_id=507f191e810c19729de860e1, user=user2@sample.io, workstation=192.168.1.11]",
            "User[_id=507f191e810c19729de860e2, user=user3@sample.io, workstation=192.168.1.12]",
            "User[_id=507f191e810c19729de860e3, user=user4@sample.io, workstation=192.168.1.13]"
        );

        System.out.println("> Users found with findAll():");
        System.out.println("--------------------------------");
        for (User user : users_found) {
            System.out.println(user);
            find_all_result.add(user.toString());
        }
        System.out.println("--------------------------------");

        sort(find_all_expected);
        sort(find_all_result);

        assert find_all_expected.equals(find_all_result);

        // Test findById()
        var find_by_id_result = user_repository.findById("507f191e810c19729de860e0").toString();
        var find_by_id_expected = "User[_id=507f191e810c19729de860e0, user=user1@sample.io, workstation=192.168.1.10]";
        System.out.println("> User found with findById('507f191e810c19729de860e0'):");
        System.out.println("--------------------------------");
        System.out.println(find_by_id_result);
        System.out.println("--------------------------------");
        
        assert find_by_id_expected.equals(find_by_id_result.toString());

        // Test findByFilters()
    }

    /* Test EventRepository class */
    @Test
    void testEventRepository() {
        System.out.println(">> Testing EventRepository: ");
        
        // Test findAll()
        var find_all_result = new ArrayList<String>();
        var events_found = event_repository.findAll();
        var find_all_expected = asList(
            "Event[_id=507f191e810c19729de8aae0, type=LOGIN, time=1262340660000, user=user1@sample.io, ip=192.168.1.10]",
            "Event[_id=507f191e810c19729de8aae1, type=LOGIN, time=1262427060000, user=user2@sample.io, ip=192.168.1.11]",
            "Event[_id=507f191e810c19729de8aae2, type=LOGIN, time=1262513460000, user=user3@sample.io, ip=192.168.1.11]",
            "Event[_id=507f191e810c19729de8aae4, type=LOGOUT, time=1262369460000, user=user1@sample.io, ip=192.168.1.10]",
            "Event[_id=507f191e810c19729de8aae5, type=LOGOUT, time=1262455860000, user=user2@sample.io, ip=192.168.1.11]",
            "Event[_id=507f191e810c19729de8aae6, type=LOGOUT, time=1262542260000, user=user3@sample.io, ip=192.168.1.11]",
            "Event[_id=507f191e810c19729de8aae3, type=LOGIN, time=1262599860000, user=user4@sample.io, ip=192.168.1.13]",
            "Event[_id=507f191e810c19729de8aae7, type=LOGOUT, time=1262628660000, user=user4@sample.io, ip=192.168.1.13]"
        );

        System.out.println("> Events found with findAll():");
        System.out.println("--------------------------------");
        for (Event event : events_found) {
            System.out.println(event);
            find_all_result.add(event.toString());
        }
        System.out.println("--------------------------------");

        sort(find_all_expected);
        sort(find_all_result);
        
        assert find_all_expected.equals(find_all_result);

        // Test findById()
        var find_by_id_result = event_repository.findById("507f191e810c19729de8aae6").toString();
        var find_by_id_expected = "Event[_id=507f191e810c19729de8aae6, type=LOGOUT, time=1262542260000, user=user3@sample.io, ip=192.168.1.11]";

        System.out.println("> Event found with findById('507f191e810c19729de8aae6'):");
        System.out.println("--------------------------------");
        System.out.println(find_by_id_result);
        System.out.println("--------------------------------");
        
        assert find_by_id_expected.equals(find_by_id_result);

        // Test findByFilters()
    }

}
