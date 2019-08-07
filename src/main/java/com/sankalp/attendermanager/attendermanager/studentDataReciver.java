package com.sankalp.attendermanager.attendermanager;

public class studentDataReciver {


  String rollNo;
  String status1;
  String urlimg;

    public studentDataReciver(String rollNo, String status1, String urlimg) {
       this.rollNo = rollNo;
       this.status1 = status1;
       this.urlimg = urlimg;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getXStatus() {
        if (status1==null)
        {
            status1="0";
            return status1;
        }else {
            return status1;
        }
    }

    public void setXStatus(String status) {
        this.status1 = status;
    }

    public String getUrlimg() {
        return urlimg;
    }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }

    public studentDataReciver() {

    }

}