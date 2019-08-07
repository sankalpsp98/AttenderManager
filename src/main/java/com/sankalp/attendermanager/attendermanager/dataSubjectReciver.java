package com.sankalp.attendermanager.attendermanager;

public class dataSubjectReciver {




    public String setSubjectStatus;
    String ttStamp;

    public dataSubjectReciver()
    {

    }

    public dataSubjectReciver(String ttStamp,String setSubjectStatus) {
        this.ttStamp = ttStamp;
        this.setSubjectStatus = setSubjectStatus;
    }
    public String getTtStamp() {
        return ttStamp;
    }

    public void setTtStamp(String ttStamp) {
        this.ttStamp = ttStamp;
    }
    public String getSetSubjectStatus() {

        if (setSubjectStatus==null)
        {
            setSubjectStatus="0";
            return setSubjectStatus;
        }else {
            return setSubjectStatus;
        }

    }

    public void setSubjectStatus(String setSubjectStatus) {
        this.setSubjectStatus = setSubjectStatus;
    }


}
