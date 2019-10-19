/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cegepgim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
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
 * @author Jay Shah
 */
@Path("/regions")
public class Region {

    @Context
    private UriInfo context;
    
    Connection con = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    private JSONObject mainObj;
    private JSONArray regionArr;
    int regionId;
    String regionName;
    Region reg;
    
    
    public Region() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
            System.out.println("In try block of constructor");
        } catch (SQLException ex) {
            System.out.println("In catch of constructor");
            Logger.getLogger(Region.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @GET
    @Path("regList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getList() {
         ResultSet rs = null;
            JSONObject listRegObj = new JSONObject();
            reg = new Region();
           System.out.println("Inside regions");
        
        try {
            String sql;
            regionArr = new JSONArray();
            sql = "select * from regions";
            System.out.println("ok");
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while(rs.next())
            {
                mainObj = new JSONObject();
                regionId = rs.getInt(1);
                regionName = rs.getString(2);
                mainObj.accumulate("regionId", regionId);
                mainObj.accumulate("regionName", regionName);
                regionArr.add(mainObj);
                
            }
            listRegObj.accumulate("Status","ok");
            listRegObj.accumulate("TimeStamp",timeStamp());
            listRegObj.accumulate("regions", regionArr);
            
          
        } catch (SQLException ex) {
             listRegObj.accumulate("Status","ok");
            listRegObj.accumulate("TimeStamp",timeStamp());
            listRegObj.accumulate("message", "Error");
            Logger.getLogger(Region.class.getName()).log(Level.SEVERE, null, ex);
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
        return listRegObj.toString();
    }
    
    // insert
    @GET()
    @Path("insertReg&{regionId}&{regionName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String insertLocation(@PathParam("regionId") int theRegionId,
                                 @PathParam("regionName") String theRegionName) {
        reg = new Region();
                try {
                    String sql;
                    
                    sql = "insert into regions values(?, ?)";
                    stm = con.prepareStatement(sql);
                    stm.setInt(1, theRegionId);
                    stm.setString(2, theRegionName);
                                       
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
    
    // single list
    @GET()
    @Path("/singleList&{regionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSingleList(@PathParam("regionId") int theRegionId) {
        ResultSet rs = null;
        JSONObject singleListObj = new JSONObject();
        reg = new Region();
        
                try {
                    String sql;
                    sql = "select * from regions where region_id=?";
                    // to show that this statement has parameter
                    stm = con.prepareStatement(sql);
                    stm.setInt(1, theRegionId);
                    rs = stm.executeQuery();
                    while(rs.next())
                    {
                         mainObj = new JSONObject();
                         regionId = rs.getInt(1);
                         regionName = rs.getString(2);
                         mainObj.accumulate("regionId", regionId);
                         mainObj.accumulate("regionName", regionName);
                    }
                    singleListObj.accumulate("Status", "OK");
                    singleListObj.accumulate("Timestamp", timeStamp());
                    singleListObj.accumulate("locations", mainObj);
                } catch (SQLException ex) {
                    singleListObj.accumulate("Status", "FAIL");
                    singleListObj.accumulate("Timestamp", timeStamp());
                    singleListObj.accumulate("message", "Error");
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
        return singleListObj.toString();
    }
    
    // update
    @GET()
    @Path("/updateReg&{regionId}&{regionName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUpdate(@PathParam("regionId") int theRegionid,
                            @PathParam("regionName") String theRegionName) {
        reg = new Region();
        
                try {
                    String sql;
                    sql = "update regions set region_name=? where region_id=?";
                    // to show that this statement has parameter
                    stm = con.prepareStatement(sql);
                    stm.setString(1, theRegionName);
                    stm.setInt(2, theRegionid);
                    int rs = stm.executeUpdate();
                    mainObj = new JSONObject();
                    if(rs==1)
                    {
                        mainObj.accumulate("Status", "OK");
                        mainObj.accumulate("Timestamp", timeStamp());
                        mainObj.accumulate("message", "Successfully updated");
                    }
                } catch (SQLException ex) {
                    mainObj.accumulate("Status", "FAIL");
                    mainObj.accumulate("Timestamp", timeStamp());
                    mainObj.accumulate("message", "Error");
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
    
    // delete
    @GET()
    @Path("/deleteReg&{regionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDelete(@PathParam("regionId") int theRegionid) {
        reg = new Region();
        
                try {
                    String sql;
                    sql = "delete from regions where region_id=?";
                    // to show that this statement has parameter
                    stm = con.prepareStatement(sql);
                    stm.setInt(1, theRegionid);
                    int rs = stm.executeUpdate();
                    mainObj = new JSONObject();
                    if(rs==1)
                    {
                        mainObj.accumulate("Status", "OK");
                        mainObj.accumulate("Timestamp", timeStamp());
                        mainObj.accumulate("message", "Successfully deleted");
                    }
                } catch (SQLException ex) {
                    mainObj.accumulate("Status", "FAIL");
                    mainObj.accumulate("Timestamp", timeStamp());
                    mainObj.accumulate("message", "Error");
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
