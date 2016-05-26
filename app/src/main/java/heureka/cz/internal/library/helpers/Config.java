package heureka.cz.internal.library.helpers;

/**
 * Created by tomas on 26.4.16.
 */
public class Config {

    public static final String API_BASE_URL = "https://library.fiction-group.eu/api/v1/";

    public static final String URL_BOOKS = "books";

    public static final String URL_BOOKS_MY = "books/my/{user}";

    public static final String URL_BOOKS_USER = "book/user-history/{user}";

    public static final String URL_BOOK = "book/{code}";

    public static final String URL_BORROW_BOOK = "book/borrow/{code}/user/{user}";

    public static final String URL_RESERVE_BOOK = "book/reservation/{id}/user/{user}";

    public static final String URL_RETURN_BOOK = "book/return/id/{id}/user/{user}/place/{place}/rate/{rate}/rate_text/{ratetext}";

    public static final String URL_ONE_BOOK_HISTORY = "book/history/{id}";

    public static final String URL_USERS = "add-user/{name}/email/{email}";

    public static final String URL_REGISTER_TOKEN = "user-token/{email}/{token}";

    public static final String URL_APIS = "list";

    public static final String URL_HEUREKOVINY="internal-books";

    public static final String URL_APIS_IMG = "book/image/#id";

    public static final String URL_POSITIONS = "filters/locality";

    public static final String URL_FORM = "filters/form";

    public static final String URL_LANG = "filters/lang";

    public static final String URL_DOWNLOAD_INTERNAL_BOOK = "book/internal-book/{id}";

    public static final String URL_DOWNLOAD_BOOK = "book/book/{id}";


}
