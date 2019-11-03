/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cegepgim;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *
 * @author manojagarwal
 */
@Path("jobHistory")
public class JobHistory {

    @Context
    private UriInfo context;
    Connection con = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    private JSONObject mainObj;
    private JSONArray jobHistoryArr;
    int emplId, deptId;
    String jobId;
    String startDate, endDate;
    JobHistory jobHist;
    /**
     * Creates a new instance of JobHistory
     */
    public JobHistory() {
        try {        
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        } catch (SQLException ex) {
            System.out.println("In catch of constructor");
            Logger.getLogger(Locations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Employee id = 176, Job_id = st_clerk, Dept Id = 110
    // link: cegepgim/jobHistory/jobHistorylist
    
    // list
    @GET
    @Path("/jobHistorylist")
    @Produces(MediaType.APPLICATION_JSON)
    public String getList() {
        ResultSet rs = null;
        JSONObject listObj = new JSONObject();
        jobHist = new JobHistory();
        
                try {
                    String sql;
                    jobHistoryArr = new JSONArray();
                    sql = "select * from job_history";
                    stm = con.prepareStatement(sql);
                    rs = stm.executeQuery();
                    while(rs.next())
                    {
                         mainObj = new JSONObject();
                         emplId = rs.getInt(1);
                         startDate = rs.getDate(2).toString();
                         endDate = rs.getDate(3).toString();
                         jobId = rs.getString(4);
                         deptId = rs.getInt(5);
                         mainObj.accumulate("employeeId", emplId);
                         mainObj.accumulate("startDate", startDate);
                         mainObj.accumulate("endDate", endDate);
                         mainObj.accumulate("jobId", jobId);
                         mainObj.accumulate("deptId", deptId);
                         jobHistoryArr.add(mainObj);
                    }
                    listObj.accumulate("Status", "OK");
                    listObj.accumulate("Timestamp", timeStamp());
                    listObj.accumulate("jobHistory", jobHistoryArr);
                } catch (SQLException ex) {
                    listObj.accumulate("Status", "FAIL");
                    listObj.accumulate("Timestamp", timeStamp());
                    listObj.accumulate("message", "Error");
                    Logger.getLogger(Locations.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally{
                        mainObj.clear();
                        if (rs != null) {
                            try {
                                  rs.close();
                                } catch (SQLException e) {
                            /* ignored */
                                }
                            }
                        if (stm != null) {
                            try {
                                    stm.close();
                                } catch (SQLException e) {
                            /* ignored */
                                }
                            }
                        if (con != null) {
                            try {
                                    con.close();
                                } catch (SQLException e) {
                            /* ignored */
                                }
                            }
                }
        return listObj.toString();
    }
    
    // singleList
    @GET
    @Path("/jobHistorySingleList&{empId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSingleList(@PathParam("empId") int theEmpId) {
        ResultSet rs = null;
        JSONObject listObj = new JSONObject();
        jobHist = new JobHistory();
        
                try {
                    String sql;
                    sql = "select * from job_history where employee_id=?";
                    stm = con.prepareStatement(sql);
                    stm.setInt(1, theEmpId);
                    rs = stm.executeQuery();
                    while(rs.next())
                    {
                         mainObj = new JSONObject();
                         emplId = rs.getInt(1);
                         startDate = rs.getDate(2).toString();
                         endDate = rs.getDate(3).toString();
                         jobId = rs.getString(4);
                         deptId = rs.getInt(5);
                         mainObj.accumulate("employeeId", emplId);
                         mainObj.accumulate("startDate", startDate);
                         mainObj.accumulate("endDate", endDate);
                         mainObj.accumulate("jobId", jobId);
                         mainObj.accumulate("deptId", deptId);
                    }
                    listObj.accumulate("Status", "OK");
                    listObj.accumulate("Timestamp", timeStamp());
                    listObj.accumulate("jobHistory", mainObj);
                } catch (SQLException ex) {
                    listObj.accumulate("Status", "FAIL");
                    listObj.accumulate("Timestamp", timeStamp());
                    listObj.accumulate("message", "Error");
                    Logger.getLogger(Locations.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally{
                        mainObj.clear();
                        if (rs != null) {
                            try {
                                  rs.close();
                                } catch (SQLException e) {
                            /* ignored */
                                }
                            }
                        if (stm != null) {
                            try {
                                    stm.close();
                                } catch (SQLException e) {
                            /* ignored */
                                }
                            }
                        if (con != null) {
                            try {
                                    con.close();
                                } catch (SQLException e) {
                            /* ignored */
                                }
                            }
                }
        return listObj.toString();
    }
    
    // insertJobHistory
    @GET()
    @Path("insertJobHistory&{empId}&{startDate}&{endDate}&{jobId}&{deptId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String insertLocation(@PathParam("empId") int theEmpId,
                                 @PathParam("startDate") String theStartDate,
                                 @PathParam("endDate") String theEndDate,
                                 @PathParam("jobId") String theJobId,
                                 @PathParam("deptId") String theDeptId) {
        jobHist = new JobHistory();
                try {
                    String sql;
                    
                    sql = "insert into job_history values(?, to_date(?, 'YYYY-MM-DD'), to_date(?, 'YYYY-MM-DD'), ?, ?)";
                    stm = con.prepareStatement(sql);
                    stm.setInt(1, theEmpId);
                    stm.setString(2, theStartDate);
                    stm.setString(3, theEndDate);
                    stm.setString(4, theJobId);
                    stm.setString(5, theDeptId);
                                       
                    int rs1 = stm.executeUpdate();
                    mainObj = new JSONObject();
                    if(rs1==1)
                    {
                        mainObj.accumulate("Status", "OK");
                        mainObj.accumulate("Timestamp", timeStamp());
                        mainObj.accumulate("message", "Successfully inserted");
                    }
                } catch (SQLException ex) {
                    mainObj.accumulate("Status", "FAIL");
                    mainObj.accumulate("Timestamp", timeStamp());
                    mainObj.accumulate("message", "Error inserting");
                    Logger.getLogger(Locations.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally{ 
                        if (rs != null) {
                            try {
                                  rs.close();
                                } catch (SQLException e) {
                            /* ignored */
                                }
                            }
                        if (stm != null) {
                            try {
                                    stm.close();
                                } catch (SQLException e) {
                            /* ignored */
                                }
                            }
                        if (con != null) {
                            try {
                                    con.close();
                                } catch (SQLException e) {
                            /* ignored */
                                }
                            }
                }
                return mainObj.toString();
    }
    
    public String timeStamp()
    {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String tm = "" + ts.getTime();
        return tm;
    }
}
