package com.newput.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newput.domain.Employee;

/**
 * 
 * @author Newput Description : Use to create and parse the json object
 */

@Service
public class JsonResService {

	@Autowired
	private TTUtil util;

	private boolean success;
	private String rcode;
	private String error;
	private ArrayList<JSONObject> data;

	public ArrayList<JSONObject> getData() {
		return data;
	}

	public void setData(ArrayList<JSONObject> data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getRcode() {
		return rcode;
	}

	public void setRcode(String rcode) {
		this.rcode = rcode;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Description : Create a Json object of user to send as a response to UI.
	 * 
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject createEmployeeJson(Employee emp) {
		JSONObject obj = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		obj.put("id", emp.getId());
		obj.put("firstName", emp.getFirstName());
		obj.put("lastName", emp.getLastName());
		obj.put("email", emp.getEmail());		
		obj.put("dob", sdf.format(emp.getDob()));
		obj.put("doj", sdf.format(emp.getDoj()));
		obj.put("address", emp.getAddress());
		obj.put("contact", emp.getContact());
		obj.put("gender", emp.getGender());
		return obj;
	}

	/**
	 * Description : Create a Json object of time sheet to send as a response to
	 * UI.
	 * 
	 * @param map
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject createTimeSheetJson(HashMap<String, String> map) {
		JSONObject obj = new JSONObject();
		obj.put("workDate", map.get("workDate"));
		obj.put("in", map.get("in"));
		obj.put("out", map.get("out"));
		obj.put("lunchIn", map.get("lunchIn"));
		obj.put("lunchOut", map.get("lunchOut"));
		obj.put("nightIn", map.get("nightIn"));
		obj.put("nightOut", map.get("nightOut"));
		obj.put("workDesc", map.get("workDesc"));
		return obj;
	}

	/**
	 * Description : Create a Json object of time sheet to send as a response to
	 * UI.
	 * 
	 * @param map
	 * @param totalHour
	 * @param workDesc
	 * @param workDate
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getTimeSheetJson(HashMap<String, Long> map, String totalHour, String workDesc, Long workDate) {
		JSONObject obj = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String date = sdf.format(workDate);
		obj.put("workDate", date);
		obj.put("in", util.timeHrs(map.get("in"), map.get("workDate")));
		obj.put("out", util.timeHrs(map.get("out"), map.get("workDate")));
		obj.put("lunchIn", util.timeHrs(map.get("lunchIn"), map.get("workDate")));
		obj.put("lunchOut", util.timeHrs(map.get("lunchOut"), map.get("workDate")));
		obj.put("nightIn", util.timeHrs(map.get("nightIn"), map.get("workDate")));
		obj.put("nightOut", util.timeHrs(map.get("nightOut"), map.get("workDate")));
		obj.put("totalHour", totalHour);
		obj.put("workDesc", workDesc);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public void setDataValue(String str, String token) {
		ArrayList<JSONObject> objArray = new ArrayList<JSONObject>();
		JSONObject obj = new JSONObject();
		obj.put("msg", str);
		if (token != null && !token.equalsIgnoreCase("")) {
			obj.put("token", token);
		}
		objArray.add(obj);
		setData(objArray);
	}

	@SuppressWarnings("unchecked")
	public JSONObject responseSender() {
		JSONObject obj = new JSONObject();
		obj.put("expire", 3600);
		obj.put("success", isSuccess());
		obj.put("data", getData());
		obj.put("rcode", getRcode());
		obj.put("error", getError());
		return obj;
	}

	public void errorResponse(String response) {
		setDataValue(null, null);
		setError(response);
		setRcode("505");
		setSuccess(false);
	}

	public void successResponse() {
		setError(null);
		setRcode(null);
		setSuccess(true);
	}

}
