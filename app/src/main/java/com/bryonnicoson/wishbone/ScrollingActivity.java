package com.bryonnicoson.wishbone;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;


public class ScrollingActivity extends AppCompatActivity {

    DatabaseHelper db;
    ListView mDogList;
    Cursor mCursor;
    DogCursorAdapter mAdapter;
    AdapterView.OnItemClickListener mDogClickListener;
    Intent mDetailIntent;
    private CheckBox checkBoxSmall, checkBoxMedium, checkBoxLarge;
    private RadioButton radioF, radioM, radioFM;
    private View selectedRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHelper.getInstance(this);

        //seedDb();

        selectedRadio = findViewById(R.id.radio_fm);

        // Sex RadioGroup
        radioF = (RadioButton) findViewById(R.id.radio_f);
        radioM = (RadioButton) findViewById(R.id.radio_m);
        radioFM = (RadioButton) findViewById(R.id.radio_fm);

        // Size Checkboxes
        checkBoxSmall = (CheckBox) findViewById(R.id.checkbox_small);
        checkBoxMedium = (CheckBox) findViewById(R.id.checkbox_medium);
        checkBoxLarge = (CheckBox) findViewById(R.id.checkbox_large);

        mDogList = (ListView) findViewById(R.id.dog_card_list_view);
//        mDogList.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//        setListViewHeightBasedOnChildren(mDogList);

        mCursor = db.showDogs();
        mAdapter = new DogCursorAdapter(this, mCursor, 0);
        mDogList.setAdapter(mAdapter);
        // mDogList.setExpanded(true);
        mAdapter.notifyDataSetChanged();

        mDogClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mDetailIntent = new Intent(ScrollingActivity.this, DogDetailActivity.class);

                // view.findViewById = individual card view
                View photoView = view.findViewById(R.id.dog_photo);
                View nameView = view.findViewById(R.id.dog_name);
                View breedView = view.findViewById(R.id.dog_breed);
                View sexView = view.findViewById(R.id.dog_sex);
                View ageView = view.findViewById(R.id.dog_age);
                View sizeView = view.findViewById(R.id.dog_size);

                String name = mCursor.getString(mCursor.getColumnIndexOrThrow("name"));
                String breed = mCursor.getString(mCursor.getColumnIndexOrThrow("breed"));
                String size = mCursor.getString(mCursor.getColumnIndexOrThrow("size"));
                String age = mCursor.getString(mCursor.getColumnIndexOrThrow("age"));
                String sex = mCursor.getString(mCursor.getColumnIndexOrThrow("sex"));
                String desc = mCursor.getString(mCursor.getColumnIndexOrThrow("desc"));

