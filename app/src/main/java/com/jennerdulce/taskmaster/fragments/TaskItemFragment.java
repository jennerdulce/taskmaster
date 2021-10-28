package com.jennerdulce.taskmaster.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jennerdulce.taskmaster.R;

public class TaskItemFragment extends Fragment {

    public TaskItemFragment(){

    }

    public static TaskItemFragment newInstance() {
        TaskItemFragment fragment = new TaskItemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_item, container, false);
    }
}
