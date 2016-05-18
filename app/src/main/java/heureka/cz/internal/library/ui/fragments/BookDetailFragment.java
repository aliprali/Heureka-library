package heureka.cz.internal.library.ui.fragments;

/**
 * Created by Ondrej on 6. 5. 2016.
 */
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.helpers.CollectionUtils;
import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.repository.Info;
import heureka.cz.internal.library.rest.ApiDescription;
import heureka.cz.internal.library.ui.MainActivity;
import heureka.cz.internal.library.ui.adapters.AvailableRecyclerAdapter;
import heureka.cz.internal.library.ui.adapters.UsersRecyclerAdapter;
import retrofit2.Retrofit;

public class BookDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragment_book_detailk_detail.xml
        View view = inflater.inflate(R.layout.activity_book_detail, container, false);
        return view;
    }

    public static final String KEY_CAN_BORROW = "can_borrow";
    public static final String KEY_CAN_RESERVE = "can_reserve";

    private Book bookDetail;

    /**
     * vypujcka by měla byt mozna jen po nacteni knihy cteckou,
     * aby nedoslo k vypojceni jine knihy
     * */
    private boolean canBorrow = false;

    /**
     * zato rezervace kdykoliv jindy
     * */
    private boolean canReserve = false;

    private ApiDescription apiDescription;

    @Inject
    CollectionUtils collectionUtils;

    @Inject
    Retrofit retrofit;

    @Bind(R.id.coordinator)
    View coordinator;

//    @Bind(R.id.toolbar)
//    Toolbar toolbar;

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

    @Bind(R.id.btn_reserve)
    Button btnReserve;

    @OnClick(R.id.btn_borrow)
    void borrowBook() {
        btnBorrow.setEnabled(false);
        apiDescription.borrowBook(bookDetail.getBookId(), new ApiDescription.ResponseHandler() {
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

    @OnClick(R.id.btn_reserve)
    void reserveBook() {
        btnReserve.setEnabled(false);
        apiDescription.reserveBook(bookDetail.getBookId(), new ApiDescription.ResponseHandler() {
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
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((CodeCamp)getActivity().getApplication()).getApplicationComponent().inject(this);
        ButterKnife.bind(this, view);

        // predani objektu z aktivity
        if (getActivity().getIntent().getExtras() != null) {
            bookDetail = getActivity().getIntent().getExtras().getParcelable(MainActivity.KEY_BOOK_DETAIL);
            canBorrow = getActivity().getIntent().getExtras().getBoolean(KEY_CAN_BORROW);
            canReserve = getActivity().getIntent().getExtras().getBoolean(KEY_CAN_RESERVE);
        }

        // nacteni stavu po otoceni obrazovky
        if (savedInstanceState != null) {
            bookDetail = savedInstanceState.getParcelable(MainActivity.KEY_BOOK_DETAIL);
            canBorrow = savedInstanceState.getBoolean(KEY_CAN_BORROW);
            canReserve = savedInstanceState.getBoolean(KEY_CAN_RESERVE);
        }

        apiDescription = new ApiDescription(retrofit);

//        ((ActionBarActivity)getActivity())setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        if(!canBorrow) {
            btnBorrow.getLayoutParams().height = 0;
        }

        if(!canReserve) {
            btnReserve.getLayoutParams().height = 0;
        }

        initBook();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.KEY_BOOK_DETAIL, bookDetail);
        outState.putBoolean(KEY_CAN_BORROW, canBorrow);
        outState.putBoolean(KEY_CAN_RESERVE, canReserve);
    }

    private void initBook() {
        detailName.setText(bookDetail.getName());
        detailTags.setText(bookDetail.getTags().size() > 0 ? collectionUtils.implode(",", bookDetail.getTags()) : bookDetail.getDbTags());
        detailLang.setText(bookDetail.getLang());
        detailForm.setText(bookDetail.getForm());
        detailLink.setText(bookDetail.getDetailLink());

        detailAvailable.setLayoutManager(new LinearLayoutManager(getActivity()));
        AvailableRecyclerAdapter adapterAvailable = new AvailableRecyclerAdapter(bookDetail.getAvailable());
        detailAvailable.setAdapter(adapterAvailable);

        detailUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        UsersRecyclerAdapter adapterUsers = new UsersRecyclerAdapter(bookDetail.getHolders());
        detailUsers.setAdapter(adapterUsers);
    }
public Book getBook(){
    return bookDetail;
}
}
