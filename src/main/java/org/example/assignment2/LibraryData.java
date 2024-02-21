package org.example.assignment2;

import java.io.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "LibraryData", value = "/lib-data")
public class LibraryData extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public @Override void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        /**
         * The doGet method will handle book and author “views” that will be driven from queries
         * for the respective data. The view that is delivered will be differentiated by a URL
         * variable called “view” (see JSP objectives for more details).
         *
         * The book view is essentially a listing of titles from the database. Note that you should
         * include a list of author(s) for each book title.
         *
         * The author view is essentially a listing of the authors from the database. Note that you
         * should include a list of any books that an author has written.
         *
         * You can use any HTML you would like to format these outputs.
         *
         * After generating a view, provide a means for the user to return to the main JSP page.
         */
    }

    public @Override void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * The doPost method will handle submissions of form data, one for each of a book and
         * an author. You will differentiate the submission through the use of a “hidden” form
         * field.
         *
         * Posted form data will be used to insert records into the database. You should use a
         * prepared statement for all insert statements.
         *
         * Use the DBManager to save and retrieve all data.
         *
         * On submission, print a confirmation message to the user and allow them to return to
         * your main JSP page.
         */
    }

    public void destroy() {
    }
}