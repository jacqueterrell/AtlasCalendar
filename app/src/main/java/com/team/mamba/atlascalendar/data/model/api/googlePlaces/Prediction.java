package com.team.mamba.atlascalendar.data.model.api.googlePlaces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Prediction {

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("matched_substrings")
    @Expose
    private List<GoogleLocations.MatchedSubstring> matchedSubstrings = null;

    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("reference")
    @Expose
    private String reference;

    @SerializedName("structured_formatting")
    @Expose
    private GoogleLocations.StructuredFormatting structuredFormatting;

    @SerializedName("terms")
    @Expose
    private List<GoogleLocations.Term> terms = null;

    @SerializedName("types")
    @Expose
    private List<String> types = null;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<GoogleLocations.MatchedSubstring> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    public void setMatchedSubstrings(List<GoogleLocations.MatchedSubstring> matchedSubstrings) {
        this.matchedSubstrings = matchedSubstrings;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public GoogleLocations.StructuredFormatting getStructuredFormatting() {
        return structuredFormatting;
    }

    public void setStructuredFormatting(GoogleLocations.StructuredFormatting structuredFormatting) {
        this.structuredFormatting = structuredFormatting;
    }

    public List<GoogleLocations.Term> getTerms() {
        return terms;
    }

    public void setTerms(List<GoogleLocations.Term> terms) {
        this.terms = terms;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

}