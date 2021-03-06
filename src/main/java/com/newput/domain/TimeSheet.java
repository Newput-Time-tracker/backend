package com.newput.domain;

import java.io.Serializable;

import org.springframework.stereotype.Service;

@Service
public class TimeSheet implements Serializable {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column time_sheet.id
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column time_sheet.emp_id
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	private Integer empId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column time_sheet.work_date
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	private Long workDate;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column time_sheet.chunk_id
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	private Integer chunkId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column time_sheet.time_in
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	private Long timeIn;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column time_sheet.time_out
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	private Long timeOut;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column time_sheet.created
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	private Long created;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column time_sheet.updated
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	private Long updated;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table time_sheet
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column time_sheet.id
	 * @return  the value of time_sheet.id
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column time_sheet.id
	 * @param id  the value for time_sheet.id
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column time_sheet.emp_id
	 * @return  the value of time_sheet.emp_id
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public Integer getEmpId() {
		return empId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column time_sheet.emp_id
	 * @param empId  the value for time_sheet.emp_id
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column time_sheet.work_date
	 * @return  the value of time_sheet.work_date
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public Long getWorkDate() {
		return workDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column time_sheet.work_date
	 * @param workDate  the value for time_sheet.work_date
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public void setWorkDate(Long workDate) {
		this.workDate = workDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column time_sheet.chunk_id
	 * @return  the value of time_sheet.chunk_id
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public Integer getChunkId() {
		return chunkId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column time_sheet.chunk_id
	 * @param chunkId  the value for time_sheet.chunk_id
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public void setChunkId(Integer chunkId) {
		this.chunkId = chunkId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column time_sheet.time_in
	 * @return  the value of time_sheet.time_in
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public Long getTimeIn() {
		return timeIn;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column time_sheet.time_in
	 * @param timeIn  the value for time_sheet.time_in
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public void setTimeIn(Long timeIn) {
		this.timeIn = timeIn;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column time_sheet.time_out
	 * @return  the value of time_sheet.time_out
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public Long getTimeOut() {
		return timeOut;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column time_sheet.time_out
	 * @param timeOut  the value for time_sheet.time_out
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public void setTimeOut(Long timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column time_sheet.created
	 * @return  the value of time_sheet.created
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public Long getCreated() {
		return created;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column time_sheet.created
	 * @param created  the value for time_sheet.created
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public void setCreated(Long created) {
		this.created = created;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column time_sheet.updated
	 * @return  the value of time_sheet.updated
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public Long getUpdated() {
		return updated;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column time_sheet.updated
	 * @param updated  the value for time_sheet.updated
	 * @mbggenerated  Mon Oct 12 13:12:27 IST 2015
	 */
	public void setUpdated(Long updated) {
		this.updated = updated;
	}
}