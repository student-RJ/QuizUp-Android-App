package com.example.login4;

import java.io.Serializable;
import java.util.List;

public class UserProfile implements Serializable {

    private String name;
    private String email;
    private String phone;
    private String dob;
    private String location;
    private String nationality;
    private String gender;
    private String socialMediaLinks;
    private String quizHistory;
    private boolean themeEnabled;
    private boolean notificationsEnabled;
    private boolean privacyEnabled;
    private String imageUrl;
    private List<String> attemptedQuizzes;  // Array of attempted quizzes
    private OverallProgress overallProgress;  // Map for progress details
    private String selectedCourse;  // User's selected course

    // Constructor
    public UserProfile(String name, String email, String phone, String dob, String location,
                       String nationality, String gender, String socialMediaLinks,
                       String quizHistory, boolean themeEnabled, boolean notificationsEnabled,
                       boolean privacyEnabled, String imageUrl, List<String> attemptedQuizzes,
                       OverallProgress overallProgress, String selectedCourse) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.location = location;
        this.nationality = nationality;
        this.gender = gender;
        this.socialMediaLinks = socialMediaLinks;
        this.quizHistory = quizHistory;
        this.themeEnabled = themeEnabled;
        this.notificationsEnabled = notificationsEnabled;
        this.privacyEnabled = privacyEnabled;
        this.imageUrl = imageUrl;
        this.attemptedQuizzes = attemptedQuizzes;
        this.overallProgress = overallProgress;
        this.selectedCourse = selectedCourse;  // Initialize selectedCourse
    }

    // Getters and Setters
    public String getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(String selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getAttemptedQuizzes() {
        return attemptedQuizzes;
    }

    public void setAttemptedQuizzes(List<String> attemptedQuizzes) {
        this.attemptedQuizzes = attemptedQuizzes;
    }

    public OverallProgress getOverallProgress() {
        return overallProgress;
    }

    public void setOverallProgress(OverallProgress overallProgress) {
        this.overallProgress = overallProgress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSocialMediaLinks() {
        return socialMediaLinks;
    }

    public void setSocialMediaLinks(String socialMediaLinks) {
        this.socialMediaLinks = socialMediaLinks;
    }

    public String getQuizHistory() {
        return quizHistory;
    }

    public void setQuizHistory(String quizHistory) {
        this.quizHistory = quizHistory;
    }

    public boolean isThemeEnabled() {
        return themeEnabled;
    }

    public void setThemeEnabled(boolean themeEnabled) {
        this.themeEnabled = themeEnabled;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public boolean isPrivacyEnabled() {
        return privacyEnabled;
    }

    public void setPrivacyEnabled(boolean privacyEnabled) {
        this.privacyEnabled = privacyEnabled;
    }

    // Nested Class for Overall Progress
    public static class OverallProgress {
        private int completionPercentage;
        private int quizzesAttempted;
        private int totalQuizzes;

        public OverallProgress(int completionPercentage, int quizzesAttempted, int totalQuizzes) {
            this.completionPercentage = completionPercentage;
            this.quizzesAttempted = quizzesAttempted;
            this.totalQuizzes = totalQuizzes;
        }

        public int getCompletionPercentage() {
            return completionPercentage;
        }

        public void setCompletionPercentage(int completionPercentage) {
            this.completionPercentage = completionPercentage;
        }

        public int getQuizzesAttempted() {
            return quizzesAttempted;
        }

        public void setQuizzesAttempted(int quizzesAttempted) {
            this.quizzesAttempted = quizzesAttempted;
        }

        public int getTotalQuizzes() {
            return totalQuizzes;
        }

        public void setTotalQuizzes(int totalQuizzes) {
            this.totalQuizzes = totalQuizzes;
        }
    }
}
