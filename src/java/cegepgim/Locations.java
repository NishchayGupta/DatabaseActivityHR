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
 * @author manojagarwal
 */
@Path("locations")
public class Locations {

    @Context
    private UriInfo context;
    Connection con = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    private JSONObject mainObj;
    private JSONArray locationArr;
    int locationId;
    String streetAdd, postalCode, city, state, countryId;
    Locations loc;
    /**
     * Creates a new instance of Locations
     */
    public Locations() {
        try {        
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        } catch (SQLException ex) {
            System.out.println("In catch of constructor");
            Logger.getLogger(Locations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @GET()
    @Path("insert&{locationId}&{streetAddr}&{postalCode}&{city}&{state}&{countryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String insertLocation(@PathParam("locationId") int theLocatonId,
                                 @PathParam("streetAddr") String theStreetAddr,
                                 @PathParam("postalCode") String thePostalCode,
                                 @PathParam("city") String theCity,
                                 @PathParam("state") String theState,
                                 @PathParam("countryId") String theCountryId) {
                try {
                    String sql;
                    
                    sql = "insert into locations values(?, ?, ?, ?, ?, ?)";
                    stm = con.prepareStatement(sql);
                    stm.setInt(1, theLocatonId);
                    stm.setString(2, theStreetAddr);
                    stm.setString(3, thePostalCode);
                    stm.setString(4, theCity);
                    stm.setString(5, theState);
                    stm.setString(6, theCountryId);
                                       
                    int rs1 = stm.executeUpdate();
                    mainObj = new JSONObject();
                    mainObj.accumulate("Status", "OK");
                    mainObj.accumulate("Timestamp", timeStamp());
                    mainObj.accumulate("message", "Successfully inserted");
                } catch (SQLException ex) {
                    mainObj.accumulate("Status", "FAIL");
                    mainObj.accumulate("Timestamp", timeStamp());
                    mainObj.accumulate("message", "Error inserting");
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
                return mainObj.toString();
    }

    /**
     * Retrieves representation of an instance of cegepgim.Locations
     * @return an instance of java.lang.String
     */
    @GET()
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getList() {
        ResultSet rs = null;
        JSONObject listObj = new JSONObject();
        loc = new Locations();
        
                try {
                    String sql;
                    locationArr = new JSONArray();
                    sql = "select * from locations";
                    // to show that this statement has parameter
                    stm = con.prepareStatement(sql);
                    rs = stm.executeQuery();
                    while(rs.next())
                    {
                         mainObj = new JSONObject();
                         locationId = rs.getInt(1);
                         streetAdd = rs.getString(2);
                         postalCode = rs.getString(3);
                         city = rs.getString(4);
                         state = rs.getString(5);
                         countryId = rs.getString(6);
                         mainObj.accumulate("locationId", locationId);
                         mainObj.accumulate("streetAdd", streetAdd);
                         mainObj.accumulate("postalCode", postalCode);
                         mainObj.accumulate("city", city);
                         mainObj.accumulate("state", state);
                         mainObj.accumulate("countryId", countryId);
                         locationArr.add(mainObj);
                    }
                    listObj.accumulate("Status", "OK");
                    listObj.accumulate("Timestamp", timeStamp());
                    listObj.accumulate("locations", locationArr);
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
    
    public String timeStamp()
    {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String tm = "" + ts.getTime();
        return tm;
    }
}