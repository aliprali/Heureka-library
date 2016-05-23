package heureka.cz.internal.library.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;

import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.repository.BookHolders;
import heureka.cz.internal.library.repository.Heurekoviny;
import heureka.cz.internal.library.repository.Holder;
import heureka.cz.internal.library.repository.Info;
import heureka.cz.internal.library.rest.interfaces.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tomas on 26.4.16.
 */
public class ApiDescription {

    Retrofit retrofit;

    ApiInterface apiInterface;

    enum BookApi{
        BOOKS, MY_BOOKS
    }

    public interface ResponseHandler {
        public void onResponse(Object data);
        public void onFailure();
    }

    public ApiDescription(Retrofit retrofit) {
        this.retrofit = retrofit;
        this.apiInterface = retrofit.create(ApiInterface.class);
    }

    public void getBooks(final ResponseHandler responseHandler) {
        Call<ArrayList<Book>> call = apiInterface.getBooks();


        call.enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {
                responseHandler.onFailure();
            }
        });
    }

    public void getMyBooks(String user, final ResponseHandler responseHandler) {
        Call<ArrayList<Book>> call = apiInterface.getMyBooks(user);
        call.enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {
                responseHandler.onFailure();
            }
        });
    }


    public void getBook(String bookCode, final ResponseHandler responseHandler) {
        Call<Book> call = apiInterface.getBook(bookCode);

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                responseHandler.onFailure();
            }
        });
    }

    public void borrowBook(Integer bookId, final ResponseHandler responseHandler) {
        Call<Info> call = apiInterface.borrowBook(bookId);

        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                responseHandler.onFailure();
            }
        });
    }

    public void reserveBook(Integer bookId, final ResponseHandler responseHandler) {
        Call<Info> call = apiInterface.reserveBook(bookId);

        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                responseHandler.onFailure();
            }
        });
    }

    public void returnBook(Integer bookId, String place, final ResponseHandler responseHandler) {
        Call<Info> call = apiInterface.returnBook(bookId, place);

        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                responseHandler.onFailure();
            }
        });
    }

    public void historyOneBook(String bookCode, final ResponseHandler responseHandler){
        Call<ArrayList<BookHolders>> call = apiInterface.oneBookHistory(bookCode);
        System.out.println("BOOK CODE"+bookCode+"URL"+call.request().url().toString());

        call.enqueue(new Callback<ArrayList<BookHolders>>() {
            @Override
            public void onResponse(Call<ArrayList<BookHolders>> call, Response<ArrayList<BookHolders>> response) {


                    System.out.println("INRESPONSE"+response.message()+response.headers().toString());

                if (response.isSuccessful()) {
//                    ArrayList al =(ArrayList) response.body();
System.out.println("SUCESS");

                        System.out.println("RESPONSEBODY"+response.body().toString()+"json");

                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BookHolders>> call, Throwable t) {
                responseHandler.onFailure();
            }

        });

    }

    public void getHeurekoviny(final ResponseHandler responseHandler) {
        Call<ArrayList<Heurekoviny>> call = apiInterface.getHeurekoviny();

        System.out.println("HEUREKOVINY "+"URL"+call.request().url().toString());
        call.enqueue(new Callback<ArrayList<Heurekoviny>>() {
            @Override
            public void onResponse(Call<ArrayList<Heurekoviny>> call, Response<ArrayList<Heurekoviny>> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Heurekoviny>> call, Throwable t) {
                responseHandler.onFailure();
            }
        });
    }
}
