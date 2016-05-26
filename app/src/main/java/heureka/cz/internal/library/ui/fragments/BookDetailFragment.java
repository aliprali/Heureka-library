package heureka.cz.internal.library.ui.fragments;

/**
 * Created by Ondrej on 6. 5. 2016.
 */


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.helpers.CollectionUtils;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.helpers.RetrofitBuilder;
import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.repository.BookReservation;
import heureka.cz.internal.library.repository.Info;
import heureka.cz.internal.library.repository.Settings;
import heureka.cz.internal.library.rest.ApiDescription;
import heureka.cz.internal.library.services.DownloadService;
import heureka.cz.internal.library.ui.BookDetailAndResActivity;
import heureka.cz.internal.library.rest.ApiDescription.ResponseHandler;
import heureka.cz.internal.library.ui.MainActivity;
import heureka.cz.internal.library.ui.adapters.AvailableRecyclerAdapter;
import heureka.cz.internal.library.ui.adapters.UsersRecyclerAdapter;
import heureka.cz.internal.library.ui.dialogs.RateDialog;
import retrofit2.Retrofit;

public class BookDetailFragment extends Fragment {

    private Map<String, String> mimes = new HashMap<>();

    public static final String KEY_CAN_BORROW = "can_borrow";
    public static final String KEY_CAN_RESERVE = "can_reserve";
    public static final String MY_BOOK = "is_my_book";

    private Book bookDetail;

    /*
     * vypujcka by měla byt mozna jen po nacteni knihy cteckou,
     * aby nedoslo k vypojceni jine knihy */
    private boolean canBorrow = false;

    /** zato rezervace kdykoliv jindy */
    private boolean canReturn = false;
    /**
     * zato rezervace kdykoliv jindy
     * */
    private boolean canReserve = false;

    private String bookCode = "";

    private ApiDescription apiDescription;

    @Inject
    CollectionUtils collectionUtils;

    @Inject
    RetrofitBuilder retrofitBuilder;

    @Inject
    Settings settings;

    @Bind(R.id.coordinator)
    View coordinator;

    @Bind(R.id.detail_name)
    TextView detailName;

    @Bind(R.id.tags)
    TextView detailTags;

    @Bind(R.id.form)
    TextView detailForm;

    @Bind(R.id.lang)
    TextView detailLang;

    @Bind(R.id.detail_link)
    TextView detailLink;

    @Bind(R.id.detail_available)
    RecyclerView detailAvailable;

    @Bind(R.id.detail_users)
    RecyclerView detailUsers;

    @Bind(R.id.btn_borrow)
    Button btnBorrow;

    @Bind(R.id.btn_return)
    Button btnReturn;

    @Bind(R.id.btn_reserve)
    Button btnReserve;

