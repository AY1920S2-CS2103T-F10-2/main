package seedu.address.model.statistics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.model.internship.InternshipApplication;
import seedu.address.model.status.Status;

/**
 * Represents a statistics generator model.
 * Generates relevant statistics for internship applications.
 */
public class Statistics {
    public static final String TOTAL = "TOTAL";

    private Status[] statuses = Status.class.getEnumConstants();
    private HashMap<Status, Integer> statusCount = new HashMap<>();

    /**
     * Computes and updates the overall statistics based on the list of internship applications given.
     * @param internshipApplicationList
     */
    public void computeAndUpdateStatistics(ObservableList<InternshipApplication> internshipApplicationList) {
        resetStatistics();
        computeCount(internshipApplicationList);
    }

    /**
     * Computes and updates the count for each internship application status.
     * @param internshipApplicationList
     */
    public void computeCount(ObservableList<InternshipApplication> internshipApplicationList) {
        List<Status> newStatuses = internshipApplicationList.stream()
                .map(ia -> {
                    return ia.getStatus();
                })
                .collect(Collectors.toList());
        newStatuses.forEach((status) -> {
            int count = statusCount.get(status);
            statusCount.put(status, ++count);
        });
    }

    /**
     * Resets the current statistics.
     */
    public void resetStatistics() {
        for (Status status : statuses) {
            statusCount.put(status, 0);
        }
    }

    public int getCount(Status status) {
        return statusCount.get(status);
    }

    public int getTotalCount() {
        Iterator statusCountIterator = statusCount.entrySet().iterator();
        int totalCount = 0;
        while (statusCountIterator.hasNext()) {
            Map.Entry element = (Map.Entry) statusCountIterator.next();
            int count = (int) element.getValue();
            totalCount += count;
        }
        return totalCount;
    }

    public double getPercentage(Status status) {
        return ((double) statusCount.get(status)) * 100;
    }

}
