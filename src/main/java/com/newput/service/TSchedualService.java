package com.newput.service;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.ArrayList;
//
//import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newput.domain.DateSheet;
import com.newput.domain.DateSheetExample;
import com.newput.domain.TimeSheet;
import com.newput.domain.TimeSheetExample;
import com.newput.mapper.DateSheetMapper;
import com.newput.mapper.TimeSheetMapper;
import com.newput.utility.JsonResService;
import com.newput.utility.ReqParseService;

/**
 * Description : To create and update time sheet values in database.
 * 
 * @author Newput
 *
 */
@Service
public class TSchedualService {

	@Autowired
	DateSheetMapper dateSheetMapper;

	@Autowired
	private ReqParseService reqParser;

	@Autowired
	private TimeSheet timeSheet;

	@Autowired
	TimeSheetMapper timeSheetMapper;

	@Autowired
	private DateSheet dateSheet;

	@Autowired
	private JsonResService jsonResService;

	HashMap<String, String> map = new HashMap<>();

	/**
	 * Description : Set the value of time sheet in setter to update in db.
	 * 
	 * @param lunchIn - 12:00
	 * @param in - 09:00
	 * @param out - 19:00
	 * @param workDate - 20-10-2015
	 * @param lunchOut - 12:30
	 * @param nightIn - 22:00
	 * @param nightOut - 23:00
	 * @param emp_id -1
	 */
	public void timeSheetValue(String lunchIn, String in, String out, String workDate, String lunchOut, String nightIn,
			String nightOut, int emp_id) {
		map.put("workDate", workDate);
		if ((in != null && !in.equalsIgnoreCase("")) || (out != null && !out.equalsIgnoreCase(""))) {
			map.put("in", in);
			map.put("out", out);
			timeSheet = reqParser.setTimeSheetValue(workDate, in, out, "1", emp_id);
			saveTimeSheet(timeSheet);
		}
		if ((lunchIn != null && !lunchIn.equalsIgnoreCase(""))
				|| (lunchOut != null && !lunchOut.equalsIgnoreCase(""))) {
			map.put("lunchIn", lunchIn);
			map.put("lunchOut", lunchOut);
			timeSheet = reqParser.setTimeSheetValue(workDate, lunchIn, lunchOut, "2", emp_id);
			saveTimeSheet(timeSheet);
		}
		if ((nightIn != null && !nightIn.equalsIgnoreCase(""))
				|| (nightOut != null && !nightOut.equalsIgnoreCase(""))) {
			map.put("nightIn", nightIn);
			map.put("nightOut", nightOut);
			timeSheet = reqParser.setTimeSheetValue(workDate, nightIn, nightOut, "3", emp_id);
			saveTimeSheet(timeSheet);
		}
	}

	/**
	 * Description : Update and insert the time sheet value in database.
	 * 
	 * @param timeSheet - An object
	 */
	public boolean saveTimeSheet(TimeSheet timeSheet) {
		ArrayList<JSONObject> objArray = new ArrayList<JSONObject>();
		boolean status = false;
		TimeSheetExample example = new TimeSheetExample();
		example.createCriteria().andEmpIdEqualTo(timeSheet.getEmpId()).andWorkDateEqualTo(timeSheet.getWorkDate())
				.andChunkIdEqualTo(timeSheet.getChunkId());
		List<TimeSheet> timeList = timeSheetMapper.selectByExample(example);

		if (timeList.size() > 0) {
			// update
			TimeSheet timeSheet1 = new TimeSheet();
			timeSheet1 = timeList.get(0);
			example.createCriteria().andWorkDateEqualTo(timeSheet1.getWorkDate()).andEmpIdEqualTo(timeSheet1.getEmpId())
					.andChunkIdEqualTo(timeSheet1.getChunkId());
			timeSheet1.setTimeIn(timeSheet.getTimeIn());
			timeSheet1.setTimeOut(timeSheet.getTimeOut());
			timeSheet1.setUpdated(reqParser.getCurrentTime());
			int i = timeSheetMapper.updateByExampleSelective(timeSheet1, example);
			if (i == 0) {
				status = true;
			}
		} else {
			// insert
			int j = timeSheetMapper.insertSelective(timeSheet);
			if (j == 0) {
				status = true;
			}
		}
		if (status) {
			// return json object;
			// System.out.println("fail to insert or update");
			jsonResService.errorResponse("fail to insert or update");
			return false;
		} else {
			// System.out.println("successfully insert or update");
			// jsonResService.setData(jsonResService.createTimeSheetJson(map));
			objArray.add(jsonResService.createTimeSheetJson(map));
			jsonResService.setData(objArray);
			jsonResService.successResponse();
			return true;
		}
	}

	
	/**
	 * Description : Update and insert the work description of user day task.
	 */
	public void dateSheetValue() {
		ArrayList<JSONObject> objArray = new ArrayList<JSONObject>();
		map.put("workDesc", dateSheet.getWorkDesc());
		DateSheetExample example = new DateSheetExample();

		example.createCriteria().andEmpIdEqualTo(dateSheet.getEmpId()).andWorkDateEqualTo(dateSheet.getWorkDate());
		List<DateSheet> dateList = dateSheetMapper.selectByExample(example);

		if (dateList.size() > 0) {
			// update
			DateSheet dateSheet1 = new DateSheet();
			dateSheet1 = dateList.get(0);
			example.createCriteria().andWorkDateEqualTo(dateSheet1.getWorkDate())
					.andEmpIdEqualTo(dateSheet1.getEmpId());
			dateSheet1.setWorkDesc(dateSheet.getWorkDesc());
			dateSheet1.setUpdated(reqParser.getCurrentTime());
			int i = dateSheetMapper.updateByExampleSelective(dateSheet1, example);
			if (i > 0) {
				objArray.add(jsonResService.createTimeSheetJson(map));
				jsonResService.setData(objArray);
				// jsonResService.setDataValue("value updated successfully",
				// "");
				jsonResService.successResponse();
			} else {
				jsonResService.errorResponse("Value is not updated.Please try again");
			}
		} else {
			// insert
			int j = dateSheetMapper.insertSelective(dateSheet);
			if (j > 0) {
				objArray.add(jsonResService.createTimeSheetJson(map));
				jsonResService.setData(objArray);
				// jsonResService.setDataValue("value inserted successfully",
				// "");
				jsonResService.successResponse();
			} else {
				jsonResService.errorResponse("Value is not inserted.Please try again");
			}
		}
	}

	public void clearMap() {
		map.clear();
	}
}
