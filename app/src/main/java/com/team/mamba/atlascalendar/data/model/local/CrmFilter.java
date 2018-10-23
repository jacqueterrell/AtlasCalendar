package com.team.mamba.atlascalendar.data.model.local;

public class CrmFilter {
    
    private int status = -1;
    private int opportunity = -1;
    private int nextStep = -1;

    public CrmFilter(CrmFilter filter){

        this.status = filter.getStatus();
        this.opportunity = filter.opportunity;
        this.nextStep = filter.getNextStep();
    }

    public CrmFilter(){

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(int opportunity) {
        this.opportunity = opportunity;
    }

    public int getNextStep() {
        return nextStep;
    }

    public void setNextStep(int nextStep) {
        this.nextStep = nextStep;
    }
}
