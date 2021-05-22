package Classes;

import Classes.Quiz.Result;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Banks {
    // Loads the data into the ObservableList
    public static void loadQuestionBank(Boolean useDialogResult, ObservableList observableListToLoadInto) {
        // Running an attempt to retrieve the data from the questionBank
        List retrievedData = Translating.deserialiseList("questionBank.ser", useDialogResult);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            observableListToLoadInto.clear();
            observableListToLoadInto.addAll(retrievedData);
        }
    }

    // Saves the data from the ObservableList
    public static void saveQuestionBank(Boolean useDialogResult, ObservableList observableListToSave) {
        // Sending the data from the ObservableList to be serialised as a questionBank file
        Translating.serialiseObject(observableListToSave.stream().toList() ,"questionBank.ser", useDialogResult);
    }

    public static void loadTestBank(Boolean useDialogResult, ObservableList observableListToLoadInto) {
        // Running an attempt to retrieve the data from the questionBank
        List retrievedData = Translating.deserialiseList("testBank.ser", useDialogResult);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            observableListToLoadInto.clear();
            observableListToLoadInto.addAll(retrievedData);
        };
    }

    public static void saveTestBank(Boolean useDialogResult, ObservableList observableListToSave) {
        // Sending the data from the ObservableList to be serialised as a testBank file
        Translating.serialiseObject(observableListToSave.stream().toList() ,"testBank.ser", useDialogResult);
    }

    // Loads the data into the ObservableList
    public static void loadResultBank(Boolean useDialogResult, ObservableList observableListToLoadInto) {
        // Running an attempt to retrieve the data from the resultBank
        List retrievedData = Translating.deserialiseList("resultBank.ser", useDialogResult);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            observableListToLoadInto.clear();
            observableListToLoadInto.addAll(retrievedData);
        }
    }

    // Sending the data from the ObservableList to be serialised as a resultBank file
    public static void saveResultBank(Boolean useDialogResult, ObservableList observableListToSave) {
        Translating.serialiseObject(observableListToSave.stream().toList() ,"resultBank.ser", useDialogResult);
    }

    // Updating the data in the resultBank file if any
    public static void updateResultBank(Boolean useDialogResult, Result newResultToSave) {
        List<Result> resultsList = new ArrayList<>();   // Creation of empty result bank

        // Running an attempt to retrieve the data from the resultBank
        loadResultBank(false, FXCollections.observableList(resultsList));

        resultsList.add(newResultToSave); // Add result to the result bank

        // Sending the list data to be serialised as a resultBank file
        Translating.serialiseObject(resultsList, "resultBank.ser", useDialogResult);
    }
}
