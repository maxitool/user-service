package org.example;

import org.example.console.readers.primitives.BooleanConsoleReader;
import org.example.console.readers.primitives.DoubleConsoleReader;
import org.example.console.readers.primitives.IntConsoleReader;
import org.example.console.readers.primitives.LongConsoleReader;
import org.example.console.readers.primitives.StringConsoleReader;
import org.example.console.readers.primitives.responses.BooleanResponse;
import org.example.console.readers.primitives.responses.DoubleResponse;
import org.example.console.readers.primitives.responses.IntResponse;
import org.example.console.readers.primitives.responses.LongResponse;
import org.example.console.readers.primitives.responses.StringResponse;
import org.example.hibernate.dao.UserDaoImplement;
import org.example.hibernate.entities.User;

import java.util.List;
import java.util.Map;

public class GuiSingleton {
    private static final UserDaoImplement userDao = new UserDaoImplement();

    private static final String YES = "yes", NO = "no";
    private static final String GO_BACK_GUI = StringConsoleReader.GO_BACK_COMMAND + ".Go back";
    private static final String CANT_RECOGNIZE_OPTION = "Can't recognize wrote option";
    private static final String SOMETHING_WENT_WRONG_GUI = "Something went wrong";

    private static final String USER_GUI = '\n' + """
            User GUI
            1.Create
            2.Read
            3.Update
            4.Delete
            """ + StringConsoleReader.GO_BACK_COMMAND + ".Exit";
    private final Map<Integer, Runnable> USER_ACTIONS = Map.of(
            1, getInstance()::createUserFromConsole,
            2, getInstance()::readUserFromConsole,
            3, getInstance()::updateUserFromConsole,
            4, getInstance()::deleteUserFromConsole
    );

    private static final String READ_USER_GUI = '\n' + """
            Read User GUI
            1.Read all
            2.Read by id
            3.Read by email
            4.Read by name
            5.Read by age
            """ + GO_BACK_GUI;
    private final Map<Integer, Runnable> READ_USER_ACTIONS = Map.of(
            1, getInstance()::readAllUsersFromConsole,
            2, getInstance()::readByIdUserFromConsole,
            3, getInstance()::readByEmailUserFromConsole,
            4, getInstance()::readByNameUsersFromConsole,
            5, getInstance()::readByAgeUsersFromConsole
    );

    private static final String CREATE_NEW_USER_GUI = '\n' + """
            Create new User GUI
            """ + GO_BACK_GUI;
    private static final String USER_SAVED_GUI =
            "The user has been saved";

    private static final String UPDATE_USER_GUI = '\n' + """
            Update User GUI
            """ + GO_BACK_GUI;
    private static final String DO_UPDATE_NAME_GUI = '\n' +
            "Do update name? (" + YES + '/' + NO + ')';
    private static final String DO_UPDATE_EMAIL_GUI = '\n' +
            "Do update email (" + YES + '/' + NO + ')';
    private static final String DO_UPDATE_AGE_GUI = '\n' +
            "Do update age (" + YES + '/' + NO + ')';
    private static final String USER_UPDATED_GUI =
            "The user has been updated";

    private static final String DELETING_USER_GUI = '\n' + """
            Deleting User GUI
            """ + GO_BACK_GUI;
    private static final String USER_DELETED_GUI =
            "The user has been deleted";

    private static final String ENTER_ID_GUI =
            "Enter id:";
    private static final String ENTER_NAME_GUI =
            "Enter name:";
    private static final String ENTER_EMAIL_GUI =
            "Enter email:";
    private static final String ENTER_AGE_GUI =
            "Enter age:";
    private static final String BAD_ID_GUI =
            "Id must be greater than or equal to 0";
    private static final String BAD_AGE_GUI =
            "Age must be greater than or equal to 0";

    private static final String CANT_FIND_USER_BY_ID_GUI = '\n' +
            "Can't find user by id";
    private static final String CANT_FIND_USER_BY_EMAIL_GUI = '\n' +
            "Can't find user by email";
    private static final String CANT_FIND_USERS_BY_NAME_GUI = '\n' +
            "Can't find users by name";
    private static final String CANT_FIND_USERS_BY_AGE_GUI = '\n' +
            "Can't find users by age";


