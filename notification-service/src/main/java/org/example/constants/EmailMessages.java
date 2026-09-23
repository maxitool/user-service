package org.example.constants;

public class EmailMessages {
    EmailMessages() {
    }

    public static final String USER_CREATED_SUBJECT = "Welcome!";
    public static final String USER_CREATED_TEXT =
            "<p>Hello! Your account has been successfully created.</p><img src='%s'>";

    public static final String USER_DELETED_SUBJECT = "Account deleted!";
    public static final String USER_DELETED_TEXT =
            "<p>Hello! Your account has been deleted.</p><img src='%s'>";
}
