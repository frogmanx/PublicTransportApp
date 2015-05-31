package com.example.adam.pubtrans.interfaces;

import com.example.adam.pubtrans.models.NearMeResult;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public interface IResults<T> {
    public void setResults(ArrayList<T> results);
    public void refresh();
}
