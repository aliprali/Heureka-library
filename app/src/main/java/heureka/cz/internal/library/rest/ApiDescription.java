package heureka.cz.internal.library.rest;

import java.util.ArrayList;

import heureka.cz.internal.library.repository.Api;
import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.repository.BookHolders;
import heureka.cz.internal.library.repository.BookReservation;
import heureka.cz.internal.library.repository.Form;
import heureka.cz.internal.library.repository.Heurekoviny;
import heureka.cz.internal.library.repository.Info;
import heureka.cz.internal.library.repository.Lang;
import heureka.cz.internal.library.repository.Position;
import heureka.cz.internal.library.rest.interfaces.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tomas on 26.4.16.
 */
public class ApiDescription {

    public static final String TAG = "ApiDescription";

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


    public void getPositions(final ResponseHandler responseHandler) {
        Call<ArrayList<Position>> call = apiInterface.getPositions();


        call.enqueue(new Callback<ArrayList<Position>>() {
            @Override
            public void onResponse(Call<ArrayList<Position>> call, Response<ArrayList<Position>> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Position>> call, Throwable t) {
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

    public void getUserHistory(String user, final ResponseHandler responseHandler) {
        Call<ArrayList<Book>> call = apiInterface.getUserHistory(user);
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

    public void checkBorrowBook(String code, String user, final ResponseHandler responseHandler) {
        Call<ArrayList<BookReservation>> call = apiInterface.checkBorrowBook(code, user);

        call.enqueue(new Callback<ArrayList<BookReservation>>() {
            @Override
            public void onResponse(Call<ArrayList<BookReservation>> call, Response<ArrayList<BookReservation>> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BookReservation>> call, Throwable t) {
                responseHandler.onFailure();
            }
        });
    }

    public void borrowBook(String bookCode, String user, final ResponseHandler responseHandler) {
        Call<Info> call = apiInterface.borrowBook(bookCode, user);

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

    public void reserveBook(Integer bookId, String user, final ResponseHandler responseHandler) {
        Call<Info> call = apiInterface.reserveBook(bookId, user);

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

    public void returnBook(Integer bookId, String user, String place, int rate, String rateText,final ResponseHandler responseHandler) {
        Call<Info> call = apiInterface.returnBook(bookId, user, place, rate, rateText);

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

    public void historyOneBook(Integer bookId, final ResponseHandler responseHandler){
        Call<ArrayList<BookHolders>> call = apiInterface.oneBookHistory(bookId);
        call.enqueue(new Callback<ArrayList<BookHolders>>() {
            @Override
            public void onResponse(Call<ArrayList<BookHolders>> call, Response<ArrayList<BookHolders>> response) {
                if (response.isSuccessful()) {
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

    public void apiList(final ResponseHandler responseHandler){
        Call<ArrayList<Api>> call = apiInterface.api();

        call.enqueue(new Callback<ArrayList<Api>>() {
            @Override
            public void onResponse(Call<ArrayList<Api>> call, Response<ArrayList<Api>> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Api>> call, Throwable t) {
                responseHandler.onFailure();
            }
        });
    }

    public void addUser(String url, String name, String email, final ResponseHandler responseHandler){
        Call<Info> call = apiInterface.addUsers(name, email);
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

    public void registerToken(String email, String token, final ResponseHandler responseHandler) {
        Call<Info> call = apiInterface.registerToken(email, token);
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

    public void getForms(final ResponseHandler responseHandler) {
        Call<ArrayList<Form>> call = apiInterface.getForms();
        call.enqueue(new Callback<ArrayList<Form>>() {
            @Override
            public void onResponse(Call<ArrayList<Form>> call, Response<ArrayList<Form>> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Form>> call, Throwable t) {
                responseHandler.onFailure();
            }
        });
    }

    public void getLang(final ResponseHandler responseHandler) {
        Call<ArrayList<Lang>> call = apiInterface.getLangs();
        call.enqueue(new Callback<ArrayList<Lang>>() {
            @Override
            public void onResponse(Call<ArrayList<Lang>> call, Response<ArrayList<Lang>> response) {
                if (response.isSuccessful()) {
                    responseHandler.onResponse(response.body());
                } else {
                    responseHandler.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Lang>> call, Throwable t) {
                responseHandler.onFailure();
            }
        });
    }
}
