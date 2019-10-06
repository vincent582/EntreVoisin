
package com.openclassrooms.entrevoisins.neighbour_list;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_detail.DetailFragment;
import com.openclassrooms.entrevoisins.ui.neighbour_detail.DetailNeighbourActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;
    private int POSITION_ITEM = 1;

    private ListNeighbourActivity mActivity;
    private NeighbourApiService mService;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        mService = DI.getNewInstanceApiService();
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT-1));
    }

    /**
     * Open Activity detail, when click on list element.
     */
    @Test
    public void myNeighboursList_onClickItem_shouldOpenDetailActivity() {
        //Given : Start Detail Activity
        //when perform a click on item position
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        //Then : we check if text Name in DetailActivity is displayed.
        onView(withId(R.id.neigbourhNameTitle)).check(matches(isDisplayed()));
    }

    /**
     * Check if the name in DetailActivity is the same as the item selected.
     */
    @Test
    public void detailNeighbourName_onDetailActivity_isCorrect() {
        List<Neighbour> neighbourList = mService.getNeighbours();
        Neighbour neighbour = neighbourList.get(POSITION_ITEM);

        //Given : Proper name in Textview
        //when : open detailActivity
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        //Then : we check if text Name in DetailActivity is displayed.
        onView(withId(R.id.neigbourName)).check(matches(withText(neighbour.getName())));
    }

    /**
     * Check if favorite list contain only items marked as favorite.
     */
    @Test
    public void favoritesList_onFavoriteTab_showFavoriteItems() {
        //Given : Favorites list in favorite tab
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        //when : add 2 items in favorite onClick on floating action button
        onView(withId(R.id.fab_favorite))
                .perform(click());
        pressBack();
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM+1, click()));
        onView(withId(R.id.fab_favorite))
                .perform(click());
        pressBack();

        //Then : Check if the number of items in Favorite list is same as the number neigbours we added.
        onView(withId(R.id.container)).perform(swipeLeft());
        onView(ViewMatchers.withId(R.id.list_neighboursFavorite)).check(withItemCount(2));
    }

    /**
     * When we delete an item in favorite, the item is no more shown
     */
    @Test
    public void myNeighboursListFavorite_deleteAction_shouldRemoveItemFromFavorite() {

        //add item in favorite.
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        onView(withId(R.id.fab_favorite))
                .perform(click());
        pressBack();
        onView(withId(R.id.container)).perform(swipeLeft());

        // Given : We remove the element at position
        onView(ViewMatchers.withId(R.id.list_neighboursFavorite)).check(withItemCount(1));
        // When perform a click on a delete icon
        onView(ViewMatchers.withId(R.id.list_neighboursFavorite))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(ViewMatchers.withId(R.id.list_neighboursFavorite)).check(withItemCount(0));
    }
}