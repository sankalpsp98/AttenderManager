package com.sankalp.attendermanager.attendermanager;

public class dataDepartmentReceiver {
    String deptName;
    String selectedDept;
    public dataDepartmentReceiver() {
    }
    public dataDepartmentReceiver(String deptName, String selectedDept) {
        this.deptName = deptName;
        this.selectedDept = selectedDept;
    }





    public String getSelectedDept() {
        return selectedDept;
    }

    public void setSelectedDept(String selectedDept) {
        this.selectedDept = selectedDept;
    }


    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
