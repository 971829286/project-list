package com.souche.bmgateway.core.domain;

import java.util.Date;

public class Holiday {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_holiday.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_holiday.year
     *
     * @mbggenerated
     */
    private Integer year;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_holiday.month
     *
     * @mbggenerated
     */
    private Integer month;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_holiday.day
     *
     * @mbggenerated
     */
    private String day;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_holiday.gmt_created
     *
     * @mbggenerated
     */
    private Date gmtCreated;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_holiday.gmt_modified
     *
     * @mbggenerated
     */
    private Date gmtModified;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_holiday.id
     *
     * @return the value of t_holiday.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_holiday.id
     *
     * @param id the value for t_holiday.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_holiday.year
     *
     * @return the value of t_holiday.year
     *
     * @mbggenerated
     */
    public Integer getYear() {
        return year;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_holiday.year
     *
     * @param year the value for t_holiday.year
     *
     * @mbggenerated
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_holiday.month
     *
     * @return the value of t_holiday.month
     *
     * @mbggenerated
     */
    public Integer getMonth() {
        return month;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_holiday.month
     *
     * @param month the value for t_holiday.month
     *
     * @mbggenerated
     */
    public void setMonth(Integer month) {
        this.month = month;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_holiday.day
     *
     * @return the value of t_holiday.day
     *
     * @mbggenerated
     */
    public String getDay() {
        return day;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_holiday.day
     *
     * @param day the value for t_holiday.day
     *
     * @mbggenerated
     */
    public void setDay(String day) {
        this.day = day == null ? null : day.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_holiday.gmt_created
     *
     * @return the value of t_holiday.gmt_created
     *
     * @mbggenerated
     */
    public Date getGmtCreated() {
        return gmtCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_holiday.gmt_created
     *
     * @param gmtCreated the value for t_holiday.gmt_created
     *
     * @mbggenerated
     */
    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_holiday.gmt_modified
     *
     * @return the value of t_holiday.gmt_modified
     *
     * @mbggenerated
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_holiday.gmt_modified
     *
     * @param gmtModified the value for t_holiday.gmt_modified
     *
     * @mbggenerated
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}