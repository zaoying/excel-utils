package cn.edu.gdut.zaoying.excel.stream.bean;

import cn.edu.gdut.zaoying.excel.common.annotations.ExcelSheet;
import cn.edu.gdut.zaoying.excel.common.annotations.SheetHeader;

@ExcelSheet
public class Student {

    @SheetHeader(name = "序号")
    private Integer serialNo;

    @SheetHeader(name = "学号")
    private Long studentNo;

    @SheetHeader(name = "姓名")
    private String name;

    @SheetHeader(name = "专业班级")
    private String major;

    @SheetHeader(name = "年级")
    private Integer clazz;

    @SheetHeader(name = "乘车区间(广州站--家庭所在车站)")
    private String zone;

    @SheetHeader(name = "生源地")
    private String birthPlace;

    @SheetHeader(name = "备注")
    private String remark;

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Long getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(Long studentNo) {
        this.studentNo = studentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getClazz() {
        return clazz;
    }

    public void setClazz(Integer clazz) {
        this.clazz = clazz;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
