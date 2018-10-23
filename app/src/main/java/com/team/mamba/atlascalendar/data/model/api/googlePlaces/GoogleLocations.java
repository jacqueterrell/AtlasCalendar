package com.team.mamba.atlascalendar.data.model.api.googlePlaces;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GoogleLocations {

    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * MainTextMatched
     */
    public class MainTextMatchedSubstring {

        @SerializedName("length")
        @Expose
        private Integer length;
        @SerializedName("offset")
        @Expose
        private Integer offset;

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

    }


    /**
     * MatchedSubstring
     */
    public class MatchedSubstring {

        @SerializedName("length")
        @Expose
        private Integer length;
        @SerializedName("offset")
        @Expose
        private Integer offset;

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

    }


    public class StructuredFormatting {

        @SerializedName("main_text")
        @Expose
        private String mainText;
        @SerializedName("main_text_matched_substrings")
        @Expose
        private List<MainTextMatchedSubstring> mainTextMatchedSubstrings = null;
        @SerializedName("secondary_text")
        @Expose
        private String secondaryText;

        public String getMainText() {
            return mainText;
        }

        public void setMainText(String mainText) {
            this.mainText = mainText;
        }

        public List<MainTextMatchedSubstring> getMainTextMatchedSubstrings() {
            return mainTextMatchedSubstrings;
        }

        public void setMainTextMatchedSubstrings(List<MainTextMatchedSubstring> mainTextMatchedSubstrings) {
            this.mainTextMatchedSubstrings = mainTextMatchedSubstrings;
        }

        public String getSecondaryText() {
            return secondaryText;
        }

        public void setSecondaryText(String secondaryText) {
            this.secondaryText = secondaryText;
        }

    }

    public class Term {

        @SerializedName("offset")
        @Expose
        private Integer offset;
        @SerializedName("value")
        @Expose
        private String value;

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}


