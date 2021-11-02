package com.jennerdulce.taskmaster.activities;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.jennerdulce.taskmaster.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BasicFunctionalityTests {
//
//    @Rule
//    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
//
//    @Test
//    public void basicFunctionalityTests() {
//        ViewInteraction textView = onView(
//                allOf(withId(R.id.myTasksHeaderTextView), withText("My Tasks"),
//                        withParent(withParent(withId(android.R.id.content))),
//                        isDisplayed()));
//        textView.check(matches(withText("My Tasks")));
//
//        ViewInteraction appCompatImageView = onView(
//                allOf(withId(R.id.userSettingsImageView), withContentDescription("User Settings Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                3),
//                        isDisplayed()));
//        appCompatImageView.perform(click());
//
//        ViewInteraction appCompatEditText = onView(
//                allOf(withId(R.id.usernameInputPlainText),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                2),
//                        isDisplayed()));
//        appCompatEditText.perform(replaceText("jen"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText2 = onView(
//                allOf(withId(R.id.usernameInputPlainText), withText("jen"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                2),
//                        isDisplayed()));
//        appCompatEditText2.perform(pressImeActionButton());
//
//        ViewInteraction materialButton = onView(
//                allOf(withId(R.id.userSettingsSaveButton), withText("Save"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                3),
//                        isDisplayed()));
//        materialButton.perform(click());
//
//        pressBack();
//
//        ViewInteraction textView2 = onView(
//                allOf(withId(R.id.welcomeUsernameMessageTextView), withText("Welcome jen!"),
//                        withParent(withParent(withId(android.R.id.content))),
//                        isDisplayed()));
//        textView2.check(matches(withText("Welcome jen!")));
//
//        ViewInteraction materialTextView = onView(
//                allOf(withId(R.id.sampleTaskOne), withText("Sample Task One"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                4),
//                        isDisplayed()));
//        materialTextView.perform(click());
//
//        ViewInteraction textView3 = onView(
//                allOf(withId(R.id.taskDetailsHeaderTextView), withText("Sample Task One"),
//                        withParent(withParent(withId(android.R.id.content))),
//                        isDisplayed()));
//        textView3.check(matches(withText("Sample Task One")));
//
//        pressBack();
//
//        ViewInteraction materialButton2 = onView(
//                allOf(withId(R.id.addTaskButton), withText("ADD TASK"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                1),
//                        isDisplayed()));
//        materialButton2.perform(click());
//
//        ViewInteraction textView4 = onView(
//                allOf(withId(R.id.addTaskHeaderTextView), withText("Add Task"),
//                        withParent(withParent(withId(android.R.id.content))),
//                        isDisplayed()));
//        textView4.check(matches(withText("Add Task")));
//
//        pressBack();
//
//        ViewInteraction materialButton3 = onView(
//                allOf(withId(R.id.allTasksButton), withText("ALL TASKS"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                2),
//                        isDisplayed()));
//        materialButton3.perform(click());
//
//        ViewInteraction textView5 = onView(
//                allOf(withId(R.id.allTasksHeaderTextView), withText("All Tasks"),
//                        withParent(withParent(withId(android.R.id.content))),
//                        isDisplayed()));
//        textView5.check(matches(withText("All Tasks")));
//    }
//
//    private static Matcher<View> childAtPosition(
//            final Matcher<View> parentMatcher, final int position) {
//
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Child at position " + position + " in parent ");
//                parentMatcher.describeTo(description);
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//                ViewParent parent = view.getParent();
//                return parent instanceof ViewGroup && parentMatcher.matches(parent)
//                        && view.equals(((ViewGroup) parent).getChildAt(position));
//            }
//        };
//    }
}