    @OnClick(R.id.btn_borrow)
    void borrowBook() {
        btnBorrow.setEnabled(false);

        if (settings.get() == null) {
            return;
        }

        apiDescription.checkBorrowBook(bookCode, settings.get().getEmail(), new ResponseHandler() {
            @Override
            public void onResponse(Object data) {
                ArrayList<BookReservation> reservations = (ArrayList<BookReservation>)data;

                if(reservations.isEmpty()) {
                    doBorrow();
                } else {
                    Log.d("TEST", "show ask dialog...");
                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.reservation_exist)
                            .setMessage(R.string.reservation_exist_info)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    doBorrow();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    btnBorrow.setEnabled(true);
                                }
                            })
                            .show();
                }

            }

            @Override
            public void onFailure() {}
        });

    }

    @OnClick(R.id.btn_reserve)
    void reserveBook() {
        btnReserve.setEnabled(false);

        if (settings.get() == null) {
            return;
        }

        if(!"Papír".equals(bookDetail.getForm())) {
            Bundle bundle = new Bundle();
            bundle.putString(DownloadService.KEY_ID, ""+bookDetail.getBookId());

            bundle.putString(DownloadService.KEY_NAME, bookDetail.getName()+mimes.get(bookDetail.getMime()));
            bundle.putString(DownloadService.KEY_TYPE, DownloadService.TYPE_BOOK);

            Intent intent = new Intent(getContext(), DownloadService.class);
            intent.putExtras(bundle);
            getActivity().startService(intent);
        } else {
            apiDescription.reserveBook(bookDetail.getBookId(), settings.get().getEmail(), new ApiDescription.ResponseHandler() {
                @Override
                public void onResponse(Object data) {
                    Snackbar.make(coordinator, ((Info) data).getInfo(), Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure() {
                    btnBorrow.setEnabled(true);
                }
            });
        }
    }

    @OnClick(R.id.btn_return)
    void returnBook() {
        btnReturn.setEnabled(false);

        Bundle args = new Bundle();
        args.putInt(RateDialog.TAG_BOOK_ID, bookDetail.getBookId());

        RateDialog rateDialog= RateDialog.newInstance();
        rateDialog.setArguments(args);
        FragmentManager fm = getChildFragmentManager();

        rateDialog.show(fm, "fragment_rate_dialog");
        btnReturn.setEnabled(false);
        getFragmentManager().popBackStackImmediate();
    }

    @OnClick(R.id.detail_link)
    void detailLink() {

        String url = bookDetail.getDetailLink();
        if(url == null) {
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_book_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((CodeCamp)getActivity().getApplication()).getApplicationComponent().inject(this);
        ButterKnife.bind(this, view);

        // predani objektu z aktivity
        if (getActivity().getIntent().getExtras() != null) {
            bookDetail = getActivity().getIntent().getExtras().getParcelable(MainActivity.KEY_BOOK_DETAIL);
            canBorrow = getActivity().getIntent().getExtras().getBoolean(KEY_CAN_BORROW);
            canReserve = getActivity().getIntent().getExtras().getBoolean(KEY_CAN_RESERVE);
            canReturn = getActivity().getIntent().getExtras().getBoolean(MY_BOOK);
            //toto ismzbook
            bookCode = getActivity().getIntent().getExtras().getString(BookDetailAndResActivity.KEY_CODE);
        }

        // nacteni stavu po otoceni obrazovky
        if (savedInstanceState != null) {
            bookDetail = savedInstanceState.getParcelable(MainActivity.KEY_BOOK_DETAIL);
            canBorrow = savedInstanceState.getBoolean(KEY_CAN_BORROW);
            canReserve = savedInstanceState.getBoolean(KEY_CAN_RESERVE);
            canReturn = savedInstanceState.getBoolean(MY_BOOK);
            //toto ismzbook

            bookCode = savedInstanceState.getString(BookDetailAndResActivity.KEY_CODE);
        }

        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(settings.get() != null ? settings.get().getApiAddress() : Config.API_BASE_URL));

        if(!canBorrow) {
            btnBorrow.getLayoutParams().height = 0;
        }

        if(!canReserve) {
            btnReserve.getLayoutParams().height = 0;
        }

        if(!canReturn){
            btnReturn.getLayoutParams().height = 0;
        }


        initBook();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.KEY_BOOK_DETAIL, bookDetail);
        outState.putBoolean(KEY_CAN_BORROW, canBorrow);
        outState.putBoolean(KEY_CAN_RESERVE, canReserve);
        outState.putBoolean(MY_BOOK, canReturn);
    }

    private void initBook() {

        mimes.put("application/pdf", "pdf");
        mimes.put("application/vnd.amazon.ebook", "kindle");
        mimes.put("application/x-dtbook+xml", "epub");
        mimes.put("audio/mpeg", "mp3");

        detailName.setText(bookDetail.getName());
        detailTags.setText(bookDetail.getTags().size() > 0 ? collectionUtils.implode(",", bookDetail.getTags()) : "");
        detailLang.setText(bookDetail.getLang());
        detailForm.setText(bookDetail.getForm());
        detailLink.setText(bookDetail.getDetailLink());

        detailAvailable.setLayoutManager(new LinearLayoutManager(getActivity()));
        AvailableRecyclerAdapter adapterAvailable = new AvailableRecyclerAdapter(bookDetail.getAvailable());
        detailAvailable.setAdapter(adapterAvailable);

        detailUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        UsersRecyclerAdapter adapterUsers = new UsersRecyclerAdapter(bookDetail.getHolders());
        detailUsers.setAdapter(adapterUsers);

        if(!"Papír".equals(bookDetail.getForm())) {
            btnReserve.setText(R.string.download);
        }
    }

    private void doBorrow() {
        apiDescription.borrowBook(bookCode, settings.get().getEmail(), new ApiDescription.ResponseHandler() {
            @Override
            public void onResponse(Object data) {
                Snackbar.make(coordinator, ((Info)data).getInfo(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                btnBorrow.setEnabled(true);
            }
        });
    }

    public Book getBook(){
    return bookDetail;
}



}
