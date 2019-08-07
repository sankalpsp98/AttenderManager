package com.sankalp.attendermanager.attendermanager;

public class dataFileUpDown {


    String fileName;
    String fileMB;
    String uploaderName;

    public dataFileUpDown() {
    }
    public dataFileUpDown(String fileName, String fileMB,String uploaderName) {
        this.fileName = fileName;
        this.fileMB = fileMB;
        this.uploaderName=uploaderName;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileMB() {
        return fileMB;
    }

    public void setFileMB(String fileMB) {
        this.fileMB = fileMB;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }




}