                String uri = "@drawable/" + name.toLowerCase();
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());

                mDetailIntent.putExtra("PHOTO", imageResource);
                mDetailIntent.putExtra("NAME", name);
                mDetailIntent.putExtra("BREED", breed);
                mDetailIntent.putExtra("AGE", age);
                mDetailIntent.putExtra("SIZE", size);
                mDetailIntent.putExtra("SEX", sex);
                mDetailIntent.putExtra("DESC", desc);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this,
                        Pair.create(photoView, getString(R.string.transition_dog_photo)),
                        Pair.create(nameView, getString(R.string.transition_dog_name)),
                        Pair.create(breedView, getString(R.string.transition_dog_breed)),
                        Pair.create(ageView, getString(R.string.transition_dog_age)),
                        Pair.create(sizeView, getString(R.string.transition_dog_size)),
                        Pair.create(sexView, getString(R.string.transition_dog_sex)));
                ActivityCompat.startActivity(ScrollingActivity.this, mDetailIntent, options.toBundle());
            }
        };

        mDogList.setOnItemClickListener(mDogClickListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     *  method for setting the height of the ListView dynamically.
     *  hack to fix issue of not showing all the items of ListView when placed inside ScrollView
     *  thanks to Arshu: https://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
     **/
//     public static void setListViewHeightBasedOnChildren(ListView listView) {
//         ListAdapter listAdapter = listView.getAdapter();
//         if (listAdapter == null)
//             return;
//
//         int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//         int totalHeight = 0;
//         View view = null;
//         for (int i = 0; i < listAdapter.getCount(); i++) {
//             view = listAdapter.getView(i, view, listView);
//             if (i == 0)
//                 view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//             view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//             totalHeight += view.getMeasuredHeight();
//         }
//         ViewGroup.LayoutParams params = listView.getLayoutParams();
//         params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() -1));
//         listView.setLayoutParams(params);
//    }

    public void seedDb() {

        String genericDescription = "Generic description.  Blah, blah, blah.  Adopt me!  Look how cute I am!  Don't listen to those other dogs!  I am man's best friend!  Me!  Choose me!  Save me!";

        db.insertDog("Maggie", "Australian Cattle Dog", "Female",  "Medium", "Adult","Maggie is a high-energy working dog who loves her frisbee", 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Finnegan", "Siberian Husky Mix", "Male",  "Large", "Adult", "Finnegan is half Husky, quarter Boston Terrier, quarter Italian Greyhound and 100% confident and chill", 1, 1, 1, 1, 0, 0, 1);
        db.insertDog("Rusty", "Yorkshire Terrier", "Male", "Small", "Adult", "Rusty is a sweetheart who craves attention and loves his people", 0, 1, 1, 1, 0, 0, 0);
        db.insertDog("Kai", "Chinese Crested", "Female", "Small", "Adult",  "Kai is an adorable, cuddling couch-potato happy to spend the day lying around with you.", 0, 1, 1, 1, 0, 0, 0);
        db.insertDog("Bailee", "Anatolian Shepherd Mix", "Female", "Large", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Pierre", "Poodle", "Male", "Small", "Senior", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Domino", "Shih Tzu", "Male", "Small", "Senior", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Lucy", "Labrador Retriever Mix", "Female", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Lulu", "Rat Terrier", "Female", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Paprika", "Basset Hound - Beagle Mix", "Female", "Medium", "Senior", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Hermione", "Beagle Mix", "Female", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Willow", "Labrador Retriever - Feist Mix", "Female", "Medium", "Baby", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Woody", "Corgi - Miniature Pinscher Mix", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Chevy", "Catahoula Leopard Dog Mix", "Male", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Dude", "Jack Russell Terrier (Parson Russell ", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Fergie", "Pug - Beagle Mix", "Female", "Medium", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Fiona", "Beagle - Coonhound Mix", "Female", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Floyd", "Chihuahua - Spitz Mix", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Helena", "German Shepherd Dog Mix", "Female", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Jodi", "Pit Bull Terrier", "Female", "Medium", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Katie", "Shepherd - Boxer Mix", "Female", "Large", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Lando", "Beagle - Hound Mix", "Male", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("London", "Feist", "Female", "Small", "Baby", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Mama", "Pit Bull Terrier", "Female", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Mickey", "Silky Terrier", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Sadie", "Feist", "Female", "Small", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Scooter", "Jack Russell Terrier", "Female", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Arturo", "Lhasa Apso", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Cayenne", "Boxer", "Female", "Large", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Lizzie", "Labrador Retriever - Pit Bull Terrier Mix", "Female", "Large", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Polo", "Bichon Frise", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Tristan", "Vizsla - Pit Bull Terrier Mix", "Male", "Extra Large", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Socks", "Boston Terrier - Feist Mix", "Female", "Small", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Ally", "Schnauzer", "Female", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Abby", "Wheaten Terrier Mix", "Female", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Gretel", "Australian Cattle Dog (Blue Heeler)", "Female", "Medium", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Smokey", "Pit Bull Terrier", "Male", "Large", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Angus", "Cairn Terrier", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Nikki", "Boston Terrier - Pug Mix", "Female", "Small", "Senior", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Cher", "Border Collie - Terrier Mix", "Female", "Medium", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Bartok", "Miniature Pinscher", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Minnie", "Corgi - Husky Mix", "Female", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Benji", "Dachshund - Corgi Mix", "Male", "Small", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Clyde", "Labrador Retriever - Collie Mix", "Male", "Large", "Baby", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Petunia", "Border Collie - Beagle Mix", "Female", "Medium", "Baby", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Milkbone", "Italian Greyhound Mix", "Male", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Sailor", "Bichon Frise", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Daisy", "Beagle", "Female", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Chewy", "Chihuahua - Pomeranian Mix", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Mugsy", "Boston Terrier - Cattle Dog Mix", "Female", "Medium", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Maggie2", "Akita Mix", "Female", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Foxy", "American Bulldog", "Female", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("LeRoy", "Beagle", "Male", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Bandit", "Feist", "Male", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Peanut", "Chihuahua", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Hansel", "Australian Cattle Dog (Blue Heeler)", "Male", "Medium", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Maverick", "Boxer - Terrier Mix", "Male", "Medium", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Pepe", "Border Collie - Beagle Mix", "Female", "Medium", "Baby", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Jax", "Boston Terrier - Chihuahua Mix", "Male", "Small", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Charity", "Hound Mix", "Female", "Medium", "Adult", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Shadow", "Labrador Retriever Mix", "Male", "Large", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
        db.insertDog("Beethoven", "Australian Shepherd - Husky Mix", "Male", "Medium", "Young", genericDescription, 0, 1, 1, 1, 0, 0, 1);
    }
}
