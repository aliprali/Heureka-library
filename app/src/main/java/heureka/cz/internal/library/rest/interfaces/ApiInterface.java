package heureka.cz.internal.library.rest.interfaces;

import java.util.ArrayList;

import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.repository.Api;
import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.repository.BookHolders;
import heureka.cz.internal.library.repository.BookReservation;
import heureka.cz.internal.library.repository.Form;
import heureka.cz.internal.library.repository.Heurekoviny;
import heureka.cz.internal.library.repository.Info;
import heureka.cz.internal.library.repository.Lang;
import heureka.cz.internal.library.repository.Position;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * Created by tomas on 26.4.16.
 */
public interface ApiInterface {

    @GET(Config.URL_BOOKS)
    Call<ArrayList<Book>> getBooks();

    @GET(Config.URL_BOOKS_MY)
    Call<ArrayList<Book>> getMyBooks(@Path("user") String user);

    @GET(Config.URL_BOOKS_USER)
    Call<ArrayList<Book>> getUserHistory(@Path("user") String user);

    @GET(Config.URL_BOOK)
    Call<Book> getBook(@Path("code") String code);

    @POST(Config.URL_RESERVE_BOOK)
    Call<Info> reserveBook(@Path("id") Integer id, @Path("user") String user);

    @POST(Config.URL_BORROW_BOOK)
    Call<Info> borrowBook(@Path("code") String code, @Path("user") String user);

    @GET(Config.URL_BORROW_BOOK)
    Call<ArrayList<BookReservation>> checkBorrowBook(@Path("code") String id, @Path("user") String user);

    @POST(Config.URL_RETURN_BOOK)
    Call<Info> returnBook(@Path("id") int id, @Path("user") String user, @Path("place") String place, @Path("rate") int rate, @Path("ratetext") String rateText);

    @GET(Config.URL_ONE_BOOK_HISTORY)
    Call<ArrayList<BookHolders>> oneBookHistory(@Path("id") Integer bookId);

    @GET(Config.URL_POSITIONS)
    Call<ArrayList<Position>> getPositions();

    @GET(Config.URL_HEUREKOVINY)
    Call<ArrayList<Heurekoviny>> getHeurekoviny();

    @GET(Config.URL_APIS)
    Call<ArrayList<Api>> api();

    @POST(Config.URL_USERS)
    Call<Info> addUsers(@Path("name") String name, @Path("email") String email);

    @POST(Config.URL_REGISTER_TOKEN)
    Call<Info> registerToken(@Path("email") String email, @Path("token") String token);

    @GET(Config.URL_DOWNLOAD_BOOK)
    @Streaming
    Call<ResponseBody> downloadBook(@Path("id") Integer id);

    @GET(Config.URL_DOWNLOAD_INTERNAL_BOOK)
    @Streaming
    Call<ResponseBody> downloadInternalBook(@Path("id") String id);

    @GET(Config.URL_LANG)
    Call<ArrayList<Lang>> getLangs();

    @GET(Config.URL_FORM)
    Call<ArrayList<Form>> getForms();
}
