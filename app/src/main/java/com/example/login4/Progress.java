package com.example.login4;

import java.io.Serializable;
import java.util.Date;

public class Progress implements Serializable {

    private String quizId;
    private String quizTitle;
    private int score;
    private int maxScore;
    private int completionPercentage;
    private String timeTaken;
    private Date dateAttempted;

    // Constructor
    public Progress(String quizId, String quizTitle, int score, int maxScore, int completionPercentage,
                    String timeTaken, Date dateAttempted) {
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.score = score;
        this.maxScore = maxScore;
        this.completionPercentage = completionPercentage;
        this.timeTaken = timeTaken;
        this.dateAttempted = dateAttempted;
    }

    // Getters and Setters
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(int completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Date getDateAttempted() {
        return dateAttempted;
    }

    public void setDateAttempted(Date dateAttempted) {
        this.dateAttempted = dateAttempted;
    }
}