    private GuiSingleton() {}

    public static GuiSingleton getInstance() {return Holder.instance;}

    public void run() {
        IntResponse intResponse;
        do {
            System.out.println(USER_GUI);
            if ((intResponse = getIntFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            if (!USER_ACTIONS.containsKey(intResponse.intData)) {
                System.out.println(CANT_RECOGNIZE_OPTION);
                continue;
            }
            USER_ACTIONS.get(intResponse.intData).run();
        } while (true);
    }

    private void createUserFromConsole() {
        StringResponse strResponse;
        IntResponse intResponse;
        User user;
        System.out.println(CREATE_NEW_USER_GUI);
        do {
            user = new User();
            System.out.println(ENTER_NAME_GUI);
            if ((strResponse = getStrFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            user.setName(strResponse.stringData);

            System.out.println(ENTER_EMAIL_GUI);
            if ((strResponse = getStrFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            user.setEmail(strResponse.stringData);

            System.out.println(ENTER_AGE_GUI);
            if ((intResponse = getIntFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            user.setAge(intResponse.intData);

            if (userDao.save(user)) {
                System.out.println(USER_SAVED_GUI);
                return;
            }
            System.out.println(SOMETHING_WENT_WRONG_GUI);
        } while (true);
    }


    private void readUserFromConsole() {
        IntResponse intResponse;
        do {
            System.out.println(READ_USER_GUI);
            if ((intResponse = getIntFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            if (!READ_USER_ACTIONS.containsKey(intResponse.intData)) {
                System.out.println(CANT_RECOGNIZE_OPTION);
                continue;
            }
            READ_USER_ACTIONS.get(intResponse.intData).run();
        } while (true);
    }

    private void readAllUsersFromConsole() {
        userDao.findAll().forEach(System.out::println);
    }

    private void readByIdUserFromConsole() {
        LongResponse longResponse;
        User user;
        do {
            System.out.println(ENTER_ID_GUI);
            if ((longResponse = getLongFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            if (longResponse.longData < 0) {
                System.out.println(BAD_ID_GUI);
                continue;
            }
            if ((user = userDao.findById(longResponse.longData)) == null) {
                System.out.println(CANT_FIND_USER_BY_ID_GUI);
            } else {
                System.out.println(user);
            }
            System.out.println(GO_BACK_GUI);
        } while (true);
    }

    private void readByEmailUserFromConsole() {
        StringResponse strResponse;
        User user;
        do {
            System.out.println(ENTER_EMAIL_GUI);
            if ((strResponse = getStrFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            if ((user = userDao.findByEmail(strResponse.stringData)) == null) {
                System.out.println(CANT_FIND_USER_BY_EMAIL_GUI);
            } else {
                System.out.println(user);
            }
            System.out.println(GO_BACK_GUI);
        } while (true);
    }

    private void readByNameUsersFromConsole() {
        StringResponse strResponse;
        List<User> users;
        do {
            System.out.println(ENTER_NAME_GUI);
            if ((strResponse = getStrFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            if ((users = userDao.findByName(strResponse.stringData)) == null || users.isEmpty()) {
                System.out.println(CANT_FIND_USERS_BY_NAME_GUI);
            } else {
                users.forEach(System.out::println);
            }
            System.out.println(GO_BACK_GUI);
        } while (true);
    }

    private void readByAgeUsersFromConsole() {
        IntResponse intResponse;
        List<User> users;
        do {
            System.out.println(ENTER_AGE_GUI);
            if ((intResponse = getIntFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            if (intResponse.intData < 0) {
                System.out.println(BAD_AGE_GUI);
                continue;
            }
            if ((users = userDao.findByAge(intResponse.intData)) == null || users.isEmpty()) {
                System.out.println(CANT_FIND_USERS_BY_AGE_GUI);
            } else {
                users.forEach(System.out::println);
            }
            System.out.println(GO_BACK_GUI);
        } while (true);
    }


    private void updateUserFromConsole() {
        StringResponse strResponse;
        IntResponse intResponse;
        LongResponse longResponse;
        BooleanResponse booleanResponse;
        User user;
        System.out.println(UPDATE_USER_GUI);
        do {
            System.out.println(ENTER_ID_GUI);
            if ((longResponse = getLongFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            if (longResponse.longData < 0) {
                System.out.println(BAD_ID_GUI);
                continue;
            }
            if ((user = userDao.findById(longResponse.longData)) == null) {
                System.out.println(CANT_FIND_USER_BY_ID_GUI);
                continue;
            }

            System.out.println(DO_UPDATE_NAME_GUI);
            if ((booleanResponse = getYesOrNoFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            if (booleanResponse.booleanData) {
                System.out.println(ENTER_NAME_GUI);
                strResponse = getStrFromConsole();
                if (strResponse.state == StringResponse.States.BACK_COMMAND) {
                    return;
                }
                user.setName(strResponse.stringData);
            }

            System.out.println(DO_UPDATE_EMAIL_GUI);
            if ((booleanResponse = getYesOrNoFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            if (booleanResponse.booleanData) {
                System.out.println(ENTER_EMAIL_GUI);
                strResponse = getStrFromConsole();
                if (strResponse.state == StringResponse.States.BACK_COMMAND) {
                    return;
                }
                user.setEmail(strResponse.stringData);
            }

            System.out.println(DO_UPDATE_AGE_GUI);
            if ((booleanResponse = getYesOrNoFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            if (booleanResponse.booleanData) {
                System.out.println(ENTER_AGE_GUI);
                intResponse = getIntFromConsole();
                if (intResponse.state == StringResponse.States.BACK_COMMAND) {
                    return;
                }
                user.setAge(intResponse.intData);
            }

            if (userDao.update(user)) {
                System.out.println(USER_UPDATED_GUI);
            } else {
                System.out.println(SOMETHING_WENT_WRONG_GUI);
            }
            return;
        } while (true);
    }


    private void deleteUserFromConsole() {
        LongResponse response;
        System.out.println(DELETING_USER_GUI);
        do {
            System.out.println(ENTER_ID_GUI);
            if ((response = getLongFromConsole()).state == StringResponse.States.BACK_COMMAND) {
                return;
            }
            if (response.longData < 0) {
                System.out.println(BAD_ID_GUI);
                continue;
            }
            if (userDao.deleteById(response.longData)) {
                System.out.println(USER_DELETED_GUI);
            } else {
                System.out.println(CANT_FIND_USER_BY_ID_GUI + " or " + SOMETHING_WENT_WRONG_GUI);
            }
            System.out.println(GO_BACK_GUI);
        } while (true);
    }



    private StringResponse getStrFromConsole() {
        StringResponse response;
        do {
            response = StringConsoleReader.getStringData();
        } while (response.state != StringResponse.States.BACK_COMMAND && response.state != StringResponse.States.OK);
        return response;
    }

    private BooleanResponse getBoolFromConsole() {
        BooleanResponse response;
        do {
            response = BooleanConsoleReader.getBooleanData();
        } while (response.state != StringResponse.States.BACK_COMMAND && response.state != StringResponse.States.OK);
        return response;
    }

    private BooleanResponse getYesOrNoFromConsole() {
        BooleanResponse response;
        do {
            response = BooleanConsoleReader.getBooleanData(YES, NO);
        } while (response.state != StringResponse.States.BACK_COMMAND && response.state != StringResponse.States.OK);
        return response;
    }

    private IntResponse getIntFromConsole() {
        IntResponse response;
        do {
            response = IntConsoleReader.getIntData();
        } while (response.state != StringResponse.States.BACK_COMMAND && response.state != StringResponse.States.OK);
        return response;
    }

    private LongResponse getLongFromConsole() {
        LongResponse response;
        do {
            response = LongConsoleReader.getLongData();
        } while (response.state != StringResponse.States.BACK_COMMAND && response.state != StringResponse.States.OK);
        return response;
    }

    private DoubleResponse getDoubleFromConsole() {
        DoubleResponse response;
        do {
            response = DoubleConsoleReader.getDoubleData();
        } while (response.state != StringResponse.States.BACK_COMMAND && response.state != StringResponse.States.OK);
        return response;
    }


    private static class Holder {
        public static final GuiSingleton instance = new GuiSingleton();
    }
}
