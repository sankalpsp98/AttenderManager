package com.sankalp.attendermanager.attendermanager;

import java.util.HashMap;

    public class dataWire {

//======================================================main==========================================
        public static  String mainDir;


        public static String getMainDir() {
            return mainDir;
        }

        public static void setMainDir(String mainDir) {
            dataWire.mainDir = mainDir;
        }

//=====================================================end=============================================

    public static String college;
    public String course;
    public  static String subject;



    public static String  intentExtra;



    public static String getLinkAddress;

    public static String Faculty;
    public static String Year;
    public static String Sem;
    public static String Div;

    public static String hodCodeVisibler;
    public  static  String collegeCode;



    public static String acadmicYear;

    public static String getAcadmicYear() {
        return acadmicYear;
    }

    public static void setAcadmicYear(String acadmicYear) {
        dataWire.acadmicYear = acadmicYear;
    }


    public static HashMap<String,String> studentHashMap = new HashMap();

    public static HashMap<String, String> getStudentHashMap() {

        return studentHashMap;
    }

    public static void setStudentHashMap(HashMap<String, String> studentHashMap) {
        dataWire.studentHashMap = studentHashMap;
    }

    public static String getCollegeCode() {
        return collegeCode;
    }

    public static void setCollegeCode(String collegeCode) {
        dataWire.collegeCode = collegeCode;
    }

    public static String getHodCodeVisibler() {
        return hodCodeVisibler;
    }

    public static void setHodCodeVisibler(String hodCodeVisibler) {
        dataWire.hodCodeVisibler = hodCodeVisibler;
    }

    public static String Lecture;
    public static String getSubject() {
        return subject;
    }

    public static void setSubject(String subject) {

        dataWire.subject = subject;
    }
    public static void setLecture1(String lecture) {
        Lecture = lecture;
    }

    public static String getLecture1() {

        return Lecture;
    }


    public static String getFaculty1() {
        return Faculty;
    }

    public static void setFaculty1(String faculty) {
        Faculty = faculty;
    }

    public static String getYear1() {
        return Year;
    }

    public static void setYear1(String year) {
        Year = year;
    }

    public static String getSem1() {
        return Sem;
    }

    public static void setSem1(String sem) {
        Sem = sem;
    }

    public static String getDiv1() {
        return Div;
    }

    public static void setDiv1(String div) {
        Div = div;
    }

    public  static  String GoogleAccToken;

    public static String getGoogleAccToken() {
        if (GoogleAccToken== null)
        {
            GoogleAccToken = " ";

            return GoogleAccToken;
        }
        return GoogleAccToken;
    }

    public static void setGoogleAccToken(String googleAccToken) {
        GoogleAccToken = googleAccToken;
    }

    public static String getGetLinkAddress() {
        return getLinkAddress;
    }

    public static void setGetLinkAddress(String getLinkAddress) {
        dataWire.getLinkAddress = getLinkAddress;
    }

    public static String getCollege() {
        return college;
    }

    public static void setCollege(String College) {
            college = College;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public static String getIntentExtra() {
        return intentExtra;
    }

    public static void setIntentExtra(String intentExtra) {
        dataWire.intentExtra = intentExtra;
    }



}
